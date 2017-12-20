package smtp;

import java.util.regex.Pattern;

public final class Authentication {

  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

  private String username;
  private String password;
  private boolean useSsl;

  private Authentication(Builder builder) {

  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(100);
    //TODO

    return builder.toString();
  }

  public static final class Builder {

    private String username;
    private String password;

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Authentication build() {
      return new Authentication(this);
    }
  }
}