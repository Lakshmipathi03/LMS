package com.lms.service;

public interface EmailService {
    void sendRegistrationConfirmation(String to, String username);
    void sendPasswordReset(String to, String resetToken);
    void sendEnrollmentConfirmation(String to, String courseName);
    void sendAssignmentNotification(String to, String courseName, String assignmentTitle);
    void sendGradeNotification(String to, String courseName, String assignmentTitle, Double grade);
    void sendDeadlineReminder(String to, String courseName, String assignmentTitle);
    void sendBulkEmail(String[] to, String subject, String content);
}
