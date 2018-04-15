package smtp.mail;

import okio.BufferedSink;
import okio.Okio;

/** content transfer encodings for mime*/
public enum Encoding {

  /** see RFC 2045*/
  QUOTED_PRINTABLE {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return null;
    }
  },

  /** see RFC 2045*/
  BASE64 {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return Okio.buffer(new BSink(sink, lineLength));
    }
  },

  /** see RFC 6152*/
  EIGHT_BIT {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return sink;
    }
  },

  /** binary write everything into th sink directly */
  BINARY {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return sink;
    }
  },

  /***/
  SEVEN_BIT {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      return null;
    }
  },

  AUTO_SELECT {
    @Override
    public BufferedSink from(BufferedSink sink, int lineLength) {
      throw new RuntimeException("not implemented");
    }
  };

  public abstract BufferedSink from(BufferedSink sink, int lineLength);


}