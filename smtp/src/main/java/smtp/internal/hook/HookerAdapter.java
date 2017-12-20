package smtp.internal.hook;

import smtp.Hooker;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * empty implementation of {@link Hooker} Hooker
 */
public class HookerAdapter implements Hooker {

  @Override
  public <T> T hookInstantiate(@Nonnull T target) {
    return target;
  }

  @Override
  public Socket socketOpened(@Nonnull Socket socket) {
    return socket;
  }

  @Override
  public InputStream inputStreamOpened(@Nonnull InputStream in) {
    return in;
  }

  @Override
  public OutputStream outputStreamOpened(@Nonnull OutputStream out) {
    return out;
  }


}
