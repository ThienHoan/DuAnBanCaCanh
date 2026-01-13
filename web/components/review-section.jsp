<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Review Section -->
<div class="review-section">
    <div class="container">
        <div class="row">
            <div class="col-lg-8">
                <!-- Review Summary -->
                <div class="review-summary">
                    <h3>Customer Reviews</h3>
                    
                    <c:if test="${not empty product}">
                        <div class="rating-summary-wrapper">
                            <div class="rating-info">
                                <div class="index">
                                    <fmt:formatNumber value="${averageRating}" pattern="#.#" />
                                </div>
                                <div class="rating">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fa fa-star${i <= averageRating ? '' : '-o'}" style="color: #ffb400;"></i>
                                    </c:forEach>
                                </div>
                                <div class="options">
                                    <c:forEach begin="5" end="1" step="-1" var="rating">
                                        <li>
                                            <div class="detail-for">
                                                <span class="option-name">${rating} star</span>
                                                <div class="progres">
                                                    <div class="line-100percent">
                                                        <c:set var="percentage" value="${ratingCounts[rating-1] > 0 ? (ratingCounts[rating-1] * 100.0 / totalReviews) : 0}" />
                                                        <span class="percent" style="width: ${percentage}%"></span>
                                                    </div>
                                                </div>
                                                <span class="number">${ratingCounts[rating-1]}</span>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                        
                        <p class="total-reviews">Based on ${totalReviews} reviews</p>
                    </c:if>
                </div>
                
                <!-- Review Form -->
                <c:if test="${not empty user}">
                    <div class="review-form-section">
                        <h4>Write a Review</h4>
                        
                        <c:if test="${param.reviewError == 'already_reviewed'}">
                            <div class="alert alert-warning">
                                You have already reviewed this product.
                            </div>
                        </c:if>
                        
                        <c:if test="${param.reviewSuccess == 'true'}">
                            <div class="alert alert-success">
                                Your review has been submitted successfully!
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/submit-review" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="submit">
                            <input type="hidden" name="productId" value="${product.productId}">
                            
                            <div class="form-group">
                                <label>Rating:</label>
                                <div class="rating-input">
                                    <input type="radio" name="rating" value="5" id="star5" required>
                                    <label for="star5"><i class="fa fa-star"></i></label>
                                    <input type="radio" name="rating" value="4" id="star4">
                                    <label for="star4"><i class="fa fa-star"></i></label>
                                    <input type="radio" name="rating" value="3" id="star3">
                                    <label for="star3"><i class="fa fa-star"></i></label>
                                    <input type="radio" name="rating" value="2" id="star2">
                                    <label for="star2"><i class="fa fa-star"></i></label>
                                    <input type="radio" name="rating" value="1" id="star1">
                                    <label for="star1"><i class="fa fa-star"></i></label>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="comment">Comment:</label>
                                <textarea name="comment" id="comment" class="form-control" rows="4" required></textarea>
                            </div>
                            
                            <div class="form-group">
                                <label for="reviewImages">Images (optional):</label>
                                <input type="file" name="reviewImages" id="reviewImages" class="form-control" multiple accept="image/*">
                            </div>
                            
                            <button type="submit" class="btn btn-primary">Submit Review</button>
                        </form>
                    </div>
                </c:if>
                
                <!-- Review List -->
                <div class="review-list">
                    <h4>Customer Reviews</h4>
                    
                    <c:if test="${empty reviews}">
                        <p>No reviews yet. Be the first to review this product!</p>
                    </c:if>
                    
                    <c:forEach var="review" items="${reviews}">
                        <div class="review-item">
                            <div class="review-header">
                                <div class="reviewer-info">
                                    <span class="reviewer-name">User ${review.userId}</span>
                                    <span class="review-date">
                                        <fmt:formatDate value="${review.reviewDate}" pattern="MMM dd, yyyy" />
                                    </span>
                                </div>
                                <div class="review-rating">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fa fa-star${i <= review.rating ? '' : '-o'}" style="color: #ffb400;"></i>
                                    </c:forEach>
                                </div>
                            </div>
                            
                            <div class="review-content">
                                <p>${review.comment}</p>
                            </div>
                            
                            <c:if test="${not empty review.images}">
                                <div class="review-images">
                                    <c:forEach var="image" items="${review.images}">
                                        <img src="${image.imageUrl}" alt="Review image" class="review-image">
                                    </c:forEach>
                                </div>
                            </c:if>
                            
                            <c:if test="${review.isVerifiedPurchase == 1}">
                                <span class="badge badge-success">Verified Purchase</span>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
.review-section {
    margin-top: 40px;
    padding: 20px 0;
}

.review-summary {
    margin-bottom: 30px;
}

.rating-input {
    display: flex;
    flex-direction: row-reverse;
    gap: 5px;
}

.rating-input input[type="radio"] {
    display: none;
}

.rating-input label {
    cursor: pointer;
    font-size: 24px;
    color: #ddd;
}

.rating-input input[type="radio"]:checked ~ label,
.rating-input label:hover,
.rating-input label:hover ~ label {
    color: #ffb400;
}

.review-item {
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
}

.review-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.reviewer-name {
    font-weight: 600;
    color: #333;
}

.review-date {
    color: #666;
    font-size: 14px;
    margin-left: 10px;
}

.review-content {
    margin-bottom: 15px;
}

.review-images {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
}

.review-image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 4px;
    cursor: pointer;
}

.badge {
    font-size: 12px;
    padding: 4px 8px;
}

.badge-success {
    background-color: #28a745;
    color: white;
}

.review-form-section {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
}

.form-group {
    margin-bottom: 15px;
}

.form-control {
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 8px 12px;
}

.btn-primary {
    background-color: #007bff;
    border-color: #007bff;
    color: white;
    padding: 10px 20px;
    border-radius: 4px;
    cursor: pointer;
}

.btn-primary:hover {
    background-color: #0056b3;
    border-color: #0056b3;
}

.alert {
    padding: 12px 16px;
    border-radius: 4px;
    margin-bottom: 15px;
}

.alert-success {
    background-color: #d4edda;
    border-color: #c3e6cb;
    color: #155724;
}

.alert-warning {
    background-color: #fff3cd;
    border-color: #ffeaa7;
    color: #856404;
}
</style>

<script>
// Rating input functionality
document.addEventListener('DOMContentLoaded', function() {
    const ratingInputs = document.querySelectorAll('.rating-input input[type="radio"]');
    const ratingLabels = document.querySelectorAll('.rating-input label');
    
    ratingInputs.forEach((input, index) => {
        input.addEventListener('change', function() {
            // Reset all stars
            ratingLabels.forEach(label => {
                label.style.color = '#ddd';
            });
            
            // Color stars up to selected rating
            for (let i = 0; i <= index; i++) {
                ratingLabels[i].style.color = '#ffb400';
            }
        });
    });
});

// Image preview functionality
document.getElementById('reviewImages')?.addEventListener('change', function(e) {
    const files = e.target.files;
    const previewContainer = document.createElement('div');
    previewContainer.className = 'image-preview';
    previewContainer.style.display = 'flex';
    previewContainer.style.gap = '10px';
    previewContainer.style.marginTop = '10px';
    
    for (let file of files) {
        if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.style.width = '80px';
                img.style.height = '80px';
                img.style.objectFit = 'cover';
                img.style.borderRadius = '4px';
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        }
    }
    
    // Remove existing preview
    const existingPreview = document.querySelector('.image-preview');
    if (existingPreview) {
        existingPreview.remove();
    }
    
    // Add new preview
    if (previewContainer.children.length > 0) {
        e.target.parentNode.appendChild(previewContainer);
    }
});
</script> 