package smtp.mail;

import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class LineFeedSinkTest {

  @Test
  public void testWrite() throws IOException {
    Buffer buffer = new Buffer();
    LineFeedSink sink = new LineFeedSink(buffer, 4);
    final BufferedSink sink1 = Okio.buffer(sink);
    sink1.writeUtf8("第三方迪士尼佛法第三方斯蒂芬电风扇");
    sink1.flush();

    long index;
    ByteString string = ByteString.of((byte) '\r', (byte) '\n');
    Buffer buffer1 = new Buffer();
    while ((index = buffer.indexOf(string)) != -1L) {
      buffer.readFully(buffer1, index);
      buffer.skip(2);
    }
    Assert.assertEquals("第三方迪士尼佛法第三方斯蒂芬电风扇",
        new String(buffer1.readByteArray(), "UTF-8"));

  }

  @Test
  public void testWriteTwo() throws IOException {
    Random random = new Random();
    byte[] arr = new byte[random.nextInt(30)];
    random.nextBytes(arr);

    Buffer buffer = new Buffer();
    final BufferedSink sink = Okio.buffer(new LineFeedSink(buffer, 10));

    sink.write(arr);
    sink.flush();

    Buffer buffer1 = new Buffer();
    long index;
    ByteString string = ByteString.of((byte) '\r', (byte) '\n');
    while ((index = buffer.indexOf(string)) != -1L) {
      buffer.readFully(buffer1, index);
      buffer.skip(2);
    }
    Assert.assertArrayEquals(arr, buffer1.readByteArray());
  }
}
