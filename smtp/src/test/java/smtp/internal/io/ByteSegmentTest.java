package smtp.internal.io;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteSegmentTest {

  AtomicInteger size = ByteSegment.SEGMENT_SIZE;

  @Test
  public void testTake() {

    resetByteSegment();

    ByteSegment first = ByteSegment.take();
    first.recycle();
    ByteSegment second = ByteSegment.take();
    Assert.assertEquals(first, second);
    Assert.assertEquals(size.get(), 1);

    ByteSegment three = ByteSegment.take();
    three.recycle();
    ByteSegment four = ByteSegment.take();
    Assert.assertEquals(three, four);
    Assert.assertEquals(size.get(), 2);


    ByteSegment five = ByteSegment.take();
    five.recycle();
    ByteSegment six = ByteSegment.take();
    Assert.assertEquals(five, six);
    Assert.assertEquals(size.get(), 3);
  }

  @Test
  public void testFree() {

    resetByteSegment();

    //do free if link-list length > 2
    int origin = ByteSegment.MAX_SEGMENT_COUNT;
    ByteSegment.MAX_SEGMENT_COUNT = 2;

    ByteSegment first = ByteSegment.take();
    ByteSegment second = ByteSegment.take();
    ByteSegment three = ByteSegment.take();
    ByteSegment four = ByteSegment.take();
    ByteSegment five = ByteSegment.take();

    Assert.assertEquals(size.get(), 5);

    first.recycle();
    ByteSegment.free();
    Assert.assertEquals(second, ByteSegment.head);
    Assert.assertEquals(size.get(), 4);

    three.recycle();
    ByteSegment.free();
    Assert.assertEquals(size.get(), 3);

    five.recycle();
    ByteSegment.free();
    Assert.assertEquals(four, ByteSegment.tail);
    Assert.assertEquals(size.get(), 2);

    ByteSegment.MAX_SEGMENT_COUNT = origin;
  }

  @Test
  public void testAddByte() {
    resetByteSegment();

    ByteSegment arr = ByteSegment.take();

    arr.addByte((byte) 65);
    arr.addByte((byte) 66);

    byte[] bytes = arr.allByte();
    Assert.assertArrayEquals(bytes, new byte[]{65, 66});
  }

  @Test
  public void testSetByteAt() {
    resetByteSegment();

    ByteSegment arr = ByteSegment.take();

    arr.addByte((byte) 65);
    arr.addByte((byte) 66);

    arr.setByteAt(1, (byte) 67);

    byte[] bytes = arr.allByte();
    Assert.assertArrayEquals(bytes, new byte[]{65, 67});
  }

  @Test(expected = IllegalStateException.class)
  public void testRecycle() {
    resetByteSegment();
    ByteSegment arr = ByteSegment.take();
    arr.recycle();
    arr.addByte((byte) 65);
  }


  @Test
  public void testCopyTo() {
    resetByteSegment();

    ByteSegment arr = ByteSegment.take();

    arr.addByte((byte) 65);
    arr.addByte((byte) 66);
    byte[] out = new byte[2];
    arr.copyTo(out, 0);

    System.out.println(Arrays.toString(out));
  }


  private void resetByteSegment() {
    ByteSegment.head = null;
    ByteSegment.tail = null;
    ByteSegment.SEGMENT_SIZE.set(0);
  }


}
