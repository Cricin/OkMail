package okmail.pop3;

import okmail.Authentication;
import okmail.mail.Headers;
import okmail.mail.Mail;

import java.io.IOException;
import java.util.List;

public class Pop3 {

  private Pop3() {

  }

  public List<String> allIds() throws IOException {
    return null;
  }

  public List<Mail> allMail() throws IOException {
    return null;
  }

  public void delete(String id) throws IOException {
  }

  public void delete(List<String> ids) throws IOException {

  }


  public Mail mail(String id) throws IOException {
    return null;
  }

  public Headers headers(String id) throws IOException {
    return null;
  }

  public long sizeOf(String id) throws IOException {
    return 0;
  }

  public long byteUsed() throws IOException {
    return 0;
  }

  public int mailCount() throws IOException {
    return 0;
  }


  private void connectIfNeeded() throws IOException {

  }

  private void throwIfFailed(PopResponse response) throws IOException {

  }

  public Pop3 create(String host, Authentication auth) {
    return new Pop3();
  }

}
