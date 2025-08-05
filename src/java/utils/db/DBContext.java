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
    private static final String INSTANCE_NAME = "LEMANHTRUONG";
    private static final String DB_NAME = "fishshop";
    private static final String USER = "sa";
    private static final String PASS = "040604";


    private static String getURL() {
    return "jdbc:sqlserver://LEMANHTRUONG:1433;" +
           "databaseName=" + DB_NAME + ";" +
           "encrypt=true;trustServerCertificate=true;" +
           "characterEncoding=UTF-8;";
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