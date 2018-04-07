package smtp;

import smtp.mail.Mail;

public interface MailIdGenerator {

  MailIdGenerator DEFAULT = new MailIdGenerator() {
    @Override
    public String generate(Mail mail) {
      //generated message id: timestamp + user + @ + host
      final String timeStamp = String.valueOf(System.nanoTime());
      return timeStamp + mail.from();
    }
  };

  String generate(Mail mail);

}