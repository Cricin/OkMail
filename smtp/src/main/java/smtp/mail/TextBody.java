package smtp.mail;

import okio.BufferedSink;
import smtp.mime.Encoding;
import smtp.misc.Utils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;

import static smtp.mime.Encoding.BASE64;
import static smtp.mime.Encoding.QUOTED_PRINTABLE;

public class TextBody extends MailBody {

  static final MediaType TEXT_PLAIN = MediaType.parse("text/plain");

  private final String content;
  private final MediaType mediaType;

  private TextBody(String content, MediaType mediaType) {
    this.content = content;
    this.mediaType = mediaType;
  }

  @Override
  public MediaType contentType() {
    return mediaType;
  }

  @Override
  public long contentLength() {
    return content.getBytes(charset()).length;
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException {
    Charset encodeCharset = null;
    if (mediaType != null) encodeCharset = mediaType.charset();
    if (encodeCharset == null) encodeCharset = Utils.UTF8;
    sink.writeString(content, encodeCharset);
  }

  public Charset charset() {
    Charset result = null;
    if (mediaType != null) result = mediaType.charset();
    if (result == null) result = Utils.UTF8;
    return result;
  }

  /*if ascii printable character occupies 30%,
    Quoted-Printable is preferred*/
  public Encoding preferredEncoding() {
    final int totalLength = content.length();
    int asciiCount = 0;
    for (int i = 0; i < totalLength; i++) {
    }
    if (asciiCount / (float) totalLength > 0.3F) {
      return QUOTED_PRINTABLE;
    }else{
      return BASE64;
    }
  }

  public static TextBody of(String content,@Nullable MediaType mediaType) {
    mediaType = (mediaType == null ? TEXT_PLAIN : mediaType);
    if (!"text".equalsIgnoreCase(mediaType.type())) {
      throw new IllegalArgumentException("unexpected type: " + mediaType.type());
    }
    if(content.isEmpty()){
      throw new IllegalArgumentException("empty content");
    }
    return new TextBody(content, mediaType);
  }
}
