package smtp;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

public interface Sink extends Closeable, Flushable {

}
