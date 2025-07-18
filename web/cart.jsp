<%-- 
    Document   : cart
    Created on : May 20, 2025, 3:43:48 PM
    Author     : hoan6
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Biolife - Organic Food</title>
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
    <link rel="stylesheet" href="assets/css/main-color.css">
    <style>
.btn-primary,
.back-to-shop-custom {
    background-color: #5ec6fa !important;
    border-color: #5ec6fa !important;
    color: #fff !important;
    border-radius: 24px !important;
    font-weight: 500;
    transition: background 0.2s;
    min-width: 170px;
    height: 48px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.btn-primary:hover,
.back-to-shop-custom:hover {
    background-color: #36a2eb !important;
    border-color: #36a2eb !important;
    color: #fff !important;
}

.btn-update, .btn-clear {
    border-radius: 20px !important;
    padding: 5px 18px;
    min-width: 90px;
}

.btn-update {
    background-color: #5ec6fa !important;
    border: 1px solid #5ec6fa !important;
    color: #fff !important;
}

.btn-update:hover {
    background-color: #36a2eb !important;
    border-color: #36a2eb !important;
}

.btn-clear-all {
    background-color: #ff4444 !important;
    border: 1px solid #ff4444 !important;
    color: #fff !important;
    border-radius: 24px !important;
    font-weight: 500;
    min-width: 170px;
    height: 48px;
    padding: 10px 0 !important;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.btn-clear-all:hover {
    background-color: #d32f2f !important;
    border-color: #d32f2f !important;
}

/* CSS cho việc căn giữa các nút Cập nhật và Xóa trong bảng */
.action-buttons-center {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
}

/* CSS cho việc căn chỉnh nút ở phía dưới bảng */
.bottom-buttons-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
}

.clear-cart-form {
    display: inline;
    margin: 0;
}

/* Responsive cho mobile */
@media (max-width: 768px) {
    .bottom-buttons-container {
        flex-direction: column;
        gap: 15px;
        text-align: center;
    }
    
    .action-buttons-center {
        flex-direction: column;
        gap: 5px;
    }
    
    .btn-update, .btn-clear {
        min-width: 80px;
        font-size: 12px;
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
    <jsp:include page="header.jsp"></jsp:include>

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">Organic Fruits</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="index-2.html" class="permal-link">Home</a></li>
                <li class="nav-item"><span class="current-page">ShoppingCart</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain shopping-cart">

        <!-- Main content -->
        <div id="main-content" class="main-content">
            <div class="container">

                <!--Top banner-->
                <div class="top-banner background-top-banner-for-shopping min-height-346px">
                    <h3 class="title">Save $50!*</h3>
                    <p class="subtitle">Save $50 when you open an account online & spen $50 on your first online purchase to day</p>
                    <ul class="list">
                        <li>
                            <div class="price-less">
                                <span class="desc">Purchase amount</span>
                                <span class="cost">$0.00</span>
                            </div>
                        </li>
                        <li>
                            <div class="price-less">
                                <span class="desc">Credit on billing statement</span>
                                <span class="cost">$0.00</span>
                            </div>
                        </li>
                        <li>
                            <div class="price-less sum">
                                <span class="desc">Cost affter statemen credit</span>
                                <span class="cost">$0.00</span>
                            </div>
                        </li>
                    </ul>
                    <p class="btns">
                        <a href="#" class="btn">Open Account</a>
                        <a href="#" class="btn">Learn more</a>
                    </p>
                </div>

                <!--Cart Table-->
<div class="container">
    <!-- Container để hiển thị thông báo AJAX -->
    <div class="alert-container" style="margin-top: 15px;"></div>
    
    <!-- Alert Messages -->
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show">
            ${sessionScope.message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show">
            ${sessionScope.error}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <c:if test="${not empty cartItems}">
        <div class="shopping-cart-container">
            <div class="row">
                <div class="col-lg-9 col-md-12 col-sm-12 col-xs-12">
                    <h3 class="box-title">Giỏ hàng của bạn</h3>
                    <!-- Cart Items Table -->
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>STT</th>
                                            <th>Sản phẩm</th>
                                            <th>Đơn giá</th>
                                            <th>Số lượng</th>
                                            <th>Thành tiền</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${cartItems}" varStatus="status">
                                            <tr id="cart-item-${item.cartItemId}">
                                                <td>
                                                    <span class="badge badge-primary">${status.index + 1}</span>
                                                </td>
                                                <td>
                                                    <div class="product-name">${item.productName}</div>
                                                </td>
                                                <td>
                                                    <span class="price">
                                                        <fmt:formatNumber value="${item.productPrice}" pattern="#,##0"/> ₫
                                                    </span>
                                                </td>
                                                <td>
                                                    <div class="input-group" style="max-width: 120px; margin: 0 auto;">
                                                        <input type="number" id="quantity-${item.cartItemId}" name="quantity" value="${item.quantity}" 
                                                               min="1" max="20" class="quantity-input"/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="price-total" id="item-total-${item.cartItemId}">
                                                        <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0"/> ₫
                                                    </span>
                                                </td>
                                                <td>
                                                    <div class="action-buttons-center">
                                                        <button type="button" class="btn btn-update btn-sm" onclick="updateCartItem(${item.cartItemId})">
                                                            Cập nhật
                                                        </button>
                                                        <button type="button" class="btn btn-clear btn-sm" onclick="removeCartItem(${item.cartItemId})">
                                                            Xóa
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!-- Hai nút nằm ở hai bên phía dưới bảng -->
                            <div class="bottom-buttons-container">
                                <a href="products.jsp" class="btn btn-primary btn-lg back-to-shop-custom">
                                    Tiếp tục mua hàng
                                </a>
                                <button type="button" class="btn btn-clear-all btn-lg" onclick="clearCart()">
                                    Xóa toàn bộ giỏ hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-12 col-sm-12 col-xs-12">
                    <div class="shpcart-subtotal-block">
                        <div class="cart-summary">
                            <h4>
                                Tổng kết đơn hàng
                            </h4>
                            <div class="summary-item">
                                <span>Tổng số sản phẩm:</span>
                                <span class="cart-counter" id="cart-counter">${itemCount}</span>
                            </div>
                            <div class="summary-item">
                                <span>Tổng tiền:</span>
                                <span class="price-total" id="cart-total">
                                    <fmt:formatNumber value="${cartTotal}" pattern="#,##0"/> ₫
                                </span>
                            </div>
                        </div>
                        <div class="subtotal-line">
                            <b class="stt-name">Phí giao hàng</b>
                            <span class="stt-price">0 ₫</span>
                        </div>
                        <div class="tax-fee">
                            <p class="title">Thuế và phí dự kiến</p>
                            <p class="desc">Tính theo vị trí giao hàng</p>
                        </div>
                        <div class="btn-checkout">
                            <a href="checkout.jsp" class="btn checkout btn-success">
                                Thanh toán
                            </a>
                        </div>
                        <p class="pickup-info"><b>Nhận tại cửa hàng</b> có thể trong ngày hôm nay. Tìm hiểu thêm về giao hàng và nhận hàng</p>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${empty cartItems}">
        <div class="shopping-cart-container">
            <div class="row">
                <div class="col-12 text-center">
                    <div class="empty-state py-5">
                        <i class="fas fa-shopping-cart fa-5x text-muted mb-4"></i>
                        <h5>Giỏ hàng của bạn đang trống</h5>
                        <p>Hãy khám phá các sản phẩm tuyệt vời của chúng tôi!</p>
                        <a href="products.jsp" class="btn btn-primary btn-lg back-to-shop-custom">
                            Mua sắm ngay
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>

<script>
    // Ẩn thông báo sau 5 giây
    setTimeout(function(){
        let alerts = document.querySelectorAll('.alert-success, .alert-danger');
        alerts.forEach(alert => {
            alert.classList.add('fade-out');
            setTimeout(() => {
                alert.style.display = 'none';
            }, 500);
        });
    }, 5000);

    // Animation cho buttons khi hover
    document.querySelectorAll('.btn').forEach(btn => {
        btn.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
            this.style.transition = 'transform 0.2s ease';
        });
        btn.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Quantity input validation
    document.querySelectorAll('input[name="quantity"]').forEach(input => {
        input.addEventListener('change', function() {
            if (this.value < 1) {
                this.value = 1;
            }
            if (this.value > 20) {
                this.value = 20;
            }
        });
    });

    // Hàm cập nhật sản phẩm
   function updateCartItem(cartItemId) {
    var quantity = $("#quantity-" + cartItemId).val();
    
    $.ajax({
        type: "POST",
        url: "cartClient",
        data: {
            action: "update",
            cartItemId: cartItemId,
            quantity: quantity
        },
        dataType: "json",
        success: function(response) {
            if (response.success) {
                // Cập nhật UI
                $("#item-total-" + cartItemId).text(formatCurrency(response.itemTotal) + " ₫");
                $("#cart-total").text(formatCurrency(response.cartTotal) + " ₫");
                $("#cart-counter").text(response.itemCount);
                
                // Hiển thị thông báo
                showMessage("success", response.message);
            } else {
                showMessage("danger", response.message);
            }
        },
        error: function() {
            showMessage("danger", "Có lỗi xảy ra!");
        }
    });
}
    function removeCartItem(cartItemId) {
        if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")) {
            return;
        }
        
        $.ajax({
            type: "POST",
            url: "cartClient",
            data: {
                action: "remove",
                cartItemId: cartItemId
            },
            dataType: "json",
            success: function(response) {
                if (response.success) {
                    // Xóa dòng sản phẩm
                    $("#cart-item-" + cartItemId).fadeOut(300, function() {
                        $(this).remove();
                        
                        // Nếu không còn sản phẩm nào, làm mới trang
                        if (response.itemCount === 0) {
                            location.reload();
                        }
                    });
                    
                    // Cập nhật tổng tiền và số lượng
                    $("#cart-total").text(formatCurrency(response.cartTotal) + " ₫");
                    $("#cart-counter").text(response.itemCount);
                    
                    // Hiển thị thông báo
                    showMessage("success", response.message);
                } else {
                    showMessage("danger", response.message);
                }
            },
            error: function() {
                showMessage("danger", "Có lỗi xảy ra khi xóa sản phẩm!");
            }
        });
    }

    // Hàm xóa toàn bộ giỏ hàng
    function clearCart() {
        if (!confirm("Bạn có chắc chắn muốn xóa toàn bộ giỏ hàng?")) {
            return;
        }
        
        $.ajax({
            type: "POST",
            url: "cartClient",
            data: {
                action: "clear"
            },
            dataType: "json",
            success: function(response) {
                if (response.success) {
                    // Làm mới trang
                    location.reload();
                } else {
                    showMessage("danger", response.message);
                }
            },
            error: function() {
                showMessage("danger", "Có lỗi xảy ra khi xóa giỏ hàng!");
            }
        });
    }

    // Hàm hiển thị thông báo
    // Hàm hiển thị thông báo - SỬA LẠI HOÀN TOÀN
function showMessage(type, message) {
    console.log("showMessage called with:", type, message);
    
    // Tạo thông báo đơn giản trước
    var alertDiv = '<div class="alert alert-' + type + '" style="position: fixed; top: 20px; right: 20px; z-index: 9999; padding: 15px; border-radius: 5px; ' +
        (type === 'success' ? 'background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb;' : 'background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;') +
        '">' + message + 
        '<button onclick="$(this).parent().remove()" style="float: right; background: none; border: none; font-size: 18px; cursor: pointer;">×</button></div>';
    
    // Xóa thông báo cũ
    $('.alert').remove();
    
    // Thêm vào body
    $('body').append(alertDiv);
    
    // Tự động xóa sau 3 giây
    setTimeout(function() {
        $('.alert').fadeOut(function() {
            $(this).remove();
        });
    }, 3000);
}
    // Hàm định dạng tiền tệ
    function formatCurrency(value) {
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
</script>
<style>
.alert-container {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 9999;
    width: 300px;
}
</style>



                <!--Related Product-->
                <div class="product-related-box single-layout">
                    <div class="biolife-title-box lg-margin-bottom-26px-im">
                        <span class="biolife-icon icon-organic"></span>
                        <span class="subtitle">All the best item for You</span>
                        <h3 class="main-title">Related Products</h3>
                    </div>
                    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":0,"slidesToShow":5, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20}},{"breakpoint":768, "settings":{ "slidesToShow": 2, "slidesMargin":10}}]}'>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-13.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price ">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-14.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-15.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-10.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-08.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-21.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="product-item">
                            <div class="contain-product layout-default">
                                <div class="product-thumb">
                                    <a href="#" class="link-to-product">
                                        <img src="assets/images/products/p-18.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                    </a>
                                </div>
                                <div class="info">
                                    <b class="categories">Fresh Fruit</b>
                                    <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                    <div class="price">
                                        <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                    </div>
                                    <div class="slide-down-box">
                                        <p class="message">All products are carefully selected to ensure food safety.</p>
                                        <div class="buttons">
                                            <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                            <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                            <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>

            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <footer id="footer" class="footer layout-03">
        <div class="footer-content background-footer-03">
            <div class="container">
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-sm-9">
                        <section class="footer-item">
                            <a href="#" class="logo footer-logo"><img src="assets/images/organic-3.png" alt="biolife logo" width="135" height="34"></a>
                            <div class="footer-phone-info">
                                <i class="biolife-icon icon-head-phone"></i>
                                <p class="r-info">
                                    <span>Got Questions ?</span>
                                    <span>(700)  9001-1909  (900) 689 -66</span>
                                </p>
                            </div>
                            <div class="newsletter-block layout-01">
                                <h4 class="title">Newsletter Signup</h4>
                                <div class="form-content">
                                    <form action="#" name="new-letter-foter">
                                        <input type="email" class="input-text email" value="" placeholder="Your email here...">
                                        <button type="submit" class="bnt-submit" name="ok">Sign up</button>
                                    </form>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Useful Links</h3>
                            <div class="row">
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="#">About Us</a></li>
                                            <li><a href="#">About Our Shop</a></li>
                                            <li><a href="#">Secure Shopping</a></li>
                                            <li><a href="#">Delivery infomation</a></li>
                                            <li><a href="#">Privacy Policy</a></li>
                                            <li><a href="#">Our Sitemap</a></li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="#">Who We Are</a></li>
                                            <li><a href="#">Our Services</a></li>
                                            <li><a href="#">Projects</a></li>
                                            <li><a href="#">Contacts Us</a></li>
                                            <li><a href="#">Innovation</a></li>
                                            <li><a href="#">Testimonials</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Transport Offices</h3>
                            <div class="contact-info-block footer-layout xs-padding-top-10px">
                                <ul class="contact-lines">
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-location"></i>
                                            <b class="desc">7563 St. Vicent Place, Glasgow, Greater Newyork NH7689, UK </b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-phone"></i>
                                            <b class="desc">Phone: (+067) 234 789  (+068) 222 888</b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-letter"></i>
                                            <b class="desc">Email:  contact@company.com</b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-clock"></i>
                                            <b class="desc">Hours: 7 Days a week from 10:00 am</b>
                                        </p>
                                    </li>
                                </ul>
                            </div>
                            <div class="biolife-social inline">
                                <ul class="socials">
                                    <li><a href="#" title="twitter" class="socail-btn"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="facebook" class="socail-btn"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="pinterest" class="socail-btn"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="youtube" class="socail-btn"><i class="fa fa-youtube" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="instagram" class="socail-btn"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                </ul>
                            </div>
                        </section>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="separator sm-margin-top-70px xs-margin-top-40px"></div>
                    </div>
                    <div class="col-lg-6 col-sm-6 col-xs-12">
                        <div class="copy-right-text"><p><a href="templateshub.net">Templates Hub</a></p></div>
                    </div>
                    <div class="col-lg-6 col-sm-6 col-xs-12">
                        <div class="payment-methods">
                            <ul>
                                <li><a href="#" class="payment-link"><img src="assets/images/card1.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card2.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card3.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card4.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card5.jpg" width="51" height="36" alt=""></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!--Footer For Mobile-->
    <div class="mobile-footer">
        <div class="mobile-footer-inner">
            <div class="mobile-block block-menu-main">
                <a class="menu-bar menu-toggle btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                    <span class="fa fa-bars"></span>
                    <span class="text">Menu</span>
                </a>
            </div>
            <div class="mobile-block block-sidebar">
                <a class="menu-bar filter-toggle btn-toggle" data-object="open-mobile-filter" href="javascript:void(0)">
                    <i class="fa fa-sliders" aria-hidden="true"></i>
                    <span class="text">Sidebar</span>
                </a>
            </div>
            <div class="mobile-block block-minicart">
                <a class="link-to-cart" href="#">
                    <span class="fa fa-shopping-bag" aria-hidden="true"></span>
                    <span class="text">Cart</span>
                </a>
            </div>
            <div class="mobile-block block-global">
                <a class="menu-bar myaccount-toggle btn-toggle" data-object="global-panel-opened" href="javascript:void(0)">
                    <span class="fa fa-globe"></span>
                    <span class="text">Global</span>
                </a>
            </div>
        </div>
    </div>

    <div class="mobile-block-global">
        <div class="biolife-mobile-panels">
            <span class="biolife-current-panel-title">Global</span>
            <a class="biolife-close-btn" data-object="global-panel-opened" href="#">&times;</a>
        </div>
        <div class="block-global-contain">
            <div class="glb-item my-account">
                <b class="title">My Account</b>
                <ul class="list">
                    <li class="list-item"><a href="#">Login/register</a></li>
                    <li class="list-item"><a href="#">Wishlist <span class="index">(8)</span></a></li>
                    <li class="list-item"><a href="#">Checkout</a></li>
                </ul>
            </div>
            <div class="glb-item currency">
                <b class="title">Currency</b>
                <ul class="list">
                    <li class="list-item"><a href="#">€ EUR (Euro)</a></li>
                    <li class="list-item"><a href="#">$ USD (Dollar)</a></li>
                    <li class="list-item"><a href="#">£ GBP (Pound)</a></li>
                    <li class="list-item"><a href="#">¥ JPY (Yen)</a></li>
                </ul>
            </div>
            <div class="glb-item languages">
                <b class="title">Language</b>
                <ul class="list inline">
                    <li class="list-item"><a href="#"><img src="assets/images/languages/us.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/fr.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/ger.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/jap.jpg" alt="flag" width="24" height="18"></a></li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
</body>

</html>
