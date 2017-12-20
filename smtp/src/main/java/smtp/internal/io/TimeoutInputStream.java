package smtp.internal.io;

import java.io.IOException;
import java.io.InputStream;

public class TimeoutInputStream extends InputStream {

  @Override
  public int read() throws IOException {
    return 0;
  }


  private static class WatchDog extends Thread {

    WatchDog() {
      super("OkMail Timeout Watchdog");
    }


  }

}
