package com.praveen.jpa.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;

  public void sendEmail(String recipient, String subject, String content) {
    try {
      MimeMessage mimeMessage = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setFrom("email.from@gmail.com");
      helper.setTo(recipient);
      helper.setSubject(subject);
      helper.setText(content);
      emailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new RuntimeException("Error while sending email", e);
    }
  }
}
