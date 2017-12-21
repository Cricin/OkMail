package smtp.internal.io;

import javax.annotation.Nullable;

//todo make this more useful and memory efficiently

public class ByteArray {

  private int[] arr;
  private int index;
  private int byteShift;

  public ByteArray(int size) {
    arr = new int[size];
    byteShift = 0xFF000000;
  }

  public void add(byte b) {
    arr[index] = arr[index] & byteShift;
    byteShift >>= 8;
    if (byteShift == 0) {
      byteShift = 0xFF000000;
      index++;
    }
  }

  public void add(int b) {
    add((byte) b);
  }

  @Nullable
  public byte[] toBytes() {
    if (index == 0 && byteShift == 0) {
      return null;
    }

    return null;
  }
}
