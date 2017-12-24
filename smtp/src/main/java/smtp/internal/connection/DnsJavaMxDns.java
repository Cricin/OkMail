package smtp.internal.connection;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import smtp.MxDns;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * dnsjava implementations to resolve dns mx records
 */
public final class DnsJavaMxDns implements MxDns {

  @Override
  public List<InetAddress> lookup(@Nonnull String hostname) throws UnknownHostException {
    List<InetAddress> out = new LinkedList<>();
    try {
      Lookup mxLookup = new Lookup(Name.fromString(hostname), Type.MX);
      mxLookup.run();
      if (mxLookup.getResult() == Lookup.SUCCESSFUL) {
        Record[] records = mxLookup.getAnswers();
        for (Record record : records) {
          if (record instanceof MXRecord) {
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
