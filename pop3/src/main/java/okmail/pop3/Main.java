package okmail.pop3;

import okio.BufferedSource;
import okmail.network.Channel;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;

public class Main {

  public static void main(String[] args) throws IOException {

    Channel c = Channel.DEFAULT_FACTORY.connect(SocketFactory.getDefault(), InetAddress.getByName("cricin.cn"), 110);

    BufferedSource source = Util.buffer(c.source());


    System.out.println(source.readUtf8Line());
    source.close();
    c.socket().close();
  }


}
