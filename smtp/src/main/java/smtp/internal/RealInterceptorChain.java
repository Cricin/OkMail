package smtp.internal;

import smtp.Channel;
import smtp.Interceptor;
import smtp.Mail;
import smtp.MailException;
import smtp.Response;
import smtp.Session;

import javax.annotation.Nonnull;
import java.util.List;

public class RealInterceptorChain implements Interceptor.Chain {

  private List<Interceptor> interceptors;
  private Session session;
  private int index;

  //lazy settle
  private Channel channel;

  private Mail mail;

  public RealInterceptorChain(Session session, List<Interceptor> interceptors) {
    this.interceptors = interceptors;
    this.session = session;
    index = 0;
  }

  @Override
  public Response proceed(@Nonnull Mail mail) throws MailException {
    this.mail = mail;
    Response response = null;
    if (index < interceptors.size()) {
      final Interceptor i = interceptors.get(index);
      response = i.intercept(this);
      index++;
    }
    return response;
  }

  @Override
  @Nonnull
  public Mail mail() {
    return mail;
  }

  @Override
  @Nonnull
  public Session session() {
    return session;
  }

  public Channel channel() {
    return channel;
  }

  //this should only be called by ConnectInterceptor
  //before any Command
  public void setChannel(Channel channel) {
    this.channel = channel;
  }


}
