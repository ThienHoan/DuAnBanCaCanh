package dao.interfaces;

import model.entity.InventoryLog;
import java.util.List;

/**
 * Interface for InventoryLog data access operations
 */
public interface InventoryLogDAO {
    
    /**
     * Get all inventory logs with product information
     * @return List of inventory logs
     */
    List<InventoryLog> getAllInventoryLogs();
    
    /**
     * Get inventory logs for a specific product with product information
     * @param productId Product ID to filter logs for
     * @return List of inventory logs for the specified product
     */
    List<InventoryLog> getInventoryLogsByProductId(int productId);
    
    /**
     * Get recent inventory logs with product information
     * @param limit Maximum number of logs to return
     * @return List of recent inventory logs
     */
    List<InventoryLog> getRecentInventoryLogs(int limit);
    
    /**
     * Insert a new inventory log
     * @param log The inventory log to insert
     * @return true if successful, false otherwise
     */
    boolean insertInventoryLog(InventoryLog log);
    
    /**
     * Get inventory logs by change type with product information
     * @param changeType Type of change (increase, decrease)
     * @return List of inventory logs with the specified change type
     */
    List<InventoryLog> getInventoryLogsByChangeType(String changeType);
} 