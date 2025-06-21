<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Admin Sidebar Navigation -->
<nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
    <div class="position-sticky pt-3">
        <h4 class="text-white mb-4"><i class="bi bi-speedometer2"></i> Admin Panel</h4>
        <p class="text-white-50" style="text-align: center">Xin chào, ${user.fullName != null ? user.fullName : user.username}</p>
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link text-white ${param.page == 'dashboard' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-dashboard">
                    <i class="bi bi-house"></i> Dashboard
                </a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link text-white ${param.page == 'users' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-users">
                    <i class="bi bi-people"></i> Users
                </a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link text-white ${param.page == 'products' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-products">
                    <i class="bi bi-box"></i> Products
                </a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link text-white ${param.page == 'orders' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin-orders">
                    <i class="bi bi-cart"></i> Orders
                </a>
            </li>
            
            <!-- Blog Management Menu -->
            <li class="nav-item">
                <a class="nav-link text-white ${param.page == 'blog' || param.page == 'categories' ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#blogMenu" 
                   aria-expanded="${param.page == 'blog' || param.page == 'categories' ? 'true' : 'false'}">
                    <i class="bi bi-journal-text"></i> Blog Management <i class="bi bi-chevron-down ms-auto"></i>
                </a>
                <div class="collapse ${param.page == 'blog' || param.page == 'categories' ? 'show' : ''}" id="blogMenu">                    <ul class="nav flex-column ms-3">
                        <li class="nav-item">
                            <a class="nav-link text-white-50 ${param.page == 'blog' && param.subpage == 'list' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/admin-blogs">
                                <i class="bi bi-list-ul"></i> Danh sách bài viết
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white-50 ${param.page == 'blog' && param.subpage == 'form' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/admin-blogs?action=create">
                                <i class="bi bi-plus-circle"></i> Thêm bài viết
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white-50 ${param.page == 'categories' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/admin-categories">
                                <i class="bi bi-tags"></i> Quản lý danh mục
                            </a>
                        </li>
                    </ul>
                </div>
            </li>
            
            <!-- System Tools Menu -->
            <li class="nav-item">
                <a class="nav-link text-white ${param.page == 'system' ? 'active' : ''}" 
                   href="#" data-bs-toggle="collapse" data-bs-target="#systemMenu" 
                   aria-expanded="${param.page == 'system' ? 'true' : 'false'}">
                    <i class="bi bi-gear"></i> System Tools <i class="bi bi-chevron-down ms-auto"></i>
                </a>
                <div class="collapse ${param.page == 'system' ? 'show' : ''}" id="systemMenu">
                    <ul class="nav flex-column ms-3">
                        <li class="nav-item">
                            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/reset-all-identity">
                                <i class="bi bi-arrow-clockwise"></i> Reset All Identity Values
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/test-blog-category">
                                <i class="bi bi-bug"></i> Test Blog Categories
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/test-user">
                                <i class="bi bi-bug"></i> Test User Management
                            </a>
                        </li>
                    </ul>
                </div>
            </li>
            
            <hr class="text-white-50">
            
            <li class="nav-item">
                <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                    <i class="bi bi-arrow-left"></i> Back to Site
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

<!-- CSS cho sidebar (chỉ load một lần) -->
<c:if test="${empty sidebarCssLoaded}">
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .sidebar .nav-link {
            border-radius: 5px;
            margin: 2px 0;
            transition: all 0.3s ease;
        }
        .sidebar .nav-link:hover {
            background-color: rgba(255, 255, 255, 0.1);
            transform: translateX(5px);
        }
        .sidebar .nav-link.active {
            background-color: rgba(255, 255, 255, 0.2);
            font-weight: bold;
        }
        .sidebar .collapse .nav-link {
            padding-left: 2rem;
            font-size: 0.9rem;
        }
        .sidebar hr {
            margin: 1rem 0;
            opacity: 0.5;
        }
    </style>
    <c:set var="sidebarCssLoaded" value="true" scope="request"/>
</c:if>
