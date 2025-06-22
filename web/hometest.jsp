<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Trang Chủ - Cửa Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Header -->
            <div class="col-12">
                <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                    <div class="container-fluid">
                        <a class="navbar-brand" href="#">
                            <i class="fas fa-store me-2"></i>
                            Cửa Hàng Online
                        </a>
                        <div class="navbar-nav ms-auto">
                            <a class="nav-link" href="cart.jsp">
                                <i class="fas fa-shopping-cart me-1"></i>
                                Giỏ Hàng (<span id="cartCount">0</span>)
                            </a>
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <div class="row mt-4">
            <!-- Sidebar -->
            <div class="col-md-2">
                <div class="card">
                    <div class="card-header">
                        <h6><i class="fas fa-chart-bar me-2"></i>Thống Kê</h6>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <small class="text-muted">Tổng sản phẩm</small>
                            <div class="h4 text-primary">${totalProducts}</div>
                        </div>
                        <div class="mb-3">
                            <small class="text-muted">Sản phẩm nổi bật</small>
                            <div class="h4 text-success">${featuredCount}</div>
                        </div>
                        <div class="mb-3">
                            <small class="text-muted">Còn hàng</small>
                            <div class="h4 text-info">${inStockProducts}</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-10">
                <!-- Featured Products Section -->
                <c:if test="${not empty featuredProducts}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5><i class="fas fa-star text-warning me-2"></i>Sản Phẩm Nổi Bật</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <c:forEach var="product" items="${featuredProducts}" varStatus="status">
                                    <c:if test="${status.index < 6}"> <!-- Chỉ hiển thị 6 sản phẩm đầu -->
                                        <div class="col-md-4 mb-3">
                                            <div class="card border-warning h-100">
                                                <div class="card-body d-flex flex-column">
                                                    <h6 class="card-title">${product.name}</h6>
                                                    <p class="card-text text-muted flex-grow-1">${product.shortDescription}</p>
                                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                                        <div>
                                                            <c:if test="${product.salePrice != null && product.salePrice.compareTo(product.price) < 0}">
                                                                <span class="text-decoration-line-through text-muted">
                                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫"/>
                                                                </span>
                                                                <br>
                                                                <span class="text-danger fw-bold">
                                                                    <fmt:formatNumber value="${product.salePrice}" type="currency" currencySymbol="₫"/>
                                                                </span>
                                                            </c:if>
                                                            <c:if test="${product.salePrice == null || product.salePrice.compareTo(product.price) >= 0}">
                                                                <span class="fw-bold">
                                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫"/>
                                                                </span>
                                                            </c:if>
                                                        </div>
                                                        <small class="text-muted">SL: ${product.quantity}</small>
                                                    </div>
                                                    <div class="mb-2">
                                                        <span class="badge ${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
                                                            ${product.quantity > 0 ? 'Còn hàng' : 'Hết hàng'}
                                                        </span>
                                                        <span class="badge bg-warning">Nổi bật</span>
                                                    </div>
                                                    <c:if test="${product.quantity > 0 && product.status == 'active'}">
                                                        <button class="btn btn-primary btn-sm" 
                                                                onclick="addToCart(${product.productId}, '${product.name}', ${product.salePrice != null && product.salePrice.compareTo(product.price) < 0 ? product.salePrice : product.price})">
                                                            <i class="fas fa-cart-plus me-1"></i>Thêm vào giỏ
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${product.quantity <= 0 || product.status != 'active'}">
                                                        <button class="btn btn-secondary btn-sm" disabled>
                                                            <i class="fas fa-ban me-1"></i>Không khả dụng
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- All Products Section -->
                <div class="card">
                    <div class="card-header">
                        <h5><i class="fas fa-boxes me-2"></i>Tất Cả Sản Phẩm</h5>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty products}">
                            <div class="alert alert-info text-center">
                                <i class="fas fa-info-circle me-2"></i>
                                Chưa có sản phẩm nào trong hệ thống.
                            </div>
                        </c:if>

                        <c:if test="${not empty products}">
                            <div class="row">
                                <c:forEach var="product" items="${products}">
                                    <c:if test="${product.status == 'active'}"> <!-- Chỉ hiển thị sản phẩm active -->
                                        <div class="col-md-4 mb-4">
                                            <div class="card h-100">
                                                <div class="card-body d-flex flex-column">
                                                    <h6 class="card-title">${product.name}</h6>
                                                    <small class="text-muted mb-2">SKU: ${product.sku}</small>
                                                    <c:if test="${not empty product.shortDescription}">
                                                        <p class="card-text text-muted flex-grow-1">${product.shortDescription}</p>
                                                    </c:if>
                                                    
                                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                                        <div>
                                                            <c:if test="${product.salePrice != null && product.salePrice.compareTo(product.price) < 0}">
                                                                <span class="text-decoration-line-through text-muted">
                                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫"/>
                                                                </span>
                                                                <br>
                                                                <span class="text-danger fw-bold">
                                                                    <fmt:formatNumber value="${product.salePrice}" type="currency" currencySymbol="₫"/>
                                                                </span>
                                                            </c:if>
                                                            <c:if test="${product.salePrice == null || product.salePrice.compareTo(product.price) >= 0}">
                                                                <span class="fw-bold">
                                                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫"/>
                                                                </span>
                                                            </c:if>
                                                        </div>
                                                        <small class="text-muted">SL: ${product.quantity}</small>
                                                    </div>

                                                    <div class="mb-2">
                                                        <span class="badge ${product.quantity > 0 ? 'bg-success' : 'bg-danger'}">
                                                            ${product.quantity > 0 ? 'Còn hàng' : 'Hết hàng'}
                                                        </span>
                                                        <c:if test="${product.getFeatured() == 1}">
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-star"></i> Nổi bật
                                                            </span>
                                                        </c:if>
                                                    </div>

                                                    <c:if test="${product.quantity > 0}">
                                                        <button class="btn btn-primary btn-sm" 
                                                                onclick="addToCart(${product.productId}, '${product.name}', ${product.salePrice != null && product.salePrice.compareTo(product.price) < 0 ? product.salePrice : product.price})">
                                                            <i class="fas fa-cart-plus me-1"></i>Thêm vào giỏ
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${product.quantity <= 0}">
                                                        <button class="btn btn-secondary btn-sm" disabled>
                                                            <i class="fas fa-ban me-1"></i>Hết hàng
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Toast notification -->
    <div class="position-fixed top-0 end-0 p-3" style="z-index: 1050">
        <div id="cartToast" class="toast hide" role="alert">
            <div class="toast-header bg-success text-white">
                <i class="fas fa-check-circle me-2"></i>
                <strong class="me-auto">Thành công</strong>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast"></button>
            </div>
            <div class="toast-body" id="toastMessage">
                Đã thêm sản phẩm vào giỏ hàng!
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Giỏ hàng lưu trong sessionStorage
        let cart = JSON.parse(sessionStorage.getItem('cart') || '[]');
        
        // Cập nhật số lượng giỏ hàng khi tải trang
        updateCartCount();
        
        function addToCart(productId, productName, price) {
            // Kiểm tra sản phẩm đã có trong giỏ chưa
            const existingItem = cart.find(item => item.id === productId);
            
            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({
                    id: productId,
                    name: productName,
                    price: price,
                    quantity: 1
                });
            }
            
            // Lưu vào sessionStorage
            sessionStorage.setItem('cart', JSON.stringify(cart));
            
            // Cập nhật số lượng hiển thị
            updateCartCount();
            
            // Hiển thị thông báo
            showToast(`Đã thêm "${productName}" vào giỏ hàng!`);
        }
        
        function updateCartCount() {
            const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
            document.getElementById('cartCount').textContent = totalItems;
        }
        
        function showToast(message) {
            document.getElementById('toastMessage').textContent = message;
            const toast = new bootstrap.Toast(document.getElementById('cartToast'));
            toast.show();
        }
    </script>
</body>
</html>