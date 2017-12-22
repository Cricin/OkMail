package smtp;

import org.junit.Test;
import smtp.internal.codec.Base64;

import java.io.*;
import java.net.*;

public class SocketSendMailTest {

  BufferedReader reader = null;

  @Test
  public void sendMain() throws IOException {
    Socket socket = new Socket();
    InetAddress address = InetAddress.getByName("smtp.126.com");
    socket.connect(new InetSocketAddress(address, 25));
    OutputStream out = socket.getOutputStream();
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    PrintWriter writer = new PrintWriter(out);

    readLine();
    writer.append("helo cricin\r\n");
    writer.flush();
    readLine();

    writer.append("auth login\r\n");
    writer.flush();
    readLine();

    writer.append(Base64.encode("cricin@126.com".getBytes()));
    writer.append("\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    writer.append(Base64.encode("t150h0550s4431".getBytes()));
    writer.append("\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    writer.append("MAIL FROM:<cricin@126.com>\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    writer.append("RCPT TO:<cricin@cricin.cn>\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    writer.append("DATA\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    writer.append("Subject: test subject\r\n");
    writer.append("from:<cricin@126.com>\r\n");
    writer.append("to:<cricin@cricin.cn>\r\n");
    writer.append("\r\n");
    writer.append("test body\r\n");
    writer.append(".\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    writer.append("quit\r\n");
    writer.flush();
    System.out.println(reader.readLine());

    reader.close();
    reader = null;
    writer.close();
    socket.close();
  }

  public void readLine() throws IOException {
    System.out.println(reader.readLine());
  }

}
