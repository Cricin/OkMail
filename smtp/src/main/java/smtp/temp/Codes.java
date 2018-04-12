package smtp.temp;

public interface Codes {
   /*
      500 格式错误，命令不可识别（此错误也包括命令行过长）
      501 参数格式错误
      502 命令不可实现
      503 错误的命令序列
      504 命令参数不可实现
      211 系统状态或系统帮助响应
      214 帮助信息
      220 <domain> 服务就绪
      221 <domain> 服务关闭传输信道
      421 <domain> 服务未就绪，关闭传输信道（当必须关闭时，此应答可以作为对任何命令的响应）
      250 要求的邮件操作完成
      251 用户非本地，将转发向<forward-path>
      450 要求的邮件操作未完成，邮箱不可用（例如，邮箱忙）
      550 要求的邮件操作未完成，邮箱不可用（例如，邮箱未找到，或不可访问）
      451 放弃要求的操作；处理过程中出错
      551 用户非本地，请尝试<forward-path>
      452 系统存储不足，要求的操作未执行
      552 过量的存储分配，要求的操作未执行
      553 邮箱名不可用，要求的操作未执行（例如邮箱格式错误）
      354 开始邮件输入，以<CRLF>.<CRLF>结束
      554 操作失败
   */


  /*
          所有可能的应答 S(成功) F(失败),I(中间态),E(错误)
          CONNECTION ESTABLISHMENT（建立连接）
          S: 220
          F: 421

          HELO
          S: 250
          E: 500, 501, 504, 421

          MAIL
          S: 250
          F: 552, 451, 452
          E: 500, 501, 421

          RCPT
          S: 250, 251
          F: 550, 551, 552, 553, 450, 451, 452
          E: 500, 501, 503, 421

          DATA
          I: 354 -> data -> S: 250
          F: 552, 554, 451, 452
          F: 451, 554
          E: 500, 501, 503, 421

          RSET
          S: 250
          E: 500, 501, 504, 421

          SEND
          S: 250
          F: 552, 451, 452
          E: 500, 501, 502, 421

          SOML
          S: 250
          F: 552, 451, 452
          E: 500, 501, 502, 421

          SAML
          S: 250
          F: 552, 451, 452
          E: 500, 501, 502, 421

          VRFY
          S: 250, 251
          F: 550, 551, 553
          E: 500, 501, 502, 504, 421

          EXPN
          S: 250
          F: 550
          E: 500, 501, 502, 504, 421

          HELP
          S: 211, 214
          E: 500, 501, 502, 504, 421

          NOOP
          S: 250
          E: 500, 421

          QUIT
          S: 221
          E: 500

          TURN
          S: 250
          F: 502
          E: 500, 503
   */



  int OK = 250;
  int Failure = 550;
  int Intermediate = 354;

}