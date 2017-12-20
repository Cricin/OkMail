package smtp;

import smtp.internal.hook.HookerAdapter;
import smtp.internal.SystemMxDns;

import javax.net.SocketFactory;

/**
 * A mail client which allow user send e-mail or any attachment
 * using <a href="http://tools.ietf.org/html/rfc821">RFC 821</a>
 * SMTP protocol.
 */
public final class Client implements Session.SessionFactory {

  final MxDns mxDns;
  final SocketFactory socketFactory;
  final Hooker hooker;

  /**
   * use all default settings to build an client
   */
  public Client() {
    this(new Builder());
  }

  /*package*/Client(Builder builder) {
    mxDns = builder.mxDns;
    socketFactory = builder.socketFactory;
    hooker = builder.hooker;
  }

  public MxDns mxDns() {
    return mxDns;
  }

  public Session newSession(Mail mail) {
    return null;//TODO
  }


  /**********************builder*************************/

  public static class Builder {
    MxDns mxDns = new SystemMxDns();
    SocketFactory socketFactory = SocketFactory.getDefault();
    Hooker hooker = new HookerAdapter();

    public Builder() {
    }

    public Builder mxDns(MxDns mxDns) {
      this.mxDns = mxDns;
      return this;
    }

    public Builder socketFactory(SocketFactory factory) {
      this.socketFactory = factory;
      return this;
    }

    public Builder hooker(Hooker hooker) {
      this.hooker = hooker;
      return this;
    }

    public Client build() {
      return new Client(this);
    }
  }

}
