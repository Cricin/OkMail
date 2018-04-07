package smtp.mail;

import okio.BufferedSink;
import smtp.misc.Utils;

import java.io.IOException;
import java.nio.charset.Charset;

public class TextBody extends MailBody {

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
    if(mediaType != null) encodeCharset = mediaType.charset();
    if(encodeCharset == null) encodeCharset = Utils.UTF8;
    sink.writeString(content, encodeCharset);
  }

  private Charset charset(){
    Charset result = null;
    if(mediaType != null) result = mediaType.charset();
    if(result == null) result = Utils.UTF8;
    return result;
  }

  public static TextBody from(String content, MediaType mediaType) {
    return new TextBody(content, mediaType);
  }
}
