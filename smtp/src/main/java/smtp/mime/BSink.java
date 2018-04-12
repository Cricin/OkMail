package smtp.mime;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

import java.io.IOException;

public class BSink implements Sink {

  final Sink sink;
  final int maxLength;

  public BSink(Sink sink, int maxLengthPerLine) {
    this.sink = sink;
    this.maxLength = maxLengthPerLine;
  }

  @Override
  public void write(Buffer source, long byteCount) throws IOException {

  }

  @Override
  public void flush() throws IOException {
    sink.flush();
  }

  @Override
  public Timeout timeout() {
    return sink.timeout();
  }

  @Override
  public void close() throws IOException {
    sink.close();
  }
}
