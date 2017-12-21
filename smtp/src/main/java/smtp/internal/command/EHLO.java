package smtp.internal.command;

import smtp.MailException;

public class EHLO extends Command {
  @Override
  public String name() {
    return "EHLO";
  }

  @Override
  protected void doCommand() throws MailException {

  }
}
