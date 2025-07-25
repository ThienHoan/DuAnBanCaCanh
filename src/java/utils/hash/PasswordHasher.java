package utils.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 * Sử dụng SHA-256 với salt để mã hóa mật khẩu an toàn
 */
public class PasswordHasher {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 32;
    
    /**
     * Hash mật khẩu với salt ngẫu nhiên
     * @param password mật khẩu cần hash
     * @return chuỗi hash với format: salt:hash
     */
    public static String hashPassword(String password) {
        try {
            // Tạo salt ngẫu nhiên
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password với salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            // Encode salt và hash thành Base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);
            
            // Trả về format: salt:hash
            return saltBase64 + ":" + hashBase64;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify mật khẩu với hash đã lưu
     * @param password mật khẩu cần kiểm tra
     * @param storedHash hash đã lưu (format: salt:hash)
     * @return true nếu mật khẩu đúng
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Tách salt và hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String saltBase64 = parts[0];
            String hashBase64 = parts[1];
            
            // Decode salt và hash từ Base64
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            byte[] storedHashBytes = Base64.getDecoder().decode(hashBase64);
            
            // Hash password với salt đã lưu
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            // So sánh hash
            return MessageDigest.isEqual(storedHashBytes, hashedPassword);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Kiểm tra xem mật khẩu có đủ mạnh không
     * @param password mật khẩu cần kiểm tra
     * @return true nếu mật khẩu đủ mạnh
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }
        
        // Yêu cầu ít nhất 3 trong 4 loại ký tự
        int score = 0;
        if (hasLower) score++;
        if (hasUpper) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;
        
        return score >= 3 && password.length() >= 8;
    }
    
    /**
     * Tính điểm mạnh của mật khẩu (0-100)
     * @param password mật khẩu cần đánh giá
     * @return điểm từ 0-100
     */
    public static int getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }
        
        int score = 0;
        
        // Độ dài
        if (password.length() >= 8) score += 20;
        if (password.length() >= 12) score += 20;
        
        // Có chữ thường
        if (password.matches(".*[a-z].*")) score += 20;
        
        // Có chữ hoa
        if (password.matches(".*[A-Z].*")) score += 20;
        
        // Có số
        if (password.matches(".*[0-9].*")) score += 20;
        
        return Math.min(score, 100);
    }
    
    /**
     * Tạo mật khẩu ngẫu nhiên
     * @param length độ dài mật khẩu
     * @return mật khẩu ngẫu nhiên
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
}
