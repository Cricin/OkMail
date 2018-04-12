package smtp.interceptor;


import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Channel;
import smtp.Interceptor;
import smtp.ServerOptions;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.List;

/**
 * this interceptor is for client exchange information with
 * server by smtp protocol
 */
public class ProtocolInterceptor implements Interceptor {
  private BufferedSource source;
  private BufferedSink sink;
  private Chain chain;

  @Override
  public void intercept(Chain chain) throws IOException {
    source = chain.channel().source();
    sink = chain.channel().sink();
    this.chain = chain;



    List<Mailbox> recipients = chain.mail().recipients();



    //SMTP MAIL
    Utils.d("sending mail command");
    doMail();

    Utils.d("sending rcpt command");
    for (Mailbox mailbox : recipients) {
      doRcpt(mailbox);
    }

    Utils.d("sending data command");
    doData();

    Utils.d("sending quit command");
    doQuit();

  }


  protected void doMail() throws IOException {
    final Mailbox from = chain.mail().from();
    sink.writeUtf8("MAIL FROM: ")
        .writeUtf8(from.name())
        .writeUtf8("@")
        .writeUtf8(from.host())
        .write(Utils.CRLF)
        .flush();
    String response = Response.readLine(source);
    if (Response.parseCode(response) != 250) throwException("MAIL", response);
  }


  protected void doRcpt(Mailbox mailbox) throws IOException {
    sink.writeUtf8("RCPT TO: ")
        .writeUtf8(mailbox.name())
        .writeUtf8("@")
        .writeUtf8(mailbox.host())
        .write(Utils.CRLF)
        .flush();
    String response = Response.readLine(source);
    if (Response.parseCode(response) != 250) throwException("RCPT", response);
  }

  private void doData() throws IOException {
    sink.writeUtf8("DATA")
        .write(Utils.CRLF)
        .flush();
    String response = source.readUtf8Line();
    int code = Response.parseCode(response);
    if (code != 354) throwException("DATA", response);

    //really send data here
    doDataInternal();

    code = Response.parseCode(response);
    if (code != 250) throwException("DATA", response);

  }

  private void doDataInternal() {
    chain.serverOptions().eightBitMimeSupported();


  }

  protected void doQuit() throws IOException {
    sink.writeUtf8("QUIT\r\n")
        .flush();
    //close all network resources
    final Channel channel = chain.channel();
    channel.sink().close();
    channel.source().close();
    channel.socket().close();
  }

  private static void throwException(String command, String response) throws IOException {
    throw new IOException("server denied " + command + ", response = " + response);
  }


}
