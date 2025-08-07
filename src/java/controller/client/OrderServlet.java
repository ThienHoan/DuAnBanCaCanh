package controller.client;

import dao.impl.OrderDAOImpl;
import dao.impl.AddressDAO;
import dao.impl.ProductDAO;
import dao.impl.CartDAO;
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
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;

/**
 * Servlet for managing orders for both admin and customers
 * Includes: order listing, order details, order cancellation, shipping confirmation, and inventory management
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private OrderDAOImpl orderDAO;
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private static final int ORDERS_PER_PAGE = 10;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        productDAO = new ProductDAO();
        cartDAO = new CartDAO();
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
            case "reorder":
                reorderItems(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    /**
     * Display list of orders with pagination and filtering
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            String status = request.getParameter("status");
            int page = 1;
            
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                }
            }
            
            List<Order> allOrders = orderDAO.searchOrders(keyword, status, user.getUserId(), user.getRole());
            
            int totalOrders = allOrders.size();
            int totalPages = (int) Math.ceil((double) totalOrders / ORDERS_PER_PAGE);
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            int start = (page - 1) * ORDERS_PER_PAGE;
            int end = Math.min(start + ORDERS_PER_PAGE, totalOrders);
            
            List<Order> pagedOrders = allOrders.subList(start, end);
            
            Map<Integer, Boolean> refundedPayments = new HashMap<>();
            for (Order order : pagedOrders) {
                if ("e-wallet".equals(order.getPaymentMethod()) && "cancelled".equals(order.getStatus())) {
                    refundedPayments.put(order.getOrderId(), orderDAO.isPaymentRefunded(order.getOrderId()));
                }
            }
            
            request.setAttribute("orders", pagedOrders);
            request.setAttribute("refundedPayments", refundedPayments);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching order list: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while loading the order list: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * View detailed information of a specific order
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
            
            if (!"admin".equals(user.getRole()) && order.getUserId() != user.getUserId()) {
                response.sendRedirect("order?error=You don't have permission to view this order");
                return;
            }
            
            List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
            order.setOrderItems(orderItems);
            
            AddressDAO addressDAO = new AddressDAO();
            Address shippingAddress = null;
            Address billingAddress = null;
            
            if (order.getShippingAddressId() > 0) {
                shippingAddress = addressDAO.getAddressById(order.getShippingAddressId());
            }
            
            if (order.getBillingAddressId() > 0) {
                billingAddress = addressDAO.getAddressById(order.getBillingAddressId());
            }
            
            boolean isPaymentRefunded = false;
            if ("e-wallet".equals(order.getPaymentMethod()) && "cancelled".equals(order.getStatus())) {
                isPaymentRefunded = orderDAO.isPaymentRefunded(orderId);
            }
            
            request.setAttribute("order", order);
            request.setAttribute("shippingAddress", shippingAddress);
            request.setAttribute("billingAddress", billingAddress);
            request.setAttribute("isPaymentRefunded", isPaymentRefunded);
            
            request.getRequestDispatcher("/order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error viewing order details: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error viewing order details: " + e.getMessage());
        }
    }

    /**
     * Cancel an order and restore inventory
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
            
            if (!"admin".equals(user.getRole()) && order.getUserId() != user.getUserId()) {
                response.sendRedirect("order?error=You don't have permission to cancel this order");
                return;
            }
            
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
     * Confirm order shipment (admin only)
     * Updates order status and handles inventory based on payment method
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
            
            if (!"confirmed".equals(order.getStatus()) && !"processing".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot mark as shipped for order in current status");
                return;
            }
            
            if ("shipping".equals(order.getStatus())) {
                response.sendRedirect("order?error=Order is already marked as shipped");
                return;
            }
            
            String stockError = orderDAO.validateOrderStockForShipping(orderId);
            if (stockError != null) {
                response.sendRedirect("order?error=" + stockError);
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(orderId, "shipping");
            
            if (success) {
                
                response.sendRedirect("order?message=Order marked as shipped successfully");
            } else {
                response.sendRedirect("order?error=Failed to mark order as shipped");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error confirming shipment: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error confirming shipment: " + e.getMessage());
        }
    }
    
  
    private void confirmReceived(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect("order?error=Order not found");
                return;
            }
            
            if (order.getUserId() != user.getUserId()) {
                response.sendRedirect("order?error=You don't have permission to confirm this order");
                return;
            }
            
            if (!"shipping".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot confirm receipt for order in current status");
                return;
            }
            
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
     * Confirm payment for COD orders (admin only)
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
            
            if (!"cod".equals(order.getPaymentMethod())) {
                response.sendRedirect("order?error=This action is only for COD orders");
                return;
            }
            
            if ("paid".equals(order.getPaymentStatus())) {
                response.sendRedirect("order?error=Order is already paid");
                return;
            }
            
            if (!"shipping".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot confirm payment for order in current status");
                return;
            }
            
            LOGGER.log(java.util.logging.Level.INFO, "Confirming payment for order: " + orderId);
            
            boolean success = orderDAO.updatePaymentStatus(orderId, "paid");
            
            if (success) {
                try {
                    boolean paymentUpdateSuccess = orderDAO.updatePaymentStatusWithTransactionId(orderId, "completed", null);
                    if (paymentUpdateSuccess) {
                        LOGGER.log(java.util.logging.Level.INFO, "Payment status updated in Payments table successfully");
                    } else {
                        LOGGER.log(java.util.logging.Level.WARNING, "Could not update Payments table");
                    }
                    
                    LOGGER.log(java.util.logging.Level.INFO, "Payment confirmed for order #" + orderId + ". Inventory was already updated when order was created.");
                    
                    response.sendRedirect("order?message=Payment confirmed successfully");
                } catch (Exception e) {
                    LOGGER.log(java.util.logging.Level.WARNING, "Could not update Payments table, but Orders was updated: " + e.getMessage(), e);
                    response.sendRedirect("order?message=Payment confirmed successfully but payment record not updated");
                }
            } else {
                response.sendRedirect("order?error=Failed to confirm payment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error confirming payment: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error confirming payment: " + e.getMessage());
        }
    }
    
    /**
     * Confirm order (admin only)
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
            
            if (!"pending".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot confirm order in current status");
                return;
            }
            
            String stockError = orderDAO.validateOrderStockForShipping(orderId);
            if (stockError != null) {
                response.sendRedirect("order?error=" + stockError);
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
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error confirming order: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error confirming order: " + e.getMessage());
        }
    }



    /**
     * Mark VNPay cancelled order as refunded (admin only)
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
            
            if (!"e-wallet".equals(order.getPaymentMethod())) {
                response.sendRedirect("order?error=This action is only for VNPay orders");
                return;
            }
            
            if (!"cancelled".equals(order.getStatus())) {
                response.sendRedirect("order?error=Cannot mark as refunded for order in current status");
                return;
            }
            
            LOGGER.log(java.util.logging.Level.INFO, "Marking order as refunded: {0}", orderId);
            
            try {
                boolean success = orderDAO.markPaymentRefunded(orderId);
                
                if (success) {
                    LOGGER.log(java.util.logging.Level.INFO, "Marked refund successfully for order: {0}", orderId);
                    response.sendRedirect("order?message=Order marked as refunded successfully");
                } else {
                    LOGGER.log(java.util.logging.Level.WARNING, "Could not mark order as refunded: {0}", orderId);
                    response.sendRedirect("order?error=Failed to mark order as refunded");
                }
            } catch (Exception e) {
                LOGGER.log(java.util.logging.Level.SEVERE, "Error marking refund: " + e.getMessage(), e);
                response.sendRedirect("order?error=Error marking order as refunded: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        }
    }

    /**
     * Reorder items from a previous order
     * Adds all items from the specified order to the user's cart
     */
    private void reorderItems(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                session.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn hàng.");
                response.sendRedirect("order");
                return;
            }
            

            if (!"admin".equals(user.getRole()) && order.getUserId() != user.getUserId()) {
                session.setAttribute("ERROR_MESSAGE", "Bạn không có quyền truy cập đơn hàng này.");
                response.sendRedirect("order");
                return;
            }
            
        
            List<OrderItem> orderItems = orderDAO.getOrderItemsById(orderId);
            
            if (orderItems.isEmpty()) {
                session.setAttribute("ERROR_MESSAGE", "Đơn hàng không có sản phẩm nào.");
                response.sendRedirect("order?action=detail&id=" + orderId);
                return;
            }
            
       
            int addedCount = 0;
            int skippedCount = 0;
            
            for (OrderItem item : orderItems) {
                try {
            
                    int stockQuantity = productDAO.getProductStockQuantity(item.getProductId());
                    
                    if (stockQuantity > 0) {
                   
                        int quantityToAdd = Math.min(item.getQuantity(), stockQuantity);
                        boolean success = cartDAO.addItemToCartByUserId(user.getUserId(), item.getProductId(), quantityToAdd);
                        
                        if (success) {
                            addedCount++;
                        } else {
                            skippedCount++;
                        }
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Lỗi khi thêm sản phẩm " + item.getProductId() + " vào giỏ hàng: " + e.getMessage());
                    skippedCount++;
                }
            }
            
 
            response.sendRedirect("cartClient");
            
        } catch (NumberFormatException e) {
            session.setAttribute("ERROR_MESSAGE", "ID đơn hàng không hợp lệ.");
            response.sendRedirect("order");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi mua lại đơn hàng: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi mua lại đơn hàng: " + e.getMessage());
            response.sendRedirect("order");
        }
    }
} 