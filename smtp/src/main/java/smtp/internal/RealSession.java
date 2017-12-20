package smtp.internal;

import smtp.Session;

public final class RealSession implements Session {
  @Override
  public boolean write() {
    return false;
  }
}
