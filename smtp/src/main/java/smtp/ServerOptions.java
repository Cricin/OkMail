package smtp;

import smtp.auth.AuthMethod;

import java.util.List;

public interface ServerOptions {

  boolean eightBitMimeSupported();

  boolean startTlsSupported();

  boolean pipeLiningSupported();

  List<AuthMethod> authMethods();

}
