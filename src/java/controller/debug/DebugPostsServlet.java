package controller.debug;

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
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/debug-posts")
public class DebugPostsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DebugPostsServlet.class.getName());
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
        out.println("<title>Debug Posts</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Debug: All Posts in Database</h1>");
        
        try {
            // Check if user is logged in as admin
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    out.println("<p>Current user: " + user.getUsername() + " (ID: " + user.getUserId() + ", Role: " + user.getRole() + ")</p>");
                    
                    // Get all posts for admin (including drafts)
                    List<BlogPost> allPosts = blogService.getAllPostsForAdmin(user.getUserId());
                    out.println("<h2>All Posts for Admin (" + allPosts.size() + " posts)</h2>");
                    
                    if (allPosts.isEmpty()) {
                        out.println("<p>No posts found.</p>");
                    } else {
                        out.println("<table>");
                        out.println("<tr>");
                        out.println("<th>ID</th>");
                        out.println("<th>Title</th>");
                        out.println("<th>Status</th>");
                        out.println("<th>Category ID</th>");
                        out.println("<th>Category Name</th>");
                        out.println("<th>Author ID</th>");
                        out.println("<th>Created At</th>");
                        out.println("<th>Is Deleted</th>");
                        out.println("</tr>");
                        
                        for (BlogPost post : allPosts) {
                            out.println("<tr>");
                            out.println("<td>" + post.getPostId() + "</td>");                            out.println("<td>" + (post.getTitle() != null ? post.getTitle() : "NULL") + "</td>");
                            out.println("<td>" + (post.getStatus() != null ? post.getStatus() : "NULL") + "</td>");
                            out.println("<td>" + post.getCategoryId() + "</td>");
                            out.println("<td>" + (post.getCategoryObject() != null ? post.getCategoryObject().getCategoryName() : "NULL") + "</td>");
                            out.println("<td>" + post.getAuthorId() + "</td>");
                            out.println("<td>" + (post.getCreatedAt() != null ? post.getCreatedAt().toString() : "NULL") + "</td>");
                            out.println("<td>" + post.isDeleted() + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }
                }
            }
            
            // Also get published posts
            List<BlogPost> publishedPosts = blogService.getAllPublishedPosts();
            out.println("<h2>Published Posts (" + publishedPosts.size() + " posts)</h2>");
            
            if (publishedPosts.isEmpty()) {
                out.println("<p>No published posts found.</p>");
            } else {
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>ID</th>");
                out.println("<th>Title</th>");
                out.println("<th>Status</th>");
                out.println("<th>Category</th>");
                out.println("<th>Published At</th>");
                out.println("</tr>");
                
                for (BlogPost post : publishedPosts) {                    out.println("<tr>");
                    out.println("<td>" + post.getPostId() + "</td>");
                    out.println("<td>" + (post.getTitle() != null ? post.getTitle() : "NULL") + "</td>");
                    out.println("<td>" + (post.getStatus() != null ? post.getStatus() : "NULL") + "</td>");
                    out.println("<td>" + (post.getCategoryObject() != null ? post.getCategoryObject().getCategoryName() : "NULL") + "</td>");
                    out.println("<td>" + (post.getPublishedAt() != null ? post.getPublishedAt().toString() : "NULL") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            LOGGER.severe("Error in debug posts servlet: " + e.getMessage());
        }
        
        out.println("<p><a href='/admin-blog'>Back to Admin Blog</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}
