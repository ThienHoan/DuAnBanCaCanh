package controller.client;

import model.entity.User;
import model.entity.BlogCategory;
import model.entity.BlogPost;
import model.entity.Product;
import model.entity.Category;
import model.entity.ProductImage;
import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import dao.impl.ProductDAO;
import dao.impl.CategoryDAO;
import dao.impl.ProductImageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }
    
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
        }        // Load products for home page
        try {
            List<Product> allProducts = productDAO.getActiveProducts();
            List<Product> homeProducts = new ArrayList<>();
            Map<Integer, String> productImages = new HashMap<>();
            Map<Integer, String> productCategories = new HashMap<>();
            
            ProductImageDAO imageDAO = new ProductImageDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            
            // Limit to 8 products for display
            int productCount = 0;
            for (Product product : allProducts) {
                if (productCount < 8) {
                    homeProducts.add(product);
                    
                    // Get main image
                    ProductImage mainImage = imageDAO.getMainImageByProductId(product.getProductId());
                    if (mainImage != null) {
                        productImages.put(product.getProductId(), mainImage.getImageUrl());
                    }
                    
                    // Get category name
                    if (product.getCategoryId() != null) {
                        Category category = categoryDAO.getCategoryById(product.getCategoryId());
                        if (category != null) {
                            productCategories.put(product.getProductId(), category.getName());
                        }
                    }
                    
                    productCount++;
                }
            }
            
            request.setAttribute("homeProducts", homeProducts);
            request.setAttribute("productImages", productImages);
            request.setAttribute("productCategories", productCategories);
            LOGGER.info("HomeServlet: Loaded " + homeProducts.size() + " active products for home page");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading products for home page", e);
            request.setAttribute("homeProducts", new ArrayList<Product>());
            request.setAttribute("productImages", new HashMap<Integer, String>());
            request.setAttribute("productCategories", new HashMap<Integer, String>());
        }
        
        // Load categories for product filtering (if needed)
        try {
            List<Category> allProductCategories = categoryDAO.getAllCategories();
            List<Category> activeCategories = new ArrayList<>();
            
            for (Category cat : allProductCategories) {
                if (cat.isActive()) {
                    activeCategories.add(cat);
                }
            }
            
            request.setAttribute("productCategories", activeCategories);
            LOGGER.info("HomeServlet: Loaded " + activeCategories.size() + " product categories");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading product categories", e);
            request.setAttribute("productCategories", new ArrayList<Category>());
        }
          HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // If not logged in, still allow viewing home page normally
        if (user == null) {
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }
        
        // If logged in, check role
        if ("admin".equals(user.getRole())) {
            // Admin can see dashboard link in header
            request.setAttribute("showDashboard", true);        }
        
        // Forward to home.jsp
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}