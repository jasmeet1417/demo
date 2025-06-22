package com.corp.match.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;  // injected from properties

    public String sendVerificationOtp(String toEmail, String otp) {
        String domain = toEmail.substring(toEmail.indexOf('@') + 1);

        String messageText = "Your email verification code is: " + otp;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification OTP");
        message.setText(messageText);
        message.setFrom(fromEmail);  // from spring.mail.username

        javaMailSender.send(message);
        return domain;
    }

}
