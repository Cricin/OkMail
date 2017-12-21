package smtp.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketIO {

  private Socket socket;
  private OutputStream out;
  private InputStream in;


  public Socket socket() {
    return socket;
  }


}
