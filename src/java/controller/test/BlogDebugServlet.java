package controller.test;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import model.entity.BlogPost;
import model.entity.BlogCategory;
import utils.ConfigUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Debug servlet để kiểm tra blog pagination
 */
@WebServlet("/blog-debug")
public class BlogDebugServlet extends HttpServlet {
    
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
            // Lấy thông tin cấu hình
            int pageSize = ConfigUtil.getBlogPostsPerPage();
            int totalPosts = blogService.getTotalPostsCount();
            int totalPages = blogService.getTotalPages(pageSize);
            
            // Lấy bài viết trang 1
            List<BlogPost> postsPage1 = blogService.getPostsWithPagination(1, pageSize);
            
            // Lấy bài viết trang 2 (nếu có)
            List<BlogPost> postsPage2 = blogService.getPostsWithPagination(2, pageSize);
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Blog Debug Info</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("table { border-collapse: collapse; width: 100%; margin: 10px 0; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println(".section { margin: 20px 0; padding: 15px; border: 1px solid #ccc; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Blog Debug Information</h1>");
            
            // Thông tin cấu hình
            out.println("<div class='section'>");
            out.println("<h2>Configuration</h2>");
            out.println("<table>");
            out.println("<tr><th>Config Key</th><th>Value</th></tr>");
            out.println("<tr><td>blog.posts.per.page</td><td>" + pageSize + "</td></tr>");
            out.println("<tr><td>Total Posts</td><td>" + totalPosts + "</td></tr>");
            out.println("<tr><td>Total Pages</td><td>" + totalPages + "</td></tr>");
            out.println("<tr><td>Should Show Pagination?</td><td>" + (totalPages > 1 ? "YES" : "NO") + "</td></tr>");
            out.println("</table>");
            out.println("</div>");
            
            // Posts Page 1
            out.println("<div class='section'>");
            out.println("<h2>Posts - Page 1 (Limit: " + pageSize + ")</h2>");
            if (postsPage1.isEmpty()) {
                out.println("<p>No posts found on page 1</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Title</th><th>Status</th><th>Created At</th></tr>");
                for (BlogPost post : postsPage1) {
                    out.println("<tr>");
                    out.println("<td>" + post.getPostId() + "</td>");
                    out.println("<td>" + (post.getTitle() != null ? post.getTitle() : "N/A") + "</td>");
                    out.println("<td>" + post.getStatus() + "</td>");
                    out.println("<td>" + post.getCreatedAt() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("<p>Found " + postsPage1.size() + " posts on page 1</p>");
            }
            out.println("</div>");
            
            // Posts Page 2
            out.println("<div class='section'>");
            out.println("<h2>Posts - Page 2 (Limit: " + pageSize + ")</h2>");
            if (postsPage2.isEmpty()) {
                out.println("<p>No posts found on page 2</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Title</th><th>Status</th><th>Created At</th></tr>");
                for (BlogPost post : postsPage2) {
                    out.println("<tr>");
                    out.println("<td>" + post.getPostId() + "</td>");
                    out.println("<td>" + (post.getTitle() != null ? post.getTitle() : "N/A") + "</td>");
                    out.println("<td>" + post.getStatus() + "</td>");
                    out.println("<td>" + post.getCreatedAt() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("<p>Found " + postsPage2.size() + " posts on page 2</p>");
            }
            out.println("</div>");
            
            // Categories
            List<BlogCategory> categories = blogService.getAllCategories();
            out.println("<div class='section'>");
            out.println("<h2>Categories</h2>");
            if (categories.isEmpty()) {
                out.println("<p>No categories found</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Name</th><th>Slug</th><th>Active</th></tr>");
                for (BlogCategory cat : categories) {
                    out.println("<tr>");
                    out.println("<td>" + cat.getCategoryId() + "</td>");
                    out.println("<td>" + (cat.getCategoryName() != null ? cat.getCategoryName() : "N/A") + "</td>");
                    out.println("<td>" + (cat.getSlug() != null ? cat.getSlug() : "N/A") + "</td>");
                    out.println("<td>" + cat.isActive() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            out.println("</div>");
            
            out.println("<div class='section'>");
            out.println("<h2>Test Links</h2>");
            out.println("<p><a href='/DuAnBanCaCanh/blog'>View Blog Page</a></p>");
            out.println("<p><a href='/DuAnBanCaCanh/blog?page=1'>Blog Page 1</a></p>");
            out.println("<p><a href='/DuAnBanCaCanh/blog?page=2'>Blog Page 2</a></p>");
            out.println("</div>");
            
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
