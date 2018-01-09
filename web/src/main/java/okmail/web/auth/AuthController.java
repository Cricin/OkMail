package okmail.web.auth;

import okmail.web.security.SecureFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/auth")
@Controller
public class AuthController {

  public static final String COOKIE_NAME = "okmail-expire";

  @Autowired
  private SecureFilter secureFilter;

  @ResponseBody
  @PostMapping("/login")
  public Object auth(HttpServletRequest req, HttpServletResponse res) {
    return null;
  }
}
