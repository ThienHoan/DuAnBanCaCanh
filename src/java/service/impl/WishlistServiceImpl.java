package service.impl;

import service.interfaces.WishlistService;
import dao.interfaces.WishlistDAO;
import dao.impl.WishlistDAOImpl;
import model.entity.Wishlist;
import dao.interfaces.WishlistDAO.WishlistStatistics;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Implementation của WishlistService
 */
public class WishlistServiceImpl implements WishlistService {
    private static final Logger LOGGER = Logger.getLogger(WishlistServiceImpl.class.getName());
    private final WishlistDAO wishlistDAO;
    
    public WishlistServiceImpl() {
        this.wishlistDAO = new WishlistDAOImpl();
    }
    
    @Override
    public boolean addToWishlist(int userId, int productId) {
        try {
            // Validate dữ liệu
            if (!validateWishlistData(userId, productId)) {
                LOGGER.warning("Invalid wishlist data: userId=" + userId + ", productId=" + productId);
                return false;
            }
            
            // Kiểm tra xem đã có trong wishlist chưa
            if (isInWishlist(userId, productId)) {
                LOGGER.info("Product already in wishlist: userId=" + userId + ", productId=" + productId);
                return true; // Coi như thành công vì đã có sẵn
            }
            
            boolean result = wishlistDAO.addToWishlist(userId, productId);
            if (result) {
                LOGGER.info("Added to wishlist successfully: userId=" + userId + ", productId=" + productId);
            }
            return result;
            
        } catch (Exception e) {
            LOGGER.severe("Error adding to wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean removeFromWishlist(int userId, int productId) {
        try {
            if (!validateWishlistData(userId, productId)) {
                return false;
            }
            
            boolean result = wishlistDAO.removeFromWishlist(userId, productId);
            if (result) {
                LOGGER.info("Removed from wishlist successfully: userId=" + userId + ", productId=" + productId);
            }
            return result;
            
        } catch (Exception e) {
            LOGGER.severe("Error removing from wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean removeWishlistItem(int wishlistId, int userId) {
        try {
            if (wishlistId <= 0 || userId <= 0) {
                return false;
            }
            
            boolean result = wishlistDAO.removeWishlistItem(wishlistId, userId);
            if (result) {
                LOGGER.info("Removed wishlist item successfully: wishlistId=" + wishlistId + ", userId=" + userId);
            }
            return result;
            
        } catch (Exception e) {
            LOGGER.severe("Error removing wishlist item: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean toggleWishlist(int userId, int productId) {
        try {
            if (!validateWishlistData(userId, productId)) {
                return false;
            }
            
            if (isInWishlist(userId, productId)) {
                // Nếu đã có thì xóa
                return !removeFromWishlist(userId, productId); // Return false để biết đã xóa
            } else {
                // Nếu chưa có thì thêm
                return addToWishlist(userId, productId); // Return true để biết đã thêm
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error toggling wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isInWishlist(int userId, int productId) {
        try {
            if (!validateWishlistData(userId, productId)) {
                return false;
            }
            
            return wishlistDAO.isInWishlist(userId, productId);
            
        } catch (Exception e) {
            LOGGER.severe("Error checking wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Wishlist> getUserWishlist(int userId) {        try {
            if (userId <= 0) {
                LOGGER.warning("Invalid userId: " + userId);
                return Collections.emptyList(); // Return empty list
            }
            
            return wishlistDAO.getWishlistByUser(userId);
            
        } catch (Exception e) {
            LOGGER.severe("Error getting user wishlist: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    @Override
    public int getWishlistCount(int userId) {
        try {
            if (userId <= 0) {
                return 0;
            }
            
            return wishlistDAO.getWishlistCount(userId);
            
        } catch (Exception e) {
            LOGGER.severe("Error getting wishlist count: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean clearWishlist(int userId) {
        try {
            if (userId <= 0) {
                return false;
            }
            
            boolean result = wishlistDAO.clearWishlist(userId);
            if (result) {
                LOGGER.info("Cleared wishlist successfully: userId=" + userId);
            }
            return result;
            
        } catch (Exception e) {
            LOGGER.severe("Error clearing wishlist: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<WishlistStatistics> getMostWishedProducts(int limit) {        try {
            if (limit <= 0) {
                limit = 10; // Default limit
            }
            
            return wishlistDAO.getMostWishedProducts(limit);
            
        } catch (Exception e) {
            LOGGER.severe("Error getting most wished products: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    @Override
    public boolean validateWishlistData(int userId, int productId) {
        if (userId <= 0) {
            LOGGER.warning("Invalid userId: " + userId);
            return false;
        }
        
        if (productId <= 0) {
            LOGGER.warning("Invalid productId: " + productId);
            return false;
        }
        
        return true;
    }
}
