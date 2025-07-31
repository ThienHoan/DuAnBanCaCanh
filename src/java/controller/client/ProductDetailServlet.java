package controller.client;

import dao.impl.CategoryDAO;
import dao.impl.ProductDAO;
import dao.impl.ProductDetailDAO;
import dao.impl.ProductImageDAO;
import dao.impl.UserDAO; // Thêm import này
import dao.impl.pAttribute.ProductAttributeDAO;
import dao.impl.pAttribute.ProductAttributeValueDAO;
import dao.impl.pReview.ReviewDAO;
import dao.impl.pReview.ReviewImageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.entity.Category;
import model.entity.Product;
import model.entity.ProductDetail;
import model.entity.ProductImage;
import model.entity.User; // Thêm import này
import model.entity.pAttribute.ProductAttribute;
import model.entity.pAttribute.ProductAttributeValue;
import model.entity.pReview.Review;
import model.entity.pReview.ReviewImage;
import service.impl.ReviewServiceImpl;
import service.interfaces.ReviewService;
import utils.SessionUtils;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product-detail"})
public class ProductDetailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String productIdParam = request.getParameter("id");
            if (productIdParam == null || productIdParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            int productId = Integer.parseInt(productIdParam);
            
            // Fetch product
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductById(productId);
            System.out.println("product +:" +product);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            
            // Fetch category
            Category category = new CategoryDAO().getCategoryById(product.getCategoryId());

            // Fetch product detail
            ProductDetail productDetail = new ProductDetailDAO().getProductDetailByProductId(productId);

            // Fetch product images
            ProductImageDAO productImageDAO = new ProductImageDAO();
            List<ProductImage> productImages = productImageDAO.getImagesByProductId(productId);
            ProductImage mainImage = productImageDAO.getMainImageByProductId(productId);

            // Fetch product attributes & values
            List<ProductAttribute> productAttributes = new ProductAttributeDAO().getAllProductAttributes();
            List<Product> sameNameProducts = productDAO.getProductsWithSameName(product.getName());
            List<ProductAttributeValue> productAttributeValues = new ProductAttributeValueDAO()
                    .getProductAttributeValuesByProductId(productId);
            List<ProductAttributeValue> productAttributeValueSameNameProduct = new ProductAttributeValueDAO()
                    .getProductAttributeValuesByProductList(sameNameProducts);
            
            // Chuẩn bị Map: color -> productId (chỉ giữ unique, mỗi màu chỉ lấy 1 productId)
            Map<String, Integer> colorToProductId = new LinkedHashMap<>();
            for (ProductAttributeValue attr : productAttributeValueSameNameProduct) {
                if (attr.getAttributeId() == 1 && (attr.getIsDeleted() == null || attr.getIsDeleted() == 0)) {
                    // Nếu chưa có màu này thì mới cho vào map (đảm bảo không trùng)
                    colorToProductId.putIfAbsent(attr.getValue(), attr.getProductId());
                }
            }

            String currentColor = new ProductAttributeValueDAO().getCurrentColorByProductId(productId);
            List<ProductAttributeValue> productAttributeValueSizes = getSizesByCurrentColor(productAttributeValueSameNameProduct, currentColor);

            // After getting productSameCategory
            List<Product> productSameCategory = new ProductDAO().getProductsByCategoryId(product.getCategoryId());

            // Create a filtered list with unique product names
            List<Product> uniqueNameProductSameCategory = new ArrayList<>();
            Set<String> productNameUnique = new HashSet<>();

            for (Product p : productSameCategory) {
                // Skip the current product and only add products with unique names
                if (!productNameUnique.contains(p.getName().toLowerCase().trim())) {
                    uniqueNameProductSameCategory.add(p);
                    productNameUnique.add(p.getName().toLowerCase().trim());
                }
            }

            // Tạo map để lưu hình ảnh sản phẩm
            Map<Integer, String> uniqueNameProductSameCategoryImages = new HashMap<>();

            // Lấy ProductImageDAO
            ProductImageDAO imageDAO = new ProductImageDAO();

            // Lấy hình ảnh cho mỗi sản phẩm liên quan
            for (Product p : uniqueNameProductSameCategory) {
                // Lấy hình ảnh chính
                ProductImage mainImageUniqueNameProductSameCategory = imageDAO.getMainImageByProductId(p.getProductId());
                if (mainImageUniqueNameProductSameCategory != null) {
                    uniqueNameProductSameCategoryImages.put(p.getProductId(), mainImageUniqueNameProductSameCategory.getImageUrl());
                }
            }
            
            // --- PHẦN XỬ LÝ REVIEW ĐÃ ĐƯỢC TỐI ƯU ---
            
            // Bước 1: Lấy danh sách review và tất cả ảnh liên quan
            ReviewDAO reviewDAO = new ReviewDAO();
            List<Review> reviews = reviewDAO.getAllReviewsByProductIdForCus(productId);
            List<ReviewImage> allReviewImages = new ReviewImageDAO().getReviewImagesByReviewList(reviews);
            
            // Bước 2: Lấy danh sách tất cả userId từ reviews để truy vấn user một lần
            Set<Integer> userIds = new HashSet<>();
            for (Review review : reviews) {
                if (review.getUserId() != null) {
                    userIds.add(review.getUserId());
                }
            }
            
            // Bước 3: Lấy thông tin user cho tất cả userId cùng một lúc (tối ưu hơn N+1 query)
            UserDAO userDAO = new UserDAO();
            Map<Integer, String> userIdToUsernameMap = new HashMap<>();
            for (Integer userId : userIds) {
                User user = userDAO.getUserById(userId);
                if (user != null) {
                    userIdToUsernameMap.put(userId, user.getUsername());
                } else {
                    userIdToUsernameMap.put(userId, "Người dùng ẩn danh");
                }
            }

            // Bước 4: Nhóm các ảnh vào một Map để tra cứu nhanh theo reviewId
            Map<Integer, List<ReviewImage>> imagesByReviewId = new HashMap<>();
            for (ReviewImage image : allReviewImages) {
                imagesByReviewId.computeIfAbsent(image.getReviewId(), k -> new ArrayList<>()).add(image);
            }

            // Bước 5: Tạo một danh sách mới để chứa dữ liệu đã xử lý hoàn chỉnh
            List<Map<String, Object>> processedReviews = new ArrayList<>();
            for (Review review : reviews) {
                Map<String, Object> reviewData = new HashMap<>();

                // Lấy username từ map đã chuẩn bị
                String username = userIdToUsernameMap.get(review.getUserId());
                if (username == null) {
                    username = "Người dùng ẩn danh";
                }

                reviewData.put("review", review); // Đưa object review gốc vào
                reviewData.put("username", username); // Đưa username đã lấy được vào
                
                // Lấy danh sách ảnh tương ứng, nếu không có thì tạo list rỗng
                List<ReviewImage> reviewImages = imagesByReviewId.get(review.getReviewId());
                if (reviewImages == null) {
                    reviewImages = new ArrayList<>();
                }
                reviewData.put("images", reviewImages);

                processedReviews.add(reviewData);
            }
            
            // Đẩy lên request
            request.setAttribute("colorToProductId", colorToProductId);
                       
            // Đẩy dữ liệu lên JSP
            request.setAttribute("product", product);
            request.setAttribute("category", category);
            request.setAttribute("productDetail", productDetail);
            request.setAttribute("productImages", productImages);
            request.setAttribute("mainImage", mainImage);
            request.setAttribute("productAttributes", productAttributes);
            request.setAttribute("productAttributeValues", productAttributeValues);
            request.setAttribute("productAttributeValueSameNameProduct", productAttributeValueSameNameProduct);
            request.setAttribute("productAttributeValueSizes", productAttributeValueSizes);
            request.setAttribute("uniqueNameProductSameCategory", uniqueNameProductSameCategory);
            request.setAttribute("uniqueNameProductSameCategoryImages", uniqueNameProductSameCategoryImages);
            
            // --- PHẦN KIỂM TRA QUYỀN ĐÁNH GIÁ ---
            User currentUser = SessionUtils.getUser(request.getSession());
            ReviewService reviewService = new ReviewServiceImpl();
            
            boolean canReview = false;
            boolean hasUserReview = false;
            Review userReview = null;
            
            if (currentUser != null) {
                // Kiểm tra xem user đã mua sản phẩm chưa
                canReview = reviewService.hasUserPurchasedProduct(currentUser.getUserId(), productId);
                
                // Kiểm tra xem user đã đánh giá chưa
                hasUserReview = reviewService.hasUserReviewedProduct(currentUser.getUserId(), productId);
                
                // Nếu đã đánh giá, lấy thông tin review của user
                if (hasUserReview) {
                    List<Review> userReviews = reviewDAO.getReviewsByUserId(currentUser.getUserId());
                    for (Review review : userReviews) {
                        if (review.getProductId() != null && review.getProductId() == productId) {
                            userReview = review;
                            break;
                        }
                    }
                    
                    // Nếu tìm thấy review của user, lấy thêm thông tin user và ảnh
                    if (userReview != null) {
                        // Lấy username
                        User user = userDAO.getUserById(userReview.getUserId());
                        String username = (user != null) ? user.getUsername() : "Người dùng ẩn danh";
                        
                        // Lấy ảnh review
                        List<ReviewImage> userReviewImages = new ReviewImageDAO().getReviewImagesByReviewId(userReview.getReviewId());
                        
                        // Tạo Map chứa thông tin đầy đủ
                        Map<String, Object> userReviewData = new HashMap<>();
                        userReviewData.put("review", userReview);
                        userReviewData.put("username", username);
                        userReviewData.put("images", userReviewImages);
                        
                        request.setAttribute("userReviewData", userReviewData);
                    }
                }
            }
            
            // Tính toán thống kê review
            double averageRating = reviewService.getAverageRatingByProductId(productId);
            int totalReviews = reviewService.getReviewCountByProductId(productId);
            int[] ratingCounts = reviewService.getReviewCountsByRating(productId);
            
            // Đếm số lượng review theo từng sao
            int star1 = 0, star2 = 0, star3 = 0, star4 = 0, star5 = 0;
            for (Review review : reviews) {
                if (review.getRating() != null) {
                    switch (review.getRating()) {
                        case 1: star1++; break;
                        case 2: star2++; break;
                        case 3: star3++; break;
                        case 4: star4++; break;
                        case 5: star5++; break;
                    }
                }
            }
            
            // Gửi danh sách reviews đã xử lý
            request.setAttribute("reviews", reviews);
            request.setAttribute("processedReviews", processedReviews);
            
            // Debug log
            System.out.println("DEBUG - Total reviews found: " + (reviews != null ? reviews.size() : 0));
            System.out.println("DEBUG - Current user: " + (currentUser != null ? currentUser.getUsername() : "null"));
            System.out.println("DEBUG - Can review: " + canReview);
            System.out.println("DEBUG - Has user review: " + hasUserReview);
            System.out.println("DEBUG - Total reviews for display: " + totalReviews);
            System.out.println("DEBUG - Average rating: " + averageRating);
            
            // Gửi thông tin quyền đánh giá
            request.setAttribute("user", currentUser);
            request.setAttribute("canReview", canReview);
            request.setAttribute("hasUserReview", hasUserReview);
            request.setAttribute("userReview", userReview);
            
            // Gửi thống kê review
            request.setAttribute("averageRating", averageRating);
            request.setAttribute("totalReviews", totalReviews);
            request.setAttribute("ratingCounts", ratingCounts);
            request.setAttribute("star1", star1);
            request.setAttribute("star2", star2);
            request.setAttribute("star3", star3);
            request.setAttribute("star4", star4);
            request.setAttribute("star5", star5);

            // Forward đến JSP
            request.getRequestDispatcher("product_detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
    
    private List<ProductAttributeValue> getSizesByCurrentColor(
            List<ProductAttributeValue> productAttributeValueSameNameProduct, 
            String currentColor) {
        
        List<ProductAttributeValue> sizeList = new ArrayList<>();
        
        // Bước 1: Tìm tất cả product ID có màu hiện tại
        List<Integer> productIdsWithCurrentColor = new ArrayList<>();
        for (ProductAttributeValue attr : productAttributeValueSameNameProduct) {
            if (attr.getAttributeId() == 1 && // attribute ID = 1 là màu
                currentColor.equals(attr.getValue()) && 
                (attr.getIsDeleted() == null || attr.getIsDeleted() == 0)) {
                productIdsWithCurrentColor.add(attr.getProductId());
            }
        }
        
        // Bước 2: Lấy tất cả size (attribute ID = 2) của các product ID đó
        for (ProductAttributeValue attr : productAttributeValueSameNameProduct) {
            if (attr.getAttributeId() == 2 && // attribute ID = 2 là size
                productIdsWithCurrentColor.contains(attr.getProductId()) &&
                (attr.getIsDeleted() == null || attr.getIsDeleted() == 0)) {
                sizeList.add(attr);
            }
        }
        
        return sizeList;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}