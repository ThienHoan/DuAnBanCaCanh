package controller.client;

import dao.impl.CartDAO;
import model.entity.User;
import model.entity.pCart.Cart;
import model.entity.pCart.CartItem;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@MultipartConfig
public class CartControllerClient extends HttpServlet {
    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("bạn vào do get");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int userId = user.getUserId();
        

        try {
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            double cartTotal = cartDAO.getCartTotal(cart.getCartId());
            int itemCount = cartDAO.getCartItemCount(cart.getCartId());

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", cartTotal);
            request.setAttribute("itemCount", itemCount);
            request.setAttribute("cartId", cart.getCartId());

            request.getRequestDispatcher("cart.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"Bạn chưa đăng nhập!\"}");
            return;
        }
        int userId = user.getUserId();
        System.out.println("ban đang ở do post "+ action);
        try {
            // Xử lý đúng từng action, chỉ trả về JSON cho action "add"
            if ("add".equals(action)) {
                System.out.println("bạn đang đã action = add");
                handleAddToCart(request, response, userId);
            } else if ("update".equals(action)) {
                handleUpdateQuantity(request, response, userId);
            } else if ("remove".equals(action)) {
                handleRemoveItem(request, response, userId);  
            } else if ("clear".equals(action)) {
                handleClearCart(request, response, userId);
            } else {
                // Nếu không phải AJAX action thì forward lại cart.jsp (hoặc có thể trả về lỗi)
                doGet(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"Database error!\"}");
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println("da vao ham handle add to cart");
        try {
            System.out.println("vao try catch");
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            System.out.println("product id :"+ productId+"quantity :" +quantity +"user ID :" +userId );
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            System.out.println("Cart created/retrieved with ID: " + cart.getCartId());
            boolean success = cartDAO.addItemToCart(cart.getCartId(), productId, quantity);

            if (success) {
                int itemCount = cartDAO.getCartItemCount(cart.getCartId());
                response.getWriter().write("{\"success\": true, \"message\": \"Sản phẩm đã được thêm vào giỏ hàng!\", \"itemCount\": " + itemCount + "}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra khi thêm sản phẩm!\"}");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi server: " + ex.getMessage() + "\"}");
        }
    }

    private void handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response, int userId)
        throws IOException, SQLException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    boolean success = cartDAO.updateCartItemQuantity(cartItemId, quantity);
    
    Cart cart = cartDAO.getOrCreateCartByUserId(userId);
    double itemTotal = 0;
    
    // Lấy tổng tiền của item vừa cập nhật
    for (CartItem item : cartDAO.getCartItemsByCartId(cart.getCartId())) {
        if (item.getCartItemId() == cartItemId) {
            itemTotal = item.getTotalPrice();
            break;
        }
    }
    
    double cartTotal = cartDAO.getCartTotal(cart.getCartId());
    int itemCount = cartDAO.getCartItemCount(cart.getCartId());
    
    String jsonResponse;
    if (success) {
        jsonResponse = "{\"success\": true, \"message\": \"Cập nhật thành công!\", " +
                      "\"itemTotal\": " + itemTotal + ", " +
                      "\"cartTotal\": " + cartTotal + ", " +
                      "\"itemCount\": " + itemCount + "}";
    } else {
        jsonResponse = "{\"success\": false, \"message\": \"Có lỗi xảy ra khi cập nhật!\"}";
    }
    
    response.getWriter().write(jsonResponse);
}
    private void handleRemoveItem(HttpServletRequest request, HttpServletResponse response, int userId)
        throws IOException, SQLException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
    boolean success = cartDAO.removeCartItem(cartItemId);
    
    Cart cart = cartDAO.getOrCreateCartByUserId(userId);
    double cartTotal = cartDAO.getCartTotal(cart.getCartId());
    int itemCount = cartDAO.getCartItemCount(cart.getCartId());
    
    String jsonResponse;
    if (success) {
        jsonResponse = "{\"success\": true, \"message\": \"Đã xóa sản phẩm!\", " +
                      "\"cartTotal\": " + cartTotal + ", " +
                      "\"itemCount\": " + itemCount + "}";
    } else {
        jsonResponse = "{\"success\": false, \"message\": \"Có lỗi xảy ra khi xóa sản phẩm!\"}";
    }
    
    response.getWriter().write(jsonResponse);
}
   private void handleClearCart(HttpServletRequest request, HttpServletResponse response, int userId)
        throws IOException, SQLException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    Cart cart = cartDAO.getOrCreateCartByUserId(userId);
    boolean success = cartDAO.clearCart(cart.getCartId());
    
    String jsonResponse;
    if (success) {
        jsonResponse = "{\"success\": true, \"message\": \"Đã xóa tất cả sản phẩm!\"}";
    } else {
        jsonResponse = "{\"success\": false, \"message\": \"Có lỗi xảy ra khi xóa giỏ hàng!\"}";
    }
    
    response.getWriter().write(jsonResponse);
}
}