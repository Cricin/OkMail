package smtp.interceptor;

import smtp.*;
import smtp.mail.Mail;
import smtp.mail.MultipartBody;
import smtp.mail.SmtpDate;
import smtp.mail.TextBody;
import smtp.mail.Encoding;
import smtp.misc.Base64;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

import static smtp.mail.Encoding.*;

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

    TransferSpec spec = chain.client().transferSpec();
    Encoding theEncoding = spec.encoding();
    if (theEncoding == AUTO_SELECT) {
      ServerOptions options = chain.serverOptions();
      if (options.eightBitMimeSupported()) theEncoding = EIGHT_BIT;
      else {
        theEncoding = mail.body().transferEncoding();
      }
    }
    spec = spec.newBuilder().encoding(theEncoding).build();

    builder.addHeader("Content-Transfer-Encoding", theEncoding.encodingName());

    RealInterceptorChain r = (RealInterceptorChain) chain;
    r.setTransferSpec(spec);
    chain.proceed(builder.build());
  }
}
