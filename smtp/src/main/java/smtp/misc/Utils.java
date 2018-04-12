package smtp.misc;

import okio.Buffer;
import okio.BufferedSource;
import smtp.mail.TextBody;
import smtp.mime.Encoding;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public final class Utils {

  public static final Charset ASCII = Charset.forName("ASCII");
  public static final Charset UTF8 = Charset.forName("UTF-8");
  public static final int SP = 20;
  public static final int CR = 13;
  public static final int LF = 10;
  public static final int DOT = 45;

  /**line end in smtp protocol*/
  public static final byte[] CRLF = {'\r', '\n'};


  public static void closeQuietly(Closeable c) {
    if (c == null) return;
    try {
      c.close();
    } catch (IOException ignored) {
    }
  }


  public static long checkNotNegative(long n) {
    if (n < 0) throw new IllegalArgumentException(n + " < 0");
    return n;
  }


  public static ThreadFactory makeThreadFactory(final String name, final boolean daemon) {
    return new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, name);
        thread.setDaemon(daemon);
        return thread;
      }
    };
  }

  public static List<String> readEachLine(BufferedSource source) throws IOException {
    List<String> result = new ArrayList<>();
    String line;
    do {
      line = source.readUtf8Line();
      result.add(line);
    } while (line != null && line.charAt(3) != '=');
    return result;
  }

  public static String readAll(BufferedSource source) throws IOException {
    StringBuilder builder = new StringBuilder();
    String line;
    do {
      line = source.readUtf8Line();
      builder.append(line);
      builder.append("\r\n");
    } while (line != null && line.charAt(3) != '=');
    return builder.toString();
  }

  public static int parseCode(String response) {
    if (response == null) return -1;
    if (response.length() < 3) return -1;
    try {
      return Integer.parseInt(response.substring(0, 3));
    } catch (NumberFormatException nfe) {
      return -1;
    }
  }


  /*   log  */

  public static void i(String msg) {
    logInternal("INFO", msg);
  }

  public static void d(String msg) {
    logInternal("DEBUG", msg);
  }

  public static void e(String msg) {
    logInternal("ERROR", msg);
  }


  private static void logInternal(String tag, String msg) {
    System.out.print(tag);
    System.out.print(" ------> ");
    System.out.println(msg);
  }


}
