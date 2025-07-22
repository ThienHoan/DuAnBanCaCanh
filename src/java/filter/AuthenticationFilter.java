package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter để xác thực người dùng trước khi truy cập các trang được bảo vệ
 */
public class AuthenticationFilter implements Filter {
    // Các trang không cần đăng nhập
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/login",
        "/register",
        "/home",
        "/auth/google",
        "/googlecallback",
        "/forgot-password",
        "/reset-password",
        "/assets/",
        "/product_detail",
        "/contact_us",
        "/blog",
        "/ai-agent.jsp",
        "/ai-agent-api.jsp",
        "/CategoryServlet",
        "/category",
        "/sepay-webhook"  // Thêm webhook URL vào danh sách công khai
    );
    
    // Các trang giỏ hàng và thanh toán - cần đăng nhập nhưng không cần quyền admin
    private static final List<String> CART_PATHS = Arrays.asList(
        "/cartClient",
        "/cart",
        "/checkout",
        "/vnpay-return",
        "/apply-coupon",
        "/order"
    );

    // Các trang chỉ dành cho admin
    private static final List<String> ADMIN_PATHS = Arrays.asList(
        "/admin-dashboard",
        "/admin-dashboard.jsp",
        "/admin/"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Log request để debug
        System.out.println("=== AuthenticationFilter ===");
        System.out.println("Full URI: " + requestURI);
        System.out.println("Context Path: " + contextPath);
        System.out.println("Processed Path: " + path);
        
        // Đặc biệt log cho admin-dashboard
        if (path.equals("/admin-dashboard")) {
            System.out.println("🔍 ADMIN-DASHBOARD DETECTED!");
        }
        
        // Kiểm tra nếu là trang công khai
        if (isPublicPath(path)) {
            System.out.println("Public path detected, allowing access: " + path);
            chain.doFilter(request, response);
            return;
        }

        // Lấy session và user
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        System.out.println("Session exists: " + (session != null));
        System.out.println("User logged in: " + (user != null));
        if (user != null) {
            System.out.println("User role: " + user.getRole());
        }
        
        // Kiểm tra đăng nhập
        if (user == null) {
            // Chưa đăng nhập - lưu URL hiện tại để redirect sau khi đăng nhập nếu là trang giỏ hàng/thanh toán
            if (isCartPath(path)) {
                System.out.println("💾 Saving cart/checkout URL for redirect after login: " + requestURI);
                session = httpRequest.getSession(true);
                session.setAttribute("redirectURL", requestURI);
            }
            
            // Chuyển về trang login
            System.out.println("❌ User not logged in, redirecting to login");
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        // Đảm bảo thông tin người dùng được lưu trong session với các key chuẩn
        if (session != null && user != null) {
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getFullName());
            System.out.println("✅ User info stored in session: userId=" + user.getUserId() + ", email=" + user.getEmail());
        }

        // Kiểm tra quyền admin
        if (isAdminPath(path) && !"admin".equals(user.getRole())) {
            // Không phải admin nhưng truy cập trang admin
            System.out.println("❌ Non-admin user trying to access admin page");
            httpResponse.sendRedirect(contextPath + "/home");
            return;
        }
        
        // Cho phép truy cập
        System.out.println("✅ Access granted for path: " + path);
        chain.doFilter(request, response);
    }
    
    /**
     * Kiểm tra xem path có phải là trang công khai không
     */
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Kiểm tra xem path có phải là trang giỏ hàng/thanh toán không
     */
    private boolean isCartPath(String path) {
        for (String cartPath : CART_PATHS) {
            if (path.startsWith(cartPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem path có phải là trang admin không
     */
    private boolean isAdminPath(String path) {
        System.out.println("🔍 Checking if admin path: " + path);
        for (String adminPath : ADMIN_PATHS) {
            System.out.println("  - Comparing with: " + adminPath);
            if (path.startsWith(adminPath)) {
                System.out.println("  ✅ MATCH! This is an admin path");
                return true;
            }
        }
        System.out.println("  ❌ Not an admin path");
        return false;
    }

    @Override
    public void destroy() {
        // Cleanup khi filter bị destroy
    }
}