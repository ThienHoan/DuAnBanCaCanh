package controller.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import model.entity.pReview.Review;
import model.entity.User;
import service.impl.ReviewServiceImpl;
import service.interfaces.ReviewService;
import utils.SessionUtils;

@WebServlet(name = "ReviewController", urlPatterns = {"/review"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 10,  // 10MB
    maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class ReviewController extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads/reviews";
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        super.init();
        reviewService = new ReviewServiceImpl();
        
        // Create upload directory if it doesn't exist
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to product detail page if someone tries to access this directly
        response.sendRedirect(request.getContextPath() + "/home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("submit".equals(action)) {
            submitReview(request, response);
        } else if ("delete".equals(action)) {
            deleteReview(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
    
    private void submitReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // Redirect to login page if not logged in
            response.sendRedirect(request.getContextPath() + "/login?redirect=product-detail?id=" + request.getParameter("productId"));
            return;
        }
        
        try {
            // Get form data
            int productId = Integer.parseInt(request.getParameter("productId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            
            // Validate input
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Invalid rating. Must be between 1 and 5.");
            }
            
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Review comment cannot be empty.");
            }
            
            // Check if user has already reviewed this product
            if (reviewService.hasUserReviewedProduct(user.getUserId(), productId)) {
                request.setAttribute("errorMessage", "You have already reviewed this product.");
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&reviewError=already_reviewed");
                return;
            }
            
            // Create review object
            Review review = new Review();
            review.setProductId(productId);
            review.setUserId(user.getUserId());
            review.setRating(rating);
            review.setComment(comment);
            review.setReviewDate(LocalDateTime.now());
            review.setStatus("approved"); // Default status
            review.setVerifiedPurchase(false); // Set to true if you have order verification logic
            review.setIsDeleted(0); // Not deleted
            
            // Process image uploads if any
            List<String> imageUrls = new ArrayList<>();
            Collection<Part> fileParts = request.getParts();
            
            for (Part part : fileParts) {
                if (part.getName().equals("reviewImages") && part.getSize() > 0) {
                    String fileName = getUniqueFileName(part);
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                    String filePath = uploadPath + File.separator + fileName;
                    
                    // Save the file
                    part.write(filePath);
                    
                    // Add the relative URL to the list
                    String imageUrl = request.getContextPath() + "/" + UPLOAD_DIRECTORY + "/" + fileName;
                    imageUrls.add(imageUrl);
                }
            }
            
            // Save the review and images
            int reviewId = reviewService.addReview(review, imageUrls);
            
            if (reviewId > 0) {
                // Success - redirect back to product page with success message
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&reviewSuccess=true");
            } else {
                // Error - redirect back with error message
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&reviewError=save_failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + request.getParameter("productId") + "&reviewError=invalid_input");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + request.getParameter("productId") + "&reviewError=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product-detail?id=" + request.getParameter("productId") + "&reviewError=system_error");
        }
    }
    
    private void deleteReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        User user = (User) SessionUtils.getUser(request);
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            
            // TODO: Add check to verify this user owns this review or is an admin
            
            boolean deleted = reviewService.deleteReview(reviewId);
            
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&deleteSuccess=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&deleteError=true");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
    
    private String getUniqueFileName(Part part) {
        String originalFileName = part.getSubmittedFileName();
        String extension = "";
        
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        return UUID.randomUUID().toString() + extension;
    }
} 