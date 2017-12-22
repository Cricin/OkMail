package smtp.internal.io;

/**
 * ByteArray is same as List<Byte>, but more memory efficient
 * without auto-boxing
 */
public interface ByteArray {

  boolean isEmpty();

  int size();

  byte[] toArray();

  void add(byte b);

  void addAll(byte[] in);

  void addAll(ByteArray other);

  void clear();

  byte get(int index);

  void set(int index, byte value);

  int indexOf(byte b);

  int lastIndexOf(byte b);

  ByteArray subArray(int fromIndex, int toIndex);

  void deAllocate();

}
