<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.entity.User" %>

<%
    // Check if user is logged in and is admin
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Fish Store</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            position: fixed;
            top: 0;
            left: 0;
            width: 250px;
            z-index: 1000;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        
        .admin-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        
        .admin-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
        }
        
        .admin-card .card-body {
            padding: 2rem;
            text-align: center;
        }
        
        .admin-card .icon {
            font-size: 3rem;
            margin-bottom: 1rem;
        }
        
        .blog-management {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .users-management {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        
        .products-management {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            color: white;
        }
        
        .orders-management {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            color: white;
        }
        
        .nav-link:hover {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 5px;
        }
        
        .nav-link.active {
            background: rgba(255, 255, 255, 0.2);
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <nav class="sidebar">
        <div class="position-sticky pt-3">
            <div class="text-center mb-4">
                <h4 class="text-white"><i class="bi bi-speedometer2"></i> Admin Panel</h4>
                <p class="text-white-50">Xin chào, ${user.fullName != null ? user.fullName : user.username}</p>
            </div>
            
            <ul class="nav flex-column px-3">
                <li class="nav-item mb-2">
                    <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin-panel">
                        <i class="bi bi-house"></i> Dashboard
                    </a>
                </li>
                
                <li class="nav-item mb-2">
                    <a class="nav-link text-white" href="#" data-bs-toggle="collapse" data-bs-target="#blogMenu" aria-expanded="true">
                        <i class="bi bi-journal-text"></i> Blog Management <i class="bi bi-chevron-down ms-auto"></i>
                    </a>
                    <div class="collapse show" id="blogMenu">
                        <ul class="nav flex-column ms-3">
                            <li class="nav-item">
                                <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin-blog">
                                    <i class="bi bi-list-ul"></i> Danh sách bài viết
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin-blog-form">
                                    <i class="bi bi-plus-circle"></i> Thêm bài viết
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin-blog-categories">
                                    <i class="bi bi-tags"></i> Quản lý danh mục
                                </a>
                            </li>
                        </ul>
                    </div>
                </li>
                
                <li class="nav-item mb-2">
                    <a class="nav-link text-white" href="admin-users.jsp">
                        <i class="bi bi-people"></i> Quản lý Users
                    </a>
                </li>
                
                <li class="nav-item mb-2">
                    <a class="nav-link text-white" href="admin-products.jsp">
                        <i class="bi bi-box"></i> Quản lý Products
                    </a>
                </li>
                
                <li class="nav-item mb-2">
                    <a class="nav-link text-white" href="admin-orders.jsp">
                        <i class="bi bi-cart"></i> Quản lý Orders
                    </a>
                </li>
                
                <hr class="text-white-50">
                
                <li class="nav-item mb-2">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/blog" target="_blank">
                        <i class="bi bi-eye"></i> Xem Blog
                    </a>
                </li>
                
                <li class="nav-item mb-2">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-arrow-left"></i> Về trang chủ
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/logout">
                        <i class="bi bi-box-arrow-right"></i> Đăng xuất
                    </a>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="main-content">
        <div class="container-fluid">
            <!-- Header -->
            <div class="row mb-4">
                <div class="col-12">
                    <h1 class="h2">Admin Dashboard</h1>
                    <p class="text-muted">Chào mừng đến với trang quản trị. Chọn một chức năng để bắt đầu.</p>
                </div>
            </div>

            <!-- Quick Access Cards -->
            <div class="row">
                <!-- Blog Management -->
                <div class="col-lg-6 col-md-6 mb-4">
                    <div class="card admin-card blog-management">
                        <div class="card-body">
                            <i class="bi bi-journal-text icon"></i>
                            <h5 class="card-title">Blog Management</h5>
                            <p class="card-text">Quản lý bài viết, danh mục blog và nội dung</p>
                            <div class="row">
                                <div class="col-4">
                                    <a href="${pageContext.request.contextPath}/admin-blog" class="btn btn-light btn-sm">
                                        <i class="bi bi-list-ul"></i><br>Bài viết
                                    </a>
                                </div>
                                <div class="col-4">
                                    <a href="${pageContext.request.contextPath}/admin-blog-form" class="btn btn-light btn-sm">
                                        <i class="bi bi-plus-circle"></i><br>Thêm mới
                                    </a>
                                </div>
                                <div class="col-4">
                                    <a href="${pageContext.request.contextPath}/admin-blog-categories" class="btn btn-light btn-sm">
                                        <i class="bi bi-tags"></i><br>Danh mục
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Users Management -->
                <div class="col-lg-6 col-md-6 mb-4">
                    <div class="card admin-card users-management">
                        <div class="card-body">
                            <i class="bi bi-people icon"></i>
                            <h5 class="card-title">Users Management</h5>
                            <p class="card-text">Quản lý người dùng và phân quyền</p>
                            <a href="admin-users.jsp" class="btn btn-light">
                                <i class="bi bi-arrow-right"></i> Quản lý Users
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Products Management -->
                <div class="col-lg-6 col-md-6 mb-4">
                    <div class="card admin-card products-management">
                        <div class="card-body">
                            <i class="bi bi-box icon"></i>
                            <h5 class="card-title">Products Management</h5>
                            <p class="card-text">Quản lý sản phẩm, danh mục và kho hàng</p>
                            <a href="admin-products.jsp" class="btn btn-light">
                                <i class="bi bi-arrow-right"></i> Quản lý Products
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Orders Management -->
                <div class="col-lg-6 col-md-6 mb-4">
                    <div class="card admin-card orders-management">
                        <div class="card-body">
                            <i class="bi bi-cart icon"></i>
                            <h5 class="card-title">Orders Management</h5>
                            <p class="card-text">Quản lý đơn hàng và trạng thái giao hàng</p>
                            <a href="admin-orders.jsp" class="btn btn-light">
                                <i class="bi bi-arrow-right"></i> Quản lý Orders
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Activities -->
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h5><i class="bi bi-clock-history"></i> Hoạt động gần đây</h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group list-group-flush">
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="bi bi-journal-plus text-primary"></i>
                                        <span class="ms-2">Blog management được kích hoạt</span>
                                    </div>
                                    <small class="text-muted">Vừa xong</small>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="bi bi-shield-check text-success"></i>
                                        <span class="ms-2">Hệ thống bảo mật admin được cập nhật</span>
                                    </div>
                                    <small class="text-muted">5 phút trước</small>
                                </div>
                                <div class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="bi bi-database text-info"></i>
                                        <span class="ms-2">Database blog categories được cập nhật</span>
                                    </div>
                                    <small class="text-muted">10 phút trước</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    
    <script>
        $(document).ready(function() {
            // Auto-collapse/expand blog menu based on current page
            const currentPath = window.location.pathname;
            if (currentPath.includes('admin-blog')) {
                $('#blogMenu').addClass('show');
                $('#blogMenu').prev().find('.bi-chevron-down').addClass('rotate');
            }
            
            // Add rotation animation for chevron
            $('[data-bs-toggle="collapse"]').on('click', function() {
                $(this).find('.bi-chevron-down').toggleClass('rotate');
            });
            
            // Welcome message
            console.log('Admin Panel loaded successfully');
        });
    </script>
    
    <style>
        .bi-chevron-down {
            transition: transform 0.3s ease;
        }
        
        .bi-chevron-down.rotate {
            transform: rotate(180deg);
        }
        
        .list-group-item {
            border: none;
            padding: 0.75rem 0;
        }
        
        .list-group-item:not(:last-child) {
            border-bottom: 1px solid #dee2e6;
        }
    </style>
</body>
</html>
