<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fish Shop - Đăng ký</title>
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
        
        /* Social Login Styles */
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
        
        .register-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
    </style>
</head>
<body class="biolife-body">
<jsp:include page="header.jsp"></jsp:include>
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

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">Đăng ký tài khoản</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home.jsp" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><span class="current-page">Đăng ký</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain login-page">
        <!-- Main content -->
        <div id="main-content" class="main-content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8 col-md-8 col-sm-10 col-xs-12 mx-auto">
                        <div class="register-container">
                            <div class="signin-container">
                                <h2 class="box-title text-center">Đăng ký tài khoản</h2>
                                
                                <!-- Hiển thị thông báo lỗi nếu có -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger" role="alert">
                                        ${error}
                                    </div>
                                </c:if>
                                
                                <form action="register" name="frm-register" method="post">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="form-row">
                                                <label for="username">Tên đăng nhập:<span class="requite">*</span></label>
                                                <input type="text" id="username" name="username" value="${param.username}" class="txt-input" required>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <p class="form-row">
                                                <label for="email">Email:<span class="requite">*</span></label>
                                                <input type="email" id="email" name="email" value="${param.email}" class="txt-input" required>
                                            </p>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="form-row">
                                                <label for="fullname">Họ và tên:<span class="requite">*</span></label>
                                                <input type="text" id="fullname" name="fullname" value="${param.fullname}" class="txt-input" required>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <p class="form-row">
                                                <label for="phone">Số điện thoại:</label>
                                                <input type="text" id="phone" name="phone" value="${param.phone}" class="txt-input">
                                            </p>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="form-row">
                                                <label for="password">Mật khẩu:<span class="requite">*</span></label>
                                                <input type="password" id="password" name="password" value="" class="txt-input" required>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <p class="form-row">
                                                <label for="confirm_password">Xác nhận mật khẩu:<span class="requite">*</span></label>
                                                <input type="password" id="confirm_password" name="confirm_password" value="" class="txt-input" required>
                                            </p>
                                        </div>
                                    </div>
                                    
                                    <p class="form-row">
                                        <input type="checkbox" name="agree_terms" id="agree_terms" value="1" required>
                                        <label for="agree_terms">Tôi đồng ý với <a href="#" style="color: #7faf51;">Điều khoản dịch vụ</a> và <a href="#" style="color: #7faf51;">Chính sách bảo mật</a></label>
                                    </p>
                                      <p class="form-row wrap-btn text-center">
                                        <button class="btn btn-submit btn-bold" type="submit">Đăng ký</button>
                                    </p>
                                    
                                    <p class="form-row text-center">
                                        <a href="login" class="link-to-help">Đã có tài khoản? Đăng nhập ngay</a>
                                    </p>
                                </form>
                                
                                <!-- Google Sign Up -->
                                <div class="social-login">
                                    <p class="or-divider"><span>hoặc</span></p>
                                    <a href="auth/google" class="btn btn-google">
                                        <i class="fa fa-google"></i> Đăng ký bằng Google
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
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
    
    <script>
        // Validation script
        document.querySelector('form[name="frm-register"]').addEventListener('submit', function(e) {
            var password = document.getElementById('password').value;
            var confirmPassword = document.getElementById('confirm_password').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu xác nhận không khớp!');
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                alert('Mật khẩu phải có ít nhất 6 ký tự!');
                return false;
            }
        });
    </script>
</body>

</html>
