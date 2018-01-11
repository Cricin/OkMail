package okmail.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class Mail {

  private final Mailbox from;
  private final List<Mailbox> recipients;
  private final String password;
  private final List<Mailbox> carbonCopy;
  private final Headers headers;
  private final String content;


  private Mail(Builder builder) {
    from = builder.from;
    password = builder.password;
    headers = builder.headers.build();
    content = builder.content;
    recipients = Collections.unmodifiableList(new ArrayList<>(builder.recipients));

    if (!builder.carbonCopy.isEmpty()) {
      List<Mailbox> temp = new ArrayList<>(builder.carbonCopy.size());
      temp.addAll(builder.carbonCopy);
      carbonCopy = Collections.unmodifiableList(temp);
    } else {
      carbonCopy = Collections.emptyList();
    }
  }

  public Mailbox from() {
    return from;
  }

  public List<Mailbox> recipients() {
    return recipients;
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
    out.recipients.addAll(recipients);
    out.carbonCopy.addAll(carbonCopy);
    out.content = content;
    out.headers = headers.newBuilder();
    out.password = password;
    //todo add other field
    return out;
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder(256);
    out.append("From: <").append(from).append(">\n");
    out.append("To: <").append(recipients).append(">\n");
    out.append(headers);
    return out.toString();
  }

  public String password() {
    return password;
  }

  /**********************builder*************************/

  public static class Builder {

    private Mailbox from;
    private HashSet<Mailbox> recipients = new HashSet<>();
    private HashSet<Mailbox> carbonCopy = new HashSet<>();
    private Headers.Builder headers = new Headers.Builder();
    private String content;
    private String password;

    public Builder() {
    }

    // sender mail address
    public Builder from(Mailbox from) {
      this.from = from;
      return this;
    }

    // receiver mail address
    public Builder addRecipient(Mailbox recipient) {
      recipients.add(recipient);
      return this;
    }

    // carbon copy mail address(抄送,可以添加多个)
    public Builder cc(Mailbox cc) {
      carbonCopy.add(cc);
      return this;
    }

    public Builder password(String password) {
      this.password = password;
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
      this.headers.add("Subject", subject);
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Mail build() {
      if (from == null) throw new IllegalStateException("from == null");
      if (recipients.isEmpty()) throw new IllegalStateException("no recipients");
      return new Mail(this);
    }

  }
}
