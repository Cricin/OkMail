package smtp.internal.command;

import smtp.MailException;

public class RCPT extends Command {
  @Override
  public String name() {
    return "RCPT";
  }

  @Override
  protected void doCommand() throws MailException {

  }

  @Override
  public boolean shouldRecall() {
    return super.shouldRecall();
  }
}