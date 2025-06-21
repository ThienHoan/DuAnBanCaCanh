package service.interfaces;

import model.entity.Wishlist;
import dao.interfaces.WishlistDAO.WishlistStatistics;
import java.util.List;

/**
 * Service interface cho Wishlist
 */
public interface WishlistService {
    
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
     * Xóa item khỏi wishlist bằng wishlist ID
     * @param wishlistId ID của wishlist item
     * @param userId ID của user
     * @return true nếu thành công, false nếu thất bại
     */
    boolean removeWishlistItem(int wishlistId, int userId);
    
    /**
     * Toggle wishlist - thêm nếu chưa có, xóa nếu đã có
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return true nếu đã thêm, false nếu đã xóa
     */
    boolean toggleWishlist(int userId, int productId);
    
    /**
     * Kiểm tra sản phẩm có trong wishlist không
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return true nếu có, false nếu không
     */
    boolean isInWishlist(int userId, int productId);
    
    /**
     * Lấy danh sách wishlist của user
     * @param userId ID của user
     * @return List wishlist items
     */
    List<Wishlist> getUserWishlist(int userId);
    
    /**
     * Lấy số lượng item trong wishlist
     * @param userId ID của user
     * @return số lượng item
     */
    int getWishlistCount(int userId);
    
    /**
     * Xóa tất cả item trong wishlist
     * @param userId ID của user
     * @return true nếu thành công, false nếu thất bại
     */
    boolean clearWishlist(int userId);
    
    /**
     * Lấy danh sách sản phẩm được yêu thích nhiều nhất
     * @param limit số lượng sản phẩm
     * @return List sản phẩm phổ biến
     */
    List<WishlistStatistics> getMostWishedProducts(int limit);
    
    /**
     * Validate dữ liệu trước khi thêm vào wishlist
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return true nếu hợp lệ, false nếu không
     */
    boolean validateWishlistData(int userId, int productId);
}
