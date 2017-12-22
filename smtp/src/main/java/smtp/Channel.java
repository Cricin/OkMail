package smtp;

import smtp.internal.io.Sink;
import smtp.internal.io.Source;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * a channel holds all lower level connection resources
 */
public interface Channel {

  @Nonnull
  Socket socket();

  @Nonnull
  InputStream inputStream();

  @Nonnull
  OutputStream outputStream();

  @Nonnull
  Sink sink();

  @Nonnull
  Source source();

}
