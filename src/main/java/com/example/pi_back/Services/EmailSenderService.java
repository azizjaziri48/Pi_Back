package com.example.pi_back.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String toEmail, String htmlBody, String subject) throws MessagingException {
      /*  SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mohamedaziz.jaziri1@esprit.tn");
        message.setTo(toEmail);
        message.setText(htmlBody,true);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");*/
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHealper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHealper.setFrom("mohamedaziz.jaziri1@esprit.tn");
        mimeMessageHealper.setTo(toEmail);
        mimeMessageHealper.setText(htmlBody,true);
        mimeMessageHealper.setSubject(subject);
        mailSender.send(mimeMessage);
        System.out.println("Mail sent successfully!");
    }
}
