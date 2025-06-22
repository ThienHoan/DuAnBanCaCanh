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
    <title>Đổi mật khẩu - Fish Shop</title>
    <meta name="description" content="Thay đổi mật khẩu bảo mật tài khoản tại Fish Shop">
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
        body {
            font-family: 'Roboto', Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
            line-height: 1.6;
        }
        
        .change-password-container {
            max-width: 600px;
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
        
        .security-tips {
            background: #e7f3ff;
            border: 1px solid #b3d7ff;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
        }
        
        .security-tips h3 {
            color: #0066cc;
            margin-bottom: 15px;
            font-size: 1.2rem;
        }
        
        .security-tips ul {
            margin: 0;
            padding-left: 20px;
        }
        
        .security-tips li {
            margin-bottom: 8px;
            color: #333;
        }
        
        .form-group {
            margin-bottom: 25px;
            position: relative;
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
        
        .password-input-group {
            position: relative;
        }
        
        .form-control {
            width: 100%;
            padding: 12px 50px 12px 16px;
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
        
        .password-toggle {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #6c757d;
            cursor: pointer;
            font-size: 1.1rem;
        }
        
        .password-toggle:hover {
            color: #73814B;
        }
        
        .password-strength {
            margin-top: 10px;
            padding: 10px;
            border-radius: 5px;
            display: none;
        }
        
        .strength-weak {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .strength-medium {
            background: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }
        
        .strength-strong {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .form-help {
            font-size: 0.875rem;
            color: #6c757d;
            margin-top: 5px;
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
            color: white;
        }
        
        .btn-primary:hover {
            
            transform: translateY(-2px);
        }
        
        .btn-primary:disabled {
            background: #ccc;
            cursor: not-allowed;
            transform: none;
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
        
        .password-requirements {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 15px;
            margin-top: 10px;
        }
        
        .requirement {
            display: flex;
            align-items: center;
            margin-bottom: 5px;
            font-size: 0.875rem;
        }
        
        .requirement:last-child {
            margin-bottom: 0;
        }
        
        .requirement i {
            margin-right: 8px;
            width: 16px;
        }
        
        .requirement.valid {
            color: #28a745;
        }
        
        .requirement.invalid {
            color: #dc3545;
        }
        
        @media (max-width: 768px) {
            .change-password-container {
                padding: 10px;
            }
            
            .form-body {
                padding: 20px;
            }
            
            .btn-group {
                flex-direction: column;
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
        <h1 class="page-title">Đổi mật khẩu</h1>
    </div>
    
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/profile" class="permal-link">Thông tin cá nhân</a></li>
                <li class="nav-item"><span class="current-page">Đổi mật khẩu</span></li>
            </ul>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="change-password-container">
        <div class="form-card">
            <div class="form-header">
                <h2><i class="fas fa-key"></i> Đổi mật khẩu</h2>
                <p>Thay đổi mật khẩu để bảo vệ tài khoản của bạn</p>
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
                
                <!-- Security Tips -->
                <div class="security-tips">
                    <h3><i class="fas fa-shield-alt"></i> Lời khuyên bảo mật</h3>
                    <ul>
                        <li>Sử dụng mật khẩu mạnh với ít nhất 8 ký tự</li>
                        <li>Kết hợp chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                        <li>Không sử dụng thông tin cá nhân dễ đoán</li>
                        <li>Không chia sẻ mật khẩu với bất kỳ ai</li>
                        <li>Thay đổi mật khẩu định kỳ để tăng cường bảo mật</li>
                    </ul>
                </div>
                
                <!-- Change Password Form -->
                <form action="${pageContext.request.contextPath}/profile" method="post" id="changePasswordForm">
                    <input type="hidden" name="action" value="change-password">
                    
                    <div class="form-group">
                        <label for="currentPassword" class="form-label">
                            Mật khẩu hiện tại <span class="required">*</span>
                        </label>
                        <div class="password-input-group">
                            <input type="password" 
                                   id="currentPassword" 
                                   name="currentPassword" 
                                   class="form-control" 
                                   required
                                   autocomplete="current-password">
                            <button type="button" class="password-toggle" onclick="togglePassword('currentPassword')">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                        <div class="form-help">Nhập mật khẩu bạn đang sử dụng</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="newPassword" class="form-label">
                            Mật khẩu mới <span class="required">*</span>
                        </label>
                        <div class="password-input-group">
                            <input type="password" 
                                   id="newPassword" 
                                   name="newPassword" 
                                   class="form-control" 
                                   required
                                   minlength="6"
                                   autocomplete="new-password"
                                   oninput="checkPasswordStrength()">
                            <button type="button" class="password-toggle" onclick="togglePassword('newPassword')">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                        
                        <!-- Password Strength Indicator -->
                        <div id="passwordStrength" class="password-strength"></div>
                        
                        <!-- Password Requirements -->
                        <div class="password-requirements">
                            <div class="requirement" id="req-length">
                                <i class="fas fa-times"></i>
                                Ít nhất 6 ký tự
                            </div>
                            <div class="requirement" id="req-uppercase">
                                <i class="fas fa-times"></i>
                                Ít nhất 1 chữ hoa
                            </div>
                            <div class="requirement" id="req-lowercase">
                                <i class="fas fa-times"></i>
                                Ít nhất 1 chữ thường
                            </div>
                            <div class="requirement" id="req-number">
                                <i class="fas fa-times"></i>
                                Ít nhất 1 chữ số
                            </div>
                            <div class="requirement" id="req-special">
                                <i class="fas fa-times"></i>
                                Ít nhất 1 ký tự đặc biệt (!@#$%^&*)
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword" class="form-label">
                            Xác nhận mật khẩu mới <span class="required">*</span>
                        </label>
                        <div class="password-input-group">
                            <input type="password" 
                                   id="confirmPassword" 
                                   name="confirmPassword" 
                                   class="form-control" 
                                   required
                                   autocomplete="new-password"
                                   oninput="checkPasswordMatch()">
                            <button type="button" class="password-toggle" onclick="togglePassword('confirmPassword')">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                        <div class="form-help" id="passwordMatchStatus">Nhập lại mật khẩu mới để xác nhận</div>
                    </div>
                    
                    <div class="btn-group">
                        <button type="submit" class="btn btn-primary" id="submitBtn">
                            <i class="fas fa-save"></i> Đổi mật khẩu
                        </button>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Hủy bỏ
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>    <!-- Footer -->
    <%-- <jsp:include page="../components/footer.jsp" /> --%>
    
    <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>
    <script>
        // Khởi tạo lại dropdown sau khi trang load
        $(document).ready(function() {
            if (typeof initAccountDropdown === 'function') {
                initAccountDropdown();
            }
        });
    </script>
    <script>
        // Toggle password visibility
        function togglePassword(fieldId) {
            const field = document.getElementById(fieldId);
            const toggle = field.nextElementSibling.querySelector('i');
            
            if (field.type === 'password') {
                field.type = 'text';
                toggle.className = 'fas fa-eye-slash';
            } else {
                field.type = 'password';
                toggle.className = 'fas fa-eye';
            }
        }
        
        // Check password strength
        function checkPasswordStrength() {
            const password = document.getElementById('newPassword').value;
            const strengthDiv = document.getElementById('passwordStrength');
            
            if (password.length === 0) {
                strengthDiv.style.display = 'none';
                return;
            }
            
            let score = 0;
            const requirements = {
                'req-length': password.length >= 6,
                'req-uppercase': /[A-Z]/.test(password),
                'req-lowercase': /[a-z]/.test(password),
                'req-number': /\d/.test(password),
                'req-special': /[!@#$%^&*(),.?":{}|<>]/.test(password)
            };
            
            // Update requirement indicators
            for (const [reqId, met] of Object.entries(requirements)) {
                const reqElement = document.getElementById(reqId);
                const icon = reqElement.querySelector('i');
                
                if (met) {
                    reqElement.className = 'requirement valid';
                    icon.className = 'fas fa-check';
                    score++;
                } else {
                    reqElement.className = 'requirement invalid';
                    icon.className = 'fas fa-times';
                }
            }
            
            // Show strength indicator
            strengthDiv.style.display = 'block';
            
            if (score < 3) {
                strengthDiv.className = 'password-strength strength-weak';
                strengthDiv.innerHTML = '<i class="fas fa-exclamation-triangle"></i> Mật khẩu yếu';
            } else if (score < 5) {
                strengthDiv.className = 'password-strength strength-medium';
                strengthDiv.innerHTML = '<i class="fas fa-shield-alt"></i> Mật khẩu trung bình';
            } else {
                strengthDiv.className = 'password-strength strength-strong';
                strengthDiv.innerHTML = '<i class="fas fa-check-circle"></i> Mật khẩu mạnh';
            }
            
            checkPasswordMatch();
        }
        
        // Check password match
        function checkPasswordMatch() {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const statusDiv = document.getElementById('passwordMatchStatus');
            const submitBtn = document.getElementById('submitBtn');
            
            if (confirmPassword.length === 0) {
                statusDiv.innerHTML = 'Nhập lại mật khẩu mới để xác nhận';
                statusDiv.style.color = '#6c757d';
                return;
            }
              if (newPassword === confirmPassword) {
                statusDiv.innerHTML = '<i class="fas fa-check"></i> Mật khẩu khớp';
                statusDiv.style.color = '#28a745';
                // submitBtn.disabled = false; // Comment out để luôn enable
            } else {
                statusDiv.innerHTML = '<i class="fas fa-times"></i> Mật khẩu không khớp';
                statusDiv.style.color = '#dc3545';
                // submitBtn.disabled = true; // Comment out để luôn enable
            }
        }
          // Form validation
        document.getElementById('changePasswordForm').addEventListener('submit', function(e) {
            console.log('Form submitted!');
            
            const currentPassword = document.getElementById('currentPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            console.log('Current password length:', currentPassword.length);
            console.log('New password length:', newPassword.length);
            console.log('Confirm password length:', confirmPassword.length);
            
            // Check if all fields are filled
            if (!currentPassword || !newPassword || !confirmPassword) {
                alert('Vui lòng điền đầy đủ thông tin');
                e.preventDefault();
                return;
            }
            
            // Check password length
            if (newPassword.length < 6) {
                alert('Mật khẩu mới phải có ít nhất 6 ký tự');
                e.preventDefault();
                return;
            }
            
            // Check password match
            if (newPassword !== confirmPassword) {
                alert('Xác nhận mật khẩu không khớp');
                e.preventDefault();
                return;
            }
            
            // Check if new password is different from current
            if (currentPassword === newPassword) {
                alert('Mật khẩu mới phải khác mật khẩu hiện tại');
                e.preventDefault();
                return;
            }
            
            // Show confirmation
            if (!confirm('Bạn có chắc chắn muốn đổi mật khẩu?')) {
                e.preventDefault();
                return;
            }
            
            console.log('Form validation passed, submitting...');
        });
          // Auto hide alerts
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 300);
            });
        }, 5000);
        
        // Initialize - enable submit button by default
        document.getElementById('submitBtn').disabled = false;
    </script>
</body>

</html>
