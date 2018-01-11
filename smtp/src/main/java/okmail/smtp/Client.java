package okmail.smtp;

import okmail.dns.DnsJavaMxDns;
import okmail.dns.MxDns;
import okmail.dns.SystemMxDns;
import okmail.mail.Mail;
import okmail.smtp.internal.hook.HookerComposite;

import javax.net.SocketFactory;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A mail client which allow user send e-mail or any attachment
 * using <a href="http://tools.ietf.org/html/rfc821">RFC 821</a>
 * SMTP protocol.
 */
public final class Client implements Session.SessionFactory {

  final MxDns mxDns;
  final SocketFactory socketFactory;
  final Hooker hooker;
  final Executor executor;

  /**
   * use all default settings to build an client
   */
  public Client() {
    this(new Builder());
  }

  /*package*/Client(Builder builder) {
    mxDns = builder.mxDns;
    socketFactory = builder.socketFactory;
    hooker = builder.hookers;
    executor = builder.executor;
  }

  public MxDns mxDns() {
    return mxDns;
  }

  public Hooker hooker() {
    return hooker;
  }

  public SocketFactory socketFactory() {
    return socketFactory;
  }

  @Override
  public Session newSession(Mail mail) {
    return RealSession.newRealSession(this, mail);
  }

  /**********************builder*************************/

  public static class Builder {

    MxDns mxDns = DnsJavaMxDns.isAvailable() ? new DnsJavaMxDns() : new SystemMxDns();
    SocketFactory socketFactory = SocketFactory.getDefault();
    HookerComposite hookers = new HookerComposite();
    Executor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<>(), command -> {
      Thread thread = new Thread(command);
      thread.setDaemon(true);
      return thread;
    });

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

    public Builder connectTimeout(long timeoutMillis) {
      return this;
    }


    public Builder readTimeout(long timeoutMillis) {
      return this;
    }

    public Builder writeTimeout(long timeoutMillis) {
      return this;
    }

    public Builder addHooker(Hooker hooker) {
      hookers.addHooker(hooker);
      return this;
    }

    public Builder executor(Executor executor) {
      this.executor = executor;
      return this;
    }

    public Client build() {
      return new Client(this);
    }
  }

}
