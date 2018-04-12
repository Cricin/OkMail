package smtp.interceptor;

import smtp.Interceptor;
import smtp.SmtpClient;

import java.io.IOException;

public class StartTlsInterceptor implements Interceptor {
  @Override
  public void intercept(Chain chain) throws IOException {
    final SmtpClient client = chain.client();
    if (client.useStartUls() && chain.serverOptions().startTlsSupported()) {
      //todo establish tls connection here
    }
    chain.proceed(chain.mail());
  }
}
