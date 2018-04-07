package smtp.auth;

import smtp.Session;

/**
 * AuthMethod represent a server authentication procedure when
 * server requires user auth before any mail being sent.
 */
public interface AuthMethod {

  /**
   * @param session a session that sending an mail.
   * @return true if auth succeed
   */
  boolean auth(Session session);

  AuthMethod AUTH = new AuthMethod() {
    @Override
    public boolean auth(Session session) {
      return false;
    }
  };

  AuthMethod PLAIN = new AuthMethod() {
    @Override
    public boolean auth(Session session) {
      return false;
    }
  };


  AuthMethod CRAM_MD5 = new AuthMethod() {
    @Override
    public boolean auth(Session session) {
      return false;
    }
  };


}
