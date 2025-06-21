<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://cdn.datatables.net/1.11.3/css/dataTables.bootstrap5.min.css" rel="stylesheet">    <style>
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .table th {
            background-color: #f8f9fa;
            border-top: none;
        }
        .btn-group .btn {
            margin-right: 5px;
        }
        .status-badge {
            font-size: 0.875rem;
        }
    </style>
</head>
<body>    <div class="container-fluid">
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
                            <i class="bi bi-plus-circle"></i> Thêm danh mục
                        </button>
                    </div>
                </div>

                <!-- Success/Error Messages -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${sessionScope.successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${sessionScope.errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>

                <!-- Debug section - có thể xóa sau khi fix xong -->
                <c:if test="${param.debug eq '1'}">
                    <div class="card mb-4">
                        <div class="card-header">
                            Debug Information
                        </div>
                        <div class="card-body">
                            <h5>Categories Data:</h5>
                            <p>Size: ${not empty categories ? fn:length(categories) : 'null'}</p>
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>isActive() Value</th>
                                        <th>active Property</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cat" items="${categories}">
                                        <tr>
                                            <td>${cat.categoryId}</td>
                                            <td>${cat.categoryName}</td>
                                            <td>${cat.isActive()}</td>
                                            <td>${cat.active}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>

                <!-- Categories Table -->
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="categoriesTable" class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên danh mục</th>
                                        <th>Slug</th>
                                        <th>Mô tả</th>
                                        <th>Trạng thái</th>
                                        <th>Ngày tạo</th>
                                        <th>Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="category" items="${categories}">
                                        <tr>
                                            <td>${category.categoryId}</td>
                                            <td><strong>${fn:escapeXml(category.categoryName)}</strong></td>
                                            <td><code>${fn:escapeXml(category.slug)}</code></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty category.description}">
                                                        ${fn:length(category.description) > 50 ? 
                                                          fn:substring(fn:escapeXml(category.description), 0, 50).concat('...') : 
                                                          fn:escapeXml(category.description)}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <em class="text-muted">Không có mô tả</em>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>                                            <td>
                                                <c:choose>
                                                    <c:when test="${category.isActive()}">
                                                        <span class="badge bg-success status-badge">Đang hoạt động</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary status-badge">Đã ẩn</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty category.createdAt}">
                                                        ${category.createdAt}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <em class="text-muted">N/A</em>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>                                            <td>
                                                <div class="btn-group" role="group">                                                    <button type="button" class="btn btn-sm btn-outline-primary edit-btn" 
                                                            data-id="${category.categoryId}"
                                                            data-name="${category.categoryName}"
                                                            data-slug="${category.slug}"
                                                            data-description="${category.description}"
                                                            data-active="${category.isActive()}">
                                                        <i class="bi bi-pencil"></i>
                                                    </button>
                                                      <c:choose>
                                                        <c:when test="${category.isActive()}">
                                                            <button type="button" class="btn btn-sm btn-outline-warning toggle-btn" 
                                                                    data-id="${category.categoryId}" data-action="deactivate">
                                                                <i class="bi bi-eye-slash"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="button" class="btn btn-sm btn-outline-success toggle-btn" 
                                                                    data-id="${category.categoryId}" data-action="activate">
                                                                <i class="bi bi-eye"></i>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    
                                                    <button type="button" class="btn btn-sm btn-outline-danger delete-btn" 
                                                            data-id="${category.categoryId}"
                                                            data-name="${category.categoryName}">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Category Modal -->
    <div class="modal fade" id="categoryModal" tabindex="-1" aria-labelledby="categoryModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="categoryModalLabel">Thêm danh mục</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="categoryForm" method="post" action="${pageContext.request.contextPath}/admin-category-action">
                    <div class="modal-body">
                        <input type="hidden" name="action" id="categoryAction" value="create">
                        <input type="hidden" name="categoryId" id="categoryId">
                        
                        <div class="mb-3">
                            <label for="categoryName" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="slug" class="form-label">Slug</label>
                            <input type="text" class="form-control" id="slug" name="slug">
                            <div class="form-text">Để trống để tự động tạo từ tên danh mục</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </div>
                        
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="isActive" name="isActive" value="true" checked>
                            <label class="form-check-label" for="isActive">
                                Kích hoạt danh mục
                            </label>
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

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.3/js/dataTables.bootstrap5.min.js"></script>    <script>
        $(document).ready(function() {
            // Initialize DataTable
            $('#categoriesTable').DataTable({
                "pageLength": 25,
                "order": [[ 0, "desc" ]],
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
                }
            });

            // Auto-generate slug from category name
            $('#categoryName').on('input', function() {
                const name = $(this).val();
                const slug = generateSlug(name);
                $('#slug').val(slug);
            });

            // Edit button click handler
            $('.edit-btn').on('click', function() {
                const id = $(this).data('id');
                const name = $(this).data('name');
                const slug = $(this).data('slug');
                const description = $(this).data('description');
                const isActive = $(this).data('active');
                
                editCategory(id, name, slug, description, isActive);
            });

            // Toggle button click handler
            $('.toggle-btn').on('click', function() {
                const categoryId = $(this).data('id');
                const action = $(this).data('action');
                toggleCategoryStatus(categoryId, action);
            });

            // Delete button click handler
            $('.delete-btn').on('click', function() {
                const categoryId = $(this).data('id');
                const categoryName = $(this).data('name');
                deleteCategory(categoryId, categoryName);
            });
        });

        function editCategory(id, name, slug, description, isActive) {
            $('#categoryId').val(id);
            $('#categoryName').val(name || '');
            $('#slug').val(slug || '');
            $('#description').val(description || '');
            $('#isActive').prop('checked', isActive);
            
            $('#categoryAction').val('update');
            $('#categoryModalLabel').text('Chỉnh sửa danh mục');
            $('#categorySubmitBtn').text('Cập nhật danh mục');
            
            $('#categoryModal').modal('show');
        }

        function toggleCategoryStatus(categoryId, action) {
            const actionText = action === 'activate' ? 'kích hoạt' : 'ẩn';
            
            if (confirm('Bạn có chắc chắn muốn ' + actionText + ' danh mục này?')) {
                const form = document.createElement('form');
                form.method = 'post';
                form.action = '${pageContext.request.contextPath}/admin-category-action';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = action;
                
                const idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'categoryId';
                idInput.value = categoryId;
                
                form.appendChild(actionInput);
                form.appendChild(idInput);
                document.body.appendChild(form);
                form.submit();
            }
        }

        function deleteCategory(categoryId, categoryName) {
            if (confirm('Bạn có chắc chắn muốn xóa danh mục "' + categoryName + '"?\\n\\nLưu ý: Việc xóa danh mục có thể ảnh hưởng đến các bài viết liên quan.')) {
                const form = document.createElement('form');
                form.method = 'post';
                form.action = '${pageContext.request.contextPath}/admin-category-action';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                
                const idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'categoryId';
                idInput.value = categoryId;
                
                form.appendChild(actionInput);
                form.appendChild(idInput);
                document.body.appendChild(form);
                form.submit();
            }
        }

        // Reset modal when closed
        $('#categoryModal').on('hidden.bs.modal', function () {
            $('#categoryForm')[0].reset();
            $('#categoryId').val('');
            $('#categoryAction').val('create');
            $('#categoryModalLabel').text('Thêm danh mục');
            $('#categorySubmitBtn').text('Thêm danh mục');
        });

        function generateSlug(text) {
            if (!text) return '';
            
            return text.toLowerCase()
                .trim()
                // Replace Vietnamese characters
                .replace(/[áàảãạăắằẳẵặâấầẩẫậ]/g, 'a')
                .replace(/[éèẻẽẹêếềểễệ]/g, 'e')
                .replace(/[íìỉĩị]/g, 'i')
                .replace(/[óòỏõọôốồổỗộơớờởỡợ]/g, 'o')
                .replace(/[úùủũụưứừửữự]/g, 'u')
                .replace(/[ýỳỷỹỵ]/g, 'y')
                .replace(/đ/g, 'd')
                // Remove special characters
                .replace(/[^a-z0-9\\s-]/g, '')
                // Replace spaces with hyphens
                .replace(/\\s+/g, '-')
                // Remove multiple hyphens
                .replace(/-+/g, '-')
                // Remove leading/trailing hyphens
                .replace(/^-|-$/g, '');
        }
    </script>
</body>
</html>
