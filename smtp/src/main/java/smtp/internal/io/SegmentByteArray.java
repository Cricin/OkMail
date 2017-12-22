package smtp.internal.io;

import javax.annotation.Nullable;

public final class SegmentByteArray {

  private int size;
  private ByteSegment head;
  private ByteSegment tail;
  private ByteSegment cur;

  private SegmentByteArray() {
    head = tail = cur = ByteSegment.take();
  }

  /*public methods*/

  public void putAll(byte[] arr) {
    putAll(arr, 0, arr.length);
  }

  public void putAll(byte[] arr, int startPos, int length) {
    growSize(length);
    int copied = 0;
    ByteSegment p = cur;
    while (copied != length) {
      copied += p.addBytes(arr, startPos + copied, length - copied);
      p = p.reserved;
    }
    size += copied;
  }

  public void put(byte b) {
    while (cur.addByte(b) != 1) {
      takeSegment();
      cur = tail;
    }
    size++;
  }

  public byte getAt(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
    ByteSegment p = this.head;
    int base = 0;
    while (p != null) {
      if (base + ByteSegment.DATA_SIZE > index) {
        return p.getByteAt(index - base);
      }
      base += ByteSegment.DATA_SIZE;
      p = p.reserved;
    }
    throw new AssertionError(); //never happen
  }

  public int size() {
    return size;
  }

  @Nullable
  public byte[] toBytes() {
    if (size == 0) {
      return null;
    }
    byte[] out = new byte[size];
    ByteSegment p = head;
    int position = 0;
    while (p != null) {
      position += p.copyTo(out, position);
      p = p.reserved;
    }
    return out;
  }

  /**
   * called when used, every time, to make ByteSegment
   * works properly
   */
  public final void deAllocate() {
    ByteSegment p = head;
    while (p != null) {
      p.recycle();
      p = p.reserved;
    }
    head = null;
  }

  /*private methods*/
  private void growSize(int size) {
    int fillIndex = size % ByteSegment.DATA_SIZE;
    int remain = size - (ByteSegment.DATA_SIZE - fillIndex - 1);
    if (remain > 0) {
      takeSegment();
      remain -= ByteSegment.DATA_SIZE;
      growSize(remain);
    }
  }

  private void takeSegment() {
    ByteSegment taken = ByteSegment.take();
    tail.reserved = taken;
    tail = tail.reserved;
  }

  /*static factory methods*/
  public static SegmentByteArray allocate() {
    return new SegmentByteArray();
  }

}
