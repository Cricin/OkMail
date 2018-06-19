package smtp.mail;

import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import smtp.interceptor.TransferSpec;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** A mime multipart body */
public class MultipartBody extends MailBody {

  public static final MediaType MIXED = MediaType.parse("multipart/mixed");
  public static final MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
  public static final MediaType PARALLEL = MediaType.parse("multipart/parallel");

  private static final byte[] COLONSPACE = {':', ' '};
  private static final byte[] CRLF = {'\r', '\n'};
  private static final byte[] DASHDASH = {'-', '-'};


  private final ByteString boundary;
  private final List<Part> parts;
  private final MediaType contentType;
  private final MediaType originalType;

  MultipartBody(ByteString boundary, MediaType type, List<Part> parts) {
    this.boundary = boundary;
    this.originalType = type;
    this.contentType = MediaType.parse(type.toString() + "; boundary=\"" + boundary.utf8() + "\"");
    this.parts = Collections.unmodifiableList(parts);
  }

  public MediaType type() {
    return originalType;
  }

  public String boundary() {
    return boundary.utf8();
  }

  /** The number of parts in this multipart body. */
  public int size() {
    return parts.size();
  }

  public List<Part> parts() {
    return parts;
  }

  public Part part(int index) {
    return parts.get(index);
  }

  @Override
  public MediaType contentType() {
    return contentType;
  }

  @Override
  public void writeBody(BufferedSink sink, TransferSpec spec) throws IOException {

    for (int p = 0, partCount = parts.size(); p < partCount; p++) {
      Part part = parts.get(p);
      Headers headers = part.headers;
      MailBody body = part.body;

      sink.write(DASHDASH);
      sink.write(boundary);
      sink.write(CRLF);

      if (headers != null) {
        for (int h = 0, headerCount = headers.size(); h < headerCount; h++) {
          sink.writeUtf8(headers.name(h))
              .write(COLONSPACE)
              .writeUtf8(headers.value(h))
              .write(CRLF);
        }
      }

      final Encoding encoding = body.chooseEncoding(spec);
      sink.writeUtf8("Content-Transfer-Encoding: ")
          .writeUtf8(encoding.name())
          .write(CRLF);

      MediaType contentType = body.contentType();
      if (contentType != null) {
        sink.writeUtf8("Content-Type: ")
            .writeUtf8(contentType.toString())
            .write(CRLF);
      }
      if (body instanceof Attachment) {
        final Attachment attachment = (Attachment) body;
        sink.writeUtf8("Content-Disposition: ")
            .writeUtf8(attachment.contentDisposition())
            .write(CRLF);
      }

      sink.write(CRLF);

      body.writeBody(sink, spec);

      sink.write(CRLF);
    }

    sink.write(DASHDASH);
    sink.write(boundary);
    sink.write(DASHDASH);
    sink.write(CRLF);
  }

  @Override
  public Encoding transferEncoding() {
    return Encoding.EIGHT_BIT;
  }


  public static final class Part {
    public static Part create(MailBody body) {
      return create(null, body);
    }

    public static Part create(@Nullable Headers headers, MailBody body) {
      if (body == null) {
        throw new NullPointerException("body == null");
      }
      if (headers != null && headers.get("Content-Type") != null) {
        throw new IllegalArgumentException("Unexpected header: Content-Type");
      }
      if (headers != null && headers.get("Content-Length") != null) {
        throw new IllegalArgumentException("Unexpected header: Content-Length");
      }
      return new Part(headers, body);
    }

    final @Nullable
    Headers headers;
    final MailBody body;

    private Part(@Nullable Headers headers, MailBody body) {
      this.headers = headers;
      this.body = body;
    }

    public @Nullable
    Headers headers() {
      return headers;
    }

    public MailBody body() {
      return body;
    }
  }


  public static final class Builder {

    final List<Part> parts = new ArrayList<>();
    final ByteString boundary;
    MediaType type = MIXED;

    public Builder() {
      this(UUID.randomUUID().toString().substring(5));
    }

    public Builder(String boundary) {
      this.boundary = ByteString.encodeUtf8(boundary);
    }

    public Builder setType(MediaType type) {
      if (type == null) {
        throw new NullPointerException("type == null");
      }
      if (!type.type().equals("multipart")) {
        throw new IllegalArgumentException("multipart != " + type);
      }
      this.type = type;
      return this;
    }

    public Builder addAttachment(Attachment attachment) {
      parts.add(Part.create(attachment));
      return this;
    }

    public Builder addText(TextBody textBody) {
      parts.add(Part.create(textBody));
      return this;
    }

    public MultipartBody build() {
      if (parts.isEmpty())
        throw new IllegalStateException("Multipart body must have at least one part.");
      return new MultipartBody(boundary, type, parts);
    }

  }


}