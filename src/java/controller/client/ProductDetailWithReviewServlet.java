package controller.client;

import dao.impl.ProductDAO;
import dao.impl.pReview.ReviewDAO;
import dao.impl.pReview.ReviewImageDAO;
import model.entity.Product;
import model.entity.pReview.Review;
import model.entity.pReview.ReviewImage;
import service.impl.ReviewServiceImpl;
import service.interfaces.ReviewService;
import utils.SessionUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailWithReviewServlet", urlPatterns = {"/product-detail-with-review"})
public class ProductDetailWithReviewServlet extends HttpServlet {
    
    private ProductDAO productDAO;
    private ReviewService reviewService;
    private ReviewImageDAO reviewImageDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
        reviewService = new ReviewServiceImpl();
        reviewImageDAO = new ReviewImageDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get product ID from request
            String productIdStr = request.getParameter("id");
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            
            // Get product details
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            
            // Get reviews for this product
            List<Review> reviews = reviewService.getReviewsByProductId(productId);
            
            // Load images for each review
            for (Review review : reviews) {
                List<ReviewImage> images = reviewImageDAO.getReviewImagesByReviewId(review.getReviewId());
                // You could store these images in the Review object if needed
                // For now, we'll pass them separately
                request.setAttribute("reviewImages_" + review.getReviewId(), images);
            }
            
            // Get review statistics
            double averageRating = reviewService.getAverageRatingByProductId(productId);
            int totalReviews = reviewService.getReviewCountByProductId(productId);
            int[] ratingCounts = reviewService.getReviewCountsByRating(productId);
            
            // Set attributes for JSP
            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.setAttribute("averageRating", averageRating);
            request.setAttribute("totalReviews", totalReviews);
            request.setAttribute("ratingCounts", ratingCounts);
            request.setAttribute("user", SessionUtils.getUser(request.getSession()));
            
            // Forward to product detail page
            request.getRequestDispatcher("/product_detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 