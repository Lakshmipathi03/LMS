package com.lms.service.impl;

import com.lms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendRegistrationConfirmation(String to, String username) {
        Context context = new Context();
        context.setVariable("username", username);
        String content = templateEngine.process("registration-template", context);
        sendHtmlEmail(to, "Welcome to LMS!", content);
    }

    @Override
    public void sendPasswordReset(String to, String resetToken) {
        Context context = new Context();
        context.setVariable("resetToken", resetToken);
        String content = templateEngine.process("password-reset-template", context);
        sendHtmlEmail(to, "Password Reset Request", content);
    }

    @Override
    public void sendEnrollmentConfirmation(String to, String courseName) {
        Context context = new Context();
        context.setVariable("courseName", courseName);
        String content = templateEngine.process("enrollment-template", context);
        sendHtmlEmail(to, "Course Enrollment Confirmation", content);
    }

    @Override
    public void sendAssignmentNotification(String to, String courseName, String assignmentTitle) {
        Context context = new Context();
        context.setVariable("courseName", courseName);
        context.setVariable("assignmentTitle", assignmentTitle);
        String content = templateEngine.process("assignment-template", context);
        sendHtmlEmail(to, "New Assignment Posted", content);
    }

    @Override
    public void sendGradeNotification(String to, String courseName, String assignmentTitle, Double grade) {
        Context context = new Context();
        context.setVariable("courseName", courseName);
        context.setVariable("assignmentTitle", assignmentTitle);
        context.setVariable("grade", grade);
        String content = templateEngine.process("grade-template", context);
        sendHtmlEmail(to, "Assignment Graded", content);
    }

    @Override
    public void sendDeadlineReminder(String to, String courseName, String assignmentTitle) {
        Context context = new Context();
        context.setVariable("courseName", courseName);
        context.setVariable("assignmentTitle", assignmentTitle);
        String content = templateEngine.process("deadline-reminder-template", context);
        sendHtmlEmail(to, "Assignment Deadline Reminder", content);
    }

    @Override
    public void sendBulkEmail(String[] to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setBcc(to);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
