package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Response;
import smtp.Server;
import smtp.mail.Headers;
import smtp.mail.Mail;
import smtp.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DATA extends Command {

  private static final String NAME = "DATA";
  private static final byte[] DATA_END = {Utils.CR, Utils.LF, Utils.DOT, Utils.CR, Utils.LF};

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {
    sink.writeUtf8(NAME)
        .writeByte(Utils.CR)
        .writeByte(Utils.LF)
            .flush();

    Response next = readResponse(source);
    int code = next.code();
    if (code != 354) {
      throwErrorCode(code, "send data command failed");
    }
    //headers
    String headers = buildHeaders(mail);
    sink.writeUtf8(headers)
        .writeByte(Utils.CR)
        .writeByte(Utils.LF);

    //content
    String capableString = escapeMailContent(mail);
    sink.writeUtf8(capableString);

    //end
    sink.write(DATA_END).flush();
    next = readResponse(source);
    code = next.code();
    if (code != 250) {
      throwErrorCode(code, "send mail body failed");
    }
  }

  private String escapeMailContent(Mail mail) {
    //todo escape string
    return mail.content();
  }

  private String buildHeaders(Mail mail) {
    Headers headers = mail.headers();
    StringBuilder out = new StringBuilder(256);
    Set<String> names = headers.names();
    for (String name : names) {
      List<String> list = headers.values(name);
      out.append(name).append(": ");
      for (String value : list) {
        out.append(value);
      }
      out.append("\r\n");
    }
    return out.toString();
  }

}
