package okmail.demo;

import smtp.Session;
import smtp.SmtpClient;
import smtp.auth.Authentication;
import smtp.mail.Mail;
import smtp.mail.Mailbox;

import javax.net.SocketFactory;
import java.io.IOException;

public class SmtpSendPlainMail {

  public static void main(String[] args) {
    SmtpClient client = new SmtpClient
            .Builder()
            .connectTimeout(20000)
            .writeTimeout(10000)
            .readTimeout(10000)
//            .dns(hostname -> Collections.singletonList(InetAddress.getByName("smtp.126.com")))
            .socketFactory(SocketFactory.getDefault())
            .build();

    Mail mail = new Mail.Builder().from(Mailbox.parse("cricin@126.com"))
            .addRecipient(Mailbox.parse("cricin@cricin.cn"))
        .authentication(Authentication.of("cricin@126.com", "abcdefg"))
            .subject("This Is A Title")
            .content("This Is A Content In Mail")
            .build();

    Session session = client.newSession(mail);

    try {
      session.send();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.print("Something wrong happened");
    }
  }

}
