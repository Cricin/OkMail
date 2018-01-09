package okmail.smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;
import okmail.smtp.Mail;
import okmail.smtp.Response;
import okmail.smtp.Server;
import okmail.smtp.internal.Util;

import java.io.IOException;

public class MAIL extends Command {

  private static final String NAME = "MAIL";
  private static final String PARAM = "FROM:";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {
    sink.writeUtf8(NAME)
            .writeByte(Util.SP)
            .writeUtf8(PARAM)
            .writeUtf8(mail.from().canonical())
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();

    Response next = readResponse(source);
    final int code = next.code();
    if (code != 250) {
      throwErrorCode(code, next.message());
    }
  }


}
