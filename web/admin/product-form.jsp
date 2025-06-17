<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><c:choose><c:when test="${product != null}">Sửa Sản Phẩm</c:when><c:otherwise>Thêm Sản Phẩm Mới</c:otherwise></c:choose></title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
                <style>
                    
                    :root {
                        --primary-color: #4f46e5;
                        --primary-light: #6366f1;
                        --secondary-color: #64748b;
                        --success-color: #10b981;
                        --warning-color: #f59e0b;
                        --danger-color: #ef4444;
                        --background-color: #f8fafc;
                        --card-background: #ffffff;
                        --border-color: #e2e8f0;
                        --text-primary: #1e293b;
                        --text-secondary: #64748b;
                        --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
                        --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
                        --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
                        --border-radius: 12px;
                        --border-radius-sm: 8px;
                    }

                    * {
                        box-sizing: border-box;
                    }

                    body {
                        font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        color: var(--text-primary);
                        font-size: 14px;
                        line-height: 1.6;
                    }

                    .main-container {
                        padding: 2rem 1rem;
                        min-height: 100vh;
                    }

                    .form-wrapper {
                        max-width: 1200px;
                        margin: 0 auto;
                    }

                    .header-section {
                        background: var(--card-background);
                        border-radius: var(--border-radius);
                        box-shadow: var(--shadow-md);
                        padding: 2rem;
                        margin-bottom: 2rem;
                        border: 1px solid var(--border-color);
                    }

                    .header-title {
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                        margin: 0;
                        color: var(--text-primary);
                        font-weight: 700;
                        font-size: 1.75rem;
                    }

                    .header-icon {
                        width: 48px;
                        height: 48px;
                        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
                        border-radius: var(--border-radius-sm);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        color: white;
                        font-size: 1.25rem;
                    }

                    .back-btn {
                        background: var(--background-color);
                        border: 1px solid var(--border-color);
                        color: var(--text-secondary);
                        padding: 0.75rem 1.5rem;
                        border-radius: var(--border-radius-sm);
                        text-decoration: none;
                        font-weight: 500;
                        transition: all 0.2s ease;
                        display: inline-flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .back-btn:hover {
                        background: var(--border-color);
                        color: var(--text-primary);
                        text-decoration: none;
                        transform: translateY(-1px);
                    }

                    .form-card {
                        background: var(--card-background);
                        border-radius: var(--border-radius);
                        box-shadow: var(--shadow-md);
                        border: 1px solid var(--border-color);
                        overflow: hidden;
                        margin-bottom: 2rem;
                    }

                    .form-card-header {
                        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
                        color: white;
                        padding: 1.5rem 2rem;
                        border-bottom: none;
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                    }

                    .form-card-header h5 {
                        margin: 0;
                        font-weight: 600;
                        font-size: 1.1rem;
                    }

                    .form-card-body {
                        padding: 2rem;
                    }

                    .form-group {
                        margin-bottom: 1.5rem;
                    }

                    .form-label {
                        font-weight: 600;
                        color: var(--text-primary);
                        margin-bottom: 0.5rem;
                        display: block;
                        font-size: 0.875rem;
                    }

                    .required {
                        color: var(--danger-color);
                        margin-left: 0.25rem;
                    }

                    .form-control {
                        border: 2px solid var(--border-color);
                        border-radius: var(--border-radius-sm);
                        padding: 0.75rem 1rem;
                        font-size: 0.875rem;
                        transition: all 0.2s ease;
                        background: var(--card-background);
                        color: var(--text-primary);
                        height: 50px;
                    }

                    .form-control:focus {
                        border-color: var(--primary-color);
                        box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
                        outline: none;
                    }

                    .form-control.is-invalid {
                        border-color: var(--danger-color);
                        box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
                    }

                    .form-control.is-valid {
                        border-color: var(--success-color);
                        box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
                    }

                    .input-group {
                        position: relative;
                    }

                    .input-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        color: var(--text-secondary);
                        font-size: 0.875rem;
                        z-index: 5;
                    }

                    .form-control.has-icon {
                        padding-left: 2.75rem;
                    }

                    .form-text {
                        font-size: 0.75rem;
                        color: var(--text-secondary);
                        margin-top: 0.5rem;
                    }

                    .custom-checkbox {
                        position: relative;
                        padding-left: 2rem;
                    }

                    .custom-checkbox input {
                        position: absolute;
                        opacity: 0;
                        cursor: pointer;
                    }

                    .checkmark {
                        position: absolute;
                        top: 0.125rem;
                        left: 0;
                        height: 1.25rem;
                        width: 1.25rem;
                        background: var(--card-background);
                        border: 2px solid var(--border-color);
                        border-radius: 4px;
                        transition: all 0.2s ease;
                    }

                    .custom-checkbox input:checked ~ .checkmark {
                        background: var(--primary-color);
                        border-color: var(--primary-color);
                    }

                    .checkmark:after {
                        content: "";
                        position: absolute;
                        display: none;
                        left: 0.25rem;
                        top: 0.125rem;
                        width: 0.25rem;
                        height: 0.5rem;
                        border: solid white;
                        border-width: 0 2px 2px 0;
                        transform: rotate(45deg);
                    }

                    .custom-checkbox input:checked ~ .checkmark:after {
                        display: block;
                    }

                    .alert {
                        border: none;
                        border-radius: var(--border-radius-sm);
                        padding: 1rem 1.5rem;
                        margin-bottom: 1.5rem;
                        border-left: 4px solid;
                    }

                    .alert-danger {
                        background: rgba(239, 68, 68, 0.1);
                        border-left-color: var(--danger-color);
                        color: #dc2626;
                    }

                    .alert-success {
                        background: rgba(16, 185, 129, 0.1);
                        border-left-color: var(--success-color);
                        color: #059669;
                    }

                    .btn {
                        padding: 0.75rem 1.5rem;
                        border-radius: var(--border-radius-sm);
                        font-weight: 600;
                        font-size: 0.875rem;
                        border: 2px solid;
                        transition: all 0.2s ease;
                        display: inline-flex;
                        align-items: center;
                        gap: 0.5rem;
                        text-decoration: none;
                        cursor: pointer;
                    }

                    .btn-primary {
                        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
                        border-color: var(--primary-color);
                        color: white;
                    }

                    .btn-primary:hover {
                        background: linear-gradient(135deg, var(--primary-light), var(--primary-color));
                        border-color: var(--primary-light);
                        color: white;
                        transform: translateY(-2px);
                        box-shadow: var(--shadow-lg);
                    }

                    .btn-secondary {
                        background: var(--background-color);
                        border-color: var(--border-color);
                        color: var(--text-secondary);
                    }

                    .btn-secondary:hover {
                        background: var(--border-color);
                        border-color: var(--text-secondary);
                        color: var(--text-primary);
                        text-decoration: none;
                    }

                    .btn-danger {
                        background: var(--danger-color);
                        border-color: var(--danger-color);
                        color: white;
                    }

                    .btn-danger:hover {
                        background: #dc2626;
                        border-color: #dc2626;
                        color: white;
                    }

                    .image-upload-area {
                        border: 2px dashed var(--border-color);
                        border-radius: var(--border-radius-sm);
                        padding: 2rem;
                        text-align: center;
                        transition: all 0.2s ease;
                        background: var(--background-color);
                    }

                    .image-upload-area:hover {
                        border-color: var(--primary-color);
                        background: rgba(79, 70, 229, 0.05);
                    }

                    .image-upload-area.dragover {
                        border-color: var(--primary-color);
                        background: rgba(79, 70, 229, 0.1);
                    }

                    .image-preview {
                        position: relative;
                        border-radius: 8px;
                        overflow: hidden;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        transition: all 0.2s ease;
                        width: 100%;
                        background: #f8f9fa;
                    }

                    .image-preview:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 4px 8px rgba(0,0,0,0.15);
                    }

                    .image-preview img {
                        width: 100%;
                        height: auto;
                        max-height: 400px;
                        object-fit: contain;
                        display: block;
                    }

                    .image-remove {
                        position: absolute;
                        top: 10px;
                        right: 10px;
                        background: rgba(255,255,255,0.9);
                        border: none;
                        border-radius: 50%;
                        width: 30px;
                        height: 30px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        cursor: pointer;
                        color: #dc3545;
                    }

                    .image-remove:hover {
                        background: #dc3545;
                        color: white;
                    }

                    .main-image-badge {
                        position: absolute;
                        top: 10px;
                        left: 10px;
                        background: rgba(255,255,255,0.9);
                        padding: 5px 10px;
                        border-radius: 4px;
                        font-size: 0.9em;
                        color: #ffc107;
                    }

                    .form-actions {
                        background: var(--background-color);
                        padding: 1.5rem 2rem;
                        border-top: 1px solid var(--border-color);
                        display: flex;
                        justify-content: flex-end;
                        gap: 1rem;
                    }

                    .status-badge {
                        padding: 0.25rem 0.75rem;
                        border-radius: 1rem;
                        font-size: 0.75rem;
                        font-weight: 600;
                        text-transform: uppercase;
                        letter-spacing: 0.025em;
                    }

                    .status-active {
                        background: rgba(16, 185, 129, 0.1);
                        color: var(--success-color);
                    }

                    .status-inactive {
                        background: rgba(107, 114, 128, 0.1);
                        color: #6b7280;
                    }

                    .status-out-of-stock {
                        background: rgba(239, 68, 68, 0.1);
                        color: var(--danger-color);
                    }

                    .info-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 1.5rem;
                    }

                    .fade-in {
                        animation: fadeIn 0.5s ease-in-out;
                    }

                    @keyframes fadeIn {
                        from {
                            opacity: 0;
                            transform: translateY(1rem);
                        }
                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    .loading-spinner {
                        display: inline-block;
                        width: 1rem;
                        height: 1rem;
                        border: 2px solid transparent;
                        border-top: 2px solid currentColor;
                        border-radius: 50%;
                        animation: spin 1s linear infinite;
                    }

                    @keyframes spin {
                        to {
                            transform: rotate(360deg);
                        }
                    }

                    @media (max-width: 768px) {
                        .main-container {
                            padding: 1rem;
                        }

                        .header-section {
                            padding: 1.5rem;
                        }

                        .form-card-body {
                            padding: 1.5rem;
                        }

                        .header-title {
                            font-size: 1.5rem;
                        }

                        .info-grid {
                            grid-template-columns: 1fr;
                        }

                        .form-actions {
                            flex-direction: column;
                            gap: 0.75rem;
                        }

                        .btn {
                            justify-content: center;
                        }
                    }

                    .tooltip {
                        position: relative;
                        display: inline-block;
                        cursor: help;
                    }

                    .tooltip .tooltiptext {
                        visibility: hidden;
                        width: 200px;
                        background-color: rgba(0, 0, 0, 0.9);
                        color: #fff;
                        text-align: center;
                        border-radius: 6px;
                        padding: 5px 10px;
                        position: absolute;
                        z-index: 1;
                        bottom: 125%;
                        left: 50%;
                        margin-left: -100px;
                        opacity: 0;
                        transition: opacity 0.3s;
                        font-size: 12px;
                    }

                    .tooltip:hover .tooltiptext {
                        visibility: visible;
                        opacity: 1;
                    }

                    select optgroup {
                        font-weight: bold;
                        color: #666;
                    }

                    select option {
                        font-weight: normal;
                        padding-left: 15px;
                    }

                    /* Style cho option được chọn */
                    select option:checked {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                    }
                    .select-wrapper {
                        position: relative;
                    }

                    .select-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        color: var(--text-secondary);
                        font-size: 0.875rem;
                        z-index: 5;
                        pointer-events: none;
                    }

                    .select-wrapper {
                        position: relative;
                    }

                    .select-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        color: var(--text-secondary);
                        font-size: 0.875rem;
                        z-index: 5;
                        pointer-events: none;
                    }


                </style>
            </head>
            <body>
                <div class="main-container">
                    <div class="form-wrapper">
                        <!-- Header Section -->
                        <div class="header-section fade-in">
                            <div class="d-flex justify-content-between align-items-center">
                                <h1 class="header-title">
                                    <div class="header-icon">
                                        <i class="fas fa-fish"></i>
                                    </div>
                            <c:choose>
                                <c:when test="${product != null}">
                                    Chỉnh sửa sản phẩm
                                </c:when>
                                <c:otherwise>
                                    Thêm sản phẩm mới
                                </c:otherwise>
                            </c:choose>
                        </h1>
                        <a href="products" class="back-btn">
                            <i class="fas fa-arrow-left"></i>
                            Quay lại
                        </a>
                    </div>
                </div>

                <form action="products" method="post" onsubmit="return validateForm()" >
                    <c:if test="${product != null}">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${product.productId}">
                        
                    </c:if>
                    <c:if test="${product == null}">
                        <input type="hidden" name="action" value="insert">
                    </c:if>

                    <!-- Alert Messages -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger fade-in">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            ${errorMessage}
                        </div>
                    </c:if>

                    <!-- Basic Information Card -->
                    <div class="form-card fade-in">
                        <div class="form-card-header">
                            <i class="fas fa-info-circle"></i>
                            <h5>Thông tin cơ bản</h5>
                        </div>
                        <div class="form-card-body">
                            <div class="info-grid">
                                <div>
                                    <!-- Product Name -->
                                    <div class="form-group">
                                        <label class="form-label" for="name">
                                            Tên sản phẩm <span class="required">*</span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-tag"></i>
                                            <input type="text" class="form-control has-icon" id="name" name="name" 
                                                   value="${product.name}" required maxlength="255"
                                                   placeholder="Nhập tên sản phẩm">
                                        </div>
                                    </div>

                                   <div class="form-group">
    <label class="form-label" for="categoryId">
        Danh mục <span class="required">*</span>
    </label>
    <div class="select-wrapper">
        <i class="select-icon fas fa-layer-group"></i>
        <select class="form-control has-icon" id="categoryId" name="categoryId" required>
            <c:forEach var="category" items="${listCategory}">
                <c:choose>
                    <c:when test="${category.parentId == category.categoryId}">
                        <!-- Parent Category -->
                        <option value="${category.categoryId}" 
                                class="parent-category"
                                ${product.categoryId == category.categoryId ? 'selected' : ''}>
                            🐠  ${category.name}
                        </option>
                    </c:when>
                    <c:otherwise>
                        <!-- Child Category with Parent Name -->
                        <c:forEach var="parentCategory" items="${listCategory}">
                            <c:if test="${parentCategory.categoryId == category.parentId}">
                                <option value="${category.categoryId}" 
                                        class="child-category"
                                        ${product.categoryId == category.categoryId ? 'selected' : ''}>
                                        🐟 ${category.name} - Thuộc: ${parentCategory.name}
                                </option>
                            </c:if>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
    </div>
    <small class="form-text">Chọn danh mục phù hợp cho sản phẩm</small>
</div>



                                    <!-- SKU -->
                                    <div class="form-group">
                                        <label class="form-label" for="sku">
                                            SKU <span class="required">*</span>
                                            <span class="tooltip">
                                                <i class="fas fa-question-circle text-muted"></i>
                                                <span class="tooltiptext">Mã sản phẩm duy nhất để quản lý kho</span>
                                            </span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-barcode"></i>
                                            <input type="text" class="form-control has-icon" id="sku" name="sku" 
                                                   value="${product.sku}" required maxlength="50"
                                                   placeholder="VD: BETTA_001">
                                        </div>
                                        <small class="form-text">Mã sản phẩm duy nhất</small>
                                    </div>
                                </div>

                                <div>
                                    <!-- Price -->
                                    <div class="form-group">
                                        <label class="form-label" for="price">
                                            Giá gốc (₫) <span class="required">*</span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-dollar-sign"></i>
                                            <input type="number" class="form-control has-icon" id="price" name="price" 
                                                   value="${product.price}" required min="0" step="1000"
                                                   placeholder="0">
                                        </div>
                                    </div>

                                    <!-- Sale Price -->
                                    <div class="form-group">
                                        <label class="form-label" for="salePrice">
                                            Giá khuyến mãi (₫)
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-percentage"></i>
                                            <input type="number" class="form-control has-icon" id="salePrice" name="salePrice" 
                                                   value="${product.salePrice}" min="0" step="1000"
                                                   placeholder="0 (để trống nếu không có khuyến mãi)">
                                        </div>
                                    </div>

                                    <!-- Quantity -->
                                    <div class="form-group">
                                        <label class="form-label" for="quantity">
                                            Số lượng <span class="required">*</span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-boxes"></i>
                                            <input type="number" class="form-control has-icon" id="quantity" name="quantity" 
                                                   value="${product.quantity}" required min="0"
                                                   placeholder="0">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <!-- Status -->
                                    <div class="form-group">
                                        <label class="form-label" for="status">
                                            Trạng thái <span class="required">*</span>
                                        </label>
                                        <select class="form-control" id="status" name="status" required>
                                            <option value="active" ${product.status == 'active' ? 'selected' : ''}>
                                                🟢 Hoạt động
                                            </option>
                                            <option value="inactive" ${product.status == 'inactive' ? 'selected' : ''}>
                                                ⚫ Không hoạt động
                                            </option>
                                            <option value="out_of_stock" ${product.status == 'out_of_stock' ? 'selected' : ''}>
                                                🔴 Hết hàng
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <!-- Featured -->
                                    <div class="form-group">
                                        <label class="form-label">Tùy chọn</label>
                                        <div class="custom-checkbox">
                                            <input type="checkbox" id="featured" name="featured" 
                                                   value="true" ${product.featured ? 'checked' : ''}>
                                            <span class="checkmark"></span>
                                            <label for="featured" class="form-label mb-0">
                                                <i class="fas fa-star text-warning"></i> Sản phẩm nổi bật
                                            </label>
                                        </div>
                                        <small class="form-text">Sản phẩm nổi bật sẽ hiển thị ưu tiên</small>
                                    </div>
                                </div>
                            </div>

                            <!-- Descriptions -->
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label" for="shortDescription">Mô tả ngắn</label>
                                        <textarea class="form-control" id="shortDescription" name="shortDescription" 
                                                  rows="3" maxlength="255" placeholder="Mô tả ngắn gọn về sản phẩm">${product.shortDescription}</textarea>
                                        <small class="form-text">Tối đa 255 ký tự</small>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label" for="description">Mô tả chi tiết</label>
                                        <textarea class="form-control" id="description" name="description" 
                                                  rows="3" placeholder="Mô tả chi tiết về sản phẩm, cách chăm sóc, đặc điểm...">${product.description}</textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Fish Details Card -->
                    <div class="form-card fade-in">
    <div class="form-card-header">
        <i class="fas fa-fish"></i>
        <h5>Thông tin chi tiết cá cảnh</h5>
    </div>
    <div class="form-card-body">
        <c:set var="hasProductDetail" value="${productDetail != null}" />
        
        <div class="info-grid">
            <div>
                <div class="form-group">
                    <label class="form-label" for="origin">Xuất xứ</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-globe"></i>
                        <input type="text" class="form-control has-icon" id="origin" name="origin" 
                               value="${hasProductDetail ? productDetail.origin : ''}"
                               placeholder="VD: Việt Nam, Thái Lan">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="size">Kích thước</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-ruler"></i>
                        <input type="text" class="form-control has-icon" id="size" name="size" 
                               value="${hasProductDetail ? productDetail.size : ''}"
                               placeholder="VD: 5-7cm">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="waterTemperature">Nhiệt độ nước</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-thermometer-half"></i>
                        <input type="text" class="form-control has-icon" id="waterTemperature" name="waterTemperature" 
                               value="${hasProductDetail ? productDetail.waterTemperature : ''}"
                               placeholder="VD: 24-28°C">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="waterPh">Độ pH nước</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-flask"></i>
                        <input type="text" class="form-control has-icon" id="waterPh" name="waterPh" 
                               value="${hasProductDetail ? productDetail.waterPh : ''}"
                               placeholder="VD: 6.5-7.5">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="lifespan">Tuổi thọ</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-clock"></i>
                        <input type="text" class="form-control has-icon" id="lifespan" name="lifespan" 
                               value="${hasProductDetail ? productDetail.lifespan : ''}"
                               placeholder="VD: 2-3 năm">
                    </div>
                </div>
            </div>

            <div>
                <div class="form-group">
                    <label class="form-label" for="scientificName">Tên khoa học</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-microscope"></i>
                        <input type="text" class="form-control has-icon" id="scientificName" name="scientificName" 
                               value="${hasProductDetail ? productDetail.scientificName : ''}"
                               placeholder="VD: Betta splendens">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="commonName">Tên thông thường</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-fish"></i>
                        <input type="text" class="form-control has-icon" id="commonName" name="commonName" 
                               value="${hasProductDetail ? productDetail.commonName : ''}"
                               placeholder="VD: Cá betta">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="waterType">Loại nước</label>
                    <select class="form-control" id="waterType" name="waterType">
                        <option value="" ${hasProductDetail && productDetail.waterType == null ? 'selected' : ''}>-- Chọn loại nước --</option>
                        <option value="freshwater" ${hasProductDetail && productDetail.waterType == 'freshwater' ? 'selected' : ''}>🌊 Nước ngọt</option>
                        <option value="saltwater" ${hasProductDetail && productDetail.waterType == 'saltwater' ? 'selected' : ''}>🌊 Nước mặn</option>
                        <option value="brackish" ${hasProductDetail && productDetail.waterType == 'brackish' ? 'selected' : ''}>🌊 Nước lợ</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="careLevel">Mức độ chăm sóc</label>
                    <select class="form-control" id="careLevel" name="careLevel">
                        <option value="" ${hasProductDetail && productDetail.careLevel == null ? 'selected' : ''}>-- Chọn mức độ chăm sóc --</option>
                        <option value="easy" ${hasProductDetail && productDetail.careLevel == 'easy' ? 'selected' : ''}>🟢 Dễ</option>
                        <option value="moderate" ${hasProductDetail && productDetail.careLevel == 'moderate' ? 'selected' : ''}>🟡 Trung bình</option>
                        <option value="difficult" ${hasProductDetail && productDetail.careLevel == 'difficult' ? 'selected' : ''}>🔴 Khó</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="breedingDifficulty">Độ khó nhân giống</label>
                    <select class="form-control" id="breedingDifficulty" name="breedingDifficulty">
                        <option value="" ${hasProductDetail && productDetail.breedingDifficulty == null ? 'selected' : ''}>-- Chọn độ khó nhân giống --</option>
                        <option value="easy" ${hasProductDetail && productDetail.breedingDifficulty == 'easy' ? 'selected' : ''}>🟢 Dễ</option>
                        <option value="moderate" ${hasProductDetail && productDetail.breedingDifficulty == 'moderate' ? 'selected' : ''}>🟡 Trung bình</option>
                        <option value="difficult" ${hasProductDetail && productDetail.breedingDifficulty == 'difficult' ? 'selected' : ''}>🔴 Khó</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label" for="diet">Chế độ ăn</label>
                    <textarea class="form-control" id="diet" name="diet" 
                              rows="3" placeholder="Mô tả chế độ ăn, loại thức ăn phù hợp...">${hasProductDetail ? productDetail.diet : ''}</textarea>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label" for="compatibility">Đặc tính/Tương thích</label>
                    <textarea class="form-control" id="compatibility" name="compatibility" 
                              rows="3" placeholder="Mô tả đặc tính, khả năng tương thích với các loài khác...">${hasProductDetail ? productDetail.compatibility : ''}</textarea>
                </div>
            </div>
        </div>
    </div>
</div>
         
<!-- Product Attributes Card -->
<c:forEach var="attr" items="${listProductAttribute}">
    <c:set var="matchedValue" value="" />
    <c:set var="matchedValueId" value="" />
    <c:forEach var="value" items="${listProductAttributeValueByPID}">
        <c:if test="${value.attributeId == attr.attributeId}">
            <c:set var="matchedValue" value="${value.value}" />
            <c:set var="matchedValueId" value="${value.valueId}" />
        </c:if>
    </c:forEach>

    <tr data-attribute-value-id="${matchedValueId}">
        <td><label for="attr_${attr.attributeId}" class="mb-0">${attr.name}</label></td>
        <td>
            <div class="input-group">
                <input type="hidden" name="attributeIds[]" value="${attr.attributeId}">
                <input type="text"
                       class="form-control"
                       id="attr_${attr.attributeId}"
                       name="attributeValues[${attr.attributeId}]"
                       placeholder="Nhập ${attr.name}"
                       value="${matchedValue}">
            </div>
        </td>
        <td>
            <c:if test="${not empty matchedValueId}">
                <button type="button" 
                        class="btn btn-danger btn-sm delete-attribute-value" 
                        onclick="deleteAttributeValue(${matchedValueId}, '${attr.name}')"
                        title="Xóa giá trị thuộc tính">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </c:if>
        </td>
    </tr>
</c:forEach>

                    <!-- Image Management Card -->
                    <div class="form-card fade-in">
                        <div class="form-card-header">
                            <i class="fas fa-images"></i>
                            <h5>Quản lý hình ảnh</h5>
                        </div>
                        <div class="form-card-body">
                            <!-- Current Images -->
                            <c:if test="${not empty productImages}">
                                <div class="mb-4">
                                    <h6 class="text-muted mb-3">
                                        <i class="fas fa-image"></i> Hình ảnh hiện tại
                                    </h6>

                                    <!-- Main Image -->
                                    <div class="row mb-4">
                                        <c:forEach var="image" items="${productImages}">
                                            <c:if test="${image.isMain == 1}">
                                                <div class="col-md-6 mb-3">
                                                    <div class="image-preview">
                                                        <img src="${image.imageUrl}" alt="Ảnh chính">
                                                        <div class="main-image-badge">
                                                            <i class="fas fa-star"></i> Ảnh chính
                                                        </div>
                                                        <button type="button" class="image-remove" onclick="removeImage(${image.imageId})">
                                                            <i class="fas fa-times"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                    <h6 class="text-muted mb-3">
                                        <i class="fas fa-image"></i> ảnh phụ
                                    </h6>
                                    <!-- Additional Images -->
                                    <div class="row">
                                        <c:forEach var="image" items="${productImages}">
                                            <c:if test="${image.isMain != 1}">
                                                <div class="col-md-3 col-6 mb-3">
                                                    <div class="image-preview">
                                                        <img src="${image.imageUrl}" alt="Ảnh phụ">
                                                        <button type="button" class="image-remove" onclick="removeImage(${image.imageId})">
                                                            <i class="fas fa-times"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>
                            

                            <!-- Upload New Images -->
                            <div class="upload-section">
                                <h6 class="text-muted mb-3">
                                    <i class="fas fa-upload"></i> Thêm hình ảnh mới
                                </h6>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Ảnh chính</label>
                                        <div class="image-upload-area" id="mainImageArea">
                                            <i class="fas fa-cloud-upload-alt fa-2x text-muted mb-2"></i>
                                            <p class="text-muted mb-2">Kéo thả hoặc click để chọn ảnh chính</p>
                                            <input type="file" class="d-none" id="mainImage" name="mainImage" accept="image/*">
                                            <button type="button" class="btn btn-secondary btn-sm" onclick="document.getElementById('mainImage').click()">
                                                <i class="fas fa-folder-open"></i> Chọn file
                                            </button>
                                        </div>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Ảnh phụ</label>
                                        <div class="image-upload-area" id="additionalImagesArea">
                                            <i class="fas fa-images fa-2x text-muted mb-2"></i>
                                            <p class="text-muted mb-2">Có thể chọn nhiều ảnh</p>
                                            <input type="file" class="d-none" id="additionalImages" name="additionalImages" accept="image/*" multiple>
                                            <button type="button" class="btn btn-secondary btn-sm" onclick="document.getElementById('additionalImages').click()">
                                                <i class="fas fa-folder-open"></i> Chọn files
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Image Preview -->
                            <div id="imagePreview" class="mt-4">
                                <div class="row" id="previewContainer"></div>
                            </div>
                        </div>
                    </div>
                    

                    <!-- Form Actions -->
                    <div class="form-card fade-in">
                        <div class="form-actions">
                            <a href="products" class="btn btn-secondary">
                                <i class="fas fa-times"></i>
                                Hủy bỏ
                            </a>
                            <button type="submit" class="btn btn-primary" id="submitBtn">
                                <i class="fas fa-save"></i>
                                <c:choose>
                                    <c:when test="${product != null}">
                                        Cập nhật sản phẩm
                                    </c:when>
                                    <c:otherwise>
                                        Thêm sản phẩm
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script>
    function deleteAttributeValue(valueId, attributeName) {
        // Tìm button được click và từ đó tìm row cha
        const button = event.target.closest('button');
        const row = button.closest('tr');
        const input = row.querySelector('input[type="text"]');
        
        // Set value thành "null"
        input.value = '';
        input.classList.add('text-muted');
        
        // Xóa nút delete
        button.remove();
    }
</script>
        <script>
                                                // Form validation
                                                function validateForm() {
                                                    let isValid = true;
                                                    const submitBtn = document.getElementById('submitBtn');
                                                    const originalText = submitBtn.innerHTML;

                                                    // Show loading state
                                                    submitBtn.innerHTML = '<span class="loading-spinner"></span> Đang xử lý...';
                                                    submitBtn.disabled = true;

                                                    // Clear previous validation states
                                                    document.querySelectorAll('.form-control').forEach(field => {
                                                        field.classList.remove('is-invalid', 'is-valid');
                                                    });

                                                    // Validate required fields
                                                    const requiredFields = ['name', 'categoryId', 'sku', 'price', 'quantity', 'status', 'origin', 'size'];

                                                    requiredFields.forEach(fieldId => {
                                                        const field = document.getElementById(fieldId);
                                                        if (!field.value.trim()) {
                                                            field.classList.add('is-invalid');
                                                            isValid = false;
                                                        } else {
                                                            field.classList.add('is-valid');
                                                        }
                                                    });

                                                    // Validate price
                                                    const price = parseFloat(document.getElementById('price').value);
                                                    const salePrice = parseFloat(document.getElementById('salePrice').value);

                                                    if (price < 0) {
                                                        document.getElementById('price').classList.add('is-invalid');
                                                        showNotification('Giá gốc phải lớn hơn hoặc bằng 0!', 'danger');
                                                        isValid = false;
                                                    }

                                                    if (salePrice && salePrice >= price) {
                                                        document.getElementById('salePrice').classList.add('is-invalid');
                                                        showNotification('Giá khuyến mãi phải nhỏ hơn giá gốc!', 'danger');
                                                        isValid = false;
                                                    }

                                                    // Validate quantity
                                                    const quantity = parseInt(document.getElementById('quantity').value);
                                                    if (quantity < 0) {
                                                        document.getElementById('quantity').classList.add('is-invalid');
                                                        showNotification('Số lượng phải lớn hơn hoặc bằng 0!', 'danger');
                                                        isValid = false;
                                                    }

                                                    if (!isValid) {
                                                        // Reset button state
                                                        submitBtn.innerHTML = originalText;
                                                        submitBtn.disabled = false;
                                                        showNotification('Vui lòng kiểm tra lại các thông tin bắt buộc!', 'danger');
                                                    }

                                                    return isValid;
                                                }

                                                // Real-time validation
                                                document.getElementById('salePrice').addEventListener('blur', function () {
                                                    const price = parseFloat(document.getElementById('price').value);
                                                    const salePrice = parseFloat(this.value);

                                                    if (salePrice && price && salePrice >= price) {
                                                        this.classList.add('is-invalid');
                                                        showNotification('Giá khuyến mãi phải nhỏ hơn giá gốc!', 'warning');
                                                    } else {
                                                        this.classList.remove('is-invalid');
                                                        if (this.value)
                                                            this.classList.add('is-valid');
                                                    }
                                                });

                                                // Format number inputs
                                                ['price', 'salePrice'].forEach(id => {
                                                    document.getElementById(id).addEventListener('blur', function () {
                                                        if (this.value) {
                                                            this.value = parseInt(this.value).toLocaleString('vi-VN');
                                                        }
                                                    });

                                                    document.getElementById(id).addEventListener('focus', function () {
                                                        this.value = this.value.replace(/[^\d]/g, '');
                                                    });
                                                });

                                                // Image upload handling
                                                function setupImageUpload() {
                                                    const mainImageInput = document.getElementById('mainImage');
                                                    const additionalImagesInput = document.getElementById('additionalImages');

                                                    mainImageInput.addEventListener('change', function () {
                                                        previewImages(this, 'main');
                                                    });

                                                    additionalImagesInput.addEventListener('change', function () {
                                                        previewImages(this, 'additional');
                                                    });

                                                    // Drag and drop functionality
                                                    ['mainImageArea', 'additionalImagesArea'].forEach(areaId => {
                                                        const area = document.getElementById(areaId);

                                                        area.addEventListener('dragover', function (e) {
                                                            e.preventDefault();
                                                            this.classList.add('dragover');
                                                        });

                                                        area.addEventListener('dragleave', function (e) {
                                                            e.preventDefault();
                                                            this.classList.remove('dragover');
                                                        });

                                                        area.addEventListener('drop', function (e) {
                                                            e.preventDefault();
                                                            this.classList.remove('dragover');

                                                            const files = e.dataTransfer.files;
                                                            const input = areaId === 'mainImageArea' ? mainImageInput : additionalImagesInput;
                                                            input.files = files;

                                                            previewImages(input, areaId === 'mainImageArea' ? 'main' : 'additional');
                                                        });
                                                    });
                                                }

                                                function previewImages(input, type) {
                                                    const previewContainer = document.getElementById('previewContainer');

                                                    if (input.files && input.files.length > 0) {
                                                        Array.from(input.files).forEach((file, index) => {
                                                            const reader = new FileReader();
                                                            reader.onload = function (e) {
                                                                const isMain = type === 'main';
                                                                const preview = document.createElement('div');
                                                                preview.className = 'col-md-3 col-6 mb-3';
                                                                preview.innerHTML = `
                                <div class="image-preview">
                                    <img src="${e.target.result}" alt="Preview">
            ${isMain ? '<div class="main-image-badge"><i class="fas fa-star"></i> Ảnh chính</div>' : ''}
                                    <button type="button" class="image-remove preview-remove">
                                        <i class="fas fa-times"></i>
                                    </button>
                                </div>
                            `;
                                                                previewContainer.appendChild(preview);
                                                            };
                                                            reader.readAsDataURL(file);
                                                        });
                                                    }
                                                }

                                                // Remove preview images
                                                document.addEventListener('click', function (e) {
                                                    if (e.target.closest('.preview-remove')) {
                                                        e.target.closest('.col-md-3').remove();
                                                    }
                                                });

                                                // Remove existing images
                                                function removeImage(imageId) {
                                                    if (confirm('Bạn có chắc chắn muốn xóa ảnh này?')) {
                                                        // Show loading state
                                                        const button = event.target.closest('button');
                                                        button.innerHTML = '<span class="loading-spinner"></span>';

                                                        // Simulate API call - replace with actual implementation
                                                        setTimeout(() => {
                                                            button.closest('.col-md-3, .col-md-6').remove();
                                                            showNotification('Đã xóa ảnh thành công!', 'success');
                                                        }, 1000);

                                                        // Actual implementation would be:
                                                        /*
                                                         fetch('products?action=deleteImage', {
                                                         method: 'POST',
                                                         headers: {
                                                         'Content-Type': 'application/json',
                                                         },
                                                         body: JSON.stringify({ imageId: imageId })
                                                         })
                                                         .then(response => response.json())
                                                         .then(data => {
                                                         if (data.success) {
                                                         button.closest('.col-md-3, .col-md-6').remove();
                                                         showNotification('Đã xóa ảnh thành công!', 'success');
                                                         } else {
                                                         showNotification('Có lỗi xảy ra khi xóa ảnh!', 'danger');
                                                         }
                                                         })
                                                         .catch(error => {
                                                         showNotification('Có lỗi xảy ra!', 'danger');
                                                         });
                                                         */
                                                    }
                                                }

                                                // Notification system
                                                function showNotification(message, type = 'info') {
                                                    const notification = document.createElement('div');
                                                    notification.className = `alert alert-${type} alert-dismissible fade show`;
                                                    notification.style.cssText = `
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    z-index: 1050;
                    min-width: 300px;
                    animation: slideInRight 0.3s ease;
                `;

                                                    let icon = 'info-circle';
                                                    if (type === 'success') {
                                                        icon = 'check-circle';
                                                    } else if (type === 'danger') {
                                                        icon = 'exclamation-circle';
                                                    }

                                                    notification.innerHTML = `
            <i class="fas fa-${icon}"></i>
            ${message}
            <button type="button" class="close" data-dismiss="alert">&times;</button>
        `;

                                                    document.body.appendChild(notification);

                                                    setTimeout(() => {
                                                        if (notification.parentNode) {
                                                            notification.remove();
                                                        }
                                                    }, 5000);
                                                }

                                                // Initialize everything when DOM is ready
                                                document.addEventListener('DOMContentLoaded', function () {
                                                    setupImageUpload();

                                                    // Add smooth scrolling for form sections
                                                    document.querySelectorAll('.form-card').forEach((card, index) => {
                                                        card.style.animationDelay = `${index * 0.1}s`;
                                                    });
                                                });

                                                // Add CSS for animations
                                                const style = document.createElement('style');
                                                style.textContent = `
                @keyframes slideInRight {
                    from {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    to {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }
            `;
                                                document.head.appendChild(style);
        </script>
    </body>
</html>