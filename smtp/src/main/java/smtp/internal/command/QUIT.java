package smtp.internal.command;

import smtp.MailException;

public class QUIT extends Command {

  @Override
  public String name() {
    return "QUIT";
  }

  @Override
  protected void doCommand() throws MailException {

  }
}
