<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Kho Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #dcdce4 0%, #201c20 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
        }

        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .table-container {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }

        .header-buttons a {
            margin-left: 10px;
        }

        .badge {
            border-radius: 12px;
            padding: 5px 10px;
            font-size: 0.85rem;
        }

        .featured-badge {
            background: linear-gradient(90deg,#f7971e 0%, #ffd200 100%);
            color: white;
            padding: 5px 10px;
            border-radius: 10px;
        }
        
        .nav-tabs .nav-link {
            border: none;
            color: #555;
            font-weight: 500;
            border-radius: 0;
            padding: 10px 20px;
        }
        
        .nav-tabs .nav-link.active {
            border-bottom: 3px solid #0d6efd;
            color: #0d6efd;
            background-color: transparent;
        }
        
        .inventory-increase {
            color: #198754;
            font-weight: bold;
        }
        
        .inventory-decrease {
            color: #dc3545;
            font-weight: bold;
        }
        
        .product-filter {
            margin-bottom: 20px;
        }
        
        .import-form {
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 20px;
        }
        
        .import-form h5 {
            margin-bottom: 15px;
            color: #495057;
        }
        
        .file-upload-wrapper {
            position: relative;
            margin-bottom: 15px;
        }
        
        .file-upload-wrapper input[type="file"] {
            display: block;
            width: 100%;
            padding: 0.375rem 0.75rem;
            font-size: 1rem;
            font-weight: 400;
            line-height: 1.5;
            color: #212529;
            background-color: #fff;
            background-clip: padding-box;
            border: 1px solid #ced4da;
            border-radius: 0.25rem;
            transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
        }
        
        .help-text {
            font-size: 0.85rem;
            color: #6c757d;
            margin-top: 5px;
        }
        
        /* Sortable column styling */
        th a {
            color: white !important;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 8px 4px;
            transition: all 0.2s;
            width: 100%;
            height: 100%;
        }
        
        th a:hover {
            color: white !important;
            text-decoration: none;
            background-color: rgba(255, 255, 255, 0.1);
        }
        
        th a i {
            margin-left: 5px;
            opacity: 0.7;
            transition: all 0.2s;
            width: 10px; /* Fixed width for icons */
            text-align: center;
        }
        
        th a:hover i {
            opacity: 1;
        }
        
        /* Default sort icon */
        i.fa-sort {
            opacity: 0.5;
        }
        
        .sortable-header {
            cursor: pointer;
            white-space: nowrap;
            padding: 0 !important;
            border-bottom: 2px solid transparent;
        }
        
        /* Highlight sorted column while maintaining black background */
        th.sorted {
            background-color: #212529 !important; /* Same as table-dark */
            position: relative;
        }
        
        th.sorted a {
            background-color: rgba(255, 255, 255, 0.1); /* Subtle highlight */
        }
        
        /* Table header bottom borders - match the image exactly */
        .table-dark th {
            border-bottom: 2px solid #0d6efd; /* Blue underline for all headers */
        }
        
        /* No special underline for sorted column - keeping it consistent */
        th.sorted {
            background-color: #212529 !important; /* Same as table-dark */
        }
        
        /* Make sort icons more visible */
        th.sorted i.fa-sort-up, th.sorted i.fa-sort-down {
            opacity: 1;
            color: white;
        }
        
        /* Fix icon spacing */
        i.fa-sort, i.fa-sort-up, i.fa-sort-down {
            display: inline-block;
            width: 10px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar nếu có -->
        <jsp:include page="/admin/includes/sidebar.jsp">
            <jsp:param name="page" value="warehouse"/>
        </jsp:include>

        <!-- Nội dung chính -->
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="text-black"><i class="fas fa-warehouse"></i> Quản Lý Kho Hàng</h2>
                <div class="header-buttons">
                    <c:if test="${activeTab eq 'inventory'}">
                        <a href="${pageContext.request.contextPath}/ExportWarehouseServlet?type=inventory<c:if test="${not empty selectedProductId}">&productId=${selectedProductId}</c:if>" class="btn btn-primary">
                            <i class="fas fa-file-csv"></i> Xuất CSV (Nhật Ký)
                        </a>
                    </c:if>
                    <c:if test="${activeTab eq 'products' or empty activeTab}">
                        <a href="${pageContext.request.contextPath}/ExportWarehouseServlet?type=products" class="btn btn-primary">
                            <i class="fas fa-file-csv"></i> Xuất CSV (Sản Phẩm)
                        </a>
                    </c:if>
                </div>
            </div>

            <!-- Thông báo -->
            <c:if test="${param.success eq 'export'}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="fas fa-check-circle"></i> Báo cáo đã được tải xuống thành công!
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${param.error eq 'export'}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="fas fa-exclamation-circle"></i> Có lỗi xảy ra khi xuất báo cáo!
                    <c:if test="${not empty param.msg}">
                        <div class="mt-2 small">Chi tiết: ${param.msg}</div>
                    </c:if>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Thông báo import -->
            <c:if test="${param.success eq 'import'}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="fas fa-check-circle"></i> ${param.msg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${param.error eq 'import'}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="fas fa-exclamation-circle"></i> Có lỗi xảy ra khi nhập dữ liệu!
                    <c:if test="${not empty param.msg}">
                        <div class="mt-2 small">Chi tiết: ${param.msg}</div>
                    </c:if>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${param.warning eq 'import'}">
                <div class="alert alert-warning alert-dismissible fade show">
                    <i class="fas fa-exclamation-triangle"></i> Đã nhập ${param.successCount} bản ghi thành công với một số cảnh báo!
                    <c:if test="${not empty param.msg}">
                        <div class="mt-2 small">Chi tiết: ${param.msg}</div>
                    </c:if>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Tab Navigation -->
            <ul class="nav nav-tabs mb-4">
                <li class="nav-item">
                    <a class="nav-link ${activeTab eq 'products' or empty activeTab ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/warehouse?tab=products">
                        <i class="fas fa-boxes"></i> Danh Sách Sản Phẩm
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab eq 'inventory' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/warehouse?tab=inventory">
                        <i class="fas fa-history"></i> Nhật Ký Kho Hàng
                    </a>
                </li>
            </ul>

            <!-- Tab Content -->
            <div class="tab-content">
                <!-- Products Tab -->
                <div class="tab-pane fade ${activeTab eq 'products' or empty activeTab ? 'show active' : ''}" id="products">
            <div class="table-container">
                <c:if test="${not empty listProduct}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-dark text-center">
                                <tr>
                                    <th class="sortable-header${param.sort == 'id' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=id&order=${param.sort == 'id' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            ID
                                            <i class="fas fa-sort${param.sort == 'id' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'name' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=name&order=${param.sort == 'name' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Tên Sản Phẩm
                                            <i class="fas fa-sort${param.sort == 'name' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'category' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=category&order=${param.sort == 'category' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Danh Mục
                                            <i class="fas fa-sort${param.sort == 'category' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'sku' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=sku&order=${param.sort == 'sku' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            SKU
                                            <i class="fas fa-sort${param.sort == 'sku' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'price' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=price&order=${param.sort == 'price' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Giá
                                            <i class="fas fa-sort${param.sort == 'price' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'salePrice' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=salePrice&order=${param.sort == 'salePrice' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Khuyến Mãi
                                            <i class="fas fa-sort${param.sort == 'salePrice' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'quantity' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=quantity&order=${param.sort == 'quantity' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Số Lượng
                                            <i class="fas fa-sort${param.sort == 'quantity' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'status' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=status&order=${param.sort == 'status' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Trạng Thái
                                            <i class="fas fa-sort${param.sort == 'status' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'featured' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=featured&order=${param.sort == 'featured' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Nổi Bật
                                            <i class="fas fa-sort${param.sort == 'featured' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                    <th class="sortable-header${param.sort == 'createdAt' ? ' sorted' : ''}">
                                        <a href="?tab=products&sort=createdAt&order=${param.sort == 'createdAt' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                            Ngày Tạo
                                            <i class="fas fa-sort${param.sort == 'createdAt' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                        </a>
                                    </th>
                                </tr>
                            </thead>
                            <tbody class="text-center">
                                <c:forEach var="product" items="${listProduct}">
                                    <tr>
                                        <td><span class="badge bg-secondary">${product.productId}</span></td>
                                        <td>
                                            <strong>${product.name}</strong>
                                            <c:if test="${not empty product.shortDescription}">
                                                <div class="text-muted" style="font-size: 0.9rem;">
                                                    ${product.shortDescription}
                                                </div>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:forEach var="category" items="${listCategory}">
                                                <c:if test="${category.categoryId == product.categoryId}">
                                                    <span class="badge bg-info">${category.name}</span>
                                                    <c:forEach var="parentCategory" items="${listCategory}">
                                                        <c:if test="${parentCategory.categoryId == category.parentId}">
                                                            <span class="badge bg-light text-dark">${parentCategory.name}</span>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td><code>${product.sku}</code></td>
                                        <td><fmt:formatNumber value="${product.price}" pattern="#,##0₫"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.salePrice != null}">
                                                    <span class="text-danger fw-bold">
                                                        <fmt:formatNumber value="${product.salePrice}" pattern="#,##0₫"/>
                                                    </span>
                                                </c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.quantity > 10}">
                                                    <span class="badge bg-success">${product.quantity}</span>
                                                </c:when>
                                                <c:when test="${product.quantity > 0}">
                                                    <span class="badge bg-warning text-dark">${product.quantity}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger">${product.quantity}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.status == 'active'}">
                                                    <span class="badge bg-success"><i class="fas fa-check"></i> Hoạt động</span>
                                                </c:when>
                                                <c:when test="${product.status == 'inactive'}">
                                                    <span class="badge bg-secondary"><i class="fas fa-pause"></i> Không hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-warning"><i class="fas fa-exclamation-triangle"></i> Hết hàng</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${product.featured}">
                                                <span class="featured-badge"><i class="fas fa-star"></i></span>
                                            </c:if>
                                        </td>
                                        <td><small class="text-muted">${product.createdAt}</small></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <c:if test="${empty listProduct}">
                    <div class="text-center py-5">
                                <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
                                <h4 class="text-muted">Chưa có sản phẩm nào trong kho</h4>
                                <p class="text-muted">Hãy kiểm tra lại hệ thống hoặc thêm mới sản phẩm.</p>
                            </div>
                        </c:if>
                    </div>
                </div>
                
                <!-- Inventory Logs Tab -->
                <div class="tab-pane fade ${activeTab eq 'inventory' ? 'show active' : ''}" id="inventory">
                    <div class="table-container">
                        <!-- Import Form - Only show when explicitly requested -->
                        <c:if test="${param.showImport eq 'true'}">
                        <div class="import-form mb-4">
                            <h5><i class="fas fa-file-import"></i> Nhập Dữ Liệu Kho Hàng</h5>
                            <form action="${pageContext.request.contextPath}/ExportWarehouseServlet" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
                                <input type="hidden" name="action" value="importInventory">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="file-upload-wrapper">
                                            <label for="fileUpload" class="form-label">Chọn file để nhập dữ liệu</label>
                                            <input type="file" class="form-control" id="fileUpload" name="file" accept=".csv,.txt,.xlsx,.xls" required>
                                            <div class="help-text">
                                                <i class="fas fa-info-circle"></i> Định dạng hỗ trợ: CSV là tốt nhất (.csv). <strong>Cấu trúc file:</strong>
                                                <ul class="mb-0 ps-3 small">
                                                    <li><strong>Đúng theo thứ tự cột:</strong> ID | Sản Phẩm | SKU | SL Trước | SL Sau | Thay Đổi | Loại | Lý Do | Thời Gian</li>
                                                    <li><strong>SKU:</strong> Mã sản phẩm (bắt buộc) - ví dụ: CA01, CA02</li>
                                                    <li><strong>SL Trước:</strong> Để trống, hệ thống sẽ tự động lấy số lượng hiện tại</li>
                                                    <li><strong>SL Sau:</strong> Số lượng mới sau khi nhập/xuất - ví dụ: 95, 80</li>
                                                    <li><strong>Thay Đổi:</strong> Số lượng tăng/giảm - ví dụ: -5, +30 (sẽ dùng nếu không có SL Sau)</li>
                                                    <li><strong>Loại:</strong> "Tăng" hoặc "Giảm" (nếu không điền, sẽ tự xác định)</li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="form-text text-muted">
                                            <i class="fas fa-exclamation-triangle text-warning"></i> <strong>Lưu ý quan trọng:</strong> File CSV phải đúng định dạng với đầy đủ các cột như mẫu.
                                            <a href="${pageContext.request.contextPath}/ExportWarehouseServlet?type=template" class="text-primary">
                                                <i class="fas fa-download"></i> <strong>Tải mẫu CSV để điền</strong>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="col-md-6 d-flex align-items-end">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-upload"></i> Tải Lên và Xử Lý
                                        </button>
                                    </div>
                                </div>
                                
                                <!-- Display any errors -->
                                <c:if test="${not empty errors}">
                                    <div class="alert alert-danger mt-3">
                                        <strong>Lỗi khi nhập dữ liệu:</strong>
                                        <ul class="mb-0 mt-2">
                                            <c:forEach var="error" items="${errors}">
                                                <li>${error}</li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </c:if>
                                
                                <!-- Success message -->
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success mt-3">
                                        <i class="fas fa-check-circle"></i> ${success}
                                    </div>
                                </c:if>
                            </form>
                        </div>
                        </c:if>
                        
                        <!-- Show import button when form is hidden -->
                        <c:if test="${param.showImport ne 'true' && activeTab eq 'inventory'}">
                            <div class="mb-4 text-end">
                                <a href="${pageContext.request.contextPath}/admin/warehouse?tab=inventory&showImport=true" class="btn btn-primary">
                                    <i class="fas fa-file-import"></i> Nhập Dữ Liệu Kho Hàng
                                </a>
                            </div>
                        </c:if>
                        
                        <!-- Product Filter -->
                        <div class="product-filter">
                            <form action="${pageContext.request.contextPath}/admin/warehouse" method="get" class="row g-3 align-items-center">
                                <input type="hidden" name="tab" value="inventory">
                                <div class="col-md-6">
                                    <div class="input-group">
                                        <select name="productId" class="form-select">
                                            <option value="">Tất cả sản phẩm</option>
                                            <c:forEach var="product" items="${listProduct}">
                                                <option value="${product.productId}" ${selectedProductId eq product.productId ? 'selected' : ''}>
                                                    [${product.sku}] ${product.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-filter"></i> Lọc
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        
                        <!-- Inventory Logs Table -->
                        <c:if test="${not empty inventoryLogs}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-dark text-center">
                                        <tr>
                                            <th class="sortable-header${param.sort == 'id' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=id&order=${param.sort == 'id' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    ID
                                                    <i class="fas fa-sort${param.sort == 'id' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'name' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=name&order=${param.sort == 'name' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    Sản Phẩm
                                                    <i class="fas fa-sort${param.sort == 'name' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'sku' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=sku&order=${param.sort == 'sku' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    SKU
                                                    <i class="fas fa-sort${param.sort == 'sku' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'qtyBefore' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=qtyBefore&order=${param.sort == 'qtyBefore' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    SL Trước
                                                    <i class="fas fa-sort${param.sort == 'qtyBefore' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'qtyAfter' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=qtyAfter&order=${param.sort == 'qtyAfter' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    SL Sau
                                                    <i class="fas fa-sort${param.sort == 'qtyAfter' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'change' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=change&order=${param.sort == 'change' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    Thay Đổi
                                                    <i class="fas fa-sort${param.sort == 'change' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'type' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=type&order=${param.sort == 'type' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    Loại
                                                    <i class="fas fa-sort${param.sort == 'type' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'reason' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=reason&order=${param.sort == 'reason' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    Lý Do
                                                    <i class="fas fa-sort${param.sort == 'reason' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                            <th class="sortable-header${param.sort == 'time' ? ' sorted' : ''}">
                                                <a href="?tab=inventory&sort=time&order=${param.sort == 'time' && param.order == 'asc' ? 'desc' : 'asc'}" class="text-white">
                                                    Thời Gian
                                                    <i class="fas fa-sort${param.sort == 'time' ? param.order == 'asc' ? '-up' : '-down' : ''}"></i>
                                                </a>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="log" items="${inventoryLogs}">
                                            <tr>
                                                <td class="text-center">${log.logId}</td>
                                                <td>${log.productName}</td>
                                                <td><code>${log.productSku}</code></td>
                                                <td class="text-center">${log.quantityBefore}</td>
                                                <td class="text-center">${log.quantityAfter}</td>
                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${log.quantityAfter > log.quantityBefore}">
                                                            <span class="inventory-increase">+${log.quantityAfter - log.quantityBefore}</span>
                                                        </c:when>
                                                        <c:when test="${log.quantityAfter < log.quantityBefore}">
                                                            <span class="inventory-decrease">${log.quantityAfter - log.quantityBefore}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span>0</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${log.changeType eq 'increase'}">
                                                            <span class="badge bg-success">Tăng</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-danger">Giảm</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${log.reason}</td>
                                                <td>${log.createdAt}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                        <c:if test="${empty inventoryLogs}">
                            <div class="text-center py-5">
                                <i class="fas fa-history fa-3x text-muted mb-3"></i>
                                <h4 class="text-muted">Chưa có nhật ký kho hàng nào</h4>
                                <p class="text-muted">Nhật ký sẽ được ghi lại khi có thay đổi số lượng sản phẩm.</p>
                    </div>
                </c:if>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Highlight the sorted column
    document.addEventListener('DOMContentLoaded', function() {
        // Get current sort parameters
        const urlParams = new URLSearchParams(window.location.search);
        const sortColumn = urlParams.get('sort');
        const sortOrder = urlParams.get('order');
        const currentTab = urlParams.get('tab') || 'products';
        
        if (sortColumn) {
            // Add 'sorted' class to the sorted column header
            const sortedHeaders = document.querySelectorAll('.sortable-header');
            sortedHeaders.forEach(header => {
                const link = header.querySelector('a');
                if (link && link.href.includes(`sort=${sortColumn}`) && link.href.includes(`tab=${currentTab}`)) {
                    header.classList.add('sorted');
                    
                    // Update the icon to ensure it's visible
                    const icon = link.querySelector('i');
                    if (icon) {
                        icon.style.opacity = '1';
                    }
                }
            });
        }
        
        // Add click handlers for better UX
        document.querySelectorAll('.sortable-header a').forEach(link => {
            link.addEventListener('click', function(e) {
                // Add a visual cue on click
                const headers = document.querySelectorAll('.sortable-header');
                headers.forEach(h => h.classList.remove('sorted'));
                this.closest('th').classList.add('sorted');
                
                // Optional: add loading indicator
                document.body.style.cursor = 'wait';
            });
        });
    });
</script>
</body>
</html>
