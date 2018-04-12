package smtp.interceptor;

import okio.BufferedSink;
import okio.Sink;
import smtp.mail.Headers;
import smtp.mime.Encoding;
import smtp.misc.Utils;

import java.io.IOException;
import java.nio.charset.Charset;

public class SmtpHeader {
  private SmtpHeader() {
  }

  public static void writeHeader(BufferedSink sink,
                                 Headers headers,
                                 Encoding encoding,
                                 Charset charset,
                                 int maxLengthPerLine) throws IOException {
    for (int i = 0, size = headers.size(); i < size; i++) {
      String name = headers.name(i);
      String value = headers.value(i);
      if (shouldMimeConvertFor(name)) {
        value = mimeConvert(value, encoding, charset);
      } else {
        writeWithLength(sink, maxLengthPerLine, name + ": " + value);
      }
    }
  }

  private static boolean shouldMimeConvertFor(String name) {
    return "Subject".equalsIgnoreCase(name)
        || "From".equalsIgnoreCase(name)
        || "To".equalsIgnoreCase(name)
        || "Cc".equalsIgnoreCase(name)
        || "Bcc".equalsIgnoreCase(name);
  }

  public static String mimeConvert(String header, Encoding encoding, Charset charset) {
    String fieldBody = header.substring(header.indexOf(":")).trim();
    return null;
  }

  static void writeWithLength(
      BufferedSink sink,
      int maxLength,
      String header) throws IOException {

    int wroteCount = 0;
    int length = header.length();
    int remain = header.length();

    //first line, account in crlf, we can write maxLength-2 character
    maxLength -= 2;

    boolean firstLine = true;

    while (true) {
      sink.writeUtf8(header, wroteCount, wroteCount + Math.min(maxLength, remain));
      sink.write(Utils.CRLF);

      wroteCount += Math.min(maxLength, remain);
      remain = length - wroteCount;
      if (wroteCount == length) break;

      if (firstLine) {
        firstLine = false;
        maxLength -= 2;
      }
      //if we goes here, we should add lswp-char as padding, so add two SPACE here
      sink.writeByte(Utils.SP).writeByte(Utils.SP);
    }
  }

}
