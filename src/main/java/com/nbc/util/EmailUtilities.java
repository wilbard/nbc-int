package com.nbc.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Component
public class EmailUtilities {

    private JavaMailSender javaMailSender;

    public EmailUtilities(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendEmailTemplate(String toEmail, String subject, String htmlMessage) throws MessagingException, IOException {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom("hekima.website@gmail.com");
            helper.setTo(toEmail);
            helper.setBcc("willydammas@gmail.com");

            helper.setSubject(subject);
            helper.setText(htmlMessage, true);
            msg.setHeader("X-Priority", "1");
            javaMailSender.send(msg);
        } catch (Exception e) {
            boolean res = false;
        }
    }

    public boolean sendWebsiteEmail(String toEmail, String subject, String htmlMessage) throws MessagingException, IOException {
        boolean res = true;
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom("hekima.website@gmail.com");
            helper.setTo(toEmail);
            helper.setBcc("willydammas@gmail.com");
            /*helper.setReplyTo(fromEmail, fromName);*/

            helper.setSubject(subject);
            helper.setText(htmlMessage, false);
            msg.setHeader("X-Priority", "1");
            javaMailSender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }
}
