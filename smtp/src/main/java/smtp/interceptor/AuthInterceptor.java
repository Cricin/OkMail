package smtp.interceptor;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Interceptor;
import smtp.Version;
import smtp.auth.Authentication;
import smtp.util.Utils;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
  public AuthInterceptor(Authentication auth) {

  }

  @Override
  public void intercept(Chain chain) throws IOException {
    final BufferedSink sink = chain.channel().sink();
    final BufferedSource source = chain.channel().source();

    //write ehlo command
    sink.writeUtf8("EHLO ")
        .writeUtf8(Version.VERSION_TEXT)
        .writeByte(Utils.CR)
        .writeByte(Utils.LF)
        .flush();


  }
}
