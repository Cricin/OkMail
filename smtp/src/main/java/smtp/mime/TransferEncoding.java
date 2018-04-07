package smtp.mime;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

public interface TransferEncoding {

  /* decode */

  int read(BufferedSource in) throws IOException;

  int read(BufferedSource in, byte[] buffer, int offset, int length) throws IOException;

  /* encode */

  void write(BufferedSink out, int b) throws IOException;

  void write(BufferedSink out, byte[] buffer, int offset, int length) throws IOException;

  void complete();

}