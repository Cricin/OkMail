package okmail.demo;

import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.io.InputStream;

public class EmlRead {
  public static void main(String[] args) throws IOException {
    InputStream in = ClassLoader.getSystemResourceAsStream("mail1.eml");
    BufferedSource source = Okio.buffer(Okio.source(in));
//    Mail m = MailFactory.read(source);
    source.close();

//    System.out.println(m.from());
//    System.out.println(m.recipients());
//    System.out.println(m.headers());
  }

}
