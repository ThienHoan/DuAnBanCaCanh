package utils.admin;

import utils.db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Công cụ để quản lý các tác vụ cơ sở dữ liệu cho quản trị viên
 */
public class DatabaseAdminUtils {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java DatabaseAdminUtils [command]");
            System.out.println("Commands:");
            System.out.println("  check-identity table_name - Kiểm tra giá trị IDENTITY hiện tại của bảng");
            System.out.println("  reset-identity table_name new_value - Reset giá trị IDENTITY của bảng");
            return;
        }

        String command = args[0];
        
        switch (command) {
            case "check-identity":
                if (args.length < 2) {
                    System.out.println("Error: Missing table name");
                    return;
                }
                String tableName = args[1];
                int currentValue = getCurrentIdentityValue(tableName);
                System.out.println("Current IDENTITY value for table " + tableName + ": " + currentValue);
                break;
                
            case "reset-identity":
                if (args.length < 3) {
                    System.out.println("Error: Missing table name or new value");
                    return;
                }
                tableName = args[1];
                int newValue = Integer.parseInt(args[2]);
                
                boolean success = resetIdentity(tableName, newValue);
                if (success) {
                    System.out.println("Successfully reset IDENTITY value for table " + tableName + " to " + newValue);
                } else {
                    System.out.println("Failed to reset IDENTITY value");
                }
                break;
                
            default:
                System.out.println("Unknown command: " + command);
        }
    }
    
    public static int getCurrentIdentityValue(String tableName) {
        String sql = "SELECT IDENT_CURRENT(?) as current_identity";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("current_identity");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting current IDENTITY value: " + e.getMessage());
        }
        return -1;
    }
    
    public static boolean resetIdentity(String tableName, int newValue) {
        String sql = "DBCC CHECKIDENT(?, RESEED, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tableName);
            ps.setInt(2, newValue);
            ps.execute();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error resetting IDENTITY value: " + e.getMessage());
        }
        return false;
    }
}
