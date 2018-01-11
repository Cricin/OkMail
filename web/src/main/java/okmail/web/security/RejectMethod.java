package okmail.web.security;

public enum RejectMethod {
  /**
   * Redirect url to the authentication page
   */
  REDIRECT,
  /**
   * Http Error with showing error page
   */
  ERROR,

}
