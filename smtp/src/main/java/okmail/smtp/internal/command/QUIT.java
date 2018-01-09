package okmail.smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;
import okmail.smtp.Mail;
import okmail.smtp.Server;
import okmail.smtp.internal.Util;

import java.io.IOException;

public class QUIT extends Command {

  private static final String NAME = "QUIT";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {
    //do not care response, just send quit command
    sink.writeUtf8(NAME)
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();
  }

}
