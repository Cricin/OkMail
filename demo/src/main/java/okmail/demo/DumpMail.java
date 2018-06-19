package okmail.demo;

import smtp.Interceptor;
import smtp.Session;
import smtp.SmtpClient;
import smtp.auth.Authentication;
import smtp.interceptor.BridgeInterceptor;
import smtp.interceptor.RealInterceptorChain;
import smtp.mail.Attachment;
import smtp.mail.Encoding;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.MediaType;
import smtp.mail.MultipartBody;
import smtp.mail.TextBody;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DumpMail {

  public static void main(String[] args) {
    System.out.println("\n\n\n\n");
    SmtpClient client = new SmtpClient.Builder().transferEncoding(Encoding.BASE64).build();
    TextBody text = TextBody.create(
        Assets.SPRING_SHORT,
        MediaType.parse("text/plain; charset=utf-8"));

    MultipartBody multipartBody = new MultipartBody.Builder()
        .setType(MultipartBody.MIXED)
        .addText(text)
        .build();

    Mail mail = new Mail.Builder()
        .from(Configure.getFrom())
        .recipients(Collections.singletonList(Configure.getTo()))
        .auth(Authentication.of(Configure.getAccount(), Configure.getToken()))
        .subject("朱自清《春》")
        .body(multipartBody)
        .build();

    Session session = client.newSession(mail, Configure.getAddress());
    List<Interceptor> list = Collections.singletonList(
        new BridgeInterceptor(client.mailIdGenerator())
    );
    RealInterceptorChain chain = new RealInterceptorChain(session, list);
    chain.setTransferSpec(client.transferSpec());
    try {
      chain.proceed(mail);

      Utils.dumpMail(chain.mail(), client.transferSpec(), System.out);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
