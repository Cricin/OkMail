package okmail.smtp;

import okmail.mail.Mail;

import java.io.IOException;

public interface Session {

  Mail mail();

  void send() throws IOException;

  Session clone();

  interface SessionFactory {
    Session newSession(Mail mail);
  }

}