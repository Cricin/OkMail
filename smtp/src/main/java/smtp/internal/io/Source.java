package smtp.internal.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class Source implements Closeable {

  public Source(InputStream in) {

  }

  void write(int i) throws IOException {

  }

  void writeLine() throws IOException {

  }



  @Override
  public void close() throws IOException {

  }

  public static Source wrap(InputStream in) {
    return new Source(in);
  }
}
