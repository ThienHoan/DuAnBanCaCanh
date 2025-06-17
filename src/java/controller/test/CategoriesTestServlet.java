package controller.test;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import model.entity.BlogCategory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Test servlet để kiểm tra categories
 */
@WebServlet("/test-categories")
public class CategoriesTestServlet extends HttpServlet {
    
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
        
        try {
            List<BlogCategory> categories = blogService.getAllCategories();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Categories Test</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("table { border-collapse: collapse; width: 100%; margin: 10px 0; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println(".active { color: green; font-weight: bold; }");
            out.println(".inactive { color: red; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Blog Categories Test</h1>");
            out.println("<p><strong>Total Categories Found: " + categories.size() + "</strong></p>");
            
            if (categories.isEmpty()) {
                out.println("<div style='background: #fff3cd; padding: 15px; border: 1px solid #ffeaa7; border-radius: 4px;'>");
                out.println("<h3>⚠️ No Categories Found!</h3>");
                out.println("<p>Possible reasons:</p>");
                out.println("<ul>");
                out.println("<li>Database table 'blog_categories' is empty</li>");
                out.println("<li>All categories have is_deleted = 1</li>");
                out.println("<li>Database connection issue</li>");
                out.println("<li>SQL query error</li>");
                out.println("</ul>");
                out.println("</div>");
            } else {
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>ID</th>");
                out.println("<th>Category Name</th>");
                out.println("<th>Slug</th>");
                out.println("<th>Description</th>");
                out.println("<th>Active</th>");
                out.println("<th>Created At</th>");
                out.println("</tr>");
                
                for (BlogCategory category : categories) {
                    out.println("<tr>");
                    out.println("<td>" + category.getCategoryId() + "</td>");
                    out.println("<td>" + (category.getCategoryName() != null ? category.getCategoryName() : "N/A") + "</td>");
                    out.println("<td>" + (category.getSlug() != null ? category.getSlug() : "N/A") + "</td>");
                    out.println("<td>" + (category.getDescription() != null ? category.getDescription() : "N/A") + "</td>");
                    out.println("<td class='" + (category.isActive() ? "active" : "inactive") + "'>" + 
                              (category.isActive() ? "✓ Active" : "✗ Inactive") + "</td>");
                    out.println("<td>" + (category.getCreatedAt() != null ? category.getCreatedAt() : "N/A") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                
                // Show which categories will appear in dropdown
                out.println("<h2>Categories for Dropdown Filter</h2>");
                List<BlogCategory> activeCategories = categories.stream()
                    .filter(cat -> cat.isActive())
                    .toList();
                
                if (activeCategories.isEmpty()) {
                    out.println("<div style='background: #f8d7da; padding: 15px; border: 1px solid #f5c6cb; border-radius: 4px;'>");
                    out.println("<h3>❌ No Active Categories!</h3>");
                    out.println("<p>All categories are inactive. The dropdown will be empty.</p>");
                    out.println("</div>");
                } else {
                    out.println("<p><strong>Active Categories (" + activeCategories.size() + "):</strong></p>");
                    out.println("<ul>");
                    for (BlogCategory cat : activeCategories) {
                        out.println("<li><strong>" + cat.getCategoryName() + "</strong> (slug: " + cat.getSlug() + ")</li>");
                    }
                    out.println("</ul>");
                }
            }
            
            out.println("<h2>Test Links</h2>");
            out.println("<p><a href='/DuAnBanCaCanh/blog'>→ Go to Blog Page</a></p>");
            out.println("<p><a href='/DuAnBanCaCanh/blog-debug'>→ Blog Debug Info</a></p>");
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace(out);
        } finally {
            out.close();
        }
    }
}
