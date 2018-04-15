package smtp.interceptor;


import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Channel;
import smtp.Interceptor;
import smtp.SmtpClient;
import smtp.mail.*;
import smtp.misc.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * this interceptor is for client exchange information with
 * server by smtp protocol
 */
public class ProtocolInterceptor implements Interceptor {
  BufferedSource source;
  BufferedSink sink;
  Chain chain;

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
        .writeUtf8(from.canonicalAddress())
        .write(Utils.CRLF)
        .flush();
    String response = Response.readLine(source);
    if (Response.parseCode(response) != 250) throwException("MAIL", response);
  }


  protected void doRcpt(Mailbox mailbox) throws IOException {
    sink.writeUtf8("RCPT TO: ")
        .writeUtf8(mailbox.canonicalAddress())
        .write(Utils.CRLF)
        .flush();
    String response = Response.readLine(source);
    if (Response.parseCode(response) != 250) throwException("RCPT", response);
  }

  private void doData() throws IOException {
    sink.writeUtf8("DATA")
        .write(Utils.CRLF)
        .flush();
    String response = Response.readLine(source);
    int code = Response.parseCode(response);
    if (code != 354) throwException("DATA", response);

    //really send data here
    doDataInternal();

    response = Response.readLine(source);
    code = Response.parseCode(response);
    if (code != 250) throwException("DATA", response);

  }

  protected void doDataInternal() throws IOException {
    //write headers
    final TransferSpec spec = chain.transferSpec();

    SmtpHeader.writeAllHeaders(sink,
        chain.mail().headers(),
        spec);

    SmtpHeader.writeMailbox(sink, spec, "From", Collections.singletonList(chain.mail().from()));
    SmtpHeader.writeMailbox(sink, spec,"To", chain.mail().recipients());
    SmtpHeader.writeMailbox(sink, spec,"Cc", chain.mail().cc());
    SmtpHeader.writeMailbox(sink, spec,"Bcc", chain.mail().bcc());

    sink.write(Utils.CRLF);

    SmtpBody.writeBodies(sink, chain.mail().body(), spec);

    sink.writeUtf8(".\r\n");
    sink.flush();
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
