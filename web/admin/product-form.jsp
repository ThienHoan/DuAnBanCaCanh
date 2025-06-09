<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:choose><c:when test="${product != null}">Sửa Sản Phẩm</c:when><c:otherwise>Thêm Sản Phẩm Mới</c:otherwise></c:choose></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .container-fluid {
            margin-top: 20px;
        }
        .required {
            color: red;
        }
        .form-group label {
            font-weight: 600;
        }
        .card-header {
            background-color: #f8f9fa;
        }
        .btn-back {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header">
                        <div class="row">
                            <div class="col-md-6">
                                <h3 class="card-title">
                                    <i class="fas fa-fish"></i>
                                    <c:choose>
                                        <c:when test="${product != null}">
                                            Sửa Sản Phẩm
                                        </c:when>
                                        <c:otherwise>
                                            Thêm Sản Phẩm Mới
                                        </c:otherwise>
                                    </c:choose>
                                </h3>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="products" class="btn btn-secondary btn-back">
                                    <i class="fas fa-arrow-left"></i> Quay lại
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="card-body">
                        <!-- Alert Messages -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                            </div>
                        </c:if>
                        
                        <!-- Product Form -->
                        <form action="products" method="post" onsubmit="return validateForm()">
                            <c:if test="${product != null}">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id" value="${product.productId}">
                            </c:if>
                            <c:if test="${product == null}">
                                <input type="hidden" name="action" value="insert">
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <!-- Product Name -->
                                    <div class="form-group">
                                        <label for="name">Tên Sản Phẩm <span class="required">*</span></label>
                                        <input type="text" class="form-control" id="name" name="name" 
                                               value="${product.name}" required maxlength="255"
                                               placeholder="Nhập tên sản phẩm">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập tên sản phẩm!
                                        </div>
                                    </div>
                                    
                                    <!-- Category -->
                                    <div class="form-group">
                                        <label for="categoryId">Danh Mục <span class="required">*</span></label>
                                        <select class="form-control" id="categoryId" name="categoryId" required>
                                            <option value="">-- Chọn danh mục --</option>
                                            <c:forEach var="category" items="${listCategory}">
                                                <option value="${category.categoryId}" 
                                                    ${product.categoryId == category.categoryId ? 'selected' : ''}>
                                                    ${category.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <div class="invalid-feedback">
                                            Vui lòng chọn danh mục!
                                        </div>
                                    </div>
                                    
                                    <!-- SKU -->
                                    <div class="form-group">
                                        <label for="sku">SKU <span class="required">*</span></label>
                                        <input type="text" class="form-control" id="sku" name="sku" 
                                               value="${product.sku}" required maxlength="50"
                                               placeholder="Mã sản phẩm (VD: BETTA_001)">
                                        <small class="form-text text-muted">Mã sản phẩm duy nhất</small>
                                        <div class="invalid-feedback">
                                            Vui lòng nhập SKU!
                                        </div>
                                    </div>
                                    
                                    <!-- Price -->
                                    <div class="form-group">
                                        <label for="price">Giá Gốc (₫) <span class="required">*</span></label>
                                        <input type="number" class="form-control" id="price" name="price" 
                                               value="${product.price}" required min="0" step="1000"
                                               placeholder="0">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập giá hợp lệ!
                                        </div>
                                    </div>
                                    
                                    <!-- Sale Price -->
                                    <div class="form-group">
                                        <label for="salePrice">Giá Khuyến Mãi (₫)</label>
                                        <input type="number" class="form-control" id="salePrice" name="salePrice" 
                                               value="${product.salePrice}" min="0" step="1000"
                                               placeholder="0 (để trống nếu không có khuyến mãi)">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <!-- Quantity -->
                                    <div class="form-group">
                                        <label for="quantity">Số Lượng <span class="required">*</span></label>
                                        <input type="number" class="form-control" id="quantity" name="quantity" 
                                               value="${product.quantity}" required min="0"
                                               placeholder="0">
                                        <div class="invalid-feedback">
                                            Vui lòng nhập số lượng hợp lệ!
                                        </div>
                                    </div>
                                    
                                    <!-- Status -->
                                    <div class="form-group">
                                        <label for="status">Trạng Thái <span class="required">*</span></label>
                                        <select class="form-control" id="status" name="status" required>
                                            <option value="active" ${product.status == 'active' ? 'selected' : ''}>
                                                Hoạt động
                                            </option>
                                            <option value="inactive" ${product.status == 'inactive' ? 'selected' : ''}>
                                                Không hoạt động
                                            </option>
                                            <option value="out_of_stock" ${product.status == 'out_of_stock' ? 'selected' : ''}>
                                                Hết hàng
                                            </option>
                                        </select>
                                    </div>
                                    
                                    <!-- Featured -->
                                    <div class="form-group">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" id="featured" name="featured" 
                                                   value="true" ${product.featured ? 'checked' : ''}>
                                            <label class="form-check-label" for="featured">
                                                <i class="fas fa-star text-warning"></i> Sản phẩm nổi bật
                                            </label>
                                        </div>
                                        <small class="form-text text-muted">Sản phẩm nổi bật sẽ hiển thị ưu tiên</small>
                                    </div>
                                    
                                    <!-- Short Description -->
                                    <div class="form-group">
                                        <label for="shortDescription">Mô Tả Ngắn</label>
                                        <textarea class="form-control" id="shortDescription" name="shortDescription" 
                                                  rows="3" maxlength="255" placeholder="Mô tả ngắn gọn về sản phẩm">${product.shortDescription}</textarea>
                                        <small class="form-text text-muted">Tối đa 255 ký tự</small>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Full Description -->
                            <div class="form-group">
                                <label for="description">Mô Tả Chi Tiết</label>
                                <textarea class="form-control" id="description" name="description" 
                                          rows="5" placeholder="Mô tả chi tiết về sản phẩm, cách chăm sóc, đặc điểm...">${product.description}</textarea>
                            </div>
                            
                            <!-- Form Actions -->
                            <div class="form-group text-right">
                                <a href="products" class="btn btn-secondary">
                                    <i class="fas fa-times"></i> Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i>
                                    <c:choose>
                                        <c:when test="${product != null}">
                                            Cập Nhật
                                        </c:when>
                                        <c:otherwise>
                                            Thêm Mới
                                        </c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    
    <script>
        function validateForm() {
            let isValid = true;
            
            // Clear previous validation states
            $('.form-control').removeClass('is-invalid');
            
            // Validate required fields
            const requiredFields = ['name', 'categoryId', 'sku', 'price', 'quantity', 'status'];
            
            requiredFields.forEach(function(fieldId) {
                const field = document.getElementById(fieldId);
                if (!field.value.trim()) {
                    field.classList.add('is-invalid');
                    isValid = false;
                }
            });
            
            // Validate price
            const price = parseFloat(document.getElementById('price').value);
            const salePrice = parseFloat(document.getElementById('salePrice').value);
            
            if (price < 0) {
                document.getElementById('price').classList.add('is-invalid');
                alert('Giá gốc phải lớn hơn hoặc bằng 0!');
                isValid = false;
            }
            
            if (salePrice && salePrice >= price) {
                document.getElementById('salePrice').classList.add('is-invalid');
                alert('Giá khuyến mãi phải nhỏ hơn giá gốc!');
                isValid = false;
            }
            
            // Validate quantity
            const quantity = parseInt(document.getElementById('quantity').value);
            if (quantity < 0) {
                document.getElementById('quantity').classList.add('is-invalid');
                alert('Số lượng phải lớn hơn hoặc bằng 0!');
                isValid = false;
            }
            
            return isValid;
        }
        
        // Real-time validation
        document.getElementById('salePrice').addEventListener('blur', function() {
            const price = parseFloat(document.getElementById('price').value);
            const salePrice = parseFloat(this.value);
            
            if (salePrice && price && salePrice >= price) {
                this.classList.add('is-invalid');
                alert('Giá khuyến mãi phải nhỏ hơn giá gốc!');
            } else {
                this.classList.remove('is-invalid');
            }
        });
        
        // Format number inputs
        document.getElementById('price').addEventListener('blur', function() {
            if (this.value) {
                this.value = parseInt(this.value);
            }
        });
        
        document.getElementById('salePrice').addEventListener('blur', function() {
            if (this.value) {
                this.value = parseInt(this.value);
            }
        });
    </script>
</body>
</html>