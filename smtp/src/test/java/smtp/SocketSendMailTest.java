package smtp;

import org.junit.Test;
import smtp.internal.codec.Base64;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

public class SocketSendMailTest {

  @Test
  public void sendMain() throws IOException {
    Socket socket = new Socket();
    InetAddress address = InetAddress.getByName("Smtp.126.com");
    socket.connect(new InetSocketAddress(address, 25));
    OutputStream out = socket.getOutputStream();
    PrintWriter writer = new PrintWriter(out);

    writer.append("helo cricin\n");
    writer.append("auth login\n");

    writer.append(Base64.encode("cricin@126.com".getBytes()));
    writer.append('\n');

    writer.append(Base64.encode("t150h0550s4431".getBytes()));
    writer.append('\n');
    writer.append("MAIL FROM:<cricin@126.com>\n");
    writer.append("RCPT TO:<cricin@qq.com>\n");
    writer.append("DATA\n");
    writer.append("subject: test subject\n");
    writer.append("from:<cricin@126.com>\n");
    writer.append("to:<cricin@qq.com>\n");
    writer.append("\n");
    writer.append("test body\n");

    writer.append(".\n");

    writer.append("quit\n");

    writer.close();
    socket.close();
  }


}
