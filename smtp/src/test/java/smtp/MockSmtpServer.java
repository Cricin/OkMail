package smtp;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MockSmtpServer {


  public static void main(String[] args) throws IOException {

    ServerSocket socket = new ServerSocket(2500);

    final Socket accept = socket.accept();
    final BufferedSource source = Okio.buffer(Okio.source(accept));
    final BufferedSink sink = Okio.buffer(Okio.sink(accept));

    System.out.println();
    System.out.println();
    System.out.println();

    write(sink, "220 126.com Anti-spam GT for Coremail System (126com[20140526])\r\n");

    String read = read(source);

    if (!read.startsWith("EHLO")) {
      write(sink, "500 error\r\n");
      return;
    }

    write(sink, "250-mail\r\n" +
        "250-PIPELINING\r\n" +
        "250-AUTH LOGIN PLAIN \r\n" +
        "250-AUTH=LOGIN PLAIN\r\n" +
        "250-STARTTLS\r\n" +
        "250 8BITMIME\r\n");

    read = read(source);
    if (!read.startsWith("AUTH LOGIN")) {
      write(sink, "500 error\r\n");
      return;
    }


    write(sink, "334 dXNlcm5hbWU6\r\n");

    read(source);


    write(sink, "334 UGFzc3dvcmQ6\r\n");

    read(source);

    write(sink, "235 authentication success\r\n");


    read = read(source);

    if (!read.startsWith("MAIL")) {
      write(sink, "500 error\r\n");
    }

    write(sink, "250 ok\r\n");


    read = read(source);

    if (!read.startsWith("RCPT")) {
      write(sink, "500 error\r\n");
    }

    write(sink, "250 ok\r\n");

    read = read(source);

    if (!read.startsWith("DATA")) {
      write(sink, "500 error\r\n");
    }

    write(sink, "354 end with <CR><LF>.<CR><LF>\r\n");

    StringBuilder builder = new StringBuilder();
    while (isNotEnd(builder)) {
      builder.append(source.readUtf8Line()).append("\r\n");
    }
    System.out.println("c: " + builder);


    write(sink, "250 ok, message queued\r\n");


    read = read(source);
    if (!read.startsWith("QUIT")) {
      write(sink, "500 error\r\n");
    }

    write(sink, "bye\r\n");

    socket.close();

  }

  private static boolean isNotEnd(StringBuilder builder) {
    return builder.lastIndexOf("\r\n.\r\n") == -1;
  }


  public static void write(BufferedSink sink, String text) throws IOException {
    sink.writeUtf8(text).flush();
    System.out.print("s: " + text);
  }

  public static String read(BufferedSource source) throws IOException {
    String line = source.readUtf8Line();
    System.out.println("c " + line.replace("\r\n", ""));
    return line;
  }


}
