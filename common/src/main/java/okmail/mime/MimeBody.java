package okmail.mime;

import okio.BufferedSink;

import java.io.IOException;

public abstract class MimeBody {

  public abstract MediaType contentType();

  public long contentLength() throws IOException{
    return -1L;
  }

  public abstract void writeTo(BufferedSink sink) throws IOException;

}
