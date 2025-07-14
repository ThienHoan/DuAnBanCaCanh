package controller.client;

import dao.impl.OrderDAOImpl;
import dao.impl.AddressDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import model.entity.Address;
import model.entity.pOrder.Order;
import model.entity.pOrder.OrderItem;
import model.entity.pOrder.Payment;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;

/**
 * Servlet để quản lý đơn hàng cho cả admin và khách hàng
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private OrderDAOImpl orderDAO;
    private static final int ORDERS_PER_PAGE = 10;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listOrders(request, response, user);
                break;
            case "detail":
                viewOrderDetail(request, response, user);
                break;
            default:
                listOrders(request, response, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        switch (action) {
            case "cancel":
                cancelOrder(request, response, user);
                break;
            case "confirm-received":
                confirmReceived(request, response, user);
                break;
            case "confirm-shipped":
                confirmShipped(request, response, user);
                break;
            case "confirm-order":
                confirmOrder(request, response, user);
                break;
            case "confirm-payment":
                confirmPayment(request, response, user);
                break;
            case "mark-refunded":
                markRefunded(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    /**
     * Hiển thị danh sách đơn hàng
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            // Lấy tham số từ request
            String keyword = request.getParameter("keyword");
            String status = request.getParameter("status");
            int page = 1;
            
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    // Nếu không phải số, giữ nguyên page = 1
                }
            }
            
            // Lấy danh sách đơn hàng theo tiêu chí
            List<Order> allOrders = orderDAO.searchOrders(keyword, status, user.getUserId(), user.getRole());
            
            // Phân trang
            int totalOrders = allOrders.size();
            int totalPages = (int) Math.ceil((double) totalOrders / ORDERS_PER_PAGE);
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            int start = (page - 1) * ORDERS_PER_PAGE;
            int end = Math.min(start + ORDERS_PER_PAGE, totalOrders);
            
            List<Order> pagedOrders = allOrders.subList(start, end);
            
            // Kiểm tra trạng thái hoàn tiền cho từng đơn hàng
            Map<Integer, Boolean> refundedPayments = new HashMap<>();
            for (Order order : pagedOrders) {
                if ("e-wallet".equals(order.getPaymentMethod()) && "cancelled".equals(order.getStatus())) {
                    refundedPayments.put(order.getOrderId(), orderDAO.isPaymentRefunded(order.getOrderId()));
                }
            }
            
            // Set các thuộc tính cho request
            request.setAttribute("orders", pagedOrders);
            request.setAttribute("refundedPayments", refundedPayments);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            
            // Forward đến trang danh sách đơn hàng
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Xem chi tiết đơn hàng
     */
    private void viewOrderDetail(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra quyền truy cập
            if (!"admin".equals(user.getRole()) && order.getUserId() != user.getUserId()) {
                response.sendRedirect("order?error=You don't have permission to view this order");
                return;
            }
            
            // Lấy danh sách sản phẩm trong đơn hàng với thông tin chi tiết
            List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
            order.setOrderItems(orderItems);
            
            // Lấy thông tin địa chỉ
            AddressDAO addressDAO = new AddressDAO();
            Address shippingAddress = null;
            Address billingAddress = null;
            
            if (order.getShippingAddressId() > 0) {
                shippingAddress = addressDAO.getAddressById(order.getShippingAddressId());
            }
            
            if (order.getBillingAddressId() > 0) {
                billingAddress = addressDAO.getAddressById(order.getBillingAddressId());
            }
            
            // Kiểm tra trạng thái hoàn tiền
            boolean isPaymentRefunded = false;
            if ("e-wallet".equals(order.getPaymentMethod()) && "cancelled".equals(order.getStatus())) {
                isPaymentRefunded = orderDAO.isPaymentRefunded(orderId);
            }
            
            // Set thuộc tính cho request
            request.setAttribute("order", order);
            request.setAttribute("shippingAddress", shippingAddress);
            request.setAttribute("billingAddress", billingAddress);
            request.setAttribute("isPaymentRefunded", isPaymentRefunded);
            
            // Forward đến trang chi tiết đơn hàng
            request.getRequestDispatcher("/order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xem chi tiết đơn hàng: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error viewing order details: " + e.getMessage());
        }
    }

    /**
     * Hủy đơn hàng
     */
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra quyền truy cập
            if (!"admin".equals(user.getRole()) && order.getUserId() != user.getUserId()) {
                response.sendRedirect("order?error=You don't have permission to cancel this order");
                return;
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (!"pending".equals(order.getStatus()) && !"confirmed".equals(order.getStatus()) && !"processing".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot cancel order in current status");
                return;
            }
            
            boolean success = orderDAO.cancelOrder(orderId);
            
            if (success) {
                response.sendRedirect("order?message=Order cancelled successfully");
            } else {
                response.sendRedirect("order?error=Failed to cancel order");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        }
    }

    /**
     * Xác nhận đã giao hàng (admin)
     */
    private void confirmShipped(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect("order?error=You don't have permission to perform this action");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (!"confirmed".equals(order.getStatus()) && !"processing".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot mark as shipped for order in current status");
                return;
            }
            
            // Không cần kiểm tra trạng thái thanh toán trước khi giao hàng
            // Đơn hàng COD sẽ được thanh toán khi giao hàng
            
            boolean success = orderDAO.updateOrderStatus(orderId, "shipping");
            
            if (success) {
                response.sendRedirect("order?message=Order marked as shipped successfully");
            } else {
                response.sendRedirect("order?error=Failed to mark order as shipped");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        }
    }

    /**
     * Xác nhận đã nhận hàng (khách hàng)
     */
    private void confirmReceived(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra quyền truy cập
            if (order.getUserId() != user.getUserId()) {
                response.sendRedirect("order?error=You don't have permission to confirm this order");
                return;
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (!"shipping".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot confirm receipt for order in current status");
                return;
            }
            
            // Đối với đơn hàng COD, chỉ có thể xác nhận đã nhận hàng sau khi admin xác nhận thanh toán
            if ("cod".equals(order.getPaymentMethod()) && "pending".equals(order.getPaymentStatus())) {
                response.sendRedirect("order?error=Cannot confirm receipt until payment is confirmed by admin");
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(orderId, "delivered");
            
            if (success) {
                response.sendRedirect("order?message=Order confirmed received successfully");
            } else {
                response.sendRedirect("order?error=Failed to confirm receipt");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        }
    }
    
    /**
     * Xác nhận thanh toán cho đơn hàng COD (admin)
     */
    private void confirmPayment(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect("order?error=You don't have permission to perform this action");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra phương thức thanh toán
            if (!"cod".equals(order.getPaymentMethod())) {
                response.sendRedirect("order?error=This action is only for COD orders");
                return;
            }
            
            // Kiểm tra trạng thái thanh toán
            if ("paid".equals(order.getPaymentStatus())) {
                response.sendRedirect("order?error=Order is already paid");
                return;
            }
            
            // Kiểm tra trạng thái đơn hàng - chỉ có thể xác nhận thanh toán khi đang giao hàng
            if (!"shipping".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot confirm payment for order in current status");
                return;
            }
            
            // Thêm log trước khi thực hiện cập nhật
            LOGGER.log(java.util.logging.Level.INFO, "Đang xác nhận thanh toán cho đơn hàng: " + orderId);
            
            // Cập nhật trạng thái thanh toán trong bảng Orders
            boolean success = orderDAO.updatePaymentStatus(orderId, "paid");
            
            if (success) {
                try {
                    // Cập nhật trạng thái thanh toán trong bảng Payments
                    String updatePaymentSql = "UPDATE Payments SET status = 'completed' WHERE order_id = ?";
                    try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                         java.sql.PreparedStatement ps = conn.prepareStatement(updatePaymentSql)) {
                        ps.setInt(1, orderId);
                        int rows = ps.executeUpdate();
                        LOGGER.log(java.util.logging.Level.INFO, "Cập nhật trạng thái thanh toán trong bảng Payments: " + rows + " hàng bị ảnh hưởng");
                    }
                    
                    response.sendRedirect("order?message=Payment confirmed successfully");
                } catch (Exception e) {
                    LOGGER.log(java.util.logging.Level.WARNING, "Không thể cập nhật bảng Payments, nhưng Orders đã được cập nhật: " + e.getMessage(), e);
                    response.sendRedirect("order?message=Payment confirmed successfully but payment record not updated");
                }
            } else {
                response.sendRedirect("order?error=Failed to confirm payment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi xác nhận thanh toán: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error confirming payment: " + e.getMessage());
        }
    }
    
    /**
     * Xác nhận đơn hàng (admin)
     */
    private void confirmOrder(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect("order?error=You don't have permission to perform this action");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (!"pending".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot confirm order in current status");
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(orderId, "confirmed");
            
            if (success) {
                response.sendRedirect("order?message=Order confirmed successfully");
            } else {
                response.sendRedirect("order?error=Failed to confirm order");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        }
    }

    /**
     * Đánh dấu đã hoàn tiền cho đơn hàng VNPay đã hủy
     */
    private void markRefunded(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect("order?error=You don't have permission to perform this action");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            // Kiểm tra phương thức thanh toán
            if (!"e-wallet".equals(order.getPaymentMethod())) {
                response.sendRedirect("order?error=This action is only for VNPay orders");
                return;
            }
            
            // Kiểm tra trạng thái đơn hàng - chỉ có thể đánh dấu hoàn tiền khi đơn hàng đã hủy
            if (!"cancelled".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot mark as refunded for order in current status");
                return;
            }
            
            // Thêm log trước khi gọi markPaymentRefunded
            LOGGER.log(java.util.logging.Level.INFO, "Đang đánh dấu đơn hàng đã hoàn tiền: " + orderId);
            
            // Thử thực hiện cập nhật trạng thái thanh toán
            try {
                boolean success = orderDAO.markPaymentRefunded(orderId);
                
                if (success) {
                    LOGGER.log(java.util.logging.Level.INFO, "Đánh dấu hoàn tiền thành công cho đơn hàng: " + orderId);
                    response.sendRedirect("order?message=Order marked as refunded successfully");
                } else {
                    LOGGER.log(java.util.logging.Level.WARNING, "Không thể đánh dấu hoàn tiền cho đơn hàng: " + orderId);
                    response.sendRedirect("order?error=Failed to mark order as refunded");
                }
            } catch (Exception e) {
                LOGGER.log(java.util.logging.Level.SEVERE, "Lỗi khi đánh dấu hoàn tiền: " + e.getMessage(), e);
                response.sendRedirect("order?error=Error marking order as refunded: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        }
    }
} 