<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Top Admin Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <!-- Brand/Logo -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin-dashboard">
            <i class="bi bi-fish"></i> Fish Shop Admin
        </a>

        <!-- Mobile toggle button -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar content -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Main navigation links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link ${param.page == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-dashboard">
                        <i class="bi bi-house"></i> Dashboard
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link ${param.page == 'users' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-users">
                        <i class="bi bi-people"></i> Users
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link ${param.page == 'products' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-products">
                        <i class="bi bi-box"></i> Products
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link ${param.page == 'orders' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-orders">
                        <i class="bi bi-cart"></i> Orders
                    </a>
                </li>
                
                <!-- Blog dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page == 'blog' || param.page == 'categories' ? 'active' : ''}" 
                       href="#" id="blogDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-journal-text"></i> Blog
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="blogDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin-blogs">
                            <i class="bi bi-list-ul"></i> Danh sách bài viết
                        </a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin-blogs?action=create">
                            <i class="bi bi-plus-circle"></i> Thêm bài viết
                        </a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin-categories">
                            <i class="bi bi-tags"></i> Quản lý danh mục
                        </a></li>
                    </ul>
                </li>
                
                <!-- System Tools dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page == 'system' ? 'active' : ''}" 
                       href="#" id="systemDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-gear"></i> System
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="systemDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/reset-all-identity">
                            <i class="bi bi-arrow-clockwise"></i> Reset Identity Values
                        </a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/test-blog-category">
                            <i class="bi bi-bug"></i> Test Blog Categories
                        </a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/test-user">
                            <i class="bi bi-bug"></i> Test User Management
                        </a></li>
                    </ul>
                </li>
            </ul>

            <!-- Right side items -->
            <ul class="navbar-nav ms-auto">
                <!-- User info -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle"></i> ${user.fullName != null ? user.fullName : user.username}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                            <i class="bi bi-person"></i> Profile
                        </a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/settings">
                            <i class="bi bi-gear"></i> Settings
                        </a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-arrow-left"></i> Back to Site
                        </a></li>
                        <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                            <i class="bi bi-box-arrow-right"></i> Đăng xuất
                        </a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- CSS cho navbar (chỉ load một lần) -->
<c:if test="${empty navbarCssLoaded}">
    <style>
        .navbar-brand {
            font-weight: bold;
            font-size: 1.25rem;
        }
        
        .navbar-nav .nav-link {
            transition: all 0.3s ease;
            border-radius: 4px;
            margin: 0 2px;
        }
        
        .navbar-nav .nav-link:hover {
            background-color: rgba(255, 255, 255, 0.1);
            transform: translateY(-1px);
        }
        
        .navbar-nav .nav-link.active {
            background-color: rgba(255, 255, 255, 0.2);
            font-weight: bold;
        }
        
        .dropdown-menu {
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            border: none;
        }
        
        .dropdown-item {
            transition: all 0.2s ease;
        }
        
        .dropdown-item:hover {
            background-color: #f8f9fa;
            transform: translateX(5px);
        }
        
        .dropdown-item.text-danger:hover {
            background-color: #f8d7da;
        }
        
        /* Make sure content doesn't get hidden behind fixed navbar */
        body {
            padding-top: 56px;
        }
        
        /* Responsive adjustments */
        @media (max-width: 991.98px) {
            .navbar-nav .nav-link {
                margin: 2px 0;
            }
        }
    </style>
    <c:set var="navbarCssLoaded" value="true" scope="request"/>
</c:if>
