package controller.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
        try {
        reviewService = new ReviewServiceImpl();
            System.out.println("ReviewController - ReviewService initialized successfully");
        } catch (Exception e) {
            System.err.println("ReviewController - Error initializing ReviewService: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create upload directory if it doesn't exist
        try {
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            }
            System.out.println("ReviewController - Upload directory created: " + uploadPath);
        } catch (Exception e) {
            System.err.println("ReviewController - Error creating upload directory: " + e.getMessage());
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
        
        // Debug: Log the action
        System.out.println("ReviewController - Action: " + action);
        System.out.println("ReviewController - Request URI: " + request.getRequestURI());
        
        if ("submit".equals(action)) {
            submitReview(request, response);
        } else if ("delete".equals(action)) {
            deleteReview(request, response);
        } else {
            System.out.println("ReviewController - No action specified, redirecting to home");
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
    
    private void submitReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ReviewController - submitReview method called");
        
        // Check if user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        System.out.println("ReviewController - User: " + (user != null ? user.getUserId() : "null"));
        
        if (user == null) {
            // Redirect to login page if not logged in
            System.out.println("ReviewController - User not logged in, redirecting to login");
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
            
            // Check if user has purchased this product
            if (!reviewService.hasUserPurchasedProduct(user.getUserId(), productId)) {
                System.out.println("User " + user.getUserId() + " has not purchased product " + productId);
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId + "&reviewError=not_purchased");
                return;
            }
            
            // Check if user has already reviewed this product
            if (reviewService.hasUserReviewedProduct(user.getUserId(), productId)) {
                System.out.println("User " + user.getUserId() + " has already reviewed product " + productId);
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
            review.setIsVerifiedPurchase(0); // Set to 1 if you have order verification logic
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
                System.out.println("ReviewController - Review saved successfully, redirecting to product detail");
                String redirectUrl = request.getContextPath() + "/product-detail?id=" + productId + "&reviewSuccess=true";
                System.out.println("ReviewController - Redirect URL: " + redirectUrl);
                response.sendRedirect(redirectUrl);
            } else {
                // Error - redirect back with error message
                System.out.println("ReviewController - Failed to save review, redirecting with error");
                String redirectUrl = request.getContextPath() + "/product-detail?id=" + productId + "&reviewError=save_failed";
                System.out.println("ReviewController - Redirect URL: " + redirectUrl);
                response.sendRedirect(redirectUrl);
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
        User user = (User) SessionUtils.getUser(request.getSession());
        
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