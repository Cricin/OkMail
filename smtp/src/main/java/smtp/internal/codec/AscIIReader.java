package smtp.internal.codec;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

public class AscIIReader extends Reader {
  @Override
  public int read(@Nonnull char[] cbuf, int off, int len) throws IOException {
    return 0;
  }

  @Override
  public void close() throws IOException {

  }
}
