package utils;

import jakarta.servlet.http.HttpSession;
import model.entity.User;

/**
 * Utility class for session management
 */
public class SessionUtils {
    
    private static final String USER_SESSION_KEY = "user";
    
    /**
     * Get the current logged-in user from session
     * 
     * @param session The HttpSession object
     * @return The User object or null if not logged in
     */
    public static User getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(USER_SESSION_KEY);
    }
    
    /**
     * Set the user in session (login)
     * 
     * @param session The HttpSession object
     * @param user The User object to set
     */
    public static void setUser(HttpSession session, User user) {
        if (session != null) {
            session.setAttribute(USER_SESSION_KEY, user);
        }
    }
    
    /**
     * Remove the user from session (logout)
     * 
     * @param session The HttpSession object
     */
    public static void removeUser(HttpSession session) {
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
        }
    }
    
    /**
     * Check if a user is logged in
     * 
     * @param session The HttpSession object
     * @return true if logged in, false otherwise
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }
    
    /**
     * Check if the logged in user has admin role
     * 
     * @param session The HttpSession object
     * @return true if admin, false otherwise
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return false;
        }
        return "admin".equalsIgnoreCase(user.getRole());
    }
}
