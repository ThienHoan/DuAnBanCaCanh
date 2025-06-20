package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.AddressDAO;
import dao.CartDAO;
import dao.DBConnection;
import dao.OrderDAO;
import model.Address;
import model.Cart;
import model.Coupon;
import model.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CheckoutServlet.class.getName());
    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private AddressDAO addressDAO;
    private Connection conn;

    // VNPay configuration
    private static final String VNPAY_TMN_CODE = "25SYU20K";
    private static final String VNPAY_HASH_SECRET = "T29F4QPB100BW89WAVJXB3PQNRZ4F2P1";
    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNPAY_RETURN_URL = "http://localhost:8080/DuAnBanCaCanh/vnpay-return";

    @Override
    public void init() {
        cartDAO = new CartDAO();
        orderDAO = new OrderDAO();
        addressDAO = new AddressDAO();
        conn = DBConnection.getConnection();
        if (conn == null) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu");
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu");
        }
        LOGGER.info("CheckoutServlet initialized successfully");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "/checkout.jsp";
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Cart cart = (Cart) session.getAttribute("CART");
            Coupon coupon = (Coupon) session.getAttribute("COUPON");
            if (cart == null || cart.getItems().isEmpty()) {
                request.setAttribute("error", "Giỏ hàng trống. Vui lòng thêm sản phẩm.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // Fetch existing addresses, but don't block if none exist
            List<Address> addresses = addressDAO.getAddressesByUserId(user.getUserId());
            request.setAttribute("ADDRESSES", addresses);

            String action = request.getParameter("action");
            if ("placeOrder".equalsIgnoreCase(action)) {
                String addressIdStr = request.getParameter("addressId");
                String paymentMethod = request.getParameter("paymentMethod");
                String notes = request.getParameter("notes");

                if (!List.of("cod", "e-wallet").contains(paymentMethod)) {
                    request.setAttribute("error", "Phương thức thanh toán không hợp lệ.");
                    request.getRequestDispatcher(url).forward(request, response);
                    return;
                }

                if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                    request.setAttribute("error", "Vui lòng chọn một địa chỉ hoặc nhập địa chỉ mới.");
                    request.getRequestDispatcher(url).forward(request, response);
                    return;
                }

                int addressId;
                if ("new".equals(addressIdStr)) {
                    String recipientName = request.getParameter("recipientName");
                    String phone = request.getParameter("phone");
                    String province = request.getParameter("province");
                    String district = request.getParameter("district");
                    String ward = request.getParameter("ward");
                    String addressDetail = request.getParameter("addressDetail");

                    // Validate new address fields
                    if (recipientName == null || recipientName.trim().isEmpty()) {
                        request.setAttribute("error", "Tên người nhận không được để trống.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                    if (phone == null || phone.trim().isEmpty() || !phone.matches("\\d{10,11}")) {
                        request.setAttribute("error", "Số điện thoại không hợp lệ. Vui lòng nhập 10-11 chữ số.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                    if (province == null || province.trim().isEmpty()) {
                        request.setAttribute("error", "Tỉnh/Thành phố không được để trống.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                    if (district == null || district.trim().isEmpty()) {
                        request.setAttribute("error", "Quận/Huyện không được để trống.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                    if (ward == null || ward.trim().isEmpty()) {
                        request.setAttribute("error", "Phường/Xã không được để trống.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                    if (addressDetail == null || addressDetail.trim().isEmpty()) {
                        request.setAttribute("error", "Chi tiết địa chỉ không được để trống.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }

                    Address newAddress = new Address(0, user.getUserId(), recipientName, phone, province, district, ward, addressDetail, false, "shipping", false);
                    // Check for duplicates even if no addresses exist
                    boolean isDuplicate = addresses.stream().anyMatch(a -> 
                        a.getRecipientName().equalsIgnoreCase(recipientName) &&
                        a.getPhone().equals(phone) &&
                        a.getProvince().equalsIgnoreCase(province) &&
                        a.getDistrict().equalsIgnoreCase(district) &&
                        a.getWard().equalsIgnoreCase(ward) &&
                        a.getAddressDetail().equalsIgnoreCase(addressDetail) &&
                        !a.getIsDeleted()
                    );
                    if (isDuplicate) {
                        request.setAttribute("error", "Địa chỉ này đã tồn tại. Vui lòng chọn địa chỉ có sẵn hoặc nhập địa chỉ khác.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                    addressId = addressDAO.addAddress(newAddress);
                } else {
                    try {
                        addressId = Integer.parseInt(addressIdStr);
                        if (!addresses.isEmpty() && !addresses.stream().anyMatch(a -> a.getAddressId() == addressId && !a.getIsDeleted())) {
                            LOGGER.warning("Invalid addressId: " + addressId + " for userId: " + user.getUserId());
                            request.setAttribute("error", "Địa chỉ không hợp lệ hoặc đã bị xóa.");
                            request.getRequestDispatcher(url).forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Invalid addressId format: " + addressIdStr);
                        request.setAttribute("error", "Địa chỉ không hợp lệ.");
                        request.getRequestDispatcher(url).forward(request, response);
                        return;
                    }
                }

                // Validate cart stock
                String stockError = cartDAO.validateCartStock(cart);
                if (stockError != null) {
                    request.setAttribute("error", stockError);
                    request.getRequestDispatcher("/cart.jsp").forward(request, response);
                    return;
                }

                double totalPrice = 0;
                for (var item : cart.getItems()) {
                    double price = item.getProduct().getSalePrice() != null ? item.getProduct().getSalePrice() : item.getProduct().getPrice();
                    totalPrice += price * item.getQuantity();
                }
                double discountAmount = coupon != null ? coupon.getDiscountAmount() : 0;
                double finalAmount = totalPrice - discountAmount + 20000;

                String orderNumber = "ORD" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                if ("e-wallet".equals(paymentMethod)) {
                    // Store order details in session instead of creating order
                    session.setAttribute("PENDING_ORDER", Map.of(
                        "userId", user.getUserId(),
                        "cart", cart,
                        "orderNumber", orderNumber,
                        "addressId", addressId,
                        "paymentMethod", paymentMethod,
                        "notes", notes != null ? notes : "",
                        "discountAmount", discountAmount
                    ));
                    String vnpayUrl = createVNPayUrl(request, orderNumber, finalAmount);
                    response.sendRedirect(vnpayUrl);
                    return;
                } else {
                    // For COD, create order immediately
                    int orderId = orderDAO.createOrder(user.getUserId(), cart, orderNumber, addressId, paymentMethod, notes != null ? notes : "", discountAmount);
                    cartDAO.clearCart(cart.getCartId());
                    session.removeAttribute("CART");
                    session.removeAttribute("COUPON");
                    response.sendRedirect(request.getContextPath() + "/order-confirmation.jsp?orderId=" + orderId);
                    return;
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CheckoutServlet error: " + ex.getMessage(), ex);
            request.setAttribute("error", "Không thể xử lý thanh toán: " + ex.getMessage());
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    private String createVNPayUrl(HttpServletRequest request, String orderNumber, double amount) throws IOException {
        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VNPAY_TMN_CODE);
        vnp_Params.put("vnp_Amount", String.valueOf((long)(amount * 100)));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderNumber);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + orderNumber);
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPAY_RETURN_URL);
        vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());

        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(entry.getKey()).append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII.toString()));
        }

        String vnp_SecureHash = hmacSHA512(VNPAY_HASH_SECRET, hashData.toString());

        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            if (query.length() > 0) query.append('&');
            query.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return VNPAY_URL + "?" + query.toString();
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký VNPay", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}