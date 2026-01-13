<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fish Shop - Đăng nhập</title>
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
    <link rel="stylesheet" href="assets/css/main-color.css">    <style>
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
        }
        .alert-danger {
            color: #721c24;
            background-color: #f8d7da;
            border-color: #f5c6cb;
        }
        .alert-success {
            color: #155724;
            background-color: #d4edda;
            border-color: #c3e6cb;
        }
        
        /* Google Login button style */
        .social-login {
            margin-top: 30px;
            border-top: 1px solid #eee;
            padding-top: 20px;
        }
        
        .or-divider {
            text-align: center;
            position: relative;
            margin: 15px 0;
        }
        
        .or-divider:before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #e1e1e1;
            z-index: 1;
        }
        
        .or-divider span {
            display: inline-block;
            position: relative;
            padding: 0 15px;
            background: #fff;
            color: #999;
            z-index: 2;
            font-size: 14px;
        }
        
        .btn-google {
            display: block;
            width: 100%;
            background: #fff;
            border: 1px solid #ddd;
            color: #333;
            font-weight: 500;
            text-align: center;
            padding: 12px 15px;
            border-radius: 4px;
            margin: 10px 0;
            transition: all 0.3s;
            text-decoration: none;
        }
        
        .btn-google:hover {
            background: #f5f5f5;
            border-color: #ccc;
            text-decoration: none;
        }
        
        .btn-google i {
            color: #DB4437;
            margin-right: 10px;
        }
    </style>
</head>
<body class="biolife-body">

    <!-- Preloader -->
    <div id="biof-loading">
        <div class="biof-loading-center">
            <div class="biof-loading-center-absolute">
                <div class="dot dot-one"></div>
                <div class="dot dot-two"></div>
                <div class="dot dot-three"></div>
            </div>
        </div>
    </div>

    <!-- HEADER -->
    <jsp:include page="header.jsp"></jsp:include>

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">Đăng nhập</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home.jsp" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><span class="current-page">Đăng nhập</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain login-page">

        <!-- Main content -->
        <div id="main-content" class="main-content">
            <div class="container">

                <div class="row">

                    <!--Form Sign In-->
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                        <div class="signin-container">
                            <h2 class="box-title">Đăng nhập</h2>
                            
                            <!-- Hiển thị thông báo lỗi nếu có -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger" role="alert">
                                    ${error}
                                </div>
                            </c:if>
                            
                            <!-- Hiển thị thông báo thành công nếu có -->
                            <c:if test="${not empty success}">
                                <div class="alert alert-success" role="alert">
                                    ${success}
                                </div>
                            </c:if>
                            
                            <form action="login" name="frm-login" method="post">
                                <p class="form-row">
                                    <label for="fid-name">Tên đăng nhập hoặc Email:<span class="requite">*</span></label>
                                    <input type="text" id="fid-name" name="user" 
                                           value="${not empty rememberedUsername ? rememberedUsername : (not empty enteredUsername ? enteredUsername : '')}" 
                                           class="txt-input" required>
                                </p>
                                <p class="form-row">
                                    <label for="fid-pass">Mật khẩu:<span class="requite">*</span></label>
                                    <input type="password" id="fid-pass" name="pass" 
                                           value="${not empty rememberedPassword ? rememberedPassword : ''}" 
                                           class="txt-input" required>
                                </p>
                                <p class="form-row">
                                    <input type="checkbox" name="remember" id="remember" value="1" 
                                           ${isRemembered ? 'checked' : ''}>
                                    <label for="remember">Ghi nhớ đăng nhập</label>
                                    <c:if test="${isRemembered}">
                                        <button type="button" id="clear-saved" class="btn-clear-saved" 
                                                style="margin-left: 10px; padding: 2px 8px; font-size: 12px; color: #666; background: none; border: 1px solid #ccc; border-radius: 3px; cursor: pointer;"
                                                title="Xóa thông tin đã lưu">
                                            <i class="fa fa-times"></i> Xóa
                                        </button>
                                    </c:if>
                                </p>                                <p class="form-row wrap-btn">
                                    <button class="btn btn-submit btn-bold" type="submit">Đăng nhập</button>
                                    <a href="forgot-password" class="link-to-help">Quên mật khẩu?</a>
                                </p>
                            </form>
                            
                            <!-- Google Login -->
                            <div class="social-login">
                                <p class="or-divider"><span>hoặc</span></p>
                                <a href="auth/google" class="btn btn-google">
                                    <i class="fa fa-google"></i> Đăng nhập bằng Google
                                </a>
                            </div>
                            
                        </div>
                    </div>

                    <!--Go to Register form-->
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                        <div class="register-in-container">
                            <div class="intro">
                                <h4 class="box-title">Bạn chưa có tài khoản?</h4>
                                <p class="sub-title">Tạo tài khoản với chúng tôi và bạn sẽ có thể:</p>
                                <ul class="lis">
                                    <li>Thanh toán nhanh hơn</li>
                                    <li>Lưu nhiều địa chỉ giao hàng</li>
                                    <li>Truy cập lịch sử đơn hàng</li>
                                    <li>Theo dõi đơn hàng mới</li>
                                    <li>Lưu sản phẩm vào danh sách yêu thích</li>
                                </ul>
                                <a href="register" class="btn btn-bold">Tạo tài khoản</a>
                            </div>
                        </div>
                    </div>

                </div>

            </div>

        </div>

    </div>

    <jsp:include page="footer.jsp" />

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
    
    <!-- Custom script for remember me functionality -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const rememberCheckbox = document.getElementById('remember');
            const usernameInput = document.getElementById('fid-name');
            const passwordInput = document.getElementById('fid-pass');
            const clearButton = document.getElementById('clear-saved');
            
            // Function để xóa cookies và form
            function clearSavedData() {
                // Xóa cookies bằng cách set maxAge = 0
                document.cookie = "remembered_username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                document.cookie = "remembered_password=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                
                // Xóa nội dung trong form
                usernameInput.value = '';
                passwordInput.value = '';
                rememberCheckbox.checked = false;
                
                // Ẩn nút xóa
                if (clearButton) {
                    clearButton.style.display = 'none';
                }
                
                console.log('Remember me cookies cleared');
            }
            
            // Khi người dùng bỏ check remember me, xóa cookies
            rememberCheckbox.addEventListener('change', function() {
                if (!this.checked) {
                    clearSavedData();
                }
            });
            
            // Xử lý nút "Xóa thông tin đã lưu"
            if (clearButton) {
                clearButton.addEventListener('click', function() {
                    if (confirm('Bạn có chắc chắn muốn xóa thông tin đăng nhập đã lưu?')) {
                        clearSavedData();
                    }
                });
            }
            
            // Khi người dùng thay đổi username hoặc password, uncheck remember me nếu khác với saved
            const originalUsername = usernameInput.value;
            const originalPassword = passwordInput.value;
            
            function checkForChanges() {
                if (rememberCheckbox.checked) {
                    if (usernameInput.value !== originalUsername || passwordInput.value !== originalPassword) {
                        // Nếu có thay đổi, hỏi người dùng có muốn cập nhật không
                        if (usernameInput.value !== originalUsername || passwordInput.value !== originalPassword) {
                            // User has made changes, keep checkbox but they'll need to login to update cookies
                        }
                    }
                }
            }
            
            usernameInput.addEventListener('input', checkForChanges);
            passwordInput.addEventListener('input', checkForChanges);
        });
    </script>
</body>

</html>