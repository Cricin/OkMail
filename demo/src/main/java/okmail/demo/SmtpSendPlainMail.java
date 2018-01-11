package okmail.demo;

import okmail.dns.SystemMxDns;
import okmail.mail.Mail;
import okmail.mail.Mailbox;
import okmail.smtp.Client;
import okmail.smtp.Session;

import javax.net.SocketFactory;
import java.io.IOException;

public class SmtpSendPlainMail {

  public static void main(String[] args) {
    Client client = new Client
            .Builder()
            .connectTimeout(20000)
            .writeTimeout(10000)
            .readTimeout(10000)
            .mxDns(new SystemMxDns())
            .socketFactory(SocketFactory.getDefault())
            .build();

    Mail mail = new Mail.Builder().from(Mailbox.parse("zysaaa@cricin.cn"))
            .addRecipient(Mailbox.parse("cricin@cricin.cn"))
            .password("zysaaa")
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
