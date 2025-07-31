<%@page import="model.entity.pOrder.Order"%>
<%@page import="dao.impl.OrderDAO"%>
<%@page import="dao.impl.ProductDAO"%>
<%@page import="dao.impl.UserDAO"%>
<%@page import="service.interfaces.BlogService"%>
<%@page import="service.impl.BlogServiceImpl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.*, java.math.BigDecimal, model.entity.*, dao.impl.*" %>
<%@page import="com.google.gson.Gson" %>

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
        totalRevenue = BigDecimal.ZERO;
    }
    
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
    
    // Lấy dữ liệu cho biểu đồ từ cơ sở dữ liệu
    Map<String, BigDecimal> monthlyRevenue = new HashMap<>();
    Map<String, Integer> orderStatistics = new HashMap<>();
    
    try {
        // Thử lấy doanh thu từ tất cả đơn hàng trước
        monthlyRevenue = orderDAO.getAllMonthlyRevenue();
        System.out.println("All Monthly Revenue from DAO: " + monthlyRevenue);
        
        // So sánh với method cũ
        Map<String, BigDecimal> filteredRevenue = orderDAO.getMonthlyRevenue();
        System.out.println("Filtered Monthly Revenue from DAO: " + filteredRevenue);
        
        // Debug: Kiểm tra tất cả đơn hàng và status
        List<Order> allOrders = orderDAO.getAllOrders();
        System.out.println("Total orders in database: " + (allOrders != null ? allOrders.size() : 0));
        if (allOrders != null) {
            Map<String, Integer> statusCount = new HashMap<>();
            for (Order order : allOrders) {
                if (order != null && order.getStatus() != null) {
                    String status = order.getStatus();
                    statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
                    System.out.println("Order #" + order.getOrderId() + " - Status: " + status + " - Amount: " + order.getTotalAmount());
                }
            }
            System.out.println("Status distribution: " + statusCount);
        }
        
        // Debug: Gọi method debug để xem dữ liệu chi tiết
        orderDAO.debugMonthlyOrders();
        
    } catch (Exception e) {
        System.out.println("Error getting monthly revenue: " + e.getMessage());
        e.printStackTrace();
        // Khởi tạo dữ liệu mặc định nếu có lỗi
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (String month : months) {
            monthlyRevenue.put(month, BigDecimal.ZERO);
        }
    }
    
    try {
        orderStatistics = orderDAO.getOrderStatistics();
        System.out.println("Order Statistics from DAO: " + orderStatistics);
    } catch (Exception e) {
        System.out.println("Error getting order statistics: " + e.getMessage());
        e.printStackTrace();
        // Khởi tạo dữ liệu mặc định nếu có lỗi
        orderStatistics.put("pending", 0);
        orderStatistics.put("processing", 0);
        orderStatistics.put("completed", 0);
        orderStatistics.put("cancelled", 0);
        orderStatistics.put("shipping", 0);
        orderStatistics.put("delivered", 0);
    }
    
    // Store in request scope
    request.setAttribute("totalUsers", totalUsers);
    request.setAttribute("totalProducts", totalProducts);
    request.setAttribute("totalOrders", totalOrders);
    request.setAttribute("totalRevenue", totalRevenue);
    request.setAttribute("totalBlogPosts", totalBlogPosts);
    request.setAttribute("totalCategories", totalCategories);
    request.setAttribute("monthlyRevenue", monthlyRevenue);
    request.setAttribute("orderStatistics", orderStatistics);
    
    // Convert data to JSON for JavaScript
    Gson gson = new Gson();
    String monthlyRevenueJson = gson.toJson(monthlyRevenue);
    String orderStatisticsJson = gson.toJson(orderStatistics);
    
    // Debug: In ra dữ liệu để kiểm tra
    System.out.println("Monthly Revenue JSON: " + monthlyRevenueJson);
    System.out.println("Order Statistics JSON: " + orderStatisticsJson);
    System.out.println("Monthly Revenue Map: " + monthlyRevenue);
    System.out.println("Order Statistics Map: " + orderStatistics);
    
    // Store JSON data in request scope for JSTL
    request.setAttribute("monthlyRevenueJson", monthlyRevenueJson);
    request.setAttribute("orderStatisticsJson", orderStatisticsJson);
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
<body>    <div class="container-fluid">
        <div class="row">
            <!-- Include Admin Sidebar -->
            <jsp:include page="admin/includes/sidebar.jsp">
                <jsp:param name="page" value="dashboard" />
            </jsp:include>

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
                                <small><a href="${pageContext.request.contextPath}/admin-blogs" class="text-white">Manage Posts</a></small>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Safe chart initialization with error handling
        document.addEventListener('DOMContentLoaded', function() {
            // Check if Chart.js is loaded
            if (typeof Chart === 'undefined') {
                console.error('Chart.js is not loaded!');
                return;
            }
            console.log('Chart.js is loaded successfully');
            
            // Initialize charts with dynamic data from database
            initializeCharts();
        });

        function initializeCharts() {
            try {
                // Revenue Chart with dynamic data from database
                const revenueCanvas = document.getElementById('revenueChart');
                if (revenueCanvas) {
                    const revenueCtx = revenueCanvas.getContext('2d');
                    
                    // Get monthly revenue data from database - handle empty data safely
                    let monthlyRevenueData = {};
                    try {
                        const monthlyRevenueJson = '${monthlyRevenueJson}';
                        console.log('Raw monthly revenue JSON:', monthlyRevenueJson);
                        if (monthlyRevenueJson && monthlyRevenueJson !== '{}' && monthlyRevenueJson !== 'null' && monthlyRevenueJson !== '') {
                            monthlyRevenueData = JSON.parse(monthlyRevenueJson);
                        } else {
                            console.log('Monthly revenue data is empty or null');
                            // Use test data if no real data
                            monthlyRevenueData = {
                                "Jan": 1000,
                                "Feb": 1500,
                                "Mar": 1200,
                                "Apr": 1800,
                                "May": 2000,
                                "Jun": 2500,
                                "Jul": 2200,
                                "Aug": 2800,
                                "Sep": 3000,
                                "Oct": 3500,
                                "Nov": 3200,
                                "Dec": 4000
                            };
                        }
                    } catch (e) {
                        console.log('Error parsing monthly revenue JSON:', e);
                        monthlyRevenueData = {};
                    }
                    
                    // Prepare data for chart
                    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                    const revenueData = months.map(month => {
                        return monthlyRevenueData[month] ? parseFloat(monthlyRevenueData[month]) : 0;
                    });
                    
                    console.log('Revenue data:', revenueData);
                    
                    new Chart(revenueCtx, {
                        type: 'line',
                        data: {
                            labels: months,
                            datasets: [{
                                label: 'Revenue ($)',
                                data: revenueData,
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
                                    beginAtZero: true,
                                    ticks: {
                                        callback: function(value) {
                                            return '$' + value.toLocaleString();
                                        }
                                    }
                                }
                            },
                            plugins: {
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            return 'Revenue: $' + context.parsed.y.toLocaleString();
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            } catch (error) {
                console.error('Error initializing revenue chart:', error);
            }

            try {
                // Orders Chart with dynamic data from database
                const ordersCanvas = document.getElementById('ordersChart');
                if (ordersCanvas) {
                    const ordersCtx = ordersCanvas.getContext('2d');
                    
                    // Get order statistics data from database - handle empty data safely
                    let orderStatisticsData = {};
                    try {
                        const orderStatisticsJson = '${orderStatisticsJson}';
                        console.log('Raw order statistics JSON:', orderStatisticsJson);
                        if (orderStatisticsJson && orderStatisticsJson !== '{}' && orderStatisticsJson !== 'null' && orderStatisticsJson !== '') {
                            orderStatisticsData = JSON.parse(orderStatisticsJson);
                        } else {
                            console.log('Order statistics data is empty or null');
                            // Use test data if no real data
                            orderStatisticsData = {
                                "pending": 5,
                                "processing": 3,
                                "completed": 8,
                                "cancelled": 2,
                                "shipping": 4,
                                "delivered": 6
                            };
                        }
                    } catch (e) {
                        console.log('Error parsing order statistics JSON:', e);
                        orderStatisticsData = {
                            "pending": 5,
                            "processing": 3,
                            "completed": 8,
                            "cancelled": 2,
                            "shipping": 4,
                            "delivered": 6
                        };
                    }
                    
                    console.log('Order statistics data:', orderStatisticsData);
                    
                    // Prepare data for chart
                    const statusLabels = [];
                    const statusData = [];
                    const statusColors = [
                        'rgba(255, 99, 132, 0.8)',   // Pending - Red
                        'rgba(255, 206, 86, 0.8)',   // Processing - Yellow
                        'rgba(75, 192, 192, 0.8)',   // Completed - Green
                        'rgba(153, 102, 255, 0.8)',  // Cancelled - Purple
                        'rgba(255, 159, 64, 0.8)',   // Shipping - Orange
                        'rgba(54, 162, 235, 0.8)'    // Delivered - Blue
                    ];
                    
                    let colorIndex = 0;
                    for (const [status, count] of Object.entries(orderStatisticsData)) {
                        // Chuyển đổi tên trạng thái sang tiếng Việt cho hiển thị
                        let displayName = status;
                        switch(status.toLowerCase()) {
                            case 'pending':
                                displayName = 'Chờ xử lý';
                                break;
                            case 'processing':
                                displayName = 'Đang xử lý';
                                break;
                            case 'confirmed':
                                displayName = 'Đã xác nhận';
                                break;
                            case 'shipping':
                                displayName = 'Đang giao';
                                break;
                            case 'delivered':
                                displayName = 'Đã giao';
                                break;
                            case 'completed':
                                displayName = 'Hoàn thành';
                                break;
                            case 'cancelled':
                                displayName = 'Đã hủy';
                                break;
                            default:
                                displayName = status.charAt(0).toUpperCase() + status.slice(1);
                        }
                        
                        statusLabels.push(displayName);
                        statusData.push(count);
                        colorIndex = (colorIndex + 1) % statusColors.length;
                    }
                    
                    // If no data, show default
                    if (statusLabels.length === 0) {
                        statusLabels.push('Không có đơn hàng');
                        statusData.push(1);
                        statusColors[0] = 'rgba(200, 200, 200, 0.8)';
                    }
                    
                    console.log('Status labels:', statusLabels);
                    console.log('Status data:', statusData);
                    
                    new Chart(ordersCtx, {
                        type: 'doughnut',
                        data: {
                            labels: statusLabels,
                            datasets: [{
                                data: statusData,
                                backgroundColor: statusColors.slice(0, statusLabels.length),
                                borderWidth: 2
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                            const percentage = ((context.parsed / total) * 100).toFixed(1);
                                            return context.label + ': ' + context.parsed + ' (' + percentage + '%)';
                                        }
                                    }
                                },
                                legend: {
                                    position: 'bottom',
                                    labels: {
                                        padding: 20,
                                        usePointStyle: true
                                    }
                                }
                            }
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