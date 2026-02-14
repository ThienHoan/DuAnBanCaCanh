package utils;

/**
 * Class để test mã hóa và kiểm tra mật khẩu
 */
public class TestPasswordEncryption {
    
    public static void main(String[] args) {
        // Test mã hóa mật khẩu
        String plainPassword = "123456";
        
        System.out.println("=== TEST MÃ HÓA MẬT KHẨU ===");
        System.out.println("Mật khẩu gốc: " + plainPassword);
        
        // Mã hóa mật khẩu
        String hashedPassword = PasswordEncryption.hashPassword(plainPassword);
        System.out.println("Mật khẩu đã mã hóa: " + hashedPassword);
        
        // Kiểm tra mật khẩu đúng
        boolean isCorrect = PasswordEncryption.checkPassword(plainPassword, hashedPassword);
        System.out.println("Kiểm tra mật khẩu đúng: " + isCorrect);
        
        // Kiểm tra mật khẩu sai
        boolean isWrong = PasswordEncryption.checkPassword("wrongpassword", hashedPassword);
        System.out.println("Kiểm tra mật khẩu sai: " + isWrong);
        
        System.out.println("\n=== TEST NHIỀU LẦN MÃ HÓA ===");
        // Mã hóa cùng một mật khẩu nhiều lần để kiểm tra salt
        for (int i = 1; i <= 3; i++) {
            String hash = PasswordEncryption.hashPassword(plainPassword);
            System.out.println("Lần " + i + ": " + hash);
        }
        
        System.out.println("\n=== TEST CÁC MẬT KHẨU KHÁC NHAU ===");
        String[] passwords = {"password", "admin", "user123", "mypassword"};
        
        for (String pwd : passwords) {
            String hash = PasswordEncryption.hashPassword(pwd);
            boolean check = PasswordEncryption.checkPassword(pwd, hash);
            System.out.println("Password: " + pwd + " -> Hash: " + hash.substring(0, 20) + "... -> Check: " + check);
        }
    }
} 