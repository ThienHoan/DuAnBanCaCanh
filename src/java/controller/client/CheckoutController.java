package controller.client;

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
import jakarta.servlet.annotation.WebServlet;
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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringJoiner;
import dao.impl.pAttribute.ProductAttributeValueDAO;
import dao.impl.ProductImageDAO;
import model.entity.pAttribute.ProductAttributeValue;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CheckoutController.class.getName());
    private CartDAO cartDAO;
    private AddressDAO addressDAO;
    private OrderDAOImpl orderDAO;
    private CouponDAO couponDAO;
    private ProductDAO productDAO;
    private ProductAttributeValueDAO productAttributeValueDAO;
    private ProductImageDAO productImageDAO;

    // VNPay configuration
    private static final String VNPAY_RETURN_URL = "http://localhost:8080/DuAnCaCanh/vnpay-return";

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("redirectUrl", "checkout");
            response.sendRedirect("login.jsp");
            return;
        }

        // Xóa mã giảm giá khỏi session khi tải lại trang
        if (request.getParameter("keepCoupon") == null) {
            session.removeAttribute("COUPON");
        }

        // Get cart
        Cart cart;
        List<CartItem> cartItems;
        
        try {
            cart = cartDAO.getOrCreateCartByUserId(user.getUserId());
            cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            if (cartItems.isEmpty()) {
                session.setAttribute("ERROR_MESSAGE", "Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.");
                response.sendRedirect("cartClient");
                return;
            }
            
            // Calculate totals
            double cartTotal = cartDAO.getCartTotal(cart.getCartId());
            
            // Get addresses
            List<Address> addresses = addressDAO.getAddressesByUserId(user.getUserId());
            
            // Get coupon from session
            Coupon coupon = (Coupon) session.getAttribute("COUPON");
            double discountAmount = coupon != null ? coupon.getDiscountAmount().doubleValue() : 0;
            
            // Calculate tax (5% of cart total AFTER discount)
            double tax = (cartTotal - discountAmount) * 0.05;
            
            // Set attributes
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("cartTotal", cartTotal);
            request.setAttribute("discountAmount", discountAmount);
            request.setAttribute("tax", tax);
            request.setAttribute("shippingFee", 20000.0);
            request.setAttribute("finalAmount", cartTotal - discountAmount + 20000 + tax); // Include tax in final amount
            request.setAttribute("addresses", addresses);
            
            // Get product images for cart items
            Map<Integer, String> productMainImages = new HashMap<>();
            // Get product attributes for cart items
            Map<Integer, List<ProductAttributeValue>> productAttributesMap = new HashMap<>();
            
            for (CartItem item : cartItems) {
                // Get main image
                String mainImage = productImageDAO.getMainImageByProductId1(item.getProductId());
                productMainImages.put(item.getProductId(), mainImage);
                
                // Get attributes
                List<ProductAttributeValue> attributes = productAttributeValueDAO.getProductAttributeValuesWithNamesByProductId(item.getProductId());
                productAttributesMap.put(item.getProductId(), attributes);
            }
            
            request.setAttribute("productMainImages", productMainImages);
            request.setAttribute("productAttributesMap", productAttributesMap);
            
            // Forward to checkout page
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in checkout process: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra: " + e.getMessage());
            if (!response.isCommitted()) {
                response.sendRedirect("cartClient");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("redirectUrl", "checkout");
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        
        try {
            if ("placeOrder".equals(action)) {
                placeOrder(request, response, user);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing checkout: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi xử lý đơn hàng: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        
        // Get cart
        Cart cart = cartDAO.getOrCreateCartByUserId(user.getUserId());
        List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
        
        if (cartItems.isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "Giỏ hàng của bạn đang trống.");
            response.sendRedirect("cartClient");
            return;
        }
        
        // Validate stock
        String stockError = orderDAO.validateCartStock(cartItems);
        if (stockError != null) {
            session.setAttribute("ERROR_MESSAGE", stockError);
            response.sendRedirect("cartClient");
            return;
        }
        
        // Process address
        int addressId;
        String addressIdParam = request.getParameter("addressId");
        
        if ("new".equals(addressIdParam)) {
            // Create new address
            String recipientName = request.getParameter("recipientName");
            String phone = request.getParameter("phone");
            String province = request.getParameter("province");
            String district = request.getParameter("district");
            String ward = request.getParameter("ward");
            String addressDetail = request.getParameter("addressDetail");
            
            // Validate address fields
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
            
            // Create new address
            Address newAddress = new Address(0, user.getUserId(), recipientName, phone, province, 
                                           district, ward, addressDetail, false, "shipping", false);
            addressId = addressDAO.addAddress(newAddress);
            
            if (addressId <= 0) {
                session.setAttribute("ERROR_MESSAGE", "Không thể tạo địa chỉ mới.");
                response.sendRedirect("checkout");
                return;
            }
        } else {
            try {
                addressId = Integer.parseInt(addressIdParam);
                Address address = addressDAO.getAddressById(addressId);
                
                if (address == null || address.getUserId() != user.getUserId()) {
                    session.setAttribute("ERROR_MESSAGE", "Địa chỉ không hợp lệ.");
                    response.sendRedirect("checkout");
                    return;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("ERROR_MESSAGE", "Địa chỉ không hợp lệ.");
                response.sendRedirect("checkout");
                return;
            }
        }
        
        // Get payment method
        String paymentMethod = request.getParameter("paymentMethod");
        if (!List.of("cod", "e-wallet").contains(paymentMethod)) {
            session.setAttribute("ERROR_MESSAGE", "Phương thức thanh toán không hợp lệ.");
            response.sendRedirect("checkout");
            return;
        }
        
        // Get notes
        String notes = request.getParameter("notes");
        if (notes == null) {
            notes = "";
        }
        
        // Calculate totals
        double cartTotal = cartDAO.getCartTotal(cart.getCartId());
        Coupon coupon = (Coupon) session.getAttribute("COUPON");
        BigDecimal discountAmount = coupon != null ? coupon.getDiscountAmount() : BigDecimal.ZERO;
        
        // Calculate tax (5% of cart total AFTER discount)
        BigDecimal cartTotalAfterDiscount = BigDecimal.valueOf(cartTotal).subtract(discountAmount);
        BigDecimal tax = cartTotalAfterDiscount.multiply(BigDecimal.valueOf(0.05));
        
        // Calculate final amount with tax
        BigDecimal finalAmount = cartTotalAfterDiscount
                .add(BigDecimal.valueOf(20000)) // 20000 is shipping fee
                .add(tax); // Add tax to final amount
        
        // Generate order number
        String orderNumber = orderDAO.generateOrderNumber();
        
        if ("e-wallet".equals(paymentMethod)) {
            // Store pending order details in session
            Map<String, Object> pendingOrder = new HashMap<>();
            pendingOrder.put("userId", user.getUserId());
            pendingOrder.put("cartId", cart.getCartId());
            pendingOrder.put("orderNumber", orderNumber);
            pendingOrder.put("addressId", addressId);
            pendingOrder.put("paymentMethod", paymentMethod);
            pendingOrder.put("notes", notes);
            pendingOrder.put("discountAmount", discountAmount);
            pendingOrder.put("finalAmount", finalAmount);
            
            // Store coupon information if available
            if (coupon != null) {
                pendingOrder.put("couponId", coupon.getCouponId());
            }
            
            session.setAttribute("PENDING_ORDER", pendingOrder);
            
            // Create VNPay URL
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
            
            // Build hash data
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
            
            // Create secure hash
            String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            
            // Append secure hash to URL
            StringJoiner queryUrl = new StringJoiner("&");
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                queryUrl.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII.toString()));
            }
            queryUrl.add("vnp_SecureHash=" + secureHash);
            
            String paymentUrl = vnp_Url + "?" + queryUrl.toString();
            response.sendRedirect(paymentUrl);
            return;
        } else {
            // For COD, create order immediately
            try {
                int orderId = orderDAO.createOrder(
                    user.getUserId(), 
                    cart, 
                    orderNumber, 
                    addressId, 
                    paymentMethod, 
                    notes, 
                    discountAmount
                );
                
                // Record coupon usage if coupon was applied
                if (coupon != null) {
                    couponDAO.recordCouponUsage(coupon.getCouponId(), user.getUserId(), orderId, discountAmount);
                }
                
                // Clear coupon from session
                session.removeAttribute("COUPON");
                
                // Forward to confirmation page
                request.getRequestDispatcher("order-confirmation.jsp?orderId=" + orderId).forward(request, response);
                return;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error creating order: " + e.getMessage(), e);
                session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi tạo đơn hàng: " + e.getMessage());
                response.sendRedirect("checkout");
                return;
            }
        }
    }
    
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