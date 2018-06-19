package okmail.demo;

import smtp.Channel;
import smtp.Dns;
import smtp.MailIdGenerator;
import smtp.Session;
import smtp.SmtpClient;
import smtp.auth.Authentication;
import smtp.mail.Attachment;
import smtp.mail.Encoding;
import smtp.mail.Mail;
import smtp.mail.Mailbox;
import smtp.mail.MediaType;
import smtp.mail.MultipartBody;
import smtp.mail.TextBody;
import smtp.misc.SystemDns;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Executors;

@SuppressWarnings("Duplicates")
public class PluginSystem {

  public static void main(String[] args) {

    SmtpClient client = new SmtpClient.Builder()
        .writeTimeout(1000) //socket 写入超时

        .connectTimeout(3000) //socket 连接超时

        .readTimeout(1000) //socket 读取超时

        .defaultPort(25) //服务器的smtp端口

        .useStartTls(false) //是否开启starttls以提供ssl/tls安全传输

        .charset(Charset.forName("utf-8")) //缺省字符集

        .maxLineLength(76) //smtp协议中规定了每行字符(不是字节)的长度最
        // 大不要超过1000，现在主流的smtp客户端都为76

        .socketFactory(new SocketFactory() {   //socket 创建策略
          SocketFactory socketFactory = SocketFactory.getDefault();

          @Override
          public Socket createSocket() throws IOException {
            log("create socket");
            return socketFactory.createSocket();
          }

          @Override
          public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
            return null;
          }

          @Override
          public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws
              IOException, UnknownHostException {
            return null;
          }

          @Override
          public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            return null;
          }

          @Override
          public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1,
                                     int i1) throws IOException {
            return null;
          }
        })

        .channelConnector(new Channel.ChannelConnector() {
          @Override
          public Channel connect(SmtpClient cl, InetAddress address) throws IOException { //
            // socket连接器
            log("connecting address: " + address);
            return Channel.DIRECT.connect(cl, address);
          }
        })

        .dns(new Dns() {                    //dns lookup
          Dns SYSTEM = new SystemDns();

          @Override
          public List<InetAddress> lookupByMxRecord(String hostname) throws UnknownHostException {
            log("performing dns mx record lookup for: " + hostname);
            return SYSTEM.lookupByMxRecord(hostname);
          }

          @Override
          public List<InetAddress> lookupByARecord(String hostname) throws UnknownHostException {
            log("performing dns a record lookup for: " + hostname);
            return SYSTEM.lookupByARecord(hostname);
          }
        })

        .executor(Executors.newCachedThreadPool()) //配置并发线程池

        .mailIdGenerator(new MailIdGenerator() {
          @Override
          public String generate(Mail mail) {
            return String.valueOf(System.nanoTime()) + mail.from().canonicalAddress();
          }
        }) //message-id 生成策略

        .transferEncoding(Encoding.AUTO_SELECT) //传输编码方式

        .build();


    Attachment article = Attachment.create("荷塘月色.txt",
        Assets.resolve("荷塘月色.txt"),
        MediaType.parse("text/plain"));

    MultipartBody body = new MultipartBody
        .Builder()
        .addText(TextBody.plain(Assets.SPRING_SHORT))
        .addAttachment(article)
        .build();

    Mail mail = new Mail.Builder()
        .from(Mailbox.parse("炒面<cricin@cricin.cn>"))
        .addRecipient(Mailbox.parse("测试<test@cricin.cn>"))
        .auth(Authentication.of("cricin", "illusion"))
        .subject("朱自清《春》- 节选")
        .body(body)
        .build();


    Session session = client.newSession(mail, null);
    try {
      session.send();
      System.out.println("邮件发送成功！");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("邮件发送失败！");
    }
  }


  static void log(String str) {
    System.out.println("========>  " + str);
  }


}
