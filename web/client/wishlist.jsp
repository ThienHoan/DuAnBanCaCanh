<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Danh sách yêu thích - Fish Shop</title>
    
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/favicon.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/nice-select.css">
    <link rel="stylesheet" href="assets/css/slick.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color04.css">
    
    <style>
        .wishlist-container {
            padding: 60px 0;
            background-color: #f8f9fa;
        }
        
        .wishlist-header {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .wishlist-header h1 {
            color: #333;
            font-size: 2.5rem;
            margin-bottom: 10px;
        }
        
        .wishlist-header p {
            color: #666;
            font-size: 1.1rem;
        }
        
        .wishlist-item {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        
        .wishlist-item:hover {
            transform: translateY(-2px);
        }
        
        .wishlist-item-image {
            width: 120px;
            height: 120px;
            object-fit: cover;
            border-radius: 8px;
        }
        
        .wishlist-item-info h4 {
            color: #333;
            margin-bottom: 10px;
            font-size: 1.3rem;
        }
        
        .wishlist-item-price {
            color: #7fad39;
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .wishlist-item-category {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 15px;
        }
        
        .wishlist-item-date {
            color: #999;
            font-size: 0.8rem;
        }
        
        .wishlist-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }
        
        .btn-remove-wishlist {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        
        .btn-remove-wishlist:hover {
            background-color: #c82333;
        }
        
        .btn-add-to-cart {
            background-color: #7fad39;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        
        .btn-add-to-cart:hover {
            background-color: #6d941f;
        }
        
        .empty-wishlist {
            text-align: center;
            padding: 80px 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .empty-wishlist i {
            font-size: 4rem;
            color: #ddd;
            margin-bottom: 20px;
        }
        
        .empty-wishlist h3 {
            color: #666;
            margin-bottom: 15px;
        }
        
        .empty-wishlist p {
            color: #999;
            margin-bottom: 30px;
        }
        
        .wishlist-actions-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .wishlist-count {
            font-size: 1.1rem;
            color: #666;
        }
        
        .btn-clear-all {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        
        .btn-clear-all:hover {
            background-color: #c82333;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
        }
        
        .alert-success {
            color: #155724;
            background-color: #d4edda;
            border-color: #c3e6cb;
        }
        
        .alert-danger {
            color: #721c24;
            background-color: #f8d7da;
            border-color: #f5c6cb;
        }
        
        @media (max-width: 768px) {
            .wishlist-item {
                padding: 15px;
            }
            
            .wishlist-item-image {
                width: 80px;
                height: 80px;
            }
            
            .wishlist-item-info h4 {
                font-size: 1.1rem;
            }
            
            .wishlist-actions {
                flex-direction: column;
            }
            
            .wishlist-actions-top {
                flex-direction: column;
                gap: 15px;
                text-align: center;
            }
        }
    </style>
</head>

<body class="biolife-body">
    <!-- Preloader -->
    <div id="biof-loading">
        <div class="biof-loading-center">
            <div class="biof-loading-center-absolute">
                <div class="dot dot-one"></div>
                <div class="dot dot-two"></div>
                <div class="dot dot-three"></div>
            </div>
        </div>
    </div>

    <!-- HEADER -->
    <jsp:include page="../header.jsp"></jsp:include>

    <!-- Main Content -->
    <div class="page-contain wishlist-container">
        <div class="container">
            <!-- Wishlist Header -->
            <div class="wishlist-header">
                <h1><i class="fa fa-heart" style="color: #e74c3c;"></i> Danh sách yêu thích</h1>
                <p>Những sản phẩm bạn đã lưu để mua sau</p>
            </div>
            
            <!-- Alert Messages -->
            <div id="alert-container"></div>
            
            <c:choose>
                <c:when test="${not empty wishlist}">
                    <!-- Wishlist Actions -->
                    <div class="wishlist-actions-top">
                        <div class="wishlist-count">
                            <strong>Bạn có <span id="wishlist-count">${wishlistCount}</span> sản phẩm yêu thích</strong>
                        </div>
                        <c:if test="${wishlistCount > 0}">
                            <button type="button" class="btn-clear-all" onclick="clearAllWishlist()">
                                <i class="fa fa-trash"></i> Xóa tất cả
                            </button>
                        </c:if>
                    </div>
                    
                    <!-- Wishlist Items -->
                    <div id="wishlist-items">
                        <c:forEach var="item" items="${wishlist}">
                            <div class="wishlist-item" data-wishlist-id="${item.wishlistId}" data-product-id="${item.productId}">
                                <div class="row align-items-center">
                                    <div class="col-md-2">
                                        <c:choose>
                                            <c:when test="${not empty item.productImageUrl}">
                                                <img src="${item.productImageUrl}" alt="${item.productName}" class="wishlist-item-image">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="wishlist-item-image d-flex align-items-center justify-content-center" style="background-color: #f8f9fa;">
                                                    <i class="fa fa-image" style="font-size: 2rem; color: #ddd;"></i>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="wishlist-item-info">
                                            <h4>${item.productName}</h4>
                                            <div class="wishlist-item-price">
                                                <fmt:formatNumber value="${item.productPrice}" type="currency" currencySymbol="₫" />
                                            </div>
                                            <c:if test="${not empty item.categoryName}">
                                                <div class="wishlist-item-category">
                                                    <i class="fa fa-tag"></i> ${item.categoryName}
                                                </div>
                                            </c:if>
                                            <div class="wishlist-item-date">
                                                <i class="fa fa-calendar"></i> Thêm ngày: 
                                                <fmt:formatDate value="${item.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="wishlist-actions">                                            <c:if test="${item.stockQuantity > 0}">
                                                <button type="button" class="btn-add-to-cart" onclick="addToCart('${item.productId}')">
                                                    <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                </button>
                                            </c:if>
                                            <c:if test="${item.stockQuantity <= 0}">
                                                <span class="text-muted">Hết hàng</span>
                                            </c:if>
                                            <button type="button" class="btn-remove-wishlist" onclick="removeFromWishlist('${item.wishlistId}', '${item.productId}')">
                                                <i class="fa fa-trash"></i> Xóa
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- Empty Wishlist -->
                    <div class="empty-wishlist">
                        <i class="fa fa-heart-o"></i>
                        <h3>Danh sách yêu thích trống</h3>
                        <p>Bạn chưa có sản phẩm nào trong danh sách yêu thích.<br>
                           Hãy khám phá các sản phẩm và thêm những sản phẩm bạn quan tâm.</p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-bold" style="background-color: #7fad39; color: white; padding: 12px 30px; border-radius: 4px; text-decoration: none;">
                            <i class="fa fa-shopping-bag"></i> Khám phá sản phẩm
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- FOOTER -->
    <footer id="footer" class="footer layout-03">
        <div class="footer-content background-footer-03">
            <div class="container">
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-sm-9">
                        <section class="footer-item">
                            <a href="home" class="logo footer-logo">
                                <img src="assets/images/organic-4.png" alt="Fish Shop logo" width="135" height="36">
                            </a>
                            <div class="footer-phone-info">
                                <i class="biolife-icon icon-head-phone"></i>
                                <p class="r-info">
                                    <span>Có câu hỏi?</span>
                                    <span>(024) 1234-5678</span>
                                </p>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Liên kết hữu ích</h3>
                            <div class="row">
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="home">Trang chủ</a></li>
                                            <li><a href="products">Sản phẩm</a></li>
                                            <li><a href="blog">Blog</a></li>
                                            <li><a href="contact">Liên hệ</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!-- Scripts -->
    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
    
    <script>
        function removeFromWishlist(wishlistId, productId) {
            if (!confirm('Bạn có chắc muốn xóa sản phẩm này khỏi danh sách yêu thích?')) {
                return;
            }
            
            $.ajax({
                url: '${pageContext.request.contextPath}/wishlist',
                type: 'POST',
                data: {
                    action: 'remove',
                    wishlistId: wishlistId
                },
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        // Remove item from DOM
                        $('[data-wishlist-id="' + wishlistId + '"]').fadeOut(300, function() {
                            $(this).remove();
                            
                            // Update count
                            $('#wishlist-count').text(response.count);
                            
                            // If no items left, reload page to show empty state
                            if (response.count === 0) {
                                location.reload();
                            }
                        });
                        showAlert('success', response.message);
                    } else {
                        showAlert('danger', response.message);
                    }
                },
                error: function() {
                    showAlert('danger', 'Đã xảy ra lỗi. Vui lòng thử lại.');
                }
            });
        }
        
        function clearAllWishlist() {
            if (!confirm('Bạn có chắc muốn xóa tất cả sản phẩm khỏi danh sách yêu thích?')) {
                return;
            }
            
            $.ajax({
                url: '${pageContext.request.contextPath}/wishlist',
                type: 'POST',
                data: {
                    action: 'clear'
                },
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        location.reload();
                    } else {
                        showAlert('danger', response.message);
                    }
                },
                error: function() {
                    showAlert('danger', 'Đã xảy ra lỗi. Vui lòng thử lại.');
                }
            });
        }
        
        function addToCart(productId) {
            // This function should be implemented based on your cart system
            alert('Chức năng thêm vào giỏ hàng sẽ được triển khai sau!');
        }
        
        function showAlert(type, message) {
            const alertHtml = `
                <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    ${message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            `;
            
            $('#alert-container').html(alertHtml);
            
            // Auto hide after 5 seconds
            setTimeout(function() {
                $('.alert').fadeOut();
            }, 5000);
        }
        
        // Update wishlist count in header (if exists)
        function updateWishlistCountInHeader() {
            $.ajax({
                url: '${pageContext.request.contextPath}/wishlist?action=count',
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    $('.wishlist-count-badge').text(response.count);
                }
            });
        }
    </script>
</body>
</html>
