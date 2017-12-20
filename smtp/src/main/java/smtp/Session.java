package smtp;

public interface Session {

  boolean write();

  interface SessionFactory {
    Session newSession(Mail mail);
  }

}