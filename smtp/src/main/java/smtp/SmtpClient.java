package smtp;

import smtp.Channel.ChannelConnector;
import smtp.auth.Authentication;
import smtp.mail.Mail;
import smtp.mime.Encoding;
import smtp.net.SystemDns;
import smtp.util.Utils;

import javax.annotation.Nullable;
import javax.net.SocketFactory;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A mail client which allow user send e-mail or any attachment
 * using <a href="http://tools.ietf.org/html/rfc821">RFC 821</a>
 * SMTP protocol.
 */
public final class SmtpClient implements Session.SessionFactory {
  private final long connectTimeout;
  private final long readTimeout;
  private final long writeTimeout;
  private final Dns dns;
  private final SocketFactory socketFactory;
  private final ExecutorService executorService;
  private final ChannelConnector channelConnector;
  private final MailIdGenerator mailIdGenerator;
  private Encoding transferEncoding;
  private final int defaultPort;


  /**
   * use all default settings to build an client
   */
  public SmtpClient() {
    this(new Builder());
  }

  /*package*/SmtpClient(Builder builder) {
    defaultPort = builder.defaultPort;
    dns = builder.dns;
    socketFactory = builder.socketFactory;
    executorService = builder.executorService;
    connectTimeout = builder.connectTimeout;
    readTimeout = builder.readTimeout;
    writeTimeout = builder.writeTimeout;
    channelConnector = builder.channelConnector;
    mailIdGenerator = builder.mailIdGenerator;
    transferEncoding = builder.transferEncoding;
  }

  public int defaultPort() {
    return defaultPort;
  }

  public Dns dns() {
    return dns;
  }

  public SocketFactory socketFactory() {
    return socketFactory;
  }

  public ExecutorService executorService() {
    return executorService;
  }

  public long connectTimeout() {
    return connectTimeout;
  }


  public long readTimeout() {
    return readTimeout;
  }

  public long writeTimeout() {
    return writeTimeout;
  }

  public ChannelConnector channelConnector() {
    return channelConnector;
  }

  public MailIdGenerator mailIdGenerator() {
    return mailIdGenerator;
  }

  public Encoding transferEncoding() {
    return transferEncoding;
  }


  @Override

  public Session newSession(Mail mail, @Nullable InetAddress serverAddress, Authentication auth) {
    return RealSession.newRealSession(this, mail, serverAddress, auth);
  }

  public Builder newBuilder() {
    Builder out = new Builder();
    out.defaultPort = defaultPort;
    out.connectTimeout = connectTimeout;
    out.readTimeout = readTimeout;
    out.writeTimeout = writeTimeout;
    out.dns = dns;
    out.socketFactory = socketFactory;
    out.executorService = executorService;
    out.channelConnector = channelConnector;
    out.mailIdGenerator = mailIdGenerator;
    out.transferEncoding = transferEncoding;
    return out;
  }

  /**********************builder*************************/

  public static class Builder {
    private int defaultPort = 25;
    long connectTimeout = 0L;
    long readTimeout = 0L;
    long writeTimeout = 0L;
    Dns dns = new SystemDns();
    ChannelConnector channelConnector = Channel.DIRECT;
    SocketFactory socketFactory = SocketFactory.getDefault();
    MailIdGenerator mailIdGenerator = MailIdGenerator.DEFAULT;
    Encoding transferEncoding;
    ExecutorService executorService = new ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        new SynchronousQueue<Runnable>(),
        Utils.makeThreadFactory("OkMail", false));

    public Builder() {
    }

    public Builder defaultPort(int port) {
      this.defaultPort = port;
      return this;
    }

    public Builder dns(Dns dns) {
      this.dns = dns;
      return this;
    }

    public Builder socketFactory(SocketFactory factory) {
      this.socketFactory = factory;
      return this;
    }

    public Builder connectTimeout(long timeoutMillis) {
      connectTimeout = Utils.checkNotNegative(timeoutMillis);
      return this;
    }


    public Builder readTimeout(long timeoutMillis) {
      readTimeout = Utils.checkNotNegative(timeoutMillis);
      return this;
    }

    public Builder writeTimeout(long timeoutMillis) {
      writeTimeout = Utils.checkNotNegative(timeoutMillis);
      return this;
    }

    public Builder executor(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
    }

    public Builder channelConnector(ChannelConnector connector) {
      this.channelConnector = connector;
      return this;
    }

    public Builder mailIdGenerator(MailIdGenerator generator) {
      this.mailIdGenerator = generator;
      return this;
    }

    public Builder defaultTransferCodec(Encoding encoding) {
      this.transferEncoding = encoding;
      return this;
    }

    public SmtpClient build() {
      return new SmtpClient(this);
    }
  }

}
