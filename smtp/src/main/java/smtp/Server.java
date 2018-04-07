package smtp;

import smtp.auth.AuthMethod;

import java.util.List;

public interface Server {

  boolean eightBitMimeSupported();

  List<AuthMethod> authMethods();

}
