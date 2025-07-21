package utils;

import dao.impl.InventoryLogDAOImpl;
import dao.interfaces.InventoryLogDAO;
import model.entity.InventoryLog;
import model.entity.Product;

/**
 * Utility class for inventory log operations
 */
public class InventoryLogUtil {
    
    private static final InventoryLogDAO inventoryLogDAO = new InventoryLogDAOImpl();
    
    /**
     * Create an inventory log when a product's quantity changes
     * 
     * @param product The product whose quantity changed
     * @param oldQuantity The quantity before the change
     * @param reason The reason for the change
     * @param referenceId Optional reference ID (e.g., order ID)
     * @param referenceType Optional reference type (e.g., "order", "manual_adjustment")
     * @return true if the log was created successfully, false otherwise
     */
    public static boolean logInventoryChange(Product product, int oldQuantity, String reason, 
                                             Integer referenceId, String referenceType) {
        if (product == null || oldQuantity == product.getQuantity()) {
            return false; // No change or invalid product
        }
        
        String changeType = (product.getQuantity() > oldQuantity) ? "increase" : "decrease";
        
        InventoryLog log = new InventoryLog(
                product.getProductId(),
                oldQuantity,
                product.getQuantity(),
                changeType,
                reason,
                referenceId,
                referenceType
        );
        
        return inventoryLogDAO.insertInventoryLog(log);
    }
    
    /**
     * Simplified version without reference ID and type
     */
    public static boolean logInventoryChange(Product product, int oldQuantity, String reason) {
        return logInventoryChange(product, oldQuantity, reason, null, null);
    }
}