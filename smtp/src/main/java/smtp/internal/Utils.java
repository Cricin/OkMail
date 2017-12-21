package smtp.internal;

import java.nio.charset.Charset;

/**
 * Junk drawer for OkMail library internally
 */
public final class Utils {

  public static final Charset UTF_8 = Charset.forName("UTF-8");
  public static final Charset ASCII = Charset.forName("ASCII");

  public static boolean equals(Object a, Object b) {
    if (a == null) return b == null;
    return a.equals(b);
  }

  public static int hashCode(Object o) {
    return o == null ? 0 : o.hashCode();
  }



}
