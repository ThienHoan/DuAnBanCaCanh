<%-- 
    Document   : cart
    Created on : May 20, 2025, 3:43:48 PM
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
    <jsp:include page="header.jsp"/>

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">Organic Fruits</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                <li class="nav-item"><span class="current-page">ShoppingCart</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain shopping-cart">

        <!-- Main content -->
        <div id="main-content" class="main-content">
            <div class="container">

                <!--Top banner-->
  

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
                                <a href="category" class="btn btn-primary btn-lg back-to-shop-custom">
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
                            <a href="checkout" class="btn checkout btn-success">
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
                        <a href="category" class="btn btn-primary btn-lg back-to-shop-custom">
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



          
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />

    
    
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
