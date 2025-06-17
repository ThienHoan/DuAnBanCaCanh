package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.Random;

/**
 * Utility class để gửi email
 */
public class EmailUtil {
    
    // Cấu hình email server (Gmail)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "Hoan64735@gmail.com"; // Thay bằng email thật
    private static final String EMAIL_PASSWORD = "cwoc yqfu xajg igev"; // Thay bằng app password thật
    
    /**
     * Tạo mã code ngẫu nhiên 6 chữ số
     */
    public static String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Tạo số từ 100000 đến 999999
        return String.valueOf(code);
    }
    
    /**
     * Gửi email reset password
     */
    public static boolean sendResetPasswordEmail(String toEmail, String resetCode) {
        try {
            // Cấu hình properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // Tạo session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            // Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "Biolife Store"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã reset mật khẩu - Biolife Store");
            
            // Nội dung email
            String emailContent = createResetEmailContent(resetCode);
            message.setContent(emailContent, "text/html; charset=utf-8");
            
            // Gửi email
            Transport.send(message);
            
            System.out.println("Reset password email sent to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tạo nội dung email HTML
     */
    private static String createResetEmailContent(String resetCode) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background-color: #7fad39; color: white; padding: 20px; text-align: center; }" +
                "        .content { background-color: #f9f9f9; padding: 30px; }" +
                "        .code-box { background-color: #fff; border: 2px solid #7fad39; border-radius: 5px; " +
                "                   padding: 20px; margin: 20px 0; text-align: center; }" +
                "        .code { font-size: 32px; font-weight: bold; color: #7fad39; letter-spacing: 5px; }" +
                "        .footer { background-color: #333; color: white; padding: 15px; text-align: center; font-size: 12px; }" +
                "        .warning { color: #e74c3c; font-weight: bold; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>🌿 Biolife Store</h1>" +
                "            <p>Yêu cầu reset mật khẩu</p>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <h2>Xin chào!</h2>" +
                "            <p>Chúng tôi đã nhận được yêu cầu reset mật khẩu cho tài khoản của bạn.</p>" +
                "            <p>Vui lòng sử dụng mã code bên dưới để reset mật khẩu:</p>" +
                "            <div class='code-box'>" +
                "                <div class='code'>" + resetCode + "</div>" +
                "                <p>Mã reset mật khẩu</p>" +
                "            </div>" +
                "            <p class='warning'>⚠️ Mã này chỉ có hiệu lực trong 15 phút!</p>" +
                "            <p>Nếu bạn không yêu cầu reset mật khẩu, vui lòng bỏ qua email này.</p>" +
                "            <hr>" +
                "            <h3>Hướng dẫn reset mật khẩu:</h3>" +
                "            <ol>" +
                "                <li>Truy cập trang reset mật khẩu</li>" +
                "                <li>Nhập email và mã code trên</li>" +
                "                <li>Nhập mật khẩu mới</li>" +
                "                <li>Hoàn tất!</li>" +
                "            </ol>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>© 2024 Biolife Store. Tất cả quyền được bảo lưu.</p>" +
                "            <p>Email này được gửi tự động, vui lòng không reply.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
    
    /**
     * Gửi email thông báo mật khẩu đã được thay đổi
     */
    public static boolean sendPasswordChangedNotification(String toEmail, String username) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "Biolife Store"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mật khẩu đã được thay đổi - Biolife Store");
            
            String emailContent = "<!DOCTYPE html>" +
                    "<html><head><meta charset='UTF-8'></head><body>" +
                    "<h2>🌿 Biolife Store</h2>" +
                    "<p>Xin chào <strong>" + username + "</strong>,</p>" +
                    "<p>Mật khẩu tài khoản của bạn đã được thay đổi thành công.</p>" +
                    "<p>Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ với chúng tôi ngay lập tức.</p>" +
                    "<p>Trân trọng,<br>Đội ngũ Biolife Store</p>" +
                    "</body></html>";
            
            message.setContent(emailContent, "text/html; charset=utf-8");
            Transport.send(message);
            
            System.out.println("Password changed notification sent to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
            return false;
        }
    }
} 