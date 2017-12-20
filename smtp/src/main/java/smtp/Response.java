package smtp;

import javax.annotation.Nullable;
import java.util.Objects;

public class Response {

  private int code;
  private String message;
  private int[] codes;
  private String[] responses;

  Response(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Response) {
      Response other = (Response) obj;
      return code == other.code && Objects.equals(message, other.message);
    }
    return false;
  }

  public boolean isMultiline() {
    return false; //TODO support multiline response
  }

  @Override
  public int hashCode() {
    int out = 17 + 31 * code;
    out += message == null ? 0 : message.hashCode();
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
}
