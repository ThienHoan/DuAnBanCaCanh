package ai;

import ai.tools.AquariumTools;
// Import các lớp chính xác từ thư viện Vertex AI
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
// Thay thế import sai bằng import đúng
import com.google.cloud.vertexai.api.GenerateContentResponse;
import dao.impl.CartDAO;
import dao.impl.ProductDAO;
import dao.impl.UserDAO;
import dao.impl.AddressDAO;
import dao.impl.OrderDAOImpl;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.entity.Product;
import model.entity.pCart.Cart;
import model.entity.pCart.CartItem;
import model.entity.User;
import model.entity.Address;
import java.math.BigDecimal;
import model.entity.pOrder.Order;
import java.util.Date;
import utils.SePayConfig;
import dao.impl.NotificationDAO;
import model.entity.Notification;
import dao.impl.OrderDAO;
import jakarta.servlet.http.HttpSession;

/**
 * PHIÊN BẢN CUỐI CÙNG, ĐÚNG VÀ HOÀN CHỈNH
 */
public class SimpleAIService {
    
    private final VertexAI vertexAIClient; 
    private final AquariumTools aquariumTools;
    private final ProductDAO productDAO;
    private final CartDAO cartDAO;
    private final UserDAO userDAO;
    private final AddressDAO addressDAO;
    
    // Các pattern nhận dạng ý định mua hàng
    private static final Pattern BUY_PATTERN = Pattern.compile("(?i)(mua|đặt|thêm vào giỏ|cho.+?(\\d+)\\s+(.+?)|lấy.+?(\\d+)\\s+(.+?))");
    // Sửa pattern quantity để nhận diện cả '2 cá', '2 con', '2 sản phẩm', v.v.
    private static final Pattern QUANTITY_PATTERN = Pattern.compile("(?i)(\\d+)\\s*(cá|con|cái|sản phẩm|cây|bịch|gói|hộp|chai)?");
    private static final Pattern PRODUCT_NAME_PATTERN = Pattern.compile("(?i)(cá|thức ăn|thuốc|bể|lọc|đèn|cây|phụ kiện)\\s+([\\w\\s]+)");
    private static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("(?i)(?:(?:đơn|đơn hàng|order|mã đơn|mã)\\s*(?:số|number)?\\s*[:#\\s]?\\s*)?(ORD[A-Za-z0-9]+|[A-Za-z0-9]{10,})");
    private static final Pattern BANK_CONFIG_PATTERN = Pattern.compile("(?i)(?:cấu hình|cài đặt|thiết lập|đặt)\\s+(?:tài khoản|tk|ngân hàng)\\s+([a-zA-Z]+)\\s+([0-9]+)\\s+(.+)");

    public SimpleAIService() {
        // !!! QUAN TRỌNG: Thay "your-gcp-project-id" bằng ID dự án Google Cloud của bạn
        String projectId = "fishshop-462614"; 
        String location = "asia-southeast1"; 
        
        this.vertexAIClient = new VertexAI(projectId, location);
        
        this.aquariumTools = new AquariumTools();
        this.productDAO = new ProductDAO();
        this.cartDAO = new CartDAO();
        this.userDAO = new UserDAO();
        this.addressDAO = new AddressDAO();
    }

    /**
     * Phương thức chính để xử lý câu hỏi của người dùng và trả về phản hồi AI
     */
    public String getAIResponse(String userQuestion, List<String> chatHistory, Map<String, Object> context, HttpSession session) {
        if (vertexAIClient == null) {
            return "Lỗi cấu hình: Không thể kết nối đến Vertex AI.";
        }
         
        try {
            // Lấy thông tin người dùng từ context
            Integer userId = (Integer) context.get("userId");
            boolean isLoggedIn = context.containsKey("isLoggedIn") ? (Boolean) context.get("isLoggedIn") : false;
            String userEmail = (String) context.get("userEmail");
            String userName = (String) context.get("userName");
            
            System.out.println("DEBUG - User message: " + userQuestion);
            System.out.println("DEBUG - User ID: " + userId);
            System.out.println("DEBUG - Is logged in: " + isLoggedIn);
            System.out.println("DEBUG - Context keys: " + context.keySet());
            
            // Debug thêm thông tin chi tiết về context
            if (context != null) {
                System.out.println("DEBUG - Context details:");
                for (Map.Entry<String, Object> entry : context.entrySet()) {
                    System.out.println("   " + entry.getKey() + " = " + entry.getValue());
                }
            }
            
            // Trước khi trả lời, kiểm tra xem có thông báo thanh toán thành công nào không
            // Chỉ kiểm tra khi người dùng đã đăng nhập và yêu cầu kiểm tra thanh toán
            if (isLoggedIn && userId != null && userId > 0 && 
                (userQuestion.toLowerCase().contains("kiểm tra thanh toán") || 
                 userQuestion.toLowerCase().contains("check thanh toán") ||
                 userQuestion.toLowerCase().contains("trạng thái thanh toán") ||
                 userQuestion.toLowerCase().contains("đã thanh toán chưa"))) {
                
                String paymentSuccessNotification = getPaymentSuccessNotifications(userId, context);
                if (paymentSuccessNotification != null && !paymentSuccessNotification.isEmpty()) {
                    // Xóa các thông tin thanh toán tạm thời trong context
                    context.remove("pending_sepay_checkout");
                    context.remove("temp_order_number");
                    context.remove("selected_address_id");
                    context.remove("cart_id");
                    context.remove("final_amount");
                    
                    // Đánh dấu tất cả thông báo thanh toán là đã đọc
                    NotificationDAO notificationDAO = new NotificationDAO();
                    notificationDAO.markAllPaymentNotificationsAsRead(userId);
                    System.out.println("DEBUG - Marked all payment notifications as read for user " + userId);
                    
                    return paymentSuccessNotification;
                }
            }
            
            // Xử lý trực tiếp thanh toán QR
            String lowerQuestion = userQuestion.toLowerCase();
            if (lowerQuestion.equals("thanh toán") ||
                lowerQuestion.contains("thanh toán qr") || 
                lowerQuestion.contains("thanh toán mã qr") || 
                lowerQuestion.contains("thanh toán bằng qr") ||
                lowerQuestion.contains("thanh toán chuyển khoản") ||
                lowerQuestion.contains("thanh toán sepay")) {
                
                System.out.println("DEBUG - Direct QR payment detected");
                if (isLoggedIn && userId != null && userId > 0) {
                    return handleSePayQRPayment(userId, context);
                } else {
                    return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                }
            }
            
            // Xử lý trực tiếp kiểm tra thanh toán
            if (lowerQuestion.contains("kiểm tra thanh toán") || 
                lowerQuestion.contains("trạng thái thanh toán") ||
                lowerQuestion.contains("đã thanh toán chưa") ||
                lowerQuestion.contains("thanh toán xong") ||
                lowerQuestion.contains("check thanh toán") ||
                lowerQuestion.equals("kiểm tra") ||
                lowerQuestion.equals("check")) {
                
                System.out.println("DEBUG - Direct payment status check detected");
                if (isLoggedIn && userId != null && userId > 0) {
                    return handleCheckSePayStatus(userId, context);
                } else {
                    return "🛍️ Bạn cần đăng nhập để kiểm tra trạng thái thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                }
            }
            
            // Kiểm tra trực tiếp các từ khóa COD
            if (userQuestion.toLowerCase().contains("thanh toán cod") || 
                userQuestion.toLowerCase().contains("đặt hàng cod") ||
                userQuestion.toLowerCase().contains("đặt cod") ||
                userQuestion.toLowerCase().contains("thanh toán khi nhận hàng")) {
                
                System.out.println("DEBUG - Detected direct COD checkout intent");
                if (isLoggedIn && userId != null && userId > 0) {
                    return handleDirectCODCheckout(userId, context);
                } else {
                    return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                }
            }
            
            // Kiểm tra trực tiếp xác nhận COD
            if (userQuestion.toLowerCase().contains("xác nhận") && 
                (userQuestion.toLowerCase().contains("cod") || 
                 userQuestion.toLowerCase().contains("thanh toán") || 
                 userQuestion.toLowerCase().contains("đặt hàng"))) {
                
                System.out.println("DEBUG - Detected confirm COD order intent");
                if (isLoggedIn && userId != null && userId > 0) {
                    // Kiểm tra context
                    System.out.println("DEBUG - Context has pending_cod_checkout: " + context.containsKey("pending_cod_checkout"));
                    if (context.containsKey("pending_cod_checkout") && 
                        Boolean.TRUE.equals(context.get("pending_cod_checkout"))) {
                        
                        int addressId = (Integer)context.get("selected_address_id");
                        return handleConfirmCODOrder(userId, addressId, context);
                    } else {
                        return "🤔 Hmm, bạn chưa bắt đầu quá trình đặt hàng COD. Hãy gõ \"thanh toán COD\" để bắt đầu nhé!";
                    }
                } else {
                    return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                }
            }
            
            // Kiểm tra nếu có ý định đặc biệt
            Map<String, Object> purchaseIntent = detectPurchaseIntent(userQuestion);
            if (purchaseIntent != null && purchaseIntent.containsKey("intent")) {
                String intent = (String) purchaseIntent.get("intent");
                System.out.println("DEBUG - Detected intent: " + intent);
                
                // Cấu hình tài khoản ngân hàng
                if ("configure_bank".equals(intent)) {
                    System.out.println("DEBUG - Processing configure_bank intent");
                    if (isLoggedIn && userId != null && userId > 0) {
                        String bankName = (String) purchaseIntent.get("bank_name");
                        String accountNumber = (String) purchaseIntent.get("account_number");
                        String accountName = (String) purchaseIntent.get("account_name");
                        
                        return configureBankAccount(bankName, accountNumber, accountName, context);
                    } else {
                        return "🛍️ Bạn cần đăng nhập để cấu hình tài khoản ngân hàng nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Xem giỏ hàng
                if ("view_cart".equals(intent)) {
                    if (isLoggedIn && userId != null && userId > 0) {
                        return handleViewCart(userId);
                    } else {
                        return "🛒 Bạn cần đăng nhập để xem giỏ hàng nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Thanh toán
                if ("checkout".equals(intent)) {
                    if (isLoggedIn && userId != null && userId > 0) {
                        return handleCheckout(userId);
                    } else {
                        return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Thanh toán COD trực tiếp trong chat
                if ("checkout_cod_direct".equals(intent)) {
                    System.out.println("DEBUG - Processing checkout_cod_direct intent");
                    if (isLoggedIn && userId != null && userId > 0) {
                        // Lưu thông tin giỏ hàng và addressId vào context để xác nhận sau
                        return handleDirectCODCheckout(userId, context);
                    } else {
                        return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Thanh toán qua SePay
                if ("checkout_sepay".equals(intent)) {
                    System.out.println("DEBUG - Processing checkout_sepay intent");
                    if (isLoggedIn && userId != null && userId > 0) {
                        return handleSePayQRPayment(userId, context);
                    } else {
                        return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Kiểm tra trạng thái thanh toán
                if ("check_payment_status".equals(intent)) {
                    System.out.println("DEBUG - Processing check_payment_status intent");
                    if (isLoggedIn && userId != null && userId > 0) {
                        return handleCheckSePayStatus(userId, context);
                    } else {
                        return "🛍️ Bạn cần đăng nhập để kiểm tra trạng thái thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Xác nhận đặt hàng COD
                if ("confirm_cod_order".equals(intent)) {
                    System.out.println("DEBUG - Processing confirm_cod_order intent");
                    if (isLoggedIn && userId != null && userId > 0) {
                        System.out.println("DEBUG - Context has pending_cod_checkout: " + context.containsKey("pending_cod_checkout"));
                        if (context.containsKey("pending_cod_checkout") && 
                            Boolean.TRUE.equals(context.get("pending_cod_checkout"))) {
                            
                            int addressId = (Integer)context.get("selected_address_id");
                            return handleConfirmCODOrder(userId, addressId, context);
                        } else {
                            return "🤔 Hmm, bạn chưa bắt đầu quá trình đặt hàng COD. Hãy gõ \"thanh toán COD\" để bắt đầu nhé!";
                        }
                    } else {
                        return "🛍️ Bạn cần đăng nhập để tiến hành thanh toán nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Hủy đơn hàng
                if ("cancel_order".equals(intent)) {
                    System.out.println("DEBUG - Processing cancel_order intent");
                    if (isLoggedIn && userId != null && userId > 0) {
                        // Kiểm tra xem có mã đơn hàng trong câu hỏi không
                        Matcher orderNumberMatcher = ORDER_NUMBER_PATTERN.matcher(userQuestion);
                        if (orderNumberMatcher.find()) {
                            String orderNumber = orderNumberMatcher.group(1);
                            System.out.println("DEBUG - Found order number: " + orderNumber);
                            
                            // Tìm đơn hàng theo mã đơn hàng
                            OrderDAOImpl orderDAO = new OrderDAOImpl();
                            Order order = orderDAO.getOrderByOrderNumber(orderNumber);
                            
                            if (order != null) {
                                return handleCancelOrder(userId, order.getOrderId(), context);
                            } else {
                                return "❌ Không tìm thấy đơn hàng với mã " + orderNumber + "! Vui lòng kiểm tra lại mã đơn hàng.";
                            }
                        }
                        // Trường hợp người dùng chỉ gõ "hủy" và có order_number trong context
                        else if (userQuestion.toLowerCase().equals("hủy") && context.containsKey("order_number")) {
                            String orderNumber = (String) context.get("order_number");
                            System.out.println("DEBUG - Using order number from context: " + orderNumber);
                            
                            OrderDAOImpl orderDAO = new OrderDAOImpl();
                            Order order = orderDAO.getOrderByOrderNumber(orderNumber);
                            
                            if (order != null) {
                                return handleCancelOrder(userId, order.getOrderId(), context);
                            } else {
                                return "❌ Không tìm thấy đơn hàng với mã " + orderNumber + "! Có thể đơn hàng đã bị hủy trước đó.";
                            }
                        }
                        // Kiểm tra xem có đơn hàng đang xử lý không
                        else if (context.containsKey("confirmed_order_id")) {
                            int orderId = (Integer) context.get("confirmed_order_id");
                            return handleCancelOrder(userId, orderId, context);
                        } else {
                            return "🤔 Hmm, có vẻ như bạn chưa có đơn hàng nào đang xử lý. Nếu bạn muốn hủy đơn hàng cụ thể, vui lòng cho tôi biết mã đơn hàng nhé!";
                        }
                    } else {
                        return "🛍️ Bạn cần đăng nhập để quản lý đơn hàng nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
                
                // Thêm vào giỏ hàng
                if ("add_to_cart".equals(intent)) {
                    String productName = (String) purchaseIntent.get("productName");
                    int quantity = (int) purchaseIntent.get("quantity");
                    
                    if (isLoggedIn && userId != null && userId > 0) {
                        return handleAddToCart(productName, quantity, userId, chatHistory);
                    } else {
                        return "🛒 Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng nhé! Hãy đăng nhập và thử lại nha 😊";
                    }
                }
            }
            
            // Xử lý bình thường nếu không phải ý định mua hàng
            String normalizedQuestion = Normalizer.normalize(userQuestion, Normalizer.Form.NFC).toLowerCase();
            List<String> allProductNames = productDAO.getAllProductNames();
            
            // Tìm sản phẩm được nhắc đến trong câu hỏi
            List<Product> mentionedProducts = new ArrayList<>();
            for (String productName : allProductNames) {
                String normalizedProductName = Normalizer.normalize(productName, Normalizer.Form.NFC).toLowerCase();
                List<String> searchTerms = new ArrayList<>();
                searchTerms.add(normalizedProductName);
                
                if (normalizedProductName.contains(" ")) {
                    String[] words = normalizedProductName.split(" ");
                    if (words.length >= 2) {
                        searchTerms.add(words[0] + " " + words[1]);
                    }
                }
                
                for (String term : searchTerms) {
                    if (normalizedQuestion.contains(term)) {
                        List<Product> foundProducts = productDAO.getProductsByName(productName);
                        mentionedProducts.addAll(foundProducts);
                        break;
                    }
                }
            }
            
            // Nếu tìm thấy sản phẩm, gọi API với thông tin sản phẩm
            if (!mentionedProducts.isEmpty()) {
                return callSmartProductResponseAI(userQuestion, mentionedProducts, chatHistory, context);
            }
            
            // Nếu không tìm thấy sản phẩm cụ thể, kiểm tra xem có phải câu hỏi về danh mục không
            if (normalizedQuestion.contains("cá cảnh") || 
                normalizedQuestion.contains("thức ăn") || 
                normalizedQuestion.contains("phụ kiện") ||
                normalizedQuestion.contains("bể cá") ||
                normalizedQuestion.contains("thuốc") ||
                normalizedQuestion.contains("cây thủy sinh")) {
                
                return handleCategoryQuestion(userQuestion, chatHistory, context);
            }
            
            // Mặc định, gọi API chung
            return callGenericVertexAI(userQuestion, chatHistory, context);
        } catch (Exception e) { 
            e.printStackTrace(); 
            return "Xin lỗi, đã có lỗi xảy ra khi xử lý yêu cầu của bạn: " + e.getMessage(); 
        }
    }

    /**
     * Phương thức tiện ích để gọi API với context rỗng
     */
    private String getAIResponse(String userQuestion, List<String> chatHistory, Map<String, Object> context) {
        // Tạo một session giả để tương thích với phương thức mới
        return getAIResponse(userQuestion, chatHistory, context, null);
    }

    /**
     * Phương thức tiện ích để gọi API với chatHistory và context rỗng
     */
    private String getAIResponse(String userQuestion, List<String> chatHistory) {
        Map<String, Object> emptyContext = new HashMap<>();
        return getAIResponse(userQuestion, chatHistory, emptyContext, null);
    }

    /**
     * Phát hiện ý định mua hàng từ câu hỏi của người dùng
     */
    private Map<String, Object> detectPurchaseIntent(String userQuestion) {
        Map<String, Object> result = new HashMap<>();
        
        // Kiểm tra ý định cấu hình tài khoản ngân hàng
        Matcher bankConfigMatcher = BANK_CONFIG_PATTERN.matcher(userQuestion);
        if (bankConfigMatcher.find()) {
            result.put("intent", "configure_bank");
            result.put("bank_name", bankConfigMatcher.group(1));
            result.put("account_number", bankConfigMatcher.group(2));
            result.put("account_name", bankConfigMatcher.group(3));
            return result;
        }
        
        // Kiểm tra ý định xem giỏ hàng
        if (userQuestion.toLowerCase().contains("xem giỏ hàng") || 
            userQuestion.toLowerCase().contains("giỏ hàng của tôi") ||
            userQuestion.toLowerCase().contains("trong giỏ hàng") ||
            userQuestion.toLowerCase().contains("giỏ hàng hiện tại")) {
            result.put("intent", "view_cart");
            return result;
        }
        
        // Kiểm tra ý định thanh toán
        if (userQuestion.toLowerCase().contains("thanh toán") || 
            userQuestion.toLowerCase().contains("đặt hàng") ||
            userQuestion.toLowerCase().contains("mua ngay") ||
            userQuestion.toLowerCase().contains("checkout")) {
            result.put("intent", "checkout");
            return result;
        }
        
        // Kiểm tra ý định thanh toán COD trực tiếp
        if (userQuestion.toLowerCase().contains("thanh toán cod") || 
            userQuestion.toLowerCase().contains("đặt hàng cod") ||
            userQuestion.toLowerCase().contains("đặt cod") ||
            userQuestion.toLowerCase().contains("thanh toán khi nhận hàng") ||
            userQuestion.toLowerCase().contains("đặt hàng và thanh toán")) {
            result.put("intent", "checkout_cod_direct");
            return result;
        }
        
        // Kiểm tra ý định thanh toán qua SePay
        if (userQuestion.toLowerCase().contains("thanh toán sepay") || 
            userQuestion.toLowerCase().contains("thanh toán qua sepay") ||
            userQuestion.toLowerCase().contains("thanh toán bằng sepay") ||
            userQuestion.toLowerCase().contains("thanh toán qr") ||
            userQuestion.toLowerCase().contains("thanh toán mã qr") ||
            userQuestion.toLowerCase().contains("thanh toán bằng qr") ||
            userQuestion.toLowerCase().equals("thanh toán") ||
            userQuestion.toLowerCase().contains("thanh toán chuyển khoản")) {
            result.put("intent", "checkout_sepay");
            return result;
        }
        
        // Kiểm tra ý định kiểm tra trạng thái thanh toán
        if (userQuestion.toLowerCase().contains("kiểm tra thanh toán") || 
            userQuestion.toLowerCase().contains("trạng thái thanh toán") ||
            userQuestion.toLowerCase().contains("đã thanh toán chưa") ||
            userQuestion.toLowerCase().contains("thanh toán xong") ||
            userQuestion.toLowerCase().contains("check thanh toán")) {
            result.put("intent", "check_payment_status");
            return result;
        }
        
        // Kiểm tra xác nhận đặt hàng COD
        if (userQuestion.toLowerCase().contains("xác nhận đặt cod") || 
            userQuestion.toLowerCase().contains("đồng ý đặt hàng cod") ||
            userQuestion.toLowerCase().contains("xác nhận thanh toán") ||
            userQuestion.toLowerCase().contains("xác nhận đặt hàng")) {
            result.put("intent", "confirm_cod_order");
            return result;
        }
        
        // Kiểm tra ý định hủy đơn hàng
        if (userQuestion.toLowerCase().contains("hủy đơn hàng") || 
            userQuestion.toLowerCase().contains("hủy đơn") ||
            userQuestion.toLowerCase().contains("cancel order") ||
            userQuestion.toLowerCase().contains("không muốn đặt nữa") ||
            userQuestion.toLowerCase().contains("không mua nữa") ||
            userQuestion.toLowerCase().equals("hủy")) {
            result.put("intent", "cancel_order");
            return result;
        }
        
        // Kiểm tra xem có ý định mua hàng không
        Matcher buyMatcher = BUY_PATTERN.matcher(userQuestion);
        if (buyMatcher.find()) {
            result.put("intent", "add_to_cart");
            
            // Tìm số lượng
            int quantity = 1; // Mặc định là 1
            Matcher quantityMatcher = QUANTITY_PATTERN.matcher(userQuestion);
            if (quantityMatcher.find()) {
                try {
                    quantity = Integer.parseInt(quantityMatcher.group(1));
                } catch (NumberFormatException e) {
                    // Giữ giá trị mặc định
                }
            }
            result.put("quantity", quantity);
            
            // Tìm tên sản phẩm
            String productName = null;
            Matcher productMatcher = PRODUCT_NAME_PATTERN.matcher(userQuestion);
            if (productMatcher.find()) {
                productName = productMatcher.group().trim();
            } else {
                // Nếu không tìm thấy theo pattern, thử tìm từ danh sách sản phẩm
                List<String> allProductNames = productDAO.getAllProductNames();
                for (String name : allProductNames) {
                    if (userQuestion.toLowerCase().contains(name.toLowerCase())) {
                        productName = name;
                        break;
                    }
                }
            }
            
            if (productName != null) {
                result.put("productName", productName);
                return result;
            }
        }
        
        return null;
    }

    /**
     * Xử lý thêm sản phẩm vào giỏ hàng
     */
    private String handleAddToCart(String productName, int quantity, int userId, List<String> chatHistory) {
        try {
            // Tìm sản phẩm theo tên
            List<Product> products = productDAO.getProductsByName(productName);
            if (products.isEmpty()) {
                return "😅 Xin lỗi bạn, mình không tìm thấy sản phẩm \"" + productName + "\" trong cửa hàng. Bạn có thể mô tả rõ hơn về sản phẩm không?";
            }
            Product product = products.get(0); // Lấy sản phẩm đầu tiên tìm thấy

            // Lấy hoặc tạo giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            // Thêm sản phẩm vào giỏ hàng
            boolean success = cartDAO.addItemToCartByUserId(userId, product.getProductId(), quantity);

            if (!success) {
                return "😓 Xin lỗi bạn, đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng. Bạn thử lại sau nhé!";
            }

            // Tạo phản hồi thân thiện
            StringBuilder response = new StringBuilder();
            response.append("🛒 Tuyệt vời! Mình đã thêm ");
            response.append(quantity).append(" ");
            response.append(product.getName());
            response.append(" vào giỏ hàng của bạn rồi nhé! 😊\n\n");
            response.append("💰 Thông tin sản phẩm:\n");
            response.append("- Tên: ").append(product.getName()).append("\n");
            response.append("- Giá: ").append(String.format("%,.0f", product.getPrice())).append(" VNĐ\n");
            response.append("- Số lượng: ").append(quantity).append("\n\n");
            response.append("✨ Bạn có thể tiếp tục mua sắm hoặc vào giỏ hàng để thanh toán nha!\n");
            response.append("👉 Bạn cần mình giúp gì thêm không?");
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi bạn, đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage();
        }
    }

    /**
     * Xử lý hiển thị giỏ hàng
     */
    private String handleViewCart(int userId) {
        try {
            // Lấy giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            // Nếu giỏ hàng trống
            if (cartItems == null || cartItems.isEmpty()) {
                return "🛒 Giỏ hàng của bạn đang trống rỗng! Hãy thêm vài sản phẩm vào giỏ hàng nhé 😊";
            }
            
            // Tạo phản hồi hiển thị giỏ hàng
            StringBuilder response = new StringBuilder();
            response.append("🛒 **Giỏ hàng của bạn:**\n\n");
            
            double total = 0.0;
            int itemCount = 0;
            
            for (CartItem item : cartItems) {
                double itemTotal = item.getProductPrice() * item.getQuantity();
                total += itemTotal;
                itemCount += item.getQuantity();
                
                response.append("• **").append(item.getProductName()).append("**\n");
                response.append("  - Số lượng: ").append(item.getQuantity()).append("\n");
                response.append("  - Giá: ").append(String.format("%,.0f", item.getProductPrice())).append(" VNĐ\n");
                response.append("  - Thành tiền: ").append(String.format("%,.0f", itemTotal)).append(" VNĐ\n\n");
            }
            
            response.append("�� **Tổng giỏ hàng:**\n");
            response.append("- Số lượng sản phẩm: ").append(itemCount).append("\n");
            response.append("- Tổng tiền: ").append(String.format("%,.0f", total)).append(" VNĐ\n\n");
            
            response.append("✨ **Các lựa chọn tiếp theo:**\n");
            response.append("- Gõ \"Thanh toán\" để tiến hành đặt hàng\n");
            response.append("- Gõ \"Tiếp tục mua sắm\" để xem thêm sản phẩm\n");
            
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi, đã xảy ra lỗi khi tải giỏ hàng của bạn: " + e.getMessage();
        }
    }

    /**
     * Xử lý thanh toán từ chat
     */
    private String handleCheckout(int userId) {
        try {
            // Lấy giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            // Nếu giỏ hàng trống
            if (cartItems == null || cartItems.isEmpty()) {
                return "🛒 Giỏ hàng của bạn đang trống rỗng! Bạn cần thêm sản phẩm trước khi thanh toán nhé 😊";
            }
            
            // Lấy thông tin người dùng và địa chỉ
            AddressDAO addressDAO = new AddressDAO();
            List<Address> addresses = addressDAO.getAddressesByUserId(userId);
            
            if (addresses == null || addresses.isEmpty()) {
                return "🏠 Bạn chưa có địa chỉ giao hàng! Vui lòng truy cập trang cá nhân để thêm địa chỉ giao hàng trước khi thanh toán.";
            }
            
            // Tạo link thanh toán
            String checkoutUrl = "/DuAnBanCaCanh/checkout";
            
            // Tạo phản hồi
            StringBuilder response = new StringBuilder();
            response.append("🛍️ **Thông tin thanh toán:**\n\n");
            
            // Hiển thị tổng quan giỏ hàng
            double total = 0.0;
            int itemCount = 0;
            
            for (CartItem item : cartItems) {
                double itemTotal = item.getProductPrice() * item.getQuantity();
                total += itemTotal;
                itemCount += item.getQuantity();
            }
            
            response.append("📊 **Tổng giỏ hàng:**\n");
            response.append("- Số lượng sản phẩm: ").append(itemCount).append("\n");
            response.append("- Tổng tiền hàng: ").append(String.format("%,.0f", total)).append(" VNĐ\n");
            response.append("- Phí vận chuyển: 20,000 VNĐ\n");
            response.append("- Thuế VAT (5%): ").append(String.format("%,.0f", total * 0.05)).append(" VNĐ\n");
            response.append("- **Tổng thanh toán: ").append(String.format("%,.0f", total + 20000 + (total * 0.05))).append(" VNĐ**\n\n");
            
            // Hiển thị địa chỉ giao hàng mặc định hoặc địa chỉ đầu tiên
            Address defaultAddress = addresses.stream()
                .filter(addr -> addr.isIsDefault())
                .findFirst()
                .orElse(addresses.get(0));
            
            response.append("🏠 **Địa chỉ giao hàng:**\n");
            response.append(defaultAddress.getRecipientName()).append("\n");
            response.append(defaultAddress.getPhone()).append("\n");
            response.append(defaultAddress.getAddressDetail()).append(", ");
            response.append(defaultAddress.getWard()).append(", ");
            response.append(defaultAddress.getDistrict()).append(", ");
            response.append(defaultAddress.getProvince()).append("\n\n");
            
            // Hướng dẫn tiếp theo
            response.append("✅ **Tiếp tục thanh toán:**\n");
            response.append("Vui lòng nhấp vào đường dẫn sau để hoàn tất đơn hàng:\n");
            response.append("👉 [Hoàn tất thanh toán](").append(checkoutUrl).append(")\n\n");
            response.append("Tại trang thanh toán, bạn có thể:\n");
            response.append("- Chọn địa chỉ giao hàng khác\n");
            response.append("- Chọn phương thức thanh toán\n");
            response.append("- Nhập mã giảm giá (nếu có)\n");
            response.append("- Xác nhận đơn hàng\n\n");
            response.append("*Lưu ý: Giỏ hàng sẽ được giữ nguyên cho đến khi bạn hoàn tất thanh toán.*");
            
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi, đã xảy ra lỗi khi chuẩn bị thanh toán: " + e.getMessage();
        }
    }

    /**
     * Xử lý thanh toán COD trực tiếp trong chat
     */
    private String handleDirectCODCheckout(int userId, Map<String, Object> context) {
        try {
            System.out.println("DEBUG - Starting handleDirectCODCheckout for userId: " + userId);
            
            // Lấy giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            System.out.println("DEBUG - Cart items count: " + (cartItems != null ? cartItems.size() : "null"));
            
            // Nếu giỏ hàng trống
            if (cartItems == null || cartItems.isEmpty()) {
                return "🛒 Giỏ hàng của bạn đang trống rỗng! Bạn cần thêm sản phẩm trước khi thanh toán nhé 😊";
            }
            
            // Lấy thông tin người dùng và địa chỉ
            AddressDAO addressDAO = new AddressDAO();
            List<Address> addresses = addressDAO.getAddressesByUserId(userId);
            
            System.out.println("DEBUG - Addresses count: " + (addresses != null ? addresses.size() : "null"));
            
            if (addresses == null || addresses.isEmpty()) {
                return "🏠 Bạn chưa có địa chỉ giao hàng! Vui lòng truy cập trang cá nhân để thêm địa chỉ giao hàng trước khi thanh toán.";
            }
            
            // Tính toán giá trị đơn hàng
            double total = 0.0;
            int itemCount = 0;
            
            for (CartItem item : cartItems) {
                double itemTotal = item.getProductPrice() * item.getQuantity();
                total += itemTotal;
                itemCount += item.getQuantity();
            }
            
            // Thuế và phí vận chuyển
            double tax = total * 0.05;
            double shippingFee = 20000;
            double finalAmount = total + tax + shippingFee;
            
            // Địa chỉ giao hàng mặc định hoặc đầu tiên
            Address defaultAddress = addresses.stream()
                .filter(addr -> addr.isIsDefault())
                .findFirst()
                .orElse(addresses.get(0));
            
            // Lưu thông tin vào context để sử dụng khi xác nhận
            context.put("pending_cod_checkout", Boolean.TRUE);
            context.put("selected_address_id", defaultAddress.getAddressId());
            context.put("cart_id", cart.getCartId());
            context.put("final_amount", finalAmount);
            
            System.out.println("DEBUG - Set context values: pending_cod_checkout=" + context.get("pending_cod_checkout"));
            System.out.println("DEBUG - Set context values: selected_address_id=" + context.get("selected_address_id"));
            
            // Tạo phản hồi
            StringBuilder response = new StringBuilder();
            response.append("🛍️ **Đặt hàng COD (Thanh toán khi nhận hàng)**\n\n");
            
            // Hiển thị tổng quan giỏ hàng
            response.append("📊 **Chi tiết giỏ hàng:**\n");
            for (CartItem item : cartItems) {
                double itemTotal = item.getProductPrice() * item.getQuantity();
                response.append("• **").append(item.getProductName()).append("** x").append(item.getQuantity());
                response.append(" - ").append(String.format("%,.0f", itemTotal)).append(" VNĐ\n");
            }
            response.append("\n");
            
            // Hiển thị tổng tiền
            response.append("📝 **Thông tin thanh toán:**\n");
            response.append("- Tổng tiền hàng: ").append(String.format("%,.0f", total)).append(" VNĐ\n");
            response.append("- Phí vận chuyển: ").append(String.format("%,.0f", shippingFee)).append(" VNĐ\n");
            response.append("- Thuế VAT (5%): ").append(String.format("%,.0f", tax)).append(" VNĐ\n");
            response.append("- **Tổng thanh toán: ").append(String.format("%,.0f", finalAmount)).append(" VNĐ**\n\n");
            
            // Hiển thị địa chỉ giao hàng
            response.append("🏠 **Địa chỉ giao hàng:**\n");
            response.append(defaultAddress.getRecipientName()).append("\n");
            response.append(defaultAddress.getPhone()).append("\n");
            response.append(defaultAddress.getAddressDetail()).append(", ");
            response.append(defaultAddress.getWard()).append(", ");
            response.append(defaultAddress.getDistrict()).append(", ");
            response.append(defaultAddress.getProvince()).append("\n\n");
            
            // Hướng dẫn xác nhận
            response.append("✅ **Xác nhận đơn hàng:**\n");
            response.append("- Gõ \"xác nhận đặt COD\" để hoàn tất đặt hàng\n");
            response.append("- Gõ \"thay đổi địa chỉ\" nếu muốn dùng địa chỉ khác\n");
            response.append("- Gõ \"hủy\" để hủy đơn hàng\n\n");
            response.append("*Lưu ý: Đơn hàng sẽ được xử lý với phương thức thanh toán COD (Thanh toán khi nhận hàng)*");
            
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi, đã xảy ra lỗi khi chuẩn bị thanh toán: " + e.getMessage();
        }
    }

    /**
     * Xác nhận và xử lý đặt hàng COD
     */
    private String handleConfirmCODOrder(int userId, int addressId, Map<String, Object> context) {
        try {
            // Lấy giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            
            // Tạo đơn hàng
            OrderDAOImpl orderDAO = new OrderDAOImpl();
            
            // Tạo số đơn hàng
            String orderNumber = orderDAO.generateOrderNumber();
            String notes = "Đơn hàng được tạo qua AI chat";
            
            // Thực hiện tạo đơn hàng
            int orderId = orderDAO.createOrder(
                userId, 
                cart, 
                orderNumber, 
                addressId, 
                "cod", // Phương thức thanh toán COD
                notes, 
                BigDecimal.ZERO // Không có giảm giá
            );
            
            // Lưu thông tin đơn hàng vào context để có thể hủy sau này
            context.put("confirmed_order_id", orderId);
            context.put("order_number", orderNumber);
            
            // Xóa thông tin thanh toán COD đang chờ
            context.remove("pending_cod_checkout");
            context.remove("selected_address_id");
            context.remove("cart_id");
            context.remove("final_amount");
            
            // Tạo phản hồi
            StringBuilder response = new StringBuilder();
            response.append("✅ **Đặt hàng thành công!**\n\n");
            response.append("🎉 Cảm ơn bạn đã đặt hàng! Đơn hàng của bạn đã được tạo thành công.\n\n");
            
            response.append("📦 **Thông tin đơn hàng:**\n");
            response.append("- Mã đơn hàng: ").append(orderNumber).append("\n");
            response.append("- Phương thức thanh toán: Thanh toán khi nhận hàng (COD)\n\n");
            
            response.append("🔔 **Tiếp theo:**\n");
            response.append("- Đơn hàng của bạn sẽ được xử lý và giao trong vòng 2-3 ngày làm việc\n");
            response.append("- Bạn có thể theo dõi đơn hàng trong mục \"Đơn hàng của tôi\" trên website\n");
            response.append("- Bạn sẽ nhận được thông báo khi đơn hàng được giao\n\n");
            
            response.append("🛍️ Chúc bạn có trải nghiệm mua sắm tuyệt vời với shop chúng mình!");
            
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi, đã xảy ra lỗi khi xác nhận đơn hàng: " + e.getMessage();
        }
    }

    /**
     * Xử lý yêu cầu hủy đơn hàng
     */
    private String handleCancelOrder(int userId, int orderId, Map<String, Object> context) {
        try {
            System.out.println("DEBUG - handleCancelOrder - userId: " + userId + ", orderId: " + orderId);
            
            // Tạo đối tượng OrderDAO để truy vấn và cập nhật đơn hàng
            OrderDAOImpl orderDAO = new OrderDAOImpl();
            
            // Kiểm tra xem đơn hàng có tồn tại và thuộc về người dùng không
            Order order = orderDAO.getOrderById(orderId);
            System.out.println("DEBUG - handleCancelOrder - order found: " + (order != null));
            
            if (order == null) {
                return "❌ Không tìm thấy đơn hàng! Có thể đơn hàng đã bị hủy hoặc không tồn tại.";
            }
            
            System.out.println("DEBUG - handleCancelOrder - order userId: " + order.getUserId() + ", status: " + order.getStatus());
            
            if (order.getUserId() != userId) {
                return "⚠️ Đơn hàng này không thuộc về bạn!";
            }
            
            // Kiểm tra trạng thái đơn hàng
            if (order.getStatus().equalsIgnoreCase("cancelled")) {
                return "ℹ️ Đơn hàng này đã được hủy trước đó rồi!";
            }
            
            if (order.getStatus().equalsIgnoreCase("completed") || 
                order.getStatus().equalsIgnoreCase("shipped")) {
                return "❌ Không thể hủy đơn hàng đã hoàn thành hoặc đã giao!";
            }
            
            // Hủy đơn hàng
            boolean success = orderDAO.updateOrderStatus(orderId, "cancelled");
            System.out.println("DEBUG - handleCancelOrder - updateOrderStatus result: " + success);
            
            if (success) {
                // Xóa thông tin đơn hàng khỏi context
                context.remove("confirmed_order_id");
                context.remove("order_number");
                
                StringBuilder response = new StringBuilder();
                response.append("✅ **Đơn hàng đã được hủy thành công!**\n\n");
                response.append("📝 **Thông tin đơn hàng:**\n");
                response.append("- Mã đơn hàng: ").append(order.getOrderNumber()).append("\n");
                response.append("- Ngày đặt: ").append(order.getCreatedAt()).append("\n");
                response.append("- Tổng tiền: ").append(String.format("%,.0f", order.getTotalAmount())).append(" VNĐ\n\n");
                
                response.append("💬 Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi! Nếu bạn có bất kỳ câu hỏi nào khác, đừng ngần ngại hỏi nhé!\n\n");
                response.append("🛒 Bạn có thể tiếp tục mua sắm hoặc xem các sản phẩm khác của chúng tôi.");
                
                return response.toString();
            } else {
                return "❌ Có lỗi xảy ra khi hủy đơn hàng! Vui lòng thử lại sau hoặc liên hệ với bộ phận hỗ trợ khách hàng.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi, đã xảy ra lỗi khi hủy đơn hàng: " + e.getMessage();
        }
    }

    /**
     * Xử lý câu hỏi về danh mục sản phẩm
     */
    private String handleCategoryQuestion(String userQuestion, List<String> chatHistory, Map<String, Object> context) throws Exception {
        int categoryId = -1;
        String categoryName = "";
        
        // Lấy thông tin người dùng từ context
        Integer userId = (Integer) context.get("userId");
        boolean isLoggedIn = context.containsKey("isLoggedIn") ? (Boolean) context.get("isLoggedIn") : false;
        
        // Xác định danh mục từ câu hỏi
        if (userQuestion.toLowerCase().contains("cá cảnh")) {
            categoryId = 1; // Giả sử ID 1 là danh mục cá cảnh
            categoryName = "cá cảnh";
        } else if (userQuestion.toLowerCase().contains("thức ăn")) {
            categoryId = 2; // Giả sử ID 2 là danh mục thức ăn
            categoryName = "thức ăn";
        } else if (userQuestion.toLowerCase().contains("phụ kiện")) {
            categoryId = 3; // Giả sử ID 3 là danh mục phụ kiện
            categoryName = "phụ kiện";
        } else if (userQuestion.toLowerCase().contains("bể cá")) {
            categoryId = 4; // Giả sử ID 4 là danh mục bể cá
            categoryName = "bể cá";
        } else if (userQuestion.toLowerCase().contains("thuốc")) {
            categoryId = 5; // Giả sử ID 5 là danh mục thuốc
            categoryName = "thuốc";
        } else if (userQuestion.toLowerCase().contains("cây thủy sinh")) {
            categoryId = 6; // Giả sử ID 6 là danh mục cây thủy sinh
            categoryName = "cây thủy sinh";
        }
        
        if (categoryId > 0) {
            // Lấy danh sách sản phẩm theo danh mục
            List<Product> products = productDAO.getProductsByCategoryId(categoryId);
            if (!products.isEmpty()) {
                // Giới hạn số lượng sản phẩm hiển thị
                List<Product> topProducts = products.size() > 5 ? products.subList(0, 5) : products;
                
        StringBuilder promptBuilder = new StringBuilder();
                promptBuilder.append("Bối cảnh: Bạn là một nhân viên tư vấn nhiệt tình, vui vẻ và thân thiện của một cửa hàng cá cảnh. ");
                promptBuilder.append("Nhiệm vụ của bạn là giới thiệu danh mục sản phẩm ").append(categoryName).append(" cho khách hàng một cách thân thiện, gần gũi và hài hước nhẹ nhàng. ");
                promptBuilder.append("Hãy giới thiệu tổng quan về danh mục và liệt kê 5 sản phẩm nổi bật kèm giá. Trả lời bằng tiếng Việt.\n\n");
                
                // Thêm hướng dẫn về phong cách trò chuyện
                promptBuilder.append("Phong cách trò chuyện:\n");
                promptBuilder.append("1. Sử dụng emoji phù hợp (như 🐠, 😊, 👋, 💙, 🌟) để làm trò chuyện sinh động\n");
                promptBuilder.append("2. Sử dụng ngôn ngữ thân thiện, gần gũi như đang nói chuyện với bạn bè\n");
                promptBuilder.append("3. Thỉnh thoảng sử dụng từ ngữ thân mật như 'bạn ơi', 'nè', 'nhé', 'nha'\n");
                promptBuilder.append("4. Tránh cách nói cứng nhắc, hãy nói chuyện tự nhiên\n");
                promptBuilder.append("5. Thể hiện sự nhiệt tình và vui vẻ\n\n");
                
                promptBuilder.append("--- Thông tin danh mục ").append(categoryName).append(" ---\n");
                promptBuilder.append("Số lượng sản phẩm: ").append(products.size()).append("\n");
                promptBuilder.append("Các sản phẩm nổi bật:\n");
                
                for (Product p : topProducts) {
                    promptBuilder.append("- Tên: ").append(p.getName()).append("\n");
                    promptBuilder.append("  Giá: ").append(String.format("%,.0f", p.getPrice())).append(" VNĐ\n");
                    if (p.getShortDescription() != null && !p.getShortDescription().isEmpty()) {
                        promptBuilder.append("  Mô tả: ").append(p.getShortDescription()).append("\n");
                    }
                    promptBuilder.append("\n");
                }
                
                promptBuilder.append("--- Câu hỏi của khách hàng ---\n");
                promptBuilder.append(userQuestion);
                
                String modelName = "gemini-2.5-flash";
                GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
                GenerateContentResponse response = model.generateContent(promptBuilder.toString());
                
                return ResponseHandler.getText(response);
            }
        }
        
        // Nếu không tìm thấy danh mục hoặc không có sản phẩm, gọi API chung
        return callGenericVertexAI(userQuestion, chatHistory, context);
    }

    private String callSmartProductResponseAI(String userQuestion, List<Product> products, List<String> history, Map<String, Object> context) throws Exception {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Bối cảnh: Bạn là một nhân viên tư vấn nhiệt tình, vui vẻ và thân thiện của một cửa hàng cá cảnh. ");
        promptBuilder.append("Nhiệm vụ của bạn là trả lời câu hỏi của khách hàng một cách thân thiện, gần gũi và hài hước nhẹ nhàng, dựa trên thông tin sản phẩm được cung cấp và lịch sử trò chuyện. ");
        promptBuilder.append("Không tự bịa thêm thông tin không có trong danh sách. Trả lời bằng tiếng Việt.\n\n");
        
        // Thêm hướng dẫn về phong cách trò chuyện
        promptBuilder.append("Phong cách trò chuyện:\n");
        promptBuilder.append("1. Sử dụng emoji phù hợp (như 🐠, 😊, 👋, 💙, 🌟) để làm trò chuyện sinh động\n");
        promptBuilder.append("2. Sử dụng ngôn ngữ thân thiện, gần gũi như đang nói chuyện với bạn bè\n");
        promptBuilder.append("3. Thỉnh thoảng sử dụng từ ngữ thân mật như 'bạn ơi', 'nè', 'nhé', 'nha'\n");
        promptBuilder.append("4. Tránh cách nói cứng nhắc, hãy nói chuyện tự nhiên\n");
        promptBuilder.append("5. Thể hiện sự nhiệt tình và vui vẻ\n\n");
        
        // Thêm hướng dẫn về mua hàng
        promptBuilder.append("Hướng dẫn về mua hàng:\n");
        promptBuilder.append("1. Gợi ý khách hàng có thể mua sản phẩm bằng cách nhắn \"Mua [số lượng] [tên sản phẩm]\"\n");
        promptBuilder.append("2. Nếu khách hỏi về cách mua hàng, hướng dẫn họ có thể mua trực tiếp qua chat\n");
        promptBuilder.append("3. Nhắc nhở khách hàng rằng họ cần đăng nhập để mua hàng\n\n");
        
        promptBuilder.append("--- Thông tin sản phẩm liên quan ---\n");
        for (Product p : products) {
            promptBuilder.append("Tên: ").append(p.getName()).append("\n");
            promptBuilder.append("Giá: ").append(String.format("%,.0f", p.getPrice())).append(" VNĐ\n");
            if (p.getShortDescription() != null && !p.getShortDescription().isEmpty()) {
                promptBuilder.append("Mô tả: ").append(p.getShortDescription()).append("\n");
            }
            promptBuilder.append("---\n");
        }
        promptBuilder.append("\n--- Lịch sử trò chuyện ---\n");
        for (String msg : history) {
            promptBuilder.append(msg).append("\n");
        }
        promptBuilder.append("\n--- Câu hỏi mới của khách hàng ---\n");
        promptBuilder.append(userQuestion);

        String modelName = "gemini-2.5-flash";
        
        GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
        GenerateContentResponse response = model.generateContent(promptBuilder.toString());

        return ResponseHandler.getText(response);
    }
    
    private String callGenericVertexAI(String userQuestion, List<String> history, Map<String, Object> context) throws Exception {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Bối cảnh: Bạn là trợ lý AI vui vẻ, nhiệt tình và thân thiện của một cửa hàng cá cảnh. ");
        promptBuilder.append("Nhiệm vụ của bạn là trả lời câu hỏi của khách hàng một cách thân thiện, gần gũi và hài hước nhẹ nhàng. ");
        promptBuilder.append("Nếu bạn không biết câu trả lời, hãy nói rằng bạn sẽ kiểm tra lại với nhân viên cửa hàng. Trả lời bằng tiếng Việt.\n\n");
        
        // Thêm hướng dẫn về phong cách trò chuyện
        promptBuilder.append("Phong cách trò chuyện:\n");
        promptBuilder.append("1. Sử dụng emoji phù hợp (như 🐠, 😊, 👋, 💙, 🌟) để làm trò chuyện sinh động\n");
        promptBuilder.append("2. Sử dụng ngôn ngữ thân thiện, gần gũi như đang nói chuyện với bạn bè\n");
        promptBuilder.append("3. Thỉnh thoảng sử dụng từ ngữ thân mật như 'bạn ơi', 'nè', 'nhé', 'nha'\n");
        promptBuilder.append("4. Tránh cách nói cứng nhắc, hãy nói chuyện tự nhiên\n");
        promptBuilder.append("5. Thể hiện sự nhiệt tình và vui vẻ\n\n");
        
        // Thêm hướng dẫn về mua hàng
        promptBuilder.append("Hướng dẫn về mua hàng:\n");
        promptBuilder.append("1. Gợi ý khách hàng có thể mua sản phẩm bằng cách nhắn \"Mua [số lượng] [tên sản phẩm]\"\n");
        promptBuilder.append("2. Nếu khách hỏi về cách mua hàng, hướng dẫn họ có thể mua trực tiếp qua chat\n");
        promptBuilder.append("3. Nhắc nhở khách hàng rằng họ cần đăng nhập để mua hàng\n\n");
        
        promptBuilder.append("--- Lịch sử trò chuyện ---\n");
        for (String msg : history) {
            promptBuilder.append(msg).append("\n");
        }
        promptBuilder.append("\n--- Câu hỏi mới của khách hàng ---\n");
        promptBuilder.append(userQuestion);

        String modelName = "gemini-2.5-flash";
        
        GenerativeModel model = new GenerativeModel(modelName, this.vertexAIClient);
        GenerateContentResponse response = model.generateContent(promptBuilder.toString());
        
        return ResponseHandler.getText(response);
    }
    
    /**
     * Tích hợp với SePay API
     */
    private static class SePayApiClient {
        private static final String SEPAY_API_BASE_URL = "https://api.sepay.vn/v1"; 
        private String apiKey;
        private String secretKey;
        private String webhookUrl;
        
        public SePayApiClient(String apiKey, String secretKey, String webhookUrl) {
            this.apiKey = apiKey;
            this.secretKey = secretKey;
            this.webhookUrl = webhookUrl;
        }
        
        /**
         * Tạo yêu cầu thanh toán qua SePay
         */
        public Map<String, Object> createPaymentRequest(String orderNumber, double amount, String description) {
            try {
                // Chuẩn bị dữ liệu gửi đến SePay API
                Map<String, Object> requestData = new HashMap<>();
                requestData.put("order_id", orderNumber);
                requestData.put("amount", amount);
                requestData.put("description", description);
                requestData.put("webhook_url", webhookUrl);
                
                System.out.println("DEBUG - Calling SePay API with data: " + requestData);
                System.out.println("DEBUG - Using webhook URL: " + webhookUrl);
                
                // Trong môi trường phát triển, sử dụng VietQR trực tiếp
                // TODO: Thay thế bằng HTTP client thực tế khi tích hợp với SePay
                
                // Tạo URL VietQR
                String bankName = SePayConfig.getDefaultBankName().toLowerCase();
                String accountNumber = SePayConfig.getDefaultAccountNumber();
                String formattedAmount = String.valueOf((int)amount);
                String accountName = SePayConfig.getDefaultAccountName().replace(" ", "%20");
                
                String vietQrUrl = "https://img.vietqr.io/image/" + bankName + 
                                   "-" + accountNumber + 
                                   "-compact.jpg?amount=" + formattedAmount + 
                                   "&addInfo=" + orderNumber + 
                                   "&accountName=" + accountName;
                
                System.out.println("DEBUG - Generated VietQR URL: " + vietQrUrl);
                
                // Phản hồi
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("order_id", orderNumber); // Quan trọng: Trả về đúng orderNumber đã gửi
                response.put("amount", amount);
                response.put("qr_code", vietQrUrl);
                
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("DEBUG - Error creating payment request: " + e.getMessage());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", e.getMessage());
                return errorResponse;
            }
        }
        
        /**
         * Kiểm tra trạng thái thanh toán
         */
        public Map<String, Object> checkPaymentStatus(String orderNumber) {
            try {
                System.out.println("DEBUG - Checking payment status for order: " + orderNumber);
                
                // TODO: Thực hiện HTTP request đến SePay API để kiểm tra trạng thái
                // Trong môi trường phát triển, luôn trả về completed sau khi người dùng kiểm tra
                
                // Phản hồi
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("order_id", orderNumber);
                response.put("status", "completed"); // Giả định là đã hoàn thành khi người dùng kiểm tra
                response.put("transaction_id", "TX" + System.currentTimeMillis());
                
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("DEBUG - Error checking payment status: " + e.getMessage());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("error", e.getMessage());
                return errorResponse;
            }
        }
    }
    
    /**
     * Xử lý thanh toán qua SePay bằng mã QR
     */
    private String handleSePayQRPayment(int userId, Map<String, Object> context) {
        try {
            System.out.println("DEBUG - Starting handleSePayQRPayment for userId: " + userId);
            
            // Tạo SePayApiClient mới với URL webhook mới nhất
            String webhookUrl = SePayConfig.getWebhookUrl();
            System.out.println("DEBUG - Using webhook URL from config: " + webhookUrl);
            
            SePayApiClient sePayApiClient = new SePayApiClient(
                SePayConfig.getApiKey(),
                SePayConfig.getSecretKey(),
                webhookUrl
            );
            System.out.println("DEBUG - Created new SePayApiClient with webhook URL: " + webhookUrl);
            
            // Lấy giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(userId);
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            System.out.println("DEBUG - Cart items count: " + (cartItems != null ? cartItems.size() : "null"));
            
            if (cartItems.isEmpty()) {
                return "🛒 Giỏ hàng của bạn đang trống! Hãy thêm sản phẩm vào giỏ hàng trước khi thanh toán nhé.";
            }
            
            // Lấy địa chỉ mặc định
            Address defaultAddress = addressDAO.getDefaultAddress(userId);
            System.out.println("DEBUG - Default address: " + (defaultAddress != null ? defaultAddress.getAddressId() : "null"));
            
            // Kiểm tra xem có địa chỉ mặc định không
            if (defaultAddress == null) {
                // Kiểm tra xem có địa chỉ nào không
                List<Address> addresses = addressDAO.getAddressesByUserId(userId);
                
                if (addresses != null && !addresses.isEmpty()) {
                    // Sử dụng địa chỉ đầu tiên
                    defaultAddress = addresses.get(0);
                    System.out.println("DEBUG - Using first address: " + defaultAddress.getAddressId());
                } else {
                    // Nếu không có địa chỉ nào, hướng dẫn người dùng thêm địa chỉ
                    StringBuilder response = new StringBuilder();
                    response.append("📝 **Cần thêm địa chỉ giao hàng**\n\n");
                    response.append("Bạn chưa có địa chỉ giao hàng nào. Vui lòng thêm địa chỉ giao hàng trước khi thanh toán.\n\n");
                    response.append("Bạn có thể thêm địa chỉ bằng cách:\n");
                    response.append("1. Truy cập trang [Tài khoản của tôi](/DuAnBanCaCanh/client/account/dashboard.jsp)\n");
                    response.append("2. Chọn mục \"Địa chỉ giao hàng\"\n");
                    response.append("3. Nhấn \"Thêm địa chỉ mới\"\n\n");
                    response.append("Sau khi thêm địa chỉ, bạn có thể quay lại đây và tiếp tục thanh toán.");
                    
                    return response.toString();
                }
            }
            
            // Tính tổng tiền
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getProductPrice() * item.getQuantity();
            }
            
            // Phí vận chuyển và thuế
            double shippingFee = 20000; // 20,000 VNĐ
            double tax = total * 0.05; // 5% VAT
            double finalAmount = total + shippingFee + tax;
            
            System.out.println("DEBUG - Order total: " + finalAmount);
            
            // Tạo mã đơn hàng tạm thời
            String tempOrderNumber = "SE" + System.currentTimeMillis();
            System.out.println("DEBUG - Generated temp order number: " + tempOrderNumber);
            
            // Mô tả đơn hàng
            StringBuilder orderDescription = new StringBuilder("Thanh toán đơn hàng " + tempOrderNumber);
            
            // Gọi API SePay để tạo yêu cầu thanh toán
            System.out.println("DEBUG - Calling SePay API to create payment request");
            Map<String, Object> paymentRequest = sePayApiClient.createPaymentRequest(
                tempOrderNumber, 
                finalAmount, 
                orderDescription.toString()
            );
            
            System.out.println("DEBUG - SePay API response: " + paymentRequest);
            
            if (!(Boolean)paymentRequest.get("success")) {
                return "❌ Có lỗi xảy ra khi tạo yêu cầu thanh toán: " + paymentRequest.get("error");
            }
            
            // Lưu thông tin vào context để theo dõi
            context.put("pending_sepay_checkout", true);
            context.put("temp_order_number", tempOrderNumber);
            context.put("selected_address_id", defaultAddress.getAddressId());
            context.put("cart_id", cart.getCartId());
            context.put("final_amount", finalAmount);
            
            System.out.println("DEBUG - Context updated with SePay checkout info");
            
            // Tạo đơn hàng tạm thời trong database
            OrderDAOImpl orderDAO = new OrderDAOImpl();
            String notes = "Đơn hàng tạm thời cho thanh toán qua SePay, chờ xác nhận thanh toán";
            int tempOrderId = orderDAO.createTemporaryOrder(
                userId, 
                cart.getCartId(), 
                tempOrderNumber, 
                defaultAddress.getAddressId(), 
                notes, 
                new BigDecimal(finalAmount)
            );
            
            System.out.println("DEBUG - Created temporary order with ID: " + tempOrderId);
            
            if (tempOrderId > 0) {
                context.put("temp_order_id", tempOrderId);
            }
            
            // Lấy URL QR code
            String qrCodeUrl = (String) paymentRequest.get("qr_code");
            
            // Tạo phản hồi
            StringBuilder response = new StringBuilder();
            response.append("🛍️ **Thanh toán qua SePay**\n\n");
            
            // Thông tin giỏ hàng
            response.append("📊 **Chi tiết giỏ hàng:**\n");
            for (CartItem item : cartItems) {
                Product product = productDAO.getProductById(item.getProductId());
                response.append("• ").append(product.getName())
                       .append(" x").append(item.getQuantity())
                       .append(" - ").append(String.format("%,.0f", item.getProductPrice() * item.getQuantity())).append(" VNĐ\n");
            }
            response.append("\n");
            
            // Thông tin thanh toán
            response.append("📝 **Thông tin thanh toán:**\n");
            response.append("- Tổng tiền hàng: ").append(String.format("%,.0f", total)).append(" VNĐ\n");
            response.append("- Phí vận chuyển: ").append(String.format("%,.0f", shippingFee)).append(" VNĐ\n");
            response.append("- Thuế VAT (5%): ").append(String.format("%,.0f", tax)).append(" VNĐ\n");
            response.append("- Tổng thanh toán: ").append(String.format("%,.0f", finalAmount)).append(" VNĐ\n\n");
            
            // Thông tin địa chỉ
            response.append("🏠 **Địa chỉ giao hàng:**\n");
            response.append(defaultAddress.getRecipientName()).append("\n");
            response.append(defaultAddress.getPhone()).append("\n");
            response.append(defaultAddress.getAddressDetail()).append(", ").append(defaultAddress.getWard())
                   .append(", ").append(defaultAddress.getDistrict()).append(", ").append(defaultAddress.getProvince()).append("\n\n");
            
            // Thông tin chuyển khoản
            response.append("🏦 **Thông tin chuyển khoản:**\n");
            response.append("- Ngân hàng: ").append(SePayConfig.getDefaultBankName()).append("\n");
            response.append("- Số tài khoản: ").append(SePayConfig.getDefaultAccountNumber()).append("\n");
            response.append("- Tên tài khoản: ").append(SePayConfig.getDefaultAccountName()).append("\n");
            response.append("- Số tiền: ").append(String.format("%,.0f", finalAmount)).append(" VNĐ\n");
            response.append("- Nội dung chuyển khoản: ").append(tempOrderNumber).append("\n\n");
            
            // Thông tin thanh toán QR
            response.append("📱 **Thanh toán qua mã QR:**\n");
            response.append("1. Mở ứng dụng ngân hàng của bạn\n");
            response.append("2. Quét mã QR bên dưới hoặc chọn chức năng quét VietQR\n");
            response.append("3. Kiểm tra thông tin và xác nhận thanh toán\n\n");
            
            // Hiển thị QR Code bằng HTML thay vì Markdown
            response.append("<img src=\"").append(qrCodeUrl).append("\" alt=\"Mã QR thanh toán\" width=\"200\" height=\"200\">\n\n");
            
            
            response.append("✅ Sau khi thanh toán, hệ thống sẽ tự động cập nhật trạng thái đơn hàng của bạn thông qua webhook.\n");
            response.append("❓ Gõ \"kiểm tra thanh toán\" để kiểm tra trạng thái thanh toán.\n");
            response.append("❌ Gõ \"hủy\" để hủy đơn hàng.\n\n");
            
            // Thêm nút kiểm tra thanh toán nổi bật hơn
            response.append("🔄 **SAU KHI THANH TOÁN XONG, VUI LÒNG GÕ \"KIỂM TRA THANH TOÁN\" ĐỂ XÁC NHẬN GIAO DỊCH**\n\n");
            
            // Thêm thông báo về việc tự động kiểm tra
            response.append("⏱️ *Hệ thống sẽ tự động kiểm tra trạng thái thanh toán sau 30 giây. Bạn có thể đợi hoặc gõ \"kiểm tra thanh toán\" sau khi hoàn tất thanh toán.*");
            
            // Tạo một thread để tự động kiểm tra trạng thái thanh toán sau 30 giây
            final String tempOrderNumberFinal = tempOrderNumber;
            new Thread(() -> {
                try {
                    // Đợi 30 giây
                    Thread.sleep(30000);
                    
                    System.out.println("DEBUG - Auto-check starting for order " + tempOrderNumberFinal);
                    
                    // Kiểm tra xem đơn hàng đã được thanh toán chưa
                    OrderDAOImpl orderDAOCheck = new OrderDAOImpl();
                    Order order = orderDAOCheck.getOrderByOrderNumber(tempOrderNumberFinal);
                    
                    if (order != null && "paid".equals(order.getPaymentStatus())) {
                        System.out.println("DEBUG - Auto-check: Payment already processed for order " + tempOrderNumberFinal);
                        return;
                    }
                    
                    // Tạo SePayApiClient mới với URL webhook mới nhất
                    SePayApiClient sePayApiClientCheck = new SePayApiClient(
                        SePayConfig.getApiKey(),
                        SePayConfig.getSecretKey(),
                        SePayConfig.getWebhookUrl()
                    );
                    System.out.println("DEBUG - Created new SePayApiClient with webhook URL: " + SePayConfig.getWebhookUrl());
                    
                    // Gọi API SePay để kiểm tra trạng thái thanh toán
                    Map<String, Object> paymentStatus = sePayApiClientCheck.checkPaymentStatus(tempOrderNumberFinal);
                    System.out.println("DEBUG - Auto-check payment status: " + paymentStatus);
                    
                    if ((Boolean)paymentStatus.get("success")) {
                        String status = (String) paymentStatus.get("status");
                        
                        if ("completed".equals(status) || "success".equals(status)) {
                            System.out.println("DEBUG - Auto-check: Payment completed for order " + tempOrderNumberFinal);
                            
                            // Tạo đơn hàng
                            int cartId = (Integer) context.get("cart_id");
                            int addressId = (Integer) context.get("selected_address_id");
                            
                            // Tạo số đơn hàng chính thức
                            String orderNumber = orderDAOCheck.generateOrderNumber();
                            String orderNotes = "Đơn hàng được thanh toán qua SePay (tự động kiểm tra), mã đơn hàng tạm thời: " + tempOrderNumberFinal;
                            
                            // Thực hiện tạo đơn hàng
                            int orderId = orderDAOCheck.createOrder(
                                userId, 
                                cartId, 
                                orderNumber, 
                                addressId, 
                                "bank_transfer", // Thay đổi từ "sepay" thành "bank_transfer"
                                orderNotes, 
                                BigDecimal.ZERO // Không có giảm giá
                            );
                            
                            // Cập nhật trạng thái đơn hàng thành đã thanh toán
                            orderDAOCheck.updatePaymentStatus(orderId, "paid");
                            orderDAOCheck.updateOrderStatus(orderId, "processing");
                            
                            System.out.println("DEBUG - Auto-check: Order created and marked as paid: " + orderNumber);
                            
                            // Cập nhật context
                            context.remove("pending_sepay_checkout");
                            context.remove("temp_order_number");
                            context.remove("selected_address_id");
                            context.remove("cart_id"); // Xóa cart_id để làm trống giỏ hàng
                            context.remove("final_amount");
                            context.put("confirmed_order_id", orderId);
                            context.put("order_number", orderNumber);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG - Auto-check error: " + e.getMessage());
                }
            }).start();
            
            return response.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "😓 Xin lỗi, đã xảy ra lỗi khi tạo yêu cầu thanh toán: " + e.getMessage();
        }
    }
    
    /**
     * Xử lý kiểm tra trạng thái thanh toán SePay
     */
    private String handleCheckSePayStatus(int userId, Map<String, Object> context) {
        try {
            System.out.println("DEBUG - Starting handleCheckSePayStatus for userId: " + userId);
            System.out.println("DEBUG - Context: " + context);
            
            // Tạo SePayApiClient mới với URL webhook mới nhất
            String webhookUrl = SePayConfig.getWebhookUrl();
            System.out.println("DEBUG - Using webhook URL from config: " + webhookUrl);
            
            SePayApiClient sePayApiClient = new SePayApiClient(
                SePayConfig.getApiKey(),
                SePayConfig.getSecretKey(),
                webhookUrl
            );
            System.out.println("DEBUG - Created new SePayApiClient with webhook URL: " + webhookUrl);
            
            // Kiểm tra xem có đang trong quá trình thanh toán SePay không
            if (!context.containsKey("pending_sepay_checkout") || 
                !Boolean.TRUE.equals(context.get("pending_sepay_checkout")) ||
                !context.containsKey("temp_order_number")) {
                return "❓ Bạn chưa bắt đầu quá trình thanh toán qua SePay. Hãy gõ \"thanh toán qua SePay\" để bắt đầu.";
            }
            
            String tempOrderNumber = (String) context.get("temp_order_number");
            System.out.println("DEBUG - Checking payment status for temp order number: " + tempOrderNumber);
            
            // Kiểm tra trạng thái đơn hàng trong cơ sở dữ liệu trước
            OrderDAOImpl orderDAO = new OrderDAOImpl();
            Order existingOrder = orderDAO.getOrderByOrderNumber(tempOrderNumber);
            System.out.println("DEBUG - getOrderByOrderNumber - searching for: " + tempOrderNumber);
            System.out.println("DEBUG - getOrderByOrderNumber - result: " + (existingOrder != null ? existingOrder.getOrderId() : "null"));
            
            if (existingOrder != null) {
                System.out.println("DEBUG - Existing order found with status: " + existingOrder.getStatus());
                
                // Nếu đơn hàng đã được thanh toán
                if ("paid".equals(existingOrder.getPaymentStatus())) {
                    context.remove("pending_sepay_checkout");
                    context.remove("temp_order_number");
                    context.remove("selected_address_id");
                    context.remove("final_amount");
                    
                    // Xóa giỏ hàng
                    try {
                        CartDAO cartDAO = new CartDAO();
                        Cart userCart = cartDAO.getOrCreateCartByUserId(userId);
                        if (userCart != null) {
                            // Xóa các item trong giỏ hàng
                            boolean clearCartResult = cartDAO.clearCartItems(userCart.getCartId());
                            System.out.println("DEBUG - Clear cart items for user " + userId + ": " + clearCartResult);
                            
                            // Xóa cart_id từ context
                            context.remove("cart_id");
                        }
                    } catch (Exception e) {
                        System.out.println("DEBUG - Error clearing cart: " + e.getMessage());
                    }
                    
                    // Lưu thông tin đơn hàng vào context
                    context.put("confirmed_order_id", existingOrder.getOrderId());
                    context.put("order_number", existingOrder.getOrderNumber());
                    
                    return "✅ **Đơn hàng đã được thanh toán thành công!**\n\n" +
                           "🎉 Cảm ơn bạn đã thanh toán! Đơn hàng của bạn đã được xác nhận và đang được xử lý.\n\n" +
                           "📦 **Thông tin đơn hàng:**\n" +
                           "- Mã đơn hàng: " + existingOrder.getOrderNumber() + "\n" +
                           "- Phương thức thanh toán: Chuyển khoản ngân hàng\n" +
                           "- Trạng thái: Đã thanh toán\n\n" +
                           "🔔 Chúng tôi sẽ giao hàng trong thời gian sớm nhất. Cảm ơn bạn đã mua sắm!";
                }
            }
            
            // Gọi API SePay để kiểm tra trạng thái thanh toán
            Map<String, Object> paymentStatus = sePayApiClient.checkPaymentStatus(tempOrderNumber);
            System.out.println("DEBUG - Payment status response: " + paymentStatus);
            
            if (!(Boolean)paymentStatus.get("success")) {
                return "❌ Có lỗi xảy ra khi kiểm tra trạng thái thanh toán: " + paymentStatus.get("error");
            }
            
            String status = (String) paymentStatus.get("status");
            System.out.println("DEBUG - Payment status: " + status);
            
            // Nếu chưa thanh toán hoặc chưa nhận được webhook
            if ("pending".equals(status)) {
                return "⏳ Đơn hàng của bạn chưa được thanh toán. Vui lòng hoàn tất thanh toán và thử lại sau.";
            }
            
            // Nếu đã thanh toán nhưng chưa nhận được webhook hoặc chưa xử lý webhook
            if ("completed".equals(status) || "success".equals(status)) {
                // Tạo đơn hàng
                int cartId = (Integer) context.get("cart_id");
                int addressId = (Integer) context.get("selected_address_id");
                double finalAmount = (Double) context.get("final_amount");
                
                // Tạo số đơn hàng chính thức
                String orderNumber = orderDAO.generateOrderNumber();
                String notes = "Đơn hàng được thanh toán qua SePay, mã giao dịch: " + paymentStatus.get("transaction_id") + 
                               ", mã đơn hàng tạm thời: " + tempOrderNumber;
                
                System.out.println("DEBUG - Creating new order: cartId=" + cartId + ", addressId=" + addressId + 
                                  ", orderNumber=" + orderNumber);
                
                // Thực hiện tạo đơn hàng
                int orderId = orderDAO.createOrder(
                    userId, 
                    cartId, 
                    orderNumber, 
                    addressId, 
                    "bank_transfer", // Thay đổi từ "sepay" thành "bank_transfer"
                    notes, 
                    BigDecimal.ZERO // Không có giảm giá
                );
                
                System.out.println("DEBUG - Order created with ID: " + orderId);
                
                // Cập nhật trạng thái đơn hàng thành đã thanh toán
                boolean updatePaymentResult = orderDAO.updatePaymentStatus(orderId, "paid");
                boolean updateStatusResult = orderDAO.updateOrderStatus(orderId, "processing");
                
                System.out.println("DEBUG - Update payment status: " + updatePaymentResult);
                System.out.println("DEBUG - Update order status: " + updateStatusResult);
                
                // Xóa giỏ hàng sau khi thanh toán thành công
                try {
                    CartDAO cartDAO = new CartDAO();
                    Cart userCart = cartDAO.getOrCreateCartByUserId(userId);
                    if (userCart != null) {
                        // Xóa các item trong giỏ hàng
                        boolean clearCartResult = cartDAO.clearCartItems(userCart.getCartId());
                        System.out.println("DEBUG - Clear cart items for user " + userId + ": " + clearCartResult);
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG - Error clearing cart: " + e.getMessage());
                }
                
                // Xóa thông tin thanh toán khỏi context
                context.remove("pending_sepay_checkout");
                context.remove("temp_order_number");
                context.remove("selected_address_id");
                context.remove("cart_id"); // Xóa cart_id để làm trống giỏ hàng
                context.remove("final_amount");
                
                // Lưu thông tin đơn hàng vào context
                context.put("confirmed_order_id", orderId);
                context.put("order_number", orderNumber);
                
                // Tạo thông báo thanh toán thành công
                try {
                    NotificationDAO notificationDAO = new NotificationDAO();
                    String message = "Đơn hàng " + orderNumber + " đã được thanh toán thành công! Cảm ơn bạn đã mua hàng.";
                    notificationDAO.createPaymentSuccessNotification(userId, orderId, message);
                } catch (Exception e) {
                    System.out.println("DEBUG - Error creating notification: " + e.getMessage());
                }
                
                // Tạo phản hồi
                StringBuilder response = new StringBuilder();
                response.append("✅ **Thanh toán thành công!**\n\n");
                response.append("🎉 Cảm ơn bạn đã thanh toán! Đơn hàng của bạn đã được xác nhận.\n\n");
                
                response.append("📦 **Thông tin đơn hàng:**\n");
                response.append("- Mã đơn hàng: ").append(orderNumber).append("\n");
                response.append("- Mã giao dịch SePay: ").append(paymentStatus.get("transaction_id")).append("\n");
                response.append("- Phương thức thanh toán: Chuyển khoản ngân hàng\n");
                response.append("- Trạng thái: Đã thanh toán\n\n");
                
                response.append("🔔 **Tiếp theo:**\n");
                response.append("- Đơn hàng của bạn sẽ được xử lý và giao trong vòng 2-3 ngày làm việc\n");
                response.append("- Bạn có thể theo dõi đơn hàng trong mục \"Đơn hàng của tôi\" trên website\n");
                response.append("- Bạn sẽ nhận được thông báo khi đơn hàng được giao\n\n");
                
                response.append("🛍️ Chúc bạn có trải nghiệm mua sắm tuyệt vời với shop chúng mình!");
                
                return response.toString();
            }
            
            // Trạng thái khác (failed, cancelled, etc.)
            return "❌ Thanh toán không thành công. Trạng thái: " + status + ". Vui lòng thử lại hoặc chọn phương thức thanh toán khác.";
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG - Error in handleCheckSePayStatus: " + e.getMessage());
            return "😓 Xin lỗi, đã xảy ra lỗi khi kiểm tra trạng thái thanh toán: " + e.getMessage();
        }
    }
    
    /**
     * Cấu hình thông tin tài khoản ngân hàng
     */
    private String configureBankAccount(String bankName, String accountNumber, String accountName, Map<String, Object> context) {
        String bankId = SePayConfig.getBankBinCode(bankName.toLowerCase());
        
        if (bankId.isEmpty()) {
            return "❌ Ngân hàng không được hỗ trợ. Các ngân hàng được hỗ trợ: Vietcombank, Vietinbank, BIDV, Techcombank, MBBank, ACB, VPBank, Sacombank, TPBank.";
        }
        
        // Cập nhật cấu hình tài khoản ngân hàng
        SePayConfig.updateDefaultBankAccount(bankName, accountNumber, accountName);
        
        return "✅ Đã cấu hình thành công tài khoản ngân hàng:\n" +
               "- Ngân hàng: " + bankName + "\n" +
               "- Số tài khoản: " + accountNumber + "\n" +
               "- Tên tài khoản: " + accountName;
    }

    /**
     * Phương thức để thiết lập userId cho phiên chat hiện tại
     * Gọi phương thức này sau khi người dùng đăng nhập
     */
    public void setCurrentUserId(int userId) {
        // Lưu userId vào context để sử dụng cho việc thêm vào giỏ hàng
    }

    /**
     * Lấy thông báo thanh toán thành công cho người dùng
     * 
     * @param userId ID của người dùng
     * @param context Context chứa thông tin về đơn hàng hiện tại
     * @return Thông báo thanh toán thành công hoặc null nếu không có
     */
    private String getPaymentSuccessNotifications(int userId, Map<String, Object> context) {
        try {
            // Lấy thông tin đơn hàng mới nhất từ context
            String currentOrderNumber = (String) context.get("temp_order_number");
            
            // Lấy danh sách thông báo chưa đọc và mới nhất
            NotificationDAO notificationDAO = new NotificationDAO();
            List<Notification> notifications = notificationDAO.getRecentNotifications(userId, 5); // Lấy 5 thông báo gần nhất để tìm đơn hàng hiện tại
            
            if (notifications == null || notifications.isEmpty()) {
                return null;
            }
            
            // Lọc các thông báo thanh toán thành công
            List<Notification> paymentNotifications = new ArrayList<>();
            Notification currentOrderNotification = null;
            
            for (Notification notification : notifications) {
                if ("payment_success".equals(notification.getType())) {
                    // Kiểm tra xem thông báo này có phải là cho đơn hàng hiện tại không
                    if (currentOrderNumber != null && notification.getMessage().contains(currentOrderNumber)) {
                        currentOrderNotification = notification;
                        break; // Tìm thấy thông báo cho đơn hàng hiện tại
                    }
                    paymentNotifications.add(notification);
                }
            }
            
            // Nếu tìm thấy thông báo cho đơn hàng hiện tại, ưu tiên hiển thị nó
            Notification notificationToShow = currentOrderNotification != null ? 
                                             currentOrderNotification : 
                                             (paymentNotifications.isEmpty() ? null : paymentNotifications.get(0));
            
            if (notificationToShow == null) {
                return null;
            }
            
            // Tạo phản hồi với thông báo thanh toán
            StringBuilder response = new StringBuilder();
            response.append("✅ **Thông báo thanh toán thành công**\n\n");
            response.append("🛍️ ").append(notificationToShow.getMessage()).append("\n\n");
            
            // Đánh dấu thông báo đã đọc
            if (!notificationToShow.isRead()) {
                notificationDAO.markAsRead(notificationToShow.getNotificationId());
            }
            
            // Thêm thông tin về việc kiểm tra đơn hàng
            response.append("Bạn có thể kiểm tra chi tiết đơn hàng tại [Đơn hàng của tôi](/DuAnBanCaCanh/orders.jsp)\n\n");
            response.append("Cảm ơn bạn đã mua hàng tại cửa hàng của chúng tôi! 😊");
            
            return response.toString();
            
        } catch (Exception e) {
            System.out.println("DEBUG - Error getting payment notifications: " + e.getMessage());
            return null;
        }
    }
}
