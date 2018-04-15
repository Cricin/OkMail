package smtp.interceptor;

import okio.BufferedSink;
import smtp.mail.Headers;
import smtp.mail.Encoding;
import smtp.mail.Mailbox;
import smtp.misc.Base64;
import smtp.misc.QuotedP;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.List;

import static smtp.mail.Encoding.BASE64;

public class SmtpHeader {
  private SmtpHeader() {
  }

  static void writeAllHeaders(BufferedSink sink,
                              Headers headers,
                              TransferSpec spec) throws IOException {
    for (int i = 0, size = headers.size(); i < size; i++) {
      String name = headers.name(i);
      String value = headers.value(i);
      if ("Subject".equalsIgnoreCase(name)) {
        writeSubject(sink, spec, value);
      } else {
        writeNormalHeader(sink, spec.lengthLimit(), name + ": " + value);
      }
    }
  }

  static void writeSubject(BufferedSink sink,
                           TransferSpec spec,
                           String value) throws IOException {
    sink.writeUtf8("Subject: ");
    if ((Utils.asciiCharacterCount(value) == value.length())) {
      sink.writeUtf8(value).write(Utils.CRLF);
    } else {
      sink.writeUtf8("=?")
          .writeUtf8(spec.charset().name())
          .writeUtf8("?")
          .writeUtf8(spec.encoding() == BASE64 ? "B" : "Q")
          .writeUtf8("?");

      byte[] bytes = value.getBytes(spec.charset());
      String encoded;
      if (spec.encoding() == BASE64) {
        encoded = Base64.encode(bytes);
      } else {
        encoded = QuotedP.encode(bytes);
      }
      sink.writeUtf8(encoded)
          .writeUtf8("?=")
          .write(Utils.CRLF);
    }
  }

  static void writeNormalHeader(
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

  static void writeMailbox(BufferedSink sink,
                           TransferSpec spec,
                           String name,
                           List<Mailbox> mailboxes) throws IOException {
    //if no mailbox in list, just skipped
    if (mailboxes.isEmpty()) return;
    sink.writeUtf8(name).writeUtf8(": ");
    for (int i = 0; i < mailboxes.size(); i++) {
      Mailbox mailbox = mailboxes.get(i);
      String displayName = mailbox.displayName();
      //if is pure ascii, transfer directly
      if (Utils.asciiCharacterCount(displayName) == displayName.length()) {

        sink.writeUtf8("\"")
            .writeUtf8(displayName)
            .writeUtf8("\"");

      } else {
        // if not pure ascii, encode needed
        sink.writeUtf8("=?")
            .writeUtf8(spec.charset().name())
            .writeUtf8("?")
            .writeUtf8(spec.encoding() == BASE64 ? "B" : "Q")
            .writeUtf8("?");

        byte[] bytes = displayName.getBytes(spec.charset());
        String encoded;
        if (spec.encoding() == BASE64) {
          encoded = Base64.encode(bytes);
        } else {
          encoded = QuotedP.encode(bytes);
        }
        sink.writeUtf8(encoded)
            .writeUtf8("?=");
      }
      sink.writeUtf8(" ")
          .writeUtf8(mailbox.canonicalAddress());

      //if we come to last mailbox, "," is not needed
      if (i != mailboxes.size() - 1) {
        sink.writeUtf8(",");
      }

      sink.write(Utils.CRLF);
    }
  }


}
