package smtp;

public interface Interceptor {

  Response intercept(Chain chain) throws MailException;

  interface Chain {

    Response proceed(Mail mail) throws MailException;

    Mail mail();

    Channel channel();

    Session session();
  }

}
