package smtp;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketSendMailTest {

  @Test
  public void sendMain() throws IOException {
    Socket socket = new Socket();
    InetAddress address = InetAddress.getByName("smtp.126.com");
    socket.connect(new InetSocketAddress(address, 25));
    BufferedSink sink = Okio.buffer(Okio.sink(socket));
    BufferedSource source = Okio.buffer(Okio.source(socket));

    System.out.println(source.readUtf8Line());

    sink.writeUtf8("ehlo cricin\r\n").flush();
    System.out.println(source.readUtf8Line());






    sink.writeUtf8("quit\r\n").flush();
    System.out.println(source.readUtf8Line());

//    writer.append("auth login\r\n");
//    writer.flush();
//    readLine();
//
//    writer.append(Base64.encode("cricin@126.com".getBytes()));
//    writer.append("\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
//
//    writer.append(Base64.encode("t150h0550s4431".getBytes()));
//    writer.append("\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
//
//    writer.append("MAIL FROM:<cricin@126.com>\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
//
//    writer.append("RCPT TO:<cricin@cricin.cn>\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
//
//    writer.append("DATA\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
//
//    writer.append("Subject: test subject\r\n");
//    writer.append("from:<cricin@126.com>\r\n");
//    writer.append("to:<cricin@cricin.cn>\r\n");
//    writer.append("\r\n");
//    writer.append("test body\r\n");
//    writer.append(".\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
//
//    writer.append("quit\r\n");
//    writer.flush();
//    System.out.println(reader.readLine());
    sink.close();
    source.close();
    socket.close();
  }

}
