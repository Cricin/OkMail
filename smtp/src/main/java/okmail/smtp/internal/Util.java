package okmail.smtp.internal;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * Junk drawer for OkMail library internally
 */
public final class Util {

  public static final byte CR = 13;
  public static final byte LF = 10;
  public static final byte SP = 32;
  public static final byte DASH = 45;
  public static final byte DOT = 46;

  public static final Charset UTF_8 = Charset.forName("UTF-8");
  public static final Charset ASCII = Charset.forName("ASCII");


  public static boolean equals(Object a, Object b) {
    if (a == null) return b == null;
    return a.equals(b);
  }

  public static int hashCode(Object o) {
    return o == null ? 0 : o.hashCode();
  }

  public static void closeQuietly(Closeable c) {
    if (c == null) return;
    try {
      c.close();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception ignore) {
    }
  }

  /**
   * convert an ascii encoded value to an int
   *
   * @param ascii the raw ascii value
   * @return the decoded int value
   */
  private static int asciiToInt(int ascii) {
    if (ascii < 48 || ascii > 57) {
      return -1;
    } else {
      return ascii - 48;
    }
  }

  public static int readCode(String line) {
    int value = asciiToInt(line.charAt(0)) * 100;
    value += asciiToInt(line.charAt(1)) * 10;
    return value + asciiToInt(line.charAt(2));
  }

  public static String readMessage(String rawResponse) {
    return rawResponse.substring(4);
  }
}
