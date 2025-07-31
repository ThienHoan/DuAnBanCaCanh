<%-- 
    Document   : product_detail
    Created on : May 20, 2025, 11:14:51 PM
    Author     : hoan6
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.name} - Fish Shop</title>
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
        .feature-table th {
            width: 40%;
            font-weight: 600;
        }
        .attribute-list {
            margin-bottom: 0;
        }
        .attribute-list li {
            padding: 8px 0;
            border-bottom: 1px solid #e9ecef;
        }
        .attribute-list li:last-child {
            border-bottom: none;
        }
        .feature-label {
            color: #666;
            font-weight: 600;
        }
        
        /* Rating Summary Styles */
        .rating-summary-wrapper {
            display: flex;
            width: 100%;
            margin-bottom: 30px;
        }
        
        .rating-info {
            background: #fff;
            border-radius: 12px;
            padding: 26px 28px;
            margin-bottom: 30px;
            box-shadow: 0 4px 12px rgba(0,0,0,.08);
            transition: box-shadow .25s ease, transform .25s;
            width: 100%;
            display: flex;
            flex-direction: column;
        }
        
        .rating-info:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(0,0,0,.12);
        }
        
        .rating-info .index {
            width: 100%;
            margin-bottom: 20px;
            font-size: 28px;
        }
        
        .rating-info .rating {
            width: 100%;
            margin-bottom: 20px;
        }
        
        .rating-info .options {
            width: 100%;
            padding: 0;
            margin: 0;
            list-style: none;
        }
        
        .rating-info .options li {
            margin-bottom: 15px;
            width: 100%;
        }
        
        .detail-for {
            display: flex;
            align-items: center;
            width: 100%;
            gap: 15px;
        }
        
        .option-name {
            min-width: 80px;
            font-weight: 500;
            color: #6c757d;
        }
        
        .progres {
            flex: 1;
            margin: 0 14px;
        }
        
        .line-100percent {
            width: 100%;
            height: 8px;
            background: #e9ecef;
            border-radius: 4px;
            overflow: hidden;
        }
        
        .percent {
            display: block;
            height: 100%;
            background: linear-gradient(90deg,#ffb400,#ff8c00);
            transition: width .5s ease;
            border-radius: 4px;
        }
        
        .number {
            min-width: 40px;
            text-align: right;
            font-weight: 600;
        }
        
        @media (max-width: 768px) {
            .rating-summary-wrapper {
                flex-direction: column;
            }
            
            .rating-info {
                padding: 20px;
            }
            
            .option-name {
                min-width: 60px;
            }
        }
        
        .tab-content {
            padding: 20px;
        }
        .specification-item {
            margin-bottom: 10px;
        }
        .spec-title {
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        /* Fixed styles for tabs */
        .product-tabs .tab-head .tab-element {
            cursor: pointer;
        }
        .product-tabs .tab-content .tab-contain {
            display: none;
        }
        .product-tabs .tab-content .tab-contain.active {
            display: block;
        }
        
        /* New styles for enhanced product details */
        .badge {
            font-size: 90%;
            font-weight: 500;
        }
        .badge-success {
            background-color: #28a745;
            color: white;
        }
        .badge-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .badge-danger {
            background-color: #dc3545;
            color: white;
        }
        .product-meta {
            border-top: 1px solid #eee;
            padding-top: 15px;
        }
        .meta-label {
            font-weight: 600;
            margin-right: 10px;
            color: #666;
        }
        .meta-value {
            color: #333;
        }
        .card {
            margin-bottom: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            border: none;
        }
        .card-header {
            background-color: #f8f9fa;
            font-weight: 600;
            padding: 12px 15px;
        }
        .alert {
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }
        .stock-info-detailed {
            padding: 5px;
        }
        
        /* Product variant styles */
        .product-variants {
            margin-top: 15px;
            margin-bottom: 20px;
        }
               .variant-group {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }
        .variant-title {
            font-size: 16px;
            font-weight: 600;
            margin-top: 0;
            margin-right: 10px;
        }
        .variant-options {
            display: flex;
            flex-wrap: wrap;
        }
        .color-option {
    width: auto;
    height: auto;
    padding: 5px 10px;
    border-radius: 4px;
    margin-right: 10px;
    margin-bottom: 10px;
    cursor: pointer;
    transition: transform 0.2s, border-color 0.2s;
    color: #333;
    background-color: #f8f9fa !important;
    border: 1px solid #ddd;
    display: inline-block;
    text-align: center;
}
         .color-option:hover {
            transform: scale(1.1);
            border-color: #7FAF51 !important;
        }
        .color-option.active {
            border: 2px solid #00CAFF !important;
        }
        .size-option, .generic-option {
            padding: 5px 15px;
            margin-right: 10px;
            margin-bottom: 10px;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.2s;
            text-decoration: none;
        }
        .size-option:hover, .generic-option:hover {
            border-color: #e73918 !important;
            color: #e73918 !important;
        }
        .size-option.active, .generic-option.active {
            border-color: #e73918 !important;
            color: #e73918 !important;
            font-weight: bold;
        }
        .d-flex {
            display: flex;
        }
        .flex-wrap {
            flex-wrap: wrap;
        }
        .mt-4 {
            margin-top: 1.5rem;
        }
        .mb-3 {
            margin-bottom: 1rem;
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

    <!-- HEADER - Replace with your header.jsp include -->
    <jsp:include page="header.jsp" />

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">${product.name}</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                <c:if test="${category != null}">
                    <li class="nav-item"><a href="category?id=${category.categoryId}" class="permal-link">${category.name}</a></li>
                </c:if>
                <li class="nav-item"><span class="current-page">${product.name}</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain single-product">
        <div class="container">

            <!-- Main content -->
            <div id="main-content" class="main-content">
                
                <!-- summary info -->
                <div class="sumary-product single-layout">
                    <div class="media">
                        <!-- Main Images Slider -->
                        <ul class="biolife-carousel slider-for" data-slick='{"arrows":false,"dots":false,"slidesMargin":30,"slidesToShow":1,"slidesToScroll":1,"fade":true,"asNavFor":".slider-nav"}'>
                            <c:if test="${not empty productImages}">
                                <c:forEach var="image" items="${productImages}">
                                    <li><img src="${image.imageUrl}" alt="${product.name}" width="500" height="500"></li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty productImages}">
                                <li><img src="assets/images/products/p-05.jpg" alt="Default Product" width="500" height="500"></li>
                            </c:if>
                        </ul>
                        <!-- Thumbnail Navigation -->
                        <ul class="biolife-carousel slider-nav" data-slick='{"arrows":false,"dots":false,"centerMode":false,"focusOnSelect":true,"slidesMargin":10,"slidesToShow":4,"slidesToScroll":1,"asNavFor":".slider-for"}'>
                            <c:if test="${not empty productImages}">
                                <c:forEach var="image" items="${productImages}">
                                    <li><img src="${image.imageUrl}" alt="${product.name}" width="88" height="88"></li>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty productImages}">
                                <li><img src="assets/images/products/p-05.jpg" alt="Default Product" width="88" height="88"></li>
                            </c:if>
                        </ul>
                    </div>
                    <div class="product-attribute">
                        <h3 class="title">${product.name}</h3>
                        <div class="rating">
                            <div class="star-display">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fa ${i <= averageRating ? 'fa-star text-warning' : 'fa-star-o text-muted'}" style="font-size: 18px; margin-right: 2px;"></i>
                                </c:forEach>
                            </div>
                            <span class="review-count">(${totalReviews} Đánh giá)</span>
                            <span class="qa-text">Hỏi & Đáp</span>
                            <c:if test="${category != null}">
                                <b class="category">Loại: ${category.name}</b>
                            </c:if>
                        </div>
                        <span class="sku">Mã sản phẩm: ${product.sku}</span>
                        <p class="excerpt">${product.shortDescription}</p>
                        <div class="price">
                            <c:if test="${product.salePrice.doubleValue() > 0}">
                                <ins><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0"/></span></ins>
                                <del><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${product.price}" pattern="#,##0"/></span></del>
                                <p class="saving-info">Tiết kiệm: <span class="percentage"><fmt:formatNumber value="${((product.price.doubleValue() - product.salePrice.doubleValue()) / product.price.doubleValue()) * 100}" pattern="#.#"/>%</span></p>
                            </c:if>
                            <c:if test="${product.salePrice.doubleValue() <= 0}">
                                <ins><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${product.price}" pattern="#,##0"/></span></ins>
                            </c:if>
                        </div>
                        <div class="shipping-info">
                            <p class="shipping-day">Giao hàng trong 3 ngày</p>
                            <p class="for-today">Nhận hàng miễn phí tại cửa hàng</p>
                        </div>
                        
                        <!-- Product Variants Selection -->
<%-- Xác định màu sắc hiện tại của sản phẩm --%>

<c:set var="currentColor" value="" />
<c:forEach var="attr" items="${productAttributeValues}">
    <c:if test="${attr.attributeId == 1}">
        <c:set var="currentColor" value="${attr.value}" />
    </c:if>
</c:forEach>

<div class="product-variants">
    <!-- Color Selection -->
    <div class="variant-group">
        <h5 class="variant-title">Màu sắc:</h5>
        <div class="variant-options">
            <div class="d-flex flex-wrap">
                <c:forEach var="color" items="${colorToProductId.keySet()}">
                    <c:set var="isCurrentColor" value="${color == currentColor}" />
                    <a href="product-detail?id=${colorToProductId[color]}"
                       class="color-option${isCurrentColor ? ' active' : ''}"
                       data-color="${color}"
                       title="${color}">
                        ${color}
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- Size Selection -->
<div class="variant-group">
    <h5 class="variant-title">Kích thước:</h5>
    <div class="variant-options">
        <div class="d-flex flex-wrap">
            <c:forEach var="size" items="${productAttributeValueSizes}">
                <c:set var="isSizeSelected" value="false" />
                <c:forEach var="attr" items="${productAttributeValues}">
                    <c:if test="${attr.attributeId == 2 && attr.value == size.value}">
                        <c:set var="isSizeSelected" value="true" />
                    </c:if>
                </c:forEach>
                                <a href="product-detail?id=${size.productId}" 
                   class="color-option${isSizeSelected ? ' active' : ''}"
                   data-size="${size.value}"
                   title="${size.value}">
                    ${size.value}
                </a>
            </c:forEach>
        </div>
    </div>
</div>
                        
                        <!-- Enhanced stock status display -->
                        <div class="stock-info">
                            <c:choose>
                                <c:when test="${product.quantity > 10}">
                                    <p class="stock-status in-stock">
                                        <span class="badge badge-success p-2">
                                            <i class="fa fa-check-circle" aria-hidden="true"></i> Còn hàng
                                        </span>
                                        <span class="text-success ml-2">(${product.quantity} sản phẩm)</span>
                                    </p>
                                </c:when>
                                <c:when test="${product.quantity > 0}">
                                    <p class="stock-status in-stock">
                                        <span class="badge badge-warning p-2">
                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i> Sắp hết hàng
                                        </span>
                                        <span class="text-warning ml-2">(Chỉ còn ${product.quantity} sản phẩm)</span>
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <p class="stock-status out-stock">
                                        <span class="badge badge-danger p-2">
                                            <i class="fa fa-times-circle" aria-hidden="true"></i> Hết hàng
                                        </span>
                                    </p>
                                </c:otherwise>
                            </c:choose>
                            
                            <!-- Display product SKU and status -->
                            <div class="product-meta mt-3">
                                <div class="product-sku mb-2">
                                    <span class="meta-label">Mã sản phẩm:</span>
                                    <span class="meta-value">${product.sku}</span>
                                </div>
                                <div class="product-status">
                                    <span class="meta-label">Trạng thái:</span>
                                    <span class="meta-value">
                                        <c:choose>
                                            <c:when test="${product.status == 'active'}">
                                                <span class="text-success">Đang kinh doanh</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-secondary">${product.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="action-form">
                        <div class="quantity-box">
                            <span class="title">Số lượng:</span>
                            <div class="qty-input">
                                <input type="text" name="qty" value="1" data-max_value="${product.quantity > 0 ? product.quantity : 0}" data-min_value="1" data-step="1">
                                <a href="#" class="qty-btn btn-up"><i class="fa fa-caret-up" aria-hidden="true"></i></a>
                                <a href="#" class="qty-btn btn-down"><i class="fa fa-caret-down" aria-hidden="true"></i></a>
                            </div>
                        </div>
                        <div class="buttons">
                            <a href="javascript:void(0);" 
                               class="btn add-to-cart-btn" 
                               data-product-id="${product.productId}"
                               onclick="addToCart(event, ${product.productId})"
                               ${product.quantity <= 0 ? 'disabled' : ''}>
                                <i class="fa fa-cart-arrow-down" aria-hidden="true"></i>
                                thêm vào giỏ hàng
                            </a>
                            <p class="pull-row">
                                <a href="#" class="btn wishlist-btn">yêu thích</a>
                                <a href="#" class="btn compare-btn">so sánh</a>
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Tab info -->
                <div class="product-tabs single-layout biolife-tab-contain">
                    <div class="tab-head">
                        <ul class="tabs">
                            <li class="tab-element active"><a href="#tab_1st" class="tab-link">Mô tả sản phẩm</a></li>
                            <li class="tab-element"><a href="#tab_2nd" class="tab-link">Thông tin cơ bản</a></li>
                            <li class="tab-element"><a href="#tab_3rd" class="tab-link">Chi tiết cá cảnh</a></li>
                            <li class="tab-element"><a href="#tab_4th" class="tab-link">Hướng dẫn chăm sóc</a></li>
                            <li class="tab-element"><a href="#tab_5th" class="tab-link">Đánh giá <sup>(${totalReviews})</sup></a></li>
                            <li class="tab-element"><a href="#tab_6th" class="tab-link">Vận chuyển & Kho hàng</a></li>
                        </ul>
                    </div>
                    <div class="tab-content">
                        <div id="tab_1st" class="tab-contain desc-tab active">
                            <p class="desc">${product.description}</p>
                        </div>
                        
                        <!-- Thông tin cơ bản -->
                        <div id="tab_2nd" class="tab-contain basic-info-tab">
                            <div class="row">
                                <div class="col-md-12">
                                    <h4 class="mb-3"><i class="fa fa-info-circle"></i> Thông tin cơ bản</h4>
                                    <table class="tbl_attributes table table-bordered">
                                        <tbody>
                                            <tr>
                                                <th style="width: 30%;">Danh mục:</th>
                                                <td>${category != null ? category.name : 'Chưa phân loại'}</td>
                                            </tr>
                                            <tr>
                                                <th>Mã sản phẩm:</th>
                                                <td>${product.sku}</td>
                                            </tr>
                                            <tr>
                                                <th>Trạng thái:</th>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${product.status == 'active'}">
                                                            <span class="text-success">Đang kinh doanh</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-secondary">${product.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Số lượng trong kho:</th>
                                                <td>${product.quantity} sản phẩm</td>
                                            </tr>
                                            <tr>
                                                <th>Ngày tạo:</th>
                                                <td>${product.createdAt != null ? product.createdAt : 'N/A'}</td>
                                            </tr>
                                            <tr>
                                                <th>Cập nhật lần cuối:</th>
                                                <td>${product.updatedAt != null ? product.updatedAt : 'N/A'}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Chi tiết cá cảnh -->
                        <div id="tab_3rd" class="tab-contain fish-details-tab">
    <c:if test="${productDetail != null}">
        <div class="row">
            <div class="col-md-12">
                <h4 class="mb-3"><i class="fa fa-fish"></i> Chi tiết cá cảnh</h4>
                <table class="tbl_attributes table table-bordered">
                    <tbody>
                        <tr>
                            <th>Tên khoa học:</th>
                            <td>${not empty productDetail.scientificName ? productDetail.scientificName : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Tên thông dụng:</th>
                            <td>${not empty productDetail.commonName ? productDetail.commonName : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Nguồn gốc:</th>
                            <td>${not empty productDetail.origin ? productDetail.origin : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Kích thước:</th>
                            <td>${not empty productDetail.size ? productDetail.size : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Tuổi thọ:</th>
                            <td>${not empty productDetail.lifespan ? productDetail.lifespan : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Loại nước:</th>
                            <td>${not empty productDetail.waterType ? productDetail.waterType : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Nhiệt độ nước:</th>
                            <td>${not empty productDetail.waterTemperature ? productDetail.waterTemperature : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Độ pH:</th>
                            <td>${not empty productDetail.waterPh ? productDetail.waterPh : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Chế độ ăn:</th>
                            <td>${not empty productDetail.diet ? productDetail.diet : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Độ khó sinh sản:</th>
                            <td>${not empty productDetail.breedingDifficulty ? productDetail.breedingDifficulty : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Mức độ chăm sóc:</th>
                            <td>${not empty productDetail.careLevel ? productDetail.careLevel : 'N/A'}</td>
                        </tr>
                        <tr>
                            <th>Tương thích:</th>
                            <td>${not empty productDetail.compatibility ? productDetail.compatibility : 'N/A'}</td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
    <c:if test="${productDetail == null}">
        <p>Không có thông tin chi tiết cho sản phẩm này.</p>
    </c:if>
</div>
                        
                        <!-- Hướng dẫn chăm sóc -->
                        <div id="tab_4th" class="tab-contain care-instructions-tab">
                            <c:if test="${productDetail != null}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <h4 class="mb-3"><i class="fa fa-heart"></i> Yêu cầu chăm sóc</h4>
                                        <ul class="attribute-list">
                                            <li>
                                                <span class="feature-label">Chế độ ăn:</span>
                                                <span class="feature-value">${not empty productDetail.diet ? productDetail.diet : 'Không có thông tin'}</span>
                                            </li>
                                            <li>
                                                <span class="feature-label">Độ khó khi sinh sản:</span>
                                                <span class="feature-value">${not empty productDetail.breedingDifficulty ? productDetail.breedingDifficulty : 'Không có thông tin'}</span>
                                            </li>
                                            <li>
                                                <span class="feature-label">Mức độ chăm sóc:</span>
                                                <span class="feature-value">${not empty productDetail.careLevel ? productDetail.careLevel : 'Không có thông tin'}</span>
                                            </li>
                                            <li>
                                                <span class="feature-label">Khả năng tương thích:</span>
                                                <span class="feature-value">${not empty productDetail.compatibility ? productDetail.compatibility : 'Không có thông tin'}</span>
                                            </li>
                                        </ul>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <h4 class="mb-3"><i class="fa fa-heart"></i> Yêu cầu về nước</h4>
                                        <ul class="attribute-list">
                                            <li>
                                                <span class="feature-label">Loại nước:</span>
                                                <span class="feature-value">${not empty productDetail.waterType ? productDetail.waterType : 'Không có thông tin'}</span>
                                            </li>
                                            <li>
                                                <span class="feature-label">Nhiệt độ nước:</span>
                                                <span class="feature-value">${not empty productDetail.waterTemperature ? productDetail.waterTemperature : 'Không có thông tin'}</span>
                                            </li>
                                            <li>
                                                <span class="feature-label">Độ pH nước:</span>
                                                <span class="feature-value">${not empty productDetail.waterPh ? productDetail.waterPh : 'Không có thông tin'}</span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${productDetail == null}">
                                <p>Không có hướng dẫn chăm sóc cho sản phẩm này.</p>
                            </c:if>
                        </div>
                        
                        <!-- ====== REVIEW TAB - GIỮ NGUYÊN TỪ PASTE-2 ====== -->
<div id="tab_5th" class="tab-contain review-tab">

    
      <!-- Cột bên trái cho rating summary và danh sách đánh giá -->
      <div class="col-md-5">
        <!-- ====== TỔNG HỢP ĐÁNH GIÁ ====== -->
        <div class="rating-summary-block">
          <div class="rating-info">
        <%-- Tính toán số lượng đánh giá cho mỗi mức sao --%>
        <c:set var="star1" value="0"/>
        <c:set var="star2" value="0"/>
        <c:set var="star3" value="0"/>
        <c:set var="star4" value="0"/>
        <c:set var="star5" value="0"/>
        <c:set var="totalRating" value="0"/>
        
        <%-- Sử dụng reviews thay vì processedReviews --%>
        <c:forEach var="review" items="${reviews}">
            <c:set var="rating" value="${review.rating}"/>
            <c:choose>
                <c:when test="${rating == 1}"><c:set var="star1" value="${star1 + 1}"/></c:when>
                <c:when test="${rating == 2}"><c:set var="star2" value="${star2 + 1}"/></c:when>
                <c:when test="${rating == 3}"><c:set var="star3" value="${star3 + 1}"/></c:when>
                <c:when test="${rating == 4}"><c:set var="star4" value="${star4 + 1}"/></c:when>
                <c:when test="${rating == 5}"><c:set var="star5" value="${star5 + 1}"/></c:when>
            </c:choose>
            <c:set var="totalRating" value="${totalRating + rating}"/>
        </c:forEach>

        <%-- Sử dụng giá trị từ servlet --%>
        <c:set var="averagePercent" value="${(averageRating / 5) * 100}"/>

        <%-- Tính phần trăm cho mỗi mức sao --%>
        <c:set var="star1Percent" value="${totalReviews > 0 ? (star1 * 100) / totalReviews : 0}"/>
        <c:set var="star2Percent" value="${totalReviews > 0 ? (star2 * 100) / totalReviews : 0}"/>
        <c:set var="star3Percent" value="${totalReviews > 0 ? (star3 * 100) / totalReviews : 0}"/>
        <c:set var="star4Percent" value="${totalReviews > 0 ? (star4 * 100) / totalReviews : 0}"/>
        <c:set var="star5Percent" value="${totalReviews > 0 ? (star5 * 100) / totalReviews : 0}"/>

        <p class="index">
          <strong class="rating">
            <fmt:formatNumber value="${averageRating}" maxFractionDigits="1" minFractionDigits="1"/>
          </strong> out of 5
        </p>

        <div class="rating">
          <div class="star-rating">
            <div class="stars-outer">
              <div class="stars-inner" style="width: ${averagePercent}%"></div>
            </div>
          </div>
        </div>
        
        <style>
            
        .star-rating {
            position: relative;
            display: inline-block;
            font-size: 24px;
            line-height: 1;
            width: 120px;
        }
        .star-rating::before {
  content: none !important;
  display: none !important;
}
        
        .stars-outer {
            position: relative;
            display: inline-block;
        }
        
        .stars-outer::before {
            content: "★★★★★";
            color: #d8d8d8;
        }
        
        .stars-inner {
            position: absolute;
            top: 0;
            left: 0;
            white-space: nowrap;
            overflow: hidden;
            width: 0;
        }
        
        .stars-inner::before {
            content: "★★★★★";
            color: #ffc107;
        }
        </style>

        <ul class="options">
          <li>
            <div class="detail-for">
              <span class="option-name">5 stars</span>
              <span class="progres">
                <span class="line-100percent">
                  <span class="percent" style="width:${star5Percent}%;"></span>
                </span>
              </span>
              <span class="number">${star5}</span>
            </div>
          </li>
          <li>
            <div class="detail-for">
              <span class="option-name">4 stars</span>
              <span class="progres">
                <span class="line-100percent">
                  <span class="percent" style="width:${star4Percent}%;"></span>
                </span>
              </span>
              <span class="number">${star4}</span>
            </div>
          </li>
          <li>
            <div class="detail-for">
              <span class="option-name">3 stars</span>
              <span class="progres">
                <span class="line-100percent">
                  <span class="percent" style="width:${star3Percent}%;"></span>
                </span>
              </span>
              <span class="number">${star3}</span>
            </div>
          </li>
          <li>
            <div class="detail-for">
              <span class="option-name">2 stars</span>
              <span class="progres">
                <span class="line-100percent">
                  <span class="percent" style="width:${star2Percent}%;"></span>
                </span>
              </span>
              <span class="number">${star2}</span>
            </div>
          </li>
          <li>
            <div class="detail-for">
              <span class="option-name">1 star</span>
              <span class="progres">
                <span class="line-100percent">
                  <span class="percent" style="width:${star1Percent}%;"></span>
                </span>
              </span>
              <span class="number">${star1}</span>
            </div>
          </li>
        </ul>
      </div>
    </div>
        </div>
 
      <!-- Cột bên phải cho form đánh giá -->
      <div class="col-md-7">
        <div class="user-review-section">
          <h4>Xem các đánh giá</h4>

          <c:choose>
            <c:when test="${empty reviews}">
              <div class="no-reviews text-center py-4">
                <p class="text-muted">Chưa có đánh giá nào cho sản phẩm này.</p>
              </div>
            </c:when>
            <c:otherwise>
              <!-- KHUNG CUỘN -->
              <div class="reviews-scroll">
                <div class="reviews-list">
                  <c:forEach var="review" items="${reviews}">
                    <div class="review-item mb-4">
                      <div class="review-header d-flex justify-content-between align-items-start">
                        <div class="reviewer-info">
                          <h5 class="reviewer-name mb-1">User #${review.userId}</h5>
                          <div class="rating mb-2">
                            <c:forEach begin="1" end="5" var="i">
                              <i class="fa ${i <= review.rating ? 'fa-star text-warning' : 'fa-star-o text-muted'}"></i>
                            </c:forEach>
                            <span class="rating-text">(${review.rating}/5 sao)</span>
                          </div>
                        </div>
                        <div class="review-date">
                          <small class="text-muted">${review.reviewDate}</small>
                        </div>
                      </div>

                      <div class="review-content mt-3">
                        <p class="mb-0">${review.comment}</p>
                      </div>

                      <c:set var="reviewImagesKey" value="reviewImages_${review.reviewId}" />
                      <c:if test="${not empty requestScope[reviewImagesKey]}">
                        <div class="review-images mt-3 d-flex flex-wrap">
                          <c:forEach var="image" items="${requestScope[reviewImagesKey]}">
                            <img src="${image.imageUrl}" alt="Review Image"
                                 class="img-thumbnail cursor-pointer"
                                 loading="lazy"
                                 style="max-width:100px;max-height:100px;object-fit:cover;"
                                 onclick="showImageModal('${image.imageUrl}')">
                          </c:forEach>
                        </div>
                      </c:if>

                      <c:if test="${review.isVerifiedPurchase == 1}">
                        <div class="mt-2">
                          <span class="badge bg-success">
                            <i class="fa fa-check-circle"></i> Đã mua hàng
                          </span>
                        </div>
                      </c:if>
                    </div>
                  </c:forEach>
                </div>
              </div>
              <!-- /reviews-scroll -->

              <div class="reviews-summary mt-3 text-center">
                <p class="text-muted"><i class="fa fa-comment"></i> Hiển thị ${totalReviews} đánh giá</p>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <!-- BÊN PHẢI: FORM ĐÁNH GIÁ / REVIEW CỦA TÔI -->
      <div class="col-lg-7 col-md-7 col-sm-6 col-xs-12">
        <div class="user-review-section mt-0">
          <h4>Đánh giá của bạn</h4>
          
          <c:choose>
            <%-- Người dùng chưa đăng nhập --%>
            <c:when test="${empty user}">
              <div class="alert alert-info">
                <p>Vui lòng <a href="${pageContext.request.contextPath}/login">đăng nhập</a> để đánh giá sản phẩm.</p>
              </div>
            </c:when>
            
            <%-- Người dùng đã đăng nhập nhưng chưa mua sản phẩm --%>
            <c:when test="${not canReview}">
              <div class="alert alert-warning">
                <p>Bạn cần mua và nhận sản phẩm này trước khi có thể đánh giá.</p>
              </div>
            </c:when>
            
            <%-- Người dùng đã đăng nhập, đã mua sản phẩm và đã đánh giá --%>
            <c:when test="${hasUserReview}">
              <!-- REVIEW ĐÃ ĐĂNG -->
              <div class="review-item mb-4">
                <div class="review-header d-flex justify-content-between align-items-start">
                  <div class="reviewer-info">
                    <h5 class="reviewer-name mb-1">${userReviewData.username}</h5>
                    <div class="rating mb-2">
                      <c:forEach begin="1" end="5" var="i">
                        <i class="fa ${i <= userReviewData.review.rating ? 'fa-star text-warning' : 'fa-star-o text-muted'}"></i>
                      </c:forEach>
                      <span class="rating-text">(${userReviewData.review.rating}/5 sao)</span>
                    </div>
                  </div>
                  <div class="review-date">
                    <small class="text-muted">${userReviewData.review.reviewDate}</small>
                  </div>
                </div>

                <div class="review-content mt-3">
                  <p class="mb-0">${userReviewData.review.comment}</p>
                </div>

                <c:if test="${not empty userReviewData.images}">
                  <div class="review-images mt-3 d-flex flex-wrap">
                    <c:forEach var="image" items="${userReviewData.images}">
                      <img src="${image.imageUrl}" alt="Review Image"
                           class="img-thumbnail cursor-pointer"
                           loading="lazy"
                           style="max-width:100px;max-height:100px;object-fit:cover;"
                           onclick="showImageModal('${image.imageUrl}')">
                    </c:forEach>
                  </div>
                </c:if>

                <c:if test="${userReviewData.review.isVerifiedPurchase == 1}">
                  <div class="mt-2">
                    <span class="badge bg-success">
                      <i class="fa fa-check-circle"></i> Đã mua hàng
                    </span>
                  </div>
                </c:if>
              </div>
            </c:when>
            
            <%-- Người dùng đã đăng nhập, đã mua sản phẩm nhưng chưa đánh giá --%>
            <c:otherwise>
              <!-- FORM ĐĂNG REVIEW MỚI -->
              <form action="${pageContext.request.contextPath}/submit-review" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="submit">
                <input type="hidden" name="productId" value="${product.productId}">
                
                <div class="mb-3">
                  <label for="rating" class="form-label">Chọn số sao:</label>
                  <select id="rating" name="rating" class="form-select" required>
                    <option value="5">5 sao</option>
                    <option value="4">4 sao</option>
                    <option value="3">3 sao</option>
                    <option value="2">2 sao</option>
                    <option value="1">1 sao</option>
                  </select>
                </div>

                <div class="mb-3">
                  <label for="comment" class="form-label">Nhận xét:</label>
                  <textarea id="comment" name="comment" class="form-control" rows="4" required></textarea>
                </div>

                <div class="mb-3">
                  <label for="reviewImages" class="form-label">Tải lên ảnh (tùy chọn):</label>
                  <input type="file" id="reviewImages" name="reviewImages" class="form-control" multiple accept="image/*">
                </div>

                <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
              </form>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
 
  </div>

  <!-- ====== MODAL ẢNH PHÓNG TO ====== -->
  <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="imageModalLabel">Ảnh đánh giá</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body text-center">
          <img id="modalImage" src="" alt="Review Image" class="img-fluid">
        </div>
      </div>
    </div>
  </div>
</div>

<!-- ====== STYLE ====== -->
<style>

/* ===== VARIABLES ===== */
:root{
  --primary:#0060df;
  --primary-dark:#0046a2;
  --secondary:#ffb400;
  --gray-100:#f8f9fa;
  --gray-200:#e9ecef;
  --gray-300:#dee2e6;
  --gray-600:#6c757d;
  --text-dark:#343a40;
  --radius-sm:8px;
  --radius-md:12px;
  --shadow-sm:0 4px 12px rgba(0,0,0,.08);
  --shadow-md:0 6px 16px rgba(0,0,0,.12);
}

/* ===== WRAPPER BLOCKS ===== */
.rating-summary-block {
  margin-bottom: 30px;
}

.rating-info,
.reviews-section,
.user-review-section{
  background:#fff;
  border-radius:var(--radius-md);
  padding:26px 28px;
  margin-bottom:30px;
  box-shadow:var(--shadow-sm);
  transition:box-shadow .25s ease,transform .25s;
  height: 100%;
}

.rating-info:hover,
.reviews-section:hover,
.user-review-section:hover{
  transform:translateY(-2px);
  box-shadow:var(--shadow-md);
}

/* ===== HORIZONTAL RATING SUMMARY ===== */
.review-tab .container {
  max-width: 100%;
  padding: 0;
}

.review-tab .row {
  margin: 0 -15px;
  display: flex;
  align-items: stretch;
}

.review-tab .col-md-5,
.review-tab .col-md-7 {
  padding: 0 15px;
  display: flex;
  flex-direction: column;
}

/* ===== FULL-WIDTH RATING ===== */
.rating-info{
  display:flex;
  align-items:center;
  justify-content:space-between;
  flex-wrap:wrap;
  width:100%;
}
.rating-info .index{font-size:28px;font-weight:700;color:var(--text-dark);margin-bottom:6px;margin-right:40px;}
.rating-info .index .rating{color:var(--secondary);font-size:34px;}

/* ===== HIDE "XEM TẤT CẢ" TEXT ===== */
.rating-info .see-all {
  display: none;
}

.rating-info .options{display:flex;flex-wrap:wrap;gap:18px 28px;list-style:none;padding:0;margin:22px 0 0;}
.rating-info .options li{margin:0;}

/* Detail for each star line */
.detail-for{display:flex;align-items:center;font-size:15px;color:var(--gray-600);}
.option-name{min-width:72px;font-weight:500;}
.progres{flex:1;margin:0 14px;}
.line-100percent{width:100%;height:8px;background:var(--gray-200);border-radius:4px;overflow:hidden;}
.percent{display:block;height:100%;background:linear-gradient(90deg,var(--secondary),#ff8c00);transition:width .5s ease;border-radius:4px;}
.number{min-width:26px;text-align:right;font-weight:600;}

/* ===== REVIEW ITEM ===== */
.review-item{background:#fff;border-radius:var(--radius-sm);padding:22px;box-shadow:var(--shadow-sm);transition:box-shadow .25s,transform .25s;}
.review-item:hover{transform:translateY(-2px);box-shadow:var(--shadow-md);}
.reviewer-name{font-size:18px;font-weight:700;color:var(--primary);}
.rating-text{font-size:14px;color:var(--gray-600);margin-left:6px;}
.review-content{font-size:16px;color:var(--text-dark);line-height:1.55;margin-top:6px;}
.review-date small{color:var(--gray-600);}

.fa-star,.fa-star-o{font-size:18px;margin-right:2px;}
.text-warning{color:var(--secondary)!important;}

/* Images */
.review-images img{border-radius:var(--radius-sm);border:1px solid var(--gray-300);max-width:110px;max-height:110px;object-fit:cover;margin-right:8px;margin-bottom:8px;transition:transform .25s,box-shadow .25s;}
.review-images img:hover{transform:scale(1.05);box-shadow:0 4px 16px rgba(0,0,0,.15);}

/* Scrollable list */
.reviews-scroll{max-height:550px;overflow-y:auto;padding-right:6px;}
.reviews-scroll::-webkit-scrollbar{width:6px;}
.reviews-scroll::-webkit-scrollbar-thumb{background:var(--gray-300);border-radius:4px;}

/* Badge */
.badge.bg-success{background:#28a745;padding:6px 12px;font-size:13px;border-radius:50rem;}

/* Form */
.user-review-section h4{font-size:22px;font-weight:700;color:var(--primary);margin-bottom:22px;}
.form-label{font-weight:500;color:var(--text-dark);margin-bottom:6px;}
.form-control,.form-select{border-radius:var(--radius-sm);border:1px solid var(--gray-300);padding:10px 12px;transition:border-color .25s,box-shadow .25s;}
.form-control:focus,.form-select:focus{border-color:var(--primary);box-shadow:0 0 0 3px rgba(0,96,223,.15);}
.btn-primary{background:var(--primary);border:none;border-radius:var(--radius-sm);padding:12px 24px;font-weight:600;transition:background .25s;}
.btn-primary:hover{background:var(--primary-dark);}

/* Modal image */
#modalImage{max-height:80vh;border-radius:var(--radius-sm);}

/* ===== RESPONSIVE ===== */
@media (max-width:768px){
  .rating-info .index{margin-bottom:12px;}
  .rating-info{justify-content:flex-start;}
  .rating-info,.reviews-section,.user-review-section{padding:22px;}
  .review-images img{max-width:90px;max-height:90px;}
  
  /* Responsive cho horizontal layout */
  .rating-summary-wrapper {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
}
</style>
                        
                        <!-- New Shipping & Stock Tab -->
                        <div id="tab_6th" class="tab-contain shipping-stock-tab">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="card mb-4">
                                        <div class="card-header bg-light">
                                            <h4 class="mb-0"><i class="fa fa-truck"></i> Thông tin vận chuyển</h4>
                                        </div>
                                        <div class="card-body">
                                            <ul class="attribute-list">
                                                <li>
                                                    <span class="feature-label">Thời gian giao hàng:</span>
                                                    <span class="feature-value">3-5 ngày làm việc</span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">Phương thức vận chuyển:</span>
                                                    <span class="feature-value">Giao hàng tiêu chuẩn</span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">Đóng gói:</span>
                                                    <span class="feature-value">Đóng gói đặc biệt cho cá cảnh</span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">Quốc tế:</span>
                                                    <span class="feature-value">Áp dụng cho một số quốc gia</span>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="card mb-4">
                                        <div class="card-header bg-light">
                                            <h4 class="mb-0"><i class="fa fa-warehouse"></i> Thông tin kho hàng</h4>
                                        </div>
                                        <div class="card-body">
                                            <div class="stock-info-detailed">
                                                <c:choose>
                                                    <c:when test="${product.quantity > 10}">
                                                        <div class="alert alert-success" role="alert">
                                                            <i class="fa fa-check-circle"></i> <strong>Còn hàng</strong>
                                                            <p class="mb-0">Hiện có ${product.quantity} sản phẩm sẵn sàng để giao hàng ngay.</p>
                                                        </div>
                                                    </c:when>
                                                    <c:when test="${product.quantity > 0}">
                                                        <div class="alert alert-warning" role="alert">
                                                            <i class="fa fa-exclamation-circle"></i> <strong>Sắp hết hàng</strong>
                                                            <p class="mb-0">Chỉ còn ${product.quantity} sản phẩm! Hãy đặt hàng sớm.</p>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="alert alert-danger" role="alert">
                                                            <i class="fa fa-times-circle"></i> <strong>Hết hàng</strong>
                                                            <p class="mb-0">Sản phẩm này hiện không có sẵn. Vui lòng quay lại sau.</p>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                                
                                                <div class="stock-details mt-3">
                                                    <h5>Thông tin nhập hàng</h5>
                                                    <p>Chúng tôi thường xuyên nhập các loại cá cảnh phổ biến. Nếu sản phẩm này đang hết hàng, vui lòng quay lại sau khoảng 7-10 ngày hoặc đăng ký nhận thông báo khi có hàng.</p>
                                                    
                                                    <h5 class="mt-3">Tình trạng</h5>
                                                    <p>Sản phẩm này ${product.status == 'active' ? 'đang được kinh doanh và có sẵn để mua' : 'hiện không hoạt động'}.</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- related products -->
<div class="product-related-box single-layout">
    <div class="biolife-title-box lg-margin-bottom-26px-im">
        <span class="biolife-icon icon-organic"></span>
        <span class="subtitle">Những sản phẩm tốt nhất dành cho bạn</span>
        <h3 class="main-title">Sản phẩm liên quan</h3>
    </div>
    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":0,"slidesToShow":5, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20 }},{"breakpoint":768, "settings":{ "slidesToShow": 2, "slidesMargin":10}}]}'>
        <c:if test="${not empty uniqueNameProductSameCategory}">
            <c:forEach var="relatedProduct" items="${uniqueNameProductSameCategory}">
                <c:if test="${relatedProduct.productId != product.productId}">
                    <li class="product-item">
                        <div class="contain-product layout-default">
                            <div class="product-thumb">
    <a href="product-detail?id=${relatedProduct.productId}" class="link-to-product">
        <c:choose>
            <c:when test="${not empty uniqueNameProductSameCategoryImages[relatedProduct.productId]}">
                <img src="${uniqueNameProductSameCategoryImages[relatedProduct.productId]}" alt="${relatedProduct.name}" width="270" height="270" class="product-thumnail">
            </c:when>
            <c:otherwise>
                <img src="assets/images/products/p-13.jpg" alt="${relatedProduct.name}" width="270" height="270" class="product-thumnail">
            </c:otherwise>
        </c:choose>
    </a>
</div>
                            <div class="info">
                                <b class="categories">${category.name}</b>
                                <h4 class="product-title"><a href="product-detail?id=${relatedProduct.productId}" class="pr-name">${relatedProduct.name}</a></h4>
                                <div class="product-rating">
                                    <c:set var="relatedProductRating" value="${relatedProductRatings[relatedProduct.productId]}" />
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fa ${i <= relatedProductRating ? 'fa-star text-warning' : 'fa-star-o text-muted'}" style="font-size: 14px;"></i>
                                    </c:forEach>
                                </div>
                                <div class="price">
                                    <c:if test="${relatedProduct.salePrice.doubleValue() > 0}">
                                        <ins><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${relatedProduct.salePrice}" pattern="#,##0"/></span></ins>
                                        <del><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${relatedProduct.price}" pattern="#,##0"/></span></del>
                                    </c:if>
                                    <c:if test="${relatedProduct.salePrice.doubleValue() <= 0}">
                                        <ins><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${relatedProduct.price}" pattern="#,##0"/></span></ins>
                                    </c:if>
                                </div>
                                <div class="slide-down-box">
                                    <p class="message">${relatedProduct.shortDescription}</p>
                                    <div class="buttons">
                                        <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                        <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>thêm vào giỏ hàng</a>
                                        <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </c:if>
            </c:forEach>
        </c:if>
    </ul>
</div>
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
    
    <!-- Additional script to ensure tabs work properly -->
    <script>
        $(document).ready(function() {
            // Manually initialize tabs
            $('.biolife-tab-contain').biolife_tab();
            
            // Fix for tab navigation - add click handlers
            $('.tab-element a').on('click', function(e) {
                e.preventDefault();
                let tabId = $(this).attr('href');
                
                // Remove active class from all tabs
                $('.tab-element').removeClass('active');
                $('.tab-contain').removeClass('active');
                
                // Add active class to current tab
                $(this).parent().addClass('active');
                $(tabId).addClass('active');
            });
            
            // Product variant selection - set colors
            $('.color-option').each(function() {
                // Get color from data attribute
                const color = $(this).data('color');
                console.log("Setting color for button:", color);
                
                // Map Vietnamese color names to CSS colors if needed
                let cssColor = color;
                if (color === 'Đỏ' || color.toLowerCase() === 'red') cssColor = 'red';
                if (color === 'Xanh dương' || color.toLowerCase() === 'blue') cssColor = 'blue';
                if (color === 'Xanh lá' || color.toLowerCase() === 'green') cssColor = 'green';
                if (color === 'Vàng' || color.toLowerCase() === 'yellow') cssColor = 'yellow';
                if (color === 'Đen' || color.toLowerCase() === 'black') cssColor = 'black';
                if (color === 'Trắng' || color.toLowerCase() === 'white') cssColor = 'white';
                if (color === 'Cam' || color.toLowerCase() === 'orange') cssColor = 'orange';
                if (color === 'Tím' || color.toLowerCase() === 'purple') cssColor = 'purple';
                if (color === 'Hồng' || color.toLowerCase() === 'pink') cssColor = 'pink';
                
                // Set background color
                $(this).css('background-color', cssColor);
                
                // Set border color based on active state
                if ($(this).hasClass('active')) {
                    $(this).css('border', '2px solid #e73918');
                } else {
                    $(this).css('border', '2px solid #ddd');
                }
                
                // Add hover effect
                $(this).hover(
                    function() {
                        if (!$(this).hasClass('active')) {
                            $(this).css('border-color', '#e73918');
                        }
                    },
                    function() {
                        if (!$(this).hasClass('active')) {
                            $(this).css('border-color', '#ddd');
                        }
                    }
                );
            });
            
            // Size options
            $('.size-option').each(function() {
                // Set border and text color based on active state
                if ($(this).hasClass('active')) {
                    $(this).css({
                        'border-color': '#e73918',
                        'color': '#e73918',
                        'font-weight': 'bold'
                    });
                } else {
                    $(this).css({
                        'border': '1px solid #ddd',
                        'color': '#333',
                        'font-weight': 'normal'
                    });
                }
                
                // Add hover effect
                $(this).hover(
                    function() {
                        if (!$(this).hasClass('active')) {
                            $(this).css({
                                'border-color': '#e73918',
                                'color': '#e73918'
                            });
                        }
                    },
                    function() {
                        if (!$(this).hasClass('active')) {
                            $(this).css({
                                'border-color': '#ddd',
                                'color': '#333'
                            });
                        }
                    }
                );
            });
        });
        
        // Script for image modal
        function showImageModal(imageUrl){
          document.getElementById('modalImage').src = imageUrl;
          const imageModal = new bootstrap.Modal(document.getElementById('imageModal'));
          imageModal.show();
        }

        /* Script for Cart Functions */
        function addToCart(event, productId) {
            if (event) event.preventDefault();
            
            // Lấy số lượng từ input
            var quantityInput = document.querySelector('.qty-input input[name="qty"]');
            var quantity = quantityInput ? parseInt(quantityInput.value) : 1;

            var formData = new FormData();
            formData.append('action', 'add');
            formData.append('productId', productId);
            formData.append('quantity', quantity);

            fetch('cartClient', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showMessage(data.message, 'success');
                    updateCartCount(data.itemCount);
                } else {
                    showMessage(data.message, 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showMessage('Có lỗi xảy ra khi thêm sản phẩm!', 'error');
            });
        }

        function showMessage(message, type) {
            // Create message element
            var messageDiv = document.createElement('div');
            messageDiv.className = 'alert alert-' + type;
            messageDiv.style.cssText = 'position: fixed; top: 20px; right: 20px; z-index: 9999; padding: 15px; border-radius: 5px; color: white; font-weight: bold;';
            
            if (type === 'success') {
                messageDiv.style.backgroundColor = '#28a745';
            } else {
                messageDiv.style.backgroundColor = '#dc3545';
            }
            
            messageDiv.textContent = message;
            document.body.appendChild(messageDiv);
            
            // Remove message after 3 seconds
            setTimeout(function() {
                document.body.removeChild(messageDiv);
            }, 3000);
        }

        function updateCartCount(count) {
            // Update cart counter if you have one
            var cartCounter = document.querySelector('.cart-counter');
            if (cartCounter) {
                cartCounter.textContent = count;
            }
        }
    </script>
</body>

</html>
