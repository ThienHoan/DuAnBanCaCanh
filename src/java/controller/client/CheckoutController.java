package controller.client;

// Import các class DAO để tương tác với cơ sở dữ liệu các thực thể như Address, Cart, Coupon, Order, Product,...
import dao.impl.AddressDAO;
import dao.impl.CartDAO;
import dao.impl.CouponDAO;
import dao.impl.OrderDAOImpl;
import dao.impl.ProductDAO;
import model.entity.Address;
import model.entity.Coupon;
import model.entity.User;
import model.entity.pCart.Cart;
import model.entity.pCart.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
// Thư viện mã hóa cho HMAC-SHA512 (dùng cho thanh toán VNPay)
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringJoiner;
// DAO để lấy các thuộc tính sản phẩm
import dao.impl.pAttribute.ProductAttributeValueDAO;
import dao.impl.ProductImageDAO;
import model.entity.pAttribute.ProductAttributeValue;

/**
 * Controller xử lý chức năng thanh toán cho khách hàng
 * Bao gồm: xử lý đơn hàng, thanh toán VNPay, quản lý tồn kho, và xử lý coupon
 */
public class CheckoutController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CheckoutController.class.getName());
    
    // Khai báo các DAO để thao tác với dữ liệu trong DB
    private CartDAO cartDAO;
    private AddressDAO addressDAO;
    private OrderDAOImpl orderDAO;
    private CouponDAO couponDAO;
    private ProductDAO productDAO;
    private ProductAttributeValueDAO productAttributeValueDAO;
    private ProductImageDAO productImageDAO;

    // URL trả về khi thanh toán qua VNPay xong (địa chỉ trong local)
    private static final String VNPAY_RETURN_URL = "http://localhost:8080/DuAnBanCaCanh/vnpay-return";

    // Khởi tạo các DAO khi Servlet được khởi tạo
    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
        addressDAO = new AddressDAO();
        orderDAO = new OrderDAOImpl();
        couponDAO = new CouponDAO();
        productDAO = new ProductDAO();
        productAttributeValueDAO = new ProductAttributeValueDAO();
        productImageDAO = new ProductImageDAO();
    }

    // Xử lý yêu cầu GET - hiển thị trang checkout, lấy thông tin giỏ hàng, địa chỉ, coupon, tính toán tổng tiền
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Nếu chưa đăng nhập, lưu url chuyển hướng và yêu cầu đăng nhập
        if (user == null) {
            session.setAttribute("redirectUrl", "checkout");
            response.sendRedirect("login.jsp");
            return;
        }

        // Nếu không giữ coupon thì xóa coupon trong session
        if (request.getParameter("keepCoupon") == null) {
            session.removeAttribute("COUPON");
        }

        Cart cart;
        List<CartItem> cartItems;
        
        try {
            // Lấy giỏ hàng của user, nếu chưa có tạo mới
            cart = cartDAO.getOrCreateCartByUserId(user.getUserId());
            // Lấy các sản phẩm trong giỏ hàng
            cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            // Nếu giỏ hàng trống báo lỗi và chuyển về trang giỏ hàng
            if (cartItems.isEmpty()) {
                session.setAttribute("ERROR_MESSAGE", "Your cart is empty. Please add products before checkout.");
                response.sendRedirect("cartClient");
                return;
            }
            
            // Tính tổng tiền giỏ hàng
            double cartTotal = cartDAO.getCartTotal(cart.getCartId());
            
            // Lấy danh sách địa chỉ của user
            List<Address> addresses = addressDAO.getAddressesByUserId(user.getUserId());
            
            // Lấy coupon nếu có trong session, tính tiền giảm giá
            Coupon coupon = (Coupon) session.getAttribute("COUPON");
            double discountAmount = coupon != null ? coupon.getDiscountAmount().doubleValue() : 0;
            
            // Tính thuế VAT 5% trên tổng tiền sau khi trừ giảm giá
            double tax = (cartTotal - discountAmount) * 0.05;
            
            // Gửi các dữ liệu cần thiết lên trang checkout.jsp
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", cartTotal);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("tax", tax);
            request.setAttribute("shippingFee", 20000.0);
            // Tính tiền cuối cùng gồm tổng tiền - giảm giá + phí ship + thuế
            request.setAttribute("finalAmount", cartTotal - discountAmount + 20000 + tax);
            request.setAttribute("addresses", addresses);
            
            // Map lưu ảnh đại diện chính của từng sản phẩm
            Map<Integer, String> productMainImages = new HashMap<>();
            // Map lưu danh sách thuộc tính của từng sản phẩm trong giỏ
            Map<Integer, List<ProductAttributeValue>> productAttributesMap = new HashMap<>();
            
            for (CartItem item : cartItems) {
                // Lấy ảnh chính của sản phẩm
                String mainImage = productImageDAO.getMainImageByProductId1(item.getProductId());
                productMainImages.put(item.getProductId(), mainImage);
                
                // Lấy danh sách thuộc tính của sản phẩm (size, màu,...)
                List<ProductAttributeValue> attributes = productAttributeValueDAO.getProductAttributeValuesWithNamesByProductId(item.getProductId());
                productAttributesMap.put(item.getProductId(), attributes);
            }
            
            // Đưa 2 map trên lên request để hiển thị trên view
            request.setAttribute("productMainImages", productMainImages);
            request.setAttribute("productAttributesMap", productAttributesMap);
            
            // Forward đến trang checkout.jsp để hiển thị
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            // Log lỗi và báo lỗi ra session, chuyển về trang giỏ hàng
            LOGGER.log(Level.SEVERE, "Error in checkout process: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra: " + e.getMessage());
            if (!response.isCommitted()) {
                response.sendRedirect("cartClient");
            }
        }
    }

    // Xử lý POST (ví dụ gửi form đặt hàng)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra đăng nhập, nếu chưa chuyển về trang login
        if (user == null) {
            session.setAttribute("redirectUrl", "checkout");
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy action để phân loại xử lý (ở đây chỉ có "placeOrder")
        String action = request.getParameter("action");
        
        try {
            if ("placeOrder".equals(action)) {
                // Gọi hàm xử lý đặt hàng
                placeOrder(request, response, user);
            } else {
                // Nếu không phải action trên thì gọi doGet (hiển thị lại trang)
                doGet(request, response);
            }
        } catch (Exception e) {
            // Log lỗi, báo lỗi vào session rồi chuyển về trang checkout
            LOGGER.log(Level.SEVERE, "Error processing checkout: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi xử lý đơn hàng: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }

    /**
     * Xử lý đặt hàng:
     * - Kiểm tra tồn kho
     * - Xử lý địa chỉ giao hàng (mới hoặc chọn địa chỉ cũ)
     * - Tính toán tổng tiền, áp dụng coupon, phí ship, thuế
     * - Xử lý thanh toán (COD hoặc ví điện tử VNPay)
     * - Cập nhật tồn kho, ghi nhận sử dụng coupon
     */
    private void placeOrder(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        
        // Lấy giỏ hàng và danh sách sản phẩm trong giỏ
        Cart cart = cartDAO.getOrCreateCartByUserId(user.getUserId());
        List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
        
        // Nếu giỏ hàng trống báo lỗi và chuyển về giỏ hàng
        if (cartItems.isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "Giỏ hàng của bạn đang trống.");
            response.sendRedirect("cartClient");
            return;
        }
        
        // Kiểm tra tồn kho đủ hay không
        String stockError = orderDAO.validateCartStock(cartItems);
        if (stockError != null) {
            session.setAttribute("ERROR_MESSAGE", stockError);
            response.sendRedirect("cartClient");
            return;
        }
        
        int addressId;
        String addressIdParam = request.getParameter("addressId");
        
        // Nếu chọn tạo địa chỉ mới
        if ("new".equals(addressIdParam)) {
            // Lấy dữ liệu từ form nhập địa chỉ mới
            String recipientName = request.getParameter("recipientName");
            String phone = request.getParameter("phone");
            String province = request.getParameter("province");
            String district = request.getParameter("district");
            String ward = request.getParameter("ward");
            String addressDetail = request.getParameter("addressDetail");
            
            // Kiểm tra dữ liệu nhập địa chỉ có hợp lệ, không rỗng, số điện thoại đúng định dạng
            if (recipientName == null || recipientName.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() || !phone.matches("\\d{10,11}") ||
                province == null || province.trim().isEmpty() ||
                district == null || district.trim().isEmpty() ||
                ward == null || ward.trim().isEmpty() ||
                addressDetail == null || addressDetail.trim().isEmpty()) {
                
                session.setAttribute("ERROR_MESSAGE", "Vui lòng điền đầy đủ thông tin địa chỉ.");
                response.sendRedirect("checkout");
                return;
            }
            
            // Tạo mới Address và thêm vào DB, lấy id địa chỉ mới
            Address newAddress = new Address(0, user.getUserId(), recipientName, phone, province, 
                                           district, ward, addressDetail, false, "shipping", false);
            addressId = addressDAO.addAddress(newAddress);
            
            // Nếu không tạo được địa chỉ báo lỗi
            if (addressId <= 0) {
                session.setAttribute("ERROR_MESSAGE", "Không thể tạo địa chỉ mới.");
                response.sendRedirect("checkout");
                return;
            }
        } else {
            // Nếu chọn địa chỉ cũ, parse id và kiểm tra quyền truy cập
            try {
                addressId = Integer.parseInt(addressIdParam);
                Address address = addressDAO.getAddressById(addressId);
                
                // Kiểm tra địa chỉ có thuộc user không
                if (address == null || address.getUserId() != user.getUserId()) {
                    session.setAttribute("ERROR_MESSAGE", "Địa chỉ không hợp lệ.");
                    response.sendRedirect("checkout");
                    return;
                }
            } catch (NumberFormatException e) {
                // Nếu không parse được id địa chỉ, báo lỗi
                session.setAttribute("ERROR_MESSAGE", "Địa chỉ không hợp lệ.");
                response.sendRedirect("checkout");
                return;
            }
        }
        
        // Lấy phương thức thanh toán và kiểm tra hợp lệ (chỉ chấp nhận cod hoặc ví điện tử)
        String paymentMethod = request.getParameter("paymentMethod");
        if (!List.of("cod", "e-wallet").contains(paymentMethod)) {
            session.setAttribute("ERROR_MESSAGE", "Phương thức thanh toán không hợp lệ.");
            response.sendRedirect("checkout");
            return;
        }
        
        // Ghi chú đơn hàng (nếu có)
        String notes = request.getParameter("notes");
        if (notes == null) {
            notes = "";
        }
        
        // Tính toán lại tổng tiền, áp dụng coupon, thuế, phí ship
        double cartTotal = cartDAO.getCartTotal(cart.getCartId());
        Coupon coupon = (Coupon) session.getAttribute("COUPON");
        BigDecimal discountAmount = coupon != null ? coupon.getDiscountAmount() : BigDecimal.ZERO;
        
        BigDecimal cartTotalAfterDiscount = BigDecimal.valueOf(cartTotal).subtract(discountAmount);
        BigDecimal tax = cartTotalAfterDiscount.multiply(BigDecimal.valueOf(0.05));
        
        BigDecimal finalAmount = cartTotalAfterDiscount
                .add(BigDecimal.valueOf(20000)) // phí ship
                .add(tax);
        
        // Tạo mã số đơn hàng (unique)
        String orderNumber = orderDAO.generateOrderNumber();
        
        // Nếu thanh toán qua ví điện tử (VNPay)
        if ("e-wallet".equals(paymentMethod)) {
            // Tạo đối tượng pendingOrder lưu tạm các thông tin đơn hàng trong session
            Map<String, Object> pendingOrder = new HashMap<>();
            pendingOrder.put("userId", user.getUserId());
            pendingOrder.put("cartId", cart.getCartId());
            pendingOrder.put("orderNumber", orderNumber);
            pendingOrder.put("addressId", addressId);
            pendingOrder.put("paymentMethod", paymentMethod);
            pendingOrder.put("notes", notes);
            pendingOrder.put("discountAmount", discountAmount);
            pendingOrder.put("finalAmount", finalAmount);
            
            if (coupon != null) {
                pendingOrder.put("couponId", coupon.getCouponId());
            }
            
            session.setAttribute("PENDING_ORDER", pendingOrder);
            
            // Tạo url thanh toán VNPay với dữ liệu order
            Map<String, Object> vnpayData = orderDAO.createVNPayUrl(
                orderNumber, 
                finalAmount, 
                VNPAY_RETURN_URL, 
                request.getRemoteAddr()
            );
            
            @SuppressWarnings("unchecked")
            Map<String, String> vnp_Params = (Map<String, String>) vnpayData.get("vnp_Params");
            String vnp_Url = (String) vnpayData.get("vnp_Url");
            String vnp_HashSecret = (String) vnpayData.get("vnp_HashSecret");
            
            // Sắp xếp các trường theo thứ tự tên trường để tạo dữ liệu hash
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Tạo chuỗi dữ liệu theo định dạng key=value, encode URL
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
            
            // Tạo secure hash HMAC-SHA512 từ chuỗi dữ liệu và secret key
            String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            
            // Tạo chuỗi query param bao gồm các tham số + hash để gọi URL thanh toán VNPay
            StringJoiner queryUrl = new StringJoiner("&");
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                queryUrl.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII.toString()));
            }
            queryUrl.add("vnp_SecureHash=" + secureHash);
            
            // Tạo URL hoàn chỉnh để redirect sang cổng thanh toán VNPay
            String paymentUrl = vnp_Url + "?" + queryUrl.toString();
            response.sendRedirect(paymentUrl);  // chuyển hướng người dùng sang VNPay để thanh toán
            return;
        } else {
            // Nếu thanh toán COD
            try {
                // Tạo đơn hàng trong DB
                int orderId = orderDAO.createOrder(
                    user.getUserId(), 
                    cart, 
                    orderNumber, 
                    addressId, 
                    paymentMethod, 
                    notes, 
                    discountAmount
                );
                
                // Danh sách cập nhật tồn kho
                List<Object[]> inventoryUpdates = new ArrayList<>();
                for (CartItem item : cartItems) {
                    int productId = item.getProductId();
                    int requiredQuantity = item.getQuantity();
                    int currentQuantity = productDAO.getProductQuantity(productId);
                    int newQuantity = currentQuantity - requiredQuantity;
                    if (newQuantity < 0) {
                        // Nếu tồn kho không đủ báo lỗi và chuyển về giỏ hàng
                        session.setAttribute("ERROR_MESSAGE", "Product '" + item.getProductName() + "' has insufficient stock.");
                        response.sendRedirect("cartClient");
                        return;
                    }
                    // Thêm vào danh sách cập nhật tồn kho
                    inventoryUpdates.add(new Object[]{newQuantity, productId, requiredQuantity});
                }
                // Thực hiện cập nhật tồn kho hàng loạt
                if (!inventoryUpdates.isEmpty()) {
                    boolean inventorySuccess = productDAO.batchUpdateInventory(inventoryUpdates);
                    if (!inventorySuccess) {
                        session.setAttribute("ERROR_MESSAGE", "Error updating inventory. Order not processed.");
                        response.sendRedirect("cartClient");
                        return;
                    }
                }
                
                if (coupon != null) {
                    couponDAO.recordCouponUsage(coupon.getCouponId(), user.getUserId(), orderId, discountAmount);
                }
                
                session.removeAttribute("COUPON");
                
                request.getRequestDispatcher("order-confirmation.jsp?orderId=" + orderId).forward(request, response);
                return;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error creating order: " + e.getMessage(), e);
                session.setAttribute("ERROR_MESSAGE", "Error occurred while creating order: " + e.getMessage());
                response.sendRedirect("checkout");
                return;
            }
        }
    }
    
    /**
     * Generate HMAC-SHA512 hash for VNPay security verification
     * @param key Secret key for hashing
     * @param data Data to be hashed
     * @return Hexadecimal string of the hash
     */
    private String hmacSHA512(String key, String data) {
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            
            byte[] hash = sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating HMAC: " + e.getMessage(), e);
            return "";
        }
    }
} 