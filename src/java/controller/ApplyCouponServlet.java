package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.CouponDAO;
import model.Cart;
import model.Coupon;
import model.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/apply-coupon")
public class ApplyCouponServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ApplyCouponServlet.class.getName());
    private CouponDAO couponDAO;

    @Override
    public void init() {
        couponDAO = new CouponDAO();
        LOGGER.info("ApplyCouponServlet initialized successfully");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("CART");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        if (cart == null || cart.getItems().isEmpty()) {
            request.setAttribute("error", "Giỏ hàng trống.");
            request.getRequestDispatcher("/checkout").forward(request, response);
            return;
        }

        String couponCode = request.getParameter("couponCode");
        try {
            double totalPrice = 0;
            for (var item : cart.getItems()) {
                double price = item.getProduct().getSalePrice() != null ? item.getProduct().getSalePrice() : item.getProduct().getPrice();
                totalPrice += price * item.getQuantity();
            }

            Coupon coupon = couponDAO.getValidCoupon(couponCode, totalPrice);
            if (coupon == null) {
                request.setAttribute("error", "Mã giảm giá không hợp lệ hoặc không áp dụng được.");
                request.getRequestDispatcher("/checkout").forward(request, response);
                return;
            }

            double discountAmount;
            if ("percentage".equals(coupon.getDiscountType())) {
                discountAmount = totalPrice * (coupon.getDiscountValue() / 100);
                if (coupon.getMaximumDiscount() > 0 && discountAmount > coupon.getMaximumDiscount()) {
                    discountAmount = coupon.getMaximumDiscount();
                }
            } else {
                discountAmount = coupon.getDiscountValue();
            }

            coupon.setDiscountAmount(discountAmount);
            session.setAttribute("COUPON", coupon);
            couponDAO.incrementUsageCount(coupon.getCouponId());
            response.sendRedirect("checkout");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi áp dụng mã giảm giá: " + e.getMessage(), e);
            request.setAttribute("error", "Lỗi khi áp dụng mã giảm giá: " + e.getMessage());
            request.getRequestDispatcher("/checkout").forward(request, response);
        }
    }
}