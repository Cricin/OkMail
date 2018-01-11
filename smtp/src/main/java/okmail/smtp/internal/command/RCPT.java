package okmail.smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;
import okmail.Util;
import okmail.mail.Mail;
import okmail.mail.Mailbox;
import okmail.smtp.Response;
import okmail.smtp.Server;

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
              .writeByte(Util.SP)
              .writeUtf8(PARAM)
              .writeUtf8(to.canonical())
              .writeByte(Util.CR)
              .writeByte(Util.LF)
              .flush();

      Response r = readResponse(source);
      final int code = r.code();
      if (code != 250) {
        throwErrorCode(code, "send mail command failed");
      }
    }
  }

}