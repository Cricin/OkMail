package okmail.smtp.internal;

import okmail.dns.SystemMxDns;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SystemMxDnsTest {

  private SystemMxDns dns= new SystemMxDns();

  @Test
  public void testLookupForCricinCn() throws UnknownHostException {
    List<InetAddress> list = dns.lookup("cricin.cn");//cricin.cn A record
    InetAddress actual = InetAddress.getByName("cricin.cn");//@cricin.cn MX record pointer to cricin.cn's A record
    Assert.assertNotNull(list);
    Assert.assertTrue(list.size() == 1);
    Assert.assertEquals(list.get(0), actual);
  }

  @Test
  public void testLookupForQQ() throws UnknownHostException {
    List<InetAddress> list = dns.lookup("qq.com");
    InetAddress actual = InetAddress.getByName("mx1.qq.com");//mx1.qq.com is a mx recorded server for qq
    boolean match = false;
    for (InetAddress expected : list) {
      match |= expected != null && expected.equals(actual);
    }
    if (!match) {
      throw new AssertionError("qq.com'mx record doesn't match smtp.qq.com");
    }
  }

}
