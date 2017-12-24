package smtp;

import smtp.internal.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Mailbox {

  private static final Pattern MAILBOX_PATTERN = Pattern.compile("^([a-zA-Z0-9_-]+)@([a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+)$");

  private String mailbox;
  private String name; //max: 64byte
  private String host; //max: 64byte

  Mailbox(String mailbox, String name, String host) {
    this.mailbox = mailbox;
    this.name = name;
    this.host = host;
  }

  @Nonnull
  public String name() {
    return name;
  }

  @Nonnull
  public String host() {
    return host;
  }

  @Nonnull
  public String mailbox() {
    return mailbox;
  }

  @Override
  public String toString() {
    return mailbox;
  }

  @Override
  public int hashCode() {
    return mailbox.hashCode();
  }

  public String canonical() {
    return "<" + mailbox + ">";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Mailbox) {
      return ((Mailbox) obj).name.equals(name);
    }
    return false;
  }

  /**
   * @param string the mailbox in text form, simply like <p>alice@host.com</p>
   * @return parsed Mailbox or null if the specified string format is illegal
   */
  @Nonnull
  public static Mailbox parse(String string) {
    Matcher matcher = MAILBOX_PATTERN.matcher(string);
    if (!matcher.lookingAt()) throw new IllegalArgumentException("invalid mailbox");
    String name = matcher.group(1).toLowerCase(Locale.US);
    if (name.getBytes(Util.ASCII).length > 64) throw new IllegalArgumentException("mailbox name too long");
    String host = matcher.group(2).toLowerCase(Locale.US);
    if (host.getBytes(Util.ASCII).length > 64) throw new IllegalArgumentException("mailbox host too long");
    return new Mailbox(string, name, host);
  }

}
