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
    Utils.d("serverOptions msg: " + serverMsg);
    sink.writeUtf8("EHLO")
        .writeUtf8(Version.VERSION_TEXT)
        .write(Utils.CRLF)
        .flush();

    Utils.d("reading serverOptions metadata");
    RealServerOptions server = new RealServerOptions(readResponse(source));
    realChain.setServerOptions(server);
  }

  private String readResponse(BufferedSource source) throws IOException {
    StringBuilder builder = new StringBuilder();
    String temp;
    do {
      temp = source.readUtf8Line();
      builder.append(temp);
    } while (temp == null || temp.charAt(3) != ' ');
    return builder.toString();
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
  }


}
