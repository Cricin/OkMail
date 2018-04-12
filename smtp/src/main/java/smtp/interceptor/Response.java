package smtp.interceptor;

import okio.BufferedSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** this class only debug purpose*/
public final class Response {
  private Response() {
  }

  public static String readLine(BufferedSource source)throws IOException {
    return source.readUtf8Line();
  }


  public static List<String> readEachLine(BufferedSource source) throws IOException {
    List<String> result = new ArrayList<>();
    String line;
    do {
      line = source.readUtf8Line();
      result.add(line);
    } while (line != null && line.charAt(3) != '=');
    return result;
  }

  public static String readAll(BufferedSource source) throws IOException {
    StringBuilder builder = new StringBuilder();
    String line;
    do {
      line = source.readUtf8Line();
      builder.append(line);
      builder.append("\r\n");
    } while (line != null && line.charAt(3) != ' ');
    return builder.toString();
  }

  public static int parseCode(String response) {
    if (response == null) return -1;
    if (response.length() < 3) return -1;
    try {
      return Integer.parseInt(response.substring(0, 3));
    } catch (NumberFormatException nfe) {
      return -1;
    }
  }


}
