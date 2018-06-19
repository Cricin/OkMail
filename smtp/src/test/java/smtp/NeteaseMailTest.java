package smtp;

import org.junit.Test;
import smtp.auth.Authentication;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.TextBody;

import java.io.IOException;
import java.net.InetAddress;

public class NeteaseMailTest {

  @Test
  public void testSend() {
    SmtpClient client = new SmtpClient();

    TextBody mailBody = TextBody.plain("这是一个文本内容,这是一个文本内容,这是一个文本内容,这是一个文本内容,");

    Mail mail = new Mail.Builder()
        .from(Mailbox.parse("炒面的126邮箱<cricin@126.com>"))
        .addRecipient(Mailbox.parse("炒面<cricin@qq.com>"))
        .subject("这是一个邮件的标题")
        .auth(Authentication.of("cricin@126.com", "t150h0550s4431"))
        .body(mailBody)
        .build();

    try {
      Session session = client.newSession(mail, InetAddress.getByName("smtp.126.com"));
      session.send();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
