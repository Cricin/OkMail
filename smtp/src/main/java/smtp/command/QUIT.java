package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Server;
import smtp.mail.Mail;
import smtp.util.Utils;

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
        .writeByte(Utils.CR)
        .writeByte(Utils.LF)
            .flush();
  }

}
