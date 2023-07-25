package vn.iodev.iosecurity.service;

import org.springframework.stereotype.Service;

import vn.iodev.iosecurity.IOConstants;
import vn.iodev.iosecurity.mail.EmailDetails;
import vn.iodev.iosecurity.model.MailQueue;
import vn.iodev.iosecurity.model.TaiKhoan;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailServiceImpl implements EmailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";
    @Autowired 
    private JavaMailSender javaMailSender;
 
    @Value("${spring.mail.username}") 
    private String sender;
 
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    ThymeleafService thymeleafService;
    
    public String sendSimpleMail(EmailDetails details)
    {
        try {
            SimpleMailMessage mailMessage
                = new SimpleMailMessage();
 
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
 
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
 
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
 
    public String sendMailWithAttachment(EmailDetails details)
    {
        MimeMessage mimeMessage
            = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        
        try {
            mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                details.getSubject());
 
            FileSystemResource file
                = new FileSystemResource(
                    new File(details.getAttachment()));
 
            mimeMessageHelper.addAttachment(
                file.getFilename(), file);
 
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }
 
        catch (MessagingException e) {
            return "Error while sending mail!!!";
        }
    }

    @Override
    public String sendActiveUserHtmlMail(TaiKhoan taiKhoan) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(taiKhoan.getEmail())});

            HashMap<String, Object> activeVariables = new HashMap<>();
            activeVariables.put("MaKichHoat", taiKhoan.getMaKichHoat());
            
            String msgBody = thymeleafService.getContent(IOConstants.ACTIVE_USER_MAIL_TEMPLATE, activeVariables);

            message.setContent(msgBody, CONTENT_TYPE_TEXT_HTML);
            message.setSubject(IOConstants.ACTIVE_USER_MAIL_SUBJECT);
            
            Transport.send(message);
            return "Mail Sent Successfully...";
        }
 
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendHtmlMailQueue(MailQueue mailQueue) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(mailQueue.getRecipient())});

            message.setContent(mailQueue.getMsgBody(), CONTENT_TYPE_TEXT_HTML);
            message.setSubject(mailQueue.getSubject());
            
            Transport.send(message);
            return "Mail Sent Successfully...";
        }
 
        catch (Exception e) {
            return "Error while Sending Mail";
        }    
    }
}
