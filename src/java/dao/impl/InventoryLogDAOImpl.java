package dao.impl;

import dao.interfaces.InventoryLogDAO;
import model.entity.InventoryLog;
import utils.db.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of InventoryLogDAO for database operations
 */
public class InventoryLogDAOImpl implements InventoryLogDAO {
    private static final Logger LOGGER = Logger.getLogger(InventoryLogDAOImpl.class.getName());

    @Override
    public List<InventoryLog> getAllInventoryLogs() {
        List<InventoryLog> logs = new ArrayList<>();
        String query = "SELECT il.log_id, il.product_id, il.quantity_before, il.quantity_after, " +
                       "il.change_type, il.reason, il.reference_id, il.reference_type, il.created_at, " +
                       "p.name AS product_name, p.sku AS product_sku " +
                       "FROM Inventory_logs il " +
                       "JOIN Products p ON il.product_id = p.product_id " +
                       "ORDER BY il.created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                InventoryLog log = mapResultSetToInventoryLog(rs);
                logs.add(log);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all inventory logs", e);
        }
        
        return logs;
    }

    @Override
    public List<InventoryLog> getInventoryLogsByProductId(int productId) {
        List<InventoryLog> logs = new ArrayList<>();
        String query = "SELECT il.log_id, il.product_id, il.quantity_before, il.quantity_after, " +
                       "il.change_type, il.reason, il.reference_id, il.reference_type, il.created_at, " +
                       "p.name AS product_name, p.sku AS product_sku " +
                       "FROM Inventory_logs il " +
                       "JOIN Products p ON il.product_id = p.product_id " +
                       "WHERE il.product_id = ? " +
                       "ORDER BY il.created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, productId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryLog log = mapResultSetToInventoryLog(rs);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving inventory logs for product ID: " + productId, e);
        }
        
        return logs;
    }

    @Override
    public List<InventoryLog> getRecentInventoryLogs(int limit) {
        List<InventoryLog> logs = new ArrayList<>();
        String query = "SELECT TOP(?) il.log_id, il.product_id, il.quantity_before, il.quantity_after, " +
                       "il.change_type, il.reason, il.reference_id, il.reference_type, il.created_at, " +
                       "p.name AS product_name, p.sku AS product_sku " +
                       "FROM Inventory_logs il " +
                       "JOIN Products p ON il.product_id = p.product_id " +
                       "ORDER BY il.created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryLog log = mapResultSetToInventoryLog(rs);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving recent inventory logs", e);
        }
        
        return logs;
    }

    @Override
    public boolean insertInventoryLog(InventoryLog log) {
        String query = "INSERT INTO Inventory_logs (product_id, quantity_before, quantity_after, " +
                       "change_type, reason, reference_id, reference_type, created_at) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, log.getProductId());
            ps.setInt(2, log.getQuantityBefore());
            ps.setInt(3, log.getQuantityAfter());
            ps.setString(4, log.getChangeType());
            ps.setString(5, log.getReason());
            if (log.getReferenceId() != null) {
                ps.setInt(6, log.getReferenceId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.setString(7, log.getReferenceType());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting inventory log", e);
            return false;
        }
    }

    @Override
    public List<InventoryLog> getInventoryLogsByChangeType(String changeType) {
        List<InventoryLog> logs = new ArrayList<>();
        String query = "SELECT il.log_id, il.product_id, il.quantity_before, il.quantity_after, " +
                       "il.change_type, il.reason, il.reference_id, il.reference_type, il.created_at, " +
                       "p.name AS product_name, p.sku AS product_sku " +
                       "FROM Inventory_logs il " +
                       "JOIN Products p ON il.product_id = p.product_id " +
                       "WHERE il.change_type = ? " +
                       "ORDER BY il.created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, changeType);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryLog log = mapResultSetToInventoryLog(rs);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving inventory logs by change type: " + changeType, e);
        }
        
        return logs;
    }
    
    // Helper method to map ResultSet to InventoryLog object
    private InventoryLog mapResultSetToInventoryLog(ResultSet rs) throws SQLException {
        InventoryLog log = new InventoryLog();
        log.setLogId(rs.getInt("log_id"));
        log.setProductId(rs.getInt("product_id"));
        log.setQuantityBefore(rs.getInt("quantity_before"));
        log.setQuantityAfter(rs.getInt("quantity_after"));
        log.setChangeType(rs.getString("change_type"));
        log.setReason(rs.getString("reason"));
        
        // Handle nullable fields
        int referenceId = rs.getInt("reference_id");
        if (!rs.wasNull()) {
            log.setReferenceId(referenceId);
        }
        
        log.setReferenceType(rs.getString("reference_type"));
        log.setCreatedAt(rs.getString("created_at"));
        
        // Product information
        log.setProductName(rs.getString("product_name"));
        log.setProductSku(rs.getString("product_sku"));
        
        return log;
    }
} 