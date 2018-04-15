package smtp;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Test;
import smtp.auth.Authentication;
import smtp.mail.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SmtpClientTest {

  @Test
  public void testSend() throws IOException {
    TextBody textBody = TextBody.plain("这是一个文本内容, 并且有超级超级长的一行遗憾");

    final File file = new File("/Users/Cricin/Downloads/中文.jpg");

    Attachment attachment = Attachment.create("文件.jpg", Okio.source(file), MediaType.parse
        ("image/jpeg"));

    MultipartBody body = new MultipartBody.Builder()
        .addText(textBody)
//        .addAttachment(attachment)
        .build();

    Mail mail = new Mail
        .Builder()
        .from(Mailbox.parse("炒面君<cricin@cricin.cn>"))
        .addRecipient(Mailbox.parse("炒面<test@cricin.cn>"))
        .subject("炒面！")
        .body(body)
        .auth(Authentication.of("cricin", "illusion"))
        .build();
    SmtpClient client = new SmtpClient.Builder().defaultPort(2500)
        .build();

//    Session session = client.newSession(mail, InetAddress.getByName("crica.iok.la"));

    Session session = client.newSession(mail, InetAddress.getLocalHost());

    try {
      session.send();
      System.out.println("send finished");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
