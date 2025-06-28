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
            min-width: 100px;
            min-height: 100px;
            width: 100px;
            height: 100px;
            object-fit: cover;
        }
        .discount-slider .product-item .price {
            color: #7fad39;
            font-weight: bold;
        }
        .discount-slider .product-item .sale-price {
            color: #ff0000;
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
            min-width: 100px;
            min-height: 100px;
            width: 100px;
            height: 100px;
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
        .attribute-select {
            margin: 10px 0;
        }
        .attribute-group {
            margin-bottom: 10px;
        }
        .attribute-group label {
            font-weight: bold;
            margin-right: 10px;
        }
        .attribute-button {
            background: #f0f0f0;
            border: 1px solid #ddd;
            padding: 5px 10px;
            margin: 0 5px 5px 0;
            cursor: pointer;
            border-radius: 4px;
            display: inline-block;
        }
        .attribute-button.selected {
            background: #7fad39;
            color: #fff;
            border-color: #7fad39;
        }
        .attribute-button:hover {
            background: #e0e0e0;
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
            background: none;
            border: none;
            font-size: 20px;
            color: #ff0000;
            cursor: pointer;
            margin-left: 5px;
            margin-top: 5px;
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
            width: 100px;
            height: 100px;
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
    <c:set var="tempUser" value="${sessionScope.user}" scope="request"/>
    <c:remove var="user" scope="session"/>
    <jsp:include page="header.jsp"></jsp:include>
    <c:set var="user" value="${requestScope.tempUser}" scope="session"/>

    <!-- Navigation Section -->
    <div class="container">
        <nav class="biolife-nav nav-86px">
            <ul>
                <li class="nav-item"><a href="index-2.html" class="permal-link">Home</a></li>
                <li class="nav-item"><span class="current-page">Contact</span></li>
            </ul>
        </nav>
    </div>

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
                    <!-- Discounted Products Slider -->
                    <c:if test="${empty param.search}">
                        <h3>Discounted Products</h3>
                        <div class="discount-slider">
                            <c:choose>
                                <c:when test="${empty discountedProducts}">
                                    <p>No discounted products available.</p>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="product" items="${discountedProducts}">
                                        <div class="product-item">
                                            <c:set var="mainImage" value="${discountedProductImagesMap[product.productId].stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                                            <c:choose>
                                                <c:when test="${not empty mainImage and not empty mainImage.imageUrl}">
                                                    <img src="${pageContext.request.contextPath}${mainImage.imageUrl}" 
                                                         alt="${product.name}" 
                                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';" />
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/assets/images/no-image.png" 
                                                         alt="No image available" />
                                                </c:otherwise>
                                            </c:choose>
                                            <h4 class="product-title"><c:out value="${product.name}" /></h4>
                                            <p class="short-description"><c:out value="${product.shortDescription}" default="No description" /></p>
                                            <p class="price">
                                                <span class="sale-price"><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="£"/></span>
                                                <fmt:formatNumber value="${product.salePrice}" type="currency" currencySymbol="£"/>
                                            </p>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
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
                                <c:forEach var="product" items="${products}">
                                    <div class="product-row">
                                        <c:set var="mainImage" value="${productImagesMap[product.productId].stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                                        <c:choose>
                                            <c:when test="${not empty mainImage and not empty mainImage.imageUrl}">
                                                <img src="${pageContext.request.contextPath}${mainImage.imageUrl}" 
                                                     alt="${product.name}" 
                                                     class="product-image"
                                                     onerror="this.onerror=null; this.style.display='none'; this.nextElementSibling.style.display='flex';" />
                                                <div class="image-placeholder" style="display: none;">
                                                    No Image<br>Available
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="image-placeholder">
                                                    No Image<br>Available
                                                </div>
                                                <c:if test="${param.debug == 'true'}">
                                                    <div class="debug-info">
                                                        Debug: No main image found for "${product.name}"<br>
                                                        Images count: ${fn:length(productImagesMap[product.productId])}<br>
                                                        Main image: ${mainImage}<br>
                                                        Image URL: ${mainImage != null ? mainImage.imageUrl : 'null'}
                                                    </div>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                        <button class="favorite-btn" onclick="window.location.href='FavoriteServlet?productId=${product.productId}'">♥</button>
                                        <div class="product-info">
                                            <h3 class="product-title"><c:out value="${product.name}"/></h3>
                                            <p class="short-description"><c:out value="${product.shortDescription}" default="No description available"/></p>
                                            <c:set var="productDetail" value="${productDetailsMap[product.productId]}"/>
                                            <c:if test="${not empty productDetail}">
                                                <div class="product-details">
                                                    <div class="product-details-column">
                                                        <p><strong>Scientific Name:</strong> <c:out value="${productDetail.scientificName}" default="N/A"/></p>
                                                        <p><strong>Common Name:</strong> <c:out value="${productDetail.commonName}" default="N/A"/></p>
                                                        <p><strong>Origin:</strong> <c:out value="${productDetail.origin}" default="N/A"/></p>
                                                        <p><strong>Size:</strong> <c:out value="${productDetail.size}" default="N/A"/></p>
                                                        <p><strong>Lifespan:</strong> <c:out value="${productDetail.lifespan}" default="N/A"/></p>
                                                        <p><strong>Water Type:</strong> <c:out value="${productDetail.waterType}" default="N/A"/></p>
                                                        <p><strong>Water Temperature:</strong> <c:out value="${productDetail.waterTemperature}" default="N/A"/></p>
                                                    </div>
                                                    <div class="product-details-column">
                                                        <p><strong>Water pH:</strong> <c:out value="${productDetail.waterPh}" default="N/A"/></p>
                                                        <p><strong>Diet:</strong> <c:out value="${productDetail.diet}" default="N/A"/></p>
                                                        <p><strong>Breeding Difficulty:</strong> <c:out value="${productDetail.breedingDifficulty}" default="N/A"/></p>
                                                        <p><strong>Care Level:</strong> <c:out value="${productDetail.careLevel}" default="N/A"/></p>
                                                        <p><strong>Compatibility:</strong> <c:out value="${productDetail.compatibility}" default="N/A"/></p>
                                                        <p></p>
                                                        <p></p>
                                                    </div>
                                                </div>
                                            </c:if>
                                            <p class="product-price">
                                                <c:choose>
                                                    <c:when test="${not empty product.salePrice}">
                                                        <del><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="£"/></del>
                                                        <fmt:formatNumber value="${product.salePrice}" type="currency" currencySymbol="£"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="£"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                            <c:set var="attributes" value="${productAttributesMap[product.productId]}"/>
                                            <c:if test="${not empty attributes}">
                                                <div class="attribute-select">
                                                    <!-- Group attributes by attributeName -->
                                                    <c:set var="attributeNames" value="" />
                                                    <c:forEach var="attr" items="${attributes}">
                                                        <c:if test="${!attr.isDeleted() && !fn:contains(attributeNames, attr.attributeName)}">
                                                            <c:set var="attributeNames" value="${attributeNames}${attr.attributeName}," />
                                                        </c:if>
                                                    </c:forEach>
                                                    <c:forEach var="attrName" items="${fn:split(attributeNames, ',')}">
                                                        <c:if test="${not empty attrName}">
                                                            <div class="attribute-group">
                                                                <label><c:out value="${attrName}"/>:</label>
                                                                <c:forEach var="attr" items="${attributes}">
                                                                    <c:if test="${!attr.isDeleted() && attr.attributeName == attrName}">
                                                                        <button type="button" 
                                                                                class="attribute-button" 
                                                                                data-product-id="${product.productId}" 
                                                                                data-attribute-id="${attr.attributeId}" 
                                                                                data-value-id="${attr.valueId}">
                                                                            <c:out value="${attr.value}"/>
                                                                        </button>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </div>
                                                        </c:if>
                                                    </c:forEach>
                                                </div>
                                            </c:if>
                                        </div>
                                        <div style="display: flex; align-items: center;">
                                            <form action="AddToCartServlet" method="post" id="cart-form-${product.productId}">
                                                <input type="hidden" name="productId" value="${product.productId}"/>
                                                <input type="hidden" name="categoryId" value="${categoryId}"/>
                                                <!-- Dynamic attribute value IDs -->
                                                <c:forEach var="attr" items="${attributes}" varStatus="loop">
                                                    <c:if test="${!attr.isDeleted()}">
                                                        <input type="hidden" 
                                                               name="attributeValueIds" 
                                                               class="attribute-value-${product.productId}-${attr.attributeId}" 
                                                               value="${loop.first ? attr.valueId : ''}"/>
                                                    </c:if>
                                                </c:forEach>
                                                <input type="number" name="quantity" value="1" min="1" max="${product.quantity}" style="width: 60px; margin-right: 10px;"/>
                                                <button type="submit" class="add-to-cart-btn">Add to Cart</button>
                                            </form>
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

            // Handle attribute button clicks
            $('.attribute-button').click(function() {
                var productId = $(this).data('product-id');
                var attributeId = $(this).data('attribute-id');
                var valueId = $(this).data('value-id');

                // Deselect other buttons in the same attribute group
                $(this).siblings('.attribute-button').removeClass('selected');
                // Select the clicked button
                $(this).addClass('selected');

                // Update the corresponding hidden input
                $('.attribute-value-' + productId + '-' + attributeId).val(valueId);
            });
        });
    </script>
</body>
</html>