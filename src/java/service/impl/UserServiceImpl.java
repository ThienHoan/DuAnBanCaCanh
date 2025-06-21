package service.impl;

import dao.interfaces.UserDAO;
import dao.impl.UserDAOImpl;
import service.interfaces.UserService;
import model.entity.User;
import utils.PasswordEncryption;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Implementation of UserService interface
 */
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDAO userDAO;
    
    // Password validation patterns
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    // Public user operations
    @Override
    public boolean registerUser(User user) {
        try {
            // Validate user data
            if (!validateUserData(user)) {
                LOGGER.warning("Invalid user data for registration: " + user.getUsername());
                return false;
            }
            
            // Check if username exists
            if (userDAO.isUsernameExists(user.getUsername())) {
                LOGGER.warning("Username already exists: " + user.getUsername());
                return false;
            }
            
            // Check if email exists
            if (userDAO.isEmailExists(user.getEmail())) {
                LOGGER.warning("Email already exists: " + user.getEmail());
                return false;
            }
            
            // Encrypt password
            String hashedPassword = PasswordEncryption.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            
            // Set default values
            user.setRole("customer");
            user.setStatus("active");
            
            boolean result = userDAO.createUser(user);
            
            if (result) {
                LOGGER.info("User registered successfully: " + user.getUsername());
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in registerUser service", e);
            return false;
        }
    }
    
    @Override
    public User login(String username, String password) {
        try {
            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                return null;
            }
            
            User user = userDAO.getUserByUsername(username.trim());
            
            if (user == null) {
                LOGGER.info("User not found: " + username);
                return null;
            }
            
            if (!"active".equals(user.getStatus())) {
                LOGGER.info("User account is not active: " + username);
                return null;
            }
            
            if (PasswordEncryption.verifyPassword(password, user.getPassword())) {
                // Update last login
                userDAO.updateLastLogin(user.getUserId());
                LOGGER.info("User logged in successfully: " + username);
                return user;
            } else {
                LOGGER.info("Invalid password for user: " + username);
                return null;
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in login service", e);
            return null;
        }
    }
    
    @Override
    public User loginWithGoogle(String googleId, String email, String fullName, String avatar) {
        try {
            if (googleId == null || email == null) {
                return null;
            }
            
            // Try to find user by Google ID first
            User user = userDAO.getUserByEmail(email);
            
            if (user != null) {
                // Update Google ID and avatar if needed
                if (user.getGoogleId() == null || !googleId.equals(user.getGoogleId())) {
                    user.setGoogleId(googleId);
                    user.setAvatar(avatar);
                    userDAO.updateUser(user);
                }
                
                // Update last login
                userDAO.updateLastLogin(user.getUserId());
                
                return user;
            } else {
                // Create new user for Google login
                User newUser = new User();
                newUser.setUsername(email.split("@")[0]); // Use email prefix as username
                newUser.setEmail(email);
                newUser.setFullName(fullName);
                newUser.setGoogleId(googleId);
                newUser.setAvatar(avatar);
                newUser.setRole("customer");
                newUser.setStatus("active");
                
                // Generate a random password (user won't use it)
                newUser.setPassword(PasswordEncryption.hashPassword("google_auth_" + System.currentTimeMillis()));
                
                if (userDAO.createUser(newUser)) {
                    LOGGER.info("New Google user created: " + email);
                    return newUser;
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in Google login service", e);
        }
        return null;
    }
    
    @Override
    public User getUserById(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getUserById service", e);
            return null;
        }
    }
    
    @Override
    public boolean updateProfile(User user) {
        try {
            if (user == null || user.getUserId() <= 0) {
                return false;
            }
            
            // Don't allow updating role/status through profile update
            User existingUser = userDAO.getUserById(user.getUserId());
            if (existingUser == null) {
                return false;
            }
            
            user.setRole(existingUser.getRole());
            user.setStatus(existingUser.getStatus());
            user.setPassword(existingUser.getPassword()); // Don't update password here
            
            return userDAO.updateUser(user);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateProfile service", e);
            return false;
        }
    }
    
    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        try {
            if (!validatePassword(newPassword)) {
                LOGGER.warning("Invalid new password format for user: " + userId);
                return false;
            }
            
            User user = userDAO.getUserById(userId);
            if (user == null) {
                return false;
            }
            
            // Verify old password
            if (!PasswordEncryption.verifyPassword(oldPassword, user.getPassword())) {
                LOGGER.warning("Invalid old password for user: " + userId);
                return false;
            }
            
            String hashedNewPassword = PasswordEncryption.hashPassword(newPassword);
            boolean result = userDAO.changePassword(userId, hashedNewPassword);
            
            if (result) {
                LOGGER.info("Password changed successfully for user: " + userId);
            }
            
            return result;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in changePassword service", e);
            return false;
        }
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        try {
            return userDAO.isUsernameExists(username);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking username exists", e);
            return false;
        }
    }
    
    @Override
    public boolean isEmailExists(String email) {
        try {
            return userDAO.isEmailExists(email);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking email exists", e);
            return false;
        }
    }
    
    @Override
    public boolean updateLastLogin(int userId) {
        try {
            return userDAO.updateLastLogin(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating last login", e);
            return false;
        }
    }
    
    // Admin operations
    @Override
    public List<User> getAllUsers(int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to get all users: " + adminUserId);
                return List.of();
            }
            
            return userDAO.getAllUsers();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getAllUsers service", e);
            return List.of();
        }
    }
    
    @Override
    public List<User> getUsersWithPagination(int page, int pageSize, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to get users with pagination: " + adminUserId);
                return List.of();
            }
            
            if (page < 1) page = 1;
            if (pageSize < 1) pageSize = 10;
            
            int offset = (page - 1) * pageSize;
            return userDAO.getUsersWithPagination(offset, pageSize);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getUsersWithPagination service", e);
            return List.of();
        }
    }
    
    @Override
    public List<User> searchUsers(String keyword, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to search users: " + adminUserId);
                return List.of();
            }
            
            if (keyword == null || keyword.trim().isEmpty()) {
                return List.of();
            }
            
            return userDAO.searchUsers(keyword.trim());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in searchUsers service", e);
            return List.of();
        }
    }
    
    @Override
    public List<User> getUsersByRole(String role, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                return List.of();
            }
            
            return userDAO.getUsersByRole(role);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getUsersByRole service", e);
            return List.of();
        }
    }
    
    @Override
    public List<User> getUsersByStatus(String status, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                return List.of();
            }
            
            return userDAO.getUsersByStatus(status);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getUsersByStatus service", e);
            return List.of();
        }
    }
    
    @Override
    public boolean createUser(User user, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to create user: " + adminUserId);
                return false;
            }
            
            return registerUser(user);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in createUser service", e);
            return false;
        }
    }
    
    @Override
    public boolean updateUser(User user, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to update user: " + adminUserId);
                return false;
            }
            
            if (user == null || user.getUserId() <= 0) {
                return false;
            }
            
            return userDAO.updateUser(user);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateUser service", e);
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(int userId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to delete user: " + adminUserId);
                return false;
            }
            
            // Don't allow admin to delete themselves
            if (userId == adminUserId) {
                LOGGER.warning("Admin attempted to delete themselves: " + adminUserId);
                return false;
            }
            
            return userDAO.deleteUser(userId);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in deleteUser service", e);
            return false;
        }
    }
    
    @Override
    public boolean restoreUser(int userId, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to restore user: " + adminUserId);
                return false;
            }
            
            return userDAO.restoreUser(userId);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in restoreUser service", e);
            return false;
        }
    }
    
    @Override
    public boolean updateUserStatus(int userId, String status, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to update user status: " + adminUserId);
                return false;
            }
            
            // Don't allow admin to deactivate themselves
            if (userId == adminUserId && "inactive".equals(status)) {
                LOGGER.warning("Admin attempted to deactivate themselves: " + adminUserId);
                return false;
            }
            
            return userDAO.updateUserStatus(userId, status);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateUserStatus service", e);
            return false;
        }
    }
    
    @Override
    public boolean updateUserRole(int userId, String role, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to update user role: " + adminUserId);
                return false;
            }
            
            return userDAO.updateUserRole(userId, role);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateUserRole service", e);
            return false;
        }
    }
    
    @Override
    public boolean resetPassword(int userId, String newPassword, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to reset password: " + adminUserId);
                return false;
            }
            
            if (!validatePassword(newPassword)) {
                LOGGER.warning("Invalid password format for reset: " + userId);
                return false;
            }
            
            String hashedPassword = PasswordEncryption.hashPassword(newPassword);
            return userDAO.changePassword(userId, hashedPassword);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in resetPassword service", e);
            return false;
        }
    }
    
    @Override
    public int getTotalPages(int pageSize, int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                return 0;
            }
            
            if (pageSize < 1) pageSize = 10;
            
            int totalUsers = userDAO.getTotalUsersCount();
            return (int) Math.ceil((double) totalUsers / pageSize);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getTotalPages service", e);
            return 0;
        }
    }
    
    @Override
    public UserDAO.UserStatistics getUserStatistics(int adminUserId) {
        try {
            if (!isUserAdmin(adminUserId)) {
                LOGGER.warning("Non-admin user attempted to get user statistics: " + adminUserId);
                return new UserDAO.UserStatistics();
            }
            
            return userDAO.getUserStatistics();
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getUserStatistics service", e);
            return new UserDAO.UserStatistics();
        }
    }
    
    @Override
    public boolean isUserAdmin(int userId) {
        try {
            User user = userDAO.getUserById(userId);
            return user != null && "admin".equals(user.getRole());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking admin status", e);
            return false;
        }
    }
    
    @Override
    public boolean validateUserData(User user) {
        if (user == null) return false;
        
        // Validate username
        if (user.getUsername() == null || user.getUsername().trim().length() < 3 || user.getUsername().trim().length() > 50) {
            return false;
        }
        
        // Validate email
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            return false;
        }
        
        // Validate password (only for new users)
        if (user.getUserId() <= 0 && !validatePassword(user.getPassword())) {
            return false;
        }
        
        // Validate full name
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean validatePassword(String password) {
        if (password == null) return false;
        
        // Simple validation: at least 6 characters
        return password.length() >= 6;
        
        // For stronger validation, use:
        // return PASSWORD_PATTERN.matcher(password).matches();
    }
}
