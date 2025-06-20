/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Coupon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CouponDAO {
    private static final Logger LOGGER = Logger.getLogger(CouponDAO.class.getName());
    private Connection conn;

    public CouponDAO() {
        conn = DBConnection.getConnection();
        if (conn == null) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu");
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu");
        }
    }

    public Coupon getValidCoupon(String code, double orderTotal) {
        String sql = "SELECT * FROM Coupons WHERE code = ? AND status = 'active' AND start_date <= GETDATE() AND end_date >= GETDATE() AND usage_count < usage_limit AND minimum_order <= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setDouble(2, orderTotal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coupon coupon = new Coupon();
                    coupon.setCouponId(rs.getInt("coupon_id"));
                    coupon.setCode(rs.getString("code"));
                    coupon.setDiscountType(rs.getString("discount_type"));
                    coupon.setDiscountValue(rs.getDouble("discount_value"));
                    coupon.setMinimumOrder(rs.getDouble("minimum_order"));
                    coupon.setMaximumDiscount(rs.getDouble("maximum_discount"));
                    return coupon;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra mã giảm giá: " + code, e);
            throw new RuntimeException("Lỗi khi kiểm tra mã giảm giá: " + e.getMessage(), e);
        }
        return null;
    }

    public void incrementUsageCount(int couponId) {
        String sql = "UPDATE Coupons SET usage_count = usage_count + 1 WHERE coupon_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tăng số lần sử dụng mã giảm giá", e);
            throw new RuntimeException("Lỗi khi cập nhật mã giảm giá: " + e.getMessage(), e);
        }
    }
}