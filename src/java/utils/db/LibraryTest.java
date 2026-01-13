
package utils.db;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import com.google.genai.Client;
import com.google.gson.Gson;

public class LibraryTest {
    public static void main(String[] args) {
        // Test GenAI SDK
        try {
            Client client = Client.builder()
                .apiKey(System.getProperty("GOOGLE_API_KEY"))
                .build();
            System.out.println("✓ GenAI SDK loaded");
        } catch (Exception e) {
            System.out.println("✗ GenAI SDK error: " + e.getMessage());
        }
        
        // Test JDBC Driver
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✓ SQL Server JDBC loaded");
        } catch (Exception e) {
            System.out.println("✗ JDBC error: " + e.getMessage());
        }
        
        // Test Gson
        try {
            new Gson();
            System.out.println("✓ Gson loaded");
        } catch (Exception e) {
            System.out.println("✗ Gson error: " + e.getMessage());
        }
    }
}



