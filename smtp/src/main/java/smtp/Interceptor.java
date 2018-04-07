package smtp;

import smtp.interceptor.ProtocolInterceptor;
import smtp.mail.Mail;

import java.io.IOException;

/**
 * @see smtp.interceptor.ValidateInterceptor
 * @see smtp.interceptor.ConnectInterceptor
 * @see smtp.interceptor.AuthInterceptor
 * @see ProtocolInterceptor
 */
public interface Interceptor {

  void intercept(Chain chain) throws IOException;

  interface Chain {

    void proceed(Mail mail) throws IOException;

    Mail mail();

    Channel channel();

    SmtpClient client();
  }

}
