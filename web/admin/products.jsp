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
            .container-fluid {
                margin-top: 20px;
            }
            .btn-action {
                margin-right: 5px;
            }
            .table th {
                background-color: #f8f9fa;
            }
            .status-active {
                color: #28a745;
            }
            .status-inactive {
                color: #dc3545;
            }
            .status-out-of-stock {
                color: #ffc107;
            }
            .featured-badge {
                background-color: #17a2b8;
                color: white;
                padding: 2px 6px;
                border-radius: 3px;
                font-size: 0.8em;
            }
            .alert {
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="row">
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
                                                <td>${product.productId}</td>
                                                <td>
                                                    <strong>${product.name}</strong>
                                                    <br>
                                                    <small class="text-muted">${product.shortDescription}</small>
                                                </td>
                                                <td>
                                                    <c:forEach var="category" items="${listCategory}">
                                                        <c:if test="${category.categoryId == product.categoryId}">
                                                            <c:forEach var="parentCategory" items="${listCategory}">
                                                                <c:if test="${parentCategory.categoryId == category.parentId}">
                                                                    ${parentCategory.name}
                                                                </c:if>
                                                            </c:forEach>
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td><code>${product.sku}</code></td>
                                                <td>
                                                    <fmt:formatNumber value="${product.price}" type="currency" 
                                                                      currencySymbol="₫" pattern="#,##0 ₫"/>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${product.salePrice != null}">
                                                            <span class="text-danger">
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
                                                    <span class="badge ${product.quantity > 0 ? 'badge-success' : 'badge-danger'}">
                                                        ${product.quantity}
                                                    </span>
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
                                                            <i class="fas fa-star"></i> Nổi bật
                                                        </span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${product.createdAt}
                                                </td>
                                                <td>
                                                    <a href="products?action=view&id=${product.productId}" 
                                                       class="btn btn-info btn-sm btn-action" title="Xem chi tiết">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <a href="products?action=edit&id=${product.productId}" 
                                                       class="btn btn-warning btn-sm btn-action" title="Sửa">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:choose>
                                                        <c:when test="${product.isDeleted == 0}">
                                                            <a href="products?action=delete&id=${product.productId}" class="btn btn-sm btn-danger" 
                                                               onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?')">
                                                                Xóa  
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="products?action=delete&id=${product.productId}" class="btn btn-sm btn-success" 
                                                               onclick="return confirm('Bạn có chắc chắn muốn khôi phục sản phẩm này không?')">
                                                                Khôi phục
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        <div id="toggleDelete${product.productId}" class="modal fade">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="products?action=delete" method="POST" >
                                                        <div class="modal-header">						
                                                            <h4 class="modal-title">Delete Product</h4>
                                                            <button type="button" class="close" data-dismiss="modal">&times;"></button>
                                                        </div>
                                                        <div class="modal-body">					
                                                            <p>Are you sure you want to delete this product?</p>
                                                            <p class="text-warning"><small>This action cannot be undone.</small></p>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <input type="hidden" name="pid" value="${product.productId}">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-danger">Delete</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Empty State -->
                            <c:if test="${empty listProduct}">
                                <div class="text-center py-5">
                                    <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">Chưa có sản phẩm nào</h5>
                                    <p class="text-muted">Hãy thêm sản phẩm đầu tiên của bạn!</p>
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