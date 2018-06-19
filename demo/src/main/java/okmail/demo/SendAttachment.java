package okmail.demo;

import smtp.Session;
import smtp.SmtpClient;
import smtp.auth.Authentication;
import smtp.mail.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("Duplicates")
public class SendAttachment {

  public static void main(String[] args) throws UnknownHostException {
    SmtpClient client = new SmtpClient();

    TextBody text = TextBody.plain(Assets.SPRING_SHORT);

    Attachment heart = Attachment.create("爱心.png",
        Assets.resolve("爱心.png"),
        MediaType.parse("image/png")
    );

    Attachment article = Attachment.create("荷塘月色.txt",
        Assets.resolve("荷塘月色.txt"),
        MediaType.parse("text/plain"));

    MultipartBody body = new MultipartBody
        .Builder()
        .addText(text)
        .addAttachment(heart)
        .addAttachment(article)
        .build();

    Mail mail = new Mail.Builder()
        .from(Configure.getFrom())
        .addRecipient(Configure.getTo())
        .auth(Configure.getAuth())
        .subject("朱自清《春》- 节选")
        .body(body)
        .build();

    Session session = client.newSession(mail,  Configure.getAddress());

    try {
      session.send();
      System.out.println("邮件发送成功！");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("邮件发送失败！");
    }
  }


}
