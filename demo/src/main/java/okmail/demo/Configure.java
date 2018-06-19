package okmail.demo;

import smtp.auth.Authentication;
import smtp.mail.Mailbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Configure {

  static final Properties properties = new Properties();

  static {
    try {
      properties.load(new FileInputStream("mail.properties"));
    } catch (IOException e) {
      throw new RuntimeException("无法加载配置文件mail.properties");
    }
  }

  public static Mailbox getFrom() {
    return Mailbox.parse(properties.getProperty("mail.accountName") + "<" + properties
        .getProperty("mail.account") + ">");
  }

  public static Mailbox getTo() {
    return Mailbox.parse(properties.getProperty("mail.rcptName") + "<" + properties.getProperty
        ("mail.rcpt") + ">");
  }

  public static InetAddress getAddress() {
    try {
      return InetAddress.getByName(properties.getProperty("mail.serverAddress"));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return null;
  }


  public static String getAccount(){
    return properties.getProperty("mail.account");
  }

  public static String getToken(){
    return properties.getProperty("mail.token");
  }

  public static Authentication getAuth(){
    return Authentication.of(getAccount(), getToken());
  }



}
