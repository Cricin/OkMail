package smtp.interceptor;

import smtp.*;
import smtp.mail.Mail;
import smtp.mail.MultipartBody;
import smtp.mail.SmtpDate;
import smtp.mail.TextBody;
import smtp.mail.Encoding;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

import static smtp.mail.Encoding.AUTO_SELECT;
import static smtp.mail.Encoding.BASE64;
import static smtp.mail.Encoding.EIGHT_BIT;

public class BridgeInterceptor implements Interceptor {
  private final MailIdGenerator idGenerator;

  public BridgeInterceptor(MailIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {
    Mail mail = chain.mail();
    Mail.Builder builder = mail.newBuilder();

    //some mail server recognise mail as invalid or junk if a mail
    //dose not contains from header
    if (mail.headers().get("X-Mailer") == null) {
      builder.addHeader("X-Mailer", Version.versionText());
    }
    if (mail.headers().get("Message-Id") == null) {
      builder.addHeader("Message-Id", idGenerator.generate(chain.mail()));
    }
    if (mail.headers().get("Date") == null) {
      builder.addHeader("Date", SmtpDate.format(new Date()));
    }
    if (mail.headers().get("MIME-Version") == null) {
      builder.addHeader("MIME-Version", "1.0");
    }
    if (mail.headers().get("Content-Type") == null && mail.body() != null) {
      builder.addHeader("Content-Type", mail.body().contentType().toString());
    }
    Encoding encoding = mail.body().transferEncoding();
    if (encoding == AUTO_SELECT) {
      ServerOptions options = chain.serverOptions();
      if (options.eightBitMimeSupported()) encoding = BASE64;
      else encoding = chain.transferSpec().encoding();
    }
    builder.addHeader("Content-Transfer-Encoding", encoding.name());

    TransferSpec spec = chain.transferSpec()
        .newBuilder()
        .encoding(encoding)
        .build();
    RealInterceptorChain r = (RealInterceptorChain) chain;
    r.setTransferSpec(spec);
    chain.proceed(builder.build());
  }
}
