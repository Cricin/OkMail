package okmail.smtp;

public interface Mime {

  default String version() {
    return "1.0";
  }

  byte[] encode();


}
