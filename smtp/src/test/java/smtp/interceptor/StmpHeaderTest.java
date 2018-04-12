package smtp.interceptor;


import okio.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StmpHeaderTest {
  @Test
  public void testWriteWithLength() throws IOException {

    Buffer buffer = new Buffer();
    SmtpHeader.writeWithLength(buffer, 10, "abc: defghijklmnopqrstuvwxyz");

    System.out.println(buffer.readUtf8());
//    Assert.assertEquals("abc: def", buffer.readUtf8Line());
//    Assert.assertEquals("  ghijklmn", buffer.readUtf8Line());
//    Assert.assertEquals("  opqrstuv", buffer.readUtf8Line());
//    Assert.assertEquals("  wxyz", buffer.readUtf8Line());
  }


}
