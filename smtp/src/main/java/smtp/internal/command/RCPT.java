package smtp.internal.command;

import smtp.Mail;
import smtp.MailException;
import smtp.internal.io.Sink;
import smtp.internal.io.Source;

public class RCPT extends Command {
  @Override
  public String name() {
    return "RCPT";
  }

  @Override
  protected void doCommand(Sink sink, Source source) throws MailException {

  }

  @Override
  public boolean shouldRecall() {
    Mail mail = mail();




    return super.shouldRecall();
  }
}