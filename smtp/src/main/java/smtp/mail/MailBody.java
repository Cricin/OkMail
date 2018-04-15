package smtp.mail;

import okio.BufferedSink;
import smtp.interceptor.TransferSpec;

import java.io.IOException;

public abstract class MailBody {

  public abstract MediaType contentType();

  public abstract void writeBody(BufferedSink sink, TransferSpec spec) throws IOException;

  public Encoding transferEncoding() {
    return Encoding.AUTO_SELECT;
  }

  protected Encoding chooseEncoding(TransferSpec spec) {
    Encoding encoding = transferEncoding();
    if (encoding == Encoding.AUTO_SELECT) {
      encoding = spec.encoding();
    }
    return encoding;
  }

}