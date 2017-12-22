package smtp.internal.io;

import org.junit.Assert;
import org.junit.Test;

public class SimpleByteArrayTest {

  @Test
  public void test() {

    ByteArray arr = new SimpleByteArray(200);

    arr.add((byte) 65);
    arr.add((byte) 66);
    arr.add((byte) 67);
    Assert.assertEquals(arr.size(), 3);
    Assert.assertArrayEquals(arr.toArray(), new byte[]{65, 66, 67});

    arr.addAll(new byte[]{68, 69});

    Assert.assertEquals(arr.size(), 5);
    Assert.assertArrayEquals(arr.toArray(), new byte[]{65, 66, 67, 68, 69});

    arr.clear();
    Assert.assertTrue(arr.isEmpty());


  }


}
