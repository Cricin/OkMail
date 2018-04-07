package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Response;
import smtp.Server;
import smtp.mail.Mail;
import smtp.util.Utils;

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
        .writeByte(Utils.SP)
            .writeUtf8(PARAM)
            .writeUtf8(mail.from().canonical())
        .writeByte(Utils.CR)
        .writeByte(Utils.LF)
            .flush();

    Response next = readResponse(source);
    final int code = next.code();
    if (code != 250) {
      throwErrorCode(code, next.message());
    }
  }


}
