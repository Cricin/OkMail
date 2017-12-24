package smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Headers;
import smtp.Mail;
import smtp.Response;
import smtp.Server;
import smtp.internal.Util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DATA extends Command {

  private static final String NAME = "DATA";
  private static final byte[] DATA_END = {Util.CR, Util.LF, Util.DOT, Util.CR, Util.LF};

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {
    sink.writeUtf8(NAME)
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();

    Response next = readResponse(source);
    int code = next.code();
    if (code != 354) {
      throwErrorCode(code, "send data command failed");
    }
    //headers
    String headers = buildHeaders(mail);
    sink.writeUtf8(headers)
            .writeByte(Util.CR)
            .writeByte(Util.LF);

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
