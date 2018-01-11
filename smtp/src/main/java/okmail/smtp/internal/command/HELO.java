package okmail.smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;
import okmail.Util;
import okmail.mail.Mail;
import okmail.smtp.Response;
import okmail.smtp.Server;

import java.io.IOException;

public class HELO extends Command {

  public static final String NAME = "HELO";
  private static final String EHLO_MESSAGE = "OkMail-0.0.1";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {

    sink.writeUtf8(NAME)
            .writeByte(Util.SP)
            .writeUtf8(EHLO_MESSAGE)
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();

    Response next = readResponse(source);
    if (next.code() != 250) {
      throwErrorCode(next.code(), "server not response 250 code");
    }
  }
}