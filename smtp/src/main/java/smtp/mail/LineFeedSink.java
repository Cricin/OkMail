package smtp.mail;

import okio.Buffer;
import okio.BufferedSink;
import okio.Sink;
import okio.Timeout;

import java.io.IOException;

class LineFeedSink implements Sink {
  final BufferedSink sink;
  final int lineLength;

  public LineFeedSink(BufferedSink sink, int lineLength) {
    this.sink = sink;
    this.lineLength = lineLength;
  }

  @Override
  public void write(Buffer source, long byteCount) throws IOException {
    long length = lineLength - 2;// count in \r\n
    long byteWrote = 0;
    while (byteWrote < byteCount) {
      long write = Math.min(length, byteCount - byteWrote);
      sink.write(source, write);
      sink.writeUtf8("\r\n");
      byteWrote += write;
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
