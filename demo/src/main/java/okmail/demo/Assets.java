package okmail.demo;

import okio.BufferedSource;
import okio.Okio;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

@SuppressWarnings("WeakerAccess")
public class Assets {

  static final String SPRING;
  static final String MOONLIGHT;
  static final String HTML;
  static final String SPRING_SHORT;
  static final String MOONLIGHT_SHORT;


  static final Properties properties = new Properties();


  static {
    SPRING = load("assets/春.txt");
    MOONLIGHT = load("assets/荷塘月色.txt");
    HTML = load("assets/春.html");

    SPRING_SHORT = load("assets/春-节选.txt");
    MOONLIGHT_SHORT = load("assets/荷塘月色-节选.txt");

    try {
      properties.load(new FileInputStream("mail.properties"));
    } catch (IOException e) {
      throw new RuntimeException("无法加载配置文件mail.properties");
    }
  }

  public static String load(String fileName) {
    File file = new File(fileName);
    try {
      BufferedSource source = Okio.buffer(Okio.source(file));
      return source.readUtf8();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  public static File resolve(String fileName) {
    return new File("assets/" + fileName);
  }
}
