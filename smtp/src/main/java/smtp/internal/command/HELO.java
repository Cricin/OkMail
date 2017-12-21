package smtp.internal.command;

import smtp.MailException;

public class HELO extends Command {

  @Override
  public String name() {
    return "HELO";
  }

  @Override
  protected void doCommand() throws MailException {

  }
}