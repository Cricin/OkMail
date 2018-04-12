package smtp.mail;

import okio.BufferedSink;

import java.io.IOException;

/** a mail attachment*/
public class Attachment extends MailBody {
  @Override
  public MediaType contentType() {
    return null;
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException {

  }
}
