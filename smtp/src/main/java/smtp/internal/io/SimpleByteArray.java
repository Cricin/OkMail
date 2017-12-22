package smtp.internal.io;

import java.util.Arrays;

public class SimpleByteArray implements ByteArray {

  private static final byte[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

  private byte[] data;
  private int size;

  public SimpleByteArray(int initialCapacity) {
    data = new byte[initialCapacity];
  }

  public SimpleByteArray(byte[] src) {
    data = Arrays.copyOf(src, src.length);
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public byte[] toArray() {
    return Arrays.copyOf(data, size);
  }

  @Override
  public void add(byte b) {
    growIfNeeded(1);
    data[size] = b;
    size++;
  }

  @Override
  public void addAll(byte[] in) {
    if (in.length == 0) return;
    growIfNeeded(in.length);
    System.arraycopy(in, 0, data, size, in.length);
    size += in.length;
  }

  @Override
  public void addAll(ByteArray other) {
    if (other instanceof SimpleByteArray) {
      addAll(((SimpleByteArray) other).data);
    } else {
      byte[] temp = other.toArray();
      addAll(temp);
    }
  }

  @Override
  public void clear() {
    this.size = 0;
  }

  @Override
  public byte get(int index) {
    checkIndex(index);
    return data[index];
  }

  @Override
  public void set(int index, byte value) {
    checkIndex(index);
    data[index] = value;
  }

  @Override
  public int indexOf(byte b) {
    for (int i = 0; i < data.length; i++) {
      if (data[i] == b) return i;
    }
    return -1;
  }

  @Override
  public int lastIndexOf(byte b) {
    for (int i = data.length - 1; i >= 0; i--) {
      if (data[i] == b) return i;
    }
    return -1;
  }

  @Override
  public ByteArray subArray(int fromIndex, int toIndex) {
    return null;
  }

  @Override
  public void deAllocate() {

  }

  private void growIfNeeded(int i) {
    final int free = data.length - size;
    if (i >= free) {
      ensureCapacity(data.length + i - free);
    }
  }

  private void ensureCapacity(int minCapacity) {
    int minExpand = (data != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
            // any size if not default element table
            ? 0
            // larger than default for default empty table. It's already
            // supposed to be at default size.
            : 10;

    if (minCapacity > minExpand) {
      ensureExplicitCapacity(minCapacity);
    }
  }


  private void checkIndex(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("index < 0");
    }
    if (index > size()) {
      throw new IndexOutOfBoundsException("index > size");
    }
  }


  private void ensureExplicitCapacity(int minCapacity) {

    // overflow-conscious code
    if (minCapacity - data.length > 0)
      grow(minCapacity);
  }

  private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = data.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
      newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
      newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    data = Arrays.copyOf(data, newCapacity);
  }


  private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
      throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
  }

}
