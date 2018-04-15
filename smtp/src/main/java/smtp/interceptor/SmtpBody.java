package smtp.interceptor;

import okio.BufferedSink;
import smtp.mail.Encoding;
import smtp.mail.MailBody;
import smtp.mail.MultipartBody;

import java.io.IOException;

import static smtp.mail.Encoding.AUTO_SELECT;

public class SmtpBody {
  private SmtpBody() {
  }

  public static void writeBodies(BufferedSink sink,
                                 MailBody body,
                                 TransferSpec spec) throws IOException {
    if (body instanceof MultipartBody) {
      sink.writeUtf8("This is a multi-part message in MIME format.\r\n\r\n");
    }

    body.writeBody(sink, spec);

//    Encoding encoding = body.transferEncoding();
//    if (encoding == AUTO_SELECT) {
//      encoding = spec.encoding();
//    }
//    final BufferedSink delegate = encoding.from(sink, spec.lengthLimit());
//    body.writeBody(delegate);
//    delegate.flush();
  }
}
