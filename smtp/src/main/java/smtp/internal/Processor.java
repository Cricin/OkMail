package smtp.internal;

import smtp.Mail;
import smtp.Session;

import java.io.IOException;

public interface Processor {

  void process(Chain chain) throws IOException;

  interface Chain {

    Mail mail();

    Session session();

  }

  interface Interceptor {
    void preProcessor(Processor target, Chain chain);

    void postProcessor(Processor target, Chain chain);
  }


}
