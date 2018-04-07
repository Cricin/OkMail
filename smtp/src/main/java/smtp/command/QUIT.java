package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.ServerOptions;
import smtp.mail.Mail;
import smtp.misc.Utils;

import java.io.IOException;

public class QUIT extends Command {

  private static final String NAME = "QUIT";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           ServerOptions serverOptions) throws IOException {
    //do not care response, just send quit command
    sink.writeUtf8(NAME)
        .writeByte(Utils.CR)
        .writeByte(Utils.LF)
            .flush();
  }

}
