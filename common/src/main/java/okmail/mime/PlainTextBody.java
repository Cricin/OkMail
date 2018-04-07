package okmail.mime;

import okio.BufferedSink;

import java.io.IOException;

public class PlainTextBody extends MimeBody {
  @Override
  public MediaType contentType() {
    return MediaType.TEXT_UTF8;
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException {

  }
}
