package controller.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.entity.BlogPost;
import model.entity.BlogCategory;
import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;

/**
 * Quick debug servlet for header issue
 */
@WebServlet("/quick-debug")
public class QuickDebugServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Quick Debug</title>");
        out.println("<style>body{font-family:Arial;margin:20px;} .error{color:red;} .success{color:green;} .info{color:blue;}</style>");
        out.println("</head><body>");
        out.println("<h1>🔍 Quick Header Debug</h1>");
        
        try {
            BlogService blogService = new BlogServiceImpl();
            
            // Check categories
            out.println("<h2>Categories:</h2>");
            List<BlogCategory> categories = blogService.getAllCategories();
            out.println("<p class='info'>Total categories: " + categories.size() + "</p>");
            
            if (categories.isEmpty()) {
                out.println("<p class='error'>❌ NO CATEGORIES! Run: resources/database/insert_blog_categories.sql</p>");
            } else {
                for (BlogCategory cat : categories) {
                    String status = cat.isActive() ? "✅ Active" : "❌ Inactive";
                    out.println("<p>" + status + " " + cat.getCategoryName() + " (ID:" + cat.getCategoryId() + ")</p>");
                }
            }
            
            // Check posts
            out.println("<h2>Posts:</h2>");
            List<BlogPost> posts = blogService.getLatestPosts(5);
            out.println("<p class='info'>Latest posts: " + posts.size() + "</p>");
            
            if (posts.isEmpty()) {
                out.println("<p class='error'>❌ NO POSTS! This is why header shows 'Xem tất cả bài viết'</p>");
                out.println("<h3>🚀 Quick Fix SQL:</h3>");
                out.println("<pre style='background:#f5f5f5;padding:10px;'>");
                out.println("INSERT INTO blog_posts (title, content, summary, author_id, category, status, published_at) VALUES");
                out.println("(N'Test Post 1', N'Content 1', N'Summary 1', 1, N'Hướng dẫn', 'published', GETDATE()),");
                out.println("(N'Test Post 2', N'Content 2', N'Summary 2', 1, N'Tin tức', 'published', GETDATE()),");
                out.println("(N'Test Post 3', N'Content 3', N'Summary 3', 1, N'Khuyến mãi', 'published', GETDATE());");
                out.println("</pre>");
            } else {
                for (BlogPost post : posts) {
                    out.println("<p>📝 " + post.getTitle() + " (Status: " + post.getStatus() + ", Views: " + post.getViewCount() + ")</p>");
                }
            }
            
            // Simulate header data loading
            out.println("<h2>Header Data Simulation:</h2>");
            request.setAttribute("headerCategories", categories);
            request.setAttribute("headerLatestPosts", posts);
            
            out.println("<p class='success'>✅ headerCategories set: " + categories.size() + " items</p>");
            out.println("<p class='success'>✅ headerLatestPosts set: " + posts.size() + " items</p>");
            
            if (posts.isEmpty()) {
                out.println("<p class='error'>⚠️ This is why you see 'Xem tất cả bài viết' instead of post list</p>");
            }
            
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        String contextPath = request.getContextPath();
        out.println("<h2>🔗 Actions:</h2>");
        out.println("<p><a href='" + contextPath + "/home' style='color:blue;'>🏠 Go to Home (test header)</a></p>");
        out.println("<p><a href='" + contextPath + "/fix-header' style='color:blue;'>🔧 Fix Header Tool</a></p>");
        out.println("<p><a href='" + contextPath + "/admin/blog-form' style='color:blue;'>✏️ Add New Post (Admin)</a></p>");
        
        out.println("</body></html>");
    }
}
