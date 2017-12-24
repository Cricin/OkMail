package smtp.internal.command;

import okio.BufferedSink;
import okio.BufferedSource;

import smtp.Mail;
import smtp.Response;
import smtp.Server;
import smtp.internal.Util;
import smtp.internal.Base64;

import java.io.IOException;

public class AUTH extends Command {

  private static final String NAME = "AUTH";
  private static final String PARAM = "LOGIN";

  @Override
  protected void doCommand(Chain chain,
                           BufferedSink sink,
                           BufferedSource source,
                           Mail mail,
                           Server server) throws IOException {


    tryLogin(sink, source, mail.from().name(), mail.password());

    Response loginResponse = readResponse(source);

    int code = loginResponse.code();
    if (code == 235) {
      return;
    }

    tryLogin(sink, source, mail.from().mailbox(), mail.password());

    loginResponse = readResponse(source);
    code = loginResponse.code();
    if (code != 235) {
      throwErrorCode(code, "login failed");
    }
  }

  public void tryLogin(BufferedSink sink, BufferedSource source,
                       String name, String password) throws IOException {
    sink.writeUtf8(NAME)
            .writeByte(Util.SP)
            .writeUtf8(PARAM)
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();

    Response userNameResponse = readResponse(source);
    int code = userNameResponse.code();
    if (code != 334) {
      throwErrorCode(code, "username input not ready");
    }

    sink.writeUtf8(Base64.encode(name.getBytes()))
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();

    Response passwordResponse = readResponse(source);
    code = passwordResponse.code();
    if (code != 334) {
      throwErrorCode(code, "password input not ready");
    }

    sink.writeUtf8(Base64.encode(password.getBytes()))
            .writeByte(Util.CR)
            .writeByte(Util.LF)
            .flush();
  }

}
