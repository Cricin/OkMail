package okmail.smtp.internal.connection;

import okio.Okio;
import okio.Sink;
import okio.Source;
import okmail.smtp.*;
import okmail.smtp.internal.RealInterceptorChain;
import okmail.smtp.internal.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ConnectInterceptor implements Interceptor {

  private MxDns mxDns;
  private SocketFactory socketFactory;

  public ConnectInterceptor(MxDns mxDns, SocketFactory socketFactory) {
    this.mxDns = mxDns;
    this.socketFactory = socketFactory;
  }

  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {
    RealInterceptorChain realChain = (RealInterceptorChain) chain;
    final Mail mail = chain.mail();
    final Mailbox from = mail.from();
    List<InetAddress> addresses;
    try {
      addresses = mxDns.lookup(from.host());
    } catch (UnknownHostException e) {
      throw new IOException("can not find any DNS mx record address with host: " + from.host());
    }
    Socket socket = findHealthySocket(addresses);
    if (socket == null) throw new IOException("can not connected to any mail server");
    InputStream in;
    OutputStream out;
    try {
      in = socket.getInputStream();
      out = socket.getOutputStream();
    } catch (java.io.IOException e) {
      throw new IOException("error opening socket stream");
    }
    realChain.setChannel(new Channel() {

      private Sink sink;
      private Source source;

      @Override
      @Nonnull
      public Socket socket() {
        return socket;
      }

      @Nonnull
      @Override
      public Sink sink() throws IOException {
        if (sink == null) {
          sink = Okio.buffer(Okio.sink(socket));
        }
        return sink;
      }

      @Nonnull
      @Override
      public Source source() throws IOException {
        if (source == null) {
          source = Okio.buffer(Okio.source(socket));
        }
        return source;
      }
    });
    chain.proceed(mail);
    //close any socket resource...
    Util.closeQuietly(socket);
  }

  @Nullable
  private Socket findHealthySocket(List<InetAddress> mailboxHosts) {
    for (int i = 0; i < mailboxHosts.size(); i++) {
      try {
        return socketFactory.createSocket(mailboxHosts.get(i), 25);
      } catch (java.io.IOException ignore) {
      }
    }
    return null;
  }

}
