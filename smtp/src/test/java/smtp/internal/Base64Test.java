package smtp.internal;

import org.junit.Assert;
import org.junit.Test;
import smtp.misc.Base64;

import java.nio.charset.Charset;

public class Base64Test {

  private Charset utf8 = Charset.forName("UTF-8");

  @Test
  public void testEncode() {
    String input = "base64 encode test";
    String encoded = Base64.encode(input.getBytes());
    Assert.assertEquals(encoded, "YmFzZTY0IGVuY29kZSB0ZXN0");
  }

  @Test
  public void testDecode() {
    String raw = "YmFzZTY0IGRlY29kZSB0ZXN0";
    byte[] bytes = Base64.decode(raw);
    String decoded = new String(bytes, utf8);
    Assert.assertEquals(decoded, "base64 decode test");
  }

}
