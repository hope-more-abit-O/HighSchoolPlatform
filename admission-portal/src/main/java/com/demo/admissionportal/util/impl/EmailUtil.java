package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Email util.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {
    private final JavaMailSender mailSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

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
                + "<h2>Chào mừng bạn đến với High School VN!</h2>"
                + "<p>Xin chào " + email + ",</p>"
                + "<p>Cảm ơn bạn đã tạo tài khoản với chúng tôi. Mã OTP của bạn để xác minh tài khoản là: "
                + "<strong>" + otp + "</strong>.</p>"
                + "<p>Vui lòng sử dụng mã OTP này để hoàn tất quá trình đăng ký của bạn.</p>"
                + "<p>Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ thêm, hãy liên hệ với đội ngũ hỗ trợ của chúng tôi.</p>"
                + "<p>Trân trọng,<br/>High School VN</p>"
                + "</body></html>";
        boolean isSuccess = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
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
                + "<h2>Xác minh cập nhật email</h2>"
                + "<p>Xin chào,</p>"
                + "<p>Bạn đã yêu cầu cập nhật địa chỉ email liên kết với tài khoản của bạn. Để xác minh thay đổi này, vui lòng sử dụng mã OTP sau:</p>"
                + "<p><strong>" + otp + "</strong></p>"
                + "<p>Nếu bạn không yêu cầu thay đổi này, vui lòng liên hệ ngay với đội ngũ hỗ trợ của chúng tôi.</p>"
                + "<p>Trân trọng,<br/>High School VN</p>"
                + "</body></html>";
        boolean isSuccess = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
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

    public boolean sendExamScoreEmail(String to, String subject, String htmlMessage) {
        return sendEmailAsync(to, subject, htmlMessage);
    }

    private boolean sendEmailAsync(String to, String subject, String htmlMessage) {
        executorService.submit(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlMessage, true);
                mailSender.send(message);
                log.info("Email sent to {}", to);
            } catch (MessagingException e) {
                log.error("Error sending email to {}: {}", to, e.getMessage());
            }
        });
        return true;
    }

    // Optional: Shutdown executor service gracefully
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public void sendAccountPasswordRegister(User account, String password) {
        String emailContent = "<html><body>"
                + "<h2>Chào mừng bạn đến với High School VN!</h2>"
                + "<p>Xin chào " + account.getUsername() + ",</p>"
                + "<p>Tài khoản của bạn đã được tạo thành công. Dưới đây là thông tin đăng nhập của bạn:</p>"
                + "<p><strong>Username:</strong> " + account.getUsername() + "</p>"
                + "<p><strong>Password:</strong> " + password + "</p>"
                + "<p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu tiên.</p>"
                + "<p>Trân trọng,<br/>High School VN</p>"
                + "</body></html>";

        sendHtmlEmail(account.getEmail(), "Thông tin tài khoản của bạn", emailContent);
    }
}