<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Sản Phẩm</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <!-- JQuery, Popper.js, và Bootstrap JS (đảm bảo đúng thứ tự) -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

        <style>
            /* Modern Product Management Styles - 125% Scale */
            :root {
                --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                --secondary-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                --warning-gradient: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
                --danger-gradient: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
                --info-gradient: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);

                --shadow-soft: 0 7.5px 32.5px 0 rgba(31, 38, 135, 0.37);
                --shadow-hover: 0 15px 35px rgba(31, 38, 135, 0.2);
                --border-radius: 22px;
                --transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
            }

            /* Global Styles - 125% Scale */
            body {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                font-size: 1rem; /* 125% of 0.8rem */
            }

            .container-fluid {
                padding: 3rem; /* 125% of 2.4rem */
                max-width: 2000px; /* 125% of 1600px */
                margin: 0 auto;
            }

            /* Glass Card Effect - 125% Scale */
            .card {
                border-radius: 27.5px; /* 125% of 22px */
                box-shadow: 0 11px 32.5px 0 rgba(90,120,200,0.16), 0 2.75px 7.5px 0 rgba(60,72,88,0.12);
                overflow: hidden;
                border: none;
            }

            .card-header {
                background: linear-gradient(90deg, #57d6fd 0%, #7857fd 100%);
                color: #fff;
                padding: 27.5px 37.5px 20px 37.5px; /* 125% scale */
                border-radius: 27.5px 27.5px 0 0 !important;
                display: flex;
                align-items: center;
                justify-content: space-between;
                border-bottom: none;
            }

            .card-title {
                font-size: 2.1rem; /* 125% of 1.68rem */
                font-weight: 800;
                text-shadow: 1.25px 2.5px 7.5px #8671e7a2;
                letter-spacing: .04em;
                margin-bottom: 0;
            }

            .btn-primary {
                background: linear-gradient(90deg,#ee9ca7 0%, #ffdde1 100%);
                color: #424874 !important;
                border: none;
                border-radius: 12.5px; /* 125% of 10px */
                font-size: 1.075em; /* 125% of 0.86em */
                font-weight: 600;
                box-shadow: 0 2.75px 13.75px #c48ce25a;
                padding: 10px 25px; /* 125% scale */
                transition: box-shadow .2s, background .25s, color .18s;
            }

            .btn-primary:hover, .btn-primary:focus {
                background: linear-gradient(90deg, #a8edea 0%, #fed6e3 100%);
                color: #7c2ae8 !important;
                box-shadow: 0 7.5px 30px #9469e46a;
                transform: translateY(-2px); /* 125% of -1.6px */
            }

            .card-body {
                padding: 2rem; /* 125% of 1.6rem */
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px); /* 125% of 8px */
            }

            /* Alert Messages - 125% Scale */
            .alert {
                border-radius: 15px; /* 125% of 12px */
                border: none;
                margin-bottom: 25px; /* 125% of 20px */
                box-shadow: 0 3.75px 15px rgba(0,0,0,0.1);
                font-size: 1rem;
            }

            .alert-success {
                background: linear-gradient(90deg, #81fbb8 0%, #28c76f 100%);
                color: #23653a;
            }

            .alert-danger {
                background: linear-gradient(90deg, #fdc5c5 0%, #fc5c7d 100%);
                color: #84213b;
            }

            /* Table Styles - 125% Scale - FIXED */
            .table-responsive {
                border-radius: 16.25px; /* 125% of 13px */
                box-shadow: var(--shadow-soft);
                overflow: hidden; /* Changed from overflow-y: auto; overflow-x: auto; */
                max-height: 800px; /* 125% of 640px */
                position: relative;
            }

            /* Create inner scrollable container */
            .table-scroll-container {
                overflow-x: auto;
                overflow-y: auto;
                max-height: 800px;
                /* Hide scrollbar but keep functionality */
                scrollbar-width: thin;
                scrollbar-color: rgba(0,0,0,0.2) transparent;
            }

            /* Webkit scrollbar styling */
            .table-scroll-container::-webkit-scrollbar {
                width: 8px;
                height: 8px;
            }

            .table-scroll-container::-webkit-scrollbar-track {
                background: rgba(0,0,0,0.05);
                border-radius: 4px;
            }

            .table-scroll-container::-webkit-scrollbar-thumb {
                background: rgba(0,0,0,0.2);
                border-radius: 4px;
            }

            .table-scroll-container::-webkit-scrollbar-thumb:hover {
                background: rgba(0,0,0,0.3);
            }

            .table {
                width: 100%;
                background: rgba(255, 255, 255, 0.9);
                backdrop-filter: blur(10px);
                margin: 0;
                border-collapse: separate;
                border-spacing: 0;
                font-size: 1rem;
            }

            .table thead th {
                background: var(--primary-gradient);
                color: white;
                font-weight: 700;
                text-align: center;
                padding: 1.2rem 0.8rem; /* 125% of 0.96rem 0.64rem */
                font-size: 0.95rem; /* 125% of 0.76rem */
                text-transform: uppercase;
                letter-spacing: 0.5px;
                border: none;
                position: sticky;
                top: 0;
                z-index: 10;
            }

            .table tbody td {
                padding: 1rem 0.8rem; /* 125% of 0.8rem 0.64rem */
                vertical-align: middle;
                text-align: center;
                border-bottom: 1px solid rgba(0, 0, 0, 0.05);
                transition: var(--transition);
                font-size: 0.95rem;
            }

            /* FIXED - Removed transform: scale() to prevent layout shifts */
            .table-hover tbody tr {
                transition: background-color 0.2s ease, box-shadow 0.2s ease;
            }

            .table-hover tbody tr:hover {
                background: linear-gradient(90deg,#e8f0fe 0%, #ffe3ed 100%);
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                /* Removed transform: scale() and z-index changes */
            }

            /* Badge Styles - 125% Scale */
            .badge {
                font-size: 0.85em; /* 125% of 0.68em */
                padding: 7.5px 12.5px; /* 125% of 6px 10px */
                border-radius: 15px; /* 125% of 12px */
                font-weight: 600;
                box-shadow: 0 2.75px 7.5px rgba(192, 178, 255, 0.26);
            }

            .badge-success {
                background: linear-gradient(90deg,#81fbb8 0%, #28c76f 100%);
                color: #23653a;
            }

            .badge-warning {
                background: linear-gradient(90deg,#fff886 0%, #f072b6 100%);
                color: #86550b;
            }

            .badge-danger {
                background: linear-gradient(90deg,#fdc5c5 0%, #fc5c7d 100%);
                color: #84213b;
            }

            .badge-secondary {
                background: linear-gradient(90deg,#e0eafc 0%, #cfdef3 100%);
                color: #424874;
            }

            .featured-badge {
                background: linear-gradient(90deg,#f7971e 0%, #ffd200 100%);
                color: white;
                font-size: 0.85em; /* 125% of 0.68em */
                border-radius: 10px; /* 125% of 8px */
                padding: 6.25px 12.5px; /* 125% of 5px 10px */
                margin-top: 2.75px;
                box-shadow: 0 2.75px 7.5px #f8d34b66;
            }

            /* Product Info Styles - 125% Scale */
            .product-name {
                font-weight: 700;
                color: #5643fa;
                font-size: 0.95em; /* 125% of 0.76em */
                margin-bottom: 2.75px;
            }

            .product-description {
                font-size: 0.8em; /* 125% of 0.64em */
                color: #6c757d;
                font-style: italic;
                line-height: 1.4;
            }

            .sku-code {
                background: linear-gradient(90deg, #f8f9fa 0%, #e9ecef 100%);
                color: #495057;
                padding: 3.75px 7.5px; /* 125% of 3px 6px */
                border-radius: 6.25px; /* 125% of 5px */
                font-family: 'Courier New', monospace;
                font-size: 0.85em; /* 125% of 0.68em */
            }

            /* Currency Formatting - 125% Scale */
            .price-original {
                font-weight: 600;
                color: #28a745;
                font-size: 1rem;
            }

            .price-sale {
                font-weight: 700;
                color: #dc3545;
                font-size: 1.05em; /* 125% of 0.84em */
            }

            /* Action Buttons - 125% Scale */
            .btn-action {
                width: 2.2rem; /* 125% of 1.76rem */
                height: 2.2rem; /* 125% of 1.76rem */
                border-radius: 7.5px; /* 125% of 6px */
                border: none;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                margin: 0.1rem; /* 125% of 0.08rem */
                transition: all 0.3s ease;
                font-size: 0.85rem; /* 125% of 0.68rem */
                position: relative;
                overflow: hidden;
            }

            .btn-action::before {
                content: '';
                position: absolute;
                top: 50%;
                left: 50%;
                width: 0;
                height: 0;
                background: rgba(255,255,255,0.3);
                border-radius: 50%;
                transform: translate(-50%, -50%);
                transition: all 0.3s ease;
            }

            .btn-action:hover::before {
                width: 100%;
                height: 100%;
            }

            .btn-action:hover {
                transform: translateY(-2px); /* 125% of -1.6px */
                box-shadow: 0 6.25px 15px rgba(0,0,0,0.2);
            }

            .btn-info {
                background: linear-gradient(90deg,#d7d2cc 0%, #304352 100%);
                color: #fff !important;
            }

            .btn-info:hover {
                background: #81ecec !important;
                color: #342343 !important;
            }

            .btn-warning {
                background: linear-gradient(90deg,#fceabb 0%, #f8b500 100%);
                color: #654321 !important;
            }

            .btn-warning:hover {
                background: #fffde4 !important;
                color: #f59e00 !important;
            }

            .btn-danger {
                background: linear-gradient(90deg,#ff5858 0%, #f09819 100%);
                color: #fff !important;
            }

            .btn-danger:hover {
                background: #ffc3a0 !important;
                color: #84213b !important;
            }

            .btn-success {
                background: linear-gradient(90deg,#43e97b 0%, #38f9d7 100%);
                color: #fff !important;
            }

            .btn-success:hover {
                background: #bff098 !important;
                color: #17575c !important;
            }

            /* Empty State - 125% Scale */
            .empty-state {
                background: linear-gradient(120deg,#f6d365 0%, #fda085 100%);
                border-radius: 27.5px; /* 125% of 22px */
                margin-top: 30px; /* 125% of 24px */
                padding: 4rem 2rem; /* 125% of 3.2rem 1.6rem */
                box-shadow: 0 2.75px 12.5px #dcb6f5a5;
                text-align: center;
            }

            .empty-state i {
                color: #ad5389;
                margin-bottom: 1rem;
                font-size: 3rem; /* 125% of 2.4rem */
            }

            .empty-state h5 {
                color: #6c5b7b;
                font-weight: 600;
                margin-bottom: 1rem;
                font-size: 1.5rem;
            }

            .empty-state p {
                color: #8b7ca3;
                margin-bottom: 2rem;
                font-size: 1.125rem;
            }

            /* Modal Styles - 125% Scale */
            .modal-content {
                border-radius: 20px; /* 125% of 16px */
                border: none;
                box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            }

            .modal-header {
                background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-bottom: none;
                border-radius: 20px 20px 0 0;
                padding: 1.5rem 2rem; /* 125% scale */
            }

            .modal-title {
                font-weight: 600;
                font-size: 1.375rem;
            }

            .modal-body {
                padding: 2rem; /* 125% of 1.6rem */
                font-size: 1.0625rem;
            }

            .modal-footer {
                border-top: none;
                padding: 1rem 2rem 2rem; /* 125% scale */
            }

            /* Status Icons */
            .status-active {
                color: #28a745;
            }
            .status-inactive {
                color: #dc3545;
            }
            .status-out-of-stock {
                color: #ffc107;
            }

            /* Responsive Design - 125% Scale */
            @media (max-width: 1200px) {
                .table th, .table td {
                    font-size: 0.9em; /* 125% of 0.72em */
                    padding: 0.8rem 0.5rem; /* 125% of 0.64rem 0.4rem */
                }
                
                .card-title {
                    font-size: 1.8rem; /* 125% of 1.44rem */
                }
                
                .container-fluid {
                    padding: 2rem; /* 125% of 1.6rem */
                }
            }

            @media (max-width: 768px) {
                .table th, .table td {
                    font-size: 0.8em; /* 125% of 0.64em */
                    padding: 0.6rem 0.3rem; /* 125% of 0.48rem 0.24rem */
                }
                
                .btn-action {
                    width: 1.8rem; /* 125% of 1.44rem */
                    height: 1.8rem; /* 125% of 1.44rem */
                    font-size: 0.7rem; /* 125% of 0.56rem */
                }
                
                .card-header {
                    flex-direction: column;
                    gap: 1rem; /* 125% of 0.8rem */
                }
                
                .card-title {
                    font-size: 1.5rem; /* 125% of 1.2rem */
                }
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="row w-100">
                                <div class="col-md-6">
                                    <h3 class="card-title">
                                        <i class="fas fa-fish"></i> Quản Lý Sản Phẩm
                                    </h3>
                                </div>
                                <div class="col-md-6 text-right">
                                    <a href="products?action=new" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> Thêm Sản Phẩm Mới
                                    </a>
                                </div>
                            </div>
                        </div>

                        <div class="card-body">
                            <!-- Alert Messages -->
                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success alert-dismissible fade show">
                                    <i class="fas fa-check-circle"></i> ${successMessage}
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                </div>
                            </c:if>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger alert-dismissible fade show">
                                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                </div>
                            </c:if>

                            <!-- Products Table -->
                            <div class="table-responsive">
                                <div class="table-scroll-container">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Tên Sản Phẩm</th>
                                                <th>Danh Mục</th>
                                                <th>SKU</th>
                                                <th>Giá</th>
                                                <th>Giá Khuyến Mãi</th>
                                                <th>Số Lượng</th>
                                                <th>Trạng Thái</th>
                                                <th>Nổi Bật</th>
                                                <th>Ngày Tạo</th>
                                                <th>Hành Động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="product" items="${listProduct}">
                                                <tr>
                                                    <td><span class="badge badge-secondary">${product.productId}</span></td>
                                                    <td>
                                                        <div class="product-name">${product.name}</div>
                                                        <c:if test="${not empty product.shortDescription}">
                                                            <div class="product-description">${product.shortDescription}</div>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:forEach var="category" items="${listCategory}">
                                                            <c:if test="${category.categoryId == product.categoryId}">
                                                                <c:forEach var="parentCategory" items="${listCategory}">
                                                                    <c:if test="${parentCategory.categoryId == category.parentId}">
                                                                        <span class="badge badge-info">${parentCategory.name}</span>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </c:if>
                                                        </c:forEach>
                                                    </td>
                                                    <td><code class="sku-code">${product.sku}</code></td>
                                                    <td>
                                                        <span class="price-original">
                                                            <fmt:formatNumber value="${product.price}" type="currency" 
                                                                              currencySymbol="₫" pattern="#,##0 ₫"/>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${product.salePrice != null}">
                                                                <span class="price-sale">
                                                                    <fmt:formatNumber value="${product.salePrice}" type="currency" 
                                                                                      currencySymbol="₫" pattern="#,##0 ₫"/>
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted">-</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${product.quantity > 10}">
                                                                <span class="badge badge-success">${product.quantity}</span>
                                                            </c:when>
                                                            <c:when test="${product.quantity > 0}">
                                                                <span class="badge badge-warning">${product.quantity}</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-danger">${product.quantity}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${product.status == 'active'}">
                                                                <span class="badge badge-success">
                                                                    <i class="fas fa-check"></i> Hoạt động
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${product.status == 'inactive'}">
                                                                <span class="badge badge-secondary">
                                                                    <i class="fas fa-pause"></i> Không hoạt động
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-warning">
                                                                    <i class="fas fa-exclamation-triangle"></i> Hết hàng
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${product.featured}">
                                                            <span class="featured-badge">
                                                                <i class="fas fa-star"></i>
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <small class="text-muted">${product.createdAt}</small>
                                                    </td>
                                                    <td>
                                                        <div class="d-flex justify-content-center">
                                                            <a href="products?action=view&id=${product.productId}" 
                                                               class="btn btn-info btn-action" title="Xem chi tiết">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            <a href="products?action=edit&id=${product.productId}" 
                                                               class="btn btn-warning btn-action" title="Sửa">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                            <c:choose>
                                                                <c:when test="${product.isDeleted == 0}">
                                                                    <a href="products?action=delete&id=${product.productId}" 
                                                                       class="btn btn-danger btn-action" 
                                                                       onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?')"
                                                                       title="Xóa sản phẩm">
                                                                        <i class="fas fa-trash"></i>
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a href="products?action=delete&id=${product.productId}" 
                                                                       class="btn btn-success btn-action" 
                                                                       onclick="return confirm('Bạn có chắc chắn muốn khôi phục sản phẩm này không?')"
                                                                       title="Khôi phục">
                                                                        <i class="fas fa-undo"></i>
                                                                    </a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Empty State -->
                            <c:if test="${empty listProduct}">
                                <div class="empty-state">
                                    <i class="fas fa-inbox fa-3x"></i>
                                    <h5>Chưa có sản phẩm nào</h5>
                                    <p>Hãy thêm sản phẩm đầu tiên của bạn!</p>
                                    <a href="products?action=new" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> Thêm Sản Phẩm Mới
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
    </body>
</html>