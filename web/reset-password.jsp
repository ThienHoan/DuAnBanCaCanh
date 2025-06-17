<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Đặt lại mật khẩu - Biolife</title>
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/favicon.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/nice-select.css">
    <link rel="stylesheet" href="assets/css/slick.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color04.css">
    
    <style>
        .reset-password-container {
            background: linear-gradient(135deg, #7fad39 0%, #5e8b2a 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px 0;
        }
        
        .reset-form-wrapper {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            overflow: hidden;
            max-width: 600px;
            width: 100%;
            margin: 0 auto;
        }
        
        .form-header {
            background: linear-gradient(135deg, #7fad39 0%, #5e8b2a 100%);
            color: white;
            padding: 40px 30px 30px;
            text-align: center;
        }
        
        .form-header h2 {
            margin: 0 0 10px;
            font-size: 28px;
            font-weight: 600;
        }
        
        .form-header p {
            margin: 0;
            opacity: 0.9;
            font-size: 16px;
        }
        
        .form-content {
            padding: 40px 30px;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
            font-size: 14px;
        }
        
        .form-control {
            width: 100%;
            padding: 15px 20px;
            border: 2px solid #e1e1e1;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s ease;
            background: #f8f9fa;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #7fad39;
            background: white;
            box-shadow: 0 0 0 3px rgba(127, 173, 57, 0.1);
        }
        
        .form-row {
            display: flex;
            gap: 15px;
        }
        
        .form-row .form-group {
            flex: 1;
        }
        
        .btn-submit {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #7fad39 0%, #5e8b2a 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-bottom: 20px;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(127, 173, 57, 0.3);
        }
        
        .btn-submit:active {
            transform: translateY(0);
        }
        
        .alert {
            padding: 15px 20px;
            border-radius: 10px;
            margin-bottom: 25px;
            font-size: 14px;
        }
        
        .alert-error {
            background: #fff2f2;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        
        .alert-success {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        
        .back-links {
            text-align: center;
            margin-top: 20px;
            display: flex;
            justify-content: space-between;
            gap: 20px;
        }
        
        .back-links a {
            color: #7fad39;
            text-decoration: none;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            transition: all 0.3s ease;
            flex: 1;
            justify-content: center;
        }
        
        .back-links a:hover {
            color: #5e8b2a;
            text-decoration: none;
        }
        
        .icon-key {
            width: 60px;
            height: 60px;
            background: rgba(255,255,255,0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            font-size: 24px;
        }
        
        .code-input {
            text-align: center;
            font-size: 18px;
            letter-spacing: 3px;
            font-weight: 600;
        }        .password-strength {
            margin-top: 8px !important;
            font-size: 14px !important;
            min-height: 20px !important;
            font-weight: 500 !important;
            display: block !important;
            visibility: visible !important;
            opacity: 1 !important;
            line-height: 1.4 !important;
        }
        
        .strength-weak { 
            color: #dc3545 !important; 
            font-weight: bold;
        }
        .strength-medium { 
            color: #ffc107 !important; 
            font-weight: bold;
        }
        .strength-strong { 
            color: #28a745 !important; 
            font-weight: bold;
        }
        
        .help-text {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
            font-size: 14px;
            color: #666;
            line-height: 1.6;
        }
          .help-text h4 {
            color: #333;
            margin-bottom: 10px;
            font-size: 16px;
        }
        
        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
                gap: 0;
            }
            
            .back-links {
                flex-direction: column;
                gap: 10px;
            }
        }
    </style>
</head>

<body>
    <div class="reset-password-container">
        <div class="reset-form-wrapper">
            <div class="form-header">
                <div class="icon-key">
                    <i class="fa fa-key"></i>
                </div>
                <h2>Đặt lại mật khẩu</h2>
                <p>Nhập mã xác thực và mật khẩu mới</p>
            </div>
            
            <div class="form-content">
                <!-- Hiển thị thông báo thành công từ forgot password -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success">
                        <i class="fa fa-check-circle"></i> ${sessionScope.successMessage}
                    </div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>
                
                <!-- Hiển thị thông báo lỗi nếu có -->
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="fa fa-exclamation-triangle"></i> ${error}
                    </div>                </c:if>
                
                <form action="reset-password" method="post" id="resetPasswordForm">
                    <div class="form-group">
                        <label for="email">
                            <i class="fa fa-envelope"></i> Địa chỉ email
                        </label>
                        <input type="email" 
                               id="email" 
                               name="email" 
                               class="form-control" 
                               placeholder="Nhập địa chỉ email..."
                               value="${not empty sessionScope.resetEmail ? sessionScope.resetEmail : enteredEmail}"
                               required>
                    </div>
                    
                    <div class="form-group">
                        <label for="resetCode">
                            <i class="fa fa-shield"></i> Mã xác thực (6 chữ số)
                        </label>
                        <input type="text" 
                               id="resetCode" 
                               name="resetCode" 
                               class="form-control code-input" 
                               placeholder="000000"
                               value="${enteredResetCode}"
                               maxlength="6"
                               pattern="[0-9]{6}"
                               required>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="newPassword">
                                <i class="fa fa-lock"></i> Mật khẩu mới
                            </label>
                            <input type="password" 
                                   id="newPassword" 
                                   name="newPassword" 
                                   class="form-control" 
                                   placeholder="Nhập mật khẩu mới..."
                                   minlength="6"
                                   required>
                           
                        </div>
                        
                        <div class="form-group">
                            <label for="confirmPassword">
                                <i class="fa fa-lock"></i> Xác nhận mật khẩu
                            </label>
                            <input type="password" 
                                   id="confirmPassword" 
                                   name="confirmPassword" 
                                   class="form-control" 
                                   placeholder="Nhập lại mật khẩu..."
                                   required>
                            <div class="password-match" id="passwordMatch"></div>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn-submit">
                        <i class="fa fa-save"></i> Đặt lại mật khẩu
                    </button>
                    
                    <div class="help-text">
                        <h4><i class="fa fa-info-circle"></i> Lưu ý:</h4>
                        <ul style="margin: 0; padding-left: 20px;">
                            <li>Mã xác thực có <strong>6 chữ số</strong></li>
                            <li>Mật khẩu mới phải có <strong>ít nhất 6 ký tự</strong></li>
                            <li>Mã chỉ có hiệu lực trong 15 phút</li>
                            <li>Sau khi đổi mật khẩu, bạn sẽ cần đăng nhập lại</li>
                        </ul>
                    </div>
                </form>
                
                <div class="back-links">
                    <a href="forgot-password">
                        <i class="fa fa-refresh"></i> Gửi lại mã
                    </a>
                    <a href="login">
                        <i class="fa fa-arrow-left"></i> Về trang đăng nhập
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Auto format reset code input
            const resetCodeInput = document.getElementById('resetCode');
            resetCodeInput.addEventListener('input', function(e) {
                // Chỉ cho phép số
                this.value = this.value.replace(/[^0-9]/g, '');
                
                if (this.value.length === 6) {
                    document.getElementById('newPassword').focus();
                }
            });            // Password strength checker
            const passwordInput = document.getElementById('newPassword');
            const strengthDiv = document.getElementById('passwordStrength');
            
            console.log('Password input:', passwordInput);
            console.log('Strength div:', strengthDiv);
            
            if (passwordInput && strengthDiv) {
                passwordInput.addEventListener('input', function() {
                    const password = this.value;
                    let strength = 0;
                    let text = '';
                    let className = '';
                    
                    console.log('Password entered:', password);
                    console.log('Password length:', password.length);
                    
                    // Tính điểm độ mạnh
                    if (password.length >= 6) {
                        strength++;
                        console.log('Length check passed: +1');
                    }
                    if (password.match(/[a-z]/)) {
                        strength++;
                        console.log('Lowercase check passed: +1');
                    }
                    if (password.match(/[A-Z]/)) {
                        strength++;
                        console.log('Uppercase check passed: +1');
                    }
                    if (password.match(/[0-9]/)) {
                        strength++;
                        console.log('Number check passed: +1');
                    }
                    if (password.match(/[^A-Za-z0-9]/)) {
                        strength++;
                        console.log('Special character check passed: +1');
                    }
                    
                    console.log('Total strength score:', strength);
                      // Xác định mức độ mạnh
                    let color = '';
                    if (strength <= 1) {
                        text = 'Yếu';
                        className = 'strength-weak';
                        color = '#dc3545';
                    } else if (strength <= 3) {
                        text = 'Trung bình';
                        className = 'strength-medium';
                        color = '#ffc107';
                    } else {
                        text = 'Mạnh';
                        className = 'strength-strong';
                        color = '#28a745';
                    }
                      console.log('Strength classification:', text, className, color);
                      // Cập nhật hiển thị
                    if (password.length > 0) {
                        // Phương pháp tách riêng label và strength text để áp dụng màu đúng
                        strengthDiv.innerHTML = `Độ mạnh: <span style="color: ${color}; font-weight: bold;">${text}</span>`;
                        strengthDiv.style.fontSize = '14px';
                        strengthDiv.style.display = 'block';
                        strengthDiv.style.visibility = 'visible';
                        
                        console.log('Updated strengthDiv content:', strengthDiv.innerHTML);
                        console.log('Element visible:', strengthDiv.offsetHeight > 0, strengthDiv.offsetWidth > 0);
                        
                        // Cập nhật debug element
                        const debugDiv = document.getElementById('debugStrength');
                        if (debugDiv) {
                            debugDiv.innerHTML = `DEBUG: "${text}" with color ${color}`;
                            debugDiv.style.color = color;
                        }
                        
                        // Kiểm tra lại sau 100ms
                        setTimeout(() => {
                            console.log('Final verification:');
                            console.log('- strengthDiv.innerHTML:', strengthDiv.innerHTML);
                            console.log('- strengthDiv visible:', strengthDiv.offsetHeight > 0);
                            console.log('- strengthDiv computed style:', window.getComputedStyle(strengthDiv).display);
                        }, 100);
                    } else {
                        strengthDiv.innerHTML = '';
                        console.log('Password empty, cleared display');
                    }
                });
            } else {
                console.error('Password input or strength div not found!');
                console.log('Available elements:', {
                    passwordInput: !!passwordInput,
                    strengthDiv: !!strengthDiv
                });
            }
              // Password confirmation checker
            const confirmPasswordInput = document.getElementById('confirmPassword');
            const matchDiv = document.getElementById('passwordMatch');
            
            function checkPasswordMatch() {
                const password = passwordInput.value;
                const confirmPassword = confirmPasswordInput.value;
                
                if (confirmPassword.length > 0) {
                    if (password === confirmPassword) {
                        matchDiv.innerHTML = '<span class="strength-strong"><i class="fa fa-check"></i> Mật khẩu khớp</span>';
                    } else {
                        matchDiv.innerHTML = '<span class="strength-weak"><i class="fa fa-times"></i> Mật khẩu không khớp</span>';
                    }
                } else {
                    matchDiv.innerHTML = '';
                }
            }
            
            if (passwordInput && confirmPasswordInput && matchDiv) {                passwordInput.addEventListener('input', checkPasswordMatch);
                confirmPasswordInput.addEventListener('input', checkPasswordMatch);
            }
              // Test function để kiểm tra password strength ngay khi load
            setTimeout(() => {
                console.log('Testing password strength display...');
                const testDiv = document.getElementById('passwordStrength');
                if (testDiv) {
                    // Test 1: textContent đơn giản
                    testDiv.textContent = 'TEST: Yếu';
                    testDiv.style.color = 'red';
                    testDiv.style.fontWeight = 'bold';
                    testDiv.style.backgroundColor = 'yellow';
                    testDiv.style.padding = '5px';
                    
                    console.log('Test 1 - textContent set:', testDiv.textContent);
                    console.log('Test div position:', testDiv.getBoundingClientRect());
                    console.log('Test div computed style:', window.getComputedStyle(testDiv));
                    
                    setTimeout(() => {
                        // Test 2: innerHTML
                        testDiv.innerHTML = '<strong style="color: blue; background: lime;">TEST: Mạnh</strong>';
                        console.log('Test 2 - innerHTML set');
                        
                        setTimeout(() => {
                            testDiv.textContent = '';
                            testDiv.style.backgroundColor = '';
                            testDiv.style.padding = '';
                            console.log('Test cleared');
                        }, 3000);
                    }, 2000);
                }
            }, 1000);

        });
    </script>
</body>

</html> 