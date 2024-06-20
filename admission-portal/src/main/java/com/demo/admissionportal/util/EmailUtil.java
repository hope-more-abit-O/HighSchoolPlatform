package com.demo.admissionportal.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * The type Email util.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {
    private final JavaMailSender mailSender;


    /**
     * Send otp email boolean.
     *
     * @param email the email
     * @param otp   the otp
     * @return the boolean
     */
    public boolean sendOtpEmail(String email, String otp) {
        String subject = "Verify OTP";
        String emailContent = "<html><body>"
                + "<h2>Welcome to High School VN!</h2>"
                + "<p>Dear " + email + ",</p>"
                + "<p>Thank you for creating an account with us. Your OTP for account verification is: "
                + "<strong>" + otp + "</strong>.</p>"
                + "<p>Please use this OTP to complete your registration process.</p>"
                + "<p>If you have any questions or need further assistance, feel free to contact our support team.</p>"
                + "<p>Best regards,<br/>High School VN</p>"
                + "</body></html>";
        boolean isSuccess = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(emailContent, true);
            log.info("Email {} has been sending ", email);
            mailSender.send(mimeMessage);
            isSuccess = true;
        } catch (Exception ex) {
            log.error("Error occurred while sending verify email: {}", ex.getMessage());
        }
        return isSuccess;
    }

    /**
     * Send otp email for update boolean.
     *
     * @param email the email
     * @param otp   the otp
     * @return the boolean
     */
    public boolean sendOtpEmailForUpdate(String email, String otp) {
        String subject = "Email Update Verification OTP";
        String emailContent = "<html><body>"
                + "<h2>Email Update Verification</h2>"
                + "<p>Dear user,</p>"
                + "<p>You have requested to update your email address associated with your account. To verify this change, please use the following OTP:</p>"
                + "<p><strong>" + otp + "</strong></p>"
                + "<p>If you did not request this change, please contact our support team immediately.</p>"
                + "<p>Best regards,<br/>High School VN</p>"
                + "</body></html>";

        boolean isSuccess = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(emailContent, true);
            log.info("Email to {} is being sent", email);
            mailSender.send(mimeMessage);
            isSuccess = true;
        } catch (Exception ex) {
            log.error("Error occurred while sending email: {}", ex.getMessage());
        }
        return isSuccess;
    }
    public void sendHtmlEmail(String to, String subject, String htmlMessage) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlMessage, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
