package smtp.internal.hook;

import smtp.Hooker;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public final class HookerComposite implements Hooker {

  private List<Hooker> hookers = new LinkedList<>();

  public void addHooker(Hooker h) {
    hookers.add(h);
  }

  public void removeHooker(Hooker h) {
    hookers.remove(h);
  }

  @Override
  public <T> T hookInstantiate(@Nonnull T target) {
    for (Hooker hooker : hookers) {
      target = hooker.hookInstantiate(target);
    }
    return target;
  }

  @Override
  public Socket socketOpened(@Nonnull Socket socket) {
    for (Hooker hooker : hookers) {
      socket = hooker.socketOpened(socket);
    }
    return socket;
  }

  @Override
  public InputStream inputStreamOpened(@Nonnull InputStream in) {
    for (Hooker hooker : hookers) {
      in = hooker.inputStreamOpened(in);
    }
    return in;
  }

  @Override
  public OutputStream outputStreamOpened(@Nonnull OutputStream out) {
    for (Hooker hooker : hookers) {
      out = hooker.outputStreamOpened(out);
    }
    return out;
  }
}
