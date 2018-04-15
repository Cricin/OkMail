package smtp.mail;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Timeout;
import smtp.misc.Base64;
import smtp.misc.Utils;

import java.io.IOException;

class BSink implements Sink {

  final BufferedSink sink;
  final int maxLength;

  public BSink(Sink sink, int lineLength) {
    this.sink = Okio.buffer(sink);
    this.maxLength = lineLength;
  }

  @Override
  public void write(Buffer source, long byteCount) throws IOException {
    final int length = (int) (maxLength * 3/(float)4 - 2);// \r\n used
    while (byteCount > 0) {
      byte[] array = source.readByteArray(Math.min(length, byteCount));
      byteCount -= Math.min(length, byteCount);
      byte[] encoded = Base64.encodeToBytes(array);
      sink.write(encoded).write(Utils.CRLF);
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
