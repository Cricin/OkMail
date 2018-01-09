package okmail.smtp.internal.hook;

import okmail.smtp.Hooker;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class HookerDelegate implements Hooker {

  private Hooker hook;

  public HookerDelegate(Hooker origin) {
    this.hook = origin;
  }

  @Override
  public <T> T hookInstantiate(@Nonnull T target) {
    return hook.hookInstantiate(target);
  }

  @Override
  public Socket socketOpened(@Nonnull Socket socket) {
    return hook.socketOpened(socket);
  }

  @Override
  public InputStream inputStreamOpened(@Nonnull InputStream in) {
    return hook.inputStreamOpened(in);
  }

  @Override
  public OutputStream outputStreamOpened(@Nonnull OutputStream out) {
    return hook.outputStreamOpened(out);
  }
}
