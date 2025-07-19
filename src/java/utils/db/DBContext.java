package utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {
    // 1. Khai báo các thông tin kết nối
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String HOST_NAME = "localhost";
    private static final String INSTANCE_NAME = "MSSQLSERVER01";
    private static final String DB_NAME = "fishshopp";
    private static final String USER = "sa";
    private static final String PASS = "123";

    /**
     * Lấy chuỗi URL kết nối đến database.
     * Đã được sửa lỗi cú pháp và thêm mã hóa UTF-8.
     * @return Chuỗi URL hợp lệ.
     */
    private static String getURL() {
        // SỬA LỖI 1: Sửa lại cú pháp URL và loại bỏ dấu " thừa.
        // SỬA LỖI 2: Thêm ;characterEncoding=UTF-8 và ;encrypt=true;trustServerCertificate=true để tương thích với các driver mới.
        return "jdbc:sqlserver://" + HOST_NAME + ";instanceName=" + INSTANCE_NAME + 
               ";databaseName=" + DB_NAME + 
               ";characterEncoding=UTF-8" +
               ";encrypt=true;trustServerCertificate=true";
    }

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(getURL(), USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Lỗi kết nối database nghiêm trọng", e);
            return null; // Trả về null nếu có lỗi
        }
    }
    
    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("✅ Kết nối database thành công!");
            } else {
                System.err.println("❌ Kết nối database thất bại. Vui lòng kiểm tra lại cấu hình trong DBContext.java.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
