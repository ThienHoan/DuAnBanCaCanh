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
 * Servlet to compare header data between home and blog pages
 */
@WebServlet("/test-header-comparison")
public class HeaderComparisonTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Header Data Comparison</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f8f9fa; }");
        out.println(".container { max-width: 1200px; margin: 0 auto; }");
        out.println(".comparison { display: flex; gap: 20px; }");
        out.println(".page-test { flex: 1; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println(".success { color: #28a745; background: #d4edda; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println(".error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println(".info { color: #0c5460; background: #d1ecf1; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println("h1 { color: #333; text-align: center; }");
        out.println("h2 { color: #555; border-bottom: 2px solid #ff6900; padding-bottom: 5px; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; background: #ff6900; color: white; text-decoration: none; border-radius: 4px; margin: 5px; }");
        out.println("table { width: 100%; border-collapse: collapse; margin: 10px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<div class='container'>");
        out.println("<h1>🔍 Header Data Comparison Test</h1>");
        out.println("<p style='text-align: center;'>This test checks if header data is loaded correctly on both Home and Blog pages.</p>");
        
        try {
            BlogService blogService = new BlogServiceImpl();
            
            // Test direct service calls
            out.println("<h2>📊 Direct Service Test</h2>");
            List<BlogCategory> categories = blogService.getAllCategories();
            List<BlogPost> posts = blogService.getLatestPosts(3);
            
            out.println("<div class='info'>");
            out.println("<strong>Direct BlogService Results:</strong><br>");
            out.println("📁 Total Categories: " + categories.size() + "<br>");
            out.println("📝 Latest Posts: " + posts.size() + "<br>");
            out.println("✅ Service Layer Working: " + (posts.size() > 0 ? "YES" : "NO"));
            out.println("</div>");
            
            // Show current data
            if (!posts.isEmpty()) {                out.println("<h3>📝 Available Posts:</h3>");
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Title</th><th>Category</th><th>Status</th><th>Views</th></tr>");
                for (BlogPost post : posts) {
                    out.println("<tr>");
                    out.println("<td>" + post.getPostId() + "</td>");
                    out.println("<td>" + post.getTitle() + "</td>");
                    out.println("<td>" + (post.getCategoryObject() != null ? post.getCategoryObject().getCategoryName() : "N/A") + "</td>");
                    out.println("<td>" + post.getStatus() + "</td>");
                    out.println("<td>" + post.getViewCount() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<div class='error'>❌ No posts found! This explains why header shows fallback text.</div>");
            }
            
            // Simulate header data loading for both pages
            out.println("<div class='comparison'>");
            
            // Home page simulation
            out.println("<div class='page-test'>");
            out.println("<h2>🏠 Home Page Simulation</h2>");
            
            // Simulate HomeServlet logic
            List<BlogCategory> homeHeaderCategories = new java.util.ArrayList<>();
            int count = 0;
            for (BlogCategory cat : categories) {
                if (cat.isActive() && !cat.isDeleted() && count < 6) {
                    homeHeaderCategories.add(cat);
                    count++;
                }
            }
            List<BlogPost> homeHeaderPosts = blogService.getLatestPosts(3);
            
            out.println("<div class='success'>");
            out.println("✅ homeHeaderCategories: " + homeHeaderCategories.size() + " items<br>");
            out.println("✅ homeHeaderPosts: " + homeHeaderPosts.size() + " items");
            out.println("</div>");
            
            if (homeHeaderPosts.isEmpty()) {
                out.println("<div class='error'>❌ This is why Home shows 'Chưa có bài viết'</div>");
            } else {
                out.println("<div class='info'>ℹ️ Home should show " + homeHeaderPosts.size() + " posts in header</div>");
            }
            
            out.println("</div>");
            
            // Blog page simulation
            out.println("<div class='page-test'>");
            out.println("<h2>📝 Blog Page Simulation</h2>");
            
            // Simulate BlogController logic
            List<BlogCategory> blogHeaderCategories = new java.util.ArrayList<>();
            count = 0;
            for (BlogCategory cat : categories) {
                if (cat.isActive() && !cat.isDeleted() && count < 6) {
                    blogHeaderCategories.add(cat);
                    count++;
                }
            }
            List<BlogPost> blogHeaderPosts = blogService.getLatestPosts(3);
            
            out.println("<div class='success'>");
            out.println("✅ blogHeaderCategories: " + blogHeaderCategories.size() + " items<br>");
            out.println("✅ blogHeaderPosts: " + blogHeaderPosts.size() + " items");
            out.println("</div>");
            
            if (blogHeaderPosts.isEmpty()) {
                out.println("<div class='error'>❌ This is why Blog shows 'Xem tất cả bài viết'</div>");
            } else {
                out.println("<div class='info'>ℹ️ Blog should show " + blogHeaderPosts.size() + " posts in header</div>");
            }
            
            out.println("</div>");
            
            out.println("</div>");
            
            // Analysis
            out.println("<h2>🔬 Analysis & Recommendations</h2>");
            
            if (posts.isEmpty()) {
                out.println("<div class='error'>");
                out.println("<h3>❌ Root Cause: No Published Posts</h3>");
                out.println("<p>Both pages will show fallback text because there are no published blog posts in the database.</p>");
                out.println("<p><strong>Solution:</strong> Add blog posts with status='published'</p>");
                out.println("</div>");
                
                out.println("<h3>🚀 Quick Fix SQL:</h3>");
                out.println("<pre style='background:#f5f5f5;padding:15px;border-radius:4px;'>");
                out.println("INSERT INTO blog_posts (title, content, summary, author_id, category, status, published_at) VALUES");
                out.println("(N'Kiến thức cơ bản cho người mới chơi cá cảnh', N'&lt;p&gt;Hướng dẫn chi tiết...&lt;/p&gt;', N'Tóm tắt bài viết', 1, N'Hướng dẫn', 'published', GETDATE()),");
                out.println("(N'Giới thiệu shop cá cảnh uy tín', N'&lt;p&gt;Nội dung giới thiệu...&lt;/p&gt;', N'Tóm tắt bài viết', 1, N'Tin tức', 'published', GETDATE()),");
                out.println("(N'Cách nuôi cá Betta hiệu quả', N'&lt;p&gt;Hướng dẫn nuôi cá...&lt;/p&gt;', N'Tóm tắt bài viết', 1, N'Hướng dẫn', 'published', GETDATE());");
                out.println("</pre>");
            } else {
                out.println("<div class='success'>");
                out.println("<h3>✅ Data Available</h3>");
                out.println("<p>Posts are available, so headers should display correctly after the HomeServlet update.</p>");
                out.println("</div>");
            }
            
        } catch (Exception e) {
            out.println("<div class='error'>");
            out.println("<h3>❌ Error</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
            e.printStackTrace(out);
        }
        
        out.println("<h2>🔗 Test Links</h2>");
        String contextPath = request.getContextPath();
        out.println("<a href='" + contextPath + "/home' class='btn' target='_blank'>🏠 Test Home Page</a>");
        out.println("<a href='" + contextPath + "/blog' class='btn' target='_blank'>📝 Test Blog Page</a>");
        out.println("<a href='" + contextPath + "/quick-debug' class='btn'>🔍 Quick Debug</a>");
        out.println("<a href='" + contextPath + "/admin/blog-form' class='btn'>✏️ Add New Post</a>");
        
        out.println("<div class='info' style='margin-top: 20px;'>");
        out.println("<h3>📋 Testing Steps:</h3>");
        out.println("1. If no posts shown above, run the SQL script<br>");
        out.println("2. Test Home page - should show posts in header menu<br>");
        out.println("3. Test Blog page - should show same posts<br>");
        out.println("4. Check console logs for any errors<br>");
        out.println("5. Clear browser cache if needed");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body></html>");
    }
}
