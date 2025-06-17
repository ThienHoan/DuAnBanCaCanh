package controller.client;

import model.entity.User;
import model.entity.BlogCategory;
import model.entity.BlogPost;
import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Load blog data for header
            BlogService blogService = new BlogServiceImpl();
            
            // Load categories for header menu
            List<BlogCategory> allCategories = blogService.getAllCategories();
            List<BlogCategory> headerCategories = new ArrayList<>();
            int count = 0;
            for (BlogCategory cat : allCategories) {
                if (cat.isActive() && !cat.isDeleted() && count < 6) {
                    headerCategories.add(cat);
                    count++;
                }
            }
            request.setAttribute("headerCategories", headerCategories);
            
            // Load latest posts for header menu
            List<BlogPost> headerLatestPosts = blogService.getLatestPosts(3);
            request.setAttribute("headerLatestPosts", headerLatestPosts);
            
            LOGGER.info("HomeServlet: Loaded " + headerCategories.size() + " categories and " + headerLatestPosts.size() + " latest posts for header");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading blog data for header in HomeServlet", e);
            // Set empty lists to avoid null pointer
            request.setAttribute("headerCategories", new ArrayList<BlogCategory>());
            request.setAttribute("headerLatestPosts", new ArrayList<BlogPost>());
        }
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
          // Nếu chưa đăng nhập, vẫn cho xem trang home bình thường
        if (user == null) {
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }
        
        // Nếu đã đăng nhập, kiểm tra role
        if ("admin".equals(user.getRole())) {
            // Admin có thể thấy link dashboard trong header
            request.setAttribute("showDashboard", true);
        }
        
        // Forward đến trang home.jsp
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}