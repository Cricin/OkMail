package okmail.smtp;

import okmail.mail.Mail;

import java.io.IOException;

public interface Interceptor {

  void intercept(Chain chain) throws IOException;

  interface Chain {

    Response proceed(Mail mail) throws IOException;

    Mail mail();

    Channel channel();

    Session session();
  }

}
