package dao.interfaces;

import model.entity.Wishlist;
import java.util.List;

/**
 * Interface cho Wishlist Data Access Object
 */
public interface WishlistDAO {
    
    /**
     * Thêm sản phẩm vào wishlist
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return true nếu thành công, false nếu thất bại
     */
    boolean addToWishlist(int userId, int productId);
    
    /**
     * Xóa sản phẩm khỏi wishlist
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return true nếu thành công, false nếu thất bại
     */
    boolean removeFromWishlist(int userId, int productId);
    
    /**
     * Xóa một item khỏi wishlist bằng wishlist ID
     * @param wishlistId ID của wishlist item
     * @param userId ID của user (để verify ownership)
     * @return true nếu thành công, false nếu thất bại
     */
    boolean removeWishlistItem(int wishlistId, int userId);
    
    /**
     * Kiểm tra sản phẩm có trong wishlist của user không
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return true nếu có, false nếu không
     */
    boolean isInWishlist(int userId, int productId);
    
    /**
     * Lấy danh sách wishlist của user
     * @param userId ID của user
     * @return List các wishlist item với thông tin sản phẩm
     */
    List<Wishlist> getWishlistByUser(int userId);
    
    /**
     * Lấy số lượng item trong wishlist của user
     * @param userId ID của user
     * @return số lượng item
     */
    int getWishlistCount(int userId);
    
    /**
     * Xóa tất cả item trong wishlist của user
     * @param userId ID của user
     * @return true nếu thành công, false nếu thất bại
     */
    boolean clearWishlist(int userId);
    
    /**
     * Lấy danh sách sản phẩm được yêu thích nhiều nhất
     * @param limit số lượng sản phẩm muốn lấy
     * @return List các sản phẩm phổ biến trong wishlist
     */
    List<WishlistStatistics> getMostWishedProducts(int limit);
    
    /**
     * Class để lưu thống kê wishlist
     */
    class WishlistStatistics {
        private int productId;
        private String productName;
        private String productImageUrl;
        private double productPrice;
        private int wishlistCount;
        
        public WishlistStatistics() {}
        
        public WishlistStatistics(int productId, String productName, String productImageUrl, 
                                double productPrice, int wishlistCount) {
            this.productId = productId;
            this.productName = productName;
            this.productImageUrl = productImageUrl;
            this.productPrice = productPrice;
            this.wishlistCount = wishlistCount;
        }
        
        // Getters and Setters
        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getProductImageUrl() { return productImageUrl; }
        public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
        
        public double getProductPrice() { return productPrice; }
        public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
        
        public int getWishlistCount() { return wishlistCount; }
        public void setWishlistCount(int wishlistCount) { this.wishlistCount = wishlistCount; }
    }
}
