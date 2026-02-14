package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/**
 * Bộ lọc này đảm bảo mọi request và response đều sử dụng mã hóa UTF-8.
 * Nó chạy trước tất cả các servlet để khắc phục lỗi ký tự tiếng Việt.
 */
@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = {"/*"})
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Chuyển yêu cầu đã được mã hóa đúng đi tiếp trong chuỗi xử lý
        chain.doFilter(request, response);
    }

    // Các phương thức init và destroy có thể để trống
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần làm gì
    }

    @Override
    public void destroy() {
        // Không cần làm gì
    }
}
