package smtp.internal;

import smtp.Smtp;

public class HELOProcessor implements Processor {

  public static final String HELO_CONTENT = Smtp.name + "-" + Smtp.versionName;

  @Override
  public void process(Chain chain) {

  }

}