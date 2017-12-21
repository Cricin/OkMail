package smtp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class Mail {

  private final Mailbox from;
  private final Mailbox to;
  private final List<Mailbox> carbonCopy;
  private final Headers headers;
  private final String subject;
  private final String content;


  private Mail(Builder builder) {
    from = builder.from;
    to = builder.to;
    headers = builder.headers.build();
    subject = builder.subject;
    content = builder.content;

    if (!builder.carbonCopy.isEmpty()) {
      List<Mailbox> temp = new ArrayList<>(builder.carbonCopy.size());
      temp.addAll(builder.carbonCopy);
      carbonCopy = Collections.unmodifiableList(temp);
    } else {
      carbonCopy = null;
    }
  }

  public Mailbox from() {
    return from;
  }

  public Mailbox to() {
    return to;
  }

  public String subject() {
    return subject;
  }

  public String content() {
    return content;
  }

  public Headers headers() {
    return headers;
  }

  public Builder newBuilder() {
    Builder out = new Builder();
    out.from = from;
    out.to = to;
    out.carbonCopy.addAll(carbonCopy);
    out.subject = subject;
    out.content = content;
    //todo add other field
    return out;
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder(256);
    out.append("From: <").append(from).append(">\n");
    out.append("To: <").append(to).append(">\n");
    out.append("Subject: ").append(subject).append('\n');
    out.append(headers);
    return out.toString();
  }

  /**********************builder*************************/

  public static class Builder {

    private Mailbox from;
    private Mailbox to;
    private HashSet<Mailbox> carbonCopy = new HashSet<>();
    private String subject;
    private Headers.Builder headers = new Headers.Builder();
    private String content;

    public Builder() {
    }

    // sender mail address
    public Builder from(Mailbox from) {
      return this;
    }

    // receiver mail address
    public Builder to(Mailbox to) {
      return this;
    }

    // carbon copy mail address(抄送,可以添加多个)
    public Builder cc(Mailbox cc) {
      carbonCopy.add(cc);
      return this;
    }

    public Builder replaceHeader(String name, String value) {
      headers.set(name, value);
      return this;
    }

    public Builder addHeader(String name, String value) {
      headers.add(name, value);
      return this;
    }

    public Builder headers(Headers headers) {
      this.headers = headers.newBuilder();
      return this;
    }

    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Mail build() {
      if (from == null) throw new IllegalStateException("from == null");
      if (to == null) throw new IllegalStateException("to == null");
      return new Mail(this);
    }

  }
}
