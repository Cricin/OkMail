package smtp.internal.command;

import smtp.MailException;
import smtp.internal.io.Sink;
import smtp.internal.io.Source;

public class EHLO extends Command {
  @Override
  public String name() {
    return "EHLO";
  }

  @Override
  protected void doCommand(Sink sink, Source source) throws MailException {

  }
}
