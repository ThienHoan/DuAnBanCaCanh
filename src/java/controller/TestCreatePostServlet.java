package controller;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import model.entity.BlogPost;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/test-create-post")
public class TestCreatePostServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TestCreatePostServlet.class.getName());
    private BlogService blogService;

    @Override
    public void init() throws ServletException {
        super.init();
        blogService = new BlogServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Test Create Post</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Test Create Post</h1>");
        
        try {
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            if (session == null) {
                out.println("<p>No session found. Please <a href='/login'>login</a> first.</p>");
                out.println("</body></html>");
                return;
            }
            
            User user = (User) session.getAttribute("user");
            if (user == null) {
                out.println("<p>No user in session. Please <a href='/login'>login</a> first.</p>");
                out.println("</body></html>");
                return;
            }
            
            out.println("<p>Current user: " + user.getUsername() + " (ID: " + user.getUserId() + ", Role: " + user.getRole() + ")</p>");
            
            // Test if user is admin
            boolean isAdmin = blogService.isUserAdmin(user.getUserId());
            out.println("<p>Is admin: " + isAdmin + "</p>");
            
            if (!isAdmin) {
                out.println("<p>User is not admin, cannot create post.</p>");
                out.println("</body></html>");
                return;
            }
            
            // Create a test post
            BlogPost testPost = new BlogPost();
            testPost.setTitle("Test Post - " + System.currentTimeMillis());
            testPost.setContent("This is a test post content.");
            testPost.setSummary("Test summary");
            testPost.setFeaturedImage("http://example.com/image.jpg");
            testPost.setAuthorId(user.getUserId());
            testPost.setCategoryId(1); // Assuming category ID 1 exists
            testPost.setTags("test, debug");
            testPost.setStatus("draft");
            
            out.println("<h2>Creating test post...</h2>");
            out.println("<p>Title: " + testPost.getTitle() + "</p>");
            out.println("<p>Category ID: " + testPost.getCategoryId() + "</p>");
            out.println("<p>Status: " + testPost.getStatus() + "</p>");
            out.println("<p>Author ID: " + testPost.getAuthorId() + "</p>");
            
            boolean success = blogService.createPost(testPost, user.getUserId());
            out.println("<h2>Result: " + (success ? "SUCCESS" : "FAILED") + "</h2>");
            
            if (success) {
                out.println("<p>Post created successfully!</p>");
                out.println("<p><a href='/admin-blog'>Go to Admin Blog List</a></p>");
            } else {
                out.println("<p>Failed to create post. Check server logs for details.</p>");
            }
            
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            LOGGER.severe("Error in test create post: " + e.getMessage());
            e.printStackTrace();
        }
        
        out.println("</body>");
        out.println("</html>");
    }
}
