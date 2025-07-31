package controller.client;

import dao.impl.CartDAO;
import dao.impl.ProductDAO;
import model.entity.User;
import model.entity.pCart.Cart;
import model.entity.pCart.CartItem;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MultipartConfig
public class CartControllerClient extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

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

            List<Integer> productIds = new ArrayList<>();
            for (CartItem item : cartItems) {
                productIds.add(item.getProductId());
            }
            Map<Integer, Integer> productQuantities = productDAO.getProductQuantitiesByIds(productIds);
            
            request.setAttribute("productQuantities", productQuantities);
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
        System.out.println("ban đang ở do post " + action);
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
            System.out.println("product id :" + productId + "quantity :" + quantity + "user ID :" + userId);
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);

            // Kiểm tra tồn kho
            int stockQuantity = productDAO.getProductStockQuantity(productId);

// Kiểm tra số lượng hiện có trong cart
            int currentQuantityInCart = cartDAO.getCurrentQuantityInCart(userId, productId);
            int totalRequestedQuantity = currentQuantityInCart + quantity;

            if (totalRequestedQuantity > stockQuantity) {
                int maxCanAdd = stockQuantity - currentQuantityInCart;
                if (maxCanAdd <= 0) {
                    response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm này đã đạt tối đa trong giỏ hàng!\"}");
                } else {
                    response.getWriter().write("{\"success\": false, \"message\": \"Chỉ có thể thêm tối đa " + maxCanAdd + " sản phẩm nữa (còn " + stockQuantity + " trong kho)!\"}");
                }
                return;
            }
            // THAY ĐỔI: Sử dụng phương thức addItemToCartByUserId 
            boolean success = cartDAO.addItemToCartByUserId(userId, productId, quantity);
            // Code ban đầu: boolean success = cartDAO.addItemToCart(userId, productId, quantity);

            // THAY ĐỔI: Thêm xử lý chuyển hướng đến trang giỏ hàng
            String redirectToCart = request.getParameter("redirectToCart");
            boolean shouldRedirect = "true".equals(redirectToCart);

            if (success) {
                int itemCount = cartDAO.getCartItemCount(cart.getCartId());

                // THAY ĐỔI: Thêm điều kiện chuyển hướng
                if (shouldRedirect) {
                    // Chuyển hướng đến trang giỏ hàng
                    response.sendRedirect("cartClient");
                } else {
                    // Trả về JSON như trước
                    response.getWriter().write("{\"success\": true, \"message\": \"Sản phẩm đã được thêm vào giỏ hàng!\", \"itemCount\": " + itemCount + "}");
                }
                // Code ban đầu: response.getWriter().write("{\"success\": true, \"message\": \"Sản phẩm đã được thêm vào giỏ hàng!\", \"itemCount\": " + itemCount + "}");
            } else {
                // THAY ĐỔI: Thêm điều kiện chuyển hướng khi có lỗi
                if (shouldRedirect) {
                    // Chuyển hướng với thông báo lỗi
                    response.sendRedirect("cartClient?error=true");
                } else {
                    response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra khi thêm sản phẩm!\"}");
                }
                // Code ban đầu: response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra khi thêm sản phẩm!\"}");
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
            jsonResponse = "{\"success\": true, \"message\": \"Cập nhật thành công!\", "
                    + "\"itemTotal\": " + itemTotal + ", "
                    + "\"cartTotal\": " + cartTotal + ", "
                    + "\"itemCount\": " + itemCount + "}";
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
            jsonResponse = "{\"success\": true, \"message\": \"Đã xóa sản phẩm!\", "
                    + "\"cartTotal\": " + cartTotal + ", "
                    + "\"itemCount\": " + itemCount + "}";
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