<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Kiểm tra đăng nhập --%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <title>Dashboard - ${sessionScope.user.fullName} | Fish Shop</title>
    <meta name="description" content="Dashboard cá nhân - Quản lý thông tin, đơn hàng và hoạt động tại Fish Shop">
    <meta name="robots" content="noindex, nofollow">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico">
      <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main-color.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">    <style>
        body {
            font-family: 'Roboto', Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
            line-height: 1.6;
        }
        
        .dashboard-container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .welcome-section {
            background: linear-gradient(135deg, #8bc8ec, #0038a8);
            border-radius: 20px;
            padding: 40px;
            margin-bottom: 40px;
            color: white;
            position: relative;
            overflow: hidden;
        }
        
        .welcome-section::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -50%;
            width: 200%;
            height: 200%;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="50" cy="50" r="2" fill="rgba(255,255,255,0.1)"/></svg>') repeat;
            animation: float 10s linear infinite;
        }
        
        @keyframes float {
            0% { transform: translateX(-100px) translateY(-100px); }
            100% { transform: translateX(100px) translateY(100px); }
        }
        
        .welcome-content {
            position: relative;
            z-index: 2;
            display: flex;
            align-items: center;
            gap: 30px;
        }
          .welcome-avatar {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            border: 4px solid rgba(255,255,255,0.3);
            background: rgba(255,255,255,0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 40px;
            font-weight: bold;
            overflow: hidden;
            position: relative;
        }
        
        .welcome-avatar img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border-radius: 50%;
        }
        
        .welcome-info h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
            font-weight: 700;
        }
        
        .welcome-info p {
            font-size: 1.1rem;
            opacity: 0.9;
            margin-bottom: 20px;
        }
        
        .quick-actions {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .quick-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 20px;
            background: rgba(255,255,255,0.2);
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: 500;
            transition: all 0.3s ease;
            backdrop-filter: blur(10px);
        }
        
        .quick-btn:hover {
            background: rgba(255,255,255,0.3);
            transform: translateY(-2px);
            color: white;
            text-decoration: none;
        }
        
        .stats-overview {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        
        .stat-card {
            background: white;
            border-radius: 16px;
            padding: 30px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            position: relative;
            overflow: hidden;
            transition: transform 0.3s ease;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
        }
        
        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: var(--card-color);
        }
        
        .stat-card.orders { --card-color: #73814B; }
        .stat-card.wishlist { --card-color: #e74c3c; }
        .stat-card.profile { --card-color: #3498db; }
        .stat-card.security { --card-color: #f39c12; }
        
        .stat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .stat-icon {
            width: 50px;
            height: 50px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
            background: var(--card-color);
        }
        
        .stat-number {
            font-size: 2.5rem;
            font-weight: 700;
            color: #333;
            margin-bottom: 8px;
        }
        
        .stat-label {
            color: #666;
            font-size: 14px;
            font-weight: 500;
        }
        
        .stat-trend {
            font-size: 12px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 4px;
        }
        
        .trend-up { color: #28a745; }
        .trend-down { color: #dc3545; }
        .trend-neutral { color: #6c757d; }
        
        .dashboard-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
            margin-bottom: 40px;
        }
        
        .main-content {
            display: flex;
            flex-direction: column;
            gap: 30px;
        }
        
        .sidebar-content {
            display: flex;
            flex-direction: column;
            gap: 30px;
        }
        
        .section-card {
            background: white;
            border-radius: 16px;
            padding: 30px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
        }
        
        .section-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #333;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .recent-activities {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        
        .activity-item {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .activity-item:last-child {
            border-bottom: none;
        }
        
        .activity-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            color: white;
            flex-shrink: 0;
        }
        
        .activity-icon.login { background: #28a745; }
        .activity-icon.profile { background: #17a2b8; }
        .activity-icon.wishlist { background: #e74c3c; }
        .activity-icon.order { background: #73814B; }
        
        .activity-content {
            flex: 1;
        }
        
        .activity-text {
            color: #333;
            font-weight: 500;
            margin-bottom: 4px;
        }
        
        .activity-time {
            color: #666;
            font-size: 12px;
        }
        
        /* Mobile responsive */
        @media (max-width: 992px) {
            .dashboard-grid {
                grid-template-columns: 1fr;
            }
            
            .welcome-content {
                flex-direction: column;
                text-align: center;
            }
            
            .welcome-info h1 {
                font-size: 2rem;
            }
        }
        
        @media (max-width: 768px) {
            .dashboard-container {
                padding: 15px;
            }
            
            .welcome-section {
                padding: 25px;
            }
            
            .stats-overview {
                grid-template-columns: 1fr;
                gap: 20px;
            }
            
            .quick-actions {
                justify-content: center;
            }
        }
    </style>
</head>

<body>
    <!-- Load jQuery first để header có thể sử dụng -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- Header -->
    <jsp:include page="../../header.jsp"></jsp:include>
    
    <!-- Breadcrumb -->
    <div class="hero-section hero-background">
        <h1 class="page-title">Dashboard</h1>
    </div>
    
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><span class="current-page">Dashboard</span></li>
            </ul>
        </nav>
    </div>

    <!-- Main Dashboard -->
    <div class="dashboard-container">
        <!-- Welcome Section -->
        <div class="welcome-section">
            <div class="welcome-content">                <div class="welcome-avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.avatar}">
                            <img src="${sessionScope.user.avatar}?v=${sessionScope.user.userId}" 
                                 alt="${sessionScope.user.fullName}" 
                                 style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;" />
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.fullName}">
                                    ${fn:substring(sessionScope.user.fullName, 0, 1).toUpperCase()}
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-user"></i>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="welcome-info">
                    <h1>Xin chào, ${sessionScope.user.fullName}!</h1>
                    <p>Chào mừng bạn trở lại Dashboard. Hãy xem tổng quan hoạt động của bạn.</p>
                    <div class="quick-actions">
                        <a href="${pageContext.request.contextPath}/profile?action=edit" class="quick-btn">
                            <i class="fas fa-edit"></i> Chỉnh sửa profile
                        </a>
                        <a href="${pageContext.request.contextPath}/wishlist" class="quick-btn">
                            <i class="fas fa-heart"></i> Danh sách yêu thích
                        </a>
                        <a href="${pageContext.request.contextPath}/profile?action=change-password" class="quick-btn">
                            <i class="fas fa-key"></i> Đổi mật khẩu
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Statistics Overview -->
        <div class="stats-overview">
            <div class="stat-card orders">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <div class="stat-trend trend-neutral">
                        <i class="fas fa-minus"></i> 0%
                    </div>
                </div>
                <div class="stat-number">0</div>
                <div class="stat-label">Tổng đơn hàng</div>
            </div>
            
            <div class="stat-card wishlist">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-heart"></i>
                    </div>
                    <div class="stat-trend trend-up">
                        <i class="fas fa-arrow-up"></i> +0%
                    </div>
                </div>
                <div class="stat-number" id="wishlist-count">0</div>
                <div class="stat-label">Sản phẩm yêu thích</div>
            </div>
            
            <div class="stat-card profile">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-user-circle"></i>
                    </div>
                    <div class="stat-trend trend-neutral">
                        <i class="fas fa-check"></i> 100%
                    </div>
                </div>
                <div class="stat-number">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.avatar and not empty sessionScope.user.phone}">
                            100
                        </c:when>
                        <c:when test="${not empty sessionScope.user.phone or not empty sessionScope.user.avatar}">
                            75
                        </c:when>
                        <c:otherwise>
                            50
                        </c:otherwise>
                    </c:choose>%
                </div>
                <div class="stat-label">Hoàn thiện profile</div>
            </div>
            
            <div class="stat-card security">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-shield-alt"></i>
                    </div>
                    <div class="stat-trend trend-up">
                        <i class="fas fa-arrow-up"></i> Tốt
                    </div>
                </div>
                <div class="stat-number">
                    <c:choose>
                        <c:when test="${sessionScope.user.status == 'active'}">
                            <i class="fas fa-check" style="color: #28a745; font-size: 2rem;"></i>
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-exclamation-triangle" style="color: #ffc107; font-size: 2rem;"></i>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Bảo mật tài khoản</div>
            </div>
        </div>

        <!-- Dashboard Grid -->
        <div class="dashboard-grid">
            <!-- Main Content -->
            <div class="main-content">
                <!-- Recent Orders -->
                <div class="section-card">
                    <h3 class="section-title">
                        <i class="fas fa-shopping-bag"></i>
                        Đơn hàng gần đây
                    </h3>
                    <div class="empty-state">
                        <i class="fas fa-shopping-cart" style="font-size: 3rem; color: #ddd; margin-bottom: 15px;"></i>
                        <p style="color: #666;">Bạn chưa có đơn hàng nào.</p>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                            <i class="fas fa-shopping-cart"></i> Bắt đầu mua sắm
                        </a>
                    </div>
                </div>

                <!-- Profile Completion -->
                <div class="section-card">
                    <h3 class="section-title">
                        <i class="fas fa-user-cog"></i>
                        Hoàn thiện thông tin cá nhân
                    </h3>
                    <div class="completion-checklist">
                        <div class="checklist-item ${not empty sessionScope.user.fullName ? 'completed' : ''}">
                            <i class="fas ${not empty sessionScope.user.fullName ? 'fa-check-circle' : 'fa-circle'}"></i>
                            <span>Họ và tên</span>
                        </div>
                        <div class="checklist-item ${not empty sessionScope.user.email ? 'completed' : ''}">
                            <i class="fas ${not empty sessionScope.user.email ? 'fa-check-circle' : 'fa-circle'}"></i>
                            <span>Email</span>
                        </div>
                        <div class="checklist-item ${not empty sessionScope.user.phone ? 'completed' : ''}">
                            <i class="fas ${not empty sessionScope.user.phone ? 'fa-check-circle' : 'fa-circle'}"></i>
                            <span>Số điện thoại</span>
                        </div>
                        <div class="checklist-item ${not empty sessionScope.user.avatar ? 'completed' : ''}">
                            <i class="fas ${not empty sessionScope.user.avatar ? 'fa-check-circle' : 'fa-circle'}"></i>
                            <span>Ảnh đại diện</span>
                        </div>
                    </div>
                    <c:if test="${empty sessionScope.user.phone or empty sessionScope.user.avatar}">
                        <div style="margin-top: 20px;">
                            <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-primary">
                                <i class="fas fa-edit"></i> Hoàn thiện thông tin
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Sidebar -->
            <div class="sidebar-content">
                <!-- Quick Stats -->
                <div class="section-card">
                    <h3 class="section-title">
                        <i class="fas fa-chart-bar"></i>
                        Thống kê nhanh
                    </h3>
                    <div class="quick-stats">
                        <div class="quick-stat-item">
                            <span class="label">Thành viên từ:</span>
                            <span class="value">
                                <fmt:formatDate value="${sessionScope.user.createdAt}" pattern="MM/yyyy" />
                            </span>
                        </div>
                        <div class="quick-stat-item">
                            <span class="label">Đăng nhập cuối:</span>
                            <span class="value">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user.lastLogin}">
                                        <fmt:formatDate value="${sessionScope.user.lastLogin}" pattern="dd/MM/yyyy" />
                                    </c:when>
                                    <c:otherwise>Hôm nay</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="quick-stat-item">
                            <span class="label">Trạng thái:</span>
                            <span class="value">
                                <c:choose>
                                    <c:when test="${sessionScope.user.status == 'active'}">
                                        <span style="color: #28a745;">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #ffc107;">Tạm khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Recent Activities -->
                <div class="section-card">
                    <h3 class="section-title">
                        <i class="fas fa-history"></i>
                        Hoạt động gần đây
                    </h3>
                    <ul class="recent-activities">
                        <li class="activity-item">
                            <div class="activity-icon login">
                                <i class="fas fa-sign-in-alt"></i>
                            </div>
                            <div class="activity-content">
                                <div class="activity-text">Đăng nhập thành công</div>
                                <div class="activity-time">Vừa xong</div>
                            </div>
                        </li>
                        <li class="activity-item">
                            <div class="activity-icon profile">
                                <i class="fas fa-user-edit"></i>
                            </div>
                            <div class="activity-content">
                                <div class="activity-text">Truy cập trang Dashboard</div>
                                <div class="activity-time">Vừa xong</div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>    <!-- Footer -->
    <%-- <jsp:include page="../../footer.jsp" /> --%>
    
    <!-- Load Bootstrap JS sau header -->
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    
    <!-- Load profile.js để có initAccountDropdown function -->
    <script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>
    
    <!-- Scripts -->
    <script>
        $(document).ready(function() {
            // Đảm bảo các script đã load xong trước khi gọi initAccountDropdown
            setTimeout(function() {
                if (typeof initAccountDropdown === 'function') {
                    initAccountDropdown();
                }
            }, 100);
            
            // Force reload avatar image to avoid cache
            refreshAvatarImage();
            
            // Load wishlist count
            loadWishlistCount();
            
            // Animate counters
            animateCounters();
        });
        
        function refreshAvatarImage() {
            const avatarImg = $('.welcome-avatar img');
            if (avatarImg.length > 0) {
                const currentSrc = avatarImg.attr('src');
                if (currentSrc) {
                    // Add timestamp to force reload
                    const timestamp = new Date().getTime();
                    const newSrc = currentSrc.split('?')[0] + '?t=' + timestamp;
                    avatarImg.attr('src', newSrc);
                }
            }
        }
        
        function loadWishlistCount() {
            $.ajax({
                url: '${pageContext.request.contextPath}/wishlist?action=count',
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        $('#wishlist-count').text(response.count);
                        
                        // Update trend
                        const trend = response.count > 0 ? 'trend-up' : 'trend-neutral';
                        $('.stat-card.wishlist .stat-trend')
                            .removeClass('trend-up trend-down trend-neutral')
                            .addClass(trend);
                    }
                },
                error: function() {
                    console.log('Failed to load wishlist count');
                }
            });
        }
        
        function animateCounters() {
            $('.stat-number').each(function() {
                const $this = $(this);
                const text = $this.text();
                
                // Only animate numeric values
                if (!isNaN(text) && text !== '') {
                    const target = parseInt(text);
                    $this.prop('Counter', 0).animate({
                        Counter: target
                    }, {
                        duration: 1500,
                        easing: 'swing',
                        step: function(now) {
                            $this.text(Math.ceil(now));
                        }
                    });
                }
            });
        }
    </script>
    
    <style>
        .empty-state {
            text-align: center;
            padding: 40px 20px;
        }
        
        .completion-checklist {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }
        
        .checklist-item {
            display: flex;
            align-items: center;
            gap: 12px;
            font-weight: 500;
        }
        
        .checklist-item.completed {
            color: #28a745;
        }
        
        .checklist-item:not(.completed) {
            color: #666;
        }
        
        .quick-stats {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        
        .quick-stat-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-bottom: 10px;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .quick-stat-item:last-child {
            border-bottom: none;
            padding-bottom: 0;
        }
        
        .quick-stat-item .label {
            color: #666;
            font-size: 14px;
        }
        
        .quick-stat-item .value {
            font-weight: 600;
            color: #333;
        }
    </style>
</body>
</html>
