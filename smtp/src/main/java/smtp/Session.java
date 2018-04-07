package smtp;

import smtp.auth.Authentication;
import smtp.mail.Mail;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;

public interface Session {

  Mail mail();

  SmtpClient client();

  void send() throws IOException;

  Session clone();

  interface SessionFactory {
    Session newSession(Mail mail, @Nullable InetAddress serverAddress, Authentication auth);
  }

}