package smtp.interceptor;

import okio.Buffer;
import org.junit.Test;
import smtp.*;
import smtp.auth.Authentication;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.TextBody;

import java.io.IOException;

public class ProtocolInterceptorTest {
  @Test
  public void test() throws IOException {
    final SmtpClient client = new SmtpClient();


    BridgeInterceptor interceptor = new BridgeInterceptor(MailIdGenerator.DEFAULT);

    TextBody textBody = TextBody.plain("这是一个文本内容, 并且有超级超级长的一行遗憾并且有超级超级长的一行遗憾");
    final Mail mail = new Mail
        .Builder()
        .from(Mailbox.parse("<cricin@126.com>"))
        .addRecipient(Mailbox.parse("cricin@qq.com"))
        .subject("test message")
        .body(textBody)
        .auth(Authentication.of("cricin@126.com", "t150h0550s4431"))
        .build();


  }


}
