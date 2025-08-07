package controller.test;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.User;
import service.interfaces.UserService;
import service.impl.UserServiceImpl;
import dao.interfaces.UserDAO;
import dao.impl.UserDAOImpl;
import utils.PasswordEncryption;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

/**
 * Test servlet to verify user management functionality
 */
@WebServlet(name = "TestUserServlet", urlPatterns = {"/test-user"})
public class TestUserServlet extends HttpServlet {
    private UserService userService;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
        this.userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>User Management Test</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("h1, h2 { color: #333; }");
            out.println("pre { background-color: #f5f5f5; padding: 10px; border-radius: 5px; overflow: auto; }");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
            out.println("tr:hover { background-color: #f1f1f1; }");
            out.println(".success { color: green; }");
            out.println(".error { color: red; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>User Management Test Servlet</h1>");
            
            // Get action parameter
            String action = request.getParameter("action");
            if (action == null) {
                action = "dashboard";
            }
            
            switch (action) {
                case "testCreate":
                    testCreateUser(out);
                    break;
                case "testGetAll":
                    testGetAllUsers(out);
                    break;
                case "testUpdate":
                    testUpdateUser(out, request);
                    break;
                case "testDelete":
                    testDeleteUser(out, request);
                    break;
                case "testRestore":
                    testRestoreUser(out, request);
                    break;
                case "testChangeStatus":
                    testChangeUserStatus(out, request);
                    break;
                case "testChangeRole":
                    testChangeUserRole(out, request);
                    break;
                case "testSearch":
                    testSearchUsers(out, request);
                    break;
                default:
                    showDashboard(out);
                    break;
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void showDashboard(PrintWriter out) {
        out.println("<h2>Test Functions</h2>");
        out.println("<ul>");
        out.println("<li><a href='test-user?action=testGetAll'>Test Get All Users</a></li>");
        out.println("<li><a href='test-user?action=testCreate'>Test Create User</a></li>");
        out.println("<li>Test Update User: <form style='display:inline' action='test-user' method='get'><input type='hidden' name='action' value='testUpdate'><input type='number' name='id' placeholder='User ID' required><input type='submit' value='Test'></form></li>");
        out.println("<li>Test Delete User: <form style='display:inline' action='test-user' method='get'><input type='hidden' name='action' value='testDelete'><input type='number' name='id' placeholder='User ID' required><input type='submit' value='Test'></form></li>");
        out.println("<li>Test Restore User: <form style='display:inline' action='test-user' method='get'><input type='hidden' name='action' value='testRestore'><input type='number' name='id' placeholder='User ID' required><input type='submit' value='Test'></form></li>");
        out.println("<li>Test Change User Status: <form style='display:inline' action='test-user' method='get'><input type='hidden' name='action' value='testChangeStatus'><input type='number' name='id' placeholder='User ID' required><select name='status'><option value='active'>Active</option><option value='inactive'>Inactive</option><option value='banned'>Banned</option></select><input type='submit' value='Test'></form></li>");
        out.println("<li>Test Change User Role: <form style='display:inline' action='test-user' method='get'><input type='hidden' name='action' value='testChangeRole'><input type='number' name='id' placeholder='User ID' required><select name='role'><option value='admin'>Admin</option><option value='customer'>Customer</option></select><input type='submit' value='Test'></form></li>");
        out.println("<li>Test Search Users: <form style='display:inline' action='test-user' method='get'><input type='hidden' name='action' value='testSearch'><input type='text' name='keyword' placeholder='Search keyword' required><input type='submit' value='Test'></form></li>");
        out.println("</ul>");
        
        // Show user statistics
        UserDAO.UserStatistics stats = userDAO.getUserStatistics();
        out.println("<h2>User Statistics</h2>");
        out.println("<table>");
        out.println("<tr><th>Total Users</th><td>" + stats.getTotalUsers() + "</td></tr>");
        out.println("<tr><th>Active Users</th><td>" + stats.getActiveUsers() + "</td></tr>");
        out.println("<tr><th>Inactive Users</th><td>" + stats.getInactiveUsers() + "</td></tr>");
        out.println("<tr><th>Admin Users</th><td>" + stats.getAdminUsers() + "</td></tr>");
        out.println("<tr><th>Customer Users</th><td>" + stats.getCustomerUsers() + "</td></tr>");
        out.println("<tr><th>New Users This Month</th><td>" + stats.getNewUsersThisMonth() + "</td></tr>");
        out.println("</table>");
        
        // Show a few users as a sample
        List<User> users = userDAO.getUsersWithPagination(0, 5);
        out.println("<h2>Sample Users (Latest 5)</h2>");
        printUserTable(out, users);
    }
    
    private void testGetAllUsers(PrintWriter out) {
        out.println("<h2>Test Get All Users</h2>");
        try {
            List<User> users = userDAO.getAllUsers();
            out.println("<p class='success'>Retrieved " + users.size() + " users successfully</p>");
            out.println("<h3>User List</h3>");
            printUserTable(out, users);
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testCreateUser(PrintWriter out) {
        out.println("<h2>Test Create User</h2>");
        try {
            // Create a unique test user
            String timestamp = String.valueOf(System.currentTimeMillis());
            User testUser = new User();
            testUser.setUsername("testuser" + timestamp);
            testUser.setPassword(PasswordEncryption.hashPassword("Test@123"));
            testUser.setEmail("testuser" + timestamp + "@example.com");
            testUser.setPhone("0987654321");
            testUser.setFullName("Test User " + timestamp);
            testUser.setRole("customer");
            testUser.setStatus("active");
            testUser.setAvatar("https://via.placeholder.com/150");
            
            boolean result = userDAO.createUser(testUser);
            
            if (result) {
                out.println("<p class='success'>User created successfully with ID: " + testUser.getUserId() + "</p>");
                out.println("<h3>User Details</h3>");
                out.println("<table>");
                out.println("<tr><th>ID</th><td>" + testUser.getUserId() + "</td></tr>");
                out.println("<tr><th>Username</th><td>" + testUser.getUsername() + "</td></tr>");
                out.println("<tr><th>Email</th><td>" + testUser.getEmail() + "</td></tr>");
                out.println("<tr><th>Phone</th><td>" + testUser.getPhone() + "</td></tr>");
                out.println("<tr><th>Full Name</th><td>" + testUser.getFullName() + "</td></tr>");
                out.println("<tr><th>Role</th><td>" + testUser.getRole() + "</td></tr>");
                out.println("<tr><th>Status</th><td>" + testUser.getStatus() + "</td></tr>");
                out.println("</table>");
            } else {
                out.println("<p class='error'>Failed to create user</p>");
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testUpdateUser(PrintWriter out, HttpServletRequest request) {
        out.println("<h2>Test Update User</h2>");
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                out.println("<p class='error'>No user ID provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            int userId = Integer.parseInt(idParam);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                out.println("<p class='error'>User not found with ID: " + userId + "</p>");
            } else {
                // Show current user details
                out.println("<h3>Current User Details</h3>");
                out.println("<table>");
                out.println("<tr><th>ID</th><td>" + user.getUserId() + "</td></tr>");
                out.println("<tr><th>Username</th><td>" + user.getUsername() + "</td></tr>");
                out.println("<tr><th>Email</th><td>" + user.getEmail() + "</td></tr>");
                out.println("<tr><th>Phone</th><td>" + user.getPhone() + "</td></tr>");
                out.println("<tr><th>Full Name</th><td>" + user.getFullName() + "</td></tr>");
                out.println("<tr><th>Role</th><td>" + user.getRole() + "</td></tr>");
                out.println("<tr><th>Status</th><td>" + user.getStatus() + "</td></tr>");
                out.println("</table>");
                
                // Update some fields
                String timestamp = String.valueOf(System.currentTimeMillis());
                String oldFullName = user.getFullName();
                user.setFullName(oldFullName + " (Updated " + timestamp + ")");
                user.setPhone("0987" + timestamp.substring(timestamp.length() - 6));
                
                boolean result = userDAO.updateUser(user);
                
                if (result) {
                    out.println("<p class='success'>User updated successfully</p>");
                    
                    // Show updated user details
                    User updatedUser = userDAO.getUserById(userId);
                    out.println("<h3>Updated User Details</h3>");
                    out.println("<table>");
                    out.println("<tr><th>ID</th><td>" + updatedUser.getUserId() + "</td></tr>");
                    out.println("<tr><th>Username</th><td>" + updatedUser.getUsername() + "</td></tr>");
                    out.println("<tr><th>Email</th><td>" + updatedUser.getEmail() + "</td></tr>");
                    out.println("<tr><th>Phone</th><td>" + updatedUser.getPhone() + "</td></tr>");
                    out.println("<tr><th>Full Name</th><td>" + updatedUser.getFullName() + "</td></tr>");
                    out.println("<tr><th>Role</th><td>" + updatedUser.getRole() + "</td></tr>");
                    out.println("<tr><th>Status</th><td>" + updatedUser.getStatus() + "</td></tr>");
                    out.println("</table>");
                } else {
                    out.println("<p class='error'>Failed to update user</p>");
                }
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testDeleteUser(PrintWriter out, HttpServletRequest request) {
        out.println("<h2>Test Delete User</h2>");
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                out.println("<p class='error'>No user ID provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            int userId = Integer.parseInt(idParam);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                out.println("<p class='error'>User not found or already deleted with ID: " + userId + "</p>");
            } else {
                // Show user details
                out.println("<h3>User Details Before Deletion</h3>");
                out.println("<table>");
                out.println("<tr><th>ID</th><td>" + user.getUserId() + "</td></tr>");
                out.println("<tr><th>Username</th><td>" + user.getUsername() + "</td></tr>");
                out.println("<tr><th>Email</th><td>" + user.getEmail() + "</td></tr>");
                out.println("<tr><th>Full Name</th><td>" + user.getFullName() + "</td></tr>");
                out.println("<tr><th>Is Deleted</th><td>" + user.isIsDeleted() + "</td></tr>");
                out.println("</table>");
                
                boolean result = userDAO.deleteUser(userId);
                
                if (result) {
                    out.println("<p class='success'>User deleted (soft delete) successfully</p>");
                    
                    // Try to get the deleted user using direct query
                    try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                         java.sql.PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE user_id = ?")) {
                        
                        ps.setInt(1, userId);
                        
                        try (java.sql.ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                boolean isDeleted = rs.getBoolean("is_deleted");
                                out.println("<p>User still exists in database with is_deleted = " + isDeleted + "</p>");
                                
                                if (isDeleted) {
                                    out.println("<p class='success'>Soft delete confirmed in database</p>");
                                } else {
                                    out.println("<p class='error'>User was not marked as deleted in database!</p>");
                                }
                            } else {
                                out.println("<p class='error'>User completely removed from database (hard delete)!</p>");
                            }
                        }
                    } catch (Exception e) {
                        out.println("<p class='error'>Database check error: " + e.getMessage() + "</p>");
                    }
                    
                    // Show that getUserById now returns null (as expected for deleted users)
                    User deletedUser = userDAO.getUserById(userId);
                    if (deletedUser == null) {
                        out.println("<p class='success'>getUserById() correctly returns null for deleted user</p>");
                    } else {
                        out.println("<p class='error'>getUserById() should return null but returned a user!</p>");
                    }
                    
                } else {
                    out.println("<p class='error'>Failed to delete user</p>");
                }
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testRestoreUser(PrintWriter out, HttpServletRequest request) {
        out.println("<h2>Test Restore User</h2>");
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                out.println("<p class='error'>No user ID provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            int userId = Integer.parseInt(idParam);
            
            // We need to check if the user exists, even if deleted
            try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE user_id = ?")) {
                
                ps.setInt(1, userId);
                
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        boolean isDeleted = rs.getBoolean("is_deleted");
                        out.println("<h3>User Details Before Restoration</h3>");
                        out.println("<table>");
                        out.println("<tr><th>ID</th><td>" + rs.getInt("user_id") + "</td></tr>");
                        out.println("<tr><th>Username</th><td>" + rs.getString("username") + "</td></tr>");
                        out.println("<tr><th>Email</th><td>" + rs.getString("email") + "</td></tr>");
                        out.println("<tr><th>Full Name</th><td>" + rs.getString("full_name") + "</td></tr>");
                        out.println("<tr><th>Is Deleted</th><td>" + isDeleted + "</td></tr>");
                        out.println("</table>");
                        
                        if (!isDeleted) {
                            out.println("<p class='error'>User is not deleted, cannot restore!</p>");
                            out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                            return;
                        }
                    } else {
                        out.println("<p class='error'>User does not exist with ID: " + userId + "</p>");
                        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                        return;
                    }
                }
            } catch (Exception e) {
                out.println("<p class='error'>Database check error: " + e.getMessage() + "</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            boolean result = userDAO.restoreUser(userId);
            
            if (result) {
                out.println("<p class='success'>User restored successfully</p>");
                
                // Now we should be able to get the user with getUserById
                User restoredUser = userDAO.getUserById(userId);
                if (restoredUser != null) {
                    out.println("<h3>Restored User Details</h3>");
                    out.println("<table>");
                    out.println("<tr><th>ID</th><td>" + restoredUser.getUserId() + "</td></tr>");
                    out.println("<tr><th>Username</th><td>" + restoredUser.getUsername() + "</td></tr>");
                    out.println("<tr><th>Email</th><td>" + restoredUser.getEmail() + "</td></tr>");
                    out.println("<tr><th>Full Name</th><td>" + restoredUser.getFullName() + "</td></tr>");
                    out.println("<tr><th>Is Deleted</th><td>" + restoredUser.isIsDeleted() + "</td></tr>");
                    out.println("</table>");
                    out.println("<p class='success'>User successfully restored and accessible via getUserById()</p>");
                } else {
                    out.println("<p class='error'>Restoration reported success but user cannot be retrieved with getUserById()!</p>");
                }
            } else {
                out.println("<p class='error'>Failed to restore user</p>");
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testChangeUserStatus(PrintWriter out, HttpServletRequest request) {
        out.println("<h2>Test Change User Status</h2>");
        try {
            String idParam = request.getParameter("id");
            String status = request.getParameter("status");
            
            if (idParam == null || idParam.isEmpty()) {
                out.println("<p class='error'>No user ID provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            if (status == null || status.isEmpty()) {
                out.println("<p class='error'>No status provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            int userId = Integer.parseInt(idParam);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                out.println("<p class='error'>User not found with ID: " + userId + "</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            out.println("<h3>User Details Before Status Change</h3>");
            out.println("<table>");
            out.println("<tr><th>ID</th><td>" + user.getUserId() + "</td></tr>");
            out.println("<tr><th>Username</th><td>" + user.getUsername() + "</td></tr>");
            out.println("<tr><th>Email</th><td>" + user.getEmail() + "</td></tr>");
            out.println("<tr><th>Current Status</th><td>" + user.getStatus() + "</td></tr>");
            out.println("</table>");
            
            if (status.equals(user.getStatus())) {
                out.println("<p class='error'>User already has status: " + status + "</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            boolean result = userDAO.updateUserStatus(userId, status);
            
            if (result) {
                out.println("<p class='success'>User status changed successfully</p>");
                
                // Verify the status change
                User updatedUser = userDAO.getUserById(userId);
                if (updatedUser != null) {
                    out.println("<h3>User Details After Status Change</h3>");
                    out.println("<table>");
                    out.println("<tr><th>ID</th><td>" + updatedUser.getUserId() + "</td></tr>");
                    out.println("<tr><th>Username</th><td>" + updatedUser.getUsername() + "</td></tr>");
                    out.println("<tr><th>Email</th><td>" + updatedUser.getEmail() + "</td></tr>");
                    out.println("<tr><th>New Status</th><td>" + updatedUser.getStatus() + "</td></tr>");
                    out.println("</table>");
                    
                    if (status.equals(updatedUser.getStatus())) {
                        out.println("<p class='success'>Status change verified</p>");
                    } else {
                        out.println("<p class='error'>Status change failed! Status is still: " + updatedUser.getStatus() + "</p>");
                    }
                } else {
                    out.println("<p class='error'>Could not retrieve user after status change</p>");
                }
            } else {
                out.println("<p class='error'>Failed to change user status</p>");
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testChangeUserRole(PrintWriter out, HttpServletRequest request) {
        out.println("<h2>Test Change User Role</h2>");
        try {
            String idParam = request.getParameter("id");
            String role = request.getParameter("role");
            
            if (idParam == null || idParam.isEmpty()) {
                out.println("<p class='error'>No user ID provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            if (role == null || role.isEmpty()) {
                out.println("<p class='error'>No role provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            int userId = Integer.parseInt(idParam);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                out.println("<p class='error'>User not found with ID: " + userId + "</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            out.println("<h3>User Details Before Role Change</h3>");
            out.println("<table>");
            out.println("<tr><th>ID</th><td>" + user.getUserId() + "</td></tr>");
            out.println("<tr><th>Username</th><td>" + user.getUsername() + "</td></tr>");
            out.println("<tr><th>Email</th><td>" + user.getEmail() + "</td></tr>");
            out.println("<tr><th>Current Role</th><td>" + user.getRole() + "</td></tr>");
            out.println("</table>");
            
            if (role.equals(user.getRole())) {
                out.println("<p class='error'>User already has role: " + role + "</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            boolean result = userDAO.updateUserRole(userId, role);
            
            if (result) {
                out.println("<p class='success'>User role changed successfully</p>");
                
                // Verify the role change
                User updatedUser = userDAO.getUserById(userId);
                if (updatedUser != null) {
                    out.println("<h3>User Details After Role Change</h3>");
                    out.println("<table>");
                    out.println("<tr><th>ID</th><td>" + updatedUser.getUserId() + "</td></tr>");
                    out.println("<tr><th>Username</th><td>" + updatedUser.getUsername() + "</td></tr>");
                    out.println("<tr><th>Email</th><td>" + updatedUser.getEmail() + "</td></tr>");
                    out.println("<tr><th>New Role</th><td>" + updatedUser.getRole() + "</td></tr>");
                    out.println("</table>");
                    
                    if (role.equals(updatedUser.getRole())) {
                        out.println("<p class='success'>Role change verified</p>");
                    } else {
                        out.println("<p class='error'>Role change failed! Role is still: " + updatedUser.getRole() + "</p>");
                    }
                } else {
                    out.println("<p class='error'>Could not retrieve user after role change</p>");
                }
            } else {
                out.println("<p class='error'>Failed to change user role</p>");
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void testSearchUsers(PrintWriter out, HttpServletRequest request) {
        out.println("<h2>Test Search Users</h2>");
        try {
            String keyword = request.getParameter("keyword");
            
            if (keyword == null || keyword.isEmpty()) {
                out.println("<p class='error'>No search keyword provided</p>");
                out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
                return;
            }
            
            out.println("<p>Searching for users matching keyword: <strong>" + keyword + "</strong></p>");
            
            List<User> users = userDAO.searchUsers(keyword);
            
            out.println("<p>Found " + users.size() + " matching users</p>");
            
            if (!users.isEmpty()) {
                out.println("<h3>Search Results</h3>");
                printUserTable(out, users);
            } else {
                out.println("<p>No users found matching the keyword.</p>");
            }
            
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("<p><a href='test-user'>Back to Dashboard</a></p>");
    }
    
    private void printUserTable(PrintWriter out, List<User> users) {
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Username</th>");
        out.println("<th>Email</th>");
        out.println("<th>Full Name</th>");
        out.println("<th>Role</th>");
        out.println("<th>Status</th>");
        out.println("<th>Created</th>");
        out.println("<th>Last Login</th>");
        out.println("</tr>");
        
        for (User user : users) {
            out.println("<tr>");
            out.println("<td>" + user.getUserId() + "</td>");
            out.println("<td>" + user.getUsername() + "</td>");
            out.println("<td>" + user.getEmail() + "</td>");
            out.println("<td>" + user.getFullName() + "</td>");
            out.println("<td>" + user.getRole() + "</td>");
            out.println("<td>" + user.getStatus() + "</td>");
            out.println("<td>" + (user.getCreatedAt() != null ? user.getCreatedAt() : "N/A") + "</td>");
            out.println("<td>" + (user.getLastLogin() != null ? user.getLastLogin() : "N/A") + "</td>");
            out.println("</tr>");
        }
        
        out.println("</table>");
    }
}
