<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi Tiết Sản Phẩm - ${product.name}</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <style>
            .container-fluid {
                margin-top: 20px;
            }
            .product-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 2rem;
                border-radius: 10px;
                margin-bottom: 30px;
            }
            .info-card {
                border: none;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                margin-bottom: 20px;
                border-radius: 10px;
            }
            .info-card .card-header {
                background-color: #f8f9fa;
                border-bottom: 2px solid #e9ecef;
                font-weight: 600;
                border-radius: 10px 10px 0 0;
            }
            .status-badge {
                font-size: 0.9em;
                padding: 8px 12px;
            }
            .price-display {
                font-size: 1.5em;
                font-weight: bold;
            }
            .original-price {
                text-decoration: line-through;
                color: #6c757d;
                font-size: 0.8em;
            }
            .sale-price {
                color: #dc3545;
            }
            .btn-back {
                margin-right: 10px;
            }
            .feature-list {
                list-style: none;
                padding: 0;
            }
            .feature-list li {
                padding: 10px 0;
                border-bottom: 1px solid #e9ecef;
                display: flex;
                justify-content: space-between;
            }
            .feature-list li:last-child {
                border-bottom: none;
            }
            .feature-label {
                color: #495057;
                font-weight: 600;
                min-width: 140px;
            }
            .feature-value {
                color: #6c757d;
                flex: 1;
                text-align: right;
            }
            .discount-info {
                background: linear-gradient(45deg, #28a745, #20c997);
                color: white;
                padding: 10px;
                border-radius: 5px;
                margin-top: 10px;
            }
            .quantity-warning {
                background: #fff3cd;
                color: #856404;
                padding: 10px;
                border-radius: 5px;
                border-left: 4px solid #ffc107;
            }
            .quantity-danger {
                background: #f8d7da;
                color: #721c24;
                padding: 10px;
                border-radius: 5px;
                border-left: 4px solid #dc3545;
            }
            .quantity-success {
                background: #d1edff;
                color: #004085;
                padding: 10px;
                border-radius: 5px;
                border-left: 4px solid #007bff;
            }
            .action-buttons {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 10px;
                margin-top: 20px;
            }
            .product-header.bg-danger {
                background: linear-gradient(135deg, #dc3545 0%, #c82333 100%) !important;
            }

            .product-header .alert {
                background-color: rgba(255, 255, 255, 0.2);
                border-color: rgba(255, 255, 255, 0.3);
                color: white;
            }

            .product-header .badge-light {
                background-color: rgba(255, 255, 255, 0.9);
                color: #dc3545;
                font-weight: bold;
            }
            .main-image {
    width: 100%;
    max-height: 400px;
    object-fit: contain;
    background: #f8f9fa;
    border: 1px solid #dee2e6;
}

.additional-image {
    width: 100%;
    height: 150px;
    object-fit: cover;
    border: 1px solid #dee2e6;
    transition: transform 0.2s;
}

.additional-image:hover {
    transform: scale(1.05);
}
        </style>
    </head>
    <body>
        
        <div class="container-fluid">
            <!-- Check if product exists -->
            <c:if test="${empty product}">
                <div class="alert alert-danger text-center">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h4>Không tìm thấy sản phẩm!</h4>
                    <p>Sản phẩm bạn đang tìm không tồn tại hoặc đã bị xóa.</p>
                    <a href="products" class="btn btn-primary">
                        <i class="fas fa-arrow-left"></i> Quay lại danh sách
                    </a>
                </div>
            </c:if>

            <c:if test="${not empty product}">
                <!-- Product Header -->
                <div class="product-header ${product.isDeleted == 1 ? 'bg-danger' : ''}">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h1 class="mb-2">
                                <i class="fas fa-fish"></i> ${product.name}
                                <c:if test="${product.featured}">
                                    <span class="badge badge-warning ml-2">
                                        <i class="fas fa-star"></i> Nổi bật
                                    </span>
                                </c:if>
                                <!-- Thêm badge cho sản phẩm đã xóa -->
                                <c:if test="${product.isDeleted == 1}">
                                    <span class="badge badge-light ml-2">
                                        <i class="fas fa-trash-alt"></i> Đã xóa
                                    </span>
                                </c:if>
                            </h1>
                            <!-- Thêm thông báo cho sản phẩm đã xóa -->
                            <c:if test="${product.isDeleted == 1}">
                                <div class="alert alert-light border-light mt-2 mb-0">
                                    <i class="fas fa-exclamation-circle"></i>
                                    <strong>Lưu ý:</strong> Sản phẩm này đã bị xóa và không hiển thị cho khách hàng.
                                </div>
                            </c:if>
                            <p class="mb-0 lead">
                                <c:choose>
                                    <c:when test="${not empty product.shortDescription}">
                                        ${product.shortDescription}
                                    </c:when>
                                    <c:otherwise>
                                        <em>Không có mô tả ngắn</em>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="col-md-4 text-right">
                            <a href="products" class="btn btn-light btn-back">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </a>
                            <a href="products?action=edit&id=${product.productId}" class="btn btn-warning">
                                <i class="fas fa-edit"></i> Sửa
                            </a>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <!-- Left Column - Basic Info -->
                    <div class="col-md-6">
                        <!-- Basic Information -->
                        <div class="card info-card">
                            <div class="card-header">
                                <i class="fas fa-info-circle"></i> Thông Tin Cơ Bản
                            </div>
                            <div class="card-body">
                                <ul class="feature-list">
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-hashtag"></i> ID:
                                        </span>
                                        <span class="feature-value">${product.productId}</span>
                                    </li>
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-tags"></i> Danh mục:
                                        </span>
                                        <span class="feature-value">
                                            <c:set var="found" value="false" />
                                            <c:forEach var="category" items="${categorys}">
                                                <c:if test="${category.categoryId == product.categoryId}">
                                                    <c:set var="found" value="true" />

                                                    <!-- Hiển thị parent category nếu có -->
                                                    <c:if test="${category.parentId != null && category.categoryId != category.parentId}">
                                                        <c:forEach var="parentCategory" items="${categorys}">
                                                            <c:if test="${parentCategory.categoryId == category.parentId}">
                                                                <span class="badge badge-secondary mr-1">
                                                                    ${parentCategory.name}
                                                                </span>
                                                            </c:if>
                                                        </c:forEach>
                                                        <i class="fas fa-arrow-right text-muted mx-1"></i>
                                                    </c:if>

                                                    <!-- Hiển thị category hiện tại -->
                                                    <span class="badge badge-primary">
                                                        ${category.name}
                                                    </span>
                                                </c:if>
                                            </c:forEach>

                                            <c:if test="${!found}">
                                                <span class="text-muted">
                                                    <i class="fas fa-exclamation-circle"></i> Chưa phân loại
                                                </span>
                                            </c:if>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-barcode"></i> SKU:
                                        </span>
                                        <span class="feature-value">
                                            <code class="bg-light p-1">${product.sku}</code>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-toggle-on"></i> Trạng thái:
                                        </span>
                                        <span class="feature-value">
                                            <c:choose>
                                                <c:when test="${product.status == 'active'}">
                                                    <span class="badge badge-success status-badge">
                                                        <i class="fas fa-check"></i> Active
                                                    </span>
                                                </c:when>
                                                <c:when test="${product.status == 'inactive'}">
                                                    <span class="badge badge-secondary status-badge">
                                                        <i class="fas fa-pause"></i> Inactive
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning status-badge">
                                                        <i class="fas fa-exclamation-triangle"></i> out_of_stock
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-warehouse"></i> Số lượng:
                                        </span>
                                        <span class="feature-value">
                                            <span class="badge ${product.quantity > 10 ? 'badge-success' : (product.quantity > 0 ? 'badge-warning' : 'badge-danger')} status-badge">
                                                ${product.quantity} sản phẩm
                                            </span>
                                        </span>
                                    </li>
                                </ul>

                                <!-- Quantity Alert -->
                                <c:if test="${product.quantity <= 10}">
                                    <div class="mt-3">
                                        <c:choose>
                                            <c:when test="${product.quantity == 0}">
                                                <div class="quantity-danger">
                                                    <i class="fas fa-exclamation-triangle"></i>
                                                    <strong>Hết hàng!</strong> Sản phẩm này đã hết hàng.
                                                </div>
                                            </c:when>
                                            <c:when test="${product.quantity <= 5}">
                                                <div class="quantity-danger">
                                                    <i class="fas fa-exclamation-triangle"></i>
                                                    <strong>Sắp hết hàng!</strong> Chỉ còn ${product.quantity} sản phẩm.
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="quantity-warning">
                                                    <i class="fas fa-info-circle"></i>
                                                    <strong>Số lượng thấp!</strong> Còn ${product.quantity} sản phẩm.
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </div>
                        </div>
<!-- Thêm vào sau phần Basic Information và trước Pricing Information -->
<div class="card info-card">
    <div class="card-header">
        <i class="fas fa-images"></i> Hình Ảnh Sản Phẩm
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty productImages}">
                <div class="row">
                    <!-- Hiển thị ảnh chính đầu tiên -->
                    <div class="col-12 mb-4">
                        <h6 class="text-muted mb-3"><i class="fas fa-star"></i> Ảnh chính</h6>
                        <c:forEach var="image" items="${productImages}">
                            <c:if test="${image.isMain == 1}">
                                <img src="${image.imageUrl}" class="main-image img-fluid rounded" 
                                     alt="Main Product Image">
                            </c:if>
                        </c:forEach>
                    </div>
                    
                    <!-- Hiển thị các ảnh phụ -->
                    <div class="col-12">
                        <h6 class="text-muted mb-3"><i class="fas fa-images"></i> Ảnh khác</h6>
                        <div class="row">
                            <c:forEach var="image" items="${productImages}">
                                <c:if test="${image.isMain != 1}">
                                    <div class="col-md-3 col-6 mb-3">
                                        <img src="${image.imageUrl}" 
                                             class="additional-image img-fluid rounded" 
                                             alt="Additional Product Image">
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center text-muted py-4">
                    <i class="fas fa-images fa-3x mb-3"></i>
                    <p class="font-italic">Chưa có hình ảnh cho sản phẩm này</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
                        <!-- Pricing Information -->
                        <div class="card info-card">
                            <div class="card-header">
                                <i class="fas fa-money-bill-wave"></i> Thông Tin Giá
                            </div>
                            <div class="card-body">
                                <div class="price-display">
                                    <c:choose>
                                        <c:when test="${product.salePrice != null}">
                                            <div class="sale-price mb-2">
                                                <i class="fas fa-tags"></i>
                                                <fmt:formatNumber value="${product.salePrice}" type="currency" 
                                                                  currencySymbol="₫" pattern="#,##0 ₫"/>
                                            </div>
                                            <div class="original-price mb-2">
                                                Giá gốc: <fmt:formatNumber value="${product.price}" type="currency" 
                                                                  currencySymbol="₫" pattern="#,##0 ₫"/>
                                            </div>
                                            <div class="discount-info">
                                                <i class="fas fa-percentage"></i>
                                                Tiết kiệm: <fmt:formatNumber value="${product.price - product.salePrice}" 
                                                                  type="currency" currencySymbol="₫" pattern="#,##0 ₫"/>
                                                (<fmt:formatNumber value="${((product.price - product.salePrice) / product.price) * 100}" 
                                                                  pattern="#.#"/>%)
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-primary">
                                                <i class="fas fa-money-bill"></i>
                                                <fmt:formatNumber value="${product.price}" type="currency" 
                                                                  currencySymbol="₫" pattern="#,##0 ₫"/>
                                            </div>
                                            <small class="text-muted">Giá niêm yết</small>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Right Column - Details -->
                    <div class="col-md-6">
                        <!-- Description -->
                        <div class="card info-card">
                            <div class="card-header">
                                <i class="fas fa-file-alt"></i> Mô Tả Chi Tiết
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty product.description}">
                                        <div style="white-space: pre-line; line-height: 1.6;">
                                            ${product.description}
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center text-muted py-4">
                                            <i class="fas fa-file-alt fa-3x mb-3"></i>
                                            <p class="font-italic">Chưa có mô tả chi tiết cho sản phẩm này</p>
                                            <a href="products?action=edit&id=${product.productId}" class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-plus"></i> Thêm mô tả
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <!-- Thông Tin Chi Tiết Cá Cảnh - Chỉ hiển thị cho category cá cảnh -->
                        <c:set var="isFishCategory" value="false" />
                        <c:set var="isEquipmentCategory" value="false" />
                        <!-- Kiểm tra category cha -->
                        <c:forEach var="category" items="${categorys}">
                            <c:if test="${category.categoryId == product.categoryId}">
                                <c:if test="${category.parentId != null && category.categoryId != category.parentId}">
                                    <c:forEach var="parentCategory" items="${categorys}">
                                        <c:if test="${parentCategory.categoryId == category.parentId}">
                                            <c:if test="${parentCategory.categoryId == 2}">
                                                <c:set var="isEquipmentCategory" value="true" />
                                            </c:if>
                                            <c:if test="${parentCategory.categoryId == 1}">
                                                <c:set var="isFishCategory" value="true" />
                                            </c:if>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </c:if>
                        </c:forEach>
                        <c:if test="${isFishCategory && !isEquipmentCategory}">
                            <div class="card info-card">
                                <div class="card-header">
                                    <i class="fas fa-info-circle"></i> Thông Tin Chi Tiết Cá Cảnh
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${not empty productDetail}">
                                            <ul class="feature-list">
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-flask"></i> Tên khoa học:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.scientificName ? productDetail.scientificName : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-signature"></i> Tên phổ biến:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.commonName ? productDetail.commonName : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-globe-americas"></i> Nguồn gốc:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.origin ? productDetail.origin : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-ruler"></i> Kích thước:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.size ? productDetail.size : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-hourglass-half"></i> Tuổi thọ:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.lifespan ? productDetail.lifespan : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-water"></i> Loại nước:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.waterType ? productDetail.waterType : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-thermometer-half"></i> Nhiệt độ nước:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.waterTemperature ? productDetail.waterTemperature : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-vial"></i> Độ pH:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.waterPh ? productDetail.waterPh : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-drumstick-bite"></i> Chế độ ăn:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.diet ? productDetail.diet : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-baby"></i> Độ khó sinh sản:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.breedingDifficulty ? productDetail.breedingDifficulty : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-hand-holding-water"></i> Mức độ chăm sóc:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.careLevel ? productDetail.careLevel : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                                <li>
                                                    <span class="feature-label">
                                                        <i class="fas fa-fish"></i> Tương thích:
                                                    </span>
                                                    <span class="feature-value">
                                                        ${not empty productDetail.compatibility ? productDetail.compatibility : '<em class="text-muted">Chưa có thông tin</em>'}
                                                    </span>
                                                </li>
                                            </ul>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-center text-muted py-4">
                                                <i class="fas fa-fish fa-3x mb-3"></i>
                                                <p class="font-italic">Chưa có thông tin chi tiết cho sản phẩm này</p>
                                                <a href="products?action=edit&id=${product.productId}" class="btn btn-sm btn-outline-primary">
                                                    <i class="fas fa-plus"></i> Thêm thông tin chi tiết
                                                </a>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <!-- Thuộc Tính Sản Phẩm - Chỉ hiển thị cho category cá cảnh -->
                            <div class="card info-card">
                                <div class="card-header">
                                    <i class="fas fa-list-alt"></i> Thuộc Tính Sản Phẩm
                                </div>
                                <div class="card-body p-2">
                                    <c:choose>
                                        <c:when test="${not empty listProductAttributeValueByPID}">
                                            <div class="table-responsive">
                                                <table class="table table-sm table-bordered mb-0">
                                                    <thead class="thead-light">
                                                        <tr>
                                                            <th style="width: 50%;">Tên Thuộc Tính</th>
                                                            <th style="width: 50%;">Giá Trị</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="attributeValue" items="${listProductAttributeValueByPID}">
                                                            <tr>
                                                                <td>
                                                                    <i class="fas fa-tag"></i>
                                                                    <c:choose>
                                                                        <c:when test="${attributeValue.attributeId == 1}">màu sắc</c:when>
                                                                        <c:otherwise>kích thước</c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <span class="badge badge-info">${attributeValue.value}</span>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-center text-muted py-2" style="font-size: 0.95em;">
                                                <i class="fas fa-info-circle"></i>
                                                Chưa có thuộc tính nào cho sản phẩm này
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:if>

                        <!-- Timeline -->
                        <div class="card info-card">
                            <div class="card-header">
                                <i class="fas fa-history"></i> Lịch Sử & Thời Gian
                            </div>
                            <div class="card-body">
                                <ul class="feature-list">
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-calendar-plus"></i> Ngày tạo:
                                        </span>
                                        <span class="feature-value">
                                            <c:choose>
                                                <c:when test="${product.createdAt != null}">
                                                    ${product.createdAt}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Không xác định</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-calendar-check"></i> Cập nhật:
                                        </span>
                                        <span class="feature-value">
                                            <c:choose>
                                                <c:when test="${product.updatedAt != null}">
                                                    ${product.updatedAt}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Không xác định</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="feature-label">
                                            <i class="fas fa-star"></i> Sản phẩm nổi bật:
                                        </span>
                                        <span class="feature-value">
                                            <c:choose>
                                                <c:when test="${product.featured}">
                                                    <span class="badge badge-warning">
                                                        <i class="fas fa-star"></i> Có
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-light">
                                                        <i class="far fa-star"></i> Không
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                                          
                                            
                                            
                                            
                <!-- Action Buttons -->
                <div class="row">
                    <div class="col-12">
                        <div class="action-buttons text-center">
                            <div class="btn-group" role="group" aria-label="Product Actions">
                                <a href="products" class="btn btn-secondary btn-lg">
                                    <i class="fas fa-list"></i> Danh sách sản phẩm
                                </a>
                                <a href="products?action=edit&id=${product.productId}" class="btn btn-warning btn-lg">
                                    <i class="fas fa-edit"></i> Chỉnh sửa
                                </a>
                                <button type="button" class="btn btn-danger btn-lg" 
                                        onclick="confirmDelete(${product.productId}, &quot;${fn:replace(product.name, '\'', '\\\'')}&quot;)">
                                    <i class="fas fa-trash"></i> Xóa sản phẩm
                                </button>
                                <a href="products?action=new" class="btn btn-primary btn-lg">
                                    <i class="fas fa-plus"></i> Thêm sản phẩm mới
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="deleteModalLabel">
                            <i class="fas fa-exclamation-triangle"></i> Xác nhận xóa sản phẩm
                        </h5>
                        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="text-center">
                            <i class="fas fa-trash-alt text-danger" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                            <h5>Bạn có chắc chắn muốn xóa sản phẩm này?</h5>
                            <p class="text-muted">
                                Sản phẩm: <strong id="productName" class="text-dark"></strong>
                            </p>
                            <div class="alert alert-warning">
                                <i class="fas fa-exclamation-triangle"></i>
                                <strong>Cảnh báo:</strong> Hành động này không thể hoàn tác!
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <i class="fas fa-times"></i> Hủy bỏ
                        </button>
                        <a href="#" id="confirmDeleteBtn" class="btn btn-danger">
                            <i class="fas fa-trash"></i> Xác nhận xóa
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

        <script>
                                            function confirmDelete(productId, productName) {
                                                document.getElementById('productName').textContent = productName;
                                                document.getElementById('confirmDeleteBtn').href = 'products?action=delete&id=' + productId;
                                                $('#deleteModal').modal('show');
                                            }

                                            // Auto focus on modal show
                                            $('#deleteModal').on('shown.bs.modal', function () {
                                                $('#confirmDeleteBtn').focus();
                                            });

                                            // Add loading effect to action buttons
                                            $(document).ready(function () {
                                                $('.btn').click(function () {
                                                    var btn = $(this);
                                                    if (!btn.hasClass('btn-secondary') && !btn.attr('data-dismiss')) {
                                                        btn.append(' <i class="fas fa-spinner fa-spin"></i>');
                                                        btn.prop('disabled', true);
                                                    }
                                                });
                                            });
        </script>
    </body>
</html>