package smtp.interceptor;

import smtp.Channel;
import smtp.Channel.ChannelConnector;
import smtp.Dns;
import smtp.Interceptor;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.misc.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


/**
 * create a transfer channel for mail delivery
 *
 * @see SocketFactory
 * @see Channel
 * @see ChannelConnector
 */
public class ConnectInterceptor implements Interceptor {

  @Nullable
  private InetAddress serverAddress;

  public ConnectInterceptor(@Nullable InetAddress serverAddress) {
    this.serverAddress = serverAddress;
  }

  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {
    final ChannelConnector connector = chain.client().channelConnector();
    final Dns dns = chain.client().dns();
    final Mail mail = chain.mail();

    Channel channel = null;

    if (serverAddress != null) {//if user specifies an exactly serverOptions address
      Utils.d("connecting to server: address "+ serverAddress);
      channel = connector.connect(chain.client(), serverAddress);
    } else { //dns lookup for serverOptions address according to From: header
      Utils.d("no server address specified, DNS query started");
      final Mailbox from = mail.from();
      List<InetAddress> addresses;
      try {
        addresses = dns.lookupByMxRecord(from.host());
      } catch (UnknownHostException e) {
        throw new IOException("can not find any dns mx record address with host: " + from.host());
      }

      for (int i = 0; i < addresses.size(); i++) {
        try {
          channel = connector.connect(chain.client(), addresses.get(i));
          break;
        } catch (java.io.IOException ignore) {
        }
      }
    }

    if (channel == null) {
      throw new IOException("error connecting to server");
    }

    ((RealInterceptorChain) chain).setChannel(channel);
    Utils.d("server connection established!");
    try {
      chain.proceed(mail);
    } finally {
      //close any socket resource...
      Utils.closeQuietly(channel.sink());
      Utils.closeQuietly(channel.source());
      Utils.closeQuietly(channel.socket());
    }
  }
}
