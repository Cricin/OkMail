package smtp;

import smtp.internal.Util;

import javax.annotation.Nullable;

public class Response {

  public static final Response EMPTY = null; //todo


  private int code;
  private String message;
  private int[] codes;
  private String[] responses;

  private Response(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public boolean containsCode(int code) {
    if (codes != null) {
      for (int i : codes) {
        if (i == code) return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Response) {
      Response other = (Response) obj;
      return code == other.code && Util.equals(message, other.message);
    }
    return false;
  }

  public boolean isMultiline() {
    return codes == null;
  }

  @Override
  public int hashCode() {
    int out = 17 + 31 * code;
    out += Util.hashCode(message);
    return out;
  }

  @Nullable
  public static Response parse(String raw) {
    String[] split = raw.split(" ");
    if (split.length < 2) {
      return null;
    }
    int code = Integer.parseInt(split[0]);
    String msg = split[1].trim();
    return of(code, msg);
  }

  public static Response of(int code, String message) {
    return new Response(code, message);
  }

  public static Response parseRaw(byte[] raw) {
    boolean multiline = (int) raw[3] == 45;


    return null;
  }

}
