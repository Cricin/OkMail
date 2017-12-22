package smtp.internal.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public final class Sink implements Closeable {

  private OutputStream out;

  private Sink(OutputStream out) {
    this.out = out;
  }

  public int read() throws IOException {
    return 0;
  }

  public ByteArray readLine() throws IOException {
    return null;
  }

  @Override
  public void close() throws IOException {
    out.close();
  }

  public static Sink wrap(OutputStream out) {
    return new Sink(out);
  }

}
