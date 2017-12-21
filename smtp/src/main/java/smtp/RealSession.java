package smtp;

import smtp.internal.RealInterceptorChain;
import smtp.internal.command.Command;
import smtp.internal.connection.ConnectInterceptor;
import smtp.internal.ValidateInterceptor;

import javax.annotation.Nonnull;
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
  public void enqueue(@Nonnull Callback callback) {
    synchronized (this) {
      if (sent) throw new IllegalStateException("Already Sent");
      sent = true;
    }
    AsyncSession as = new AsyncSession();
    client.executor.execute(as);//TODO
  }

  @Override
  public Response send() throws IOException {
    synchronized (this) {
      if (sent) throw new IllegalStateException("Already Sent");
      sent = true;
    }
    return getResponseWithInterceptor();
  }

  @Override
  public boolean isSent() {
    return sent;
  }

  @Override
  public void cancel() {

  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public Session clone() {
    return newRealSession(client, mail);
  }


  //todo
  final class AsyncSession implements Runnable {

    @Override
    public void run() {

    }
  }

  private Response getResponseWithInterceptor() throws IOException {
    List<Interceptor> interceptors = new LinkedList<>();
    interceptors.add(new ValidateInterceptor());
    interceptors.add(new ConnectInterceptor(client.mxDns, client.socketFactory));
    List<Interceptor> commands = Command.newCommandsForSession(this);
    interceptors.addAll(commands);
    Interceptor.Chain chain = new RealInterceptorChain(this, interceptors);
    return chain.proceed(mail);
  }
}