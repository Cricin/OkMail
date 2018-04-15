package smtp;

import smtp.interceptor.BridgeInterceptor;
import smtp.interceptor.ProtocolInterceptor;
import smtp.interceptor.TransferSpec;
import smtp.mail.Mail;

import java.io.IOException;

/**
 * @see BridgeInterceptor
 * @see smtp.interceptor.ConnectInterceptor
 * @see smtp.interceptor.AuthInterceptor
 * @see ProtocolInterceptor
 */
public interface Interceptor {

  void intercept(Chain chain) throws IOException;

  interface Chain {

    void proceed(Mail mail) throws IOException;

    Mail mail();

    SmtpClient client();

    Channel channel();

    ServerOptions serverOptions();

    TransferSpec transferSpec();
  }

}
