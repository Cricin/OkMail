package smtp.mail;

import okio.BufferedSink;
import smtp.interceptor.TransferSpec;
import smtp.misc.Utils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;

import static smtp.mail.Encoding.BASE64;
import static smtp.mail.Encoding.QUOTED_PRINTABLE;

public class TextBody extends MailBody {

  static final MediaType TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8");
  static final MediaType TEXT_HTML = MediaType.parse("text/html; charset=utf-8");

  private final String text;
  private final MediaType mediaType;

  private TextBody(String content, MediaType mediaType) {
    this.text = content;
    this.mediaType = mediaType;
  }

  @Override
  public MediaType contentType() {
    return mediaType;
  }

  @Override
  public void writeBody(BufferedSink sink, TransferSpec spec) throws IOException {
    Charset encodeCharset = null;
    if (mediaType != null) encodeCharset = mediaType.charset();
    if (encodeCharset == null) encodeCharset = spec.charset();
    BufferedSink encodingSink = chooseEncoding(spec).from(sink, spec.lengthLimit());
    encodingSink.writeString(text, encodeCharset);
    encodingSink.flush();
  }

  /*if ascii printable character occupies 30%,
    Quoted-Printable is preferred*/
  @Override
  public Encoding transferEncoding() {
    final int totalLength = text.length();
    int asciiCount = Utils.asciiCharacterCount(text);
    if (asciiCount / (float) totalLength > 0.3F) {
//      return QUOTED_PRINTABLE;
      return BASE64;
    } else {
      return BASE64;
    }
  }

  public Charset charset() {
    Charset result = null;
    if (mediaType != null) result = mediaType.charset();
    if (result == null) result = Utils.UTF8;
    return result;
  }

  public static TextBody create(String text, @Nullable MediaType mediaType) {
    mediaType = (mediaType == null ? TEXT_PLAIN : mediaType);
    if (!"text".equalsIgnoreCase(mediaType.type())) {
      throw new IllegalArgumentException("unexpected type: " + mediaType.type());
    }
    if (text.isEmpty()) {
      throw new IllegalArgumentException("empty text");
    }
    return new TextBody(text, mediaType);
  }

  public static TextBody plain(String text) {
    return create(text, TEXT_PLAIN);
  }

  public static TextBody html(String text) {
    return create(text, TEXT_HTML);
  }


}
