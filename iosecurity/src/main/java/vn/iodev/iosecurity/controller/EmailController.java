package vn.iodev.iosecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.iosecurity.mail.EmailDetails;
import vn.iodev.iosecurity.service.EmailService;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class EmailController {
    @Autowired 
    private EmailService emailService;

    @PostMapping("/mail/sendMail")
    public String sendMail(@RequestBody EmailDetails details)
    {
        log.info("API POST /mail/sendMail");
        String status
            = emailService.sendSimpleMail(details);
 
        return status;
    }
 
    @PostMapping("/mail/sendMailWithAttachment")
    public String sendMailWithAttachment(
        @RequestBody EmailDetails details)
    {
        log.info("API POST /mail/sendMailWithAttachment");
        String status
            = emailService.sendMailWithAttachment(details);
 
        return status;
    }    
}
