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
            
            // Gửi danh sách đã xử lý tới JSP, không cần gửi reviews và reviewImages riêng lẻ nữa
            request.setAttribute("processedReviews", processedReviews);

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