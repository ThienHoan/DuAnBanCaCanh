/* VNPayReturnServlet.java - Modified to create order on success */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.CartDAO;
import dao.OrderDAO;
import model.Cart;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(VNPayReturnServlet.class.getName());
    private CartDAO cartDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        orderDAO = new OrderDAO();
        LOGGER.info("VNPayReturnServlet initialized successfully");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String orderNumber = request.getParameter("vnp_TxnRef");

        try {
            Map<String, Object> pendingOrder = (Map<String, Object>) session.getAttribute("PENDING_ORDER");
            if (pendingOrder == null || !orderNumber.equals(pendingOrder.get("orderNumber"))) {
                LOGGER.warning("Invalid or missing pending order data for orderNumber: " + orderNumber);
                session.setAttribute("ERROR_MESSAGE", "Dữ liệu đơn hàng không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            if ("00".equals(vnp_ResponseCode)) {
                // Payment successful, create order
                int userId = (Integer) pendingOrder.get("userId");
                Cart cart = (Cart) pendingOrder.get("cart");
                int addressId = (Integer) pendingOrder.get("addressId");
                String paymentMethod = (String) pendingOrder.get("paymentMethod");
                String notes = (String) pendingOrder.get("notes");
                double discountAmount = (Double) pendingOrder.get("discountAmount");

                // Validate cart stock again to prevent race conditions
                String stockError = cartDAO.validateCartStock(cart);
                if (stockError != null) {
                    session.setAttribute("ERROR_MESSAGE", stockError);
                    response.sendRedirect(request.getContextPath() + "/checkout");
                    return;
                }

                int orderId = orderDAO.createOrder(userId, cart, orderNumber, addressId, paymentMethod, notes, discountAmount);
                cartDAO.updatePaymentStatus(orderId, "PAID");
                if (cart != null) {
                    cartDAO.clearCart(cart.getCartId());
                    session.removeAttribute("CART");
                    session.removeAttribute("COUPON");
                    session.removeAttribute("PENDING_ORDER");
                }
                response.sendRedirect(request.getContextPath() + "/order-confirmation.jsp?orderId=" + orderId);
            } else {
                // Payment failed, clear pending order
                session.removeAttribute("PENDING_ORDER");
                String errorMessage = vnp_ResponseCode.equals("24") 
                    ? "Bạn đã hủy thanh toán. Vui lòng thử lại."
                    : "Thanh toán thất bại. Mã lỗi: " + vnp_ResponseCode;
                session.setAttribute("ERROR_MESSAGE", errorMessage);
                response.sendRedirect(request.getContextPath() + "/checkout");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "VNPayReturnServlet error: " + ex.getMessage(), ex);
            session.setAttribute("ERROR_MESSAGE", "Lỗi xử lý thanh toán: " + ex.getMessage());
            session.removeAttribute("PENDING_ORDER");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
