package smtp.auth;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Channel;
import smtp.misc.Base64;
import smtp.misc.Utils;

import java.io.IOException;

/**
 * AuthMethod represent a serverOptions authentication procedure when
 * serverOptions requires user auth before any mail being sent.
 */
public interface AuthMethod {

  /**
   * @return true if auth succeed
   */
  boolean auth(Authentication auth, Channel channel) throws IOException;

  AuthMethod AUTH = new AuthMethod() {
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean auth(Authentication auth, Channel channel) throws IOException {
      final BufferedSink sink = channel.sink();
      final BufferedSource source = channel.source();
      //send auth login
      sink.writeUtf8("auth login")
          .write(Utils.CRLF)
          .flush();
      int code = Utils.parseCode(source.readUtf8Line());

      //if server want continue, then send our key
      if (code != 334) return false;
      sink.write(Base64.decode(auth.key))
          .write(Utils.CRLF)
          .flush();

      //if server want continue, then send our token
      code = Utils.parseCode(source.readUtf8Line());
      if (code != 334) return false;
      sink.write(Base64.decode(auth.token))
          .write(Utils.CRLF)
          .flush();
      code = Utils.parseCode(source.readUtf8Line());
      return code == 235;
    }
  };

  AuthMethod PLAIN = new AuthMethod() {
    @Override
    public boolean auth(Authentication auth, Channel channel) throws IOException {
      final BufferedSink sink = channel.sink();
      final BufferedSource source = channel.source();
      //send auth plain
      sink.writeUtf8("auth plain")
          .write(Utils.CRLF)
          .flush();
      int code = Utils.parseCode(source.readUtf8Line());
      if (code != 334) return false;

      //if server want continue, then send our plain auth
      sink.writeByte(0/*ASCII null*/).writeUtf8(auth.key)
          .writeByte(0/*ASCII null*/).writeUtf8(auth.token)
          .write(Utils.CRLF).flush();
      return false;
    }
  };


}
