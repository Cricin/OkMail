package okmail.demo;

import okio.BufferedSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import static java.lang.System.out;

public final class Main {
  public static void main(String[] args) throws IOException {
    addClasspathIfNeeded();
    printUsages();
    BufferedSource in = Okio.buffer(Okio.source(System.in));
    String input;
    while (true) {
      input = in.readUtf8Line();
      int code = processInput(input);
      switch (code) {
        case 1:
          DumpMail.main(null);
          break;
        case 2:
          SendPlainText.main(null);
          break;
        case 3:
          SendHtml.main(null);
          break;
        case 4:
          SendAttachment.main(null);
          break;
        case 5:
          return;
        default:
          break;
      }
      printUsages();
    }
  }

  private static int processInput(String input) {
    if (input == null) {
      return -1;
    }
    input = input.trim();
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private static void printUsages() {
    System.out.println("\n\n");
    out.println("选择功能:");
    out.println("   <1>   dump邮件");
    out.println("   <2>   发送纯文本邮件");
    out.println("   <3>   发送富文本邮件");
    out.println("   <4>   发送附件");
    out.println("   <5>   exit");
  }

  private static void addClasspathIfNeeded() {
    try {
      Class.forName("smtp.SmtpClient");
    } catch (ClassNotFoundException e) {
      File smtpJar = new File("smtp.jar");
      File okio = new File("okio.jar");
      if (!smtpJar.exists() || !okio.exists()) {
        throw new RuntimeException("没有找到OkMail");
      }
      ClassLoader loader = Main.class.getClassLoader();
      try {
        Method method = loader.getClass().getSuperclass().getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(loader, smtpJar.toURI().toURL());
        method.invoke(loader, okio.toURI().toURL());
        out.println("----> OkMail已加载！");
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }
}
