package smtp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface Channel {

  Socket socket();

  InputStream inputStream();

  OutputStream outputStream();

}
