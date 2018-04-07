package smtp.internal;

import org.junit.Assert;
import org.junit.Test;
import smtp.misc.SystemDns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SystemMxDnsTest {

  private SystemDns dns = new SystemDns();

  @Test
  public void testLookupForCricinCn() throws UnknownHostException {
    List<InetAddress> list = dns.lookupByMxRecord("cricin.cn");//cricin.cn A record
    InetAddress actual = InetAddress.getByName("cricin.cn");//@cricin.cn MX record pointer to cricin.cn's A record
    Assert.assertNotNull(list);
    Assert.assertTrue(list.size() == 1);
    Assert.assertEquals(list.get(0), actual);
  }

  @Test
  public void testLookupForQQ() throws UnknownHostException {
    List<InetAddress> list = dns.lookupByMxRecord("qq.com");
    InetAddress actual = InetAddress.getByName("mx1.qq.com");//mx1.qq.com is a mx recorded serverOptions for qq
    boolean match = false;
    for (InetAddress expected : list) {
      match |= expected != null && expected.equals(actual);
    }
    if (!match) {
      throw new AssertionError("qq.com'mx record doesn't match smtp.qq.com");
    }
  }

}
