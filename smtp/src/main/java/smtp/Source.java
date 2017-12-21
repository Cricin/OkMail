package smtp;

import java.io.Closeable;
import java.io.IOException;

public interface Source extends Closeable {

  public void write(int i) throws IOException;

  void writeLine() throws IOException;


}
