package smtp.mime;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

import java.io.IOException;

public class QSink implements Sink {

  final Sink sink;
  final int maxLength;

  public QSink(Sink sink, int maxLengthPerLine) {
    this.sink = sink;
    this.maxLength = maxLengthPerLine;
  }


  @Override
  public void write(Buffer source, long byteCount) throws IOException {
    byte b;
    for (int i = 0; i < byteCount; i++) {
      b = source.readByte();

    }
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
