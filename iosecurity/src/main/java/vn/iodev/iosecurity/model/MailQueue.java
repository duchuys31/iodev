package vn.iodev.iosecurity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "T_MailQueue")
@Table(name = "T_MailQueue")
public class MailQueue {
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getMsgBody() {
        return msgBody;
    }
    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getAttachment() {
        return attachment;
    }
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getRetry() {
        return retry;
    }
    public void setRetry(int retry) {
        this.retry = retry;
    }
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "recipient", length = 512)
    private String recipient;

    @Column(name = "msgBody", columnDefinition = "TEXT")
    private String msgBody;

    @Column(name = "subject", length = 1024)
    private String subject;

    @Column(name = "attachment", length = 1024)
    private String attachment;

    @Column(name = "status")
    private int status;

    @Column(name = "retry")
    private int retry;

    public MailQueue() {}
    public MailQueue(String recipient, String msgBody, String subject, String attachment, int status, int retry) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
        this.attachment = attachment;
        this.status = status;
        this.retry = retry;
    }
}
