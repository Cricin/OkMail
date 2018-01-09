package okmail.web.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/index")
@Controller
public class IndexController {

  @ResponseBody
  @GetMapping("/")
  public Object index() {
    return "static/main.html";
  }


}
