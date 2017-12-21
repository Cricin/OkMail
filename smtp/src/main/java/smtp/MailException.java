package smtp;

import java.io.IOException;

public final class MailException extends IOException {

  public MailException(String message) {
    super(message);
  }

  public MailException(String message, Throwable cause) {
    super(message, cause);
  }


}