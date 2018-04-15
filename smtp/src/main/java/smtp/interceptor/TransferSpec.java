package smtp.interceptor;

import smtp.mail.Encoding;
import smtp.misc.Utils;

import java.nio.charset.Charset;

public class TransferSpec {

  private final Charset charset;
  private final Encoding encoding;
  private final int lengthLimit;

  private TransferSpec(Builder builder) {
    this.charset = builder.charset;
    this.encoding = builder.encoding;
    this.lengthLimit = builder.lengthLimit;
  }

  public Charset charset() {
    return charset;
  }

  public Encoding encoding() {
    return encoding;
  }

  public int lengthLimit() {
    return lengthLimit;
  }

  public Builder newBuilder() {
    Builder out = new Builder();
    out.charset = charset;
    out.encoding = encoding;
    out.lengthLimit = lengthLimit;
    return out;
  }

  public static class Builder {
    Charset charset = Utils.UTF8;
    Encoding encoding = Encoding.BASE64;
    int lengthLimit = 76;

    public Builder charset(Charset charset) {
      this.charset = charset;
      return this;
    }

    public Builder encoding(Encoding encoding) {
      this.encoding = encoding;
      return this;
    }

    public Builder lengthLimit(int lengthLimit) {
      this.lengthLimit = lengthLimit;
      return this;
    }

    public TransferSpec build() {
      return new TransferSpec(this);
    }
  }
}
