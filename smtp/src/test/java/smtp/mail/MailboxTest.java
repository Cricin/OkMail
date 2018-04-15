package smtp.mail;

import org.junit.Assert;
import org.junit.Test;
import smtp.mail.Mailbox;

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
  public void testDisplayName() {
    Assert.assertEquals("cricin", Mailbox.parse("cricin<crica@qq.com>").displayName());
    Assert.assertEquals("1278486605", three.displayName());

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
