package smtp;

import smtp.auth.Authentication;
import smtp.command.Command;
import smtp.interceptor.*;
import smtp.mail.Mail;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

final class RealSession implements Session {

  final SmtpClient client;
  final Mail mail;
  final Authentication auth;
  @Nullable
  final InetAddress serverAddress;

  // Guarded by this.
  boolean sent;

  private RealSession(SmtpClient client, Mail mail, @Nullable InetAddress serverAddress,
                      Authentication auth) {
    this.client = client;
    this.mail = mail;
    this.serverAddress = serverAddress;
    this.auth = auth;
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
    return newRealSession(client, mail, serverAddress, auth);
  }


  private void sendWithInterceptor() throws IOException {
    List<Interceptor> interceptors = new LinkedList<>();
    interceptors.add(new ValidateInterceptor(client.mailIdGenerator()));
    interceptors.add(new ConnectInterceptor(serverAddress));
    interceptors.add(new ConfirmInterceptor());
    interceptors.add(new StartTlsInterceptor());
    interceptors.add(new AuthInterceptor(auth));
    interceptors.add(new ProtocolInterceptor(Command.newCommandsForSession(this)));
    Interceptor.Chain chain = new RealInterceptorChain(this, interceptors);
    chain.proceed(mail);
  }

  static RealSession newRealSession(SmtpClient client,
                                    Mail mail,
                                    @Nullable InetAddress serverAddress,
                                    Authentication auth) {
    return new RealSession(client, mail, serverAddress, auth);
  }
}