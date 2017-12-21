package smtp.internal.command;

import smtp.MailException;

public class MAIL extends Command {
  @Override
  public String name() {
    return "MAIL";
  }

  @Override
  protected void doCommand() throws MailException {

  }
}
