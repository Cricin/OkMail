package smtp;

import smtp.internal.dns.SystemMxDns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * A domain name service that resolves IP addresses for host names by DNS MX RECORD.
 * Most applications will use the {@link SystemMxDns DNS service}.
 * Some applications may provide their own implementation to use a different DNS server,
 * to prefer IPv6 addresses,to prefer IPv4 addresses, or to force a specific known IP
 * address.
 * <p>
 * <p>Implementations of this interface must be safe for concurrent use.
 */
public interface MxDns {

  /**
   * Returns the IP addresses of {@code hostname}, in the order they will be attempted by Smtp. If
   * a connection to an address fails, Smtp will retry the connection with the next address until
   * either a connection is made, the set of IP addresses is exhausted, or a limit is exceeded.
   * <p>
   * note: the hostname is associated with an email address, eg: xxx@qq.com, then the hostname is
   * qq.com
   */
  List<InetAddress> lookup(String hostname) throws UnknownHostException;

}
