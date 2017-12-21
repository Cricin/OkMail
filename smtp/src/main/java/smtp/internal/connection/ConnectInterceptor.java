package smtp.internal.connection;

import smtp.Channel;
import smtp.Interceptor;
import smtp.Mail;
import smtp.MailException;
import smtp.Mailbox;
import smtp.MxDns;
import smtp.Response;
import smtp.internal.RealInterceptorChain;
import smtp.internal.Util;

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
  public Response intercept(@Nonnull Chain chain) throws MailException {
    RealInterceptorChain realChain = (RealInterceptorChain) chain;
    final Mail mail = chain.mail();
    final Mailbox from = mail.from();
    List<InetAddress> addresses;
    try {
      addresses = mxDns.lookup(from.host());
    } catch (UnknownHostException e) {
      throw new MailException("can not find any DNS mx record address with host: " + from.host());
    }
    Socket socket = findHealthySocket(addresses);
    if (socket == null) throw new MailException("can not connected to any mail server");
    realChain.setChannel(new Channel() {

      @Override
      @Nonnull
      public Socket socket() {
        return socket;
      }

      @Override
      @Nonnull
      public InputStream inputStream() throws IOException {
        return socket().getInputStream();
      }

      @Override
      @Nonnull
      public OutputStream outputStream() throws IOException {
        return socket().getOutputStream();
      }
    });
    Response out = chain.proceed(mail);
    //close any socket resource...
    Util.closeQuietly(socket);
    return out;
  }

  @Nullable
  private Socket findHealthySocket(List<InetAddress> mailboxHosts) {
    for (int i = 0; i < mailboxHosts.size(); i++) {
      try {
        return socketFactory.createSocket(mailboxHosts.get(i), 25);
      } catch (IOException ignore) {
      }
    }
    return null;
  }

}
