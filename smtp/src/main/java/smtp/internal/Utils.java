package smtp.internal;

import java.nio.charset.Charset;

/**
 * Junk drawer for OkMail library internally
 */
public final class Utils {

  public static final Charset UTF_8 = Charset.forName("UTF-8");
  public static final Charset ASCII = Charset.forName("ASCII");

  public static void main(String[] args) {
    byte[] b = {65, 66, 67, 68};
    System.out.println(new String(b, ASCII));
  }

}
