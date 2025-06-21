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
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico">
      <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main-color.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
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
            background: linear-gradient(135deg, #73814B, #8fa05c);
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
            border: 5px solid #73814B;
            object-fit: cover;
            background: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
            color: #73814B;
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
            background: #73814B;
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            border: none;
        }
        
        .avatar-upload-btn:hover {
            background: #8fa05c;
            transform: translateY(-2px);
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
            background: #73814B;
            color: white;
        }
        
        .btn-primary:hover {
            background: #8fa05c;
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
                    
                    <div class="current-avatar">                        <c:choose>
                            <c:when test="${not empty user.avatar}">
                                <img src="${pageContext.request.contextPath}/uploads/avatars/${user.avatar}" alt="Avatar" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-user"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data" id="avatarForm">
                        <input type="hidden" name="action" value="update-avatar">
                        <div class="avatar-upload">
                            <input type="file" id="avatar" name="avatar" accept="image/*" onchange="previewAvatar(this)">
                            <label for="avatar" class="avatar-upload-btn">
                                <i class="fas fa-upload"></i> Chọn ảnh mới
                            </label>
                        </div>
                        <div class="form-help">Chấp nhận file JPG, PNG, GIF. Tối đa 10MB.</div>
                    </form>
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
    </div>
      <!-- Footer -->
    <%-- <jsp:include page="../components/footer.jsp" /> --%>
      <!-- Scripts -->
    <script>
        // Khởi tạo lại dropdown sau khi trang load
        $(document).ready(function() {
            if (typeof initAccountDropdown === 'function') {
                initAccountDropdown();
            }
        });
        
        // Preview avatar before upload
        function previewAvatar(input) {
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const avatarContainer = document.querySelector('.current-avatar');
                    avatarContainer.innerHTML = '<img src="' + e.target.result + '" alt="Preview" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">';
                };
                reader.readAsDataURL(input.files[0]);
                
                // Auto submit avatar form
                document.getElementById('avatarForm').submit();
            }
        }
        
        // Form validation
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
        
        // Avatar form loading
        document.getElementById('avatarForm').addEventListener('submit', function() {
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
