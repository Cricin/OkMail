package okmail.smtp.internal.io;

import org.junit.Assert;
import org.junit.Test;

public class ByteArrayTest {

  @Test
  public void testAllocate() {
    SegmentByteArray arr = SegmentByteArray.allocate();

    arr.put((byte) 65);
    arr.put((byte) 66);

    Assert.assertEquals(arr.size(), 2);
    Assert.assertArrayEquals(arr.toBytes(), new byte[]{65, 66});

    arr.deAllocate();

  }

  @Test
  public void testPutAll() {

    SegmentByteArray arr = SegmentByteArray.allocate();

    byte[] origin = {65, 66, 67, 68};
    arr.putAll(origin);
    Assert.assertEquals(arr.size(), 4);
    Assert.assertArrayEquals(origin, arr.toBytes());

    arr.putAll(origin, 1, 3);
    Assert.assertEquals(arr.size(), 7);
    Assert.assertArrayEquals(arr.toBytes(), new byte[]{65, 66, 67, 68, 66, 67, 68});


    arr.deAllocate();

  }

  @Test
  public void testExpend() {
    resetByteSegment();
    ByteSegment.DATA_SIZE = 2;

    SegmentByteArray arr = SegmentByteArray.allocate();
    arr.put((byte) 66);
    arr.put((byte) 67);
    arr.put((byte) 68);
    arr.putAll(new byte[]{69, 70});

    Assert.assertEquals(arr.size(), 5);
//    System.out.println(Arrays.toString(arr.toBytes()));
    Assert.assertArrayEquals(arr.toBytes(), new byte[]{66, 67, 68, 69, 70});

    ByteSegment.DATA_SIZE = 8000;
  }


  private void resetByteSegment() {
    ByteSegment.head = null;
    ByteSegment.tail = null;
    ByteSegment.SEGMENT_SIZE.set(0);
  }

}
