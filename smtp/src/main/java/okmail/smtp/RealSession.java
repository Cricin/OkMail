package okmail.smtp;

import okmail.smtp.internal.RealInterceptorChain;
import okmail.smtp.internal.ValidateInterceptor;
import okmail.smtp.internal.command.Command;
import okmail.smtp.internal.connection.ConnectInterceptor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

final class RealSession implements Session {

  final Client client;
  final Mail mail;

  // Guarded by this.
  boolean sent;

  private RealSession(Client client, Mail mail) {
    this.client = client;
    this.mail = mail;
  }

  static RealSession newRealSession(Client client, Mail mail) {
    return new RealSession(client, mail);
  }

  @Override
  public Mail mail() {
    return mail;
  }


  @Override
  public void send() throws IOException {
    synchronized (this) {
      if (sent) throw new IllegalStateException("Already Sent");
      sent = true;
    }
    sendWithInterceptor();
  }

  @Override
  public Session clone() {
    return newRealSession(client, mail);
  }


  private void sendWithInterceptor() throws IOException {
    List<Interceptor> interceptors = new LinkedList<>();
    interceptors.add(new ValidateInterceptor());
    interceptors.add(new ConnectInterceptor(client.mxDns, client.socketFactory));
    List<Interceptor> commands = Command.newCommandsForSession(this);
    interceptors.addAll(commands);
    Interceptor.Chain chain = new RealInterceptorChain(this, interceptors);
    chain.proceed(mail);
  }
}