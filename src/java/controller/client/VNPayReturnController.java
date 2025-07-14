package controller.client;

import dao.impl.CouponDAO;
import dao.impl.OrderDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/vnpay-return")
public class VNPayReturnController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(VNPayReturnController.class.getName());
    private OrderDAOImpl orderDAO;
    private CouponDAO couponDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        couponDAO = new CouponDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            // Lấy thông tin đơn hàng đang chờ từ session
            @SuppressWarnings("unchecked")
            Map<String, Object> pendingOrder = (Map<String, Object>) session.getAttribute("PENDING_ORDER");
            
            if (pendingOrder == null) {
                session.setAttribute("ERROR_MESSAGE", "Không tìm thấy thông tin đơn hàng.");
                response.sendRedirect("cartClient");
                return;
            }
            
            // Lấy thông tin từ VNPay
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
            String vnp_Amount = request.getParameter("vnp_Amount");
            
            // Log thông tin VNPay để debug
            LOGGER.info("VNPay Response - ResponseCode: " + vnp_ResponseCode + 
                      ", TransactionStatus: " + vnp_TransactionStatus + 
                      ", TransactionNo: " + vnp_TransactionNo + 
                      ", OrderInfo: " + vnp_OrderInfo + 
                      ", Amount: " + vnp_Amount);
            
            // Kiểm tra kết quả thanh toán
            boolean paymentSuccess = "00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus);
            
            if (paymentSuccess) {
                // Tạo đơn hàng
                int userId = (int) pendingOrder.get("userId");
                int cartId = (int) pendingOrder.get("cartId");
                String orderNumber = (String) pendingOrder.get("orderNumber");
                int addressId = (int) pendingOrder.get("addressId");
                String paymentMethod = (String) pendingOrder.get("paymentMethod");
                String notes = (String) pendingOrder.get("notes");
                BigDecimal discountAmount = (BigDecimal) pendingOrder.get("discountAmount");
                
                try {
                    // Tạo đơn hàng
                    int orderId = orderDAO.createOrder(userId, cartId, orderNumber, addressId, paymentMethod, notes, discountAmount);
                    
                    // Cập nhật thông tin thanh toán
                    orderDAO.updatePaymentStatus(orderId, "paid");
                    
                    // Cập nhật trạng thái thanh toán trong bảng Payments
                    String updatePaymentSql = "UPDATE Payments SET status = 'completed' WHERE order_id = ?";
                    try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                         java.sql.PreparedStatement ps = conn.prepareStatement(updatePaymentSql)) {
                        ps.setInt(1, orderId);
                        ps.executeUpdate();
                    } catch (Exception ex) {
                        LOGGER.log(Level.WARNING, "Không thể cập nhật trạng thái thanh toán: " + ex.getMessage(), ex);
                    }
                    
                    // Record coupon usage if coupon was applied
                    if (pendingOrder.containsKey("couponId")) {
                        int couponId = (int) pendingOrder.get("couponId");
                        couponDAO.recordCouponUsage(couponId, userId, orderId, discountAmount);
                    }
                    
                    // Xóa thông tin đơn hàng đang chờ và mã giảm giá
                    session.removeAttribute("PENDING_ORDER");
                    session.removeAttribute("COUPON");
                    
                    // Chuyển hướng đến trang xác nhận đơn hàng
                    request.getRequestDispatcher("order-confirmation.jsp?orderId=" + orderId).forward(request, response);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Lỗi khi tạo đơn hàng: " + e.getMessage(), e);
                    session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
                    response.sendRedirect("checkout");
                }
            } else {
                // Thanh toán thất bại
                if ("24".equals(vnp_ResponseCode)) {
                    session.setAttribute("ERROR_MESSAGE", "Đã hủy thanh toán thành công.");
                } else {
                session.setAttribute("ERROR_MESSAGE", "Thanh toán thất bại. Mã lỗi: " + vnp_ResponseCode);
                }
                response.sendRedirect("checkout");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi xử lý kết quả thanh toán VNPay: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi xử lý kết quả thanh toán: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }
} 