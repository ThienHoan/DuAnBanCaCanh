package controller.client;

import dao.impl.UserDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Timestamp;
import utils.OAuthConfig;



public class GoogleLoginServlet extends HttpServlet {
    
    // Google OAuth URLs
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    /**
     * Xử lý GET request - chuyển hướng đến Google OAuth hoặc xử lý callback
     */    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("=== GoogleLoginServlet doGet called ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Servlet Path: " + request.getServletPath());
        System.out.println("Context Path: " + request.getContextPath());
        
        String path = request.getServletPath();
          if ("/auth/google".equals(path)) {
            System.out.println("Redirecting to Google OAuth...");
            // Chuyển hướng đến Google OAuth
            redirectToGoogle(request, response);
        } else if ("/auth/google/callback".equals(path) || "/googlecallback".equals(path)) {
            System.out.println("Handling Google callback...");
            // Xử lý callback từ Google
            handleGoogleCallback(request, response);
        } else {
            System.out.println("Unknown path: " + path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
      /**
     * Chuyển hướng người dùng đến Google OAuth
     */
    private void redirectToGoogle(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String googleAuthURL = OAuthConfig.GOOGLE_AUTH_URL
                + "?response_type=code"
                + "&client_id=" + OAuthConfig.getGoogleClientId()
                + "&redirect_uri=" + OAuthConfig.getGoogleRedirectUri()
                + "&scope=email%20profile"
                + "&access_type=offline"
                + "&prompt=consent";
        
        System.out.println("Redirecting to Google OAuth: " + googleAuthURL);
        response.sendRedirect(googleAuthURL);
    }
    
    
    private void handleGoogleCallback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String code = request.getParameter("code");
        String error = request.getParameter("error");
        
        if (error != null) {
            System.out.println("Google OAuth error: " + error);
            request.setAttribute("error", "Đăng nhập Google bị hủy hoặc có lỗi xảy ra!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        if (code == null) {
            System.out.println("No authorization code received from Google");
            request.setAttribute("error", "Không nhận được mã xác thực từ Google!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Lấy access token từ Google
            String accessToken = getAccessToken(code);
            
            if (accessToken != null) {
                // Lấy thông tin user từ Google
                JsonObject userInfo = getUserInfo(accessToken);
                
                if (userInfo != null) {
                    // Xử lý đăng nhập/đăng ký user
                    handleGoogleUser(userInfo, request, response);
                } else {
                    throw new Exception("Không thể lấy thông tin người dùng từ Google");
                }
            } else {
                throw new Exception("Không thể lấy access token từ Google");
            }
            
        } catch (Exception e) {
            System.out.println("Google login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi trong quá trình đăng nhập Google: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
      /**
     * Lấy access token từ Google bằng authorization code
     */    private String getAccessToken(String code) throws Exception {
        String tokenRequestBody = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(OAuthConfig.getGoogleClientId(), StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(OAuthConfig.getGoogleClientSecret(), StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(OAuthConfig.getGoogleRedirectUri(), StandardCharsets.UTF_8)
                + "&grant_type=" + URLEncoder.encode(OAuthConfig.getGoogleGrantType(), StandardCharsets.UTF_8);
        
        URL url = new URL(GOOGLE_TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
          // Gửi request
        conn.getOutputStream().write(tokenRequestBody.getBytes(StandardCharsets.UTF_8));
        
        // Đọc response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        JsonParser parser = new JsonParser();
        JsonObject tokenResponse = parser.parse(response.toString()).getAsJsonObject();
        
        return tokenResponse.has("access_token") ? tokenResponse.get("access_token").getAsString() : null;
    }    /**
     * Lấy thông tin user từ Google bằng access token
     */
    private JsonObject getUserInfo(String accessToken) throws Exception {
        URL url = new URL(OAuthConfig.GOOGLE_USER_INFO_URL + "?access_token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        JsonParser parser = new JsonParser();
        JsonObject userInfo = parser.parse(response.toString()).getAsJsonObject();
        
        return userInfo;
    }
    
    /**
     * Xử lý thông tin user từ Google - tạo mới hoặc đăng nhập
     */
    private void handleGoogleUser(JsonObject userInfo, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String googleId = userInfo.get("id").getAsString();
        String email = userInfo.get("email").getAsString();
        String name = userInfo.get("name").getAsString();
        String picture = userInfo.has("picture") ? userInfo.get("picture").getAsString() : null;
        
        System.out.println("Google user info - ID: " + googleId + ", Email: " + email + ", Name: " + name);
        
        UserDAO userDAO = new UserDAO();
        
        // Kiểm tra xem user đã tồn tại chưa (theo email)
        User existingUser = userDAO.getUserByEmail(email);
        
        if (existingUser != null) {
            // User đã tồn tại, cập nhật thông tin Google nếu cần
            if (existingUser.getGoogleId() == null || !existingUser.getGoogleId().equals(googleId)) {
                userDAO.updateGoogleId(existingUser.getUserId(), googleId);
                existingUser.setGoogleId(googleId);
            }
            
            // Cập nhật avatar nếu chưa có
            if ((existingUser.getAvatar() == null || existingUser.getAvatar().isEmpty()) && picture != null) {
                userDAO.updateAvatar(existingUser.getUserId(), picture);
                existingUser.setAvatar(picture);
            }
            
            // Đăng nhập user
            loginUser(existingUser, request, response);
            
        } else {
            // Tạo user mới từ Google
            User newUser = new User();
            newUser.setUsername(generateUsername(email)); // Tạo username từ email
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setGoogleId(googleId);
            newUser.setAvatar(picture);
            newUser.setRole("customer");
            newUser.setStatus("active");
            newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            
            // Lưu user vào database
            boolean created = userDAO.createGoogleUser(newUser);
            
            if (created) {
                // Lấy user với ID đã được tạo
                User createdUser = userDAO.getUserByEmail(email);
                if (createdUser != null) {
                    loginUser(createdUser, request, response);
                } else {
                    throw new ServletException("Không thể tạo tài khoản Google");
                }
            } else {
                throw new ServletException("Không thể tạo tài khoản Google");
            }
        }
    }
    
    /**
     * Đăng nhập user vào session
     */
    private void loginUser(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        
        System.out.println("Google login successful for user: " + user.getUsername());
        
        // Cập nhật lastLogin
        UserDAO userDAO = new UserDAO();
        userDAO.updateLastLogin(user.getUserId());
        
        // Chuyển hướng theo role
        if ("admin".equals(user.getRole())) {
            response.sendRedirect("home.jsp");
        } else {
            response.sendRedirect("home.jsp");
        }
    }
    
    /**
     * Tạo username từ email
     */
    private String generateUsername(String email) {
        String username = email.substring(0, email.indexOf("@"));
        
        // Thêm số random nếu username đã tồn tại
        UserDAO userDAO = new UserDAO();
        String originalUsername = username;
        int counter = 1;
        
        while (userDAO.isUsernameExists(username)) {
            username = originalUsername + counter++;
        }
        
        return username;
    }
}
