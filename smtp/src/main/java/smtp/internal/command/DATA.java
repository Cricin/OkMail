package smtp.internal.command;


import smtp.MailException;

public class DATA extends Command {
  @Override
  public String name() {
    return "DATA";
  }

  @Override
  protected void doCommand() throws MailException {

  }
}
