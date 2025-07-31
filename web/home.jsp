<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html class="no-js" lang="en">    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <link rel="stylesheet" href="assets/css/main-color04.css">
        <link rel="stylesheet" href="assets/css/product-gridd.css">

        <!-- Custom styles for user display -->
        <style>
        /* Badges for bestseller products */
        .badges {
            position: absolute;
            top: 10px;
            right: 10px;
            z-index: 10;
        }
        .sale-badge {
            background-color: #e73918;
            color: white;
            padding: 5px 8px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
            display: inline-block;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
            .user-greeting {
                font-size: 14px !important;
                line-height: 1.5 !important;
            }

            .user-greeting strong {
                font-weight: 600 !important;
                color: #fff !important;
            }

            .user-info {
                display: inline-flex !important;
                align-items: center !important;
            }

            .header-top .horizontal-menu li a,
            .header-top .horizontal-menu li span {
                color: black !important;

            }

            .login-link {
                transition: all 0.3s ease !important;
            }

            .login-link:hover {
                color: #7fad39 !important;
            }

            /* Ensure proper font rendering */
            * {
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
            }

            /* Mobile user info styling */
            .user-mobile-info {
                border-left: 3px solid #7fad39;
            }

            /* Fix Vietnamese characters display */


            /* Responsive adjustments */
            @media (max-width: 768px) {
                .user-greeting {
                    font-size: 12px !important;
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

            <!-- Page Contain -->
            <div class="page-contain">

                <!-- Main content -->
                <div id="main-content" class="main-content">

                    <!--Block 01: Vertical Menu And Main Slide-->
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-3 col-md-4 hidden-sm hidden-xs">
                                <div class="biolife-vertical-menu none-box-shadow  ">
                                    <div class="vertical-menu vertical-category-block always ">
                                        <div class="block-title">
                                            <span class="menu-icon">
                                                <span class="line-1"></span>
                                                <span class="line-2"></span>
                                                <span class="line-3"></span>
                                            </span>
                                            <span class="menu-title">All departments</span>

                                        </div>
                                        <div class="wrap-menu">
                                            <ul class="menu clone-main-menu">
                                                <li class="menu-item menu-item-has-children has-child">
                                                    <a href="CategoryServlet?categoryId=1&isParent=true" class="menu-name" data-title="Cá cảnh"><i class="biolife-icon icon-fish"></i>Cá cảnh</a>
                                                    <ul class="sub-menu">
                                                        <li class="menu-item"><a href="CategoryServlet?categoryId=3">Cá nước ngọt</a></li>
                                                        <li class="menu-item"><a href="CategoryServlet?categoryId=4">Cá nước lợ</a></li>
                                                        <li class="menu-item"><a href="CategoryServlet?categoryId=5">Cá nước mặn</a></li>
                                                    </ul>
                                                </li>
                                                <li class="menu-item menu-item-has-children has-child">
                                                    <a href="CategoryServlet?categoryId=2&isParent=true" class="menu-name" data-title="Thiết bị và thực phẩm"><i class="biolife-icon icon-honey"></i>Thiết bị và thực phẩm</a>
                                                    <ul class="sub-menu">
                                                        <li class="menu-item"><a href="CategoryServlet?categoryId=7">Thiết bị hồ cá</a></li>
                                                        <li class="menu-item"><a href="CategoryServlet?categoryId=6">Thức ăn cho cá</a></li>
                                                        <li class="menu-item"><a href="CategoryServlet?categoryId=8">Thuốc cho cá</a></li>
                                                    </ul>
                                                </li>
                                                <li class="menu-item menu-item-has-children has-child">
                                                    <a href="ai-agent.jsp" class="menu-name" data-title="Cá cảnh"><i class="biolife-icon icon-fish"></i>Tư Vấn Bởi AI</a>
                                                    
                                                </li>
                                                
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-9 col-md-8 col-xs-12">
                                <div class="main-slide block-slider nav-change type02">
                                    <ul class="biolife-carousel" data-slick='{"arrows": true, "dots": false, "slidesMargin": 0, "slidesToShow": 1, "infinite": true, "speed": 800}' >
                                        <li>
                                            <div class="slide-contain slider-opt04__layout01">
                                                <div class="media"></div>
                                                <div class="text-content">
                                                    <i class="first-line">Pomegranate</i>
                                                    <h3 class="second-line">Fresh Juice 100% Organic</h3>
                                                    <p class="third-line">A blend of freshly squeezed green apple & fruits</p>
                                                    <p class="buttons">
                                                        <a href="#" class="btn btn-bold">Shop now</a>
                                                        <a href="#" class="btn btn-thin">View lookbook</a>
                                                    </p>
                                                </div>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="slide-contain slider-opt04__layout01">
                                                <div class="media"></div>
                                                <div class="text-content">
                                                    <i class="first-line">Pomegranate</i>
                                                    <h3 class="second-line">Fresh Juice 100% Organic</h3>
                                                    <p class="third-line">A blend of freshly squeezed green apple & fruits</p>
                                                    <p class="buttons">
                                                        <a href="#" class="btn btn-bold">Shop now</a>
                                                        <a href="#" class="btn btn-thin">View lookbook</a>
                                                    </p>
                                                </div>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="slide-contain slider-opt04__layout01">
                                                <div class="media"></div>
                                                <div class="text-content">
                                                    <i class="first-line">Pomegranate</i>
                                                    <h3 class="second-line">Fresh Juice 100% Organic</h3>
                                                    <p class="third-line">A blend of freshly squeezed green apple & fruits</p>
                                                    <p class="buttons">
                                                        <a href="#" class="btn btn-bold">Shop now</a>
                                                        <a href="#" class="btn btn-thin">View lookbook</a>
                                                    </p>
                                                </div>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="slide-contain slider-opt04__layout01">
                                                <div class="media"></div>
                                                <div class="text-content">
                                                    <i class="first-line">Pomegranate</i>
                                                    <h3 class="second-line">Fresh Juice 100% Organic</h3>
                                                    <p class="third-line">A blend of freshly squeezed green apple & fruits</p>
                                                    <p class="buttons">
                                                        <a href="#" class="btn btn-bold">Shop now</a>
                                                        <a href="#" class="btn btn-thin">View lookbook</a>
                                                    </p>
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--Block 02: Banners-->
                    <div class="banner-block sm-margin-bottom-76px xs-margin-top-80px sm-margin-top-60px">
                        <div class="container">
                            <ul class="biolife-carousel nav-center-bold nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":30,"slidesToShow":3, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 3}},{"breakpoint":992, "settings":{ "slidesToShow": 2}},{"breakpoint":768, "settings":{ "slidesToShow": 2}}, {"breakpoint":500, "settings":{ "slidesToShow": 1}}]}'>
                                <li>
                                    <div class="biolife-banner style-02 biolife-banner__style-02">
                                        <div class="banner-contain">
                                            <div class="media">
                                                <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style02-child.png" width="231" height="208" alt=""></a>
                                            </div>
                                            <div class="text-content">
                                                <span class="text1">Sumer Fruit</span>
                                                <b class="text2">100% Pure Natural Fruit Juice</b>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <div class="biolife-banner style-03 biolife-banner__style-03">
                                        <div class="banner-contain">
                                            <div class="media">
                                                <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style03-child.png" width="218" height="205" alt=""></a>
                                            </div>
                                            <div class="text-content">
                                                <span class="text1">California</span>
                                                <b class="text2">Fresh Fruit</b>
                                                <span class="text3">Association</span>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <div class="biolife-banner style-04 biolife-banner__style-04">
                                        <div class="banner-contain">
                                            <div class="media">
                                                <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style04-child.png" width="160" height="206" alt=""></a>
                                            </div>
                                            <div class="text-content">
                                                <span class="text1">Naturally fresh taste</span>
                                                <p class="text2">With <span>25% Off</span> All Teas</p>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <div class="biolife-banner style-02 biolife-banner__style-02">
                                        <div class="banner-contain">
                                            <div class="media">
                                                <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style02-child.png" width="231" height="208" alt=""></a>
                                            </div>
                                            <div class="text-content">
                                                <span class="text1">Sumer Fruit</span>
                                                <b class="text2">100% Pure Natural Fruit Juice</b>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <div class="biolife-banner style-03 biolife-banner__style-03">
                                        <div class="banner-contain">
                                            <div class="media">
                                                <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style03-child.png" width="218" height="205" alt=""></a>
                                            </div>
                                            <div class="text-content">
                                                <span class="text1">California</span>
                                                <b class="text2">Fresh Fruit</b>
                                                <span class="text3">Association</span>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li>
                                    <div class="biolife-banner style-04 biolife-banner__style-04">
                                        <div class="banner-contain">
                                            <div class="media">
                                                <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style04-child.png" width="135" height="206" alt=""></a>
                                            </div>
                                            <div class="text-content">
                                                <span class="text1">Naturally fresh taste</span>
                                                <p class="text2">With <span>25% Off</span> All Teas</p>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <!--Block 03: Categories-->
                    <div class="wrap-category xs-margin-top-80px sm-margin-top-50px">
                        <div class="container">                            <div class="biolife-title-box style-02 xs-margin-bottom-33px">
                                <span class="subtitle">Danh mục nổi bật 2025</span>
                                <h3 class="main-title">Danh mục sản phẩm</h3>
                                <p class="desc">Các sản phẩm cá cảnh và phụ kiện chất lượng cao được chọn lọc kỹ càng</p>
                            </div><ul class="biolife-carousel nav-center-bold nav-none-on-mobile" data-slick='{"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":30,"slidesToShow":4, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 3}},{"breakpoint":992, "settings":{ "slidesToShow": 3}},{"breakpoint":768, "settings":{ "slidesToShow": 2}}, {"breakpoint":500, "settings":{ "slidesToShow": 1}}]}'>                                <c:choose>
                                <c:when test="${not empty allProductCategories}">
                                    <c:forEach var="category" items="${allProductCategories}" varStatus="status">                                            <li>
                                            <div class="biolife-cat-box-item">
                                                <div class="cat-thumb">
                                                    <a href="CategoryServlet?categoryId=${category.categoryId}" class="cat-link">
                                                        <!-- Default category images cycling through available images -->
                                                        <c:set var="imageIndex" value="${(status.index % 4) + 1}"/>
                                                        <img src="assets/images/home-04/cat-thumb0${imageIndex}.jpg" alt="${category.name}">
                                                    </a>
                                                </div><a class="cat-info" href="CategoryServlet?categoryId=${category.categoryId}">
                                                    <h4 class="cat-name">${category.name}</h4>
                                                    <span class="cat-number">
                                                        <c:set var="productCount" value="${categoryProductCounts[category.categoryId]}"/>
                                                        (${productCount != null ? productCount : 0} sản phẩm)
                                                    </span>
                                                </a>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <!-- Hiển thị categories mặc định nếu không có dữ liệu -->
                                </c:otherwise>
                            </c:choose>
                        </ul>

                        <div class="biolife-service type01 biolife-service__type01 sm-margin-top-25px xs-margin-top-65px">
                            <ul class="services-list">
                                <li>
                                    <div class="service-inner">
                                        <span class="number">1</span>
                                        <span class="biolife-icon icon-beer"></span>
                                        <a class="srv-name" href="#">Sản phẩm được đóng gói đầy đủ</a>
                                    </div>
                                </li>
                                <li>
                                    <div class="service-inner">
                                        <span class="number">2</span>
                                        <span class="biolife-icon icon-schedule"></span>
                                        <a class="srv-name" href="#">Đặt và giao hàng nhanh chóng</a>
                                    </div>
                                </li>
                                <li>
                                    <div class="service-inner">
                                        <span class="number">3</span>
                                        <span class="biolife-icon icon-car"></span>
                                        <a class="srv-name" href="#">Miễn phí giao hàng ở thành phố Đà Nẵng</a>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!--Block 04: Product Tabs-->
                <div class="product-tab z-index-20 sm-margin-top-80px xs-margin-top-20px">
                    <div class="container">
                        <div class="biolife-title-box slim-item">
                            <span class="subtitle">Tất cả những sản phẩm tốt nhất cho bạn</span>
                            <h3 class="main-title">Sản phẩm của chúng tôi</h3>
                        </div>
                        <div class="biolife-tab biolife-tab-contain sm-margin-top-23px">
                            <div class="tab-head tab-head__sample-layout">
                                <ul class="tabs">
                                    <li class="tab-element active">
                                        <a href="#tab01_1st" class="tab-link">Nổi Bật</a>
                                    </li>
                                    <li class="tab-element" >
                                        <a href="#tab01_2nd" class="tab-link">Đánh Giá Cao</a>
                                    </li>
                                    <li class="tab-element" >
                                        <a href="#tab01_3rd" class="tab-link">Khuyến Mãi</a>
                                    </li>
                                </ul>
                            </div>
                            <div class="tab-content">                            <div id="tab01_1st" class="tab-contain active">
                                    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile eq-height-contain" data-slick='{"rows":1 ,"arrows":true,"dots":false,"infinite":true,"speed":400,"slidesMargin":10,"slidesToShow":4, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20}},{"breakpoint":768, "settings":{ "slidesToShow": 2, "rows":2, "slidesMargin":15}}]}'>
                                        <c:choose>
                                            <c:when test="${not empty homeProducts}">
                                                <c:forEach var="product" items="${homeProducts}">
                                                    <li class="product-item">
                                                        <div class="contain-product layout-default">
                                                            <div class="product-thumb">
                                                                <a href="product-detail?id=${product.productId}" class="link-to-product">                                                                
                                                                    <c:choose>
                                                                        <c:when test="${not empty productImages[product.productId]}">
                                                                            <img src="${productImages[product.productId]}" alt="${product.name}" width="270" height="270" class="product-thumnail">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img src="assets/images/products/p-01.jpg" alt="No Image" width="270" height="270" class="product-thumnail">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </a>
                                                                <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                            </div>
                                                            <div class="info">
                                                                <c:choose>
                                                                    <c:when test="${not empty productCategories[product.productId]}">
                                                                        <b class="categories">${productCategories[product.productId].name}</b>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <b class="categories">General</b>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                                <div class="price">
                                                                    <c:choose>
                                                                        <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                            <ins><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0.00"/></span></ins>
                                                                            <del><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></del>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <ins><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></ins>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                                <div class="slide-down-box">
                                                                    <p class="message">All products are carefully selected to ensure quality.</p>
                                                                    <div class="buttons">
                                                                        <a href="#" class="btn wishlist-btn" onclick="addToWishlist(event, '${product.productId}')"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                        <a href="#" class="btn add-to-cart-btn" data-product-id="${product.productId}" onclick="addToCart(event, '${product.productId}')"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                                                        <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Default products when no products from database -->
                                                <li class="product-item">
                                                    <div class="contain-product layout-default">
                                                        <div class="product-thumb">
                                                            <a href="#" class="link-to-product">
                                                                <img src="assets/images/products/p-05.jpg" alt="Sample Product" width="270" height="270" class="product-thumnail">
                                                            </a>
                                                            <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                        </div>
                                                        <div class="info">
                                                            <b class="categories">Sample Category</b>
                                                            <h4 class="product-title"><a href="#" class="pr-name">Sample Product</a></h4>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">$</span>85.00</span></ins>
                                                            </div>
                                                            <div class="slide-down-box">
                                                                <p class="message">All products are carefully selected to ensure quality.</p>
                                                                <div class="buttons">
                                                                    <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                    <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                                                    <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                                <div id="tab01_2nd" class="tab-contain ">
                                    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile eq-height-contain" data-slick='{"rows":1 ,"arrows":true,"dots":false,"infinite":true,"speed":400,"slidesMargin":10,"slidesToShow":4, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20}},{"breakpoint":768, "settings":{ "slidesToShow": 2, "rows":2, "slidesMargin":15}}]}'>
                                        <c:choose>
                                            <c:when test="${not empty topRatedProducts}">
                                                <c:forEach var="product" items="${topRatedProducts}">
                                                    <li class="product-item">
                                                        <div class="contain-product layout-default">
                                                            <div class="product-thumb">
                                                                <a href="product-detail?id=${product.productId}" class="link-to-product">
                                                                    <c:choose>
                                                                        <c:when test="${not empty topRatedProductImages[product.productId]}">
                                                                            <img src="${topRatedProductImages[product.productId]}" alt="${product.name}" width="270" height="270" class="product-thumnail">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img src="assets/images/products/p-01.jpg" alt="No Image" width="270" height="270" class="product-thumnail">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </a>
                                                                <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                            </div>
                                                            <div class="info">
                                                                <c:choose>
                                                                    <c:when test="${not empty topRatedProductCategories[product.productId]}">
                                                                        <b class="categories">${topRatedProductCategories[product.productId].name}</b>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <b class="categories">General</b>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                                <div class="price">
                                                                    <c:choose>
                                                                        <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                            <ins><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0.00"/></span></ins>
                                                                            <del><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></del>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <ins><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></ins>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                                <div class="slide-down-box">
                                                                    <p class="message">All products are carefully selected to ensure quality.</p>
                                                                    <div class="buttons">
                                                                        <a href="#" class="btn wishlist-btn" onclick="addToWishlist(event, '${product.productId}')"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                        <a href="#" class="btn add-to-cart-btn" data-product-id="${product.productId}" onclick="addToCart(event, '${product.productId}')"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                                                        <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Default message when no top rated products -->
                                                <li class="product-item">
                                                    <div class="contain-product layout-default">
                                                        <div class="product-thumb">
                                                            <a href="#" class="link-to-product">
                                                                <img src="assets/images/products/p-01.jpg" alt="No Products" width="270" height="270" class="product-thumnail">
                                                            </a>
                                                            <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                        </div>
                                                        <div class="info">
                                                            <b class="categories">General</b>
                                                            <h4 class="product-title"><a href="#" class="pr-name">No Products Available</a></h4>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">$</span>0.00</span></ins>
                                                            </div>
                                                            <div class="slide-down-box">
                                                                <p class="message">Check back later for amazing products!</p>
                                                                <div class="buttons">
                                                                    <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                    <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                                                    <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                                <div id="tab01_3rd" class="tab-contain ">
                                    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile eq-height-contain" data-slick='{"rows":1 ,"arrows":true,"dots":false,"infinite":true,"speed":400,"slidesMargin":10,"slidesToShow":4, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20}},{"breakpoint":768, "settings":{ "slidesToShow": 2, "rows":2, "slidesMargin":15}}]}'>
                                        <c:choose>
                                            <c:when test="${not empty onSaleProducts}">
                                                <c:forEach var="product" items="${onSaleProducts}">
                                                    <li class="product-item">
                                                        <div class="contain-product layout-default">
                                                            <div class="product-thumb">
                                                                <a href="product-detail?id=${product.productId}" class="link-to-product">
                                                                    <c:choose>
                                                                        <c:when test="${not empty onSaleProductImages[product.productId]}">
                                                                            <img src="${onSaleProductImages[product.productId]}" alt="${product.name}" width="270" height="270" class="product-thumnail">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img src="assets/images/products/p-01.jpg" alt="No Image" width="270" height="270" class="product-thumnail">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </a>
                                                                <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                            </div>
                                                            <div class="info">
                                                                <c:choose>
                                                                    <c:when test="${not empty onSaleProductCategories[product.productId]}">
                                                                        <b class="categories">${onSaleProductCategories[product.productId].name}</b>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <b class="categories">General</b>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                                <div class="price">
                                                                    <c:choose>
                                                                        <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                            <ins><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0.00"/></span></ins>
                                                                            <del><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></del>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <ins><span class="price-amount"><span class="currencySymbol">$</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></ins>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                                <div class="slide-down-box">
                                                                    <p class="message">All products are carefully selected to ensure quality.</p>
                                                                    <div class="buttons">
                                                                        <a href="#" class="btn wishlist-btn" onclick="addToWishlist(event, '${product.productId}')"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                        <a href="#" class="btn add-to-cart-btn" data-product-id="${product.productId}" onclick="addToCart(event, '${product.productId}')"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                                                        <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Default message when no sale products -->
                                                <li class="product-item">
                                                    <div class="contain-product layout-default">
                                                        <div class="product-thumb">
                                                            <a href="#" class="link-to-product">
                                                                <img src="assets/images/products/p-05.jpg" alt="No Sale Products" width="270" height="270" class="product-thumnail">
                                                            </a>
                                                            <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                        </div>
                                                        <div class="info">
                                                            <b class="categories">Sale</b>
                                                            <h4 class="product-title"><a href="#" class="pr-name">No Sale Products Available</a></h4>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">$</span>0.00</span></ins>
                                                            </div>
                                                            <div class="slide-down-box">
                                                                <p class="message">Check back later for amazing deals!</p>
                                                                <div class="buttons">
                                                                    <a href="#" class="btn wishlist-btn"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                    <a href="#" class="btn add-to-cart-btn"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>add to cart</a>
                                                                    <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--Block 05: Banner Promotion-->
                <div class="banner-promotion-04 xs-margin-top-50px sm-margin-top-49px">
                    <div class="biolife-banner promotion4 biolife-banner__promotion4">
                        <div class="container">
                            <div class="banner-contain">
                                <div class="media">
                                    <div class="img-moving position-1">
                                        <a href="#" class="banner-link"><img src="assets/images/home-04/bn_promotion-child01.png" width="800" height="600" alt="img msv"></a>
                                    </div>
                                    <div class="img-moving position-2">
                                        <img src="assets/images/home-04/bn_promotion-child02.png" width="155" height="145" alt="img msv">
                                    </div>
                                </div>
                                <div class="text-content">
                                    <b class="first-line">Special discount<br>for all fruit products</b>
                                    <div class="biolife-countdown" data-datetime="2020/01/18 00:00:00"></div>
                                    <p class="buttons">
                                        <a href="home" class="btn btn-bold green-btn">See Offer Now!</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--Block 06: Advance-Box-->
                <div class="container z-index-20 xs-margin-top-80px sm-margin-top-0">
                <div class="row">

                    <div class="col-lg-4 sm-margin-top-80px ">
                        <div class="row">
                            <div class="col-lg-12 col-md-4 col-sm-4 col-xs-12">
                                <div class="biolife-banner style-05 biolife-banner__style-05">
                                    <div class="banner-contain">
                                        <div class="media">
                                            <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style05.png" width="197" height="230" alt=""></a>
                                        </div>
                                        <div class="text-content">
                                            <b class="text1">Mid June Royal Lee cherries</b>
                                            <b class="text-pr"><span>Only:</span>£8.00</b>
                                            <a href="#" class="btn btn-shopnow">shop now</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-12 col-md-4 col-sm-4 col-xs-12 xs-margin-top-30px sm-margin-top-0 lg-margin-top-30px">
                                <div class="biolife-banner style-06 biolife-banner__style-06">
                                    <div class="banner-contain">
                                        <div class="media">
                                            <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style06.png" width="214" height="230" alt=""></a>
                                        </div>
                                        <div class="text-content">
                                            <b class="text1">California</b>
                                            <b class="text2">Peaches</b>
                                            <b class="text-pr"><span>Only:</span>£8.00</b>
                                            <a href="#" class="btn btn-shopnow">shop now</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-12 col-md-4 col-sm-4 col-xs-12 xs-margin-top-30px sm-margin-top-0 lg-margin-top-30px">
                                <div class="biolife-banner style-07 biolife-banner__style-07">
                                    <div class="banner-contain">
                                        <div class="media">
                                            <a href="#" class="bn-link"><img src="assets/images/home-04/bn_style07.png" width="204" height="230" alt=""></a>
                                        </div>
                                        <div class="text-content">
                                            <b class="text1">Grapes</b>
                                            <span class="text2">Make the plate</span>
                                            <b class="text-pr"><span>Only:</span>£18.00</b>
                                            <a href="#" class="btn btn-shopnow">shop now</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-8 sm-margin-top-84px">
                        <div class="advance-product-box">
                            <div class="biolife-title-box bold-style biolife-title-box__bold-style mobile-tiny lg-margin-bottom-26px-im">
                                <h3 class="title">Những Sản Phẩm Được Mua Nhiều Nhất</h3>
                                <p class="subtitle">Sản phẩm bán chạy nhất (trên 5 sản phẩm đã bán)</p>
                            </div>
                            <ul class="products-list biolife-carousel nav-top-right nav-main-color nav-none-on-mobile eq-height-contain" data-slick='{"rows":2 ,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":0,"slidesToShow":3, "slidesToScroll":3, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 3, "slidesToScroll":3}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesToScroll":3, "slidesMargin": 20}},{"breakpoint":768, "settings":{ "slidesToShow": 2, "slidesToScroll":2, "slidesMargin": 15}}]}'>
                                <c:choose>
                                    <c:when test="${not empty bestsellerProducts}">
                                        <c:forEach var="product" items="${bestsellerProducts}" varStatus="status" begin="0" end="9">
                                <li class="product-item">
                                    <div class="contain-product layout-default">
                                        <div class="product-thumb">
                                                        <a href="product-detail?id=${product.productId}" class="link-to-product">
                                                            <c:choose>
                                                                <c:when test="${not empty bestsellerProductImages[product.productId]}">
                                                                    <img src="${bestsellerProductImages[product.productId]}" alt="${product.name}" width="270" height="270" class="product-thumnail">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="assets/images/products/p-01.jpg" alt="No Image" width="270" height="270" class="product-thumnail">
                                                                </c:otherwise>
                                                            </c:choose>
                                            </a>
                                            <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                                        <c:if test="${product.soldQuantity > 0}">
                                                            <div class="badges">
                                                                <span class="sale-badge">${product.soldQuantity} đã bán</span>
                                        </div>
                                                        </c:if>
                                        </div>
                                        <div class="info">
                                                        <c:choose>
                                                            <c:when test="${not empty bestsellerProductCategories[product.productId]}">
                                                                <b class="categories">${bestsellerProductCategories[product.productId].name}</b>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <b class="categories">Cá cảnh</b>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                        <div class="price">
                                                            <c:choose>
                                                                <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                    <ins><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0"/></span></ins>
                                                                    <del><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${product.price}" pattern="#,##0"/></span></del>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <ins><span class="price-amount"><span class="currencySymbol">₫</span><fmt:formatNumber value="${product.price}" pattern="#,##0"/></span></ins>
                                                                </c:otherwise>
                                                            </c:choose>
                                            </div>
                                            <div class="slide-down-box">
                                                            <p class="message">Sản phẩm chất lượng cao được chọn lọc kỹ càng.</p>
                                                <div class="buttons">
                                                                <a href="#" class="btn wishlist-btn" onclick="addToWishlist(event, '${product.productId}')"><i class="fa fa-heart" aria-hidden="true"></i></a>
                                                                <a href="#" class="btn add-to-cart-btn" data-product-id="${product.productId}" onclick="addToCart(event, '${product.productId}')"><i class="fa fa-cart-arrow-down" aria-hidden="true"></i>thêm vào giỏ hàng</a>
                                                    <a href="#" class="btn compare-btn"><i class="fa fa-random" aria-hidden="true"></i></a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                <li class="product-item">
                                    <div class="contain-product layout-default">
                                        <div class="product-thumb">
                                            <a href="#" class="link-to-product">
                                                        <img src="assets/images/products/p-19.jpg" alt="Vegetables" width="270" height="270" class="product-thumnail">
                                            </a>
                                            <a class="lookup btn_call_quickview" href="#"><i class="biolife-icon icon-search"></i></a>
                                        </div>
                                        <div class="info">
                                                    <b class="categories">Cá cảnh</b>
                                                    <h4 class="product-title"><a href="#" class="pr-name">Chưa có sản phẩm bán chạy</a></h4>
                                            <div class="price ">
                                                        <ins><span class="price-amount"><span class="currencySymbol">₫</span>0</span></ins>
                                            </div>
                                            <div class="slide-down-box">
                                                        <p class="message">Chưa có sản phẩm nào bán được trên 5 sản phẩm.</p>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                    </c:otherwise>
                                </c:choose>
                            </ul>
                        </div>
                    </div>

                </div>
            </div>

                <!--Block 07: Brands-->
                <div class="brand-slide background-fafafa xs-margin-top-50px sm-margin-top-80px sm-margin-bottom-73px">
                    <div class="container">
                        <ul class="biolife-carousel nav-center-bold nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":30,"slidesToShow":4, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3}},{"breakpoint":768, "settings":{ "slidesToShow": 2}},{"breakpoint": 550, "settings":{ "slidesToShow": 1}}]}'>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-01.jpg" width="214" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-02.jpg" width="214" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-03.jpg" width="153" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-04.jpg" width="224" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-01.jpg" width="214" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-02.jpg" width="214" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-03.jpg" width="153" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                            <li>
                                <div class="biolife-brd-container">
                                    <a href="#" class="link">
                                        <figure><img src="assets/images/home-03/brd-04.jpg" width="224" height="163" alt=""></figure>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <!--Block 08: Products-->
                <div class="container">
                    <div class="row">
                        <!-- Top Rated Products -->
                        <div class="col-sm-6 col-md-4 col-xs-12">
                            <div class="advance-product-box">
                                <div class="biolife-title-box bold-style biolife-title-box__bold-style mobile-tiny sm-margin-bottom-36px">
                                    <h3 class="title">Sản phẩm đánh giá cao</h3>
                                </div>
                                <ul class="products-list vertical-layout products-list__vertical-layout">
                                    <c:choose>
                                        <c:when test="${not empty topRatedProducts}">
                                            <c:forEach var="product" items="${topRatedProducts}" begin="0" end="2">
                                                <li class="product-item">
                                                    <div class="contain-product contain-product__right-info-layout2">
                                                        <div class="product-thumb">
                                                            <a href="product-detail?id=${product.productId}" class="link-to-product">
                                                                <c:choose>
                                                                    <c:when test="${not empty topRatedProductImages[product.productId]}">
                                                                        <img src="${topRatedProductImages[product.productId]}" alt="${product.name}" width="80" height="80" class="product-thumnail">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="assets/images/no-image.jpg" alt="No Image" width="80" height="80" class="product-thumnail">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </a>
                                                        </div>
                                                        <div class="info">
                                                            <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                            <div class="price">
                                                                <c:choose>
                                                                    <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                        <ins><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0.00"/></span></ins>
                                                                        <del><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></del>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <ins><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></ins>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                            <div class="rating">
                                                                <p class="star-rating"><span class="width-80percent"></span></p>
                                                                <span class="review-count">(05 Reviews)</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="product-item">
                                                <div class="contain-product contain-product__right-info-layout2">
                                                    <div class="product-thumb">
                                                        <a href="#" class="link-to-product">
                                                            <img src="assets/images/home-04/pr-100-01.jpg" alt="Vegetables" width="80" height="80" class="product-thumnail">
                                                        </a>
                                                    </div>
                                                    <div class="info">
                                                        <h4 class="product-title"><a href="#" class="pr-name">Pumpkins Fairytale</a></h4>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                        </div>
                                                        <div class="rating">
                                                            <p class="star-rating"><span class="width-80percent"></span></p>
                                                            <span class="review-count">(05 Reviews)</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </div>
                        </div>
                        
                        <!-- Featured Products -->
                        <div class="col-sm-6 col-md-4 col-xs-12">
                            <div class="advance-product-box">
                                <div class="biolife-title-box bold-style biolife-title-box__bold-style mobile-tiny">
                                    <h3 class="title">Sản phẩm nổi bật</h3>
                                </div>
                                <ul class="products-list vertical-layout products-list__vertical-layout">
                                    <c:choose>
                                        <c:when test="${not empty homeProducts}">
                                            <c:forEach var="product" items="${homeProducts}" begin="0" end="2">
                                                <li class="product-item">
                                                    <div class="contain-product contain-product__right-info-layout2">
                                                        <div class="product-thumb">
                                                            <a href="product-detail?id=${product.productId}" class="link-to-product">
                                                                <c:choose>
                                                                    <c:when test="${not empty productImages[product.productId]}">
                                                                        <img src="${productImages[product.productId]}" alt="${product.name}" width="80" height="80" class="product-thumnail">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="assets/images/no-image.jpg" alt="No Image" width="80" height="80" class="product-thumnail">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </a>
                                                        </div>
                                                        <div class="info">
                                                            <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                            <div class="price">
                                                                <c:choose>
                                                                    <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                        <ins><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0.00"/></span></ins>
                                                                        <del><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></del>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <ins><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></ins>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                            <div class="rating">
                                                                <p class="star-rating"><span class="width-80percent"></span></p>
                                                                <span class="review-count">(05 Reviews)</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="product-item">
                                                <div class="contain-product contain-product__right-info-layout2">
                                                    <div class="product-thumb">
                                                        <a href="#" class="link-to-product">
                                                            <img src="assets/images/home-04/pr-100-04.jpg" alt="Vegetables" width="80" height="80" class="product-thumnail">
                                                        </a>
                                                    </div>
                                                    <div class="info">
                                                        <h4 class="product-title"><a href="#" class="pr-name">Pumpkins Fairytale</a></h4>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                        </div>
                                                        <div class="rating">
                                                            <p class="star-rating"><span class="width-80percent"></span></p>
                                                            <span class="review-count">(05 Reviews)</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </div>
                        </div>

                        <!-- On Sale Products -->
                        <div class="col-sm-6 col-md-4 col-xs-12 sm-margin-top-54px md-margin-top-0">
                            <div class="advance-product-box">
                                <div class="biolife-title-box bold-style biolife-title-box__bold-style mobile-tiny">
                                    <h3 class="title">On Sale Products</h3>
                                </div>
                                <ul class="products-list vertical-layout products-list__vertical-layout">
                                    <c:choose>
                                        <c:when test="${not empty onSaleProducts}">
                                            <c:forEach var="product" items="${onSaleProducts}" begin="0" end="2">
                                                <li class="product-item">
                                                    <div class="contain-product contain-product__right-info-layout2">
                                                        <div class="product-thumb">
                                                            <a href="product-detail?id=${product.productId}" class="link-to-product">
                                                                <c:choose>
                                                                    <c:when test="${not empty onSaleProductImages[product.productId]}">
                                                                        <img src="${onSaleProductImages[product.productId]}" alt="${product.name}" width="80" height="80" class="product-thumnail">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="assets/images/no-image.jpg" alt="No Image" width="80" height="80" class="product-thumnail">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </a>
                                                        </div>
                                                        <div class="info">
                                                            <h4 class="product-title"><a href="product-detail?id=${product.productId}" class="pr-name">${product.name}</a></h4>
                                                            <div class="price">
                                                                <c:choose>
                                                                    <c:when test="${not empty product.salePrice and product.salePrice gt 0}">
                                                                        <ins><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.salePrice}" pattern="#,##0.00"/></span></ins>
                                                                        <del><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></del>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <ins><span class="price-amount"><span class="currencySymbol">£</span><fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></ins>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                            <div class="rating">
                                                                <p class="star-rating"><span class="width-80percent"></span></p>
                                                                <span class="review-count">(0 Reviews)</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="product-item">
                                                <div class="contain-product contain-product__right-info-layout2">
                                                    <div class="product-thumb">
                                                        <a href="#" class="link-to-product">
                                                            <img src="assets/images/home-04/pr-100-07.jpg" alt="Vegetables" width="80" height="80" class="product-thumnail">
                                                        </a>
                                                    </div>
                                                    <div class="info">
                                                        <h4 class="product-title"><a href="#" class="pr-name">Pumpkins Fairytale</a></h4>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                        </div>
                                                        <div class="rating">
                                                            <p class="star-rating"><span class="width-80percent"></span></p>
                                                            <span class="review-count">(05 Reviews)</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <!--Block 09: Blog Posts-->
                <div class="blog-posts xs-margin-top-80px sm-margin-top-61px sm-padding-top-54px xs-padding-bottom-50px">
                    <div class="container">
                        <div class="biolife-title-box link-all">
                            <h3 class="main-title">Our Latest Articles</h3>
                            <a href="blog" class="blog-link">View All Articles</a>
                        </div>
                        <ul class="biolife-carousel nav-center xs-margin-top-33px nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":30,"slidesToShow":3, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 3}},{"breakpoint":992, "settings":{ "slidesToShow": 2}},{"breakpoint":768, "settings":{ "slidesToShow": 2}},{"breakpoint":600, "settings":{ "slidesToShow": 1}}]}'>
                            <c:choose>
                                <c:when test="${not empty headerLatestPosts}">
                                    <c:forEach var="post" items="${headerLatestPosts}" varStatus="status">
                                        <li>
                                            <div class="post-item style-bottom-info layout-02">
                                                <div class="thumbnail">
                                                    <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" class="link-to-post">
                                                        <c:choose>
                                                            <c:when test="${not empty post.featuredImage}">
                                                                <img src="${post.featuredImage}" width="370" height="270" alt="${post.title}">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="assets/images/our-blog/post-thumb-01.jpg" width="370" height="270" alt="${post.title}">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </a>
                                                    <div class="post-date">
                                                        <c:choose>
                                                            <c:when test="${not empty post.publishedAt}">
                                                                <span class="date"><fmt:formatDate value="${post.publishedAt}" pattern="dd"/></span>
                                                                <span class="month"><fmt:formatDate value="${post.publishedAt}" pattern="MMM"/></span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="date"><fmt:formatDate value="${post.createdAt}" pattern="dd"/></span>
                                                                <span class="month"><fmt:formatDate value="${post.createdAt}" pattern="MMM"/></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                                <div class="post-content">
                                                    <h4 class="post-name">
                                                        <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" class="linktopost">
                                                            <c:choose>
                                                                <c:when test="${fn:length(post.title) > 40}">
                                                                    ${fn:substring(post.title, 0, 40)}...
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${post.title}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                    </h4>
                                                    <div class="post-meta">
                                                        <a href="#" class="post-meta__item author">
                                                            <span>${post.authorName}</span>
                                                        </a>
                                                        <a href="#" class="post-meta__item btn liked-count">${post.viewCount}<span class="biolife-icon icon-comment"></span></a>
                                                        <!-- Có thể có thêm biến commentCount nếu bạn có -->
                                                        <!-- Social share giữ nguyên -->
                                                        <div class="post-meta__item post-meta__item-social-box">
                                                            <span class="tbn"><i class="fa fa-share-alt" aria-hidden="true"></i></span>
                                                            <div class="inner-content">
                                                                <ul class="socials">
                                                                    <li><a href="#" title="twitter" class="socail-btn"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                                                    <li><a href="#" title="facebook" class="socail-btn"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                                                    <li><a href="#" title="pinterest" class="socail-btn"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                                                                    <li><a href="#" title="youtube" class="socail-btn"><i class="fa fa-youtube" aria-hidden="true"></i></a></li>
                                                                    <li><a href="#" title="instagram" class="socail-btn"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <p class="excerpt">
                                                        <c:choose>
                                                            <c:when test="${not empty post.summary}">
                                                                ${post.summary}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:out value="${fn:substring(post.content, 0, 80)}..." />
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <div class="group-buttons">
                                                        <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" class="btn readmore">continue reading</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <li>
                                        <div class="post-item style-bottom-info layout-02">
                                            <div class="post-content">
                                                <h4 class="post-name">
                                                    <a href="${pageContext.request.contextPath}/blog">Chưa có bài viết</a>
                                                </h4>
                                                <span class="p-date">Thêm bài viết mới</span>
                                            </div>
                                        </div>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>

                    </div>
                </div>
            </div>










            <!-- FOOTER -->
            <jsp:include page="footer.jsp" />
            <!--Quickview Popup-->
            <div id="biolife-quickview-block" class="biolife-quickview-block">
                <div class="quickview-container">
                    <a href="#" class="btn-close-quickview" data-object="open-quickview-block"><span class="biolife-icon icon-close-menu"></span></a>
                    <div class="biolife-quickview-inner">
                        <div class="media">
                            <ul class="biolife-carousel quickview-for" data-slick='{"arrows":false,"dots":false,"slidesMargin":30,"slidesToShow":1,"slidesToScroll":1,"fade":true,"asNavFor":".quickview-nav"}'>
                                <li><img src="assets/images/details-product/detail_01.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_02.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_03.jpg" alt="" width="500" height="500"></li>
                            </ul>
                            <ul class="biolife-carousel quickview-nav" data-slick='{"arrows":true,"dots":false,"centerMode":false,"focusOnSelect":true,"slidesMargin":10,"slidesToShow":3,"slidesToScroll":1,"asNavFor":".quickview-for"}'>
                                <li><img src="assets/images/details-product/thumb_01.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_02.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_03.jpg" alt="" width="88" height="88"></li>
                            </ul>
                        </div>
                        <div class="product-attribute">
                            <h4 class="title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                            <div class="rating">
                                <p class="star-rating"><span class="width-80percent"></span></p>
                            </div>
                            <div class="price price-contain">
                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                            </div>
                            <p class="excerpt">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris vel maximus lacus. Duis ut mauris eget justo dictum tempus sed vel tellus.</p>
                            <div class="from-cart">
                                <div class="qty-input">
                                    <input type="text" name="qty12554" value="1" data-max_value="20" data-min_value="1" data-step="1">
                                    <a href="#" class="qty-btn btn-up"><i class="fa fa-caret-up" aria-hidden="true"></i></a>
                                    <a href="#" class="qty-btn btn-down"><i class="fa fa-caret-down" aria-hidden="true"></i></a>
                                </div>
                                <div class="buttons">
                                    <a href="#" class="btn add-to-cart-btn btn-bold">add to cart</a>
                                </div>
                            </div>
                            <div class="product-meta">
                                <div class="product-atts">
                                    <div class="product-atts-item">
                                        <div class="att-label">Weight:</div>
                                        <div class="att-value">600g</div>
                                    </div>
                                </div>
                                <span class="sku">SKU: N/A</span>
                                <div class="biolife-social inline add-title">
                                    <span class="fr-title">Share:</span>
                                </div>
                            </div>
                        </div>
                    </div>
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



        <!--    ==scrip cho addto cart==-->
        <script>
                                    function addToCart(event, productId) {
                                        if (event)
                                            event.preventDefault();

                                        var formData = new FormData();
                                        formData.append('action', 'add');
                                        formData.append('productId', productId);
                                        formData.append('quantity', 1);

                                        fetch('cartClient', {// Đúng url servlet!
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

                                    function addToWishlist(event, productId) {
                                        if (event)
                                            event.preventDefault();

                                        var formData = new FormData();
                                        formData.append('action', 'toggle');
                                        formData.append('productId', productId);

                                        fetch('wishlist', {
                                            method: 'POST',
                                            body: formData
                                        })
                                                .then(response => response.json())
                                                .then(data => {
                                                    if (data.success) {
                                                        showMessage(data.message, 'success');
                                                        // Update wishlist button state
                                                        var button = event.target.closest('.wishlist-btn');
                                                        if (button) {
                                                            var icon = button.querySelector('i');
                                                            if (data.added) {
                                                                icon.style.color = '#ff6b6b'; // Red color for added
                                                                button.setAttribute('title', 'Remove from wishlist');
                                                            } else {
                                                                icon.style.color = ''; // Default color
                                                                button.setAttribute('title', 'Add to wishlist');
                                                            }
                                                        }
                                                    } else {
                                                        showMessage(data.message, 'error');
                                                    }
                                                })
                                                .catch(error => {
                                                    console.error('Error:', error);
                                                    showMessage('Có lỗi xảy ra với wishlist!', 'error');
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
                                        setTimeout(function () {
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