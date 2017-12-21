package smtp.internal.command;

import smtp.Interceptor;
import smtp.MailException;
import smtp.Response;
import smtp.Session;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public abstract class Command implements Interceptor {

  private smtp.Mail mail;

  @Override
  public final Response intercept(@Nonnull Chain chain) throws MailException {
    this.mail = chain.mail();
    doCommand();
    while (shouldRecall()) {
      doCommand();
    }
    return chain.proceed(mail);
  }

  //for override purpose
  public abstract String name();

  public boolean shouldRecall() {
    return false;
  }

  @Nonnull
  protected smtp.Mail mail() {
    return mail;
  }

  protected abstract void doCommand() throws MailException;

  public static List<Interceptor> newCommandsForSession(Session session/*reserved*/) {
    List<Interceptor> commands = new LinkedList<>();
    commands.add(new QUIT());
    commands.add(new DATA());
    commands.add(new RCPT());
    commands.add(new MAIL());
    commands.add(new HELO());
    commands.add(new EHLO());
    return commands;
  }

}
