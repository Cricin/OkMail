package smtp.internal;

import smtp.Channel;
import smtp.Interceptor;
import smtp.Mail;
import smtp.Response;
import smtp.ServerOptions;
import smtp.Session;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class RealInterceptorChain implements Interceptor.Chain {

  private List<Interceptor> interceptors;
  private Session session;

  //lazy settle
  private Channel channel;


  public RealInterceptorChain(Session session, List<Interceptor> interceptors) {
    this.interceptors = interceptors;
    this.session = session;
  }

  @Override
  public Response proceed(@Nonnull Mail mail) throws IOException {
    return null;
  }

  @Override
  public Mail mail() {
    return null;
  }

  @Override
  public Response response() {
    return null;
  }

  @Override
  public Session session() {
    return null;
  }

  public Channel channel() {
    return channel;
  }

  public ServerOptions serverOptions() {
    return null;
  }


}
