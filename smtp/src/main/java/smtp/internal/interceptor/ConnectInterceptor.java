package smtp.internal.interceptor;

import smtp.Interceptor;
import smtp.Mail;
import smtp.Mailbox;
import smtp.MxDns;
import smtp.internal.RealInterceptorChain;

import javax.annotation.Nonnull;
import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
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
    List<InetAddress> lookup = mxDns.lookup(from.host());


  }
}
