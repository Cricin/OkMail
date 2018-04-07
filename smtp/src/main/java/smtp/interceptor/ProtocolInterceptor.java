package smtp.interceptor;


import smtp.Interceptor;
import smtp.command.Command;

import java.io.IOException;
import java.util.List;

/**
 * this interceptor is for client exchange information with
 * server by smtp protocol
 */
public class ProtocolInterceptor implements Interceptor {
  public ProtocolInterceptor(List<Command> smtpCommand) {

  }


  @Override
  public void intercept(Chain chain) throws IOException {

  }
}
