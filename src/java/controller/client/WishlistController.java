package controller.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.entity.User;
import model.entity.Wishlist;
import service.interfaces.WishlistService;
import service.impl.WishlistServiceImpl;
import dao.interfaces.WishlistDAO.WishlistStatistics;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller xử lý các thao tác với Wishlist
 */
public class WishlistController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(WishlistController.class.getName());
    private WishlistService wishlistService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        this.wishlistService = new WishlistServiceImpl();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?returnUrl=" + 
                                request.getRequestURL().toString());
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    handleListWishlist(request, response, currentUser.getUserId());
                    break;
                case "check":
                    handleCheckWishlist(request, response, currentUser.getUserId());
                    break;
                case "count":
                    handleGetWishlistCount(request, response, currentUser.getUserId());
                    break;
                case "popular":
                    handleGetPopularProducts(request, response);
                    break;
                default:
                    handleListWishlist(request, response, currentUser.getUserId());
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Error in WishlistController GET: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            sendJsonResponse(response, false, "Vui lòng đăng nhập để sử dụng chức năng này");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
                case "add":
                    handleAddToWishlist(request, response, currentUser.getUserId());
                    break;
                case "remove":
                    handleRemoveFromWishlist(request, response, currentUser.getUserId());
                    break;
                case "toggle":
                    handleToggleWishlist(request, response, currentUser.getUserId());
                    break;
                case "clear":
                    handleClearWishlist(request, response, currentUser.getUserId());
                    break;
                default:
                    sendJsonResponse(response, false, "Hành động không hợp lệ");
                    break;
            }
        } catch (Exception e) {
            LOGGER.severe("Error in WishlistController POST: " + e.getMessage());
            sendJsonResponse(response, false, "Đã xảy ra lỗi: " + e.getMessage());
        }
    }
    
    private void handleListWishlist(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        List<Wishlist> wishlist = wishlistService.getUserWishlist(userId);
        int wishlistCount = wishlistService.getWishlistCount(userId);
        
        request.setAttribute("wishlist", wishlist);
        request.setAttribute("wishlistCount", wishlistCount);
        
        // Nếu là AJAX request thì trả về JSON
        String ajaxRequest = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajaxRequest)) {
            Map<String, Object> data = new HashMap<>();
            data.put("wishlist", wishlist);
            data.put("count", wishlistCount);
            
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(data));
            return;
        }
        
        request.getRequestDispatcher("/client/wishlist.jsp").forward(request, response);
    }
    
    private void handleCheckWishlist(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            boolean inWishlist = wishlistService.isInWishlist(userId, productId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("inWishlist", inWishlist);
            data.put("productId", productId);
            
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(data));
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID sản phẩm không hợp lệ");
        }
    }
    
    private void handleGetWishlistCount(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        int count = wishlistService.getWishlistCount(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(data));
    }
    
    private void handleGetPopularProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int limit = 10;
        try {
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (NumberFormatException e) {
            // Use default limit
        }
        
        List<WishlistStatistics> popularProducts = wishlistService.getMostWishedProducts(limit);
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(popularProducts));
    }
    
    private void handleAddToWishlist(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            boolean success = wishlistService.addToWishlist(userId, productId);
            int newCount = wishlistService.getWishlistCount(userId);
            
            if (success) {
                sendJsonResponse(response, true, "Đã thêm vào danh sách yêu thích", newCount);
            } else {
                sendJsonResponse(response, false, "Không thể thêm vào danh sách yêu thích");
            }
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID sản phẩm không hợp lệ");
        }
    }
    
    private void handleRemoveFromWishlist(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        try {
            // Có thể xóa bằng productId hoặc wishlistId
            String productIdStr = request.getParameter("productId");
            String wishlistIdStr = request.getParameter("wishlistId");
            
            boolean success = false;
            
            if (wishlistIdStr != null && !wishlistIdStr.isEmpty()) {
                int wishlistId = Integer.parseInt(wishlistIdStr);
                success = wishlistService.removeWishlistItem(wishlistId, userId);
            } else if (productIdStr != null && !productIdStr.isEmpty()) {
                int productId = Integer.parseInt(productIdStr);
                success = wishlistService.removeFromWishlist(userId, productId);
            }
            
            int newCount = wishlistService.getWishlistCount(userId);
            
            if (success) {
                sendJsonResponse(response, true, "Đã xóa khỏi danh sách yêu thích", newCount);
            } else {
                sendJsonResponse(response, false, "Không thể xóa khỏi danh sách yêu thích");
            }
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID không hợp lệ");
        }
    }
    
    private void handleToggleWishlist(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            boolean isAdded = wishlistService.toggleWishlist(userId, productId);
            int newCount = wishlistService.getWishlistCount(userId);
            
            String message = isAdded ? "Đã thêm vào danh sách yêu thích" : "Đã xóa khỏi danh sách yêu thích";
            sendJsonResponse(response, true, message, newCount, isAdded);
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID sản phẩm không hợp lệ");
        }
    }
    
    private void handleClearWishlist(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        
        boolean success = wishlistService.clearWishlist(userId);
        
        if (success) {
            sendJsonResponse(response, true, "Đã xóa tất cả sản phẩm khỏi danh sách yêu thích", 0);
        } else {
            sendJsonResponse(response, false, "Không thể xóa danh sách yêu thích");
        }
    }
    
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) 
            throws IOException {
        sendJsonResponse(response, success, message, null, null);
    }
    
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, Integer count) 
            throws IOException {
        sendJsonResponse(response, success, message, count, null);
    }
    
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, 
                                Integer count, Boolean isAdded) throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        
        if (count != null) {
            jsonResponse.put("count", count);
        }
        
        if (isAdded != null) {
            jsonResponse.put("isAdded", isAdded);
        }
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(jsonResponse));
    }
}
