<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Demo Wishlist - Fish Shop</title>
    
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/favicon.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color04.css">
    <link rel="stylesheet" href="assets/css/wishlist.css">
    
    <style>
        .demo-container {
            padding: 60px 0;
            background-color: #f8f9fa;
        }
        
        .demo-product {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            position: relative;
        }
        
        .demo-product-image {
            width: 150px;
            height: 150px;
            object-fit: cover;
            border-radius: 8px;
            background-color: #f0f0f0;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #999;
            font-size: 48px;
        }
        
        .demo-product-info h4 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .demo-product-price {
            color: #7fad39;
            font-size: 1.3rem;
            font-weight: bold;
            margin-bottom: 15px;
        }
        
        .demo-actions {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .section-title {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .section-title h2 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 10px;
        }
        
        .login-info {
            background: #d1ecf1;
            border: 1px solid #bee5eb;
            color: #0c5460;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 30px;
        }
        
        .user-info {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 30px;
        }
    </style>
</head>

<body class="biolife-body">
    <!-- HEADER -->
    <jsp:include page="header.jsp"></jsp:include>

    <!-- Main Content -->
    <div class="page-contain demo-container">
        <div class="container">
            <div class="section-title">
                <h2><i class="fa fa-heart" style="color: #e74c3c;"></i> Demo Chức năng Wishlist</h2>
                <p>Test các chức năng thêm/xóa sản phẩm yêu thích</p>
            </div>
            
            <!-- User Status -->
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <div class="user-info">
                        <strong>Chào ${sessionScope.user.fullName}!</strong> Bạn có thể test đầy đủ chức năng wishlist.
                        <a href="${pageContext.request.contextPath}/wishlist" class="btn btn-sm btn-primary" style="margin-left: 15px;">
                            <i class="fa fa-heart"></i> Xem wishlist của tôi
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="login-info">
                        <strong>Chưa đăng nhập:</strong> Bạn cần đăng nhập để sử dụng chức năng wishlist.
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-primary" style="margin-left: 15px;">
                            <i class="fa fa-sign-in"></i> Đăng nhập
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
            
            <!-- Demo Products -->
            <div class="row">
                <div class="col-md-6">
                    <div class="demo-product">
                        <div class="row align-items-center">
                            <div class="col-4">
                                <div class="demo-product-image">
                                    <i class="fa fa-fish"></i>
                                </div>
                            </div>
                            <div class="col-8">
                                <div class="demo-product-info">
                                    <h4>Cá Vàng Nhật Bản</h4>
                                    <div class="demo-product-price">299.000 ₫</div>
                                    <p>Cá vàng chất lượng cao từ Nhật Bản, màu sắc đẹp và khỏe mạnh.</p>
                                    <div class="demo-actions">
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="1" />
                                            <jsp:param name="iconOnly" value="false" />
                                        </jsp:include>
                                        
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="1" />
                                            <jsp:param name="iconOnly" value="true" />
                                        </jsp:include>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="demo-product">
                        <div class="row align-items-center">
                            <div class="col-4">
                                <div class="demo-product-image">
                                    <i class="fa fa-fish"></i>
                                </div>
                            </div>
                            <div class="col-8">
                                <div class="demo-product-info">
                                    <h4>Cá Koi Nhật</h4>
                                    <div class="demo-product-price">1.500.000 ₫</div>
                                    <p>Cá Koi thuần chủng từ Nhật Bản, màu sắc rực rỡ và có ý nghĩa phong thủy.</p>
                                    <div class="demo-actions">
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="2" />
                                            <jsp:param name="iconOnly" value="false" />
                                        </jsp:include>
                                        
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="2" />
                                            <jsp:param name="iconOnly" value="true" />
                                        </jsp:include>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="demo-product">
                        <div class="row align-items-center">
                            <div class="col-4">
                                <div class="demo-product-image">
                                    <i class="fa fa-fish"></i>
                                </div>
                            </div>
                            <div class="col-8">
                                <div class="demo-product-info">
                                    <h4>Cá Betta Thái Lan</h4>
                                    <div class="demo-product-price">150.000 ₫</div>
                                    <p>Cá Betta đẹp từ Thái Lan, dễ nuôi và có nhiều màu sắc đẹp mắt.</p>
                                    <div class="demo-actions">
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="3" />
                                            <jsp:param name="iconOnly" value="false" />
                                        </jsp:include>
                                        
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="3" />
                                            <jsp:param name="iconOnly" value="true" />
                                        </jsp:include>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="demo-product">
                        <div class="row align-items-center">
                            <div class="col-4">
                                <div class="demo-product-image">
                                    <i class="fa fa-fish"></i>
                                </div>
                            </div>
                            <div class="col-8">
                                <div class="demo-product-info">
                                    <h4>Cá Guppy Đuôi Cờ</h4>
                                    <div class="demo-product-price">50.000 ₫</div>
                                    <p>Cá Guppy đuôi cờ đẹp, dễ nuôi và sinh sản nhanh.</p>
                                    <div class="demo-actions">
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="4" />
                                            <jsp:param name="iconOnly" value="false" />
                                        </jsp:include>
                                        
                                        <jsp:include page="components/wishlist-button.jsp">
                                            <jsp:param name="productId" value="4" />
                                            <jsp:param name="iconOnly" value="true" />
                                        </jsp:include>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Instructions -->
            <div class="row" style="margin-top: 40px;">
                <div class="col-12">
                    <div style="background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                        <h4>Hướng dẫn test:</h4>
                        <ol>
                            <li><strong>Nếu chưa đăng nhập:</strong> Click vào button wishlist sẽ hiện prompt đăng nhập</li>
                            <li><strong>Nếu đã đăng nhập:</strong> 
                                <ul>
                                    <li>Click vào button "Yêu thích" để thêm sản phẩm vào wishlist</li>
                                    <li>Click vào icon tim để toggle wishlist</li>
                                    <li>Xem số lượng wishlist thay đổi ở header</li>
                                    <li>Click vào link "Xem wishlist của tôi" để xem trang wishlist</li>
                                </ul>
                            </li>
                            <li><strong>Trong trang wishlist:</strong> Có thể xóa từng item hoặc xóa tất cả</li>
                        </ol>
                        
                        <h5 style="margin-top: 20px;">API Endpoints:</h5>
                        <ul>
                            <li><code>GET /wishlist</code> - Xem trang wishlist</li>
                            <li><code>POST /wishlist?action=add</code> - Thêm vào wishlist</li>
                            <li><code>POST /wishlist?action=remove</code> - Xóa khỏi wishlist</li>
                            <li><code>POST /wishlist?action=toggle</code> - Toggle wishlist</li>
                            <li><code>GET /wishlist?action=count</code> - Lấy số lượng</li>
                            <li><code>GET /wishlist?action=check&productId=1</code> - Kiểm tra trạng thái</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/wishlist.js"></script>
    
    <script>
        // Set context path
        window.contextPath = '${pageContext.request.contextPath}';
    </script>
</body>
</html>
