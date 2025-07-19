<%-- 
    Document   : checkout
    Created on : May 20, 2025, 3:42:10 PM
    Author     : hoan6
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Biolife - Thanh toán</title>
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
        .checkout-page {
            padding: 50px 0;
        }
        .checkout-title {
            margin-bottom: 30px;
        }
        .checkout-section {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.05);
            padding: 25px;
            margin-bottom: 30px;
        }
        .checkout-section h4 {
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-label {
            font-weight: 600;
            margin-bottom: 8px;
            display: block;
        }
        .form-control {
            border-radius: 5px;
            height: 45px;
            border: 1px solid #ddd;
            color: #333;
            background-color: #fff;
        }
        .form-control:focus {
            border-color: #5ec6fa;
            box-shadow: 0 0 0 0.2rem rgba(94, 198, 250, 0.25);
        }
        /* Reset select styles */
        select {
            -webkit-appearance: menulist !important;
            -moz-appearance: menulist !important;
            appearance: menulist !important;
            background-image: none !important;
            background-color: #ffffff !important;
            color: #333 !important;
            border: 1px solid #ddd !important;
            padding: 5px 10px !important;
            cursor: pointer !important;
        }
        select.form-control {
            background-color: #ffffff;
            color: #333;
            font-weight: 500;
            appearance: auto;
            -webkit-appearance: menulist;
            -moz-appearance: menulist;
            padding-right: 25px;
        }
        select.form-control option {
            background-color: #ffffff;
            color: #333;
            padding: 10px;
        }
        /* Thêm CSS để đảm bảo văn bản hiển thị rõ ràng */
        #addressId {
            color: #333 !important;
            background-color: #fff !important;
            font-weight: normal !important;
            text-align: left !important;
            padding: 8px 12px !important;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
        }
        #addressId option {
            color: #333 !important;
            background-color: #fff !important;
            padding: 8px !important;
        }
        /* Ẩn các phần tử không cần thiết */
        .nice-select-dropdown, .list, .nice-select:after {
            display: none !important;
        }
        .nice-select {
            display: none !important;
        }
        /* Ẩn ô vuông nhỏ bên dưới */
        #addressId + div, 
        .nice-select + div,
        .form-group > div:not(.row):not(.form-control):not(#new_address_fields) {
            display: none !important;
        }
        .payment-methods {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        .payment-method {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .payment-method:hover {
            border-color: #5ec6fa;
        }
        .payment-method.active {
            border-color: #5ec6fa;
            background-color: rgba(94, 198, 250, 0.05);
        }
        .payment-method input[type="radio"] {
            margin-right: 10px;
        }
        .payment-method-title {
            font-weight: 600;
            margin-bottom: 5px;
        }
        .payment-method-description {
            color: #666;
            font-size: 14px;
        }
        .order-summary {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 10px;
        }
        .order-summary-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        .order-summary-item:last-child {
            border-bottom: none;
        }
        .order-summary-item.total {
            font-weight: 700;
            font-size: 18px;
            border-top: 2px solid #ddd;
            border-bottom: none;
            padding-top: 15px;
            margin-top: 15px;
        }
        .btn-checkout {
            background-color: #5ec6fa;
            color: white;
            border: none;
            border-radius: 50px;
            padding: 12px 30px;
            font-size: 16px;
            font-weight: 600;
            width: 100%;
            margin-top: 20px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-checkout:hover {
            background-color: #36a2eb;
        }
        .product-item {
            display: flex;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 5px;
            margin-right: 15px;
        }
        .product-details {
            flex: 1;
        }
        .product-name {
            font-weight: 600;
            margin-bottom: 5px;
        }
        .product-price {
            color: #5ec6fa;
            font-weight: 600;
        }
        .product-quantity {
            color: #666;
            font-size: 14px;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .product-attributes {
            margin-top: 5px;
            font-size: 13px;
            color: #666;
        }
        .attribute-item {
            display: inline-block;
            margin-right: 10px;
            background-color: #f5f5f5;
            padding: 2px 8px;
            border-radius: 3px;
            font-size: 12px;
        }
        .new-address-fields, #new_address_fields {
            padding: 15px;
            background-color: #f9f9f9;
            border-radius: 5px;
            margin-top: 15px;
        }
        .coupon-section {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        .coupon-form {
            display: flex;
            gap: 10px;
        }
        .coupon-input {
            flex: 1;
        }
        .btn-apply-coupon {
            background-color: #6c757d;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-apply-coupon:hover {
            background-color: #5a6268;
        }
        .coupon-applied {
            margin-top: 10px;
            padding: 10px;
            background-color: #d4edda;
            color: #155724;
            border-radius: 5px;
        }
        
    </style>
</head>

<body class="biolife-body">

    <!-- Preloader -->


    <!-- HEADER -->
    <jsp:include page="header.jsp"/>
    
    <!-- Main content -->
    <div class="page-contain">
        <div class="container">
            <div class="checkout-page">
                <h2 class="checkout-title">Thanh toán</h2>

                <!-- Messages -->
                <c:if test="${not empty sessionScope.ERROR_MESSAGE}">
                    <div class="alert alert-danger">
                        ${sessionScope.ERROR_MESSAGE}
                        <c:remove var="ERROR_MESSAGE" scope="session" />
                    </div>
                </c:if>
                <c:if test="${not empty sessionScope.SUCCESS_MESSAGE}">
                    <div class="alert alert-success">
                        ${sessionScope.SUCCESS_MESSAGE}
                        <c:remove var="SUCCESS_MESSAGE" scope="session" />
                    </div>
                </c:if>

                <c:if test="${empty cartItems}">
                    <div class="alert alert-danger">
                        Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.
                    </div>
                    <div class="text-center">
                        <a href="HomeServlet" class="btn btn-primary">Quay lại mua sắm</a>
                    </div>
                </c:if>

                <c:if test="${not empty cartItems}">
                    <div class="row">
                        <!-- Checkout Form -->
                        <div class="col-lg-8">
                            <form action="checkout" method="POST">
                                <input type="hidden" name="action" value="placeOrder">

                                <!-- Shipping Address -->
                                <div class="checkout-section">
                                    <h4>Địa chỉ giao hàng</h4>

                                    <div class="form-group">
                                        <label for="addressId" class="form-label">Chọn địa chỉ</label>
                                        <select id="addressId" name="addressId" class="form-control" onchange="toggleNewAddressFields()">
                                            <c:forEach items="${addresses}" var="address">
                                                <option value="${address.addressId}" ${address.isDefault ? 'selected' : ''}>
                                                    ${address.recipientName} - ${address.phone} - ${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}
                                                </option>
                                            </c:forEach>
                                            <option value="new">+ Thêm địa chỉ mới</option>
                                        </select>
                                        <!-- Xóa div hiển thị địa chỉ đã chọn -->
                                    </div>

                                    <div id="new_address_fields" class="new-address-fields">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group">
                                                    <label for="recipientName" class="form-label">Tên người nhận *</label>
                                                    <input type="text" id="recipientName" name="recipientName" class="form-control" value="${sessionScope.user.fullName}">
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="form-group">
                                                    <label for="phone" class="form-label">Số điện thoại *</label>
                                                    <input type="text" id="phone" name="phone" class="form-control" pattern="\d{10,11}" title="Số điện thoại phải có 10-11 chữ số">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label for="province" class="form-label">Tỉnh/Thành phố *</label>
                                                    <input type="text" id="province" name="province" class="form-control">
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label for="district" class="form-label">Quận/Huyện *</label>
                                                    <input type="text" id="district" name="district" class="form-control">
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label for="ward" class="form-label">Phường/Xã *</label>
                                                    <input type="text" id="ward" name="ward" class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="addressDetail" class="form-label">Chi tiết địa chỉ *</label>
                                            <input type="text" id="addressDetail" name="addressDetail" class="form-control">
                                        </div>
                                    </div>
                                </div>

                                <!-- Payment Methods -->
                                <div class="checkout-section">
                                    <h4>Phương thức thanh toán</h4>
                                    <div class="payment-methods">
                                        <div class="payment-method active">
                                            <input type="radio" id="cod" name="paymentMethod" value="cod" checked>
                                            <label for="cod">
                                                <div class="payment-method-title">Thanh toán khi nhận hàng (COD)</div>
                                                <div class="payment-method-description">Thanh toán bằng tiền mặt khi nhận hàng</div>
                                            </label>
                                        </div>
                                        <div class="payment-method">
                                            <input type="radio" id="e-wallet" name="paymentMethod" value="e-wallet">
                                            <label for="e-wallet">
                                                <div class="payment-method-title">Thanh toán qua VNPay</div>
                                                <div class="payment-method-description">Thanh toán an toàn qua cổng thanh toán VNPay</div>
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Order Notes -->
                                <div class="checkout-section">
                                    <h4>Ghi chú đơn hàng</h4>
                                    <div class="form-group">
                                        <textarea name="notes" class="form-control" rows="4" placeholder="Ghi chú về đơn hàng, ví dụ: thời gian hay chỉ dẫn địa điểm giao hàng chi tiết hơn."></textarea>
                                    </div>
                                </div>

                                <!-- Submit Button -->
                                <div class="checkout-section">
                                    <button type="submit" class="btn-checkout">Đặt hàng</button>
                                </div>
                            </form>
                            
                        </div>

                        <!-- Order Summary -->
                        <div class="col-lg-4">
                            <!-- Coupon Section - Moved here -->
                            <div class="checkout-section">
                                <h4>Mã giảm giá</h4>
                                <div class="coupon-section">
                                    <form action="apply-coupon" method="POST" class="coupon-form">
                                        <input type="text" name="couponCode" class="form-control coupon-input" placeholder="Nhập mã giảm giá">
                                        <button type="submit" class="btn-apply-coupon">Áp dụng</button>
                                    </form>
                                    <c:if test="${not empty sessionScope.COUPON}">
                                        <div class="coupon-applied">
                                            <i class="fa fa-check-circle"></i> Mã giảm giá ${sessionScope.COUPON.code} đã được áp dụng (-<fmt:formatNumber value="${sessionScope.COUPON.discountAmount}" pattern="#,##0.00"/>đ)
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="checkout-section">
                                <h4>Đơn hàng của bạn</h4>

                                <!-- Product List -->
                                <div class="product-list">
                                    <c:forEach items="${cartItems}" var="item">
                                        <div class="product-item">
                                            <img src="${not empty productMainImages[item.productId] ? productMainImages[item.productId] : 'assets/images/products/p-01.jpg'}" alt="${item.productName}" class="product-image">
                                            <div class="product-details">
                                                <div class="product-name">${item.productName}</div>
                                                <div class="product-price" style="color: #000;">
                                                    <fmt:formatNumber value="${item.productPrice}" pattern="#,##0.00"/>đ
                                                </div>
                                                <div class="product-quantity">Số lượng: ${item.quantity}</div>
                                                
                                                <!-- Hiển thị thuộc tính sản phẩm -->
                                                <c:if test="${not empty productAttributesMap[item.productId]}">
                                                    <div class="product-attributes">
                                                        <c:forEach items="${productAttributesMap[item.productId]}" var="attr">
                                                            <span class="attribute-item">${attr.attributeName}: ${attr.value}</span>
                                                        </c:forEach>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Order Summary -->
                                <div class="order-summary">
                                    <div class="order-summary-item">
                                        <span>Tạm tính</span>
                                        <span><fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/>đ</span>
                                    </div>
                                    <div class="order-summary-item">
                                        <span>Giảm giá</span>
                                        <span><fmt:formatNumber value="${discountAmount}" pattern="#,##0.00"/>đ</span>
                                    </div>
                                    <div class="order-summary-item">
                                        <span>Phí vận chuyển</span>
                                        <span><fmt:formatNumber value="${shippingFee}" pattern="#,##0.00"/>đ</span>
                                    </div>
                                    <div class="order-summary-item">
                                        <span>Thuế (5%)</span>
                                        <span><fmt:formatNumber value="${tax}" pattern="#,##0.00"/>đ</span>
                                    </div>
                                    <div class="order-summary-item total">
                                        <span>Tổng cộng</span>
                                        <span><fmt:formatNumber value="${finalAmount}" pattern="#,##0.00"/>đ</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
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
    <script>
     // Hiển thị/ẩn form địa chỉ mới
        function toggleNewAddressFields() {
            const addressSelect = document.getElementById("addressId");
            const newAddressFields = document.getElementById("new_address_fields");
            
            if (addressSelect && newAddressFields) {
                console.log("toggleNewAddressFields called, value:", addressSelect.value);
                
                if (addressSelect.value === "new") {
                    newAddressFields.style.display = "block";
                    const inputs = newAddressFields.querySelectorAll("input");
                    inputs.forEach(input => {
                        input.required = true;
                        input.disabled = false;
                    });
                } else {
                    newAddressFields.style.display = "none";
                    const inputs = newAddressFields.querySelectorAll("input");
                    inputs.forEach(input => {
                        input.required = false;
                        input.disabled = true;
                    });
                }
            } else {
                console.error("Address select or new address fields element not found");
            }
        }

        // Chọn phương thức thanh toán
        function setupPaymentMethods() {
            const paymentMethods = document.querySelectorAll(".payment-method");
            paymentMethods.forEach(method => {
                method.addEventListener("click", function() {
                    paymentMethods.forEach(m => m.classList.remove("active"));
                    this.classList.add("active");
                    this.querySelector("input[type='radio']").checked = true;
                });
            });
        }

        // Khởi tạo khi trang tải xong
        window.onload = function() {
            console.log("Window loaded");
            
            // Fix select box issue
            const addressSelect = document.getElementById("addressId");
            if (addressSelect) {
                // Giải pháp 1: Tạo một select mới để thay thế cái cũ
                const fixSelectDisplay = () => {
                    // Force browser to redraw select element
                    const originalValue = addressSelect.value;
                    const originalHtml = addressSelect.innerHTML;
                    
                    // Clone select và thay thế
                    const parent = addressSelect.parentNode;
                    const newSelect = document.createElement('select');
                    newSelect.id = 'addressId';
                    newSelect.name = 'addressId';
                    newSelect.className = 'form-control';
                    newSelect.innerHTML = originalHtml;
                    newSelect.value = originalValue;
                    newSelect.onchange = toggleNewAddressFields;
                    
                    // Thay thế select cũ bằng select mới
                    parent.replaceChild(newSelect, addressSelect);
                    
                    // Kích hoạt sự kiện change để cập nhật UI
                    toggleNewAddressFields();
                    
                    // Xóa bỏ mọi phần tử không cần thiết được tạo ra bởi nice-select
                    const unnecessaryElements = document.querySelectorAll('.nice-select, .nice-select-dropdown, .list');
                    unnecessaryElements.forEach(el => {
                        if (el && el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    });
                };
                
                // Giải pháp 2: Vô hiệu hóa nice-select
                if (typeof jQuery !== 'undefined') {
                    try {
                        // Vô hiệu hóa nice-select
                        if ($.fn.niceSelect) {
                            // Vô hiệu hóa nice-select cho tất cả các select
                            $('select').each(function() {
                                try {
                                    $(this).niceSelect('destroy');
                                } catch (e) {}
                            });
                        }
                    } catch (e) {
                        console.error("Error with jQuery:", e);
                    }
                }
                
                // Thực hiện sau khi trang đã load xong
                setTimeout(fixSelectDisplay, 100);
                
                // Thêm một lần nữa để đảm bảo các phần tử không cần thiết bị xóa
                setTimeout(() => {
                    // Xóa bỏ mọi phần tử không cần thiết
                    const unnecessaryElements = document.querySelectorAll('.nice-select, .nice-select-dropdown, .list');
                    unnecessaryElements.forEach(el => {
                        if (el && el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    });
                }, 500);
            }
            
            setupPaymentMethods();
        };
    </script>
</body>

</html>
