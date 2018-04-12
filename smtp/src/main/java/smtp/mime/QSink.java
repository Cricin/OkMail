package smtp.mime;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Timeout;

import java.io.IOException;

public class QSink implements Sink {

  final BufferedSink sink;
  final int maxLength;

  public QSink(Sink sink, int maxLengthPerLine) {
    this.sink = Okio.buffer(sink);
    this.maxLength = maxLengthPerLine;
  }


  @Override
  public void write(Buffer source, long byteCount) throws IOException {
    final int maxIndex = maxLength - 3/*\r\n*/;
    int index = 0;
    for (int i = 0; i < byteCount; i++) {
      byte b = source.readByte();
      sink.writeByte(61/*ascii '='*/);
      if (b == '=') {
        sink.writeUtf8("3D");//treat it specially
        index = writeWithLength(index, "=3D");
      } else if (b == ' ' || b == 9) {//space and tab

      }

      else if (b >= 33 && b <= 126) sink.writeByte(b); //printable ascii characters

    }

  }

  public int writeWithLength(int index, String encoded) {
    return 0;


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
