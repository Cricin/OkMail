package okmail.demo;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.net.Socket;

public class Test {

  public static void main(String[] args) throws IOException {

    Socket socket = new Socket("smtp.qq.com", 25);

    BufferedSink sink = Okio.buffer(Okio.sink(socket));

    BufferedSource source = Okio.buffer(Okio.source(socket));


    System.out.println(source.readUtf8Line());


    sink.writeUtf8("ehlo cricin.cn\r\n");


    System.out.println(readOptions(source));


  }

  private static String readOptions(BufferedSource source) throws IOException {
    StringBuilder sb = new StringBuilder();
    String text = null;
    while ((text = source.readUtf8Line()) != null) {
      sb.append(text);
      if (text.indexOf(' ') != -1) {
        break;
      }
    }
    return sb.toString();
  }


}
