package smtp.auth;

import smtp.Channel;
import smtp.Session;

import java.io.IOException;

/**
 * AuthMethod represent a serverOptions authentication procedure when
 * serverOptions requires user auth before any mail being sent.
 */
public interface AuthMethod {

  /**
   * @return true if auth succeed
   */
  boolean auth(Authentication auth, Channel channel) throws IOException;

  AuthMethod AUTH = new AuthMethod() {
    @Override
    public boolean auth(Authentication auth, Channel channel) throws IOException {
      return false;
    }
  };

  AuthMethod PLAIN = new AuthMethod() {
    @Override
    public boolean auth(Authentication auth, Channel channel) throws IOException {
      return false;
    }
  };


}
