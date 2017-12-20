package smtp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface Hooker {

  <T> T hookInstantiate(T target);

  Socket socketOpened(Socket socket);

  InputStream inputStreamOpened(InputStream in);

  OutputStream outputStreamOpened(OutputStream out);

}