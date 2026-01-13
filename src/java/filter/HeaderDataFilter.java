package filter;

import service.interfaces.BlogService;
import service.impl.BlogServiceImpl;
import model.entity.BlogCategory;
import model.entity.BlogPost;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filter để thêm blog categories vào header cho tất cả các trang
 */
// @WebFilter("/*") // Commented out - using web.xml mapping instead
public class HeaderDataFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(HeaderDataFilter.class.getName());
    private BlogService blogService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        blogService = new BlogServiceImpl();
        LOGGER.info("HeaderDataFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        try {
            // Chỉ thêm data cho các request HTML (không phải CSS, JS, images, API...)
            String requestURI = httpRequest.getRequestURI();
            String contentType = httpRequest.getContentType();
            
            // Skip static resources và API endpoints
            if (!isStaticResource(requestURI) && !isApiEndpoint(requestURI)) {                // Lấy categories cho header
                List<BlogCategory> headerCategories = blogService.getAllCategories();
                
                // Chỉ lấy các categories active để hiển thị trong menu
                headerCategories = headerCategories.stream()
                    .filter(cat -> cat.isActive())
                    .limit(10) // Giới hạn 10 categories trong header menu
                    .toList();
                
                httpRequest.setAttribute("headerCategories", headerCategories);
                
                // Lấy latest posts cho header
                List<BlogPost> headerLatestPosts = blogService.getLatestPosts(3);
                httpRequest.setAttribute("headerLatestPosts", headerLatestPosts);
                
                LOGGER.fine("Added " + headerCategories.size() + " categories and " + 
                           headerLatestPosts.size() + " latest posts to header for " + requestURI);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error in HeaderDataFilter", e);
            // Không throw exception để không ảnh hưởng đến request chính
        }
        
        // Continue với filter chain
        chain.doFilter(request, response);
    }
    
    /**
     * Check if request is for static resources
     */
    private boolean isStaticResource(String requestURI) {
        return requestURI != null && (
            requestURI.endsWith(".css") ||
            requestURI.endsWith(".js") ||
            requestURI.endsWith(".png") ||
            requestURI.endsWith(".jpg") ||
            requestURI.endsWith(".jpeg") ||
            requestURI.endsWith(".gif") ||
            requestURI.endsWith(".ico") ||
            requestURI.endsWith(".svg") ||
            requestURI.endsWith(".woff") ||
            requestURI.endsWith(".woff2") ||
            requestURI.endsWith(".ttf") ||
            requestURI.endsWith(".eot") ||
            requestURI.contains("/assets/") ||
            requestURI.contains("/static/")
        );
    }
    
    /**
     * Check if request is API endpoint
     */
    private boolean isApiEndpoint(String requestURI) {
        return requestURI != null && (
            requestURI.startsWith("/api/") ||
            requestURI.contains("-debug") ||
            requestURI.contains("/ajax/")
        );
    }

    @Override
    public void destroy() {
        LOGGER.info("HeaderDataFilter destroyed");
    }
}
