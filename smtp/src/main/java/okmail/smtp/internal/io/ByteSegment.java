package okmail.smtp.internal.io;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

final class ByteSegment {

  /*package*/static int DATA_SIZE = 8000; //8kb
  /*package*/static int MAX_SEGMENT_COUNT = 512; //4mb

  /*package*/static ByteSegment head;
  /*package*/static ByteSegment tail;
  /*package*/static final AtomicInteger SEGMENT_SIZE = new AtomicInteger(0);

  private byte[] array = new byte[DATA_SIZE];
  private int size = 0;
  private boolean recycled = false;

  private ByteSegment next;
  ByteSegment reserved;

  private ByteSegment() {
  }

  void setByteAt(int index, byte b) {
    checkNotRecycled();
    if (index < 0 || index > DATA_SIZE) {
      throw new IndexOutOfBoundsException(">0 and <" + DATA_SIZE + " required");
    }
    array[index] = b;
  }

  //returns how many byte added
  int addByte(byte b) {
    checkNotRecycled();
    if (size == DATA_SIZE) {
      return 0;
    }
    array[size] = b;
    size++;
    return 1;
  }

  //returns how many byte added
  int addBytes(byte[] arr, int from, int length) {
    checkNotRecycled();
    final int remain = DATA_SIZE - size;
    if (remain >= length) {
      System.arraycopy(arr, from, array, size, length);
      size += length;
      return length;
    } else {
      System.arraycopy(arr, from, array, size, remain);
      size += remain;
      return remain;
    }
  }

  //returns how many byte added
  int addBytes(byte[] arr) {
    return addBytes(arr, 0, arr.length);
  }

  byte getByteAt(int index) {
    checkNotRecycled();
    if (index < 0 || index > this.size) {
      throw new IndexOutOfBoundsException(">0 and <" + index + " required");
    }
    return array[index];
  }

  byte[] allByte() {
    checkNotRecycled();
    return Arrays.copyOf(array, size);
  }

  /**
   * copy whole byte data we held to {@code out} out with start position
   * {@code start} start
   *
   * @param out   the data we copied to
   * @param start the position we start copy in out byte array
   * @return count of bytes we copied
   */
  int copyTo(byte[] out, int start) {
    final int length = out.length;
    if (size > length - start) {
      throw new IndexOutOfBoundsException("out byte array too small");
    }
    System.arraycopy(array, 0, out, start, size);
    return size;
  }

  synchronized void recycle() {
    if (recycled) {
      throw new IllegalStateException("recycled twice");
    }
    this.recycled = true;
  }


  private void checkNotRecycled() {
    if (recycled) {
      throw new IllegalStateException("segment recycled");
    }
  }

  /********** Static Allocator *********/

  synchronized static void free() {
    if (SEGMENT_SIZE.get() <= MAX_SEGMENT_COUNT) return;
    ByteSegment pre = null;
    ;
    ByteSegment i = head;
    while (i != null) {
      if (i.recycled) {
        // need to free i, now pre.next = i.next
        remove(pre, i);
        SEGMENT_SIZE.decrementAndGet();
      } else {
        if (pre == null) {
          pre = i;
        } else {
          pre = pre.next;
        }
      }
      i = i.next;
    }
  }

  private static void remove(ByteSegment pre, ByteSegment target) {
    if (pre == null) { //need to remove first one
      head = target.next;
    } else if (target.next == null) { //need to remove last one
      tail = pre;
    } else {
      pre.next = target.next;
    }
  }

  synchronized static ByteSegment take() {
    if (head == null) {
      SEGMENT_SIZE.incrementAndGet();
      return head = tail = new ByteSegment();
    }
    ByteSegment p = head;

    while (p != null) {
      if (p.recycled) {
        break;
      }
      p = p.next;
    }
    if (p == null) {
      SEGMENT_SIZE.incrementAndGet();
      tail.next = p = new ByteSegment();
      tail = tail.next;
    }
    p.recycled = false;
    p.size = 0;

    //release if us instance more than MAX_SEGMENT_SIZE
    free();

    return p;
  }

}