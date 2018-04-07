package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Response;
import smtp.Server;
import smtp.mail.Mail;

import java.io.IOException;

/**
 * used to receive first line response after
 * socket connected.
 */
public class CONNECT extends Command {

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {
    Response next = readResponse(source);
    final int code = next.code();
    if (code != 220) {
      throwErrorCode(code, "server maybe not ready");
    }
  }
}
