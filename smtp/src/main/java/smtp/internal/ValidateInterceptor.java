package smtp.internal;

import smtp.Interceptor;
import smtp.Mail;
import smtp.MailException;
import smtp.Response;
import smtp.internal.SmtpDate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

public class ValidateInterceptor implements Interceptor {
  @Override
  public Response intercept(@Nonnull Chain chain) throws MailException {
    Mail mail = chain.mail();
    assertNotNull(mail.from(), "from == null");
    assertNotNull(mail.to(), "to == null");
    assertNotNull(mail.subject(), "subject == null");
    assertNotNull(mail.content(), "content == null");
    Mail validated = mail.newBuilder()
            .replaceHeader("Date", SmtpDate.format(new Date()))
            .build();
    //todo add any other headers here
    return chain.proceed(validated);
  }

  private void assertNotNull(Object o, String msg) throws MailException {
    if (o == null) throw new MailException(msg);
  }


}
