
package util.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class Db {

    public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=cacanh;";
    public static String userDB = "sa";
    public static String passDB = "123";
    
    public static Connection getConnection() {
        Connection con = null;
        
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(dbURL, userDB, passDB);
            return con;
        } catch (Exception ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("Connect to JDBC success");
            }            
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
