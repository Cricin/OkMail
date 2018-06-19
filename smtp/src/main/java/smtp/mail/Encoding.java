package smtp.mail;

import okio.BufferedSink;
import okio.Okio;

/** content transfer encodings for mime*/
public enum Encoding {

  /** see RFC 2045*/
  QUOTED_PRINTABLE("Quoted-Printable") {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return null;
    }
  },

  /** see RFC 2045*/
  BASE64("Base64") {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return Okio.buffer(new BSink(sink, lineLength));
    }
  },

  /** see RFC 6152*/
  EIGHT_BIT("8Bit") {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return sink;
    }
  },

  /** binary write everything into th sink directly */
  BINARY("Binary") {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return sink;
    }
  },

  /***/
  SEVEN_BIT("7Bit") {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return null;
    }
  },

  AUTO_SELECT("auto-select") {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      throw new RuntimeException("not implemented");
    }
  };
  String name;
  public abstract BufferedSink from(BufferedSink sink, int lineLength);
  Encoding(String name){
    this.name = name;
  }

  public String encodingName(){
    return name;
  }

}