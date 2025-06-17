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
 * Servlet để fix vấn đề header không hiển thị featured/recent posts
 */
@WebServlet("/fix-header")
public class FixHeaderServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Fix Header Issue</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
        out.println(".container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println(".success { color: #28a745; background: #d4edda; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println(".error { color: #dc3545; background: #f8d7da; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println(".info { color: #0c5460; background: #d1ecf1; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println(".warning { color: #856404; background: #fff3cd; padding: 10px; border-radius: 4px; margin: 10px 0; }");
        out.println("h1 { color: #333; border-bottom: 3px solid #ff6900; padding-bottom: 10px; }");
        out.println("h2 { color: #555; margin-top: 30px; }");
        out.println("pre { background: #f8f9fa; padding: 15px; border-radius: 4px; overflow-x: auto; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; background: #ff6900; color: white; text-decoration: none; border-radius: 4px; margin: 5px; }");
        out.println(".btn:hover { background: #e55a00; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<div class='container'>");
        out.println("<h1>🔧 Fix Header Featured/Recent Posts</h1>");
        
        try {
            BlogService blogService = new BlogServiceImpl();
            
            // Check current status
            out.println("<h2>📊 Current Status Check</h2>");
            
            // Check categories
            List<BlogCategory> allCategories = blogService.getAllCategories();
            List<BlogCategory> activeCategories = new java.util.ArrayList<>();
            for (BlogCategory cat : allCategories) {
                if (cat.isActive() && !cat.isDeleted()) {
                    activeCategories.add(cat);
                }
            }
            
            if (activeCategories.isEmpty()) {
                out.println("<div class='error'>❌ No active categories found!</div>");
            } else {
                out.println("<div class='success'>✅ Found " + activeCategories.size() + " active categories</div>");
                for (BlogCategory cat : activeCategories) {
                    out.println("<div class='info'>📁 " + cat.getCategoryName() + " (ID: " + cat.getCategoryId() + ")</div>");
                }
            }
            
            // Check posts
            List<BlogPost> latestPosts = blogService.getLatestPosts(3);
            int totalPosts = blogService.getTotalPostsCount();
            
            if (latestPosts.isEmpty()) {
                out.println("<div class='error'>❌ No published posts found! This is why header shows 'Xem tất cả bài viết'</div>");
                out.println("<div class='warning'>📝 You need to add blog posts to see Featured/Recent sections</div>");
                
                out.println("<h2>🚀 Quick Fix - Run SQL Script</h2>");
                out.println("<div class='info'>Run this SQL script in your database to add sample posts:</div>");
                out.println("<pre>");
                out.println("-- File: resources/database/insert_sample_blog_posts.sql");
                out.println("-- Or run this directly:");
                out.println("");
                out.println("INSERT INTO blog_posts (title, content, summary, author_id, author_name, category, status, published_at) VALUES");
                out.println("(N'Sample Post 1', N'Content 1', N'Summary 1', 1, N'Admin', N'Hướng dẫn', 'published', GETDATE()),");
                out.println("(N'Sample Post 2', N'Content 2', N'Summary 2', 1, N'Admin', N'Tin tức', 'published', GETDATE()),");
                out.println("(N'Sample Post 3', N'Content 3', N'Summary 3', 1, N'Admin', N'Khuyến mãi', 'published', GETDATE());");
                out.println("</pre>");
                
            } else {
                out.println("<div class='success'>✅ Found " + latestPosts.size() + " published posts (Total: " + totalPosts + ")</div>");
                out.println("<h3>📄 Latest Posts:</h3>");
                for (BlogPost post : latestPosts) {
                    out.println("<div class='info'>📖 " + post.getTitle() + " (Status: " + post.getStatus() + ", Published: " + post.getPublishedAt() + ")</div>");
                }
            }
            
            // Simulate header loading
            out.println("<h2>🔄 Header Simulation Test</h2>");
            
            // Manually set header attributes
            List<BlogCategory> headerCategories = new java.util.ArrayList<>();
            int count = 0;
            for (BlogCategory cat : allCategories) {
                if (cat.isActive() && !cat.isDeleted() && count < 6) {
                    headerCategories.add(cat);
                    count++;
                }
            }
            
            request.setAttribute("headerCategories", headerCategories);
            request.setAttribute("headerLatestPosts", latestPosts);
            
            out.println("<div class='success'>✅ Header attributes set:</div>");
            out.println("<div class='info'>🏷️ headerCategories: " + headerCategories.size() + " items</div>");
            out.println("<div class='info'>📰 headerLatestPosts: " + latestPosts.size() + " items</div>");
            
            if (latestPosts.isEmpty()) {
                out.println("<div class='warning'>⚠️ headerLatestPosts is empty - this is why you see 'Xem tất cả bài viết' instead of post list</div>");
            }
            
            // Test JSP EL expressions
            out.println("<h2>🧪 JSP EL Test</h2>");
            out.println("<div class='info'>Testing JSP EL expressions that would be used in header.jsp:</div>");
            out.println("<pre>");
            out.println("${not empty headerLatestPosts} = " + (!latestPosts.isEmpty()));
            out.println("${headerLatestPosts.size()} = " + latestPosts.size());
            out.println("${headerCategories.size()} = " + headerCategories.size());
            out.println("</pre>");
            
        } catch (Exception e) {
            out.println("<div class='error'>❌ Error: " + e.getMessage() + "</div>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
        
        out.println("<h2>🎯 Solutions</h2>");
        out.println("<div class='info'>");
        out.println("<h3>If no posts found:</h3>");
        out.println("1. <a href='#' class='btn'>Run SQL script: insert_sample_blog_posts.sql</a><br>");
        out.println("2. <a href='/DuAnCaCanh/admin/blog-form' class='btn'>Create posts via Admin</a><br>");
        out.println("3. <a href='/DuAnCaCanh/test-header-data' class='btn'>Test Header Data</a>");
        out.println("</div>");
        
        out.println("<div class='info'>");
        out.println("<h3>If posts exist but not showing:</h3>");
        out.println("• Check post status = 'published'<br>");
        out.println("• Check is_deleted = 0<br>");
        out.println("• Check published_at is not null<br>");
        out.println("• Clear browser cache and refresh");
        out.println("</div>");
        
        out.println("<h2>🔗 Test Links</h2>");
        String contextPath = request.getContextPath();
        out.println("<a href='" + contextPath + "/home' class='btn'>🏠 Home (Check Header)</a>");
        out.println("<a href='" + contextPath + "/blog' class='btn'>📝 Blog Page</a>");
        out.println("<a href='" + contextPath + "/test-header-data' class='btn'>🧪 Header Data Test</a>");
        out.println("<a href='" + contextPath + "/blog-system-test' class='btn'>🔬 System Test</a>");
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
