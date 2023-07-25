package vn.iodev.iosecurity.service;

import vn.iodev.iosecurity.mail.EmailDetails;
import vn.iodev.iosecurity.model.MailQueue;
import vn.iodev.iosecurity.model.TaiKhoan;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);
    String sendActiveUserHtmlMail(TaiKhoan taiKhoan);
    String sendHtmlMailQueue(MailQueue mailQueue);
}
