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
    <title>Quên mật khẩu - Biolife</title>
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
        .forgot-password-container {
            background: linear-gradient(135deg, #7fad39 0%, #5e8b2a 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px 0;
        }
        
        .forgot-form-wrapper {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            overflow: hidden;
            max-width: 500px;
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
        
        .back-to-login {
            text-align: center;
            margin-top: 20px;
        }
        
        .back-to-login a {
            color: #7fad39;
            text-decoration: none;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            transition: all 0.3s ease;
        }
        
        .back-to-login a:hover {
            color: #5e8b2a;
            text-decoration: none;
        }
        
        .icon-email {
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
        
        .loading {
            display: none;
            opacity: 0.7;
        }
        
        .loading .btn-submit {
            pointer-events: none;
        }
    </style>
</head>

<body>
    <div class="forgot-password-container">
        <div class="forgot-form-wrapper">
            <div class="form-header">
                <div class="icon-email">
                    <i class="fa fa-envelope"></i>
                </div>
                <h2>Quên mật khẩu?</h2>
                <p>Nhập email của bạn để nhận mã reset mật khẩu</p>
            </div>
            
            <div class="form-content">
                <!-- Hiển thị thông báo lỗi nếu có -->
                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="fa fa-exclamation-triangle"></i> ${error}
                    </div>
                </c:if>
                
                <form action="forgot-password" method="post" id="forgotPasswordForm">
                    <div class="form-group">
                        <label for="email">
                            <i class="fa fa-envelope"></i> Địa chỉ email
                        </label>
                        <input type="email" 
                               id="email" 
                               name="email" 
                               class="form-control" 
                               placeholder="Nhập địa chỉ email của bạn..."
                               value="${param.email}"
                               required>
                    </div>
                    
                    <button type="submit" class="btn-submit">
                        <span class="btn-text">
                            <i class="fa fa-paper-plane"></i> Gửi mã reset
                        </span>
                        <span class="btn-loading" style="display: none;">
                            <i class="fa fa-spinner fa-spin"></i> Đang gửi...
                        </span>
                    </button>
                    
                    <div class="help-text">
                        <h4><i class="fa fa-info-circle"></i> Lưu ý:</h4>
                        <ul style="margin: 0; padding-left: 20px;">
                            <li>Mã reset sẽ được gửi đến email của bạn</li>
                            <li>Mã có hiệu lực trong <strong>15 phút</strong></li>
                            <li>Kiểm tra cả thư mục spam nếu không thấy email</li>
                            <li>Chỉ mã mới nhất mới có hiệu lực</li>
                        </ul>
                    </div>
                </form>
                
                <div class="back-to-login">
                    <a href="login">
                        <i class="fa fa-arrow-left"></i> Quay lại đăng nhập
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
            const form = document.getElementById('forgotPasswordForm');
            const submitBtn = form.querySelector('.btn-submit');
            const btnText = submitBtn.querySelector('.btn-text');
            const btnLoading = submitBtn.querySelector('.btn-loading');
            
            form.addEventListener('submit', function(e) {
                // Show loading state
                btnText.style.display = 'none';
                btnLoading.style.display = 'inline';
                submitBtn.disabled = true;
                
                // Re-enable after 5 seconds (in case of slow response)
                setTimeout(function() {
                    btnText.style.display = 'inline';
                    btnLoading.style.display = 'none';
                    submitBtn.disabled = false;
                }, 5000);
            });
            
            // Auto focus email input
            document.getElementById('email').focus();
        });
    </script>
</body>

</html> 