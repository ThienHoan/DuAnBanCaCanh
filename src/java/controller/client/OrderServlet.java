package controller.client;

import dao.impl.OrderDAOImpl;
import dao.impl.AddressDAO;
import dao.impl.InventoryLogDAOImpl;
import dao.impl.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.User;
import model.entity.Address;
import model.entity.Product;
import model.entity.InventoryLog;
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
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Servlet for managing orders for both admin and customers
 * Includes: order listing, order details, order cancellation, shipping confirmation, and inventory management
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private OrderDAOImpl orderDAO;
    private ProductDAO productDAO;
    private static final int ORDERS_PER_PAGE = 10;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        productDAO = new ProductDAO();
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
            case "updateStatus":
                updateOrderStatus(request, response, user);
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
                if ("cod".equalsIgnoreCase(order.getPaymentMethod())) {
                    addInventoryLogsForShipping(orderId);
                } else {
                    LOGGER.info("Order VNPay was already deducted when paid, only log shipping confirmation for orderId=" + orderId);
                }
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
    
    /**
     * Add inventory logs and update inventory when order is confirmed for shipping
     * Optimized with batch processing and async execution for COD orders
     */
    private void addInventoryLogsForShipping(int orderId) {
        new Thread(() -> {
            try {
                LOGGER.log(Level.INFO, "Starting inventory processing for order #" + orderId);
                
                List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
                if (orderItems == null || orderItems.isEmpty()) {
                    LOGGER.log(Level.WARNING, "No products found for order #" + orderId);
                    return;
                }
                
                List<Integer> productIds = orderItems.stream()
                    .map(OrderItem::getProductId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
                
                Map<Integer, Product> productsMap = getProductsByIds(productIds);
                
                Set<Integer> existingProductIds = getExistingInventoryLogProductIds(orderId, "order_shipped");
                
                List<InventoryLog> logsToInsert = new ArrayList<>();
                List<Object[]> inventoryUpdates = new ArrayList<>();
                
                for (OrderItem item : orderItems) {
                    Product product = productsMap.get(item.getProductId());
                    if (product != null && !existingProductIds.contains(item.getProductId())) {
                        if (product.getQuantity() >= item.getQuantity()) {
                            int oldQuantity = product.getQuantity();
                            int newQuantity = oldQuantity - item.getQuantity();
                            
                            inventoryUpdates.add(new Object[]{
                                newQuantity,
                                item.getProductId(),
                                item.getQuantity()
                            });
                            
                            InventoryLog inventoryLog = new InventoryLog(
                                item.getProductId(),
                                oldQuantity,
                                newQuantity,
                                "decrease",
                                "Order #" + orderId + " - Shipping confirmed: " + item.getProductName(),
                                orderId,
                                "order_shipped"
                            );
                            logsToInsert.add(inventoryLog);
                            
                            LOGGER.log(Level.INFO, "Preparing inventory update for product " + item.getProductId() + 
                                     ": " + oldQuantity + " -> " + newQuantity);
                        } else {
                            LOGGER.log(Level.WARNING, "Not enough stock for product " + item.getProductId() + 
                                     " (required: " + item.getQuantity() + ", available: " + product.getQuantity() + ")");
                        }
                    }
                }
                
                if (!inventoryUpdates.isEmpty()) {
                    boolean inventorySuccess = productDAO.batchUpdateInventory(inventoryUpdates);
                    if (!inventorySuccess) {
                        LOGGER.log(Level.SEVERE, "Could not update inventory for order #" + orderId);
                        return;
                    }
                }
                
                if (!logsToInsert.isEmpty()) {
                    boolean batchSuccess = batchInsertInventoryLogs(logsToInsert);
                    if (batchSuccess) {
                        LOGGER.log(Level.INFO, "Inventory updated and " + logsToInsert.size() + " inventory logs added for order #" + orderId);
                    } else {
                        LOGGER.log(Level.WARNING, "Could not add inventory logs for order #" + orderId);
                    }
                } else {
                    LOGGER.log(Level.INFO, "No inventory update needed for order #" + orderId + " (logs already exist)");
                }
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing inventory for order #" + orderId + ": " + e.getMessage(), e);
            }
        }).start();
    }
    
    /**
     * Add inventory logs and update inventory when admin confirms order
     * Optimized with batch processing and async execution
     */
    private void addInventoryLogsForOrderConfirmation(int orderId) {
        new Thread(() -> {
            try {
                LOGGER.log(Level.INFO, "Starting inventory processing for order #" + orderId + " when confirmed");
                
                List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
                if (orderItems == null || orderItems.isEmpty()) {
                    LOGGER.log(Level.WARNING, "No products found for order #" + orderId);
                    return;
                }
                
                List<Integer> productIds = orderItems.stream()
                    .map(OrderItem::getProductId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
                
                Map<Integer, Product> productsMap = getProductsByIds(productIds);
                
                Set<Integer> existingProductIds = getExistingInventoryLogProductIds(orderId, "order_confirmed");
                
                List<InventoryLog> logsToInsert = new ArrayList<>();
                List<Object[]> inventoryUpdates = new ArrayList<>();
                
                for (OrderItem item : orderItems) {
                    Product product = productsMap.get(item.getProductId());
                    if (product != null && !existingProductIds.contains(item.getProductId())) {
                        if (product.getQuantity() >= item.getQuantity()) {
                            int oldQuantity = product.getQuantity();
                            int newQuantity = oldQuantity - item.getQuantity();
                            
                            inventoryUpdates.add(new Object[]{
                                newQuantity,
                                item.getProductId(),
                                item.getQuantity()
                            });
                            
                            InventoryLog inventoryLog = new InventoryLog(
                                item.getProductId(),
                                oldQuantity,
                                newQuantity,
                                "decrease",
                                "Order #" + orderId + " - Admin confirmed order: " + item.getProductName(),
                                orderId,
                                "order_confirmed"
                            );
                            logsToInsert.add(inventoryLog);
                            
                            LOGGER.log(Level.INFO, "Preparing inventory update for product " + item.getProductId() + 
                                     ": " + oldQuantity + " -> " + newQuantity + " (confirmed order)");
                        } else {
                            LOGGER.log(Level.WARNING, "Not enough stock for product " + item.getProductId() + 
                                     " (required: " + item.getQuantity() + ", available: " + product.getQuantity() + ")");
                        }
                    }
                }
                
                if (!inventoryUpdates.isEmpty()) {
                    boolean inventorySuccess = productDAO.batchUpdateInventory(inventoryUpdates);
                    if (!inventorySuccess) {
                        LOGGER.log(Level.SEVERE, "Could not update inventory for order #" + orderId);
                        return;
                    }
                }
                
                if (!logsToInsert.isEmpty()) {
                    boolean batchSuccess = batchInsertInventoryLogs(logsToInsert);
                    if (batchSuccess) {
                        LOGGER.log(Level.INFO, "Inventory updated and " + logsToInsert.size() + " inventory logs added for order #" + orderId + " (confirmed)");
                    } else {
                        LOGGER.log(Level.WARNING, "Could not add inventory logs for order #" + orderId);
                    }
                } else {
                    LOGGER.log(Level.INFO, "No inventory update needed for order #" + orderId + " (logs already exist)");
                }
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing inventory for order #" + orderId + ": " + e.getMessage(), e);
            }
        }).start();
    }
    
    private Map<Integer, Product> getProductsByIds(List<Integer> productIds) {
        try {
            return productDAO.getProductsByIds(productIds);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching product details: " + e.getMessage(), e);
            return new HashMap<>();
        }
    }
    
    private Set<Integer> getExistingInventoryLogProductIds(int orderId, String referenceType) {
        try {
            InventoryLogDAOImpl inventoryLogDAO = new InventoryLogDAOImpl();
            return inventoryLogDAO.getExistingInventoryLogProductIds(orderId, referenceType);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking existing inventory logs: " + e.getMessage(), e);
            return new HashSet<>();
        }
    }
    

    
    private boolean batchInsertInventoryLogs(List<InventoryLog> logs) {
        if (logs.isEmpty()) {
            return true;
        }
        
        try {
            InventoryLogDAOImpl inventoryLogDAO = new InventoryLogDAOImpl();
            return inventoryLogDAO.batchInsertInventoryLogs(logs);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error batch inserting inventory logs: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Confirm order receipt (customer)
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
                addInventoryLogsForOrderConfirmation(orderId);
                
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
            
            LOGGER.log(java.util.logging.Level.INFO, "Marking order as refunded: " + orderId);
            
            try {
                boolean success = orderDAO.markPaymentRefunded(orderId);
                
                if (success) {
                    LOGGER.log(java.util.logging.Level.INFO, "Marked refund successfully for order: " + orderId);
                    response.sendRedirect("order?message=Order marked as refunded successfully");
                } else {
                    LOGGER.log(java.util.logging.Level.WARNING, "Could not mark order as refunded: " + orderId);
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
     * Update order status (admin only)
     * Called when admin changes order status from order detail page
     */
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response, User user)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to perform this operation");
        return;
    }
    try {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        String returnUrl = request.getParameter("returnUrl");
        if (returnUrl == null || returnUrl.isEmpty()) {
            returnUrl = "order?orderId=" + orderId;
        }
        boolean updated = orderDAO.updateOrderStatus(orderId, status);
        if (updated) {
            session.setAttribute("SUCCESS_MESSAGE", "Order status updated to " + status);
        } else {
            session.setAttribute("ERROR_MESSAGE", "Could not update order status");
        }
        response.sendRedirect(returnUrl);
    } catch (Exception e) {
        session.setAttribute("ERROR_MESSAGE", "Error: " + e.getMessage());
        response.sendRedirect("order");
        }
    }
} 