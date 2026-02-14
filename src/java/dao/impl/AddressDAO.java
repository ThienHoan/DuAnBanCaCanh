package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.entity.Address;
import utils.db.DBContext;

public class AddressDAO {
    private static final Logger LOGGER = Logger.getLogger(AddressDAO.class.getName());
    private final Connection conn;

    public AddressDAO() {
        try {
            conn = DBContext.getConnection();
        } catch (Exception e) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu: " + e.getMessage());
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu", e);
        }
    }

    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM Addresses WHERE user_id = ? AND is_deleted = 0 ORDER BY is_default DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address address = mapResultSetToAddress(rs);
                    addresses.add(address);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách địa chỉ: " + e.getMessage(), e);
        }
        return addresses;
    }

    public Address getAddressById(int addressId) {
        String sql = "SELECT * FROM Addresses WHERE address_id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAddress(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông tin địa chỉ: " + e.getMessage(), e);
        }
        return null;
    }

    public Address getDefaultAddress(int userId) {
        String sql = "SELECT * FROM Addresses WHERE user_id = ? AND is_default = 1 AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAddress(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy địa chỉ mặc định: " + e.getMessage(), e);
        }
        return null;
    }

    public int addAddress(Address address) {
        // If this is the default address, unset any existing default
        if (address.isIsDefault()) {
            unsetDefaultAddress(address.getUserId());
        }

        String sql = "INSERT INTO Addresses (user_id, recipient_name, phone, province, district, ward, address_detail, is_default, address_type, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getRecipientName());
            ps.setString(3, address.getPhone());
            ps.setString(4, address.getProvince());
            ps.setString(5, address.getDistrict());
            ps.setString(6, address.getWard());
            ps.setString(7, address.getAddressDetail());
            ps.setBoolean(8, address.isIsDefault());
            ps.setString(9, address.getAddressType());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm địa chỉ: " + e.getMessage(), e);
        }
        return -1;
    }

    public boolean updateAddress(Address address) {
        // If this is the default address, unset any existing default
        if (address.isIsDefault()) {
            unsetDefaultAddress(address.getUserId());
        }

        String sql = "UPDATE Addresses SET recipient_name = ?, phone = ?, province = ?, district = ?, ward = ?, address_detail = ?, is_default = ?, address_type = ? WHERE address_id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getRecipientName());
            ps.setString(2, address.getPhone());
            ps.setString(3, address.getProvince());
            ps.setString(4, address.getDistrict());
            ps.setString(5, address.getWard());
            ps.setString(6, address.getAddressDetail());
            ps.setBoolean(7, address.isIsDefault());
            ps.setString(8, address.getAddressType());
            ps.setInt(9, address.getAddressId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật địa chỉ: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean deleteAddress(int addressId) {
        String sql = "UPDATE Addresses SET is_deleted = 1 WHERE address_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa địa chỉ: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean setDefaultAddress(int addressId, int userId) {
        try {
            conn.setAutoCommit(false);
            
            // Unset current default
            unsetDefaultAddress(userId);
            
            // Set new default
            String sql = "UPDATE Addresses SET is_default = 1 WHERE address_id = ? AND user_id = ? AND is_deleted = 0";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, addressId);
                ps.setInt(2, userId);
                int result = ps.executeUpdate();
                
                conn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Lỗi khi rollback transaction: " + ex.getMessage(), ex);
            }
            LOGGER.log(Level.SEVERE, "Lỗi khi đặt địa chỉ mặc định: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đặt lại autocommit: " + e.getMessage(), e);
            }
        }
    }

    private boolean unsetDefaultAddress(int userId) {
        String sql = "UPDATE Addresses SET is_default = 0 WHERE user_id = ? AND is_default = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi bỏ đặt địa chỉ mặc định: " + e.getMessage(), e);
            return false;
        }
    }

    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setAddressId(rs.getInt("address_id"));
        address.setUserId(rs.getInt("user_id"));
        address.setRecipientName(rs.getString("recipient_name"));
        address.setPhone(rs.getString("phone"));
        address.setProvince(rs.getString("province"));
        address.setDistrict(rs.getString("district"));
        address.setWard(rs.getString("ward"));
        address.setAddressDetail(rs.getString("address_detail"));
        address.setIsDefault(rs.getBoolean("is_default"));
        address.setAddressType(rs.getString("address_type"));
        address.setIsDeleted(rs.getBoolean("is_deleted"));
        return address;
    }
} 