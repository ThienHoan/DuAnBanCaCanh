package controller.admin;

import model.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Admin Panel Controller - Main admin dashboard
 */
@WebServlet(urlPatterns = {"/admin-panel"})
public class AdminPanelController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminPanelController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check admin authentication
        if (!isAdminAuthenticated(request, response)) {
            return;
        }

        LOGGER.info("AdminPanelController: Loading admin panel");

        // Forward to admin panel JSP
        request.getRequestDispatcher("/admin-panel.jsp").forward(request, response);
    }

    /**
     * Check if user is admin
     */
    private boolean isAdminAuthenticated(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Admin role required.");
            return false;
        }

        return true;
    }
}
