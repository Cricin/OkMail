package smtp.misc;

import javax.annotation.Nullable;

public final class Response {

  public static final Response EMPTY = single(-1, "EMPTY RESPONSE");

  private final int code;
  private final String message;
  private final int[] codes;
  private final String[] messages;

  private Response(int code, String message) {
    this.code = code;
    this.message = message;
    this.codes = null;
    this.messages = null;
  }

  private Response(int[] codes, String[] messages) {
    this.code = -1;
    this.message = null;
    this.codes = codes;
    this.messages = messages;
  }


  public int code() {
    if (codes != null) {
      return codes[0];
    }
    return code;
  }

  public String message() {
    return message;
  }

  public boolean isMultiLine() {
    return codes != null;
  }

  public boolean contains(int code) {
    if (codes != null) {
      for (int i : codes) {
        if (i == code) return true;
      }
    } else {
      return this.code == code;
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(128);
    if (codes != null && messages != null) {
      builder.append("{ ");
      for (int i = 0; i < codes.length; i++) {
        builder.append("{ ");
        builder.append("code = ");
        builder.append(codes[i]);
        builder.append(", message = ");
        builder.append(messages[i]);
        builder.append(" }");
      }
      builder.append(" }");
    } else {
      builder.append("{ code = ")
              .append(code)
              .append(", message = ")
              .append(message)
              .append(" }");
    }
    return builder.toString();
  }

  public int[] codes() {
    return codes;
  }

  @Nullable
  public String[] messages() {
    return messages;
  }

  public boolean isSuccessful() {
    return false;
  }

  /******** static factory method *********/

  public static Response single(int code, String message) {
    return new Response(code, message);
  }

  public static Response multi(int[] codes, String[] messages) {
    return new Response(codes, messages);
  }

}
