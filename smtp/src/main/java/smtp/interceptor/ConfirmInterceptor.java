package smtp.interceptor;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Interceptor;
import smtp.ServerOptions;
import smtp.Version;
import smtp.auth.AuthMethod;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfirmInterceptor implements Interceptor {
  @Override
  public void intercept(Chain chain) throws IOException {
    final BufferedSink sink = chain.channel().sink();
    final BufferedSource source = chain.channel().source();
    final RealInterceptorChain realChain = (RealInterceptorChain) chain;

    final String serverMsg = source.readUtf8Line();
    Utils.d("server greet msg: " + serverMsg);
    sink.writeUtf8("EHLO")
        .writeUtf8(" ")
        .writeUtf8(Version.versionText())
        .write(Utils.CRLF)
        .flush();

    Utils.d("reading server options metadata");
    RealServerOptions serverOptions = new RealServerOptions(Response.readAll(source));
    Utils.d("options resolved:");
    Utils.d(serverOptions.toString());
    realChain.setServerOptions(serverOptions);
    chain.proceed(chain.mail());
  }

  static class RealServerOptions implements ServerOptions {
    private final boolean eightBitMime;
    private final boolean startTls;
    private final boolean pipeLining;
    private final List<AuthMethod> authMethods;

    RealServerOptions(String response) {
      eightBitMime = response.contains("8BITMIME");
      startTls = response.contains("STARTTLS");
      pipeLining = response.contains("PIPELINING");
      List<AuthMethod> methods = new ArrayList<>();
      if (response.contains("LOGIN PLAIN")) {
        methods.add(AuthMethod.PLAIN);
      }
      if (response.contains("AUTH=LOGIN")) {
        methods.add(AuthMethod.AUTH);
      }
      authMethods = Collections.unmodifiableList(methods);
    }

    @Override
    public boolean eightBitMimeSupported() {
      return eightBitMime;
    }

    @Override
    public boolean startTlsSupported() {
      return startTls;
    }

    @Override
    public boolean pipeLiningSupported() {
      return pipeLining;
    }

    @Override
    public List<AuthMethod> authMethods() {
      return authMethods;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("8BIT: ").append(eightBitMime).append(" ");
      builder.append("STARTTLS: ").append(startTls).append(" ");
      builder.append("PIPELINING: ").append(pipeLining);
      return builder.toString();
    }
  }


}
