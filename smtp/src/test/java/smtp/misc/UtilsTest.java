package smtp.misc;

import okio.BufferedSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class UtilsTest {


  @Test
  public void testReadAll() throws IOException {
    String response = "250 world1!\r\n250 world2\r\n250=world3\r\n";
    ByteArrayInputStream in = new ByteArrayInputStream(response.getBytes());
    BufferedSource source = Okio.buffer(Okio.source(in));
    Assert.assertEquals(response, Utils.readAll(source));

    response = "250 world1!\r\n250=world2\r\n250world3\r\n";
    in = new ByteArrayInputStream(response.getBytes());
    source = Okio.buffer(Okio.source(in));
    Assert.assertEquals("250 world1!\r\n250=world2\r\n", Utils.readAll(source));
  }

  @Test
  public void testReadEachLine() throws IOException {
    String response = "250 world1!\r\n250 world2\r\n250=world3\r\n";
    ByteArrayInputStream in = new ByteArrayInputStream(response.getBytes());
    BufferedSource source = Okio.buffer(Okio.source(in));
    Assert.assertEquals(Arrays.asList(response.split("\r\n")), Utils.readEachLine(source));

    response = "250 world1!\r\n250=world2\r\n250world3\r\n";
    in = new ByteArrayInputStream(response.getBytes());
    source = Okio.buffer(Okio.source(in));
    Assert.assertEquals(Arrays.asList("250 world1!", "250=world2"),
        Utils.readEachLine(source));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testCheckNotNegative() {
    Assert.assertEquals(99, Utils.checkNotNegative(99));
    Assert.assertEquals(-32, Utils.checkNotNegative(-32));
  }


  @Test
  public void testParseCode(){
    Assert.assertEquals(-1, Utils.parseCode(null));
    Assert.assertEquals(-1, Utils.parseCode("22"));
    Assert.assertEquals(221, Utils.parseCode("221"));

    int code = Utils.parseCode("221 GO AHEAD");
    Assert.assertEquals(221, code);

    Assert.assertEquals(-1, Utils.parseCode("not a number"));
  }


}
