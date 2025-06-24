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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>FishShop - Aquatic Products</title>
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu&display=swap" rel="stylesheet">
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/favicon.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/nice-select.css">
    <link rel="stylesheet" href="assets/css/slick.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color04.css">
    
    <style>
        .product-row {
            display: flex;
            align-items: center;
            border: 1px solid #eee;
            padding: 15px;
            margin-bottom: 10px;
            transition: all 0.3s ease;
        }
        .product-row:nth-child(even) {
            background-color: #f9f9f9;
        }
        .product-row:hover {
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .product-image {
            max-width: 100px;
            height: auto;
            margin-right: 15px;
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
            margin: 5px 0;
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
        .no-products {
            text-align: center;
            color: #888;
            font-size: 16px;
            margin: 50px 0;
        }
        .category-sidebar {
            border: 1px solid #eee;
            padding: 15px;
            margin-bottom: 20px;
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
            margin-bottom: 10px;
        }
        .category-sidebar ul li a {
            text-decoration: none;
            color: #333;
        }
        .category-sidebar ul li a:hover {
            color: #7fad39;
        }
        .category-sidebar ul li ul {
            padding-left: 20px;
        }
        .short-description {
            color: #666;
            font-size: 14px;
            margin: 5px 0;
        }
        .product-details {
            color: #666;
            font-size: 14px;
            margin: 5px 0;
            display: none;
        }
        .details-toggle {
            background: #007bff;
            color: #fff;
            border: none;
            padding: 6px 12px;
            cursor: pointer;
            margin-left: 10px;
        }
        .details-toggle:hover {
            background: #0056b3;
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

    <div class="page-contain">
        <div id="main-content" class="main-content">
            <div class="container">
                <div class="row">
                    <!-- Category Sidebar -->
                    <div class="col-md-3">
                        <div class="category-sidebar">
                            <h3>Categories</h3>
                            <ul>
                                <c:forEach var="category" items="${categories}">
                                    <c:if test="${category.parentId == null}">
                                        <li>
                                            <a href="CategoryServlet?categoryId=${category.categoryId}"><c:out value="${category.name}"/></a>
                                            <ul>
                                                <c:forEach var="subCategory" items="${categories}">
                                                    <c:if test="${subCategory.parentId == category.categoryId}">
                                                        <li>
                                                            <a href="CategoryServlet?categoryId=${subCategory.categoryId}"><c:out value="${subCategory.name}"/></a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>

                    <!-- Product Listing -->
                    <div class="col-md-9">
                        <h2>Products in Category: <c:out value="${products[0].categoryName}" default="Unknown Category"/></h2>
                        <c:choose>
                            <c:when test="${empty products}">
                                <p class="no-products">No products found in this category.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="product-list">
                                    <c:forEach var="product" items="${products}">
                                        <div class="product-row">
                                            <!-- Hiển thị ảnh chính -->
                                            <c:set var="mainImage" value="${product.images.stream().filter(img -> img.isMain == 1).findFirst().orElse(null)}"/>
                                            <c:choose>
                                                <c:when test="${not empty mainImage and !empty mainImage.imageUrl}">
                                                    <img src="${mainImage.imageUrl}" alt="${product.name}" class="product-image" onerror="this.src='images/betta_halfmoon_1.jpg'"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="images/betta_halfmoon_1.jpg" alt="Default Image" class="product-image"/>
                                                </c:otherwise>
                                            </c:choose>

                                            <!-- Thông tin sản phẩm -->
                                            <div class="product-info">
                                                <!-- Tên sản phẩm -->
                                                <h3 class="product-title"><c:out value="${product.name}"/></h3>

                                                <!-- Mô tả ngắn -->
                                                <p class="short-description" id="short-desc-${product.productId}">
                                                    <c:out value="${product.shortDescription}" default="No description available"/>
                                                </p>

                                                <!-- Chi tiết sản phẩm -->
                                                <c:if test="${not empty product.productDetail}">
                                                    <div class="product-details" id="details-${product.productId}">
                                                        <p><strong>Scientific Name:</strong> <c:out value="${product.productDetail.scientificName}" default="N/A"/></p>
                                                        <p><strong>Common Name:</strong> <c:out value="${product.productDetail.commonName}" default="N/A"/></p>
                                                        <p><strong>Origin:</strong> <c:out value="${product.productDetail.origin}" default="N/A"/></p>
                                                        <p><strong>Size:</strong> <c:out value="${product.productDetail.size}" default="N/A"/></p>
                                                        <p><strong>Lifespan:</strong> <c:out value="${product.productDetail.lifespan}" default="N/A"/></p>
                                                        <p><strong>Water Type:</strong> <c:out value="${product.productDetail.waterType}" default="N/A"/></p>
                                                        <p><strong>Water Temperature:</strong> <c:out value="${product.productDetail.waterTemperature}" default="N/A"/></p>
                                                        <p><strong>Water pH:</strong> <c:out value="${product.productDetail.waterPh}" default="N/A"/></p>
                                                        <p><strong>Diet:</strong> <c:out value="${product.productDetail.diet}" default="N/A"/></p>
                                                        <p><strong>Breeding Difficulty:</strong> <c:out value="${product.productDetail.breedingDifficulty}" default="N/A"/></p>
                                                        <p><strong>Care Level:</strong> <c:out value="${product.productDetail.careLevel}" default="N/A"/></p>
                                                        <p><strong>Compatibility:</strong> <c:out value="${product.productDetail.compatibility}" default="N/A"/></p>
                                                    </div>
                                                </c:if>

                                                <!-- Giá -->
                                                <p class="product-price">
                                                    <c:choose>
                                                        <c:when test="${not empty product.salePrice}">
                                                            <del><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/></del>
                                                            <fmt:formatNumber value="${product.salePrice}" type="currency" currencySymbol="$"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>

                                                <!-- Số lượng tồn kho -->
                                               <!-- 
                                               <p>Stock: <c:out value="${product.quantity}"/></p>
                                                -->
                                                <!-- Thuộc tính (ví dụ: màu sắc) -->
                                                 <!-- T
                                                <c:if test="${not empty product.attributes}">
                                                    <div class="attribute-select">
                                                        <label>Select Variant:</label>
                                                        <select name="attribute_${product.productId}" class="attribute-dropdown">
                                                            <c:forEach var="attr" items="${product.attributes}">
                                                                <c:if test="${!attr.isDeleted()}">
                                                                    <option value="${attr.valueId}">${attr.attributeName}: ${attr.value}</option>
                                                                </c:if>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </c:if>-->
                                                 
                                            </div>

                                            <!-- Nút thêm vào giỏ hàng và More/Hide -->
                                            <div style="display: flex; align-items: center;">
                                                <form action="AddToCartServlet" method="post">
                                                    <input type="hidden" name="productId" value="${product.productId}"/>
                                                    <input type="hidden" name="categoryId" value="${categoryId}"/>
                                                    <c:if test="${not empty product.attributes}">
                                                        <input type="hidden" name="attributeValueId" class="selected-attribute" value="${product.attributes[0].valueId}"/>
                                                    </c:if>
                                                    <input type="number" name="quantity" value="1" min="1" max="${product.quantity}" style="width: 60px; margin-right: 10px;"/>
                                                    <button type="submit" class="add-to-cart-btn">Add to Cart</button>
                                                </form>
                                                <c:if test="${not empty product.productDetail}">
                                                    <button class="details-toggle" data-product-id="${product.productId}" onclick="toggleDetails(${product.productId})">More</button>
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Pagination -->
                                <div class="pagination">
                                    <c:if test="${currentPage > 1}">
                                        <a href="CategoryServlet?categoryId=${categoryId}&page=${currentPage - 1}">Previous</a>
                                    </c:if>
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <a href="CategoryServlet?categoryId=${categoryId}&page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <a href="CategoryServlet?categoryId=${categoryId}&page=${currentPage + 1}">Next</a>
                                    </c:if>
                                </div>
                            </c:otherwise>
                        </c:choose>
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
    <script>
        // Cập nhật giá trị thuộc tính khi người dùng chọn
        $(document).ready(function() {
            $('.attribute-dropdown').change(function() {
                var selectedValue = $(this).val();
                $(this).closest('form').find('.selected-attribute').val(selectedValue);
            });
        });

        // Hàm chuyển đổi hiển thị chi tiết sản phẩm
        function toggleDetails(productId) {
            var shortDesc = document.getElementById('short-desc-' + productId);
            var details = document.getElementById('details-' + productId);
            var button = document.querySelector('button[data-product-id="' + productId + '"]');

            if (details.style.display === 'none' || details.style.display === '') {
                shortDesc.style.display = 'none';
                details.style.display = 'block';
                button.textContent = 'Hide';
            } else {
                shortDesc.style.display = 'block';
                details.style.display = 'none';
                button.textContent = 'More';
            }
        }
    </script>
</body>
</html>