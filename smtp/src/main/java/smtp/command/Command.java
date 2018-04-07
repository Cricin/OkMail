package smtp.command;

import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Interceptor;
import smtp.misc.Response;
import smtp.ServerOptions;
import smtp.Session;
import smtp.interceptor.RealInterceptorChain;
import smtp.mail.Mail;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class Command implements Interceptor {

  @Override
  public final void intercept(@Nonnull Chain chain) throws IOException {
    Mail mail = chain.mail();
    RealInterceptorChain realChain = (RealInterceptorChain) chain;
    final BufferedSink sink = (BufferedSink) realChain.channel().sink();
    final BufferedSource source = (BufferedSource) realChain.channel().source();
    ServerOptions serverOptions = realChain.serverOptions();
    doCommand(chain, sink, source, mail, serverOptions);
    chain.proceed(mail);
  }

  protected abstract void doCommand(Chain chain,
                                    BufferedSink sink,
                                    BufferedSource source,
                                    Mail mail,
                                    ServerOptions serverOptions) throws IOException;

  protected void throwErrorCode(int code, String msg) throws IOException {
    throw new IOException("Code Not Valid: " + code + "{ " + msg + " }");
  }

  protected Response readResponse(BufferedSource source) throws IOException {
//    String rawResponse = source.readUtf8LineStrict();
//    if (rawResponse.charAt(3) == Utils.SP) {
//      return Response.single(Utils.readCode(rawResponse), Utils.readMessage(rawResponse));
//    } else if (rawResponse.charAt(3) == Utils.DASH) {
//      List<String> lines = new LinkedList<>();
//      lines.add(rawResponse);
//      while (true) {
//        rawResponse = source.readUtf8LineStrict();
//        lines.add(rawResponse);
//        if (rawResponse.charAt(3) == Util.SP) break;
//      }
//      int[] codes = new int[lines.size()];
//      String[] messages = new String[lines.size()];
//      for (int i = 0; i < lines.size(); i++) {
//        String line = lines.get(i);
//        codes[i] = Util.readCode(line);
//        messages[i] = Util.readMessage(line);
//      }
//      return Response.multi(codes, messages);
//    }
    throw new IOException("expected <dash> or <space> in response");
  }

  public static List<Command> newCommandsForSession(Session session/*reserved*/) {
    List<Command> commands = new LinkedList<>();
    commands.add(new MAIL());
    commands.add(new RCPT());
    commands.add(new DATA());
    commands.add(new QUIT());
    return commands;
  }

}
