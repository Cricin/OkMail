package smtp;

import smtp.misc.SystemDns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * A domain name service that resolves IP addresses for host names.
 * Most applications will use the {@link SystemDns DNS service}.
 * Some applications may provide their own implementation to use a different DNS serverOptions,
 * to prefer IPv6 addresses,to prefer IPv4 addresses, or to force a specific known IP
 * address.
 * <p>
 * <p>Implementations of this interface must be safe for concurrent use.
 */
public interface Dns {

  /**
   * Returns the IP addresses of {@code hostname}, in the order they will be attempted by Smtp. If
   * a connection to an address fails, Smtp will retry the connection with the next address until
   * either a connection is made, the set of IP addresses is exhausted, or a limit is exceeded.
   * <p>
   * note: the hostname is associated with an email address, eg: xxx@host.com, then the hostname is
   * host.com
   */
  List<InetAddress> lookupByMxRecord(String hostname) throws UnknownHostException;

  List<InetAddress> lookupByARecord(String hostname) throws UnknownHostException;

}
