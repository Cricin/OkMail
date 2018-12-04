package smtp.interceptor;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import smtp.Channel;
import smtp.Interceptor;
import smtp.SmtpClient;
import smtp.misc.Utils;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class StartTlsInterceptor implements Interceptor {
  private SSLSocketFactory factory = null;

  @Override
  public void intercept(Chain chain) throws IOException {
    final SmtpClient client = chain.client();
    if (client.useStartUls() && chain.serverOptions().startTlsSupported()) {
      Utils.d("detected starttls support, configuring");
      askForTls(chain.channel());

      Utils.d("tls connect start");
      initTls();
      RealInterceptorChain c = (RealInterceptorChain) chain;
      final Channel channel = chain.channel();
      final Socket socket = factory.createSocket(channel.socket(), channel.address()
          .getHostAddress(),
        client
          .defaultPort(), false);

      final BufferedSource source = Okio.buffer(Okio.source(socket));
      final BufferedSink sink = Okio.buffer(Okio.sink(socket));

      c.setChannel(new Channel() {
        @Nonnull
        @Override
        public Socket socket() {
          return socket;
        }

        @Nonnull
        @Override
        public BufferedSink sink() {
          return sink;
        }

        @Nonnull
        @Override
        public BufferedSource source() {
          return source;
        }

        @Nonnull
        @Override
        public InetAddress address() {
          return channel.address();
        }
      });
    }
    Utils.d("tls connect established");
    chain.proceed(chain.mail());
  }

  private void askForTls(Channel channel) throws IOException {
    BufferedSink bufferedSink = channel.sink();
    bufferedSink.writeUtf8("starttls").write(Utils.CRLF).flush();
    String line = Response.readLine(channel.source());
    int code = Response.parseCode(line);
    if (code != 220) {
      throw new IOException("error start tls");
    }
  }

  private void initTls() {
    if (factory != null) return;
    try {
      SSLContext tls = SSLContext.getInstance("TLS");
      tls.init(null, new TrustManager[]{new MyX509TrustManager()}, null);
      factory = tls.getSocketFactory();
    } catch (Exception e) {
      throw new AssertionError();
    }
  }

  static class MyX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws
      CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws
      CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }
  }


}
