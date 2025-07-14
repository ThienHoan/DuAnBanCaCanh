package controller.client;

import dao.impl.CartDAO;
import dao.impl.CouponDAO;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/apply-coupon")
public class ApplyCouponController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ApplyCouponController.class.getName());
    private CouponDAO couponDAO;
    private CartDAO cartDAO;

    @Override
    public void init() throws ServletException {
        couponDAO = new CouponDAO();
        cartDAO = new CartDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("ERROR_MESSAGE", "Vui lòng đăng nhập để sử dụng mã giảm giá.");
            response.sendRedirect("login.jsp");
            return;
        }

        String couponCode = request.getParameter("couponCode");
        if (couponCode == null || couponCode.trim().isEmpty()) {
            session.setAttribute("ERROR_MESSAGE", "Vui lòng nhập mã giảm giá.");
            response.sendRedirect("checkout");
            return;
        }

        try {
            // Bước 1: Lấy thông tin giỏ hàng
            Cart cart = cartDAO.getOrCreateCartByUserId(user.getUserId());
            List<CartItem> cartItems = cartDAO.getCartItemsByCartId(cart.getCartId());
            
            if (cartItems.isEmpty()) {
                session.setAttribute("ERROR_MESSAGE", "Giỏ hàng của bạn đang trống.");
                response.sendRedirect("cartClient");
                return;
            }

            // Bước 2: Kiểm tra mã giảm giá có tồn tại và còn hiệu lực không
            Coupon coupon = couponDAO.getCouponByCode(couponCode);
            if (coupon == null) {
                session.setAttribute("ERROR_MESSAGE", "Mã giảm giá không tồn tại hoặc đã hết hạn.");
                response.sendRedirect("checkout");
                return;
            }

            // Bước 3: Kiểm tra điều kiện sử dụng (số lần sử dụng)
            if (!couponDAO.isValidForUser(couponCode, user.getUserId())) {
                session.setAttribute("ERROR_MESSAGE", "Bạn đã dùng mã giảm giá này.");
                response.sendRedirect("checkout");
                return;
            }

            // Tính tổng giá trị giỏ hàng
            double cartTotal = cartDAO.getCartTotal(cart.getCartId());
            
            // Lấy danh sách ID sản phẩm trong giỏ hàng
            List<Integer> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

            // Bước 4: Kiểm tra điều kiện đơn hàng tối thiểu và sản phẩm áp dụng
            if (!couponDAO.isValidForCart(couponCode, productIds, BigDecimal.valueOf(cartTotal))) {
                session.setAttribute("ERROR_MESSAGE", "Đơn hàng của bạn không đủ điều kiện để áp dụng mã giảm giá này.");
                response.sendRedirect("checkout");
                return;
            }

            // Bước 5: Tính toán số tiền giảm giá
            BigDecimal discountAmount = couponDAO.calculateDiscount(couponCode, BigDecimal.valueOf(cartTotal), productIds);
            coupon.setDiscountAmount(discountAmount);

            // Nếu đã có mã giảm giá trước đó, xóa đi
            if (session.getAttribute("COUPON") != null) {
                session.removeAttribute("COUPON");
            }

            // Lưu thông tin mã giảm giá vào session
            session.setAttribute("COUPON", coupon);
            session.setAttribute("SUCCESS_MESSAGE", "Áp dụng mã giảm giá " + couponCode + " thành công! Bạn được giảm " + discountAmount.toString() + "đ");
            
            // Chuyển hướng về trang thanh toán với tham số keepCoupon
            response.sendRedirect("checkout?keepCoupon=true");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi áp dụng mã giảm giá: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra khi áp dụng mã giảm giá: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }
} 