package okmail.web.security;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class AddressAuthInfo {

  /*package*/static final AddressAuthInfo EMPTY = new AddressAuthInfo(-1, "");

  /*package*/AddressAuthInfo(long lastGrantTime, String value) {
    lastGrant = lastGrantTime;
    cookieValue = value;
  }

  long lastGrant;
  @NonNull
  String cookieValue;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj instanceof AddressAuthInfo) {
      AddressAuthInfo o = (AddressAuthInfo) obj;
      return Objects.equals(o.cookieValue, cookieValue) && o.lastGrant == lastGrant;
    }
    return false;
  }
}
