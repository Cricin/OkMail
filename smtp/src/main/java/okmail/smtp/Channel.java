package okmail.smtp;

import okio.Sink;
import okio.Source;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Socket;

/**
 * a channel holds all lower level connection resources
 */
public interface Channel {

  @Nonnull
  Socket socket();

  @Nonnull
  Sink sink() throws IOException;

  @Nonnull
  Source source() throws IOException;

}
