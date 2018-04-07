package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.misc.Response;
import smtp.ServerOptions;
import smtp.mail.Mail;
import smtp.misc.Utils;

import java.io.IOException;

public class MAIL extends Command {

  private static final String NAME = "MAIL";
  private static final String PARAM = "FROM:";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           ServerOptions serverOptions) throws IOException {
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
