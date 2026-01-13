/**
 * Wishlist Management JavaScript
 * Xử lý các chức năng liên quan đến wishlist
 */

class WishlistManager {
    constructor() {
        this.contextPath = window.contextPath || '';
        this.init();
    }
    
    init() {
        this.bindEvents();
        this.updateWishlistCount();
    }
    
    bindEvents() {
        // Bind click events for wishlist buttons
        $(document).on('click', '.btn-add-wishlist', (e) => {
            e.preventDefault();
            const productId = $(e.target).closest('.btn-add-wishlist').data('product-id');
            this.addToWishlist(productId);
        });
        
        $(document).on('click', '.btn-remove-wishlist', (e) => {
            e.preventDefault();
            const productId = $(e.target).closest('.btn-remove-wishlist').data('product-id');
            this.removeFromWishlist(productId);
        });
        
        $(document).on('click', '.btn-toggle-wishlist', (e) => {
            e.preventDefault();
            const productId = $(e.target).closest('.btn-toggle-wishlist').data('product-id');
            this.toggleWishlist(productId);
        });
        
        // Bind heart icon clicks
        $(document).on('click', '.wishlist-heart', (e) => {
            e.preventDefault();
            const productId = $(e.target).closest('.wishlist-heart').data('product-id');
            this.toggleWishlist(productId);
        });
    }
    
    addToWishlist(productId) {
        if (!this.checkLogin()) return;
        
        $.ajax({
            url: this.contextPath + '/wishlist',
            type: 'POST',
            data: {
                action: 'add',
                productId: productId
            },
            dataType: 'json',
            success: (response) => {
                if (response.success) {
                    this.showNotification('success', response.message);
                    this.updateWishlistCount(response.count);
                    this.updateWishlistButtons(productId, true);
                } else {
                    this.showNotification('error', response.message);
                }
            },
            error: () => {
                this.showNotification('error', 'Đã xảy ra lỗi. Vui lòng thử lại.');
            }
        });
    }
    
    removeFromWishlist(productId, wishlistId = null) {
        if (!this.checkLogin()) return;
        
        const data = {
            action: 'remove'
        };
        
        if (wishlistId) {
            data.wishlistId = wishlistId;
        } else {
            data.productId = productId;
        }
        
        $.ajax({
            url: this.contextPath + '/wishlist',
            type: 'POST',
            data: data,
            dataType: 'json',
            success: (response) => {
                if (response.success) {
                    this.showNotification('success', response.message);
                    this.updateWishlistCount(response.count);
                    this.updateWishlistButtons(productId, false);
                } else {
                    this.showNotification('error', response.message);
                }
            },
            error: () => {
                this.showNotification('error', 'Đã xảy ra lỗi. Vui lòng thử lại.');
            }
        });
    }
    
    toggleWishlist(productId) {
        if (!this.checkLogin()) return;
        
        $.ajax({
            url: this.contextPath + '/wishlist',
            type: 'POST',
            data: {
                action: 'toggle',
                productId: productId
            },
            dataType: 'json',
            success: (response) => {
                if (response.success) {
                    this.showNotification('success', response.message);
                    this.updateWishlistCount(response.count);
                    this.updateWishlistButtons(productId, response.isAdded);
                } else {
                    this.showNotification('error', response.message);
                }
            },
            error: () => {
                this.showNotification('error', 'Đã xảy ra lỗi. Vui lòng thử lại.');
            }
        });
    }
    
    updateWishlistCount(count = null) {
        if (count === null) {
            // Fetch current count
            $.ajax({
                url: this.contextPath + '/wishlist?action=count',
                type: 'GET',
                dataType: 'json',
                success: (response) => {
                    this.displayWishlistCount(response.count);
                }
            });
        } else {
            this.displayWishlistCount(count);
        }
    }
    
    displayWishlistCount(count) {
        // Update count in header
        $('.wishlist-count').text(count);
        $('.wishlist-count-badge').text(count);
        
        // Show/hide badge based on count
        if (count > 0) {
            $('.wishlist-count-badge').show();
        } else {
            $('.wishlist-count-badge').hide();
        }
    }
    
    updateWishlistButtons(productId, isInWishlist) {
        const heartButtons = $(`.wishlist-heart[data-product-id="${productId}"]`);
        const toggleButtons = $(`.btn-toggle-wishlist[data-product-id="${productId}"]`);
        
        if (isInWishlist) {
            heartButtons.removeClass('fa-heart-o').addClass('fa-heart').css('color', '#e74c3c');
            toggleButtons.removeClass('btn-add-wishlist').addClass('btn-remove-wishlist')
                        .text('Xóa khỏi yêu thích').prepend('<i class="fa fa-heart"></i> ');
        } else {
            heartButtons.removeClass('fa-heart').addClass('fa-heart-o').css('color', '#999');
            toggleButtons.removeClass('btn-remove-wishlist').addClass('btn-add-wishlist')
                        .text('Thêm vào yêu thích').prepend('<i class="fa fa-heart-o"></i> ');
        }
    }
    
    checkWishlistStatus(productIds) {
        if (!Array.isArray(productIds) || productIds.length === 0) return;
        
        productIds.forEach(productId => {
            $.ajax({
                url: this.contextPath + '/wishlist?action=check',
                type: 'GET',
                data: { productId: productId },
                dataType: 'json',
                success: (response) => {
                    this.updateWishlistButtons(productId, response.inWishlist);
                }
            });
        });
    }
    
    checkLogin() {
        // Check if user is logged in
        const isLoggedIn = $('.user-info').length > 0 || $('#user-menu').length > 0;
        
        if (!isLoggedIn) {
            this.showNotification('warning', 'Vui lòng đăng nhập để sử dụng chức năng này.');
            setTimeout(() => {
                window.location.href = this.contextPath + '/login?returnUrl=' + encodeURIComponent(window.location.href);
            }, 2000);
            return false;
        }
        
        return true;
    }
    
    showNotification(type, message) {
        // Create notification element
        const notificationClass = type === 'success' ? 'alert-success' : 
                                type === 'error' ? 'alert-danger' : 
                                type === 'warning' ? 'alert-warning' : 'alert-info';
        
        const notification = $(`
            <div class="alert ${notificationClass} alert-dismissible fade show wishlist-notification" role="alert" style="position: fixed; top: 20px; right: 20px; z-index: 9999; min-width: 300px;">
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `);
        
        // Add to page
        $('body').append(notification);
        
        // Auto remove after 5 seconds
        setTimeout(() => {
            notification.fadeOut(() => notification.remove());
        }, 5000);
    }
    
    // Helper method to get all product IDs on current page
    getProductIdsOnPage() {
        const productIds = [];
        $('.wishlist-heart[data-product-id], .btn-toggle-wishlist[data-product-id]').each(function() {
            const productId = $(this).data('product-id');
            if (productId && productIds.indexOf(productId) === -1) {
                productIds.push(productId);
            }
        });
        return productIds;
    }
    
    // Initialize wishlist status for all products on page
    initializeWishlistStatus() {
        const productIds = this.getProductIdsOnPage();
        if (productIds.length > 0) {
            this.checkWishlistStatus(productIds);
        }
    }
}

// Initialize wishlist manager when document is ready
$(document).ready(() => {
    window.wishlistManager = new WishlistManager();
    
    // Initialize wishlist status for products on page
    setTimeout(() => {
        window.wishlistManager.initializeWishlistStatus();
    }, 500);
});

// Helper functions for backward compatibility
function addToWishlist(productId) {
    if (window.wishlistManager) {
        window.wishlistManager.addToWishlist(productId);
    }
}

function removeFromWishlist(wishlistId, productId) {
    if (window.wishlistManager) {
        window.wishlistManager.removeFromWishlist(productId, wishlistId);
    }
}

function toggleWishlist(productId) {
    if (window.wishlistManager) {
        window.wishlistManager.toggleWishlist(productId);
    }
}
