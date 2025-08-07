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
import utils.ConfigUtil;

/**
 * Servlet để test toàn bộ hệ thống blog
 */
@WebServlet("/blog-system-test")
public class BlogSystemTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Blog System Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".test-section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println(".info { color: blue; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<h1>Blog System Comprehensive Test</h1>");
        
        try {
            BlogService blogService = new BlogServiceImpl();
            
            // Test 1: Configuration
            out.println("<div class='test-section'>");
            out.println("<h2>1. Configuration Test</h2>");
            try {
                String pageSize = ConfigUtil.getProperty("blog.page.size", "10");
                String tinymceKey = ConfigUtil.getProperty("tinymce.api.key", "NOT_SET");
                out.println("<p class='success'>✓ Config loaded successfully</p>");
                out.println("<p class='info'>Blog Page Size: " + pageSize + "</p>");
                out.println("<p class='info'>TinyMCE Key: " + (tinymceKey.equals("NOT_SET") ? "NOT_SET" : "SET") + "</p>");
            } catch (Exception e) {
                out.println("<p class='error'>✗ Config error: " + e.getMessage() + "</p>");
            }
            out.println("</div>");
            
            // Test 2: Categories
            out.println("<div class='test-section'>");
            out.println("<h2>2. Categories Test</h2>");            try {
                List<BlogCategory> categories = blogService.getAllCategories();
                // Filter active categories manually
                List<BlogCategory> activeCategories = new java.util.ArrayList<>();
                for (BlogCategory cat : categories) {
                    if (cat.isActive()) {
                        activeCategories.add(cat);
                    }
                }
                
                out.println("<p class='success'>✓ Categories loaded successfully</p>");
                out.println("<p class='info'>Total Categories: " + categories.size() + "</p>");
                out.println("<p class='info'>Active Categories: " + activeCategories.size() + "</p>");
                
                if (!activeCategories.isEmpty()) {
                    out.println("<table>");
                    out.println("<tr><th>ID</th><th>Name</th><th>Slug</th><th>Status</th></tr>");
                    for (BlogCategory cat : activeCategories) {
                        out.println("<tr>");
                        out.println("<td>" + cat.getCategoryId() + "</td>");
                        out.println("<td>" + cat.getCategoryName() + "</td>");
                        out.println("<td>" + (cat.getSlug() != null ? cat.getSlug() : "N/A") + "</td>");
                        out.println("<td>" + (cat.isActive() ? "Active" : "Inactive") + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                }
            } catch (Exception e) {
                out.println("<p class='error'>✗ Categories error: " + e.getMessage() + "</p>");
                e.printStackTrace(out);
            }
            out.println("</div>");
            
            // Test 3: Posts
            out.println("<div class='test-section'>");
            out.println("<h2>3. Posts Test</h2>");            try {
                int totalPosts = blogService.getTotalPostsCount();
                List<BlogPost> recentPosts = blogService.getLatestPosts(5);
                List<BlogPost> pagedPosts = blogService.getPostsWithPagination(1, 3);
                
                out.println("<p class='success'>✓ Posts loaded successfully</p>");
                out.println("<p class='info'>Total Published Posts: " + totalPosts + "</p>");
                out.println("<p class='info'>Recent Posts: " + recentPosts.size() + "</p>");
                out.println("<p class='info'>Paged Posts (page 1, size 3): " + pagedPosts.size() + "</p>");
                
                if (!recentPosts.isEmpty()) {                    out.println("<table>");
                    out.println("<tr><th>ID</th><th>Title</th><th>Category</th><th>Status</th><th>Published</th></tr>");
                    for (BlogPost post : recentPosts) {
                        out.println("<tr>");
                        out.println("<td>" + post.getPostId() + "</td>");
                        out.println("<td>" + post.getTitle() + "</td>");
                        out.println("<td>" + (post.getCategoryObject() != null ? post.getCategoryObject().getCategoryName() : "N/A") + "</td>");
                        out.println("<td>" + post.getStatus() + "</td>");
                        out.println("<td>" + (post.getPublishedAt() != null ? post.getPublishedAt() : "N/A") + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                }
            } catch (Exception e) {
                out.println("<p class='error'>✗ Posts error: " + e.getMessage() + "</p>");
                e.printStackTrace(out);
            }
            out.println("</div>");
            
            // Test 4: Pagination
            out.println("<div class='test-section'>");
            out.println("<h2>4. Pagination Test</h2>");            try {
                int pageSize = Integer.parseInt(ConfigUtil.getProperty("blog.page.size", "10"));
                int totalPosts = blogService.getTotalPostsCount();
                int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
                
                out.println("<p class='success'>✓ Pagination calculated successfully</p>");
                out.println("<p class='info'>Page Size: " + pageSize + "</p>");
                out.println("<p class='info'>Total Posts: " + totalPosts + "</p>");
                out.println("<p class='info'>Total Pages: " + totalPages + "</p>");
                
                // Test different pages
                for (int page = 1; page <= Math.min(3, totalPages); page++) {
                    List<BlogPost> pagePosts = blogService.getPostsWithPagination(page, pageSize);
                    out.println("<p class='info'>Page " + page + " posts: " + pagePosts.size() + "</p>");
                }
            } catch (Exception e) {
                out.println("<p class='error'>✗ Pagination error: " + e.getMessage() + "</p>");
                e.printStackTrace(out);
            }
            out.println("</div>");
            
            // Test 5: Links
            out.println("<div class='test-section'>");
            out.println("<h2>5. Navigation Links Test</h2>");
            String contextPath = request.getContextPath();
            out.println("<p class='info'>Context Path: " + contextPath + "</p>");
            out.println("<h3>Client Links:</h3>");
            out.println("<ul>");
            out.println("<li><a href='" + contextPath + "/blog' target='_blank'>Blog List</a></li>");
            out.println("<li><a href='" + contextPath + "/blog-detail?id=1' target='_blank'>Blog Detail (ID=1)</a></li>");
            out.println("<li><a href='" + contextPath + "/blog-search?query=test' target='_blank'>Blog Search</a></li>");
            out.println("</ul>");
            
            out.println("<h3>Admin Links:</h3>");
            out.println("<ul>");
            out.println("<li><a href='" + contextPath + "/admin-dashboard' target='_blank'>Admin Dashboard</a></li>");
            out.println("<li><a href='" + contextPath + "/admin/blog-list' target='_blank'>Admin Blog List</a></li>");
            out.println("<li><a href='" + contextPath + "/admin/blog-categories' target='_blank'>Admin Categories</a></li>");
            out.println("</ul>");
            
            out.println("<h3>Test Links:</h3>");
            out.println("<ul>");
            out.println("<li><a href='" + contextPath + "/test-categories' target='_blank'>Test Categories</a></li>");
            out.println("<li><a href='" + contextPath + "/blog-debug' target='_blank'>Blog Debug</a></li>");
            out.println("</ul>");
            out.println("</div>");
            
        } catch (Exception e) {
            out.println("<div class='test-section'>");
            out.println("<h2>System Error</h2>");
            out.println("<p class='error'>✗ System error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
            out.println("</div>");
        }
        
        out.println("<div class='test-section'>");
        out.println("<h2>Next Steps</h2>");
        out.println("<ol>");
        out.println("<li>Verify all links above work correctly</li>");
        out.println("<li>Check header menu displays categories</li>");
        out.println("<li>Test responsive design on different devices</li>");
        out.println("<li>Test admin functionality with admin account</li>");
        out.println("<li>Verify database foreign key relationships</li>");
        out.println("</ol>");
        out.println("</div>");
        
        out.println("</body>");
        out.println("</html>");
    }
}
