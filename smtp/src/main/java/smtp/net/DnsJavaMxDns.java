package smtp.net;

import org.xbill.DNS.*;
import smtp.Dns;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * dnsjava implementations to resolve dns mx records
 */
public final class DnsJavaMxDns implements Dns {

  @Override
  public List<InetAddress> lookupByMxRecord(@Nonnull String hostname) throws UnknownHostException {
    return lookupInternal(hostname, Type.MX);
  }

  @Override
  public List<InetAddress> lookupByARecord(String hostname) throws UnknownHostException {
    return lookupInternal(hostname, Type.A);
  }

  private List<InetAddress> lookupInternal(String hostname, int type) throws UnknownHostException {
    List<InetAddress> out = new LinkedList<>();
    Class<?> recordType = type == Type.A ? ARecord.class : MXRecord.class;
    try {
      Lookup mxLookup = new Lookup(Name.fromString(hostname), type);
      mxLookup.run();
      if (mxLookup.getResult() == Lookup.SUCCESSFUL) {
        Record[] records = mxLookup.getAnswers();
        for (Record record : records) {
          if (recordType.isInstance(record)) {
            Name name = record.getAdditionalName(); //mx record point to an A record
            out.addAll(Arrays.asList(InetAddress.getAllByName(String.valueOf(name))));
          }
        }
      }
    } catch (Exception e) {
      //ignore
    }
    if (out.isEmpty()) {
      throw new UnknownHostException("Could not resolve DNS mx record of host: " + hostname);
    }
    return out;
  }

  //test if we have dnsjava in classpath
  public static boolean isAvailable() {
    try {
      Class.forName("org.xbill.DNS.MXRecord");
    } catch (ClassNotFoundException ignore) {
      return false;
    }
    return true;
  }

}
