package controller.client;

import dao.impl.CouponDAO;
import dao.impl.OrderDAOImpl;
import dao.impl.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Product;
import model.entity.pOrder.OrderItem;
import java.util.List;
import utils.InventoryLogUtil;

/**
 * Controller for handling VNPay payment callback
 * Processes payment verification, order creation, inventory management, and status updates
 */
public class VNPayReturnController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(VNPayReturnController.class.getName());
    
    private OrderDAOImpl orderDAO;
    private CouponDAO couponDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        couponDAO = new CouponDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> pendingOrder = (Map<String, Object>) session.getAttribute("PENDING_ORDER");
            
            if (pendingOrder == null) {
                session.setAttribute("ERROR_MESSAGE", "Order information not found.");
                response.sendRedirect("cartClient");
                return;
            }
            
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
            String vnp_Amount = request.getParameter("vnp_Amount");
            
            LOGGER.info("VNPay Response - ResponseCode: " + vnp_ResponseCode + 
                      ", TransactionStatus: " + vnp_TransactionStatus + 
                      ", TransactionNo: " + vnp_TransactionNo + 
                      ", OrderInfo: " + vnp_OrderInfo + 
                      ", Amount: " + vnp_Amount);
            
            boolean paymentSuccess = "00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus);
            
            if (paymentSuccess) {
                int userId = (int) pendingOrder.get("userId");
                int cartId = (int) pendingOrder.get("cartId");
                String orderNumber = (String) pendingOrder.get("orderNumber");
                int addressId = (int) pendingOrder.get("addressId");
                String paymentMethod = (String) pendingOrder.get("paymentMethod");
                String notes = (String) pendingOrder.get("notes");
                BigDecimal discountAmount = (BigDecimal) pendingOrder.get("discountAmount");
                
                try {
                    int orderId = orderDAO.createOrder(userId, cartId, orderNumber, addressId, paymentMethod, notes, discountAmount);
                    
                    orderDAO.updatePaymentStatus(orderId, "paid");
                    
                    boolean paymentUpdateSuccess = orderDAO.updatePaymentStatusWithTransactionId(orderId, "completed", vnp_TransactionNo);
                    if (!paymentUpdateSuccess) {
                        LOGGER.log(Level.WARNING, "Could not update payment status with transaction_id: " + vnp_TransactionNo);
                    }
                    
                    List<OrderItem> orderItems = orderDAO.getOrderItemsWithDetails(orderId);
                    List<Object[]> inventoryUpdates = new java.util.ArrayList<>();
                    for (OrderItem item : orderItems) {
                        int productId = item.getProductId();
                        int requiredQuantity = item.getQuantity();
                        int currentQuantity = productDAO.getProductQuantity(productId);
                        int newQuantity = currentQuantity - requiredQuantity;
                        if (newQuantity < 0) {
                            session.setAttribute("ERROR_MESSAGE", "Product '" + item.getProductName() + "' out of stock.");
                            response.sendRedirect("cartClient");
                            return;
                        }
                        inventoryUpdates.add(new Object[]{newQuantity, productId, requiredQuantity});
                    }
                    if (!inventoryUpdates.isEmpty()) {
                        boolean inventorySuccess = productDAO.batchUpdateInventory(inventoryUpdates);
                        if (!inventorySuccess) {
                            session.setAttribute("ERROR_MESSAGE", "Error updating inventory. Order not processed.");
                            response.sendRedirect("cartClient");
                            return;
                        }
                    }
                    
                    try {
                        String logSql = "INSERT INTO System_logs (log_type, message, reference_id, reference_type, created_at) " +
                                       "VALUES (?, ?, ?, ?, GETDATE())";
                        try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                             java.sql.PreparedStatement ps = conn.prepareStatement(logSql)) {
                            ps.setString(1, "payment");
                            ps.setString(2, "Payment completed for order #" + orderId + ". Inventory has been updated automatically.");
                            ps.setInt(3, orderId);
                            ps.setString(4, "order");
                            ps.executeUpdate();
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.WARNING, "Could not write system log: " + ex.getMessage(), ex);
                    }
                    
                    if (pendingOrder.containsKey("couponId")) {
                        int couponId = (int) pendingOrder.get("couponId");
                        couponDAO.recordCouponUsage(couponId, userId, orderId, discountAmount);
                    }
                    
                    session.removeAttribute("PENDING_ORDER");
                    session.removeAttribute("COUPON");
                    
                    request.getRequestDispatcher("order-confirmation.jsp?orderId=" + orderId).forward(request, response);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error creating order: " + e.getMessage(), e);
                    session.setAttribute("ERROR_MESSAGE", "An error occurred while creating order: " + e.getMessage());
                    response.sendRedirect("checkout");
                }
            } else {
                if ("24".equals(vnp_ResponseCode)) {
                    session.setAttribute("ERROR_MESSAGE", "Payment cancelled successfully.");
                } else {
                    session.setAttribute("ERROR_MESSAGE", "Payment failed. Error code: " + vnp_ResponseCode);
                }
                response.sendRedirect("checkout");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing VNPay payment result: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "An error occurred while processing payment result: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }
} 