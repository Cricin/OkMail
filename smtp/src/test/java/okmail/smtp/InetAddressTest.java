package okmail.smtp;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

  @Test
  public void testInetAddressImpl() throws UnknownHostException {
    InetAddress address = InetAddress.getByName("cricin.cn");
    Assert.assertEquals("47.96.175.46", (address.getHostAddress()));
  }

}
