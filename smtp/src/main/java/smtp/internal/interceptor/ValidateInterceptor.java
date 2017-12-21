package smtp.internal.interceptor;

import smtp.Interceptor;
import smtp.Mail;
import smtp.internal.SmtpDate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

public class ValidateInterceptor implements Interceptor {
  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {
    Mail mail = chain.mail();
    assertNotNull(mail.from(), "from == null");
    assertNotNull(mail.to(), "to == null");
    assertNotNull(mail.subject(), "subject == null");
    assertNotNull(mail.content(), "content == null");
    Mail validated = mail.newBuilder()
            .replaceHeader("Date", SmtpDate.format(new Date()))
            .build();
    chain.proceed(validated);
  }

  private void assertNotNull(Object o, String msg) throws IOException {
    if (o == null) throw new IOException(msg);
  }


}
