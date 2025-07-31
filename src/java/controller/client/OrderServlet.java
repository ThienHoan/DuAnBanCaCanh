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
            
            // Kiểm tra xem đơn hàng đã được xác nhận giao hàng chưa
            if ("shipping".equals(order.getStatus())) {
                response.sendRedirect("order?error=Order is already marked as shipped");
                return;
            }
            
            // Kiểm tra stock trước khi xác nhận giao hàng
            String stockError = orderDAO.validateOrderStockForShipping(orderId);
            if (stockError != null) {
                response.sendRedirect("order?error=" + stockError);
                return;
            }
            
            // Không cần kiểm tra trạng thái thanh toán trước khi giao hàng
            // Đơn hàng COD sẽ được thanh toán khi giao hàng
            
            boolean success = orderDAO.updateOrderStatus(orderId, "shipping");
            
            if (success) {
                // Cập nhật inventory và inventory_logs khi admin xác nhận giao hàng
                addInventoryLogsForShipping(orderId);
                
                response.sendRedirect("order?message=Order marked as shipped successfully");
            } else {
                response.sendRedirect("order?error=Failed to mark order as shipped");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xác nhận giao hàng: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error confirming shipment: " + e.getMessage());
        }
    }
    
    /**
     * Thêm inventory logs và cập nhật inventory khi đơn hàng được xác nhận giao hàng
     * Tối ưu hóa performance với batch processing và async execution
     */
    private void addInventoryLogsForShipping(int orderId) {
        // Sử dụng async processing để không block response
        new Thread(() -> {
            try {
                LOGGER.log(Level.INFO, "Bắt đầu xử lý inventory cho đơn hàng #" + orderId);
                
                // Lấy danh sách sản phẩm trong đơn hàng
                List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
                if (orderItems == null || orderItems.isEmpty()) {
                    LOGGER.log(Level.WARNING, "Không tìm thấy sản phẩm cho đơn hàng #" + orderId);
                    return;
                }
                
                // Tối ưu: Lấy tất cả product IDs một lần
                List<Integer> productIds = orderItems.stream()
                    .map(OrderItem::getProductId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
                
                // Tối ưu: Lấy tất cả sản phẩm một lần query
                Map<Integer, Product> productsMap = getProductsByIds(productIds);
                
                // Tối ưu: Kiểm tra existing logs một lần cho toàn bộ đơn hàng
                Set<Integer> existingProductIds = getExistingInventoryLogProductIds(orderId, "order_shipped");
                
                // Tối ưu: Batch update inventory và tạo logs
                List<InventoryLog> logsToInsert = new ArrayList<>();
                List<Object[]> inventoryUpdates = new ArrayList<>();
                
                for (OrderItem item : orderItems) {
                    Product product = productsMap.get(item.getProductId());
                    if (product != null && !existingProductIds.contains(item.getProductId())) {
                        // Kiểm tra stock trước khi cập nhật
                        if (product.getQuantity() >= item.getQuantity()) {
                            int oldQuantity = product.getQuantity();
                            int newQuantity = oldQuantity - item.getQuantity();
                            
                            // Chuẩn bị inventory update
                            inventoryUpdates.add(new Object[]{
                                newQuantity, // new quantity
                                item.getProductId(), // product_id
                                item.getQuantity() // required quantity for WHERE clause
                            });
                            
                            // Tạo inventory log cho việc giao hàng
                            InventoryLog inventoryLog = new InventoryLog(
                                item.getProductId(),
                                oldQuantity, // quantity_before
                                newQuantity, // quantity_after
                                "decrease", // change_type
                                "Đơn hàng #" + orderId + " - Xác nhận giao hàng: " + item.getProductName(),
                                orderId, // reference_id
                                "order_shipped" // reference_type
                            );
                            logsToInsert.add(inventoryLog);
                            
                            LOGGER.log(Level.INFO, "Chuẩn bị cập nhật inventory cho sản phẩm " + item.getProductId() + 
                                     ": " + oldQuantity + " -> " + newQuantity);
                        } else {
                            LOGGER.log(Level.WARNING, "Không đủ stock cho sản phẩm " + item.getProductId() + 
                                     " (cần: " + item.getQuantity() + ", có: " + product.getQuantity() + ")");
                        }
                    }
                }
                
                // Batch update inventory trước
                if (!inventoryUpdates.isEmpty()) {
                    boolean inventorySuccess = batchUpdateInventory(inventoryUpdates);
                    if (!inventorySuccess) {
                        LOGGER.log(Level.SEVERE, "Không thể cập nhật inventory cho đơn hàng #" + orderId);
                        return;
                    }
                }
                
                // Batch insert inventory logs sau khi cập nhật inventory thành công
                if (!logsToInsert.isEmpty()) {
                    boolean batchSuccess = batchInsertInventoryLogs(logsToInsert);
                    if (batchSuccess) {
                        LOGGER.log(Level.INFO, "Đã cập nhật inventory và thêm " + logsToInsert.size() + " inventory logs cho đơn hàng #" + orderId);
                    } else {
                        LOGGER.log(Level.WARNING, "Không thể thêm inventory logs cho đơn hàng #" + orderId);
                    }
                } else {
                    LOGGER.log(Level.INFO, "Không cần cập nhật inventory cho đơn hàng #" + orderId + " (đã có sẵn logs)");
                }
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi xử lý inventory cho đơn hàng #" + orderId + ": " + e.getMessage(), e);
            }
        }).start();
    }
    
    /**
     * Thêm inventory logs và cập nhật inventory khi admin xác nhận đơn hàng
     * Tối ưu hóa performance với batch processing và async execution
     */
    private void addInventoryLogsForOrderConfirmation(int orderId) {
        // Sử dụng async processing để không block response
        new Thread(() -> {
            try {
                LOGGER.log(Level.INFO, "Bắt đầu xử lý inventory cho đơn hàng #" + orderId + " khi xác nhận");
                
                // Lấy danh sách sản phẩm trong đơn hàng
                List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
                if (orderItems == null || orderItems.isEmpty()) {
                    LOGGER.log(Level.WARNING, "Không tìm thấy sản phẩm cho đơn hàng #" + orderId);
                    return;
                }
                
                // Tối ưu: Lấy tất cả product IDs một lần
                List<Integer> productIds = orderItems.stream()
                    .map(OrderItem::getProductId)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
                
                // Tối ưu: Lấy tất cả sản phẩm một lần query
                Map<Integer, Product> productsMap = getProductsByIds(productIds);
                
                // Tối ưu: Kiểm tra existing logs một lần cho toàn bộ đơn hàng
                Set<Integer> existingProductIds = getExistingInventoryLogProductIds(orderId, "order_confirmed");
                
                // Tối ưu: Batch update inventory và tạo logs
                List<InventoryLog> logsToInsert = new ArrayList<>();
                List<Object[]> inventoryUpdates = new ArrayList<>();
                
                for (OrderItem item : orderItems) {
                    Product product = productsMap.get(item.getProductId());
                    if (product != null && !existingProductIds.contains(item.getProductId())) {
                        // Kiểm tra stock trước khi cập nhật
                        if (product.getQuantity() >= item.getQuantity()) {
                            int oldQuantity = product.getQuantity();
                            int newQuantity = oldQuantity - item.getQuantity();
                            
                            // Chuẩn bị inventory update
                            inventoryUpdates.add(new Object[]{
                                newQuantity, // new quantity
                                item.getProductId(), // product_id
                                item.getQuantity() // required quantity for WHERE clause
                            });
                            
                            // Tạo inventory log cho việc xác nhận đơn hàng
                            InventoryLog inventoryLog = new InventoryLog(
                                item.getProductId(),
                                oldQuantity, // quantity_before
                                newQuantity, // quantity_after
                                "decrease", // change_type
                                "Đơn hàng #" + orderId + " - Admin xác nhận đơn hàng: " + item.getProductName(),
                                orderId, // reference_id
                                "order_confirmed" // reference_type
                            );
                            logsToInsert.add(inventoryLog);
                            
                            LOGGER.log(Level.INFO, "Chuẩn bị cập nhật inventory cho sản phẩm " + item.getProductId() + 
                                     ": " + oldQuantity + " -> " + newQuantity + " (xác nhận đơn hàng)");
                        } else {
                            LOGGER.log(Level.WARNING, "Không đủ stock cho sản phẩm " + item.getProductId() + 
                                     " (cần: " + item.getQuantity() + ", có: " + product.getQuantity() + ")");
                        }
                    }
                }
                
                // Batch update inventory trước
                if (!inventoryUpdates.isEmpty()) {
                    boolean inventorySuccess = batchUpdateInventory(inventoryUpdates);
                    if (!inventorySuccess) {
                        LOGGER.log(Level.SEVERE, "Không thể cập nhật inventory cho đơn hàng #" + orderId);
                        return;
                    }
                }
                
                // Batch insert inventory logs sau khi cập nhật inventory thành công
                if (!logsToInsert.isEmpty()) {
                    boolean batchSuccess = batchInsertInventoryLogs(logsToInsert);
                    if (batchSuccess) {
                        LOGGER.log(Level.INFO, "Đã cập nhật inventory và thêm " + logsToInsert.size() + " inventory logs cho đơn hàng #" + orderId + " (xác nhận)");
                    } else {
                        LOGGER.log(Level.WARNING, "Không thể thêm inventory logs cho đơn hàng #" + orderId);
                    }
                } else {
                    LOGGER.log(Level.INFO, "Không cần cập nhật inventory cho đơn hàng #" + orderId + " (đã có sẵn logs)");
                }
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi xử lý inventory cho đơn hàng #" + orderId + ": " + e.getMessage(), e);
            }
        }).start();
    }
    
    /**
     * Lấy nhiều sản phẩm theo IDs (tối ưu query)
     */
    private Map<Integer, Product> getProductsByIds(List<Integer> productIds) {
        try {
            ProductDAO productDAO = new ProductDAO();
            return productDAO.getProductsByIds(productIds);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin sản phẩm: " + e.getMessage(), e);
            return new HashMap<>();
        }
    }
    
    /**
     * Lấy danh sách product IDs đã có inventory logs (tối ưu query)
     */
    private Set<Integer> getExistingInventoryLogProductIds(int orderId, String referenceType) {
        try {
            InventoryLogDAOImpl inventoryLogDAO = new InventoryLogDAOImpl();
            return inventoryLogDAO.getExistingInventoryLogProductIds(orderId, referenceType);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra existing inventory logs: " + e.getMessage(), e);
            return new HashSet<>();
        }
    }
    
    /**
     * Batch update inventory (tối ưu performance)
     */
    private boolean batchUpdateInventory(List<Object[]> inventoryUpdates) {
        if (inventoryUpdates.isEmpty()) {
            return true;
        }
        
        try {
            String updateSql = "UPDATE Products SET quantity = ? WHERE product_id = ? AND quantity >= ?";
            try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(updateSql)) {
                
                conn.setAutoCommit(false);
                int successCount = 0;
                
                for (Object[] update : inventoryUpdates) {
                    int newQuantity = (Integer) update[0];
                    int productId = (Integer) update[1];
                    int requiredQuantity = (Integer) update[2];
                    
                    ps.setInt(1, newQuantity);
                    ps.setInt(2, productId);
                    ps.setInt(3, requiredQuantity);
                    
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        successCount++;
                    } else {
                        LOGGER.log(Level.WARNING, "Không thể cập nhật inventory cho sản phẩm " + productId + 
                                 " (không đủ stock hoặc sản phẩm không tồn tại)");
                    }
                }
                
                if (successCount == inventoryUpdates.size()) {
                    conn.commit();
                    LOGGER.log(Level.INFO, "Đã cập nhật inventory thành công cho " + successCount + " sản phẩm");
                    return true;
                } else {
                    conn.rollback();
                    LOGGER.log(Level.WARNING, "Chỉ cập nhật được " + successCount + "/" + inventoryUpdates.size() + " sản phẩm");
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi batch update inventory: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Batch insert inventory logs (tối ưu performance)
     */
    private boolean batchInsertInventoryLogs(List<InventoryLog> logs) {
        if (logs.isEmpty()) {
            return true;
        }
        
        try {
            InventoryLogDAOImpl inventoryLogDAO = new InventoryLogDAOImpl();
            return inventoryLogDAO.batchInsertInventoryLogs(logs);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi batch insert inventory logs: " + e.getMessage(), e);
            return false;
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
                    
                    // Log that payment has been confirmed
                    LOGGER.log(java.util.logging.Level.INFO, "Payment confirmed for order #" + orderId + ". Inventory was already updated when order was created.");
                    
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
            
            // Kiểm tra stock trước khi xác nhận đơn hàng
            String stockError = orderDAO.validateOrderStockForShipping(orderId);
            if (stockError != null) {
                response.sendRedirect("order?error=" + stockError);
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(orderId, "confirmed");
            
            if (success) {
                // Cập nhật inventory và inventory_logs khi admin xác nhận đơn hàng
                addInventoryLogsForOrderConfirmation(orderId);
                
                response.sendRedirect("order?message=Order confirmed successfully");
            } else {
                response.sendRedirect("order?error=Failed to confirm order");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("order?error=Invalid order ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xác nhận đơn hàng: " + e.getMessage(), e);
            response.sendRedirect("order?error=Error confirming order: " + e.getMessage());
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

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response, User user)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện thao tác này");
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
            session.setAttribute("SUCCESS_MESSAGE", "Đã cập nhật trạng thái đơn hàng thành " + status);
        } else {
            session.setAttribute("ERROR_MESSAGE", "Không thể cập nhật trạng thái đơn hàng");
        }
        response.sendRedirect(returnUrl);
    } catch (Exception e) {
        session.setAttribute("ERROR_MESSAGE", "Lỗi: " + e.getMessage());
        response.sendRedirect("order");
    }
}
} 