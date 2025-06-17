package utils;

import jakarta.servlet.http.HttpSession;
import model.entity.User;

/**
 * Utility class để quản lý session và authentication
 */
public class SessionUtils {
    
    public static final String USER_SESSION_KEY = "user";
    public static final String ADMIN_ROLE = "admin";
    public static final String CUSTOMER_ROLE = "customer";
    
    /**
     * Lưu user vào session
     */
    public static void setUser(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_KEY, user);
    }
    
    /**
     * Lấy user từ session
     */
    public static User getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(USER_SESSION_KEY);
    }
    
    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }
    
    /**
     * Kiểm tra user có phải admin không
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getUser(session);
        return user != null && ADMIN_ROLE.equals(user.getRole());
    }
    
    /**
     * Kiểm tra user có phải customer không
     */
    public static boolean isCustomer(HttpSession session) {
        User user = getUser(session);
        return user != null && CUSTOMER_ROLE.equals(user.getRole());
    }
    
    /**
     * Xóa user khỏi session (logout)
     */
    public static void logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
            session.invalidate();
        }
    }
    
    /**
     * Lấy user ID từ session
     */
    public static Integer getUserId(HttpSession session) {
        User user = getUser(session);
        return user != null ? user.getUserId() : null;
    }
    
    /**
     * Lấy username từ session
     */
    public static String getUsername(HttpSession session) {
        User user = getUser(session);
        return user != null ? user.getUsername() : null;
    }
    
    /**
     * Lấy full name từ session
     */
    public static String getFullName(HttpSession session) {
        User user = getUser(session);
        return user != null ? user.getFullName() : null;
    }
}
