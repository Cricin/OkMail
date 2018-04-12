package smtp.auth;

public final class Authentication {

  public static final Authentication NONE = of("", "");

  final String key;
  final String token;

  private Authentication(String key, String token) {
    this.key = key;
    this.token = token;
  }

  public String key() {
    return key;
  }

  public String token() {
    return token;
  }

  @Override
  public String toString() {
    return "{ key=" + key + ", token=" + token + " }";
  }

  @Override
  public int hashCode() {
    int hash = 21;
    if (key != null) {
      hash += key.hashCode();
    }
    if (token != null) {
      hash += token.hashCode();
    }
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj instanceof Authentication) {
      Authentication a = (Authentication) obj;
      boolean equals;
      if (key == null) equals = a.key == null;
      else equals = key.equals(a.key);
      if (token == null) equals &= a.token == null;
      else equals &= token.equals(a.token);
      return equals;
    }
    return false;
  }

  public static Authentication of(String key, String token) {
    return new Authentication(key, token);
  }
}
