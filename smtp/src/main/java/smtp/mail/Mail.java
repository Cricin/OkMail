package smtp.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class Mail {

  private final Mailbox from;
  private final List<Mailbox> recipients;
  private final List<Mailbox> cc;
  private final List<Mailbox> bcc;
  private final Headers headers;
  private final String content;
  private final Object tag;

  private Mail(Builder builder) {
    from = builder.from;
    headers = builder.headers.build();
    content = builder.content;
    tag = builder.tag;
    recipients = Collections.unmodifiableList(new ArrayList<>(builder.recipients));
    cc = Collections.unmodifiableList(new ArrayList<>(builder.cc));
    bcc = Collections.unmodifiableList(new ArrayList<>(builder.bcc));
  }

  public Mailbox from() {
    return from;
  }

  public List<Mailbox> recipients() {
    return recipients;
  }

  public List<Mailbox> cc() {
    return cc;
  }

  public List<Mailbox> bcc() {
    return bcc;
  }

  public Headers headers() {
    return headers;
  }

  public String content() {
    return content;
  }

  public Object tag() {
    return tag;
  }

  public Builder newBuilder() {
    Builder out = new Builder();
    out.from = from;
    out.recipients.addAll(recipients);
    out.cc.addAll(cc);
    out.bcc.addAll(bcc);
    out.content = content;
    out.headers = headers.newBuilder();
    out.tag = this.tag;
    return out;
  }

  /**********************builder*************************/

  public static class Builder {

    private Mailbox from;
    private HashSet<Mailbox> recipients = new HashSet<>();
    private HashSet<Mailbox> cc = new HashSet<>();
    private HashSet<Mailbox> bcc = new HashSet<>();
    private Headers.Builder headers = new Headers.Builder();
    private String content;
    private Object tag;

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

    public Builder recipients(List<Mailbox> recipients) {
      this.recipients.clear();
      this.recipients.addAll(recipients);
      return this;
    }

    // carbon copy mail address(抄送,可以添加多个)
    public Builder addCc(Mailbox value) {
      this.cc.add(value);
      return this;
    }

    public Builder removeCc(Mailbox value) {
      this.cc.remove(value);
      return this;
    }

    public Builder addBcc(Mailbox value) {
      this.bcc.add(value);
      return this;
    }

    public Builder removeBcc(Mailbox value) {
      this.bcc.remove(value);
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

    public Builder tag(Object tag) {
      this.tag = tag;
      return this;
    }

    public Mail build() {
      if (from == null) throw new IllegalStateException("from == null");
      if (recipients.isEmpty()) throw new IllegalStateException("no recipients");
      return new Mail(this);
    }

  }
}
