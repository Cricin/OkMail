package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Response;
import smtp.Server;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.util.Utils;

import java.io.IOException;
import java.util.List;

public class RCPT extends Command {

  private static final String NAME = "RCPT";
  private static final String PARAM = "TO:";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {
    final List<Mailbox> recipients = mail.recipients();
    for (int i = 0; i < recipients.size(); i++) {
      final Mailbox to = recipients.get(i);

      sink.writeUtf8(NAME)
          .writeByte(Utils.SP)
              .writeUtf8(PARAM)
              .writeUtf8(to.canonical())
          .writeByte(Utils.CR)
          .writeByte(Utils.LF)
              .flush();

      Response r = readResponse(source);
      final int code = r.code();
      if (code != 250) {
        throwErrorCode(code, "send mail command failed");
      }
    }
  }

}