package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Lớp tiện ích để mã hóa mật khẩu sử dụng thuật toán SHA-256 với salt
 */
public class PasswordEncryption {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Mã hóa mật khẩu sử dụng SHA-256 với salt
     * 
     * @param plainTextPassword Mật khẩu dạng văn bản thường
     * @return Mật khẩu đã được mã hóa (format: salt:hashedPassword)
     */
    public static String hashPassword(String plainTextPassword) {
        try {
            // Tạo salt ngẫu nhiên
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Mã hóa mật khẩu với salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(plainTextPassword.getBytes());
            
            // Kết hợp salt và hashedPassword, encode base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashedPasswordBase64 = Base64.getEncoder().encodeToString(hashedPassword);
            
            return saltBase64 + ":" + hashedPasswordBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi khi mã hóa mật khẩu", e);
        }
    }
    
    /**
     * Kiểm tra mật khẩu nhập vào có khớp với mật khẩu đã mã hóa hay không
     * 
     * @param plainTextPassword Mật khẩu dạng văn bản thường
     * @param hashedPassword Mật khẩu đã được mã hóa (format: salt:hashedPassword)
     * @return true nếu mật khẩu khớp, false nếu không khớp
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        try {
            // Tách salt và hashedPassword
            String[] parts = hashedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String saltBase64 = parts[0];
            String storedHashBase64 = parts[1];
            
            // Decode salt từ base64
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            
            // Mã hóa mật khẩu nhập vào với salt đã lưu
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedInputPassword = md.digest(plainTextPassword.getBytes());
            
            // So sánh với mật khẩu đã lưu
            String hashedInputBase64 = Base64.getEncoder().encodeToString(hashedInputPassword);
            
            return storedHashBase64.equals(hashedInputBase64);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Alias cho method checkPassword để tương thích
     * 
     * @param plainTextPassword Mật khẩu dạng văn bản thường
     * @param hashedPassword Mật khẩu đã được mã hóa (format: salt:hashedPassword)
     * @return true nếu mật khẩu khớp, false nếu không khớp
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return checkPassword(plainTextPassword, hashedPassword);
    }
}