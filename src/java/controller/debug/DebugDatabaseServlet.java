package controller.debug;

import utils.db.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/debug-database")
public class DebugDatabaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Debug Database</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Debug: Direct Database Query</h1>");
        
        try {
            // Test connection
            out.println("<h2>Database Connection Test</h2>");
            try (Connection conn = DBContext.getConnection()) {
                if (conn != null) {
                    out.println("<p style='color: green;'>✓ Database connection successful</p>");
                    out.println("<p>Connection: " + conn.getMetaData().getURL() + "</p>");
                } else {
                    out.println("<p style='color: red;'>✗ Database connection failed</p>");
                }
            }
            
            // Check blog_posts table
            out.println("<h2>Blog Posts Table (Raw Query)</h2>");
            String sql = "SELECT TOP 10 post_id, title, status, category_id, author_id, created_at, is_deleted FROM blog_posts ORDER BY created_at DESC";
            
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>Post ID</th>");
                out.println("<th>Title</th>");
                out.println("<th>Status</th>");
                out.println("<th>Category ID</th>");
                out.println("<th>Author ID</th>");
                out.println("<th>Created At</th>");
                out.println("<th>Is Deleted</th>");
                out.println("</tr>");
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("post_id") + "</td>");
                    out.println("<td>" + (rs.getString("title") != null ? rs.getString("title") : "NULL") + "</td>");
                    out.println("<td>" + (rs.getString("status") != null ? rs.getString("status") : "NULL") + "</td>");
                    out.println("<td>" + rs.getInt("category_id") + "</td>");
                    out.println("<td>" + rs.getInt("author_id") + "</td>");
                    out.println("<td>" + (rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : "NULL") + "</td>");
                    out.println("<td>" + rs.getBoolean("is_deleted") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("<p>Total posts found: " + count + "</p>");
                
                if (count == 0) {
                    out.println("<p style='color: red;'>No posts found in database!</p>");
                }
            }
            
            // Check blog_categories table
            out.println("<h2>Blog Categories Table</h2>");
            String categorySql = "SELECT category_id, category_name, is_active FROM blog_categories ORDER BY category_id";
            
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(categorySql);
                 ResultSet rs = stmt.executeQuery()) {
                
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>Category ID</th>");
                out.println("<th>Category Name</th>");
                out.println("<th>Is Active</th>");
                out.println("</tr>");
                
                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("category_id") + "</td>");
                    out.println("<td>" + (rs.getString("category_name") != null ? rs.getString("category_name") : "NULL") + "</td>");
                    out.println("<td>" + rs.getBoolean("is_active") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
        
        out.println("<p><a href='/admin-blog'>Back to Admin Blog</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}
