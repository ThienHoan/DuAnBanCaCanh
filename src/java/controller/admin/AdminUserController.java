package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.entity.User;
import service.interfaces.UserService;
import service.impl.UserServiceImpl;
import dao.interfaces.UserDAO.UserStatistics;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class AdminUserController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminUserController.class.getName());
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !userService.isUserAdmin(currentUser.getUserId())) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    handleListUsers(request, response, currentUser.getUserId());
                    break;
                case "view":
                    handleViewUser(request, response, currentUser.getUserId());
                    break;
                case "create":
                    handleCreateUser(request, response, currentUser.getUserId());
                    break;
                case "edit":
                    handleEditUser(request, response, currentUser.getUserId());
                    break;
                case "delete":
                    handleDeleteUser(request, response, currentUser.getUserId());
                    break;
                case "restore":
                    handleRestoreUser(request, response, currentUser.getUserId());
                    break;
                case "updateStatus":
                    handleUpdateStatus(request, response, currentUser.getUserId());
                    break;
                case "updateRole":
                    handleUpdateRole(request, response, currentUser.getUserId());
                    break;
                case "resetPassword":
                    handleResetPassword(request, response, currentUser.getUserId());
                    break;
                case "search":
                    handleSearchUsers(request, response, currentUser.getUserId());
                    break;
                case "statistics":
                    handleGetStatistics(request, response, currentUser.getUserId());
                    break;
                default:
                    handleListUsers(request, response, currentUser.getUserId());
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Error in AdminUserController: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !userService.isUserAdmin(currentUser.getUserId())) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
                case "create":
                    handleCreateUserPost(request, response, currentUser.getUserId());
                    break;
                case "update":
                    handleUpdateUserPost(request, response, currentUser.getUserId());
                    break;
                case "updateStatus":
                    handleUpdateStatusPost(request, response, currentUser.getUserId());
                    break;
                case "updateRole":
                    handleUpdateRolePost(request, response, currentUser.getUserId());
                    break;
                case "resetPassword":
                    handleResetPasswordPost(request, response, currentUser.getUserId());
                    break;                default:
                    response.sendRedirect(request.getContextPath() + "/admin-users");
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Error in AdminUserController POST: " + e.getMessage());
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        }
    }
    
    private void handleListUsers(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        // Get pagination parameters
        int page = 1;
        int pageSize = 10;
        
        try {
            page = Integer.parseInt(request.getParameter("page"));
            if (page < 1) page = 1;
        } catch (NumberFormatException e) {
            page = 1;
        }
        
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
            if (pageSize < 1 || pageSize > 100) pageSize = 10;
        } catch (NumberFormatException e) {
            pageSize = 10;
        }
        
        // Get filter parameters
        String roleFilter = request.getParameter("role");
        String statusFilter = request.getParameter("status");
        
        List<User> users;
        int totalPages;
        
        if (roleFilter != null && !roleFilter.isEmpty()) {
            users = userService.getUsersByRole(roleFilter, adminUserId);
            totalPages = 1; // Simple pagination for filtered results
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
            users = userService.getUsersByStatus(statusFilter, adminUserId);
            totalPages = 1;
        } else {
            users = userService.getUsersWithPagination(page, pageSize, adminUserId);
            totalPages = userService.getTotalPages(pageSize, adminUserId);
        }
        
        // Get statistics
        UserStatistics stats = userService.getUserStatistics(adminUserId);
        
        // Set attributes
        request.setAttribute("users", users);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("roleFilter", roleFilter);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("userStatistics", stats);
        
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }
    
    private void handleViewUser(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById(userId);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.setAttribute("viewMode", true);
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy người dùng");
                handleListUsers(request, response, adminUserId);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID người dùng không hợp lệ");
            handleListUsers(request, response, adminUserId);
        }
    }      private void handleCreateUser(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        // Tạo một User object hoàn toàn rỗng cho form tạo mới
        User newUser = new User();
        // Đảm bảo tất cả các trường đều null/empty
        newUser.setUserId(0);
        newUser.setUsername("");
        newUser.setEmail("");
        newUser.setFullName("");
        newUser.setPhone("");
        newUser.setRole("customer"); // Mặc định là customer
        newUser.setStatus("active"); // Mặc định là active
        newUser.setGoogleId(null);
        newUser.setCreatedAt(null);
        newUser.setLastLogin(null);
        
        // Xóa bất kỳ thông tin user nào có thể có trong request
        request.removeAttribute("user");
        
        request.setAttribute("user", newUser);
        request.setAttribute("createMode", true);
        request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
    }
    
    private void handleEditUser(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById(userId);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.setAttribute("editMode", true);
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy người dùng");
                handleListUsers(request, response, adminUserId);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID người dùng không hợp lệ");
            handleListUsers(request, response, adminUserId);
        }
    }
      private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            
            if (userService.deleteUser(userId, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Xóa người dùng thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa người dùng!");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ!");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi xóa người dùng: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin-users");
    }
      private void handleRestoreUser(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            
            if (userService.restoreUser(userId, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Khôi phục người dùng thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể khôi phục người dùng!");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ!");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khôi phục người dùng: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin-users");
    }
    
    private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");
            
            if (userService.updateUserStatus(userId, status, adminUserId)) {
                request.setAttribute("successMessage", "Cập nhật trạng thái thành công");
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật trạng thái");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID người dùng không hợp lệ");
        }
        
        handleListUsers(request, response, adminUserId);
    }
    
    private void handleUpdateRole(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            String role = request.getParameter("role");
            
            if (userService.updateUserRole(userId, role, adminUserId)) {
                request.setAttribute("successMessage", "Cập nhật vai trò thành công");
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật vai trò");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID người dùng không hợp lệ");
        }
        
        handleListUsers(request, response, adminUserId);
    }
    
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById(userId);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.setAttribute("resetPasswordMode", true);
                request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy người dùng");
                handleListUsers(request, response, adminUserId);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID người dùng không hợp lệ");
            handleListUsers(request, response, adminUserId);
        }
    }
    
    private void handleSearchUsers(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<User> users = userService.searchUsers(keyword.trim(), adminUserId);
            request.setAttribute("users", users);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("searchResults", true);
        } else {
            request.setAttribute("errorMessage", "Vui lòng nhập từ khóa tìm kiếm");
        }
        
        // Get statistics
        UserStatistics stats = userService.getUserStatistics(adminUserId);
        request.setAttribute("userStatistics", stats);
        
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }
    
    private void handleGetStatistics(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        UserStatistics stats = userService.getUserStatistics(adminUserId);
        request.setAttribute("userStatistics", stats);
        request.setAttribute("statisticsOnly", true);
          request.getRequestDispatcher("/admin/user-statistics.jsp").forward(request, response);
    }
    
    // POST handlers
    private void handleCreateUserPost(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));
            user.setPhone(request.getParameter("phone"));
            user.setFullName(request.getParameter("fullName"));
            user.setRole(request.getParameter("role"));
            user.setStatus(request.getParameter("status"));
            user.setAvatar(request.getParameter("avatar"));
            
            if (userService.createUser(user, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Tạo người dùng '" + user.getUsername() + "' thành công!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
                return;
            } else {
                request.setAttribute("errorMessage", "Không thể tạo người dùng. Vui lòng kiểm tra lại thông tin.");
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error creating user: " + e.getMessage());
            request.setAttribute("errorMessage", "Lỗi tạo người dùng: " + e.getMessage());
        }
        
        request.setAttribute("createMode", true);
        request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
    }
    
    private void handleUpdateUserPost(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            
            User user = userService.getUserById(userId);
            if (user == null) {
                request.setAttribute("errorMessage", "Không tìm thấy người dùng");
                handleListUsers(request, response, adminUserId);
                return;
            }
            
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setPhone(request.getParameter("phone"));
            user.setFullName(request.getParameter("fullName"));
            user.setRole(request.getParameter("role"));
            user.setStatus(request.getParameter("status"));
            user.setAvatar(request.getParameter("avatar"));
              if (userService.updateUser(user, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Cập nhật người dùng '" + user.getUsername() + "' thành công!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
                return;
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật người dùng");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID người dùng không hợp lệ");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        
        request.setAttribute("editMode", true);
        request.getRequestDispatcher("/admin/user-form.jsp").forward(request, response);
    }
      private void handleUpdateStatusPost(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String status = request.getParameter("status");
            
            if (userService.updateUserStatus(userId, status, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Cập nhật trạng thái người dùng thành công!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể cập nhật trạng thái người dùng!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin-users");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cập nhật trạng thái: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-users");
        }
    }
      private void handleUpdateRolePost(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String role = request.getParameter("role");
            
            if (userService.updateUserRole(userId, role, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Cập nhật vai trò người dùng thành công!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể cập nhật vai trò người dùng!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin-users");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cập nhật vai trò: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-users");
        }
    }
      private void handleResetPasswordPost(HttpServletRequest request, HttpServletResponse response, int adminUserId) 
            throws ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String newPassword = request.getParameter("newPassword");
            
            if (userService.resetPassword(userId, newPassword, adminUserId)) {
                request.getSession().setAttribute("successMessage", "Đặt lại mật khẩu thành công!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể đặt lại mật khẩu!");
                response.sendRedirect(request.getContextPath() + "/admin-users");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin-users");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi đặt lại mật khẩu: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin-users");
        }
    }
}
