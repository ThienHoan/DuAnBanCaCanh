
package utils.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is deprecated. Use DBContext instead.
 * Kept for backward compatibility.
 */
@Deprecated
public class DatabaseConnection {
    public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String dbURL = "jdbc:sqlserver://localhost;databaseName=fishshop";
    public static String userDB = "sa";
    public static String passDB = "040604";
    
    public static Connection getConnection() {
        return DBContext.getConnection();
    }
    
    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("Connect to JDBC success");
            }            
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
