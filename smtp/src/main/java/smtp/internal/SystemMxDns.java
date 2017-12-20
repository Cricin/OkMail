package smtp.internal;

import smtp.MxDns;

import javax.annotation.Nonnull;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * default dns mx-record resolver
 */
public final class SystemMxDns implements MxDns {

  @SuppressWarnings("unchecked")
  private static final List<InetAddress> EMPTY = Collections.EMPTY_LIST;
  private static final String[] MX_RECORD = {"MX"};

  private InitialDirContext context;
  private final HashMap<String, List<InetAddress>> hostAddressCache;

  public SystemMxDns() {
    hostAddressCache = new HashMap<>(32);
    try {
      context = new InitialDirContext();
    } catch (NamingException ignored) {
    }
  }

  @Override
  @Nonnull
  public List<InetAddress> lookup(@Nonnull String hostname) throws UnknownHostException {
    List<InetAddress> result = hostAddressCache.get(hostname);
    if (result != null) {
      return result;
    }
    try {
      String[] record = lookupMxRecord(hostname);
      result = new LinkedList<>();
      for (String realHost : record) {
        result.addAll(Arrays.asList(InetAddress.getAllByName(realHost)));
      }
      putAddressIntoCache(hostname, result);
      return result;
    } catch (NamingException e) {
      throw new UnknownHostException("Could not resolve DNS mx record of host:" + hostname);
    } catch (UnknownHostException e) {
      putAddressIntoCache(hostname, EMPTY);
      throw e;
    }
  }

  private void putAddressIntoCache(String hostname, List<InetAddress> addresses) {
    synchronized (hostAddressCache) {
      if (!hostAddressCache.containsKey(hostname)) {
        hostAddressCache.put(hostname, addresses);
      }
    }
  }

  private String[] lookupMxRecord(String hostname) throws NamingException {
    Attributes attributes = context.getAttributes("dns:/" + hostname, MX_RECORD);
    Attribute attributeMX = attributes.get("MX");

    if (attributeMX == null) {
      return (new String[]{hostname});
    }

    // split MX RRs into Preference Values(pvhn[0]) and Host Names(pvhn[1])
    String[][] pvhn = new String[attributeMX.size()][2];
    for (int i = 0; i < attributeMX.size(); i++) {
      pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
    }

    // sort the MX RRs by RR value (lower is preferred)
    Arrays.sort(pvhn, new Comparator<String[]>() {
      public int compare(String[] o1, String[] o2) {
        return (Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]));
      }
    });

    // put sorted host names in an array, get rid of any trailing '.'
    String[] sortedHostNames = new String[pvhn.length];
    for (int i = 0; i < pvhn.length; i++) {
      sortedHostNames[i] = pvhn[i][1].endsWith(".") ?
              pvhn[i][1].substring(0, pvhn[i][1].length() - 1) : pvhn[i][1];
    }
    return sortedHostNames;
  }

}
