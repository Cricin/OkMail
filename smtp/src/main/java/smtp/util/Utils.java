package smtp.util;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadFactory;

public final class Utils {

  public static final Charset ASCII = Charset.forName("ASCII");
  public static final Charset UTF8 = Charset.forName("UTF-8");
  public static final int SP = 20;
  public static final int CR = 13;
  public static final int LF = 10;
  public static final int DOT = 45;


  public static void closeQuietly(Closeable c) {
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

}
