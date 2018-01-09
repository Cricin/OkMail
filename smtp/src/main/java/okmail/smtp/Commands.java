package okmail.smtp;

/**
 * All smtp protocol interceptor supported by OkMail implementation
 * <p>
 * smtp的command是不区分大小写的，但是参数可能会区分大小写(如用户名)
 *
 * <CRLF> --> \r\n
 */
public enum Commands {

  /*
  * helo 命令用来向服务器表明身份，只是一个简单的确认，参数是没有限制的
  *
  * HELO OkMailClient-1.0.0<CRLF>
  */
  HELO,

  /**
   * auth 命令用来登录服务器，参数是login
   *
   * AUTH LOGIN<CRLF>
   */
  AUTH,

  /**
   * mail 命令的参数是回复路径， 从发送邮箱指向目的邮箱(通常只有一个)
   *
   * MAIL FROM:<alice@hosta.com><CRLF>
   */
  MAIL,

  /**
   * rcpt 命令的参数是转发路径， 从目的邮箱指向发送邮箱(通常只有一个)
   *
   * rcpt 命令可能会重复多次，指定多个接受者
   *
   * RCPT TO:<bob@hostb.com><CRLF>
   */
  RCPT,

  /**
   * data 命令表示开始发送邮件内容
   *
   * DATA<CRLF>
   *
   */
  DATA,

  /*
   * data 命令之后就是邮件的内容了
   *
   * smtp常用的邮件头有：
   * Date(日期), Subject(主题), To(接收者), Cc(抄送), From(发送者)
   * <CRLF>.<CRLF>作为邮件的结尾标志
   * 如果邮件内容中有一行单独是.的话，会错误截断邮件，所以发送方会在.之前再
   * 加一个. 而接收方需要相应的取出一个点
   *
   */

  /**
   * quit 命令表示关闭连接，通常服务器收到命令后会主动断开连接
   */
  QUIT,

  /**
   * noop 命令表示不做任何操作
   *
   * NOOP<CRLF>
   */
  NOOP,

  //extended

  /*
   * ESMTP
   */
  EHLO,



}