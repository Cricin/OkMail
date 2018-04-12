package smtp.interceptor;


import okio.BufferedSink;
import okio.BufferedSource;
import smtp.Channel;
import smtp.Interceptor;
import smtp.command.Command;
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
  private Mail mail;
  private Chain chain;


  public ProtocolInterceptor(List<Command> smtpCommand) {

  }

  @Override
  public void intercept(Chain chain) throws IOException {
    final BufferedSource source = chain.channel().source();
    final BufferedSink sink = chain.channel().sink();
    final List<Mailbox> recipients = chain.mail().recipients();


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
    final Mailbox from = mail.from();
    sink.writeUtf8("MAIL FROM: ")
        .writeUtf8(from.name())
        .writeUtf8("@")
        .writeUtf8(from.host())
        .write(Utils.CRLF)
        .flush();


  }


  protected void doRcpt(Mailbox mailbox) throws IOException {
    sink.writeUtf8("RCPT TO: ")
        .writeUtf8(mailbox.name())
        .writeUtf8("@")
        .writeUtf8(mailbox.host())
        .write(Utils.CRLF)
        .flush();

  }

  private void doData() throws IOException {
    sink.writeUtf8("DATA")
        .write(Utils.CRLF)
        .flush();
    int code = Utils.parseCode(source.readUtf8Line());




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

}
