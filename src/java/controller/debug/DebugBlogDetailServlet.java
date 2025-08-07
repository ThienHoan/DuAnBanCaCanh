package controller.debug;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import dao.interfaces.BlogDAO;
import dao.impl.BlogDAOImpl;
import model.entity.BlogPost;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DebugBlogDetailServlet", urlPatterns = {"/debug-blog-detail"})
public class DebugBlogDetailServlet extends HttpServlet {
    
    private BlogService blogService;
    private BlogDAO blogDAO;

    @Override
    public void init() throws ServletException {
        blogService = new BlogServiceImpl();
        blogDAO = new BlogDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            out.println("<h1>Debug Blog Detail - Cần parameter id</h1>");
            out.println("<p>Usage: /debug-blog-detail?id=12</p>");
            return;
        }

        try {
            int postId = Integer.parseInt(idParam);
            
            out.println("<html><head><title>Debug Blog Detail</title></head><body>");
            out.println("<h1>🔍 Debug Blog Detail - Post ID: " + postId + "</h1>");
              // Test getPostById (without increment view)
            out.println("<h2>📖 Test getPostById (BlogDAO.getPostById)</h2>");
            BlogPost post1 = blogDAO.getPostById(postId);
            if (post1 != null) {
                out.println("<table border='1'>");
                out.println("<tr><th>Property</th><th>Value</th></tr>");
                out.println("<tr><td>Post ID</td><td>" + post1.getPostId() + "</td></tr>");
                out.println("<tr><td>Title</td><td>" + (post1.getTitle() != null ? post1.getTitle() : "NULL") + "</td></tr>");
                out.println("<tr><td>Category ID</td><td>" + post1.getCategoryId() + "</td></tr>");
                out.println("<tr><td>Category Object</td><td>" + (post1.getCategoryObject() != null ? "NOT NULL" : "NULL") + "</td></tr>");
                if (post1.getCategoryObject() != null) {
                    out.println("<tr><td>Category Name</td><td>" + post1.getCategoryObject().getCategoryName() + "</td></tr>");
                    out.println("<tr><td>Category Slug</td><td>" + post1.getCategoryObject().getSlug() + "</td></tr>");
                    out.println("<tr><td>Category Active</td><td>" + post1.getCategoryObject().isActive() + "</td></tr>");
                } else {
                    out.println("<tr><td>Category Name</td><td>NULL (categoryObject is null)</td></tr>");
                }
                out.println("<tr><td>Status</td><td>" + post1.getStatus() + "</td></tr>");
                out.println("<tr><td>View Count</td><td>" + post1.getViewCount() + "</td></tr>");
                out.println("</table>");
            } else {
                out.println("<p>❌ Post not found with ID: " + postId + "</p>");
            }
            
            // Test getPostByIdAndIncrementView (service method)
            out.println("<h2>📈 Test getPostByIdAndIncrementView (BlogService)</h2>");
            BlogPost post2 = blogService.getPostByIdAndIncrementView(postId);
            if (post2 != null) {
                out.println("<table border='1'>");
                out.println("<tr><th>Property</th><th>Value</th></tr>");
                out.println("<tr><td>Post ID</td><td>" + post2.getPostId() + "</td></tr>");
                out.println("<tr><td>Title</td><td>" + (post2.getTitle() != null ? post2.getTitle() : "NULL") + "</td></tr>");
                out.println("<tr><td>Category ID</td><td>" + post2.getCategoryId() + "</td></tr>");
                out.println("<tr><td>Category Object</td><td>" + (post2.getCategoryObject() != null ? "NOT NULL" : "NULL") + "</td></tr>");
                if (post2.getCategoryObject() != null) {
                    out.println("<tr><td>Category Name</td><td>" + post2.getCategoryObject().getCategoryName() + "</td></tr>");
                    out.println("<tr><td>Category Slug</td><td>" + post2.getCategoryObject().getSlug() + "</td></tr>");
                    out.println("<tr><td>Category Active</td><td>" + post2.getCategoryObject().isActive() + "</td></tr>");
                } else {
                    out.println("<tr><td>Category Name</td><td>NULL (categoryObject is null)</td></tr>");
                }
                out.println("<tr><td>Status</td><td>" + post2.getStatus() + "</td></tr>");
                out.println("<tr><td>View Count</td><td>" + post2.getViewCount() + "</td></tr>");
                out.println("</table>");
            } else {
                out.println("<p>❌ Post not found with getPostByIdAndIncrementView, ID: " + postId + "</p>");
            }
            
            out.println("<br><hr>");
            out.println("<h2>🔗 Quick Links</h2>");
            out.println("<a href='/DuAnBanCaCanh/blog-detail?id=" + postId + "'>➡️ Go to actual blog-detail page</a><br>");
            out.println("<a href='/DuAnBanCaCanh/debug-posts'>📝 Debug all posts</a><br>");
            out.println("<a href='/DuAnBanCaCanh/debug-categories'>📂 Debug categories</a>");
            
            out.println("</body></html>");
            
        } catch (NumberFormatException e) {
            out.println("<h1>❌ Invalid post ID: " + idParam + "</h1>");
        } catch (Exception e) {
            out.println("<h1>❌ Error: " + e.getMessage() + "</h1>");
            e.printStackTrace(out);
        }
    }
}
