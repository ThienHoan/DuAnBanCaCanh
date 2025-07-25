package controller.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Test servlet để debug profile issues
 */
@WebServlet(name = "ProfileTestServlet", urlPatterns = {"/profile-debug"})
public class ProfileTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Profile Debug</title></head>");
        out.println("<body>");
        out.println("<h1>Profile Debug Information</h1>");
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        out.println("<h2>Session Info:</h2>");
        out.println("<p>Session ID: " + session.getId() + "</p>");
        out.println("<p>Session is new: " + session.isNew() + "</p>");
        out.println("<p>Max inactive interval: " + session.getMaxInactiveInterval() + "</p>");
        
        if (currentUser != null) {
            out.println("<h2>User Info:</h2>");
            out.println("<p>User ID: " + currentUser.getUserId() + "</p>");
            out.println("<p>Username: " + currentUser.getUsername() + "</p>");
            out.println("<p>Full Name: " + currentUser.getFullName() + "</p>");
            out.println("<p>Email: " + currentUser.getEmail() + "</p>");
            out.println("<p>Role: " + currentUser.getRole() + "</p>");
            out.println("<p>Status: " + currentUser.getStatus() + "</p>");
            
            out.println("<h2>Test Controllers:</h2>");
            out.println("<p><a href='" + request.getContextPath() + "/profile'>Test ProfileController</a></p>");
            out.println("<p><a href='" + request.getContextPath() + "/dashboard'>Test DashboardController</a></p>");
            
        } else {
            out.println("<h2>No user in session</h2>");
            out.println("<p><a href='" + request.getContextPath() + "/login'>Login first</a></p>");
        }
        
        out.println("<h2>Request Info:</h2>");
        out.println("<p>Context Path: " + request.getContextPath() + "</p>");
        out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
        out.println("<p>Request URL: " + request.getRequestURL() + "</p>");
        out.println("<p>Server Info: " + getServletContext().getServerInfo() + "</p>");
        
        out.println("</body>");
        out.println("</html>");
    }
}
