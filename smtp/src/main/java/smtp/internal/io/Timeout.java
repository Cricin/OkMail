package smtp.internal.io;

import java.io.Closeable;

public class Timeout {

  Timeout head;


  public void enter(Closeable target) {

  }

  public void exit() {

  }

  static final class Watchdog extends Thread {

    Watchdog() {
      super("OkMail Timeout Watchdog");
    }

    @Override
    public void run() {
    }
  }

}
