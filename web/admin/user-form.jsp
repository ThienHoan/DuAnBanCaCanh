<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${createMode}">Thêm người dùng mới</c:when>
            <c:when test="${editMode}">Chỉnh sửa người dùng</c:when>
            <c:when test="${resetPasswordMode}">Đặt lại mật khẩu</c:when>
            <c:otherwise>Chi tiết người dùng</c:otherwise>
        </c:choose>
        - Fish Shop Admin
    </title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
      <style>
        .main-content {
            background-color: #f8f9fa;
            min-height: 100vh;
        }
        .user-avatar-preview {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #dee2e6;
        }
        .form-label.required::after {
            content: " *";
            color: red;
        }
    </style>
</head>
<body>    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="includes/sidebar.jsp">
                <jsp:param name="page" value="users" />
            </jsp:include>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">
                        <c:choose>
                            <c:when test="${createMode}">
                                <i class="fas fa-user-plus me-2"></i>Thêm người dùng mới
                            </c:when>
                            <c:when test="${editMode}">
                                <i class="fas fa-user-edit me-2"></i>Chỉnh sửa người dùng
                            </c:when>
                            <c:when test="${resetPasswordMode}">
                                <i class="fas fa-key me-2"></i>Đặt lại mật khẩu
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-user me-2"></i>Chi tiết người dùng
                            </c:otherwise>
                        </c:choose>
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/admin-users" class="btn btn-outline-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Quay lại
                        </a>
                    </div>
                </div>                <!-- Success/Error Messages -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>
                
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>
                
                <!-- Fallback for request scope messages -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle me-2"></i>${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- User Form -->
                <div class="row">
                    <div class="col-lg-8">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title mb-0">
                                    <c:choose>
                                        <c:when test="${createMode}">Thông tin người dùng mới</c:when>
                                        <c:when test="${editMode}">Chỉnh sửa thông tin</c:when>
                                        <c:when test="${resetPasswordMode}">Đặt lại mật khẩu</c:when>
                                        <c:otherwise>Thông tin chi tiết</c:otherwise>
                                    </c:choose>
                                </h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${resetPasswordMode}">
                                        <!-- Reset Password Form -->
                                        <form method="POST" action="${pageContext.request.contextPath}/admin-users">
                                            <input type="hidden" name="action" value="resetPassword">
                                            <input type="hidden" name="userId" value="${user.userId}">
                                            
                                            <div class="mb-3">
                                                <label class="form-label">Người dùng</label>
                                                <input type="text" class="form-control" value="${user.fullName} (${user.username})" readonly>
                                            </div>
                                            
                                            <div class="mb-3">
                                                <label for="newPassword" class="form-label required">Mật khẩu mới</label>
                                                <input type="password" class="form-control" id="newPassword" name="newPassword" required minlength="6">
                                                <div class="form-text">Mật khẩu phải có ít nhất 6 ký tự</div>
                                            </div>
                                            
                                            <div class="mb-3">
                                                <label for="confirmPassword" class="form-label required">Xác nhận mật khẩu</label>
                                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                            </div>
                                            
                                            <div class="d-flex justify-content-end">
                                                <a href="${pageContext.request.contextPath}/admin-users" class="btn btn-secondary me-2">Hủy</a>
                                                <button type="submit" class="btn btn-primary">Đặt lại mật khẩu</button>
                                            </div>
                                        </form>
                                    </c:when>
                                    
                                    <c:when test="${viewMode}">
                                        <!-- View Mode -->
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">ID người dùng</label>
                                                    <p class="form-control-plaintext">${user.userId}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Tên đăng nhập</label>
                                                    <p class="form-control-plaintext">
                                                        ${user.username}
                                                        <c:if test="${not empty user.googleId}">
                                                            <i class="fab fa-google text-danger ms-2" title="Tài khoản Google"></i>
                                                        </c:if>
                                                    </p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Email</label>
                                                    <p class="form-control-plaintext">${user.email}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Số điện thoại</label>
                                                    <p class="form-control-plaintext">${not empty user.phone ? user.phone : 'Chưa cập nhật'}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Họ tên</label>
                                                    <p class="form-control-plaintext">${user.fullName}</p>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Vai trò</label>
                                                    <p class="form-control-plaintext">
                                                        <span class="badge ${user.role == 'admin' ? 'bg-danger' : 'bg-primary'}">
                                                            ${user.role == 'admin' ? 'Admin' : 'Customer'}
                                                        </span>
                                                    </p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Trạng thái</label>
                                                    <p class="form-control-plaintext">
                                                        <span class="badge ${user.status == 'active' ? 'bg-success' : 'bg-warning'}">
                                                            ${user.status == 'active' ? 'Hoạt động' : 'Không hoạt động'}
                                                        </span>
                                                    </p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Ngày tạo</label>
                                                    <p class="form-control-plaintext">
                                                        <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                    </p>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold">Lần đăng nhập cuối</label>
                                                    <p class="form-control-plaintext">
                                                        <c:choose>
                                                            <c:when test="${not empty user.lastLogin}">
                                                                <fmt:formatDate value="${user.lastLogin}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted">Chưa đăng nhập</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="d-flex justify-content-end">
                                            <a href="${pageContext.request.contextPath}/admin-users?action=edit&id=${user.userId}" class="btn btn-primary me-2">
                                                <i class="fas fa-edit me-1"></i>Chỉnh sửa
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin-users?action=resetPassword&id=${user.userId}" class="btn btn-warning">
                                                <i class="fas fa-key me-1"></i>Đặt lại mật khẩu
                                            </a>
                                        </div>
                                    </c:when>
                                    
                                    <c:otherwise>                                        <!-- Create/Edit Form -->
                                        <form method="POST" action="${pageContext.request.contextPath}/admin-users" id="userForm" enctype="multipart/form-data">
                                            <input type="hidden" name="action" value="${createMode ? 'create' : 'update'}">
                                            <c:if test="${editMode}">
                                                <input type="hidden" name="userId" value="${user.userId}">
                                            </c:if>
                                              <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="username" class="form-label required">Tên đăng nhập</label>
                                                        <input type="text" class="form-control" id="username" name="username" 
                                                               value="${createMode ? '' : user.username}" required ${editMode ? 'readonly' : ''}>
                                                        <c:if test="${editMode}">
                                                            <div class="form-text">Tên đăng nhập không thể thay đổi</div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="email" class="form-label required">Email</label>
                                                        <input type="email" class="form-control" id="email" name="email" 
                                                               value="${createMode ? '' : user.email}" required>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <c:if test="${createMode}">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="password" class="form-label required">Mật khẩu</label>
                                                            <input type="password" class="form-control" id="password" name="password" required minlength="6">
                                                            <div class="form-text">Mật khẩu phải có ít nhất 6 ký tự</div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="confirmPassword" class="form-label required">Xác nhận mật khẩu</label>
                                                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:if>
                                              <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="fullName" class="form-label required">Họ tên</label>
                                                        <input type="text" class="form-control" id="fullName" name="fullName" 
                                                               value="${createMode ? '' : user.fullName}" required>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="phone" class="form-label">Số điện thoại</label>
                                                        <input type="tel" class="form-control" id="phone" name="phone" 
                                                               value="${createMode ? '' : user.phone}">
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="role" class="form-label">Vai trò</label>                                                        <select class="form-select" id="role" name="role">
                                                            <option value="customer" ${user.role == 'customer' || (createMode && (user.role == null || user.role == '')) ? 'selected' : ''}>Customer</option>
                                                            <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="status" class="form-label">Trạng thái</label>                                                        <select class="form-select" id="status" name="status">
                                                            <option value="active" ${user.status == 'active' || (createMode && (user.status == null || user.status == '')) ? 'selected' : ''}>Hoạt động</option>
                                                            <option value="inactive" ${user.status == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>                                              <div class="mb-3">
                                                <label class="form-label">Avatar</label>
                                                
                                                <!-- Radio buttons để chọn loại avatar -->
                                                <div class="mb-2">
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" name="avatarType" id="avatarTypeUrl" value="url" checked onchange="toggleAvatarInput()">
                                                        <label class="form-check-label" for="avatarTypeUrl">URL Avatar</label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" name="avatarType" id="avatarTypeFile" value="file" onchange="toggleAvatarInput()">
                                                        <label class="form-check-label" for="avatarTypeFile">Upload File</label>
                                                    </div>
                                                </div>
                                                
                                                <!-- URL Avatar Input -->
                                                <div id="avatarUrlInput">
                                                    <input type="url" class="form-control" id="avatar" name="avatar" 
                                                           value="${createMode ? '' : user.avatar}" onchange="previewAvatar()">
                                                    <div class="form-text">Nhập URL của hình ảnh avatar</div>
                                                </div>
                                                
                                                <!-- File Upload Input -->
                                                <div id="avatarFileInput" style="display: none;">
                                                    <input type="file" class="form-control" id="avatarFile" name="avatarFile" 
                                                           accept="image/*" onchange="previewAvatarFile()">
                                                    <div class="form-text">Chọn file ảnh avatar (JPG, PNG, GIF). Tối đa 5MB.</div>
                                                </div>
                                            </div>
                                            
                                            <div class="d-flex justify-content-end">
                                                <a href="${pageContext.request.contextPath}/admin-users" class="btn btn-secondary me-2">Hủy</a>
                                                <button type="submit" class="btn btn-primary">
                                                    <c:choose>
                                                        <c:when test="${createMode}">
                                                            <i class="fas fa-plus me-1"></i>Tạo người dùng
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="fas fa-save me-1"></i>Cập nhật
                                                        </c:otherwise>
                                                    </c:choose>
                                                </button>
                                            </div>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Avatar Preview -->
                    <div class="col-lg-4">
                        <div class="card">
                            <div class="card-header">
                                <h6 class="card-title mb-0">Avatar</h6>
                            </div>
                            <div class="card-body text-center">                                <c:choose>
                                    <c:when test="${not empty user.avatar}">
                                        <c:choose>
                                            <c:when test="${fn:startsWith(user.avatar, 'http')}">
                                                <!-- Avatar từ Google hoặc URL khác -->
                                                <img src="${user.avatar}" alt="Avatar" class="user-avatar-preview mb-3" id="avatarPreview">
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Avatar từ file upload local -->
                                                <img src="${pageContext.request.contextPath}/uploads/avatars/${user.avatar}" alt="Avatar" class="user-avatar-preview mb-3" id="avatarPreview">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="user-avatar-preview bg-secondary d-flex align-items-center justify-content-center mx-auto mb-3" id="avatarPreview">
                                            <i class="fas fa-user fa-3x text-white"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                
                                <c:if test="${not empty user.googleId}">
                                    <div class="alert alert-info">
                                        <i class="fab fa-google me-2"></i>
                                        Tài khoản Google
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        
                        <c:if test="${editMode or viewMode}">
                            <!-- User Actions -->
                            <div class="card mt-3">
                                <div class="card-header">
                                    <h6 class="card-title mb-0">Thao tác</h6>
                                </div>
                                <div class="card-body">
                                    <div class="d-grid gap-2">                                        <c:if test="${user.userId != sessionScope.user.userId}">
                                            <c:choose>
                                                <c:when test="${user.status == 'active'}">
                                                    <button type="button" class="btn btn-outline-warning" 
                                                            onclick="updateUserStatus(<c:out value='${user.userId}'/>, 'inactive')">
                                                        <i class="fas fa-ban me-1"></i>Vô hiệu hóa
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-outline-success" 
                                                            onclick="updateUserStatus(<c:out value='${user.userId}'/>, 'active')">
                                                        <i class="fas fa-check me-1"></i>Kích hoạt
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                            
                                            <a href="${pageContext.request.contextPath}/admin-users?action=resetPassword&id=${user.userId}" 
                                               class="btn btn-outline-info">
                                                <i class="fas fa-key me-1"></i>Đặt lại mật khẩu
                                            </a>
                                            
                                            <button type="button" class="btn btn-outline-danger" 
                                                    onclick="deleteUser(<c:out value='${user.userId}'/>)">
                                                <i class="fas fa-trash me-1"></i>Xóa người dùng
                                            </button>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
      <script>
        function toggleAvatarInput() {
            const avatarTypeUrl = document.getElementById('avatarTypeUrl').checked;
            const avatarTypeFile = document.getElementById('avatarTypeFile').checked;
            const urlInput = document.getElementById('avatarUrlInput');
            const fileInput = document.getElementById('avatarFileInput');
            
            if (avatarTypeUrl) {
                urlInput.style.display = 'block';
                fileInput.style.display = 'none';
                document.getElementById('avatarFile').value = ''; // Clear file input
            } else if (avatarTypeFile) {
                urlInput.style.display = 'none';
                fileInput.style.display = 'block';
                document.getElementById('avatar').value = ''; // Clear URL input
                previewAvatar(); // Reset preview
            }
        }
        
        function previewAvatar() {
            const avatarUrl = document.getElementById('avatar').value;
            const preview = document.getElementById('avatarPreview');
            
            if (avatarUrl) {
                preview.innerHTML = '<img src="' + avatarUrl + '" alt="Avatar" class="user-avatar-preview">';
            } else {
                preview.innerHTML = '<i class="fas fa-user fa-3x text-white"></i>';
                preview.className = 'user-avatar-preview bg-secondary d-flex align-items-center justify-content-center mx-auto mb-3';
            }
        }
        
        function previewAvatarFile() {
            const fileInput = document.getElementById('avatarFile');
            const preview = document.getElementById('avatarPreview');
            
            if (fileInput.files && fileInput.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.innerHTML = '<img src="' + e.target.result + '" alt="Avatar" class="user-avatar-preview">';
                };
                reader.readAsDataURL(fileInput.files[0]);
            } else {
                preview.innerHTML = '<i class="fas fa-user fa-3x text-white"></i>';
                preview.className = 'user-avatar-preview bg-secondary d-flex align-items-center justify-content-center mx-auto mb-3';
            }
        }

        function updateUserStatus(userId, status) {
            if (confirm('Bạn có chắc chắn muốn thay đổi trạng thái người dùng này?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/admin-users';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'updateStatus';
                
                const userIdInput = document.createElement('input');
                userIdInput.type = 'hidden';
                userIdInput.name = 'userId';
                userIdInput.value = userId;
                
                const statusInput = document.createElement('input');
                statusInput.type = 'hidden';
                statusInput.name = 'status';
                statusInput.value = status;
                
                form.appendChild(actionInput);
                form.appendChild(userIdInput);
                form.appendChild(statusInput);
                
                document.body.appendChild(form);
                form.submit();
            }
        }

        function deleteUser(userId) {
            if (confirm('Bạn có chắc chắn muốn xóa người dùng này? Thao tác này có thể được hoàn tác.')) {
                window.location.href = '${pageContext.request.contextPath}/admin-users?action=delete&id=' + userId;
            }
        }

        // Validate password confirmation
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('userForm');
            if (form) {
                form.addEventListener('submit', function(e) {
                    const password = document.getElementById('password');
                    const confirmPassword = document.getElementById('confirmPassword');
                    
                    if (password && confirmPassword) {
                        if (password.value !== confirmPassword.value) {
                            e.preventDefault();
                            alert('Mật khẩu xác nhận không khớp!');
                            confirmPassword.focus();
                        }
                    }
                });
            }
            
            // Reset password form validation
            const resetForm = document.querySelector('form[action*="resetPassword"]');
            if (resetForm) {
                resetForm.addEventListener('submit', function(e) {
                    const newPassword = document.getElementById('newPassword');
                    const confirmPassword = document.getElementById('confirmPassword');
                    
                    if (newPassword.value !== confirmPassword.value) {
                        e.preventDefault();
                        alert('Mật khẩu xác nhận không khớp!');
                        confirmPassword.focus();
                    }
                });
            }
        });

        // Auto-hide alerts after 5 seconds
        setTimeout(function() {
            $('.alert').fadeOut();
        }, 5000);
    </script>
</body>
</html>
