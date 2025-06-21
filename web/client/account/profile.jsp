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
    <title>Thông tin cá nhân - Fish Shop</title>
    <meta name="description" content="Quản lý thông tin cá nhân, cập nhật profile và đổi mật khẩu tại Fish Shop">
    <meta name="robots" content="noindex, nofollow">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico">
      <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main-color.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <!-- Custom Profile CSS -->
    <style>
        .profile-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .profile-header {
            background: linear-gradient(135deg, #73814B, #8fa05c);
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            color: white;
            text-align: center;
        }
        
        .profile-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            border: 5px solid white;
            margin: 0 auto 20px;
            object-fit: cover;
            background: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
            color: #73814B;
        }
        
        .profile-name {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .profile-role {
            background: rgba(255, 255, 255, 0.2);
            padding: 5px 15px;
            border-radius: 20px;
            display: inline-block;
            font-size: 0.9rem;
        }
        
        .profile-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
        }
        
        .stat-icon {
            font-size: 2rem;
            color: #73814B;
            margin-bottom: 10px;
        }
        
        .stat-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }
        
        .stat-label {
            font-size: 0.9rem;
            color: #666;
        }
        
        .profile-tabs {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .tab-navigation {
            display: flex;
            background: #f8f9fa;
            border-bottom: 1px solid #dee2e6;
        }
        
        .tab-button {
            flex: 1;
            padding: 15px 20px;
            border: none;
            background: transparent;
            cursor: pointer;
            font-size: 1rem;
            color: #666;
            transition: all 0.3s ease;
            position: relative;
        }
        
        .tab-button.active {
            color: #73814B;
            background: white;
        }
        
        .tab-button.active::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: #73814B;
        }
        
        .tab-content {
            padding: 30px;
        }
        
        .tab-pane {
            display: none;
        }
        
        .tab-pane.active {
            display: block;
        }
        
        .info-group {
            margin-bottom: 20px;
        }
        
        .info-label {
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
            display: block;
        }
        
        .info-value {
            color: #666;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        
        .action-buttons {
            margin-top: 30px;
            text-align: center;
        }
        
        .btn-primary {
            background: #73814B;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 25px;
            text-decoration: none;
            display: inline-block;
            margin: 0 10px;
            transition: all 0.3s ease;
            cursor: pointer;
        }
        
        .btn-primary:hover {
            background: #8fa05c;
            transform: translateY(-2px);
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 25px;
            text-decoration: none;
            display: inline-block;
            margin: 0 10px;
            transition: all 0.3s ease;
            cursor: pointer;
        }
        
        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }
        
        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
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
        
        .security-info {
            background: #e7f3ff;
            border: 1px solid #b3d7ff;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .security-info h4 {
            color: #0066cc;
            margin-bottom: 10px;
        }
        
        @media (max-width: 768px) {
            .profile-container {
                padding: 10px;
            }
            
            .profile-header {
                padding: 20px;
            }
            
            .profile-avatar {
                width: 80px;
                height: 80px;
                font-size: 32px;
            }
            
            .profile-name {
                font-size: 1.5rem;
            }
            
            .tab-navigation {
                flex-direction: column;
            }
            
            .tab-button {
                text-align: left;
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
        <h1 class="page-title">Thông tin cá nhân</h1>
    </div>
    
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><span class="current-page">Thông tin cá nhân</span></li>
            </ul>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="profile-container">
        
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
        
        <!-- Profile Header -->
        <div class="profile-header">
            <div class="profile-avatar">                <c:choose>
                    <c:when test="${not empty user.avatar}">
                        <img src="${pageContext.request.contextPath}/uploads/avatars/${user.avatar}" alt="Avatar" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-user"></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="profile-name">${user.fullName}</div>
            <div class="profile-role">
                <c:choose>
                    <c:when test="${user.role == 'admin'}">Quản trị viên</c:when>
                    <c:when test="${user.role == 'staff'}">Nhân viên</c:when>
                    <c:otherwise>Khách hàng</c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <!-- Profile Stats -->
        <div class="profile-stats">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-calendar-alt"></i>
                </div>
                <div class="stat-value">
                    <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy" />
                </div>
                <div class="stat-label">Ngày tham gia</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-sign-in-alt"></i>
                </div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty user.lastLogin}">
                            <fmt:formatDate value="${user.lastLogin}" pattern="dd/MM/yyyy" />
                        </c:when>
                        <c:otherwise>Chưa có</c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Đăng nhập cuối</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-shield-alt"></i>
                </div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${user.status == 'active'}">
                            <span style="color: #28a745;">Hoạt động</span>
                        </c:when>
                        <c:when test="${user.status == 'inactive'}">
                            <span style="color: #ffc107;">Tạm khóa</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #dc3545;">Bị khóa</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Trạng thái tài khoản</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-heart"></i>
                </div>
                <div class="stat-value">0</div>
                <div class="stat-label">Sản phẩm yêu thích</div>
            </div>
        </div>
        
        <!-- Profile Tabs -->
        <div class="profile-tabs">
            <div class="tab-navigation">
                <button class="tab-button active" data-tab="info">
                    <i class="fas fa-user"></i> Thông tin cơ bản
                </button>
                <button class="tab-button" data-tab="security">
                    <i class="fas fa-lock"></i> Bảo mật
                </button>
                <button class="tab-button" data-tab="activity">
                    <i class="fas fa-history"></i> Hoạt động
                </button>
            </div>
            
            <div class="tab-content">
                <!-- Thông tin cơ bản -->
                <div class="tab-pane active" id="info">
                    <h3><i class="fas fa-user"></i> Thông tin cá nhân</h3>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="info-group">
                                <label class="info-label">Họ và tên:</label>
                                <div class="info-value">${user.fullName}</div>
                            </div>
                            
                            <div class="info-group">
                                <label class="info-label">Email:</label>
                                <div class="info-value">${user.email}</div>
                            </div>
                            
                            <div class="info-group">
                                <label class="info-label">Số điện thoại:</label>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty user.phone}">${user.phone}</c:when>
                                        <c:otherwise><em>Chưa cập nhật</em></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-6">
                            <div class="info-group">
                                <label class="info-label">Tên đăng nhập:</label>
                                <div class="info-value">${user.username}</div>
                            </div>
                            
                            <div class="info-group">
                                <label class="info-label">Vai trò:</label>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${user.role == 'admin'}">Quản trị viên</c:when>
                                        <c:when test="${user.role == 'staff'}">Nhân viên</c:when>
                                        <c:otherwise>Khách hàng</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            
                            <div class="info-group">
                                <label class="info-label">Ngày tạo tài khoản:</label>
                                <div class="info-value">
                                    <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn-primary">
                            <i class="fas fa-edit"></i> Chỉnh sửa thông tin
                        </a>
                    </div>
                </div>
                
                <!-- Bảo mật -->
                <div class="tab-pane" id="security">
                    <h3><i class="fas fa-lock"></i> Bảo mật tài khoản</h3>
                    
                    <div class="security-info">
                        <h4><i class="fas fa-info-circle"></i> Thông tin bảo mật</h4>
                        <p>Để bảo vệ tài khoản của bạn, hãy thường xuyên thay đổi mật khẩu và sử dụng mật khẩu mạnh.</p>
                        <ul>
                            <li>Mật khẩu nên có ít nhất 8 ký tự</li>
                            <li>Bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                            <li>Không sử dụng thông tin cá nhân dễ đoán</li>
                        </ul>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="info-group">
                                <label class="info-label">Mật khẩu:</label>
                                <div class="info-value">••••••••••</div>
                            </div>
                            
                            <div class="info-group">
                                <label class="info-label">Đăng nhập cuối:</label>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty user.lastLogin}">
                                            <fmt:formatDate value="${user.lastLogin}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:when>
                                        <c:otherwise>Chưa có thông tin</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-6">
                            <div class="info-group">
                                <label class="info-label">Trạng thái bảo mật:</label>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${user.status == 'active'}">
                                            <i class="fas fa-check-circle" style="color: #28a745;"></i> Tài khoản an toàn
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-exclamation-triangle" style="color: #ffc107;"></i> Cần kiểm tra
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/profile?action=change-password" class="btn-primary">
                            <i class="fas fa-key"></i> Đổi mật khẩu
                        </a>
                    </div>
                </div>
                
                <!-- Hoạt động -->
                <div class="tab-pane" id="activity">
                    <h3><i class="fas fa-history"></i> Hoạt động gần đây</h3>
                    
                    <div class="activity-list">
                        <div class="activity-item">
                            <div class="activity-icon">
                                <i class="fas fa-sign-in-alt"></i>
                            </div>
                            <div class="activity-content">
                                <div class="activity-title">Đăng nhập vào hệ thống</div>
                                <div class="activity-time">
                                    <c:choose>
                                        <c:when test="${not empty user.lastLogin}">
                                            <fmt:formatDate value="${user.lastLogin}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:when>
                                        <c:otherwise>Chưa có hoạt động</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        
                        <div class="activity-item">
                            <div class="activity-icon">
                                <i class="fas fa-user-plus"></i>
                            </div>
                            <div class="activity-content">
                                <div class="activity-title">Tạo tài khoản</div>
                                <div class="activity-time">
                                    <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
      <!-- Footer -->
    <%-- <jsp:include page="../components/footer.jsp" /> --%>
      <!-- Scripts -->
    <script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>
    <script>
        // Khởi tạo lại dropdown sau khi trang load
        $(document).ready(function() {
            if (typeof initAccountDropdown === 'function') {
                initAccountDropdown();
            }
        });
        
        // Tab switching
        document.querySelectorAll('.tab-button').forEach(button => {
            button.addEventListener('click', function() {
                // Remove active class from all buttons and panes
                document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
                document.querySelectorAll('.tab-pane').forEach(pane => pane.classList.remove('active'));
                
                // Add active class to clicked button
                this.classList.add('active');
                
                // Show corresponding pane
                const tabId = this.getAttribute('data-tab');
                document.getElementById(tabId).classList.add('active');
            });
        });
        
        // Auto hide alerts after 5 seconds
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 300);
            });
        }, 5000);
    </script>
    
    <style>
        .activity-list {
            max-width: 600px;
        }
        
        .activity-item {
            display: flex;
            align-items: center;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }
        
        .activity-item:last-child {
            border-bottom: none;
        }
        
        .activity-icon {
            width: 40px;
            height: 40px;
            background: #f8f9fa;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            color: #73814B;
        }
        
        .activity-content {
            flex: 1;
        }
        
        .activity-title {
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }
        
        .activity-time {
            font-size: 0.9rem;
            color: #666;
        }
    </style>
</body>

</html>
