package okmail.web.security;

import okmail.web.Constant;
import okmail.web.util.functional.Lambdas;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SecureFilter extends HttpFilter {

  private static final SecureFilter INSTANCE = new SecureFilter();

  private long expirePeriod = Constant.EXPIRE_PERIOD;
  private final HashMap<String, AddressAuthInfo> clientAuth = new HashMap<>();
  protected HttpServletRequest request;
  protected HttpServletResponse response;


  public static SecureFilter instance() {
    return INSTANCE;
  }

  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
          throws IOException, ServletException {
    if (!Constant.AUTH_ENABLED) {
      chain.doFilter(req, res);
      return;
    }

    this.request = req;
    this.response = res;

    //如果是/auth开头的url，不拦截
    StringBuffer rawUrl = req.getRequestURL();
    URL url = new URL(rawUrl.toString());
    if (url.getPath().startsWith("/auth/")) {
      chain.doFilter(req, res);
      return;
    }

    //确认有cookie
    Cookie[] cookies = req.getCookies();
    Cookie expireCookie = Lambdas.test(cookies, cookie -> Constant.EXPIRE_COOKIE_NAME.equals(cookie.getName()));
    if (expireCookie == null) {
      navigateToAuth();
      return;
    }

    //确认已有保存的客户端登录信息
    String remoteAddress = req.getRemoteAddr();
    AddressAuthInfo info = clientAuth.getOrDefault(remoteAddress, AddressAuthInfo.EMPTY);
    if (info == AddressAuthInfo.EMPTY) {
      navigateToAuth();
      return;
    }

    //确认cookie和超时
    String expireCookieValue = expireCookie.getValue();
    final long now = System.currentTimeMillis();
    if (!info.cookieValue.equals(expireCookieValue) || info.lastGrant + expirePeriod < now) {
      navigateToAuth();
      return;
    }

    //认证通过
    synchronized (this) {
      info.lastGrant = System.currentTimeMillis();
    }

    chain.doFilter(req, res);
  }

  public void setAddressAuthenticated(String address, Cookie cookie) {
    synchronized (this) {
      clientAuth.put(address, new AddressAuthInfo(System.currentTimeMillis(), cookie.getValue()));
    }
  }

  public void setExpirePeriod(long expire, TimeUnit unit) {
    expirePeriod = unit.toMillis(expire);
  }

  protected void navigateToAuth() {
    //add 301 http status for http redirect with location header
    response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
    String localName = request.getLocalName();
    int port = request.getLocalPort();
    response.setHeader("Location", String.format("http://%s:%d/auth.html", localName, port));
  }

}
