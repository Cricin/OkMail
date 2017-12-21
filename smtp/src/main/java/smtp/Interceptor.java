package smtp;

import java.io.IOException;
import java.net.Socket;

public interface Interceptor {

  void intercept(Chain chain) throws IOException;

  interface Chain {

    Response proceed(Mail mail) throws IOException;

    Mail mail();

    Channel channel();

    Response response();

    Session session();
  }

}
