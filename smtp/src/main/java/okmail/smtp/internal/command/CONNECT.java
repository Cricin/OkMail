package okmail.smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;
import okmail.mail.Mail;
import okmail.smtp.Response;
import okmail.smtp.Server;

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
