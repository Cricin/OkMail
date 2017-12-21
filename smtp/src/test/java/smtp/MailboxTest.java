package smtp;

import org.junit.Assert;
import org.junit.Test;

public class MailboxTest {

  private Mailbox one = Mailbox.parse("cricin@qq.com");
  private Mailbox two = Mailbox.parse("cricin@126.com");
  private Mailbox three = Mailbox.parse("1278486605@qq.com");

  @Test
  public void testName() {
    Assert.assertEquals(one.name(), "cricin");
    Assert.assertEquals(two.name(), "cricin");
    Assert.assertEquals(three.name(), "1278486605");
  }


  @Test
  public void testHost() {
    Assert.assertEquals(one.host(), "qq.com");
    Assert.assertEquals(two.host(), "126.com");
    Assert.assertEquals(three.host(), "qq.com");
  }

  @Test
  public void testMailbox() {
    Assert.assertEquals(one.mailbox(), "cricin@qq.com");
    Assert.assertEquals(two.mailbox(), "cricin@126.com");
    Assert.assertEquals(three.mailbox(), "1278486605@qq.com");
  }

  @Test
  public void testHashcode() {
    Assert.assertEquals(one.hashCode(), "cricin@qq.com".hashCode());
    Assert.assertEquals(two.hashCode(), "cricin@126.com".hashCode());
    Assert.assertEquals(three.hashCode(), "1278486605@qq.com".hashCode());
  }

  @Test
  public void testEquals() {
    Assert.assertEquals(one, new Mailbox("cricin@qq.com", "cricin", "qq.com"));
    Assert.assertEquals(two, new Mailbox("cricin@126.com", "cricin", "126.com"));
    Assert.assertEquals(three, new Mailbox("1278486605@qq.com", "1278486605", "qq.com"));
  }

  @Test
  public void testToString() {
    Assert.assertEquals(one.toString(), "cricin@qq.com");
    Assert.assertEquals(two.toString(), "cricin@126.com");
    Assert.assertEquals(three.toString(), "1278486605@qq.com");
  }

  @Test
  public void testParse() {
    Mailbox box = Mailbox.parse("cricin@qq.com");
    Assert.assertNotNull(box);
    Mailbox error = Mailbox.parse("cricin-qq.com");
    Assert.assertNull(error);
  }

}
