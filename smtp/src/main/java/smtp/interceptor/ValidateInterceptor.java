package smtp.interceptor;

import smtp.Interceptor;
import smtp.MailIdGenerator;
import smtp.Version;
import smtp.mail.Mail;
import smtp.mail.SmtpDate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

public class ValidateInterceptor implements Interceptor {
  private final MailIdGenerator idGenerator;

  public ValidateInterceptor(MailIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {
    Mail mail = chain.mail();
    assertNotNull(mail.from(), "from == null");
    assertNotNull(mail.headers().get("Subject"), "subject == null");
    Mail.Builder builder = mail.newBuilder();

    if (mail.headers().get("X-Mailer") == null) {
      builder.addHeader("X-Mailer", Version.VERSION_TEXT);
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


    chain.proceed(builder.build());
  }

  private void assertNotNull(Object o, String msg) throws IOException {
    if (o == null) throw new IOException(msg);
  }
}
