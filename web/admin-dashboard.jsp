<%@page import="dao.impl.OrderDAO"%>
<%@page import="dao.impl.ProductDAO"%>
<%@page import="dao.impl.UserDAO"%>
<%@page import="service.interfaces.BlogService"%>
<%@page import="service.impl.BlogServiceImpl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.*, java.math.BigDecimal, model.entity.*, dao.impl.*" %>

<%
    // Initialize data safely
    UserDAO userDAO = new UserDAO();
    ProductDAO productDAO = new ProductDAO();
    OrderDAO orderDAO = new OrderDAO();
    BlogService blogService = new BlogServiceImpl();
    
    // Get statistics with proper error handling
    int totalUsers = 0;
    int totalProducts = 0;
    int totalOrders = 0;
    int totalBlogPosts = 0;
    int totalCategories = 0;
    BigDecimal totalRevenue = BigDecimal.ZERO;
    
    try {
        List<User> users = userDAO.getAllUsers();
        totalUsers = (users != null) ? users.size() : 0;
    } catch (Exception e) {
        totalUsers = 0;
    }
    
    try {
        List<Product> products = productDAO.getAllProducts();
        totalProducts = (products != null) ? products.size() : 0;
    } catch (Exception e) {
        totalProducts = 0;
    }
    
    try {
        List<Order> orders = orderDAO.getAllOrders();
        totalOrders = (orders != null) ? orders.size() : 0;
        
        if (orders != null) {
            for (Order order : orders) {
                if (order != null && order.getTotalAmount() != null && order.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                    totalRevenue = totalRevenue.add(order.getTotalAmount());
                }
            }
        }
    } catch (Exception e) {
        totalOrders = 0;
        totalRevenue = BigDecimal.ZERO;    }
    
    // Get Blog statistics
    try {
        List<BlogPost> posts = blogService.getAllPosts();
        totalBlogPosts = (posts != null) ? posts.size() : 0;
        
        List<BlogCategory> categories = blogService.getAllCategories();
        totalCategories = (categories != null) ? categories.size() : 0;
    } catch (Exception e) {
        totalBlogPosts = 0;
        totalCategories = 0;
    }
    
    // Store in request scope
    request.setAttribute("totalUsers", totalUsers);
    request.setAttribute("totalProducts", totalProducts);
    request.setAttribute("totalOrders", totalOrders);
    request.setAttribute("totalRevenue", totalRevenue);
    request.setAttribute("totalBlogPosts", totalBlogPosts);
    request.setAttribute("totalCategories", totalCategories);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Fish Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .stat-card {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        .stat-card.users {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        .stat-card.products {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        .stat-card.orders {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
        .chart-container {
            position: relative;
            height: 400px;
            width: 100%;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <h4 class="text-white mb-4"><i class="bi bi-speedometer2"></i> Admin Panel</h4>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="#dashboard">
                                <i class="bi bi-house"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="admin-users.jsp">
                                <i class="bi bi-people"></i> Users
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="admin-products.jsp">
                                <i class="bi bi-box"></i> Products
                            </a>
                        </li>                        <li class="nav-item">
                            <a class="nav-link text-white" href="admin-orders.jsp">
                                <i class="bi bi-cart"></i> Orders
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="#" data-bs-toggle="collapse" data-bs-target="#blogMenu" aria-expanded="false">
                                <i class="bi bi-journal-text"></i> Blog Management <i class="bi bi-chevron-down ms-auto"></i>
                            </a>
                            <div class="collapse" id="blogMenu">
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
                                    </li>                                    <li class="nav-item">
                                        <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin-categories">
                                            <i class="bi bi-tags"></i> Quản lý danh mục
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </li>                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                                <i class="bi bi-arrow-left"></i> Back to Site
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Dashboard</h1>
                </div>                <!-- Statistics Cards -->
                <div class="row mb-4">
                    <div class="col-md-2 mb-3">
                        <div class="card stat-card users">
                            <div class="card-body text-center">
                                <i class="bi bi-people fs-1 mb-2"></i>
                                <h6 class="card-title">Total Users</h6>
                                <h3>${totalUsers}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <div class="card stat-card products">
                            <div class="card-body text-center">
                                <i class="bi bi-box fs-1 mb-2"></i>
                                <h6 class="card-title">Total Products</h6>
                                <h3>${totalProducts}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <div class="card stat-card orders">
                            <div class="card-body text-center">
                                <i class="bi bi-cart fs-1 mb-2"></i>
                                <h6 class="card-title">Total Orders</h6>
                                <h3>${totalOrders}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <div class="card stat-card">
                            <div class="card-body text-center">
                                <i class="bi bi-currency-dollar fs-1 mb-2"></i>
                                <h6 class="card-title">Total Revenue</h6>
                                <h3>$${totalRevenue}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <div class="card stat-card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <div class="card-body text-center">
                                <i class="bi bi-journal-text fs-1 mb-2"></i>
                                <h6 class="card-title">Blog Posts</h6>
                                <h3>${totalBlogPosts}</h3>
                                <small><a href="${pageContext.request.contextPath}/admin-blog" class="text-white">Manage Posts</a></small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2 mb-3">
                        <div class="card stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <div class="card-body text-center">
                                <i class="bi bi-tags fs-1 mb-2"></i>
                                <h6 class="card-title">Categories</h6>
                                <h3>${totalCategories}</h3>
                                <small><a href="${pageContext.request.contextPath}/admin-categories" class="text-white">Manage Categories</a></small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Charts -->
                <div class="row">
                    <div class="col-md-6 mb-4">
                        <div class="card">
                            <div class="card-header">
                                <h5>Monthly Revenue</h5>
                            </div>
                            <div class="card-body">
                                <div class="chart-container">
                                    <canvas id="revenueChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-4">
                        <div class="card">
                            <div class="card-header">
                                <h5>Order Statistics</h5>
                            </div>
                            <div class="card-body">
                                <div class="chart-container">
                                    <canvas id="ordersChart"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>    <script>
        // Safe chart initialization with error handling
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize charts safely with static data to avoid infinite loops
            initializeCharts();
        });

        function initializeCharts() {
            try {
                // Revenue Chart with safe static data
                const revenueCanvas = document.getElementById('revenueChart');
                if (revenueCanvas) {
                    const revenueCtx = revenueCanvas.getContext('2d');
                    
                    new Chart(revenueCtx, {
                        type: 'line',
                        data: {
                            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                            datasets: [{
                                label: 'Revenue ($)',
                                data: [100, 150, 120, 180, 200, 250],
                                borderColor: 'rgb(75, 192, 192)',
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                tension: 0.1
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                                y: {
                                    beginAtZero: true
                                }
                            }
                        }
                    });
                }
            } catch (error) {
                console.error('Error initializing revenue chart:', error);
            }

            try {
                // Orders Chart with safe static data
                const ordersCanvas = document.getElementById('ordersChart');
                if (ordersCanvas) {
                    const ordersCtx = ordersCanvas.getContext('2d');
                    
                    new Chart(ordersCtx, {
                        type: 'doughnut',
                        data: {
                            labels: ['Pending', 'Processing', 'Completed'],
                            datasets: [{
                                data: [15, 25, 35],
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.8)',
                                    'rgba(255, 206, 86, 0.8)',
                                    'rgba(75, 192, 192, 0.8)'
                                ],
                                borderWidth: 2
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false
                        }
                    });
                }
            } catch (error) {
                console.error('Error initializing orders chart:', error);
            }
        }
    </script>
</body>
</html>