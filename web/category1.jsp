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
    <title>Biolife - Organic Food</title>
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/animate.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/nice-select.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/slick.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main-color04.css">
    <style>
        .hero-background {
            background-image: url('${pageContext.request.contextPath}/assets/images/about-us/bn01.jpg');
        }
        .category-sidebar {
            padding: 15px;
            border: 1px solid #eee;
        }
        .category-sidebar h3 {
            font-size: 20px;
            margin-bottom: 15px;
        }
        .category-sidebar ul {
            list-style: none;
            padding: 0;
        }
        .category-sidebar ul li {
            margin: 5px 0;
        }
        .category-sidebar ul li a {
            text-decoration: none;
            color: #333;
            display: block;
        }
        .category-sidebar ul li a:hover {
            color: #7fad39;
        }
        .category-sidebar ul li.parent a {
            font-weight: bold;
        }
        .category-sidebar ul li.child a {
            padding-left: 20px;
        }
        .discount-slider .product-item {
            text-align: center;
            padding: 10px;
        }
        .discount-slider .product-item img {
            min-width: 150px;
            min-height: 150px;
            width: 150px;
            height: 150px;
            object-fit: cover;
        }
        .discount-slider .product-item .price {
            color: #7fad39;
            font-weight: bold;
        }
        .discount-slider .product-item .sale-price {
            color: #ff0000;
            text-decoration: none;
        }
        .discount-slider .product-item .original-price {
            color: #999;
            text-decoration: line-through;
            margin-right: 5px;
        }
        .product-row {
            display: flex;
            align-items: flex-start;
            border: 1px solid #eee;
            padding: 15px;
            margin-bottom: 10px;
            transition: all 0.3s ease;
        }
        .product-row:hover {
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .product-image {
            min-width: 180px;
            min-height: 180px;
            width: 180px;
            height: 180px;
            object-fit: cover;
            margin-right: 15px;
            border: 1px solid #eee;
            border-radius: 4px;
        }
        .product-info {
            flex-grow: 1;
        }
        .product-title {
            font-size: 16px;
            margin: 0 0 5px;
        }
        .product-price {
            color: #7fad39;
            font-weight: bold;
            margin: 5px 0;
        }
        .original-price, [class^="original-price-"] {
            text-decoration: line-through;
            color: #999;
            margin-left: 5px;
        }
        .sale-price, [class^="sale-price-"] {
            color: #ff0000;
            text-decoration: none;
        }
        .attribute-select {
            margin: 10px 0;
        }
        .attribute-group {
            margin-bottom: 10px;
        }
        .attribute-group label {
            font-weight: bold;
            margin-right: 10px;
            display: block;
            margin-bottom: 5px;
        }
        .attribute-button {
            background: #f0f0f0;
            border: 1px solid #ddd;
            padding: 5px 10px;
            margin: 0 5px 5px 0;
            cursor: pointer;
            border-radius: 4px;
            display: inline-block;
            transition: all 0.2s ease;
        }
        .attribute-button.selected {
            background: #7fad39;
            color: #fff;
            border-color: #7fad39;
        }
        .attribute-button:hover:not(.disabled) {
            background: #e0e0e0;
        }
        .attribute-button.disabled {
            cursor: not-allowed;
            opacity: 0.5;
            border-color: #ddd;
            background: #f5f5f5;
        }
        .add-to-cart-btn {
            background: #7fad39;
            color: #fff;
            border: none;
            padding: 6px 12px;
            cursor: pointer;
        }
        .add-to-cart-btn:hover {
            background: #689f38;
        }
        .favorite-btn {
            background: rgba(255, 255, 255, 0.7);
            border: none;
            font-size: 30px;
            color: #ff0000;
            cursor: pointer;
            position: absolute;
            top: 10px;
            right: 10px;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            transition: all 0.3s ease;
        }
        .favorite-btn:hover {
            background: rgba(255, 255, 255, 0.9);
            transform: scale(1.1);
        }
        .product-image-container {
            position: relative;
            min-width: 180px;
            width: 180px;
            margin-right: 15px;
        }
        .sort-container {
            margin: 20px 0;
            display: flex;
            justify-content: flex-end;
            align-items: center;
        }
        .sort-dropdown {
            display: inline-block;
            margin-left: 20px;
        }
        .sort-dropdown label {
            margin-right: 10px;
            font-weight: bold;
            color: #333;
        }
        .sort-dropdown select {
            padding: 10px 15px;
            border: 2px solid #ddd;
            border-radius: 4px;
            width: 220px;
            font-size: 14px;
            background: #fff;
            cursor: pointer;
            color: #333;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }
        .sort-dropdown select:focus {
            outline: none;
            border-color: #7fad39;
            box-shadow: 0 2px 8px rgba(127, 173, 57, 0.3);
        }
        .sort-dropdown select:hover {
            border-color: #7fad39;
        }
        .search-container {
            display: inline-block;
        }
        .search-container input[type="text"] {
            padding: 10px;
            border: 2px solid #ddd;
            border-radius: 4px;
            width: 200px;
            font-size: 14px;
            margin-right: 10px;
        }
        .search-container button {
            padding: 10px 15px;
            border: 2px solid #7fad39;
            border-radius: 4px;
            background: #7fad39;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
        }
        .search-container button:hover {
            background: #689f38;
            border-color: #689f38;
        }
        .no-products {
            text-align: center;
            color: #888;
            font-size: 16px;
            margin: 50px 0;
        }
        .pagination {
            margin-top: 20px;
            text-align: center;
        }
        .pagination a {
            margin: 0 5px;
            padding: 8px 12px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
        }
        .pagination a.active {
            background: #7fad39;
            color: #fff;
            border-color: #7fad39;
        }
        .pagination a:hover:not(.active) {
            background: #f0f0f0;
        }
        .product-details {
            display: flex;
            justify-content: space-between;
        }
        .product-details-column {
            width: 48%;
        }
        .image-placeholder {
            width: 180px;
            height: 180px;
            background: #f0f0f0;
            border: 2px dashed #ccc;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            border-radius: 4px;
            color: #999;
            font-size: 12px;
            text-align: center;
        }
        .debug-info {
            background: #ffe6e6;
            padding: 5px;
            margin: 5px 0;
            border: 1px solid #ffcccc;
            border-radius: 3px;
            font-size: 12px;
            color: #cc0000;
        }
        .discount-section {
            margin-bottom: 30px;
            padding: 15px;
            background: #f9f9f9;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }
        .discount-section h3 {
            color: #7fad39;
            margin-bottom: 15px;
            font-size: 22px;
            font-weight: 600;
        }
        .discount-cart-form {
            margin-top: 10px;
        }
        .discount-cart-inputs {
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .discount-quantity {
            width: 45px;
            padding: 5px;
            margin-right: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
        }
        .discount-add-btn {
            background: #7fad39;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 5px 8px;
            cursor: pointer;
            font-size: 12px;
            transition: background 0.3s;
        }
        .discount-add-btn:hover {
            background: #689f38;
        }
        .product-item {
            text-align: center;
            padding: 15px;
            border: 1px solid #eee;
            border-radius: 6px;
            background: white;
            transition: all 0.3s;
            margin: 0 5px;
        }
        .product-item:hover {
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .product-item img {
            width: 120px;
            height: 120px;
            object-fit: cover;
            margin: 0 auto 10px;
            display: block;
        }
        .product-item h4 {
            font-size: 16px;
            margin: 0 0 5px;
            height: 40px;
            overflow: hidden;
        }
        .product-item .price {
            margin: 5px 0;
            font-weight: bold;
        }
        .product-item .original-price {
            text-decoration: line-through;
            color: #999;
            margin-left: 5px;
            font-size: 14px;
        }
        .product-item .sale-price {
            color: #ff0000;
            text-decoration: none;
        }
        .cart-form-inputs {
            display: flex;
            align-items: center;
            margin-top: 10px;
        }
        .product-quantity {
            width: 60px;
            padding: 8px;
            margin-right: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
        }
        .add-to-cart-btn {
            background: #7fad39;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 8px 15px;
            cursor: pointer;
            transition: background 0.3s ease;
        }
        .add-to-cart-btn:hover {
            background: #689f38;
        }
        .cart-notification {
            position: fixed;
            top: -100px;
            right: 20px;
            background: #fff;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            border-radius: 8px;
            padding: 15px;
            transition: top 0.5s ease;
            z-index: 9999;
            width: 300px;
        }
        .cart-notification.show {
            top: 20px;
        }
        .cart-notification-content {
            display: flex;
            align-items: center;
            flex-wrap: wrap;
        }
        .cart-notification i {
            color: #7fad39;
            font-size: 24px;
            margin-right: 10px;
        }
        .cart-notification span {
            flex: 1;
            margin-right: 10px;
        }
        .view-cart-btn {
            background: #7fad39;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 10px;
            margin-right: 10px;
        }
        .view-cart-btn:hover {
            background: #689f38;
        }
        .close-notification {
            background: none;
            border: none;
            font-size: 20px;
            cursor: pointer;
            color: #666;
            padding: 0;
            margin-left: auto;
        }
    </style>
</head>
<body class="biolife-body">
    <!-- Cart Success Notification -->
    <div id="cart-notification" class="cart-notification">
        <div class="cart-notification-content">
            <i class="fa fa-check-circle"></i>
            <span>Product added to your cart!</span>
            <button class="view-cart-btn">View Cart</button>
            <button class="close-notification">&times;</button>
        </div>
    </div>

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
    
    <!-- Hero Section -->
    <div class="hero-section hero-background">
        <h1 class="page-title">
            <c:out value="${categoryId == 0 ? 'All Products' : (not empty categories && categoryId != 0 ? categories.stream().filter(c -> c.categoryId == categoryId).findFirst().orElse(null).name : 'Category')}" />
        </h1>
    </div>
    <div id="main-content" class="main-content">
        <div class="container">
            <div class="row">
                <!-- Category Sidebar -->
                <div class="col-md-3">
                    <div class="category-sidebar">
                        <h3>Departments</h3>
                        <ul>
                            <li class="parent"><a href="CategoryServlet?categoryId=0">Show All</a></li>
                            <c:forEach var="category" items="${categories}">
                                <c:choose>
                                    <c:when test="${category.parentId == null}">
                                        <li class="parent">
                                            <a href="CategoryServlet?categoryId=${category.categoryId}&isParent=true" style="font-weight: bold;"><c:out value="${category.name}"/></a>
                                            <ul>
                                                <c:forEach var="subCategory" items="${categories}">
                                                    <c:if test="${subCategory.parentId == category.categoryId}">
                                                        <li class="child">
                                                            <a href="CategoryServlet?categoryId=${subCategory.categoryId}"><c:out value="${subCategory.name}"/></a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </li>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <!-- Products Section -->
                <div class="col-md-9">
                    <!-- Discount Products Slider (only show if not searching) -->
                    <c:if test="${empty param.search and not empty discountedProducts}">
                        <div class="discount-section">
                            <h3>Special Offers</h3>
                        <div class="discount-slider">
                                    <c:forEach var="product" items="${discountedProducts}">
                                        <div class="product-item">
                                            <c:set var="mainImage" value="${discountedProductImagesMap[product.productId].stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                                            <c:choose>
                                                <c:when test="${not empty mainImage and not empty mainImage.imageUrl}">
                                                <img src="${pageContext.request.contextPath}/${mainImage.imageUrl}" 
                                                         alt="${product.name}" 
                                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';" />
                                                </c:when>
                                                <c:otherwise>
                                                <div class="image-placeholder">
                                                    No Image<br>Available
                                                </div>
                                                </c:otherwise>
                                            </c:choose>
                                        <h4>${product.name}</h4>
                                                                                        <p class="price">
                                                <c:choose>
                                                    <c:when test="${not empty product.salePrice}">
                                                        <span class="original-price" style="text-decoration: line-through; color: #999; margin-right: 5px;">£<fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                                                        <span class="sale-price" style="color: #ff0000; text-decoration: none;">£<fmt:formatNumber value="${product.salePrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        £<fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        <form action="cartClient" method="post" class="discount-cart-form">
                                            <input type="hidden" name="action" value="add"/>
                                            <input type="hidden" name="productId" value="${product.productId}"/>
                                            <input type="hidden" name="categoryId" value="${categoryId}"/>
                                            <input type="hidden" name="redirectToCart" value="false"/>
                                            <div class="discount-cart-inputs">
                                                <input type="number" name="quantity" value="1" min="1" class="discount-quantity"/>
                                                <button type="submit" class="discount-add-btn">Add to Cart</button>
                                            </div>
                                        </form>
                                        </div>
                                    </c:forEach>
                            </div>
                        </div>
                    </c:if>

                    <!-- Search and Sort Container -->
                    <div class="sort-container">
                        <div class="search-container">
                            <form action="CategoryServlet" method="get">
                                <input type="hidden" name="categoryId" value="${categoryId}"/>
                                <input type="hidden" name="page" value="1"/>
                                <input type="hidden" name="isParent" value="${param.isParent}"/>
                                <input type="hidden" name="sort" value="${param.sort}"/>
                                <input type="text" name="search" placeholder="Search products..." value="${param.search}"/>
                                <button type="submit">Search</button>
                            </form>
                        </div>
                        <div class="sort-dropdown">
                            <label for="sort-select">Sort by:</label>
                            <select id="sort-select" onchange="location = this.value;">
                                <option value="CategoryServlet?categoryId=${categoryId}&page=1&isParent=${param.isParent}&search=${param.search}" ${empty param.sort ? 'selected' : ''}>Default Sorting</option>
                                <option value="CategoryServlet?categoryId=${categoryId}&page=1&sort=price_asc&isParent=${param.isParent}&search=${param.search}" ${param.sort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                                <option value="CategoryServlet?categoryId=${categoryId}&page=1&sort=price_desc&isParent=${param.isParent}&search=${param.search}" ${param.sort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                                <option value="CategoryServlet?categoryId=${categoryId}&page=1&sort=name_asc&isParent=${param.isParent}&search=${param.search}" ${param.sort == 'name_asc' ? 'selected' : ''}>Name: A to Z</option>
                                <option value="CategoryServlet?categoryId=${categoryId}&page=1&sort=name_desc&isParent=${param.isParent}&search=${param.search}" ${param.sort == 'name_desc' ? 'selected' : ''}>Name: Z to A</option>
                            </select>
                        </div>
                    </div>

                    <!-- Product Listing -->
                    <c:choose>
                        <c:when test="${empty products}">
                            <p class="no-products">No products found in this category.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="product-list">
                                <c:forEach var="entry" items="${groupedProducts}">
                                    <c:set var="productName" value="${entry.key}"/>
                                    <c:set var="productGroup" value="${entry.value}"/>
                                    <c:set var="firstProduct" value="${productGroup[0]}"/>
                                    
                                    <div class="product-row">
                                        <c:set var="mainImage" value="${productImagesMap[firstProduct.productId].stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                                        <div class="product-image-container">
                                            <c:choose>
                                                <c:when test="${not empty mainImage and not empty mainImage.imageUrl}">
                                                    <img src="${pageContext.request.contextPath}/${mainImage.imageUrl}" 
                                                         class="product-image product-image-${fn:replace(productName, ' ', '-')}"
                                                         onerror="this.onerror=null; this.style.display='none'; this.nextElementSibling.style.display='flex';" />
                                                    <div class="image-placeholder" style="display: none;">
                                                        No Image<br>Available
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="image-placeholder">
                                                        No Image<br>Available
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <button class="favorite-btn" onclick="window.location.href='FavoriteServlet?productId=${firstProduct.productId}'">♥</button>
                                        </div>
                                        <div class="product-info">
                                            <h3 class="product-title"><c:out value="${productName}"/></h3>
                                            <p class="short-description product-short-desc-${fn:replace(productName, ' ', '-')}"><c:out value="${firstProduct.shortDescription}" default="No description available"/></p>
                                            <c:set var="productDetail" value="${productDetailsMap[firstProduct.productId]}"/>
                                            <c:if test="${not empty productDetail}">
                                                <div class="product-details product-details-${fn:replace(productName, ' ', '-')}">
                                                    <div class="product-details-column">
                                                        <p><strong>Scientific Name:</strong> <span class="scientific-name-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.scientificName}" escapeXml="true"/></span></p>
                                                        <p><strong>Common Name:</strong> <span class="common-name-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.commonName}" escapeXml="true"/></span></p>
                                                        <p><strong>Origin:</strong> <span class="origin-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.origin}" escapeXml="true"/></span></p>
                                                        <p><strong>Size:</strong> <span class="detail-size-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.size}" escapeXml="true"/></span></p>
                                                        <p><strong>Lifespan:</strong> <span class="lifespan-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.lifespan}" escapeXml="true"/></span></p>
                                                        <p><strong>Water Type:</strong> <span class="water-type-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.waterType}" escapeXml="true"/></span></p>
                                                        <p><strong>Water Temperature:</strong> <span class="water-temp-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.waterTemperature}" escapeXml="true"/></span></p>
                                                    </div>
                                                    <div class="product-details-column">
                                                        <p><strong>Water pH:</strong> <span class="water-ph-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.waterPh}" escapeXml="true"/></span></p>
                                                        <p><strong>Diet:</strong> <span class="diet-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.diet}" escapeXml="true"/></span></p>
                                                        <p><strong>Breeding Difficulty:</strong> <span class="breeding-difficulty-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.breedingDifficulty}" escapeXml="true"/></span></p>
                                                        <p><strong>Care Level:</strong> <span class="care-level-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.careLevel}" escapeXml="true"/></span></p>
                                                        <p><strong>Compatibility:</strong> <span class="compatibility-${fn:replace(productName, ' ', '-')}"><c:out value="${productDetail.compatibility}" escapeXml="true"/></span></p>
                                                        <p></p>
                                                        <p></p>
                                                    </div>
                                                </div>
                                            </c:if>
                                            <p class="product-price">
                                                <c:choose>
                                                    <c:when test="${not empty firstProduct.salePrice}">
                                                        <del class="original-price-${fn:replace(productName, ' ', '-')}" style="text-decoration: line-through; color: #999; margin-right: 5px;"><fmt:formatNumber value="${firstProduct.price}" type="currency" currencySymbol="£"/></del>
                                                        <span class="sale-price-${fn:replace(productName, ' ', '-')}" style="color: #ff0000; text-decoration: none;"><fmt:formatNumber value="${firstProduct.salePrice}" type="currency" currencySymbol="£"/></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="regular-price-${fn:replace(productName, ' ', '-')}"><fmt:formatNumber value="${firstProduct.price}" type="currency" currencySymbol="£"/></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                            
                                            <!-- Display grouped attribute options -->
                                            <c:set var="groupAttributes" value="${groupAttributesMap[productName]}"/>
                                            
                                            <c:if test="${not empty groupAttributes['Màu sắc']}">
                                                            <div class="attribute-group">
                                                    <label>Màu sắc:</label>
                                                    <c:forEach var="colorValue" items="${groupAttributes['Màu sắc']}">
                                                                        <button type="button" 
                                                                                class="attribute-button" 
                                                                data-attribute="Màu sắc" 
                                                                data-value="${colorValue}">
                                                            <c:out value="${colorValue}"/>
                                                                        </button>
                                                                </c:forEach>
                                                            </div>
                                                        </c:if>
                                            
                                            <c:if test="${not empty groupAttributes['Kích thước']}">
                                                <div class="attribute-group">
                                                    <label>Kích thước:</label>
                                                    <c:forEach var="sizeValue" items="${groupAttributes['Kích thước']}">
                                                        <button type="button" 
                                                                class="attribute-button" 
                                                                data-attribute="Kích thước" 
                                                                data-value="${sizeValue}">
                                                            <c:out value="${sizeValue}"/>
                                                        </button>
                                                    </c:forEach>
                                                </div>
                                            </c:if>
                                        </div>
                                        <div style="display: flex; flex-direction: column; align-items: flex-start;">
                                            <form action="cartClient" method="post" id="cart-form-group-${firstProduct.productId}">
                                                <input type="hidden" name="action" value="add"/>
                                                <input type="hidden" name="productId" value="${firstProduct.productId}" id="selected-product-id-${fn:replace(productName, ' ', '-')}"/>
                                                <input type="hidden" name="categoryId" value="${categoryId}"/>
                                                <input type="hidden" name="redirectToCart" value="false"/>
                                                <div class="cart-form-inputs">
                                                    <input type="number" name="quantity" value="1" min="1" class="product-quantity"/>
                                                <button type="submit" class="add-to-cart-btn">Add to Cart</button>
                                                </div>
                                            </form>
                                            <!-- Nút Xem chi tiết -->
                                            <a href="product-detail?id=${firstProduct.productId}" class="btn" style="background-color: #4CAF50; color: white; margin-top: 10px; padding: 8px 15px; text-decoration: none; border-radius: 4px; display: inline-block; text-align: center; width: 100%;">
                                                <i class="fa fa-search" style="margin-right: 5px;"></i> Xem chi tiết
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Pagination -->
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="CategoryServlet?categoryId=${categoryId}&page=${currentPage - 1}&sort=${param.sort}&isParent=${param.isParent}&search=${param.search}">Previous</a>
                                </c:if>
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <a href="CategoryServlet?categoryId=${categoryId}&page=${i}&sort=${param.sort}&isParent=${param.isParent}&search=${param.search}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages}">
                                    <a href="CategoryServlet?categoryId=${categoryId}&page=${currentPage + 1}&sort=${param.sort}&isParent=${param.isParent}&search=${param.search}">Next</a>
                                </c:if>
                            </div>
                        </c:otherwise>
                    </c:choose>
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
                            <a href="home-04.html" class="logo footer-logo"><img src="assets/images/organic-4.png" alt="biolife logo" width="135" height="36"></a>
                            <div class="footer-phone-info">
                                <i class="biolife-icon icon-head-phone"></i>
                                <p class="r-info">
                                    <span>Got Questions ?</span>
                                    <span>(700)ï¿½ 9001-1909  (900) 689 -66</span>
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
                        <div class="separator sm-margin-top-62px xs-margin-top-40px"></div>
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
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="user-mobile-info" style="margin-bottom: 10px; padding: 8px; background-color: #f8f9fa; border-radius: 4px;">
                            <span style="font-weight: 600; color: #333;">
                                <i class="fa fa-user" style="margin-right: 5px;"></i>
                                ${sessionScope.user.fullName}
                            </span>
                        </div>
                        <ul class="list">
                            <li class="list-item"><a href="logout">ÄÄng xuáº¥t</a></li>
                            <li class="list-item"><a href="#">Wishlist <span class="index">(8)</span></a></li>
                            <li class="list-item"><a href="#">Checkout</a></li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                <ul class="list">
                    <li class="list-item"><a href="login">Login/register</a></li>
                    <li class="list-item"><a href="#">Wishlist <span class="index">(8)</span></a></li>
                    <li class="list-item"><a href="#">Checkout</a></li>
                </ul>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="glb-item currency">
                <b class="title">Currency</b>
                <ul class="list">
                    <li class="list-item"><a href="#">? EUR (Euro)</a></li>
                    <li class="list-item"><a href="#">$ USD (Dollar)</a></li>
                    <li class="list-item"><a href="#">ï¿½ GBP (Pound)</a></li>
                    <li class="list-item"><a href="#">ï¿½ JPY (Yen)</a></li>
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

    <script src="${pageContext.request.contextPath}/assets/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.countdown.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.nice-select.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.nicescroll.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/slick.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/biolife.framework.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/functions.js"></script>
    <script>
        $(document).ready(function() {
            // Initialize global attributes object
            window.selectedAttributes = {};
            
            // Initialize attribute filters on page load
            $('.product-row').each(function() {
                var productName = $(this).find('.product-title').text().trim();
                var productNameId = productName.replace(/ /g, '-');
                
                // Initialize attributes object for this product
                window.selectedAttributes[productNameId] = {
                    'Màu sắc': '',
                    'Kích thước': ''
                };
                
                // Check if there are any pre-selected attributes (e.g., from URL parameters)
                var preSelectedColor = new URLSearchParams(window.location.search).get('color_' + productNameId);
                var preSelectedSize = new URLSearchParams(window.location.search).get('size_' + productNameId);
                
                if (preSelectedColor) {
                    $(this).find('.attribute-button[data-attribute="Màu sắc"][data-value="' + preSelectedColor + '"]').addClass('selected');
                    window.selectedAttributes[productNameId]['Màu sắc'] = preSelectedColor;
                    updateAvailableAttributes(productNameId, 'Màu sắc', preSelectedColor);
                }
                
                if (preSelectedSize) {
                    $(this).find('.attribute-button[data-attribute="Kích thước"][data-value="' + preSelectedSize + '"]').addClass('selected');
                    window.selectedAttributes[productNameId]['Kích thước'] = preSelectedSize;
                    updateAvailableAttributes(productNameId, 'Kích thước', preSelectedSize);
                }
                
                // Update product if attributes are pre-selected
                if (preSelectedColor || preSelectedSize) {
                    updateSelectedProduct(productNameId);
                }
            });
            // Initialize discount slider only if visible
            <c:if test="${empty param.search}">
            $('.discount-slider').slick({
                infinite: true,
                slidesToShow: 4,
                slidesToScroll: 1,
                autoplay: true,
                autoplaySpeed: 3000,
                responsive: [
                    { breakpoint: 1024, settings: { slidesToShow: 3 } },
                    { breakpoint: 600, settings: { slidesToShow: 2 } },
                    { breakpoint: 480, settings: { slidesToShow: 1 } }
                ]
            });
            </c:if>

            // Ensure sort dropdown is visible and functional
            $('#sort-select').show();
            
            // Add visual feedback when sorting
            $('#sort-select').change(function() {
                $(this).css('opacity', '0.7');
                setTimeout(function() {
                    $('#sort-select').css('opacity', '1');
                }, 100);
            });

            // Handle attribute button clicks for grouped products
            $('.attribute-button').click(function() {
                // Skip if button is disabled
                if ($(this).hasClass('disabled')) {
                    return;
                }
                
                // Get attribute type and value
                var attribute = $(this).data('attribute');
                var value = $(this).data('value');
                
                // Find the closest product group
                var productRow = $(this).closest('.product-row');
                var productName = productRow.find('.product-title').text().trim();
                var productNameId = productName.replace(/ /g, '-');
                
                // Check if this button is already selected (toggle functionality)
                var isAlreadySelected = $(this).hasClass('selected');
                
                // Deselect other buttons in the same attribute group
                productRow.find('.attribute-button[data-attribute="' + attribute + '"]').removeClass('selected');
                
                // Initialize selected attribute variables if they don't exist
                window.selectedAttributes = window.selectedAttributes || {};
                window.selectedAttributes[productNameId] = window.selectedAttributes[productNameId] || {
                    'Màu sắc': '',
                    'Kích thước': ''
                };
                
                // If it was already selected, just deselect it and clear the value
                if (isAlreadySelected) {
                    // Clear the selected attribute value
                    window.selectedAttributes[productNameId][attribute] = '';
                    
                    // Reset all attribute buttons to enabled state if we're deselecting
                    productRow.find('.attribute-button').removeClass('disabled').css('opacity', '1').prop('disabled', false);
                } else {
                    // Select the clicked button
                    $(this).addClass('selected');
                    
                    // Store the selected attribute value
                    window.selectedAttributes[productNameId][attribute] = value;
                    
                    // Update available attribute options based on the selection
                    updateAvailableAttributes(productNameId, attribute, value);
                }
                
                // Find product that matches selected attributes and update details
                updateSelectedProduct(productNameId);
            });
            
            // Function to update available attribute options
            function updateAvailableAttributes(productNameId, selectedAttributeType, selectedAttributeValue) {
                var productRow = $('.product-title:contains("' + productNameId.replace(/-/g, ' ') + '")').closest('.product-row');
                
                // Store all compatible combinations for this product
                var compatibleCombinations = [];
                
                <c:forEach var="entry" items="${groupedProducts}">
                    <c:set var="productNameId" value="${fn:replace(entry.key, ' ', '-')}"/>
                    if ('${productNameId}' === productNameId) {
                        <c:forEach var="product" items="${entry.value}">
                            var combination = {
                                productId: ${product.productId},
                                attributes: {}
                            };
                            
                            <c:set var="attributes" value="${productAttributesMap[product.productId]}"/>
                            <c:forEach var="attr" items="${attributes}">
                                <c:if test="${attributeMap[attr.attributeId].name == 'Màu sắc' || attributeMap[attr.attributeId].name == 'Kích thước'}">
                                    combination.attributes['${attributeMap[attr.attributeId].name}'] = '${attr.value}';
                                </c:if>
                            </c:forEach>
                            
                            compatibleCombinations.push(combination);
                        </c:forEach>
                    }
                </c:forEach>
                
                // Filter combinations that match the selected attribute
                var filteredCombinations = compatibleCombinations.filter(function(combo) {
                    return combo.attributes[selectedAttributeType] === selectedAttributeValue;
                });
                
                // For each attribute type (except the selected one), update available options
                var attributeTypes = ['Màu sắc', 'Kích thước'];
                attributeTypes.forEach(function(attrType) {
                    if (attrType !== selectedAttributeType) {
                        // Get all available values for this attribute type after filtering
                        var availableValues = filteredCombinations.map(function(combo) {
                            return combo.attributes[attrType];
                        }).filter(function(value, index, self) {
                            return value && self.indexOf(value) === index; // Remove duplicates and nulls
                        });
                        
                        // Update UI for this attribute type
                        productRow.find('.attribute-button[data-attribute="' + attrType + '"]').each(function() {
                            var btnValue = $(this).data('value');
                            if (availableValues.includes(btnValue)) {
                                // This value is compatible with the selected attribute
                                $(this).removeClass('disabled').css('opacity', '1').prop('disabled', false);
                            } else {
                                // This value is not compatible
                                $(this).addClass('disabled').css('opacity', '0.5').prop('disabled', true);
                                
                                // If this was selected, deselect it
                                if ($(this).hasClass('selected')) {
                                    $(this).removeClass('selected');
                                    window.selectedAttributes[productNameId][attrType] = '';
                                }
                            }
                        });
                    }
                });
            }
            
            // Function to update the selected product based on attributes
            function updateSelectedProduct(productNameId) {
                // Get selected attributes from our global object
                var selectedAttributes = window.selectedAttributes && window.selectedAttributes[productNameId] || { 'Màu sắc': '', 'Kích thước': '' };
                var selectedColor = selectedAttributes['Màu sắc'] || '';
                var selectedSize = selectedAttributes['Kích thước'] || '';
                var found = false;
                
                // If both attributes are empty, reset to default product
                if (selectedColor === '' && selectedSize === '') {
                    // Reset to first product in group
                    <c:forEach var="entry" items="${groupedProducts}">
                        <c:set var="productNameId" value="${fn:replace(entry.key, ' ', '-')}"/>
                        if ('${productNameId}' === productNameId) {
                            <c:set var="firstProduct" value="${entry.value[0]}"/>
                            // Update product ID in form
                            $('#selected-product-id-' + productNameId).val(${firstProduct.productId});
                            
                            // Get product details
                            <c:set var="productDetail" value="${productDetailsMap[firstProduct.productId]}"/>
                            <c:set var="mainImage" value="${productImagesMap[firstProduct.productId].stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                            
                            // Update image
                            <c:if test="${not empty mainImage and not empty mainImage.imageUrl}">
                                $('.product-image-' + productNameId).attr('src', '${pageContext.request.contextPath}/${mainImage.imageUrl}');
                            </c:if>
                            
                            // Update short description
                            $('.product-short-desc-' + productNameId).text('<c:out value="${firstProduct.shortDescription}" escapeXml="true"/>');
                            
                            // Update prices
                            <c:choose>
                                <c:when test="${not empty firstProduct.salePrice}">
                                    $('.original-price-' + productNameId).html('£<fmt:formatNumber value="${firstProduct.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>');
                                    $('.sale-price-' + productNameId).html('£<fmt:formatNumber value="${firstProduct.salePrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/>');
                                </c:when>
                                <c:otherwise>
                                    $('.regular-price-' + productNameId).html('£<fmt:formatNumber value="${firstProduct.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>');
                                </c:otherwise>
                            </c:choose>
                            
                            // Update product details
                            <c:if test="${not empty productDetail}">
                                $('.scientific-name-' + productNameId).text('<c:out value="${productDetail.scientificName}" escapeXml="true"/>');
                                $('.common-name-' + productNameId).text('<c:out value="${productDetail.commonName}" escapeXml="true"/>');
                                $('.origin-' + productNameId).text('<c:out value="${productDetail.origin}" escapeXml="true"/>');
                                $('.detail-size-' + productNameId).text('<c:out value="${productDetail.size}" escapeXml="true"/>');
                                $('.lifespan-' + productNameId).text('<c:out value="${productDetail.lifespan}" escapeXml="true"/>');
                                $('.water-type-' + productNameId).text('<c:out value="${productDetail.waterType}" escapeXml="true"/>');
                                $('.water-temp-' + productNameId).text('<c:out value="${productDetail.waterTemperature}" escapeXml="true"/>');
                                $('.water-ph-' + productNameId).text('<c:out value="${productDetail.waterPh}" escapeXml="true"/>');
                                $('.diet-' + productNameId).text('<c:out value="${productDetail.diet}" escapeXml="true"/>');
                                $('.breeding-difficulty-' + productNameId).text('<c:out value="${productDetail.breedingDifficulty}" escapeXml="true"/>');
                                $('.care-level-' + productNameId).text('<c:out value="${productDetail.careLevel}" escapeXml="true"/>');
                                $('.compatibility-' + productNameId).text('<c:out value="${productDetail.compatibility}" escapeXml="true"/>');
                            </c:if>
                            
                            return true;
                        }
                    </c:forEach>
                }
                
                <c:forEach var="entry" items="${groupedProducts}">
                    <c:set var="productNameId" value="${fn:replace(entry.key, ' ', '-')}"/>
                    if ('${productNameId}' === productNameId) {
                        <c:forEach var="product" items="${entry.value}">
                            var colorMatch = false;
                            var sizeMatch = false;
                            
                            <c:set var="colorValue" value=""/>
                            <c:set var="sizeValue" value=""/>
                            <c:set var="attributes" value="${productAttributesMap[product.productId]}"/>
                            
                            <c:forEach var="attr" items="${attributes}">
                                <c:if test="${attributeMap[attr.attributeId].name == 'Màu sắc'}">
                                    <c:set var="colorValue" value="${attr.value}"/>
                                </c:if>
                                <c:if test="${attributeMap[attr.attributeId].name == 'Kích thước'}">
                                    <c:set var="sizeValue" value="${attr.value}"/>
                                </c:if>
                            </c:forEach>
                            
                            // Check if this product matches the selected attributes
                            if ((selectedColor === '' || '${colorValue}' === selectedColor) && 
                                (selectedSize === '' || '${sizeValue}' === selectedSize)) {
                                colorMatch = (selectedColor === '' || '${colorValue}' === selectedColor);
                                sizeMatch = (selectedSize === '' || '${sizeValue}' === selectedSize);
                            }
                            
                            // If we have exact matches for all selected attributes
                            if ((selectedColor === '' || colorMatch) && (selectedSize === '' || sizeMatch)) {
                                // Update product ID in form
                                $('#selected-product-id-' + productNameId).val(${product.productId});
                                found = true;
                                
                                // Update product details and image if we have at least one attribute selected
                                if (selectedColor !== '' || selectedSize !== '') {
                                    // Get product details
                                    <c:set var="productDetail" value="${productDetailsMap[product.productId]}"/>
                                    <c:set var="mainImage" value="${productImagesMap[product.productId].stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                                    
                                    // Update image
                                    <c:if test="${not empty mainImage and not empty mainImage.imageUrl}">
                                        $('.product-image-' + productNameId).attr('src', '${pageContext.request.contextPath}/${mainImage.imageUrl}');
                                    </c:if>
                                    
                                    // Update short description
                                    $('.product-short-desc-' + productNameId).text('<c:out value="${product.shortDescription}" escapeXml="true"/>');
                                    
                                    // Update prices
                                    <c:choose>
                                        <c:when test="${not empty product.salePrice}">
                                            $('.original-price-' + productNameId).html('£<fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>');
                                            $('.sale-price-' + productNameId).html('£<fmt:formatNumber value="${product.salePrice}" type="number" minFractionDigits="2" maxFractionDigits="2"/>');
                                        </c:when>
                                        <c:otherwise>
                                            $('.regular-price-' + productNameId).html('£<fmt:formatNumber value="${product.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>');
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    // Update product details
                                    <c:if test="${not empty productDetail}">
                                        $('.scientific-name-' + productNameId).text('<c:out value="${productDetail.scientificName}" escapeXml="true"/>');
                                        $('.common-name-' + productNameId).text('<c:out value="${productDetail.commonName}" escapeXml="true"/>');
                                        $('.origin-' + productNameId).text('<c:out value="${productDetail.origin}" escapeXml="true"/>');
                                        $('.detail-size-' + productNameId).text('<c:out value="${productDetail.size}" escapeXml="true"/>');
                                        $('.lifespan-' + productNameId).text('<c:out value="${productDetail.lifespan}" escapeXml="true"/>');
                                        $('.water-type-' + productNameId).text('<c:out value="${productDetail.waterType}" escapeXml="true"/>');
                                        $('.water-temp-' + productNameId).text('<c:out value="${productDetail.waterTemperature}" escapeXml="true"/>');
                                        $('.water-ph-' + productNameId).text('<c:out value="${productDetail.waterPh}" escapeXml="true"/>');
                                        $('.diet-' + productNameId).text('<c:out value="${productDetail.diet}" escapeXml="true"/>');
                                        $('.breeding-difficulty-' + productNameId).text('<c:out value="${productDetail.breedingDifficulty}" escapeXml="true"/>');
                                        $('.care-level-' + productNameId).text('<c:out value="${productDetail.careLevel}" escapeXml="true"/>');
                                        $('.compatibility-' + productNameId).text('<c:out value="${productDetail.compatibility}" escapeXml="true"/>');
                                    </c:if>
                                }
                                
                                // If we have exact matches for all selected attributes, break the loop
                                if ((selectedColor === '' || '${colorValue}' === selectedColor) && 
                                    (selectedSize === '' || '${sizeValue}' === selectedSize)) {
                                    if ((selectedColor !== '' && '${colorValue}' === selectedColor) && 
                                        (selectedSize !== '' && '${sizeValue}' === selectedSize)) {
                                        // We found an exact match for both color and size
                                        return true;
                                    } else if ((selectedColor !== '' && '${colorValue}' === selectedColor && selectedSize === '') ||
                                              (selectedSize !== '' && '${sizeValue}' === selectedSize && selectedColor === '')) {
                                        // We found an exact match for the one attribute that was selected
                                        return true;
                                    }
                                }
                            }
                        </c:forEach>
                    }
                </c:forEach>
                
                return found;
            }

                        // Initialize validation for cart forms
            $('.discount-cart-form, form[id^="cart-form-group-"]').submit(function(e) {
                e.preventDefault(); // Prevent default form submission
                
                // Get quantity
                var quantity = parseInt($(this).find('input[name="quantity"]').val());
                
                // Check if quantity is valid
                if (isNaN(quantity) || quantity <= 0) {
                    alert('Please enter a valid quantity.');
                    return false;
                }
                
                // For main product forms, validate attribute selection if needed
                if ($(this).attr('id') && $(this).attr('id').startsWith('cart-form-group-')) {
                    var productNameId = $(this).closest('.product-row').find('.product-title').text().trim().replace(/ /g, '-');
                    
                    // Get selected attributes from our global object
                    var selectedAttributes = window.selectedAttributes && window.selectedAttributes[productNameId] || { 'Màu sắc': '', 'Kích thước': '' };
                    var selectedColor = selectedAttributes['Màu sắc'] || '';
                    var selectedSize = selectedAttributes['Kích thước'] || '';
                    
                    // If color options exist and none selected
                    if ($(this).closest('.product-row').find('.attribute-button[data-attribute="Màu sắc"]:not(.disabled)').length > 0 && !selectedColor) {
                        alert('Vui lòng chọn màu sắc.');
                        return false;
                    }
                    
                    // If size options exist and none selected
                    if ($(this).closest('.product-row').find('.attribute-button[data-attribute="Kích thước"]:not(.disabled)').length > 0 && !selectedSize) {
                        alert('Vui lòng chọn kích thước.');
                        return false;
                    }
                    
                    // Check if we have a valid product with the selected attributes
                    var validProduct = false;
                    <c:forEach var="entry" items="${groupedProducts}">
                        <c:set var="productNameId" value="${fn:replace(entry.key, ' ', '-')}"/>
                        if ('${productNameId}' === productNameId) {
                            <c:forEach var="product" items="${entry.value}">
                                var colorMatch = false;
                                var sizeMatch = false;
                                
                                <c:set var="attributes" value="${productAttributesMap[product.productId]}"/>
                                <c:forEach var="attr" items="${attributes}">
                                    <c:if test="${attributeMap[attr.attributeId].name == 'Màu sắc'}">
                                        if ('${attr.value}' === selectedColor || !selectedColor) {
                                            colorMatch = true;
                                        }
                                    </c:if>
                                    <c:if test="${attributeMap[attr.attributeId].name == 'Kích thước'}">
                                        if ('${attr.value}' === selectedSize || !selectedSize) {
                                            sizeMatch = true;
                                        }
                                    </c:if>
                                </c:forEach>
                                
                                if (colorMatch && sizeMatch) {
                                    validProduct = true;
                                }
                            </c:forEach>
                        }
                    </c:forEach>
                    
                    if (!validProduct) {
                        alert('The selected combination of attributes is not available.');
                        return false;
                    }
                }
                
                <c:if test="${empty sessionScope.user}">
                        alert('Please log in to add items to your cart.');
                        window.location.href = 'login.jsp';
                        return false;
                </c:if>
                
                // AJAX submission
                var form = $(this);
                $.ajax({
                    type: "POST",
                    url: form.attr('action'),
                    data: form.serialize(),
                    dataType: 'json',
                    success: function(response) {
                        if (response.success) {
                            // Show notification
                            $('#cart-notification').addClass('show');
                            setTimeout(function() {
                                $('#cart-notification').removeClass('show');
                            }, 5000);
                        } else {
                            alert(response.message);
                        }
                    },
                    error: function() {
                        alert('An error occurred while adding the product to your cart.');
                    }
                });
                
                return false;
            });

            // Handle cart notification
            $('.close-notification').click(function() {
                $('#cart-notification').removeClass('show');
            });
            
            $('.view-cart-btn').click(function() {
                window.location.href = 'cartClient';
            });
        });
    </script>
</body>
</html>