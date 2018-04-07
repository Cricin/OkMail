package smtp.mime;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

public class Base64TransferEncoding implements TransferEncoding {

  @Override
  public int read(BufferedSource in) throws IOException {
    return 0;
  }

  @Override
  public int read(BufferedSource in, byte[] buffer, int offset, int length) throws IOException {
    return 0;
  }

  @Override
  public void write(BufferedSink out, int b) throws IOException {

  }

  @Override
  public void write(BufferedSink out, byte[] buffer, int offset, int length) throws IOException {

  }

  @Override
  public void complete() {

  }
}
