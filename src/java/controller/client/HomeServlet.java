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
import java.math.BigDecimal;
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
          // Load Featured products (products with featured = 1)
        try {
            LOGGER.info("Loading Featured products for home page");
            List<Product> allProducts = productDAO.getActiveProducts();
            LOGGER.info("Found " + allProducts.size() + " active products");
            
            List<Product> homeProducts = new ArrayList<>();
            Map<Integer, String> productImages = new HashMap<>();
            Map<Integer, Category> productCategories = new HashMap<>();
            
            ProductImageDAO imageDAO = new ProductImageDAO();
            
            // Filter products that are featured and limit to 8
            int productCount = 0;
            int totalFeaturedFound = 0;
            for (Product product : allProducts) {
                if (product.getFeatured() != null && product.getFeatured() == 1) {
                    totalFeaturedFound++;
                    if (productCount < 8) {
                        homeProducts.add(product);
                        LOGGER.info("Added featured product: " + product.getName() + " (ID: " + product.getProductId() + ")");
                        LOGGER.info("Added featured product to display: " + product.getName() + " (ID: " + product.getProductId() + ")");
                        
                        // Get main image
                        try {
                            ProductImage mainImage = imageDAO.getMainImageByProductId(product.getProductId());
                            if (mainImage != null) {
                                productImages.put(product.getProductId(), mainImage.getImageUrl());
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading image for featured product " + product.getProductId(), e);
                        }
                        
                        // Get category name
                        if (product.getCategoryId() != null) {
                            try {
                                Category category = categoryDAO.getCategoryById(product.getCategoryId());
                                if (category != null) {
                                    productCategories.put(product.getProductId(), category);
                                }
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Error loading category for featured product " + product.getProductId(), e);
                            }
                        }
                        
                        productCount++;
                    }
                }
            }
            
            LOGGER.info("Found " + totalFeaturedFound + " total featured products, displaying " + productCount);
            
            request.setAttribute("homeProducts", homeProducts);
            request.setAttribute("productImages", productImages);
            request.setAttribute("productCategories", productCategories);
            
            LOGGER.info("Featured products loaded successfully, " + homeProducts.size() + " featured products set for display");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading Featured products for home page", e);
            request.setAttribute("homeProducts", new ArrayList<Product>());
            request.setAttribute("productImages", new HashMap<Integer, String>());
            request.setAttribute("productCategories", new HashMap<Integer, Category>());
        }
        
        // Load "On Sale" products (products with salePrice > 0)
        try {
            LOGGER.info("Loading On Sale products");
            List<Product> allProducts = productDAO.getActiveProducts();
            List<Product> onSaleProducts = new ArrayList<>();
            Map<Integer, String> onSaleProductImages = new HashMap<>();
            Map<Integer, Category> onSaleProductCategories = new HashMap<>();
            
            ProductImageDAO imageDAO = new ProductImageDAO();
            
            // Filter products that have sale price and limit to 10
            int saleProductCount = 0;
            for (Product product : allProducts) {
                if (product.getSalePrice() != null && 
                    product.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
                    
                    LOGGER.info("Found sale product: " + product.getName() + 
                               " - Price: " + product.getPrice() + 
                               " - Sale Price: " + product.getSalePrice());
                    
                    if (saleProductCount < 10) {
                        onSaleProducts.add(product);
                    
                    // Get main image
                    try {
                        ProductImage mainImage = imageDAO.getMainImageByProductId(product.getProductId());
                        if (mainImage != null) {
                            onSaleProductImages.put(product.getProductId(), mainImage.getImageUrl());
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading image for sale product " + product.getProductId(), e);
                    }
                    
                    // Get category
                    if (product.getCategoryId() != null) {
                        try {
                            Category category = categoryDAO.getCategoryById(product.getCategoryId());
                            if (category != null) {
                                onSaleProductCategories.put(product.getProductId(), category);
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading category for sale product " + product.getProductId(), e);
                        }
                    }
                    
                        saleProductCount++;
                    }
                }
            }
            
            request.setAttribute("onSaleProducts", onSaleProducts);
            request.setAttribute("onSaleProductImages", onSaleProductImages);
            request.setAttribute("onSaleProductCategories", onSaleProductCategories);
            
            LOGGER.info("On Sale products loaded successfully, " + onSaleProducts.size() + " products found");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading On Sale products", e);
            request.setAttribute("onSaleProducts", new ArrayList<Product>());
            request.setAttribute("onSaleProductImages", new HashMap<Integer, String>());
            request.setAttribute("onSaleProductCategories", new HashMap<Integer, Category>());
        }
        
        // Load "Top Rated" products (for now, use newest products as placeholder)
        try {
            LOGGER.info("Loading Top Rated products");
            List<Product> allProducts = productDAO.getActiveProducts();
            List<Product> topRatedProducts = new ArrayList<>();
            Map<Integer, String> topRatedProductImages = new HashMap<>();
            Map<Integer, Category> topRatedProductCategories = new HashMap<>();
            
            ProductImageDAO imageDAO = new ProductImageDAO();
            
            // For now, sort by product ID (newest first) as placeholder for rating
//            allProducts.sort((p1, p2) -> p2.getProductId().compareTo(p1.getProductId()));
            
            int topRatedCount = 0;
            for (Product product : allProducts) {
                if (topRatedCount < 3) {
                    topRatedProducts.add(product);
                    
                    // Get main image
                    try {
                        ProductImage mainImage = imageDAO.getMainImageByProductId(product.getProductId());
                        if (mainImage != null) {
                            topRatedProductImages.put(product.getProductId(), mainImage.getImageUrl());
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading image for top rated product " + product.getProductId(), e);
                    }
                    
                    // Get category
                    if (product.getCategoryId() != null) {
                        try {
                            Category category = categoryDAO.getCategoryById(product.getCategoryId());
                            if (category != null) {
                                topRatedProductCategories.put(product.getProductId(), category);
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading category for top rated product " + product.getProductId(), e);
                        }
                    }
                    
                    topRatedCount++;
                }
            }
            
            request.setAttribute("topRatedProducts", topRatedProducts);
            request.setAttribute("topRatedProductImages", topRatedProductImages);
            request.setAttribute("topRatedProductCategories", topRatedProductCategories);
            
            LOGGER.info("Top Rated products loaded successfully, " + topRatedProducts.size() + " products found");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading Top Rated products", e);
            request.setAttribute("topRatedProducts", new ArrayList<Product>());
            request.setAttribute("topRatedProductImages", new HashMap<Integer, String>());
            request.setAttribute("topRatedProductCategories", new HashMap<Integer, Category>());
        }
        
        // Load "Bestseller" products (products sorted by random for now - can implement sales-based logic later)
        try {
            LOGGER.info("Loading Bestseller products");
            List<Product> allProducts = productDAO.getActiveProducts();
            List<Product> bestsellerProducts = new ArrayList<>();
            Map<Integer, String> bestsellerProductImages = new HashMap<>();
            Map<Integer, Category> bestsellerProductCategories = new HashMap<>();
            
            ProductImageDAO imageDAO = new ProductImageDAO();
            
            // For now, use a different sorting criteria (e.g., by name) as placeholder for bestseller
            allProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            
            int bestsellerCount = 0;
            for (Product product : allProducts) {
                if (bestsellerCount < 6) {
                    bestsellerProducts.add(product);
                    
                    // Get main image
                    try {
                        ProductImage mainImage = imageDAO.getMainImageByProductId(product.getProductId());
                        if (mainImage != null) {
                            bestsellerProductImages.put(product.getProductId(), mainImage.getImageUrl());
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading image for bestseller product " + product.getProductId(), e);
                    }
                    
                    // Get category
                    if (product.getCategoryId() != null) {
                        try {
                            Category category = categoryDAO.getCategoryById(product.getCategoryId());
                            if (category != null) {
                                bestsellerProductCategories.put(product.getProductId(), category);
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error loading category for bestseller product " + product.getProductId(), e);
                        }
                    }
                    
                    bestsellerCount++;
                }
            }
            
            request.setAttribute("bestsellerProducts", bestsellerProducts);
            request.setAttribute("bestsellerProductImages", bestsellerProductImages);
            request.setAttribute("bestsellerProductCategories", bestsellerProductCategories);
            
            LOGGER.info("Bestseller products loaded successfully, " + bestsellerProducts.size() + " products found");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading Bestseller products", e);
            request.setAttribute("bestsellerProducts", new ArrayList<Product>());
            request.setAttribute("bestsellerProductImages", new HashMap<Integer, String>());
            request.setAttribute("bestsellerProductCategories", new HashMap<Integer, Category>());
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
        
        // Set context path for to use in relative paths
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
        // Forward to home JSP
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
