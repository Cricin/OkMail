package smtp;

import org.junit.Test;
import smtp.auth.Authentication;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.MediaType;
import smtp.mail.TextBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SmtpClientTest {

  @Test
  public void testSend() throws UnknownHostException {
    TextBody textBody = TextBody.of("这是一个文本内容", MediaType.parse("text/plain"));

    Mail mail = new Mail
        .Builder()
        .from(Mailbox.parse("<cricin@cricin.cn>"))
        .addRecipient(Mailbox.parse("cricin@126.com"))
        .subject("cricin")
        .body(textBody)
        .auth(Authentication.of("cricin", "illusion"))
        .build();
    SmtpClient client = new SmtpClient();

    Session session = client.newSession(mail, InetAddress.getByName("1.1.1.5"));
    try {
      session.send();
    } catch (IOException e) {
      System.out.println("send failed");
    }
  }


}
