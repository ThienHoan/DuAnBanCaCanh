package controller.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.entity.BlogCategory;
import dao.impl.BlogDAOImpl;
import dao.interfaces.BlogDAO;

/**
 * Servlet kiểm tra danh mục blog
 */
public class TestBlogCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            BlogDAO blogDAO = new BlogDAOImpl();
            List<BlogCategory> categories = blogDAO.getAllCategories();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Test Blog Categories</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Test Blog Categories</h1>");
            
            out.println("<h2>Categories List (" + categories.size() + " items)</h2>");
            
            if (categories.isEmpty()) {
                out.println("<p>No categories found</p>");
            } else {
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>ID</th>");
                out.println("<th>Name</th>");
                out.println("<th>Description</th>");
                out.println("<th>Slug</th>");
                out.println("<th>Created</th>");
                out.println("<th>isActive()</th>");
                out.println("<th>isDeleted()</th>");
                out.println("</tr>");
                
                for (BlogCategory category : categories) {
                    out.println("<tr>");
                    out.println("<td>" + category.getCategoryId() + "</td>");
                    out.println("<td>" + category.getCategoryName() + "</td>");
                    out.println("<td>" + (category.getDescription() != null ? category.getDescription() : "") + "</td>");
                    out.println("<td>" + (category.getSlug() != null ? category.getSlug() : "") + "</td>");
                    out.println("<td>" + (category.getCreatedAt() != null ? category.getCreatedAt() : "") + "</td>");
                    out.println("<td>" + category.isActive() + "</td>");
                    out.println("<td>" + category.isDeleted() + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
            }
            
            out.println("<h2>Actions</h2>");
            
            out.println("<form method='post'>");
            out.println("<h3>Create New Category</h3>");
            out.println("<div style='margin-bottom: 10px;'>");
            out.println("  <label for='name'>Name:</label>");
            out.println("  <input type='text' id='name' name='name' required>");
            out.println("</div>");
            out.println("<div style='margin-bottom: 10px;'>");
            out.println("  <label for='description'>Description:</label>");
            out.println("  <textarea id='description' name='description'></textarea>");
            out.println("</div>");
            out.println("<div style='margin-bottom: 10px;'>");
            out.println("  <label for='slug'>Slug:</label>");
            out.println("  <input type='text' id='slug' name='slug'>");
            out.println("</div>");
            out.println("<div style='margin-bottom: 10px;'>");
            out.println("  <label for='isActive'>Is Active:</label>");
            out.println("  <input type='checkbox' id='isActive' name='isActive' value='true' checked>");
            out.println("</div>");
            out.println("<input type='hidden' name='action' value='create'>");
            out.println("<button type='submit'>Create Category</button>");
            out.println("</form>");
            
            out.println("<hr>");
            
            out.println("<h2>Database Operations</h2>");
            out.println("<ul>");
            out.println("<li><a href='" + request.getContextPath() + "/admin-categories'>Go to Admin Categories Page</a></li>");
            out.println("</ul>");
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String slug = request.getParameter("slug");
            boolean isActive = "true".equals(request.getParameter("isActive"));
            
            try {
                BlogDAO blogDAO = new BlogDAOImpl();
                BlogCategory category = new BlogCategory(name, description, slug);
                category.setActive(isActive);
                
                boolean success = blogDAO.createCategory(category);
                
                response.sendRedirect(request.getContextPath() + "/test-blog-category?result=" + (success ? "success" : "fail"));
                
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/test-blog-category?result=error&message=" + e.getMessage());
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/test-blog-category?result=unknownAction");
        }
    }
}
