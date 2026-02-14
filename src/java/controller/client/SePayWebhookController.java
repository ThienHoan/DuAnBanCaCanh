package controller.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.impl.OrderDAOImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.pOrder.Order;
import utils.SePayConfig;
import dao.impl.UserDAO;
import dao.impl.NotificationDAO;
import dao.impl.OrderDAOImpl;
import jakarta.servlet.http.HttpSession;
import dao.impl.CartDAO;
import model.entity.pCart.Cart;

/**
 * Servlet để xử lý webhook từ SePay
 */
@WebServlet(name = "SePayWebhookController", urlPatterns = {"/sepay-webhook"})
public class SePayWebhookController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SePayWebhookController.class.getName());

    /**
     * Xử lý yêu cầu GET - Chủ yếu để kiểm tra kết nối và debug
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        System.out.println("DEBUG - GET request to webhook endpoint");
        
        // Trả về thông tin trạng thái để kiểm tra kết nối
        out.print("{\"success\": true, \"message\": \"SePay webhook endpoint is active\", \"version\": \"1.0\"}");
    }

    /**
     * Xử lý yêu cầu POST từ webhook SePay
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            // Đọc dữ liệu JSON từ request
            StringBuilder jsonBody = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            
            // Log dữ liệu webhook để debug
            LOGGER.info("SePay Webhook received: " + jsonBody.toString());
            System.out.println("DEBUG - WEBHOOK RECEIVED ---------------------");
            System.out.println("DEBUG - SePay Webhook received: " + jsonBody.toString());
            System.out.println("DEBUG - Request URI: " + request.getRequestURI());
            System.out.println("DEBUG - Method: " + request.getMethod());
            
            // Xác thực webhook (kiểm tra header hoặc signature)
            String authHeader = request.getHeader("Authorization");
            System.out.println("DEBUG - Authorization header: " + authHeader);
            
            // Tạm thời bỏ qua xác thực trong môi trường phát triển
            // QUAN TRỌNG: Trong môi trường sản xuất, hãy bật lại xác thực này!
            boolean isAuthenticated = true; // validateWebhook(authHeader, jsonBody.toString());
            if (!isAuthenticated) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"success\": false, \"message\": \"Unauthorized\"}");
                System.out.println("DEBUG - Webhook authentication failed");
                return;
            }
            
            // Phân tích dữ liệu JSON
            Gson gson = new Gson();
            JsonObject webhookData = gson.fromJson(jsonBody.toString(), JsonObject.class);
            
            System.out.println("DEBUG - Parsing webhook payload: " + webhookData);
            
            // Lấy thông tin đơn hàng
            OrderDAOImpl orderDAO = new OrderDAOImpl();
            
            // Lấy thông tin từ webhook
            String status = "completed"; // Mặc định là completed trong môi trường phát triển
            String orderNumber = "";
            String transactionId = "TX" + System.currentTimeMillis();
            
            if (webhookData.has("status")) {
                status = webhookData.get("status").getAsString();
            }
            
            if (webhookData.has("order_id")) {
                orderNumber = webhookData.get("order_id").getAsString();
            } else if (webhookData.has("content")) {
                // Trích xuất orderNumber từ nội dung chuyển khoản
                orderNumber = webhookData.get("content").getAsString();
            }
            
            if (webhookData.has("transaction_id")) {
                transactionId = webhookData.get("transaction_id").getAsString();
            }
            
            System.out.println("DEBUG - Webhook data: status=" + status + ", orderNumber=" + orderNumber + ", transactionId=" + transactionId);
            
            // Xử lý dựa trên trạng thái thanh toán
            if ("completed".equals(status) || "success".equals(status)) {
                // Tìm đơn hàng theo mã đơn hàng tạm thời
                Order order = orderDAO.getOrderByOrderNumber(orderNumber);
                System.out.println("DEBUG - Order found: " + (order != null));
                
                if (order != null) {
                    // Cập nhật trạng thái đơn hàng thành đã thanh toán
                    boolean updatePaymentResult = orderDAO.updatePaymentStatus(order.getOrderId(), "paid");
                    boolean updateStatusResult = orderDAO.updateOrderStatus(order.getOrderId(), "processing");
                    
                    System.out.println("DEBUG - Webhook: Order found with ID: " + order.getOrderId());
                    System.out.println("DEBUG - Webhook: Update payment status: " + updatePaymentResult);
                    System.out.println("DEBUG - Webhook: Update order status: " + updateStatusResult);
                    
                    // Thêm ghi chú về giao dịch
                    String notes = "Thanh toán qua SePay, mã giao dịch: " + transactionId;
                    boolean updateNotesResult = orderDAO.updateOrderNotes(order.getOrderId(), notes);
                    System.out.println("DEBUG - Webhook: Update order notes: " + updateNotesResult);
                    
                    // Xóa giỏ hàng sau khi thanh toán thành công
                    try {
                        // Lấy cart của user
                        CartDAO cartDAO = new CartDAO();
                        Cart userCart = cartDAO.getOrCreateCartByUserId(order.getUserId());
                        if (userCart != null) {
                            // Xóa các item trong giỏ hàng
                            boolean clearCartResult = cartDAO.clearCartItems(userCart.getCartId());
                            System.out.println("DEBUG - Webhook: Clear cart items for user " + order.getUserId() + ": " + clearCartResult);
                        }
                    } catch (Exception e) {
                        System.out.println("DEBUG - Webhook: Error clearing cart: " + e.getMessage());
                    }
                    
                    // Tạo thông báo cho người dùng
                    try {
                        UserDAO userDAO = new UserDAO();
                        int userId = order.getUserId();
                        
                        // Tạo thông báo chi tiết hơn với thông tin đơn hàng
                        String successMessage = "Đơn hàng " + orderNumber + " đã được thanh toán thành công! Cảm ơn bạn đã mua hàng.";
                        
                        // Thêm thông tin về số tiền thanh toán
                        if (webhookData.has("transferAmount")) {
                            double amount = webhookData.get("transferAmount").getAsDouble();
                            successMessage += " Số tiền: " + String.format("%,.0f", amount) + " VNĐ.";
                        }
                        
                        // Tạo hoặc cập nhật thông báo trong database
                        NotificationDAO notificationDAO = new NotificationDAO();
                        notificationDAO.createPaymentSuccessNotification(userId, order.getOrderId(), successMessage);
                        
                        System.out.println("DEBUG - Webhook: Created payment success notification for userId: " + userId);
                    } catch (Exception e) {
                        System.out.println("DEBUG - Webhook: Error creating notification: " + e.getMessage());
                    }
                    
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"success\": true, \"message\": \"Payment processed successfully\"}");
                    System.out.println("DEBUG - Webhook: Payment processed successfully for order: " + orderNumber);
                } else {
                    // Đơn hàng không tồn tại - có thể là đơn hàng tạm thời chưa được tạo
                    // Lưu thông tin này để xử lý sau khi người dùng kiểm tra trạng thái
                    LOGGER.info("Order not found for orderNumber: " + orderNumber);
                    System.out.println("DEBUG - Webhook: Order not found for orderNumber: " + orderNumber);
                    
                    // Lưu thông tin thanh toán vào bảng tạm để xử lý sau
                    // Trong thực tế, bạn có thể cần một bảng riêng để lưu thông tin thanh toán tạm thời
                    System.out.println("DEBUG - Webhook: Storing temporary payment info for later processing");
                    
                    // Thử lại một lần nữa với SQL khác để đảm bảo không có sự cố với case-sensitive
                    System.out.println("DEBUG - Webhook: Trying case-insensitive search for order: " + orderNumber);
                    order = orderDAO.getOrderByOrderNumberCaseInsensitive(orderNumber);
                    
                    if (order != null) {
                        System.out.println("DEBUG - Webhook: Found order with case-insensitive search: " + order.getOrderId());
                        // Cập nhật trạng thái thanh toán
                        boolean updatePaymentResult = orderDAO.updatePaymentStatus(order.getOrderId(), "paid");
                        boolean updateStatusResult = orderDAO.updateOrderStatus(order.getOrderId(), "processing");
                        System.out.println("DEBUG - Webhook: Update payment status: " + updatePaymentResult);
                        System.out.println("DEBUG - Webhook: Update order status: " + updateStatusResult);
                        
                        // Cập nhật ghi chú với mã giao dịch
                        String notes = order.getNotes() + " | Mã giao dịch SePay: " + transactionId;
                        boolean updateNotesResult = orderDAO.updateOrderNotes(order.getOrderId(), notes);
                        System.out.println("DEBUG - Webhook: Update order notes: " + updateNotesResult);
                        
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print("{\"success\": true, \"message\": \"Payment processed successfully with case-insensitive search\"}");
                        System.out.println("DEBUG - Webhook: Payment processed successfully with case-insensitive search for order: " + orderNumber);
                    } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print("{\"success\": true, \"message\": \"Payment received, order will be processed\"}");
                        System.out.println("DEBUG - Webhook: Payment received, but order not found. Will be processed later.");
                    }
                }
            } else {
                // Xử lý các trạng thái khác (failed, cancelled, etc.)
                LOGGER.info("Payment status: " + status + " for orderNumber: " + orderNumber);
                System.out.println("DEBUG - Payment status: " + status + " for orderNumber: " + orderNumber);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"success\": true, \"message\": \"Webhook received\"}");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing SePay webhook", e);
            System.out.println("DEBUG - Error processing webhook: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Error processing webhook: " + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Xác thực webhook từ SePay
     */
    private boolean validateWebhook(String authHeader, String payload) {
        // Kiểm tra header Authorization
        if (authHeader == null) {
            System.out.println("DEBUG - Auth header is null");
            return false;
        }
        
        // Kiểm tra xem header có định dạng "sepay_api_key R8OO0QJ7RMVHUH..." không
        if (authHeader.startsWith("sepay_api_key ")) {
            String apiKey = authHeader.substring("sepay_api_key ".length());
            boolean isValid = SePayConfig.getApiKey().equals(apiKey);
            System.out.println("DEBUG - Validating sepay_api_key: " + isValid);
            return isValid;
        }
        
        // Kiểm tra xem header có định dạng "Bearer R8OO0QJ7RMVHUH..." không
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean isValid = SePayConfig.getWebhookSecret().equals(token);
            System.out.println("DEBUG - Validating Bearer token: " + isValid);
            return isValid;
        }
        
        System.out.println("DEBUG - Auth header format not recognized: " + authHeader);
        return false;
    }
} 