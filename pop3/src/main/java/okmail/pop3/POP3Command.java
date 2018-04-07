package okmail.pop3;

import okio.BufferedSink;
import okmail.Util;

import javax.annotation.Nullable;
import java.io.IOException;

public enum POP3Command {

  //注意：除了STAT，LIST和UIDL的响应外，其它命令的响应均为"+OK"和 "-ERR"。响应后的所有文本将被客户略去。
  //在pop3中， 多行响应以CRLF.CRLF结尾，这与smtp邮件体的结尾是一样的


  USER {
    @Override
    public void writeToSink(BufferedSink sink, String... params) throws IOException {
      throwIfMalformed(params != null && params.length == 1, "user required");
      sink.writeUtf8(this.name())
              .writeByte(Util.SP)
              .writeUtf8(params[0])
              .writeByte(Util.CR)
              .writeByte(Util.LF);
    }

  },// phase(确认) param(用户名)

  PASS {
    @Override
    public void writeToSink(BufferedSink sink, String... params) throws IOException {
      throwIfMalformed(params != null && params.length == 1, "pass required");
      sink.writeUtf8(this.name())
              .writeByte(Util.SP)
              .writeUtf8(params[0])
              .writeByte(Util.CR)
              .writeByte(Util.LF);
    }
  },// phase(确认) param(密码)


  STAT,// phase(操作)  response(+OK 邮件数 邮件总大小 or -ERR)

  LIST {
    @Override
    public void writeToSink(BufferedSink sink, @Nullable String... params) throws IOException {
      if (params == null) {
        sink.writeUtf8(name()).writeUtf8("\r\n");
      }else{
        throwIfMalformed(params.length != 1, "mail number required");
        throwIfNotInt(params[0]);
        sink.writeUtf8(name())
                .writeByte(Util.SP)
                .writeUtf8(params[0])
                .writeByte(Util.CR)
                .writeByte(Util.LF);
      }
    }
  },// phase(操作) response(+OK 多行响应，给出每一封邮件的index 和大小，一行一个邮件  or -ERR)

  RETR {
    @Override
    public void writeToSink(BufferedSink sink, String... params) throws IOException {
      throwIfMalformed(params != null && params.length == 1, "mail number required");
      throwIfNotInt(params[0]);
      sink.writeUtf8(name())
              .writeByte(Util.SP)
              .writeUtf8(params[0])
              .writeByte(Util.CR)
              .writeByte(Util.LF);
    }
  },// phase(操作)

  DELE {
    @Override
    public void writeToSink(BufferedSink sink, String... params) throws IOException {
      throwIfMalformed(params != null && params.length == 1, "mail number required");
      throwIfNotInt(params[0]);
      sink.writeUtf8(name())
              .writeByte(Util.SP)
              .writeUtf8(params[0])
              .writeByte(Util.CR)
              .writeByte(Util.LF);
    }
  },// phase(操作) param(邮件index)
  NOOP,// phase(操作)
  RSET,// phase(操作)

  QUIT,// phase(更新)

  //Optional Command

  //TODO 是否实现apop命令？
  APOP,// phase(确认) param(name digest)

  TOP {
    @Override
    public void writeToSink(BufferedSink sink, @Nullable String... params) throws IOException {
      throwIfMalformed(params != null && params.length == 2, "mail number and line number required");
      sink.writeUtf8(name())
              .writeByte(Util.SP)
              .writeUtf8(params[0])
              .writeByte(Util.SP)
              .writeUtf8(params[1])
              .writeByte(Util.CR)
              .writeByte(Util.LF);
    }
  },// phase(操作) param(邮件index 邮件体行数) response(+OK 后接邮件header和指定行数的body or -ERR)

  //有多行 unique-id格式 ： 邮件index unique-id
  UIDL {
    @Override
    public void writeToSink(BufferedSink sink, @Nullable String... params) throws IOException {
      if (params == null) {
        sink.writeUtf8(name())
                .writeByte(Util.CR)
                .writeByte(Util.LF);
      }else{
        throwIfMalformed(params.length != 1, "mail number required");
        throwIfNotInt(params[0]);
        sink.writeUtf8(name())
                .writeByte(Util.SP)
                .writeUtf8(params[0])
                .writeByte(Util.CR)
                .writeByte(Util.LF);
      }
    }
  };// phase(操作) param(no param) or param(邮件index) response(指定参数时，+OK 给出该邮件的UNIQUE-ID， 不指定参数时, 给出所有邮件的UNIQUE-ID -ERR)

  public void writeToSink(BufferedSink sink, @Nullable String... params) throws IOException {
    sink.writeUtf8(name())
            .writeByte(Util.CR)
            .writeByte(Util.LF);
  }

  protected final void throwIfMalformed(boolean valid, String msg) throws IOException {
    if (!valid) throw new IOException(msg);
  }

  protected final void throwIfNotInt(String value) throws IOException {
    try {
      Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new IOException("expect an integer value");
    }
  }

}