package smtp.misc;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import smtp.interceptor.SmtpBody;
import smtp.interceptor.SmtpHeader;
import smtp.interceptor.TransferSpec;
import smtp.mail.Mail;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;

public final class Utils {

  public static final Charset ASCII = Charset.forName("ASCII");
  public static final Charset UTF8 = Charset.forName("UTF-8");
  public static final int SP = 20;
  public static final int CR = 13;
  public static final int LF = 10;
  public static final int DOT = 45;

  /** line end in smtp protocol */
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

  public static boolean isBlank(String text) {
    return text == null || text.length() == 0;
  }

  public static int asciiCharacterCount(String text) {
    int count = 0;
    char ch;
    for (int i = 0; i < text.length(); i++) {
      ch = text.charAt(i);
      if (ch >= 0 && ch <= 127) count++;
    }
    return count;
  }

  public static String encodeUtf8B(String text) {
    return "=?UTF-8?B?" + Base64.encode(text.getBytes(UTF8)) + "?=";
  }


  public static void dumpMail(Mail mail, TransferSpec spec, OutputStream out) throws IOException{
    final BufferedSink sink = Okio.buffer(Okio.sink(out));
    SmtpHeader.writeAllHeaders(sink, mail.headers(), spec);

    SmtpHeader.writeMailbox(sink, spec, "From", Collections.singletonList(mail.from()));
    SmtpHeader.writeMailbox(sink, spec,"To", mail.recipients());
    SmtpHeader.writeMailbox(sink, spec,"Cc", mail.cc());
    SmtpHeader.writeMailbox(sink, spec,"Bcc", mail.bcc());

    sink.write(Utils.CRLF);

    SmtpBody.writeBodies(sink, mail.body(), spec);

    sink.writeUtf8(".\r\n");
    sink.flush();
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
