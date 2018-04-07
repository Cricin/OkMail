package okmail.pop3;

import okio.Buffer;
import okio.BufferedSource;
import okio.Source;

public class PopResponse {

  public boolean isSuccessful(){
    return false;
  }

  public BufferedSource source(){
    return null;
  }


  public  static PopResponse of(String response) {
    return null;
  }

}
