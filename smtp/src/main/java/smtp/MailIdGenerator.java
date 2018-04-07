package smtp;

import smtp.mail.Mail;

public interface MailIdGenerator {

  MailIdGenerator DEFAULT = new MailIdGenerator() {
    @Override
    public String generate(Mail mail) {
      return null;
    }
  };

  String generate(Mail mail);

}