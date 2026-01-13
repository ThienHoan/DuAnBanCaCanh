package controller.client;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(urlPatterns = {"/blog", "/blog-detail", "/blog-category", "/blog-search"})
public class BlogController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BlogController.class.getName());
    private BlogService blogService;

    @Override
    public void init() throws ServletException {
        super.init();
        blogService = new BlogServiceImpl();
        LOGGER.info("BlogController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        LOGGER.info("BlogController handling request: " + path);

        try {
            switch (path) {
                case "/blog":
                    handleBlogList(request, response);
                    break;
                case "/blog-detail":
                    handleBlogDetail(request, response);
                    break;
                case "/blog-category":
                    handleBlogCategory(request, response);
                    break;
                case "/blog-search":
                    handleBlogSearch(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in BlogController", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST requests redirect to GET
        doGet(request, response);
    }

    /**
     * Handle blog list page (/blog)
     */
    private void handleBlogList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          try {
            // Get pagination parameters from config
            int page = 1;
            int pageSize = ConfigUtil.getBlogPostsPerPage(); // Get from config.properties
            
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // Get posts with pagination
            List<BlogPost> posts = blogService.getPostsWithPagination(page, pageSize);
            int totalPages = blogService.getTotalPages(pageSize);
            int totalPosts = blogService.getTotalPostsCount();
              // Get categories for filter
            List<BlogCategory> categories = blogService.getAllCategories();
            
            // Get latest posts for sidebar
            List<BlogPost> latestPosts = blogService.getLatestPosts(ConfigUtil.getFeaturedPostsCount());            // Set attributes
            request.setAttribute("posts", posts);
            request.setAttribute("categories", categories);
            
            // Set cho header
            request.setAttribute("headerCategories", categories.stream()
                .filter(cat -> cat.isActive())
                .limit(6)
                .toList());
            request.setAttribute("headerLatestPosts", latestPosts);
            
            // Debug info
            LOGGER.info("BlogController - Categories count: " + categories.size());
            for (BlogCategory cat : categories) {
                LOGGER.info("Category: " + cat.getCategoryName() + " (slug: " + cat.getSlug() + ", active: " + cat.isActive() + ")");
            }
            request.setAttribute("latestPosts", latestPosts);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalPosts", totalPosts);
            request.setAttribute("pageSize", pageSize);
            
            LOGGER.info("Blog list loaded: " + posts.size() + " posts, page " + page + "/" + totalPages + 
                       " (total: " + totalPosts + " posts, " + pageSize + " per page)");
            
            // Forward to JSP
            request.getRequestDispatcher("/blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling blog list", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle blog detail page (/blog-detail?id=xxx)
     */
    private void handleBlogDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/blog");
                return;
            }

            int postId;
            try {
                postId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/blog");
                return;
            }

            // Get post and increment view count
            BlogPost post = blogService.getPostByIdAndIncrementView(postId);
            if (post == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }            // Get related posts (same category)
            List<BlogPost> relatedPosts = blogService.getPostsByCategoryId(post.getCategoryId());
            // Remove current post from related posts
            relatedPosts.removeIf(p -> p.getPostId() == postId);
            // Limit to 5 related posts
            if (relatedPosts.size() > 5) {
                relatedPosts = relatedPosts.subList(0, 5);
            }

            // Get categories for navigation
            List<BlogCategory> categories = blogService.getAllCategories();

            // Set attributes
            request.setAttribute("post", post);
            request.setAttribute("relatedPosts", relatedPosts);
            request.setAttribute("categories", categories);

            LOGGER.info("Blog detail loaded: " + post.getTitle() + " (ID: " + postId + ")");

            // Forward to blog detail JSP (we'll create this)
            request.getRequestDispatcher("/blog-detail.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling blog detail", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }    /**
     * Handle blog category page (/blog-category?category=xxx or /blog-category?categoryId=123)
     */
    private void handleBlogCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String categoryParam = request.getParameter("category");
            String categoryIdParam = request.getParameter("categoryId");
            
            if ((categoryParam == null || categoryParam.isEmpty()) && 
                (categoryIdParam == null || categoryIdParam.isEmpty())) {
                response.sendRedirect(request.getContextPath() + "/blog");
                return;
            }

            List<BlogPost> posts;
            String selectedCategoryName = "";
            
            // Try to get posts by category ID first, then by category name
            if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
                try {
                    int categoryId = Integer.parseInt(categoryIdParam);
                    posts = blogService.getPostsByCategoryId(categoryId);
                    
                    // Get category name for display
                    List<BlogCategory> allCategories = blogService.getAllCategories();
                    for (BlogCategory cat : allCategories) {
                        if (cat.getCategoryId() == categoryId) {
                            selectedCategoryName = cat.getCategoryName();
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/blog");
                    return;
                }
            } else {
                // Get posts by category name (backward compatibility)
                posts = blogService.getPostsByCategory(categoryParam);
                selectedCategoryName = categoryParam;
            }
            
            // Get all categories
            List<BlogCategory> categories = blogService.getAllCategories();
            
            // Get latest posts for sidebar
            List<BlogPost> latestPosts = blogService.getLatestPosts(5);

            // Set attributes
            request.setAttribute("posts", posts);
            request.setAttribute("categories", categories);
            request.setAttribute("latestPosts", latestPosts);
            request.setAttribute("selectedCategory", selectedCategoryName);
            request.setAttribute("pageTitle", "Danh mục: " + selectedCategoryName);

            LOGGER.info("Blog category loaded: " + selectedCategoryName + " (" + posts.size() + " posts)");
            
            // Forward to blog JSP
            request.getRequestDispatcher("/blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling blog category", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle blog search page (/blog-search?q=xxx)
     */
    private void handleBlogSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String query = request.getParameter("q");
            if (query == null || query.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/blog");
                return;
            }

            query = query.trim();

            // Search posts
            List<BlogPost> posts = blogService.searchPosts(query);
            
            // Get categories for filter
            List<BlogCategory> categories = blogService.getAllCategories();
            
            // Get latest posts for sidebar
            List<BlogPost> latestPosts = blogService.getLatestPosts(5);

            // Set attributes
            request.setAttribute("posts", posts);
            request.setAttribute("categories", categories);
            request.setAttribute("latestPosts", latestPosts);
            request.setAttribute("searchQuery", query);
            request.setAttribute("pageTitle", "Tìm kiếm: " + query);

            LOGGER.info("Blog search performed: \"" + query + "\" (" + posts.size() + " results)");
            
            // Forward to blog JSP
            request.getRequestDispatcher("/blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling blog search", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
