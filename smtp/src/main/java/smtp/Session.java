package smtp;

import java.io.IOException;

public interface Session {

  Mail mail();

  Response send() throws IOException;

  void cancel();

  void enqueue(Callback callback);

  boolean isSent();

  boolean isCancelled();

  Session clone();

  interface SessionFactory {
    Session newSession(Mail mail);
  }

}