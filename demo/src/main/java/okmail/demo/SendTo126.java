package okmail.demo;

import smtp.Session;
import smtp.SmtpClient;
import smtp.auth.Authentication;
import smtp.mail.Attachment;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.MediaType;
import smtp.mail.MultipartBody;
import smtp.mail.TextBody;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

@SuppressWarnings("Duplicates")
public class SendTo126 {

  public static void main(String[] args) throws UnknownHostException {
    SmtpClient client = new SmtpClient();

    TextBody text = TextBody.create(Assets.SPRING_SHORT, MediaType.parse("text/plain; " +
        "charset=utf-8"));

    Attachment attachment = Attachment.create("爱心.png",
        Assets.resolve("爱心.png"),
        MediaType.parse("image/png"));

    MultipartBody multipartBody = new MultipartBody.Builder()
        .setType(MultipartBody.MIXED)
        .addAttachment(attachment)
        .addText(text)
        .build();

    Mail mail = new Mail.Builder()
        .from(Mailbox.parse("炒面<cricin@cricin.cn>"))
        .recipients(Collections.singletonList(Mailbox.parse("炒面126邮箱<cricin@126.com>")))
        .auth(Authentication.of("cricin", "illusion"))
        .subject("朱自清《春》")
        .body(multipartBody)
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
