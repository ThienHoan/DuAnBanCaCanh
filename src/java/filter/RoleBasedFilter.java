package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;

import java.io.IOException;

/**
 * Filter để kiểm tra quyền truy cập dựa trên role
 */
public class RoleBasedFilter implements Filter {

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
        
        // Chỉ kiểm tra các trang trong /admin/
        if (!path.startsWith("/admin/")) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpSession session = httpRequest.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        // Kiểm tra user tồn tại và có quyền admin
        if (user == null || !"admin".equals(user.getRole())) {
            // Không có quyền admin
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("Access Denied: Admin role required");
            return;
        }
        
        // Cho phép truy cập
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup
    }
}
