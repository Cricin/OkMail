package smtp.internal.command;

import smtp.Interceptor;
import smtp.Session;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class Command implements Interceptor {

  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {

  }

  //for override purpose
  public abstract String commandName();


  public static List<Interceptor> newCommandsForSession(Session session) {
    List<Interceptor> commands = new LinkedList<>();
    commands.add(new Ehlo());
    commands.add(new Helo());
    commands.add(new Mail());
    commands.add(new Rcpt());
    commands.add(new Data());
    commands.add(new Quit());
    return commands;
  }

  protected void writeCommand() {

  }


}
