package smtp.mail;

import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;
import smtp.misc.Utils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public abstract class MailBody {

  public abstract MediaType contentType();

  public long contentLength() {
    return -1;
  }

  public abstract void writeTo(BufferedSink sink) throws IOException;




  public static MailBody create(
      final @Nullable MediaType contentType, final ByteString content) {
    return new MailBody() {
      @Override public @Nullable MediaType contentType() {
        return contentType;
      }

      @Override public long contentLength() {
        return content.size();
      }

      @Override public void writeTo(BufferedSink sink) throws IOException {
        sink.write(content);
      }
    };
  }

  /** Returns a new request body that transmits {@code content}. */
  public static MailBody create(final @Nullable MediaType contentType, final byte[] content) {
    return create(contentType, content, 0, content.length);
  }

  /** Returns a new request body that transmits {@code content}. */
  public static MailBody create(final @Nullable MediaType contentType, final byte[] content,
                                   final int offset, final int byteCount) {
    if (content == null) throw new NullPointerException("content == null");
    if ((offset | byteCount) < 0 || offset > content.length || content.length - offset < byteCount) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return new MailBody() {
      @Override public @Nullable MediaType contentType() {
        return contentType;
      }

      @Override public long contentLength() {
        return byteCount;
      }

      @Override public void writeTo(BufferedSink sink) throws IOException {
        sink.write(content, offset, byteCount);
      }
    };
  }

  /** Returns a new request body that transmits the content of {@code file}. */
  public static MailBody create(final @Nullable MediaType contentType, final File file) {
    if (file == null) throw new NullPointerException("content == null");

    return new MailBody() {
      @Override public @Nullable MediaType contentType() {
        return contentType;
      }

      @Override public long contentLength() {
        return file.length();
      }

      @Override public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
          source = Okio.source(file);
          sink.writeAll(source);
        } finally {
          Utils.closeQuietly(source);
        }
      }
    };
  }



}