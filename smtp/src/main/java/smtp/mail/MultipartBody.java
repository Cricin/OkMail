package smtp.mail;

import okio.BufferedSink;

import java.io.IOException;

public class MultipartBody extends MailBody {

  public static final MediaType MIXED = MediaType.parse("multipart/mixed");
  public static final MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
  public static final MediaType PARALLEL = MediaType.parse("multipart/parallel");


  @Override
  public MediaType contentType() {
    return null;
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException {

  }
}