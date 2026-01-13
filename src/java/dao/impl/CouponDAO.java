package dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Collections;

import model.entity.Coupon;
import model.entity.CouponProduct;
import utils.db.DBContext;

public class CouponDAO {
    private static final Logger LOGGER = Logger.getLogger(CouponDAO.class.getName());
    private final Connection conn;

    public CouponDAO() {
        try {
            conn = DBContext.getConnection();
        } catch (Exception e) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu: " + e.getMessage());
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu", e);
        }
    }

    /**
     * Lấy thông tin mã giảm giá theo code
     */
    public Coupon getCouponByCode(String code) {
        String sql = "SELECT * FROM Coupons WHERE code = ? AND is_deleted = 0 AND status = 'active' AND GETDATE() BETWEEN start_date AND end_date";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCoupon(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin mã giảm giá: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Kiểm tra xem mã giảm giá có hợp lệ cho người dùng không
     */
    public boolean isValidForUser(String code, int userId) {
        // Kiểm tra mã giảm giá có tồn tại và còn hiệu lực không
        Coupon coupon = getCouponByCode(code);
        if (coupon == null) {
            return false;
        }

        // Kiểm tra xem người dùng đã sử dụng mã giảm giá này chưa
        String sql = "SELECT COUNT(*) FROM Coupon_usage WHERE coupon_id = ? AND user_id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coupon.getCouponId());
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int usageCount = rs.getInt(1);
                    // Nếu người dùng đã sử dụng mã này rồi, không cho sử dụng nữa
                    if (usageCount > 0) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra sử dụng mã giảm giá: " + e.getMessage(), e);
            return false;
        }

        // Kiểm tra số lượt sử dụng của mã giảm giá
        if (coupon.getUsageCount() >= coupon.getUsageLimit()) {
            return false;
        }

        return true;
    }

    /**
     * Kiểm tra xem mã giảm giá có hợp lệ cho giỏ hàng không
     */
    public boolean isValidForCart(String code, List<Integer> productIds, BigDecimal cartTotal) {
        Coupon coupon = getCouponByCode(code);
        if (coupon == null) {
            return false;
        }

        // Kiểm tra giá trị đơn hàng tối thiểu
        if (cartTotal.compareTo(coupon.getMinimumOrder()) < 0) {
            return false;
        }

        // Kiểm tra xem mã giảm giá có áp dụng cho sản phẩm cụ thể không
        List<CouponProduct> couponProducts = getCouponProducts(coupon.getCouponId());
        if (!couponProducts.isEmpty()) {
            // Nếu mã giảm giá chỉ áp dụng cho sản phẩm cụ thể, kiểm tra xem giỏ hàng có sản phẩm đó không
            boolean hasValidProduct = false;
            for (Integer productId : productIds) {
                for (CouponProduct couponProduct : couponProducts) {
                    if (couponProduct.getProductId() == productId) {
                        hasValidProduct = true;
                        break;
                    }
                }
                if (hasValidProduct) {
                    break;
                }
            }
            if (!hasValidProduct) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tính toán số tiền giảm giá
     */
    public BigDecimal calculateDiscount(String code, BigDecimal cartTotal, List<Integer> productIds) {
        Coupon coupon = getCouponByCode(code);
        if (coupon == null) {
            return BigDecimal.ZERO;
        }

        // Lấy danh sách sản phẩm được áp dụng mã giảm giá
        List<CouponProduct> couponProducts = getCouponProducts(coupon.getCouponId());

        BigDecimal eligibleAmount = cartTotal;

        // Nếu mã giảm giá chỉ áp dụng cho sản phẩm cụ thể
        if (!couponProducts.isEmpty()) {
            // Tính tổng giá trị các sản phẩm được áp dụng mã giảm giá
            eligibleAmount = getEligibleProductsTotal(couponProducts, productIds);
            
            // Nếu không có sản phẩm nào phù hợp, trả về 0
            if (eligibleAmount.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
        }

        BigDecimal discount;
        if ("percentage".equals(coupon.getDiscountType())) {
            // Giảm theo phần trăm
            BigDecimal percentageValue = coupon.getDiscountValue().divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
            discount = eligibleAmount.multiply(percentageValue).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            // Kiểm tra giảm tối đa
            if (coupon.getMaximumDiscount() != null && discount.compareTo(coupon.getMaximumDiscount()) > 0) {
                discount = coupon.getMaximumDiscount();
            }
        } else {
            // Giảm theo số tiền cố định
            discount = coupon.getDiscountValue();
            if (discount.compareTo(eligibleAmount) > 0) {
                discount = eligibleAmount;
            }
        }

        // Log để debug
        LOGGER.info("Mã giảm giá: " + code + ", Loại: " + coupon.getDiscountType() + 
                  ", Giá trị: " + coupon.getDiscountValue() + 
                  ", Tổng giỏ hàng: " + cartTotal + 
                  ", Tổng sản phẩm áp dụng: " + eligibleAmount +
                  ", Giảm giá: " + discount);

        return discount;
    }
    
    /**
     * Tính tổng giá trị các sản phẩm được áp dụng mã giảm giá
     */
    private BigDecimal getEligibleProductsTotal(List<CouponProduct> couponProducts, List<Integer> cartProductIds) {
        BigDecimal total = BigDecimal.ZERO;
        
        try {
            // Lấy danh sách ID sản phẩm được áp dụng mã giảm giá
            List<Integer> eligibleProductIds = couponProducts.stream()
                .map(CouponProduct::getProductId)
                .collect(Collectors.toList());
            
            // Lọc ra các sản phẩm trong giỏ hàng được áp dụng mã giảm giá
            List<Integer> matchingProductIds = cartProductIds.stream()
                .filter(eligibleProductIds::contains)
                .collect(Collectors.toList());
            
            if (matchingProductIds.isEmpty()) {
                return BigDecimal.ZERO;
            }
            
            // Tạo chuỗi tham số cho truy vấn SQL
            String placeholders = String.join(",", Collections.nCopies(matchingProductIds.size(), "?"));
            
            // Truy vấn tổng giá trị các sản phẩm
            String sql = "SELECT SUM(ci.quantity * p.price) as total FROM Cart_items ci " +
                         "JOIN Products p ON ci.product_id = p.product_id " +
                         "WHERE ci.product_id IN (" + placeholders + ")";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Thiết lập tham số
                for (int i = 0; i < matchingProductIds.size(); i++) {
                    ps.setInt(i + 1, matchingProductIds.get(i));
                }
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getBigDecimal("total");
                        if (total == null) {
                            total = BigDecimal.ZERO;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tính tổng giá trị sản phẩm áp dụng mã giảm giá: " + e.getMessage(), e);
        }
        
        return total;
    }

    /**
     * Ghi nhận việc sử dụng mã giảm giá
     */
    public boolean recordCouponUsage(int couponId, int userId, int orderId, BigDecimal discountAmount) {
        String sql = "INSERT INTO Coupon_usage (coupon_id, user_id, order_id, discount_amount, used_at, is_deleted) VALUES (?, ?, ?, ?, GETDATE(), 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            ps.setBigDecimal(4, discountAmount);
            ps.executeUpdate();

            // Cập nhật số lượt sử dụng của mã giảm giá
            updateCouponUsageCount(couponId);
            
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi ghi nhận sử dụng mã giảm giá: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Cập nhật số lượt sử dụng của mã giảm giá
     */
    private boolean updateCouponUsageCount(int couponId) {
        String sql = "UPDATE Coupons SET usage_count = usage_count + 1, updated_at = GETDATE() WHERE coupon_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật số lượt sử dụng mã giảm giá: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Lấy danh sách sản phẩm được áp dụng mã giảm giá
     */
    private List<CouponProduct> getCouponProducts(int couponId) {
        List<CouponProduct> couponProducts = new ArrayList<>();
        String sql = "SELECT * FROM Coupon_products WHERE coupon_id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CouponProduct couponProduct = new CouponProduct();
                    couponProduct.setCouponProductId(rs.getInt("coupon_product_id"));
                    couponProduct.setCouponId(rs.getInt("coupon_id"));
                    couponProduct.setProductId(rs.getInt("product_id"));
                    couponProduct.setIsDeleted(rs.getBoolean("is_deleted"));
                    couponProducts.add(couponProduct);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách sản phẩm áp dụng mã giảm giá: " + e.getMessage(), e);
        }
        return couponProducts;
    }

    /**
     * Map ResultSet to Coupon object
     */
    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setCouponId(rs.getInt("coupon_id"));
        coupon.setCode(rs.getString("code"));
        coupon.setDescription(rs.getString("description"));
        coupon.setDiscountType(rs.getString("discount_type"));
        coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
        coupon.setMinimumOrder(rs.getBigDecimal("minimum_order"));
        coupon.setMaximumDiscount(rs.getBigDecimal("maximum_discount"));
        coupon.setUsageLimit(rs.getInt("usage_limit"));
        coupon.setUsageCount(rs.getInt("usage_count"));
        coupon.setStartDate(rs.getTimestamp("start_date"));
        coupon.setEndDate(rs.getTimestamp("end_date"));
        coupon.setStatus(rs.getString("status"));
        coupon.setCreatedAt(rs.getTimestamp("created_at"));
        coupon.setUpdatedAt(rs.getTimestamp("updated_at"));
        coupon.setIsDeleted(rs.getBoolean("is_deleted"));
        return coupon;
    }
} 