package test;

import utils.db.DBContext;
import java.sql.*;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        System.out.println("=== TEST DATABASE CONNECTION ===");
        
        // Test connection
        try (Connection conn = DBContext.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Kết nối database THÀNH CÔNG!");
                
                // Test query đơn giản
                String sql = "SELECT COUNT(*) as total FROM Products";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("📊 Tổng số sản phẩm: " + total);
                }
                
                // Test lấy vài sản phẩm đầu tiên
                sql = "SELECT TOP 5 product_id, name, price, status FROM Products";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                
                System.out.println("\n📦 Sản phẩm trong database:");
                System.out.println("ID | Tên | Giá | Status");
                System.out.println("---|-----|-----|-------");
                
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("name");
                    Object price = rs.getObject("price");
                    String status = rs.getString("status");
                    
                    System.out.println(id + " | " + name + " | " + price + " | " + status);
                }
                
                // Test tìm kiếm betta
                sql = "SELECT * FROM Products WHERE name LIKE '%betta%' OR name LIKE '%Betta%'";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                
                System.out.println("\n🔍 Tìm kiếm 'betta':");
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("- " + rs.getString("name") + " (ID: " + rs.getInt("product_id") + ")");
                }
                
                if (!found) {
                    System.out.println("❌ Không tìm thấy sản phẩm có từ 'betta'");
                }
                
            } else {
                System.out.println("❌ KHÔNG thể kết nối database!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
