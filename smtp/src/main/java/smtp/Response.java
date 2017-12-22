package smtp;

import smtp.internal.io.ByteArray;

public final class Response {

  public int code() {
    return 0;
  }

  public String message() {
    return null;
  }

  public boolean isMultiLine() {
    return false;
  }

  public boolean isSuccessful() {
    return false;
  }


  /******** static factory method *********/

  public static Response parse(ByteArray arr) {

    return null;
  }

  public static Response parse(byte[] arr) {
    return null;
  }

  public static Response single(int code, String msg) {
    return null;
  }

  public static Response multi(int[] code, String[] msg) {
    return null;
  }

}
