package okmail.demo;

import smtp.Session;
import smtp.SmtpClient;
import smtp.auth.Authentication;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.TextBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("Duplicates")
public class SendPlainText {

  public static void main(String[] args) throws UnknownHostException {
    SmtpClient client = new SmtpClient();
    TextBody body = TextBody.plain(Assets.SPRING_SHORT);
    Mail mail = new Mail.Builder()
        .from(Configure.getFrom())
        .addRecipient(Configure.getTo())
        .auth(Configure.getAuth())
        .subject("朱自清《春》- 节选")
        .body(body)
        .build();
    Session session = client.newSession(mail, Configure.getAddress());
    try {
      session.send();
      System.out.println("邮件发送成功！");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("邮件发送失败！");
    }
  }

}
