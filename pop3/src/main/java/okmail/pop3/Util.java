package okmail.pop3;

import okio.*;

public class Util {

  public static BufferedSink buffer(Sink sink){
    if(sink instanceof BufferedSink) return (BufferedSink) sink;
    return Okio.buffer(sink);
  }

  public static BufferedSource buffer(Source source) {
    if(source instanceof BufferedSource) return (BufferedSource) source;
    return Okio.buffer(source);
  }



}
