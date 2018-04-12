package smtp.mime;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import org.junit.Test;
import smtp.misc.Base64;

import java.io.IOException;

public class BSinkTest {

  @Test
  public void testBSink() throws IOException {

    Buffer buffer = new Buffer();

    BSink bSink = new BSink(buffer, 10);

    final BufferedSink sink = Okio.buffer(bSink);


    sink.writeUtf8("hello world! cricin").flush();

    final String utf8 = buffer.readUtf8();
    System.out.println(utf8);

    final byte[] bytes = Base64.decode(utf8.replace("\r\n", ""));
    System.out.println(bytes);
    System.out.println(new String(bytes));
  }

}
