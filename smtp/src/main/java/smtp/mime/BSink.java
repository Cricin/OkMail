package smtp.mime;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

import java.io.IOException;

public class BSink implements Sink {




  @Override
  public void write(Buffer source, long byteCount) throws IOException {

  }

  @Override
  public void flush() throws IOException {

  }

  @Override
  public Timeout timeout() {
    return null;
  }

  @Override
  public void close() throws IOException {

  }
}
