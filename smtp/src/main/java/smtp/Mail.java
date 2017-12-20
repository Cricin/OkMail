package smtp;

public final class Mail {

  /*package*/Mail(Builder builder) {

  }

  public void from() {
  }

  public static class Builder {

    public Builder() {
    }

    public Builder from() {
      return this;
    }

    public Builder to() {
      return this;
    }

    public Builder subject() {
      return this;
    }

    public Builder header() {
      return this;
    }

    public Builder extra() {
      return this;
    }


    public Mail build() {
      return new Mail(this);
    }

  }
}
