package smtp.interceptor;

import smtp.Interceptor;
import smtp.ServerOptions;
import smtp.auth.AuthMethod;
import smtp.auth.Authentication;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.List;

public class AuthInterceptor implements Interceptor {
  private final Authentication auth;

  public AuthInterceptor(Authentication auth) {
    this.auth = auth;
  }

  @Override
  public void intercept(Chain chain) throws IOException {
    final ServerOptions serverOptions = chain.serverOptions();

    AuthMethod authMethod = chooseAuthMethod(serverOptions);
    if (authMethod == null) throw new IOException("AuthMethod all unavailable");
    Utils.d("selected auth method: " + authMethod);

    boolean succeed = authMethod.auth(auth, chain.channel());
    if (!succeed) throw new IOException("Authentication failed, auth=" + auth);
    Utils.d("authentication succeed!");
    chain.proceed(chain.mail());
  }

  protected AuthMethod chooseAuthMethod(ServerOptions options) {
    final List<AuthMethod> methods = options.authMethods();
    if (methods.contains(AuthMethod.AUTH)) {
      return AuthMethod.AUTH;
    }
    if (methods.contains(AuthMethod.PLAIN)) {
      return AuthMethod.PLAIN;
    }
    return methods.isEmpty() ? null : methods.get(0);
  }
}
