<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục Blog - Admin Dashboard</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.7/css/dataTables.bootstrap5.min.css">
      <style>
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .btn-action {
            margin: 2px;
        }
        .modal-body .form-group {
            margin-bottom: 15px;
        }
        .error-message {
            color: #dc3545;
            font-size: 14px;
            margin-top: 5px;
        }
        
        .success-message {
            color: #28a745;
            font-size: 14px;
            margin-top: 5px;
        }
        
        .table-actions {
            white-space: nowrap;
        }
        
        .category-description {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        
        .category-status {
            font-weight: bold;
        }
        
        .status-active {
            color: #28a745;
        }
        
        .status-inactive {
            color: #dc3545;
        }    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="includes/sidebar.jsp">
                <jsp:param name="page" value="categories" />
            </jsp:include>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Quản lý danh mục Blog</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#categoryModal">
                            <i class="bi bi-plus-circle"></i> Thêm danh mục mới
                        </button>
                    </div>
                </div>
                    </button>
                </div>
            </div>
        </div>                <!-- Messages -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Categories Table -->
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="categoriesTable" class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th width="8%">ID</th>
                            <th width="20%">Tên danh mục</th>
                            <th width="15%">Slug</th>
                            <th width="30%">Mô tả</th>
                            <th width="12%">Trạng thái</th>
                            <th width="15%">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="category" items="${categories}">
                            <tr>
                                <td>${category.categoryId}</td>
                                <td><strong>${category.categoryName}</strong></td>
                                <td><code>${category.slug}</code></td>
                                <td>
                                    <span class="category-description" title="${category.description}">
                                        ${category.description}
                                    </span>
                                </td>
                                <td>
                                    <span class="category-status ${category.isActive ? 'status-active' : 'status-inactive'}">
                                        ${category.isActive ? 'Kích hoạt' : 'Ẩn'}
                                    </span>
                                </td>                                <td class="table-actions">
                                    <button type="button" class="btn btn-sm btn-outline-primary btn-action btn-edit" 
                                            data-category-id="${category.categoryId}"
                                            data-category-name="${category.categoryName}"
                                            data-category-slug="${category.slug}"
                                            data-category-description="${category.description}"
                                            data-category-active="${category.isActive}"
                                            title="Sửa">
                                        <i class="fa fa-edit"></i>
                                    </button>
                                    
                                    <c:choose>
                                        <c:when test="${category.isActive}">
                                            <button type="button" class="btn btn-sm btn-outline-warning btn-action btn-toggle" 
                                                    data-category-id="${category.categoryId}"
                                                    data-action="deactivate"
                                                    title="Ẩn">
                                                <i class="fa fa-eye-slash"></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" class="btn btn-sm btn-outline-success btn-action btn-toggle" 
                                                    data-category-id="${category.categoryId}"
                                                    data-action="activate"
                                                    title="Hiển thị">
                                                <i class="fa fa-eye"></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    <button type="button" class="btn btn-sm btn-outline-danger btn-action btn-delete" 
                                            data-category-id="${category.categoryId}"
                                            title="Xóa">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>                    </tbody>
                </table>
                        </div>
                    </div>
                </div>

    <!-- Category Modal -->
    <div class="modal fade" id="categoryModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="categoryModalTitle">Thêm danh mục mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form id="categoryForm" method="post" action="${pageContext.request.contextPath}/admin-blog-action">
                    <div class="modal-body">
                        <input type="hidden" name="action" id="categoryAction" value="create_category">
                        <input type="hidden" name="categoryId" id="categoryId" value="">
                        
                        <div class="form-group">
                            <label for="categoryName" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="categoryName" name="categoryName" 
                                   required maxlength="100" placeholder="Nhập tên danh mục">
                            <div class="error-message" id="categoryNameError"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="categorySlug" class="form-label">Slug</label>
                            <input type="text" class="form-control" id="categorySlug" name="slug" 
                                   maxlength="100" placeholder="Tự động tạo từ tên danh mục">
                            <small class="form-text text-muted">Để trống để tự động tạo từ tên danh mục</small>
                            <div class="error-message" id="categorySlugError"></div>
                        </div>
                        
                        <div class="form-group">
                            <label for="categoryDescription" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="categoryDescription" name="description" 
                                      rows="3" maxlength="500" placeholder="Nhập mô tả cho danh mục"></textarea>
                            <div class="error-message" id="categoryDescriptionError"></div>
                        </div>
                        
                        <div class="form-group">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="categoryIsActive" name="isActive" value="true" checked>
                                <label class="form-check-label" for="categoryIsActive">
                                    Kích hoạt danh mục
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary" id="categorySubmitBtn">Thêm danh mục</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Xác nhận xóa</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn xóa danh mục này không?</p>
                    <p class="text-danger"><small><strong>Lưu ý:</strong> Các bài viết thuộc danh mục này sẽ được chuyển về "Không phân loại".</small></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Xóa</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.7/js/dataTables.bootstrap5.min.js"></script>

    <script>        $(document).ready(function() {
            // Initialize DataTable
            $('#categoriesTable').DataTable({
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json'
                },
                pageLength: 25,
                order: [[0, 'asc']],
                columnDefs: [
                    { orderable: false, targets: [5] } // Actions column
                ]
            });

            // Auto-generate slug from category name
            $('#categoryName').on('input', function() {
                if (!$('#categorySlug').val() || $('#categorySlug').data('auto-generated')) {
                    const slug = generateSlug($(this).val());
                    $('#categorySlug').val(slug).data('auto-generated', true);
                }
            });

            // Mark slug as manually edited if user types in it
            $('#categorySlug').on('input', function() {
                $(this).data('auto-generated', false);
            });

            // Form validation
            $('#categoryForm').on('submit', function(e) {
                if (!validateCategoryForm()) {
                    e.preventDefault();
                    return false;
                }
            });

            // Clear form when modal is closed
            $('#categoryModal').on('hidden.bs.modal', function() {
                resetCategoryForm();
            });

            // Event handlers for action buttons
            $(document).on('click', '.btn-edit', function() {
                const categoryId = $(this).data('category-id');
                const categoryName = $(this).data('category-name');
                const categorySlug = $(this).data('category-slug');
                const categoryDescription = $(this).data('category-description');
                const categoryActive = $(this).data('category-active');
                
                editCategory(categoryId, categoryName, categorySlug, categoryDescription, categoryActive);
            });

            $(document).on('click', '.btn-toggle', function() {
                const categoryId = $(this).data('category-id');
                const action = $(this).data('action');
                const isActive = action === 'activate';
                
                toggleCategoryStatus(categoryId, isActive);
            });

            $(document).on('click', '.btn-delete', function() {
                const categoryId = $(this).data('category-id');
                deleteCategory(categoryId);
            });
        });

        // Generate slug from text
        function generateSlug(text) {
            return text
                .toLowerCase()
                .trim()
                .replace(/[áàảãạăắằẳẵặâấầẩẫậ]/g, 'a')
                .replace(/[éèẻẽẹêếềểễệ]/g, 'e')
                .replace(/[íìỉĩị]/g, 'i')
                .replace(/[óòỏõọôốồổỗộơớờởỡợ]/g, 'o')
                .replace(/[úùủũụưứừửữự]/g, 'u')
                .replace(/[ýỳỷỹỵ]/g, 'y')
                .replace(/đ/g, 'd')
                .replace(/[^a-z0-9\s-]/g, '')
                .replace(/\s+/g, '-')
                .replace(/-+/g, '-')
                .replace(/^-|-$/g, '');
        }

        // Validate category form
        function validateCategoryForm() {
            let isValid = true;
            
            // Clear previous errors
            $('.error-message').text('');
            
            // Category name validation
            const categoryName = $('#categoryName').val().trim();
            if (!categoryName) {
                $('#categoryNameError').text('Tên danh mục không được để trống');
                isValid = false;
            } else if (categoryName.length > 100) {
                $('#categoryNameError').text('Tên danh mục không được vượt quá 100 ký tự');
                isValid = false;
            }
            
            // Slug validation
            const slug = $('#categorySlug').val().trim();
            if (slug && slug.length > 100) {
                $('#categorySlugError').text('Slug không được vượt quá 100 ký tự');
                isValid = false;
            }
            
            // Description validation
            const description = $('#categoryDescription').val().trim();
            if (description && description.length > 500) {
                $('#categoryDescriptionError').text('Mô tả không được vượt quá 500 ký tự');
                isValid = false;
            }
            
            return isValid;
        }

        // Reset category form
        function resetCategoryForm() {
            $('#categoryForm')[0].reset();
            $('#categoryAction').val('create_category');
            $('#categoryId').val('');
            $('#categoryModalTitle').text('Thêm danh mục mới');
            $('#categorySubmitBtn').text('Thêm danh mục');
            $('.error-message').text('');
            $('#categorySlug').removeData('auto-generated');
        }        // Edit category
        function editCategory(categoryId, categoryName, categorySlug, categoryDescription, categoryActive) {
            // Populate form
            $('#categoryId').val(categoryId);
            $('#categoryName').val(categoryName);
            $('#categorySlug').val(categorySlug);
            $('#categoryDescription').val(categoryDescription);
            $('#categoryIsActive').prop('checked', categoryActive);
            
            // Update form action and modal title
            $('#categoryAction').val('update_category');
            $('#categoryModalTitle').text('Cập nhật danh mục');
            $('#categorySubmitBtn').text('Cập nhật');
            
            // Show modal
            $('#categoryModal').modal('show');
        }

        // Toggle category status
        function toggleCategoryStatus(categoryId, isActive) {
            const action = isActive ? 'activate_category' : 'deactivate_category';
            const statusText = isActive ? 'kích hoạt' : 'ẩn';
            
            if (confirm(`Bạn có chắc chắn muốn ${statusText} danh mục này không?`)) {
                // Create form and submit
                const form = $('<form>', {
                    method: 'post',
                    action: '${pageContext.request.contextPath}/admin-blog-action'
                });
                
                form.append($('<input>', { type: 'hidden', name: 'action', value: action }));
                form.append($('<input>', { type: 'hidden', name: 'categoryId', value: categoryId }));
                
                $('body').append(form);
                form.submit();
            }
        }

        // Delete category
        function deleteCategory(categoryId) {
            // Store category ID for confirmation
            $('#confirmDeleteBtn').data('category-id', categoryId);
            $('#deleteModal').modal('show');
        }

        // Handle delete confirmation
        $('#confirmDeleteBtn').on('click', function() {
            const categoryId = $(this).data('category-id');
            
            // Create form and submit
            const form = $('<form>', {
                method: 'post',
                action: '${pageContext.request.contextPath}/admin-blog-action'
            });
            
            form.append($('<input>', { type: 'hidden', name: 'action', value: 'delete_category' }));
            form.append($('<input>', { type: 'hidden', name: 'categoryId', value: categoryId }));
            
            $('body').append(form);
            form.submit();        });
    </script>
            </main>
        </div>
    </div>
</body>
</html>
