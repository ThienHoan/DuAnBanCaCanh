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
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }
      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        LOGGER.info("HomeServlet doGet started");
        
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
            
            LOGGER.info("Blog data loaded successfully");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading blog data for header in HomeServlet", e);
            // Set empty lists to avoid null pointer
            request.setAttribute("headerCategories", new ArrayList<BlogCategory>());
            request.setAttribute("headerLatestPosts", new ArrayList<BlogPost>());
        }
          // Load products for home page
        try {
            LOGGER.info("Loading products for home page");
            List<Product> allProducts = productDAO.getActiveProducts();
            LOGGER.info("Found " + allProducts.size() + " active products");
            
            List<Product> homeProducts = new ArrayList<>();
            Map<Integer, String> productImages = new HashMap<>();
            Map<Integer, String> productCategories = new HashMap<>();
            
            ProductImageDAO imageDAO = new ProductImageDAO();
            
            // Limit to 8 products for display
            int productCount = 0;
            for (Product product : allProducts) {
                if (productCount < 8) {
                    homeProducts.add(product);
                    
                    // Get main image
                    try {
                        ProductImage mainImage = imageDAO.getMainImageByProductId(product.getProductId());
                        if (mainImage != null) {
                            productImages.put(product.getProductId(), mainImage.getImageUrl());
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading image for product " + product.getProductId(), e);
                    }
                    
                    // Get category name
                    if (product.getCategoryId() != null) {
                        try {
                            Category category = categoryDAO.getCategoryById(product.getCategoryId());
                            if (category != null) {
                                productCategories.put(product.getProductId(), category.getName());
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading category for product " + product.getProductId(), e);
                        }
                    }
                    
                    productCount++;
                }
            }
            
            request.setAttribute("homeProducts", homeProducts);
            request.setAttribute("productImages", productImages);
            request.setAttribute("productCategoryNames", productCategories);
            
            LOGGER.info("Products loaded successfully, " + homeProducts.size() + " products set for display");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading products for home page", e);
            request.setAttribute("homeProducts", new ArrayList<Product>());
            request.setAttribute("productImages", new HashMap<Integer, String>());
            request.setAttribute("productCategoryNames", new HashMap<Integer, String>());
        }
          // Load categories for product filtering and count products
        try {
            LOGGER.info("Loading product categories");
            List<Category> allProductCategories = categoryDAO.getAllCategories();
            LOGGER.info("Found " + allProductCategories.size() + " total categories");
            
            List<Category> activeCategories = new ArrayList<>();
            Map<Integer, Integer> categoryProductCounts = new HashMap<>();
            
            for (Category cat : allProductCategories) {
                if (cat.isActive()) {
                    activeCategories.add(cat);
                    
                    // Count products in this category
                    try {
                        List<Product> productsInCategory = productDAO.getProductsByCategory(cat.getCategoryId());
                        int activeProductCount = 0;
                        for (Product p : productsInCategory) {
                            if (p.isActive()) {
                                activeProductCount++;
                            }
                        }
                        categoryProductCounts.put(cat.getCategoryId(), activeProductCount);
                        LOGGER.info("Category " + cat.getName() + " has " + activeProductCount + " active products");
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error counting products for category " + cat.getName(), e);
                        categoryProductCounts.put(cat.getCategoryId(), 0);
                    }
                }
            }
            
            request.setAttribute("allProductCategories", activeCategories);
            request.setAttribute("categoryProductCounts", categoryProductCounts);
            
            LOGGER.info("Categories loaded successfully, " + activeCategories.size() + " active categories");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading product categories", e);
            request.setAttribute("allProductCategories", new ArrayList<Category>());
            request.setAttribute("categoryProductCounts", new HashMap<Integer, Integer>());
        }        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Set context path for JSP to use in relative paths
        request.setAttribute("contextPath", request.getContextPath());
        
        LOGGER.info("User in session: " + (user != null ? user.getUsername() : "null"));
        LOGGER.info("Context path set: " + request.getContextPath());
        
        // If not logged in, still allow viewing home page normally
        if (user == null) {
            LOGGER.info("No user logged in, forwarding to home.jsp");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }
        
        // If logged in, check role
        if ("admin".equals(user.getRole())) {
            // Admin can see dashboard link in header
            request.setAttribute("showDashboard", true);
        }
        
        LOGGER.info("User logged in as " + user.getRole() + ", forwarding to home.jsp");
        // Forward to home.jsp
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
