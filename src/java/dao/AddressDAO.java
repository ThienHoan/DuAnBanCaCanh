package dao;

import model.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressDAO {
    private static final Logger LOGGER = Logger.getLogger(AddressDAO.class.getName());
    private Connection conn;

    public AddressDAO() {
        conn = DBConnection.getConnection();
        if (conn == null) {
            LOGGER.severe("Không thể kết nối tới cơ sở dữ liệu");
            throw new RuntimeException("Không thể kết nối tới cơ sở dữ liệu");
        }
    }

public List<Address> getAddressesByUserId(int userId) {
    List<Address> addresses = new ArrayList<>();
    String sql = "SELECT * FROM Addresses WHERE user_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Address address = new Address();
                address.setAddressId(rs.getInt("address_id"));
                address.setUserId(rs.getInt("user_id"));
                address.setRecipientName(rs.getString("recipient_name"));
                address.setPhone(rs.getString("phone"));
                address.setProvince(rs.getString("province"));
                address.setDistrict(rs.getString("district"));
                address.setWard(rs.getString("ward"));
                address.setAddressDetail(rs.getString("address_detail"));
                address.setIsDefault(rs.getBoolean("is_default")); // Use setIsDefault
                address.setAddressType(rs.getString("address_type"));
                address.setDeleted(rs.getBoolean("is_deleted")); // Use setDeleted
                addresses.add(address);
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Lỗi khi lấy địa chỉ: " + e.getMessage(), e);
        throw new RuntimeException("Lỗi khi lấy địa chỉ: " + e.getMessage(), e);
    }
    return addresses;
}


public int addAddress(Address address) {
    String sql = "INSERT INTO Addresses (user_id, recipient_name, phone, province, district, ward, address_detail, is_default, address_type, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, address.getUserId());
        ps.setString(2, address.getRecipientName());
        ps.setString(3, address.getPhone());
        ps.setString(4, address.getProvince());
        ps.setString(5, address.getDistrict());
        ps.setString(6, address.getWard());
        ps.setString(7, address.getAddressDetail());
        ps.setBoolean(8, address.getIsDefault()); // Use getIsDefault
        ps.setString(9, address.getAddressType());
        ps.setBoolean(10, address.getIsDeleted()); // Use getIsDeleted
        ps.executeUpdate();
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Lỗi khi thêm địa chỉ: " + e.getMessage(), e);
        throw new RuntimeException("Lỗi khi thêm địa chỉ: " + e.getMessage(), e);
    }
    return 0;
}
}