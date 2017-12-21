package smtp;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * a channel hold all lower level connection resources
 */
public interface Channel {

  @Nonnull
  Socket socket();

  @Nonnull
  InputStream inputStream() throws IOException;

  @Nonnull
  OutputStream outputStream() throws IOException;

}
