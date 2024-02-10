package com.labs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailService")
@PropertySource(value={"classpath:application.properties"})
public class EmailService {

    @Value("${spring.mail.username}")
    private String NOREPLY_ADDRESS;

    @Autowired
    private JavaMailSender emailSender;
    
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    public void sendLogMessage(String to, String changeType, String entityClass, Long entityId) {
        StringBuilder sb = new StringBuilder();

        sb.append("Hello! A change has been made to the " + entityClass + " with id = " + entityId + ".\n\n");
        sb.append("Type of change: " + changeType + ".\n\n");
        sb.append("--\n");
        sb.append("There's no need to reply to this message.\n");

        // Convert StringBuilder to String
        String result = sb.toString();

        sendSimpleMessage(to, "A change to an entity", result);

    }
}