package controller.test;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Simple test to check if servlet is being called
 */
@WebServlet("/test-home-call")
public class TestHomeCallServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Test Home Call</title></head><body>");
        out.println("<h1>🧪 Test Home Servlet Call</h1>");
        
        // Test if we can manually call HomeServlet logic
        try {
            // Forward to home servlet and capture what happens
            out.println("<h2>Testing Home Servlet...</h2>");
            out.println("<p>Manually calling HomeServlet logic...</p>");
            
            // Try to simulate a call to /home
            response.sendRedirect(request.getContextPath() + "/home");
            
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("</body></html>");
    }
}
