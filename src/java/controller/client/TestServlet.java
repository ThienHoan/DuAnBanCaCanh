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
@WebServlet(name = "TestServlet", urlPatterns = {"/test-profile"})
public class TestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Profile Test Debug</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println(".info { background: #e8f5e8; padding: 15px; margin: 10px 0; border-radius: 5px; }");
            out.println(".error { background: #ffe8e8; padding: 15px; margin: 10px 0; border-radius: 5px; }");
            out.println(".warning { background: #fff8e1; padding: 15px; margin: 10px 0; border-radius: 5px; }");
            out.println("a { color: #007bff; text-decoration: none; margin-right: 15px; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Profile System Debug Information</h1>");
            
            // Session info
            out.println("<div class='info'>");
            out.println("<h2>Session Information</h2>");
            out.println("<p><strong>Session ID:</strong> " + session.getId() + "</p>");
            out.println("<p><strong>Session Creation Time:</strong> " + new java.util.Date(session.getCreationTime()) + "</p>");
            out.println("<p><strong>Last Accessed:</strong> " + new java.util.Date(session.getLastAccessedTime()) + "</p>");
            out.println("</div>");
            
            // User info
            if (user != null) {
                out.println("<div class='info'>");
                out.println("<h2>User Information</h2>");
                out.println("<p><strong>User ID:</strong> " + user.getUserId() + "</p>");
                out.println("<p><strong>Username:</strong> " + user.getUsername() + "</p>");
                out.println("<p><strong>Full Name:</strong> " + user.getFullName() + "</p>");
                out.println("<p><strong>Email:</strong> " + user.getEmail() + "</p>");
                out.println("<p><strong>Role:</strong> " + user.getRole() + "</p>");
                out.println("<p><strong>Status:</strong> " + user.getStatus() + "</p>");
                out.println("</div>");
            } else {
                out.println("<div class='error'>");
                out.println("<h2>No User Found</h2>");
                out.println("<p>No user found in session. Please login first.</p>");
                out.println("</div>");
            }
            
            // Context Path info
            out.println("<div class='info'>");
            out.println("<h2>Request Information</h2>");
            out.println("<p><strong>Context Path:</strong> " + request.getContextPath() + "</p>");
            out.println("<p><strong>Request URI:</strong> " + request.getRequestURI() + "</p>");
            out.println("<p><strong>Server Name:</strong> " + request.getServerName() + "</p>");
            out.println("<p><strong>Server Port:</strong> " + request.getServerPort() + "</p>");
            out.println("</div>");
            
            // Test links
            out.println("<div class='warning'>");
            out.println("<h2>Test Profile Links</h2>");
            out.println("<p>Click these links to test profile functionality:</p>");
            out.println("<a href='" + request.getContextPath() + "/profile'>Profile</a>");
            out.println("<a href='" + request.getContextPath() + "/dashboard'>Dashboard</a>");
            out.println("<a href='" + request.getContextPath() + "/profile?action=edit'>Edit Profile</a>");
            out.println("<a href='" + request.getContextPath() + "/profile?action=change-password'>Change Password</a>");
            out.println("<br><br>");
            out.println("<a href='" + request.getContextPath() + "/login'>Login</a>");
            out.println("<a href='" + request.getContextPath() + "/logout'>Logout</a>");
            out.println("</div>");
            
            // File existence check
            out.println("<div class='info'>");
            out.println("<h2>File Existence Check</h2>");
            String[] filesToCheck = {
                "/client/account/profile.jsp",
                "/client/account/dashboard.jsp", 
                "/client/account/edit-profile.jsp",
                "/client/account/change-password.jsp"
            };
            
            for (String file : filesToCheck) {
                String realPath = getServletContext().getRealPath(file);
                boolean exists = realPath != null && new java.io.File(realPath).exists();
                out.println("<p><strong>" + file + ":</strong> " + 
                           (exists ? "<span style='color: green;'>EXISTS</span>" : 
                                   "<span style='color: red;'>NOT FOUND</span>") + "</p>");
            }
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            out.println("<div class='error'>");
            out.println("<h2>Exception Occurred</h2>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</div>");
        } finally {
            out.close();
        }
    }
}
