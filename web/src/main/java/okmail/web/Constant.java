package okmail.web;

import okmail.web.security.RejectMethod;

public final class Constant {

  /*Authentication Configuration*/
  public static final boolean AUTH_ENABLED = false;
  public static final long EXPIRE_PERIOD = 30 * 60 * 1000; //30 min
  public static final String EXPIRE_COOKIE_NAME = "OkMail-Expire";
  public static final RejectMethod DEFAULT = RejectMethod.REDIRECT;
  public static final int PASSWORD_MIN_LENGTH = 4;


  public static final int MAX_MAIL_ACCOUNT = 5;



}
