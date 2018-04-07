package okmail.pop3;


import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okmail.Authentication;
import okmail.network.Channel;
import okmail.network.NetWorkSetting;

import java.io.IOException;

public class Connection {

  private final Channel channel;
  private final BufferedSource source;
  private final BufferedSink sink;

  private Connection(Channel channel, Sink sink, Source source) {
    this.channel = channel;
    this.sink = Okio.buffer(sink);
    this.source = Okio.buffer(source);
  }

  public Channel channel() {
    return channel;
  }

  public PopResponse sendCommand(POP3Command what, String... args) throws IOException {
    if (channel.socket().isClosed()) throw new IOException("socket closed");
    what.writeToSink(sink, args);
    return null;
  }

  public static Connection establish(NetWorkSetting setting,
                                     String host,
                                     int port) throws IOException {
   return null;
  }

  public void authWith(Authentication auth) throws IOException {

  }
}
