package smtp.internal.command;

import smtp.Interceptor;

import javax.annotation.Nonnull;
import java.io.IOException;

public class Quit extends Command {
  @Override
  public void intercept(@Nonnull Chain chain) throws IOException {

  }

  @Override
  public String commandName() {
    return null;
  }
}
