<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <!-- SEO Meta Tags -->
    <title>Chỉnh sửa thông tin - Fish Shop</title>
    <meta name="description" content="Cập nhật thông tin cá nhân tại Fish Shop">
    <meta name="robots" content="noindex, nofollow">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico">      <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main-color.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar-picker.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <!-- Custom CSS -->
    <style>
        .edit-profile-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .form-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .form-header {
            background: linear-gradient(135deg, #8bc8ec, #0038a8);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .form-header h2 {
            margin: 0;
            font-size: 2rem;
        }
        
        .form-header p {
            margin: 10px 0 0;
            opacity: 0.9;
        }
        
        .form-body {
            padding: 40px;
        }
          .avatar-section {
            text-align: center;
            margin-bottom: 30px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
        }
        
        .current-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            margin: 0 auto 20px;
            border: 3px solid  #0038a8;
            object-fit: cover;
            background: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
            color: #73814B;
            overflow: hidden;
        }
        
        .avatar-options {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin: 20px 0;
            flex-wrap: wrap;
        }
        
        .avatar-option-card {
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            padding: 20px;
            width: 200px;
            cursor: pointer;
            transition: all 0.3s ease;
            background: white;
        }
        
        .avatar-option-card:hover {
            border-color: #0038a8;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0,56,168,0.1);
        }
        
        .avatar-option-card.active {
            border-color: #0038a8;
            background: #f8fafe;
        }
        
        .avatar-option-icon {
            font-size: 2rem;
            color: #0038a8;
            margin-bottom: 10px;
        }
        
        .avatar-option-title {
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }
        
        .avatar-option-desc {
            font-size: 0.85rem;
            color: #666;
        }
        
        .avatar-upload-section {
            display: none;
            margin: 20px 0;
            padding: 20px;
            border: 2px dashed #ddd;
            border-radius: 10px;
            background: #fafafa;
        }
        
        .avatar-upload-section.active {
            display: block;
        }
        
        .avatar-url-section {
            display: none;
            margin: 20px 0;
            padding: 20px;
            border: 2px dashed #ddd;
            border-radius: 10px;
            background: #fafafa;
        }
        
        .avatar-url-section.active {
            display: block;
        }
        
        .avatar-upload {
            position: relative;
            display: inline-block;
        }
        
        .avatar-upload input[type=file] {
            position: absolute;
            left: -9999px;
        }
        
        .avatar-upload-btn {
            background: linear-gradient(135deg, #8bc8ec, #0038a8);
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            border: none;
        }
        
        .avatar-upload-btn:hover {
            background: linear-gradient(135deg, #8bc8ec, #0038a8);
            transform: translateY(-2px);
        }
        
        .url-input-group {
            display: flex;
            gap: 10px;
            align-items: stretch;
            margin-bottom: 15px;
        }
        
        .url-input {
            flex: 1;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 14px;
        }
        
        .url-input:focus {
            border-color: #0038a8;
            outline: none;
        }
        
        .preview-url-btn {
            padding: 12px 20px;
            background: #17a2b8;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 500;
            transition: background 0.3s ease;
        }
        
        .preview-url-btn:hover {
            background: #138496;
        }
        
        .avatar-preview {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            margin: 15px auto;
            border: 2px solid #ddd;
            overflow: hidden;
            display: none;
            background: #f8f9fa;
        }
        
        .avatar-preview img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .upload-info {
            font-size: 0.85rem;
            color: #666;
            margin-top: 10px;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        
        .required {
            color: #dc3545;
        }
        
        .form-control {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s ease;
            background: white;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #73814B;
            box-shadow: 0 0 0 3px rgba(115, 129, 75, 0.1);
        }
        
        .form-control:invalid {
            border-color: #dc3545;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        
        .btn-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 25px;
            font-size: 1rem;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #8bc8ec, #0038a8);
            color: white;
        }
        
        .btn-primary:hover {
            background: linear-gradient(135deg, #8bc8ec, #0038a8);
            transform: translateY(-2px);
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        
        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }
        
        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 25px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .form-help {
            font-size: 0.875rem;
            color: #6c757d;
            margin-top: 5px;
        }
        
        .loading {
            display: none;
            text-align: center;
            padding: 20px;
        }
        
        .loading-spinner {
            width: 40px;
            height: 40px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid #73814B;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin: 0 auto 10px;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        @media (max-width: 768px) {
            .edit-profile-container {
                padding: 10px;
            }
            
            .form-body {
                padding: 20px;
            }
            
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .btn-group {
                flex-direction: column;
            }
            
            .current-avatar {
                width: 80px;
                height: 80px;
                font-size: 32px;
            }
        }    </style>
</head>

<body>
    <!-- Load jQuery first để header có thể sử dụng -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- Header -->
    <jsp:include page="../../header.jsp"></jsp:include>
    
    <!-- Breadcrumb -->
    <div class="hero-section hero-background">
        <h1 class="page-title">Chỉnh sửa thông tin</h1>
    </div>
    
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/profile" class="permal-link">Thông tin cá nhân</a></li>
                <li class="nav-item"><span class="current-page">Chỉnh sửa</span></li>
            </ul>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="edit-profile-container">
        <div class="form-card">
            <div class="form-header">
                <h2><i class="fas fa-user-edit"></i> Chỉnh sửa thông tin cá nhân</h2>
                <p>Cập nhật thông tin của bạn để có trải nghiệm tốt hơn</p>
            </div>
            
            <div class="form-body">
                <!-- Alert Messages -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle"></i> ${success}
                    </div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>
                  <!-- Avatar Section -->
                <div class="avatar-section">
                    <h3><i class="fas fa-camera"></i> Ảnh đại diện</h3>
                    
                    <div class="current-avatar" id="currentAvatar">
                        <c:choose>
                            <c:when test="${not empty user.avatar}">
                                <c:choose>
                                    <c:when test="${fn:startsWith(user.avatar, 'http')}">
                                        <!-- Google avatar URL -->
                                        <img src="${user.avatar}" alt="Avatar" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Local avatar file -->
                                        <img src="${pageContext.request.contextPath}/uploads/avatars/${user.avatar}" alt="Avatar" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-user"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <!-- Avatar Options -->
                    <div class="avatar-options">
                        <div class="avatar-option-card active" onclick="selectAvatarOption('upload')">
                            <div class="avatar-option-icon">
                                <i class="fas fa-upload"></i>
                            </div>
                            <div class="avatar-option-title">Upload từ máy</div>
                            <div class="avatar-option-desc">Chọn ảnh từ thiết bị của bạn</div>
                        </div>
                        
                        <div class="avatar-option-card" onclick="selectAvatarOption('url')">
                            <div class="avatar-option-icon">
                                <i class="fas fa-link"></i>
                            </div>
                            <div class="avatar-option-title">Từ URL</div>
                            <div class="avatar-option-desc">Nhập đường dẫn ảnh từ internet</div>
                        </div>
                    </div>
                      <!-- Upload Section -->
                    <div class="avatar-section-content active" id="uploadSection">
                        <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data" id="avatarUploadForm">
                            <input type="hidden" name="action" value="update-avatar">
                            <input type="hidden" name="avatarType" value="upload">
                            
                            <div class="upload-drop-zone" onclick="document.getElementById('avatar').click();">
                                <div class="upload-icon">
                                    <i class="fas fa-cloud-upload-alt"></i>
                                </div>
                                <div class="upload-text">Kéo thả file ảnh vào đây</div>
                                <div class="upload-subtext">hoặc click để chọn file</div>
                            </div>
                            
                            <input type="file" id="avatar" name="avatar" accept="image/*" onchange="previewUploadAvatar(this)" style="display: none;">
                            
                            <div class="upload-info">
                                <i class="fas fa-info-circle"></i> Chấp nhận file JPG, PNG, GIF, WEBP. Tối đa 10MB.
                            </div>
                            
                            <div class="avatar-preview-container">
                                <div class="avatar-preview" id="uploadPreview"></div>
                                <button type="submit" class="submit-btn" id="uploadSubmitBtn">
                                    <i class="fas fa-save"></i> Lưu ảnh đại diện
                                </button>
                            </div>
                        </form>
                    </div>
                    
                    <!-- URL Section -->
                    <div class="avatar-section-content" id="urlSection">
                        <form action="${pageContext.request.contextPath}/profile" method="post" id="avatarUrlForm">
                            <input type="hidden" name="action" value="update-avatar">
                            <input type="hidden" name="avatarType" value="url">
                            
                            <div class="url-input-container">
                                <div class="url-input-with-button">
                                    <input type="url" 
                                           id="avatarUrl" 
                                           name="avatarUrl" 
                                           class="avatar-url-input" 
                                           placeholder="Nhập URL ảnh (ví dụ: https://example.com/avatar.jpg)"
                                           oninput="validateUrl(this.value)">
                                    <button type="button" class="url-preview-btn" onclick="previewUrlAvatar()">
                                        <i class="fas fa-eye"></i> Xem trước
                                    </button>
                                </div>
                            </div>
                            
                            <div class="upload-info">
                                <i class="fas fa-link"></i> Nhập đường dẫn đầy đủ đến ảnh từ internet
                            </div>
                            
                            <div class="avatar-preview-container">
                                <div class="avatar-preview" id="urlPreview"></div>
                                <button type="submit" class="submit-btn" id="urlSubmitBtn">
                                    <i class="fas fa-save"></i> Lưu ảnh đại diện
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Profile Form -->
                <form action="${pageContext.request.contextPath}/profile" method="post" id="profileForm">
                    <input type="hidden" name="action" value="update-profile">
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="fullName" class="form-label">
                                Họ và tên <span class="required">*</span>
                            </label>
                            <input type="text" 
                                   id="fullName" 
                                   name="fullName" 
                                   class="form-control" 
                                   value="${user.fullName}"
                                   required
                                   maxlength="100">
                            <div class="form-help">Tên đầy đủ của bạn</div>
                        </div>
                        
                        <div class="form-group">
                            <label for="email" class="form-label">
                                Email <span class="required">*</span>
                            </label>
                            <input type="email" 
                                   id="email" 
                                   name="email" 
                                   class="form-control" 
                                   value="${user.email}"
                                   required
                                   maxlength="100">
                            <div class="form-help">Email đăng nhập và liên hệ</div>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="phone" class="form-label">Số điện thoại</label>
                            <input type="tel" 
                                   id="phone" 
                                   name="phone" 
                                   class="form-control" 
                                   value="${user.phone}"
                                   pattern="[0-9]{10,11}"
                                   maxlength="15">
                            <div class="form-help">Số điện thoại liên hệ (10-11 số)</div>
                        </div>
                        
                        <div class="form-group">
                            <label for="username" class="form-label">Tên đăng nhập</label>
                            <input type="text" 
                                   id="username" 
                                   name="username" 
                                   class="form-control" 
                                   value="${user.username}"
                                   disabled>
                            <div class="form-help">Tên đăng nhập không thể thay đổi</div>
                        </div>
                    </div>
                    
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Lưu thay đổi
                        </button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Hủy bỏ
                        </a>
                    </div>
                </form>
                
                <!-- Loading -->
                <div class="loading" id="loading">
                    <div class="loading-spinner"></div>
                    <p>Đang xử lý...</p>
                </div>
            </div>
        </div>
    </div>      <!-- Footer -->
    <%-- <jsp:include page="../components/footer.jsp" /> --%>
    
    <!-- Load Bootstrap JS sau header -->
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    
    <!-- Load profile.js để có initAccountDropdown function -->
    <script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>
      <!-- Scripts -->
    <script>        // Avatar option selection
        function selectAvatarOption(type) {
            // Remove active class from all cards
            document.querySelectorAll('.avatar-option-card').forEach(card => {
                card.classList.remove('active');
            });
            
            // Hide all sections
            document.getElementById('uploadSection').classList.remove('active');
            document.getElementById('urlSection').classList.remove('active');
            
            // Clear any error/success messages
            document.querySelectorAll('.error-message, .success-message').forEach(el => el.remove());
            
            if (type === 'upload') {
                // Activate upload option
                document.querySelector('.avatar-option-card:first-child').classList.add('active');
                document.getElementById('uploadSection').classList.add('active');
                
                // Reset URL form
                document.getElementById('avatarUrl').value = '';
                document.getElementById('urlPreview').classList.remove('show');
                document.getElementById('urlSubmitBtn').classList.remove('show');
                
                // Setup drag and drop
                setupDragDrop();
            } else if (type === 'url') {
                // Activate URL option
                document.querySelector('.avatar-option-card:last-child').classList.add('active');
                document.getElementById('urlSection').classList.add('active');
                
                // Reset upload form
                document.getElementById('avatar').value = '';
                document.getElementById('uploadPreview').classList.remove('show');
                document.getElementById('uploadSubmitBtn').classList.remove('show');
            }
        }
        
        // Initialize page
        $(document).ready(function() {
            // Đảm bảo các script đã load xong trước khi gọi initAccountDropdown
            setTimeout(function() {
                if (typeof initAccountDropdown === 'function') {
                    initAccountDropdown();
                }
            }, 100);
            
            // Setup drag and drop for default upload option
            setupDragDrop();
        });
          // Preview uploaded avatar
        function previewUploadAvatar(input) {
            if (input.files && input.files[0]) {
                const file = input.files[0];
                
                if (!validateUploadFile(file)) {
                    input.value = '';
                    return;
                }
                
                const reader = new FileReader();
                reader.onload = function(e) {
                    const preview = document.getElementById('uploadPreview');
                    preview.innerHTML = '<img src="' + e.target.result + '" alt="Preview">';
                    preview.classList.add('show');
                    
                    const submitBtn = document.getElementById('uploadSubmitBtn');
                    submitBtn.classList.add('show');
                    
                    // Update current avatar display
                    updateCurrentAvatar(e.target.result);
                };
                reader.readAsDataURL(file);
            }
        }
        
        // Validate upload file
        function validateUploadFile(file) {
            // Validate file size (10MB)
            if (file.size > 10 * 1024 * 1024) {
                showError('File quá lớn. Vui lòng chọn file nhỏ hơn 10MB.');
                return false;
            }
            
            // Validate file type
            const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
            if (!allowedTypes.includes(file.type)) {
                showError('Chỉ chấp nhận file ảnh (JPG, PNG, GIF, WEBP).');
                return false;
            }
            
            return true;
        }
        
        // Setup drag and drop
        function setupDragDrop() {
            const dropZone = document.querySelector('.upload-drop-zone');
            if (!dropZone) return;
            
            ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
                dropZone.addEventListener(eventName, preventDefaults, false);
            });
            
            function preventDefaults(e) {
                e.preventDefault();
                e.stopPropagation();
            }
            
            ['dragenter', 'dragover'].forEach(eventName => {
                dropZone.addEventListener(eventName, highlight, false);
            });
            
            ['dragleave', 'drop'].forEach(eventName => {
                dropZone.addEventListener(eventName, unhighlight, false);
            });
            
            function highlight(e) {
                dropZone.classList.add('dragover');
            }
            
            function unhighlight(e) {
                dropZone.classList.remove('dragover');
            }
            
            dropZone.addEventListener('drop', handleDrop, false);
            
            function handleDrop(e) {
                const dt = e.dataTransfer;
                const files = dt.files;
                
                if (files.length > 0) {
                    const fileInput = document.getElementById('avatar');
                    fileInput.files = files;
                    previewUploadAvatar(fileInput);
                }
            }
        }
        
        // Show error message
        function showError(message) {
            // Remove existing error messages
            document.querySelectorAll('.error-message').forEach(el => el.remove());
            
            const errorDiv = document.createElement('div');
            errorDiv.className = 'error-message';
            errorDiv.textContent = message;
            
            const activeSection = document.querySelector('.avatar-section-content.active');
            if (activeSection) {
                activeSection.insertBefore(errorDiv, activeSection.firstChild);
            }
        }
        
        // Show success message
        function showSuccess(message) {
            // Remove existing messages
            document.querySelectorAll('.error-message, .success-message').forEach(el => el.remove());
            
            const successDiv = document.createElement('div');
            successDiv.className = 'success-message';
            successDiv.textContent = message;
            
            const activeSection = document.querySelector('.avatar-section-content.active');
            if (activeSection) {
                activeSection.insertBefore(successDiv, activeSection.firstChild);
            }
        }
        
        // Validate and preview URL avatar
        function validateUrl(url) {
            const urlSubmitBtn = document.getElementById('urlSubmitBtn');
            if (url.trim() === '') {
                urlSubmitBtn.style.display = 'none';
                document.getElementById('urlPreview').style.display = 'none';
            }
        }
          function previewUrlAvatar() {
            const url = document.getElementById('avatarUrl').value.trim();
            
            if (!url) {
                showError('Vui lòng nhập URL ảnh.');
                return;
            }
            
            // Validate URL format
            try {
                new URL(url);
            } catch (e) {
                showError('URL không hợp lệ. Vui lòng nhập URL đầy đủ.');
                return;
            }
            
            // Show loading
            const loadingSpinner = document.createElement('div');
            loadingSpinner.className = 'loading-spinner show';
            document.getElementById('urlPreview').appendChild(loadingSpinner);
            
            // Create image to test if URL is valid
            const img = new Image();
            img.onload = function() {
                loadingSpinner.remove();
                const preview = document.getElementById('urlPreview');
                preview.innerHTML = '<img src="' + url + '" alt="Preview">';
                preview.classList.add('show');
                
                const submitBtn = document.getElementById('urlSubmitBtn');
                submitBtn.classList.add('show');
                
                // Update current avatar display
                updateCurrentAvatar(url);
                showSuccess('Ảnh hợp lệ! Bạn có thể lưu để cập nhật avatar.');
            };
            
            img.onerror = function() {
                loadingSpinner.remove();
                showError('Không thể tải ảnh từ URL này. Vui lòng kiểm tra lại.');
                document.getElementById('urlPreview').classList.remove('show');
                document.getElementById('urlSubmitBtn').classList.remove('show');
            };
            
            img.src = url;
        }
        
        // Update current avatar display
        function updateCurrentAvatar(src) {
            const currentAvatar = document.getElementById('currentAvatar');
            currentAvatar.innerHTML = '<img src="' + src + '" alt="Avatar" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">';
        }
        
        // Form submissions with loading
        document.getElementById('avatarUploadForm').addEventListener('submit', function() {
            showLoading();
        });
        
        document.getElementById('avatarUrlForm').addEventListener('submit', function() {
            showLoading();
        });
        
        // Form validation for profile
        document.getElementById('profileForm').addEventListener('submit', function(e) {
            const fullName = document.getElementById('fullName').value.trim();
            const email = document.getElementById('email').value.trim();
            const phone = document.getElementById('phone').value.trim();
            
            // Validate full name
            if (fullName.length < 2) {
                alert('Họ tên phải có ít nhất 2 ký tự');
                e.preventDefault();
                return;
            }
            
            // Validate email
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailPattern.test(email)) {
                alert('Email không hợp lệ');
                e.preventDefault();
                return;
            }
            
            // Validate phone (if provided)
            if (phone && !/^[0-9]{10,11}$/.test(phone)) {
                alert('Số điện thoại phải có 10-11 chữ số');
                e.preventDefault();
                return;
            }
            
            // Show loading
            showLoading();
        });
        
        function showLoading() {
            document.getElementById('loading').style.display = 'block';
            document.querySelector('.form-body').style.opacity = '0.5';
        }
        
        // Auto hide alerts
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 300);
            });
        }, 5000);
        
        // Phone number formatting
        document.getElementById('phone').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 11) {
                value = value.substring(0, 11);
            }
            e.target.value = value;
        });
        
        // Real-time validation feedback
        document.getElementById('email').addEventListener('blur', function() {
            const email = this.value.trim();
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            
            if (email && !emailPattern.test(email)) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#e9ecef';
            }
        });
        
        document.getElementById('fullName').addEventListener('blur', function() {
            const fullName = this.value.trim();
            
            if (fullName.length < 2) {
                this.style.borderColor = '#dc3545';
            } else {
                this.style.borderColor = '#e9ecef';
            }
        });
    </script>
</body>

</html>
