package smtp;

import smtp.interceptor.AuthInterceptor;
import smtp.interceptor.ConfirmInterceptor;
import smtp.interceptor.ConnectInterceptor;
import smtp.interceptor.ProtocolInterceptor;
import smtp.interceptor.RealInterceptorChain;
import smtp.interceptor.StartTlsInterceptor;
import smtp.interceptor.BridgeInterceptor;
import smtp.mail.Mail;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

final class RealSession implements Session {

  final SmtpClient client;
  final Mail mail;
  @Nullable
  final InetAddress serverAddress;

  // Guarded by this.
  boolean sent;

  private RealSession(SmtpClient client, Mail mail, @Nullable InetAddress serverAddress) {
    this.client = client;
    this.mail = mail;
    this.serverAddress = serverAddress;
  }

  @Override
  public Mail mail() {
    return mail;
  }

  @Override
  public SmtpClient client() {
    return client;
  }

  @Override
  public void send() throws IOException {
    synchronized (this) {
      if (sent) throw new IllegalStateException("Already Sent");
      sent = true;
    }
    sendWithInterceptor();
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  public Session clone() {
    return newRealSession(client, mail, serverAddress);
  }


  private void sendWithInterceptor() throws IOException {
    List<Interceptor> interceptors = new LinkedList<>();
    interceptors.add(new ConnectInterceptor(serverAddress));
    interceptors.add(new ConfirmInterceptor());
    interceptors.add(new BridgeInterceptor(client.mailIdGenerator()));
    interceptors.add(new StartTlsInterceptor());
    interceptors.add(new AuthInterceptor(mail.auth()));
    interceptors.add(new ProtocolInterceptor());
    Interceptor.Chain chain = new RealInterceptorChain(this, interceptors);
    ((RealInterceptorChain) chain).setTransferSpec(client.transferSpec());
    chain.proceed(mail);
  }

  static RealSession newRealSession(SmtpClient client,
                                    Mail mail,
                                    @Nullable InetAddress serverAddress) {
    return new RealSession(client, mail, serverAddress);
  }
}