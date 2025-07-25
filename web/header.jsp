
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<header id="header" class="header-area style-01 layout-04">
    <div class="header-top bg-main hidden-xs">
        <div class="container">
            <div class="top-bar left">
                <ul class="horizontal-menu">
                    <li><a href="#"><i class="fa fa-envelope" aria-hidden="true"></i>Organic@company.com</a></li>
                    <li><a href="#">Free Shipping for all Order of $99</a></li>
                </ul>
            </div>
            <div class="top-bar right">                <ul class="horizontal-menu">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">                            <li class="horz-menu-item user-info dropdown-account">                                <a href="#" class="user-greeting dropdown-toggle" style="color: #fff; margin-right: 10px; font-weight: 500; text-decoration: none;">
                                    <!-- DEBUG: Avatar value = ${sessionScope.user.avatar} -->
                                    <!-- Display user avatar or default icon -->
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.user.avatar}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(sessionScope.user.avatar, 'http')}">
                                                    <!-- Google avatar URL -->
                                                    <img src="${sessionScope.user.avatar}" alt="Avatar" style="width: 24px; height: 24px; border-radius: 50%; margin-right: 5px; vertical-align: middle;" onerror="console.log('Avatar load error:', this.src);">
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Local avatar file -->
                                                    <img src="${pageContext.request.contextPath}/uploads/avatars/${sessionScope.user.avatar}" alt="Avatar" style="width: 24px; height: 24px; border-radius: 50%; margin-right: 5px; vertical-align: middle;" onerror="console.log('Avatar load error:', this.src);">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Default user icon -->
                                            <i class="fa fa-user" aria-hidden="true" style="margin-right: 5px;"></i>
                                        </c:otherwise>
                                    </c:choose>
                                    Xin Chào, <strong>
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.user.fullName}">
                                                ${sessionScope.user.fullName}
                                            </c:when>
                                            <c:otherwise>
                                                ${sessionScope.user.username}
                                            </c:otherwise>
                                        </c:choose>
                                    </strong>!
                                    <i class="fa fa-chevron-down" aria-hidden="true" style="margin-left: 5px; font-size: 12px;"></i>
                                </a><ul class="dropdown-menu account-menu">
                                    <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i>Dashboard</a></li>
                                    <li><a href="${pageContext.request.contextPath}/profile"><i class="fa fa-user"></i>Thông tin cá nhân</a></li>
                                    <li><a href="${pageContext.request.contextPath}/profile?action=edit"><i class="fa fa-edit"></i>Chỉnh sửa thông tin</a></li>
                                    <li><a href="${pageContext.request.contextPath}/profile?action=change-password"><i class="fa fa-key"></i>Đổi mật khẩu</a></li>
                                    <li><a href="${pageContext.request.contextPath}/wishlist"><i class="fa fa-heart"></i>Danh sách yêu thích</a></li>
                                    <li><a href="${pageContext.request.contextPath}/order"><i class="fa fa-shopping-bag"></i>Đơn mua</a></li>
                                    <c:if test="${sessionScope.user.role == 'admin'}">
                                        <li class="divider"></li>
                                        <li><a href="admin-dashboard"><i class="fa fa-cog"></i>Admin Dashboard</a></li>
                                    </c:if>
                                    <li class="divider"></li>
                                    <li><a href="logout"><i class="fa fa-sign-out"></i>Đăng Xuất</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="horz-menu-item">
                                <a href="login" class="login-link">
                                    <i class="biolife-icon icon-login"></i>Login/Register
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
    <div class="header-middle biolife-sticky-object ">
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-2 col-md-6 col-xs-6">
                    <a href="home" class="biolife-logo"><img src="assets/images/organic-4.png" alt="biolife logo" width="135" height="36"></a>
                </div>
                <div class="col-lg-6 col-md-7 hidden-sm hidden-xs">
                    <div class="primary-menu">
                        <ul class="menu biolife-menu clone-main-menu clone-primary-menu" id="primary-menu" data-menuname="main menu">
                            <li class="menu-item"><a href="home">Home</a></li>
                            <li class="menu-item menu-item-has-children has-megamenu">
                                <a href="about-shop.jsp" class="menu-name" data-title="Shop" >About Shop</a>
                            </li>
                            <li class="menu-item menu-item-has-children has-child">
                                <a href="${pageContext.request.contextPath}/category" class="menu-name" data-title="Product">Product</a>
                                <ul class="sub-menu">
                                    
                                    <li class="parent"><a href="CategoryServlet?categoryId=0">Show All</a></li>
                                    <li class="menu-item menu-item-has-children has-child">
                                        <a href="CategoryServlet?categoryId=1&isParent=true" class="menu-name" data-title="Cá cảnh">Cá cảnh</a>
                                        <ul class="sub-menu">
                                            <li class="menu-item"><a href="CategoryServlet?categoryId=3">Cá nước ngọt</a></li>
                                            <li class="menu-item"><a href="CategoryServlet?categoryId=4">Cá nước lợ</a></li>
                                            <li class="menu-item"><a href="CategoryServlet?categoryId=5">Cá nước mặn</a></li>
                                        </ul>
                                    </li>
                                    
                                    <li class="menu-item menu-item-has-children has-child">
                                        <a href="CategoryServlet?categoryId=2&isParent=true" class="menu-name" data-title="Thiết bị và thực phẩm">Thiết bị và thực phẩm</a>
                                        <ul class="sub-menu">
                                            <li class="menu-item"><a href="CategoryServlet?categoryId=7">Thiết bị hồ cá</a></li>
                                            <li class="menu-item"><a href="CategoryServlet?categoryId=6">Thức ăn cho cá</a></li>
                                            <li class="menu-item"><a href="CategoryServlet?categoryId=8">Thuốc cho cá</a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                            <li class="menu-item menu-item-has-children has-megamenu">
                                <a href="blog" class="menu-name" data-title="Blog">Blog</a>
                                <div class="wrap-megamenu lg-width-800 md-width-750">
                                    <div class="mega-content">                                            <div class="col-lg-3 col-md-3 col-xs-6">
                                            <div class="wrap-custom-menu vertical-menu">
                                                <h4 class="menu-title">Blog Categories</h4>
                                                <ul class="menu">                                                          
                                                    <c:forEach var="category" items="${headerCategories}">
                                                        <li><a href="${pageContext.request.contextPath}/blog-category?categoryId=${category.categoryId}">${category.categoryName}</a></li>
                                                        </c:forEach>
                                                </ul>
                                            </div>
                                        </div>                                            <div class="col-lg-3 col-md-3 col-xs-6">
                                            <div class="wrap-custom-menu vertical-menu">
                                                <h4 class="menu-title">Featured Posts</h4>
                                                <ul class="menu">
                                                    <c:choose>
                                                        <c:when test="${not empty headerLatestPosts}">
                                                            <c:forEach var="post" items="${headerLatestPosts}" varStatus="status">
                                                                <li><a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}">${post.title}</a></li>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li><a href="${pageContext.request.contextPath}/blog">Xem tất cả bài viết</a></li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="col-lg-6 col-md-6 col-xs-12 md-margin-top-0 xs-margin-top-25px">
                                            <div class="block-posts">
                                                <h4 class="menu-title">Recent Posts</h4>                                                    
                                                <ul class="posts">
                                                    <c:choose>
                                                        <c:when test="${not empty headerLatestPosts}">
                                                            <c:forEach var="post" items="${headerLatestPosts}" varStatus="status">
                                                                <li>
                                                                    <div class="block-post-item">
                                                                        <div class="thumb">
                                                                            <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}">
                                                                                <c:choose>
                                                                                    <c:when test="${not empty post.featuredImage}">
                                                                                        <img src="${post.featuredImage}" width="100" height="73" alt="${post.title}">
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <img src="assets/images/megamenu/thumb-0${status.count + 4}.jpg" width="100" height="73" alt="${post.title}">
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </a>
                                                                        </div>
                                                                        <div class="left-info">
                                                                            <h4 class="post-name">
                                                                                <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}">
                                                                                    <c:choose>
                                                                                        <c:when test="${fn:length(post.title) > 40}">
                                                                                            ${fn:substring(post.title, 0, 40)}...
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            ${post.title}
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </a>
                                                                            </h4>
                                                                            <span class="p-date">
                                                                                <c:choose>
                                                                                    <c:when test="${not empty post.publishedAt}">
                                                                                        <fmt:formatDate value="${post.publishedAt}" pattern="MMM dd, yyyy"/>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <fmt:formatDate value="${post.createdAt}" pattern="MMM dd, yyyy"/>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </span>
                                                                            <span class="p-comment">${post.viewCount} Views</span>
                                                                        </div>
                                                                    </div>
                                                                </li>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li>
                                                                <div class="block-post-item">
                                                                    <div class="left-info">
                                                                        <h4 class="post-name">
                                                                            <a href="${pageContext.request.contextPath}/blog">Chưa có bài viết</a>
                                                                        </h4>
                                                                        <span class="p-date">Thêm bài viết mới</span>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                            <li class="menu-item"><a href="contact_us.jsp">Contact</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-lg-3 col-md-3 col-md-6 col-xs-6">
                    <div class="biolife-cart-info">
                        <div class="mobile-search">
                            <a href="javascript:void(0)" class="open-searchbox"><i class="biolife-icon icon-search"></i></a>
                            <div class="mobile-search-content">
                                <form action="#" class="form-search" name="mobile-seacrh" method="get">
                                    <a href="#" class="btn-close"><span class="biolife-icon icon-close-menu"></span></a>
                                    <input type="text" name="s" class="input-text" value="" placeholder="Search here...">
                                    <select name="category">
                                        <option value="-1" selected>All Categories</option>
                                        <option value="vegetables">Vegetables</option>
                                        <option value="fresh_berries">Fresh Berries</option>
                                        <option value="ocean_foods">Ocean Foods</option>
                                        <option value="butter_eggs">Butter & Eggs</option>
                                        <option value="fastfood">Fastfood</option>
                                        <option value="fresh_meat">Fresh Meat</option>
                                        <option value="fresh_onion">Fresh Onion</option>
                                        <option value="papaya_crisps">Papaya & Crisps</option>
                                        <option value="oatmeal">Oatmeal</option>
                                    </select>
                                    <button type="submit" class="btn-submit">go</button>
                                </form>
                            </div>
                        </div>                        <div class="wishlist-block hidden-sm hidden-xs">
                            <a href="${pageContext.request.contextPath}/wishlist" class="link-to">
                                <span class="icon-qty-combine">
                                    <i class="icon-heart-bold biolife-icon"></i>
                                    <span class="qty wishlist-count-badge" style="display: none;">0</span>
                                </span>
                            </a>
                        </div>
                        <div class="minicart-block">
                            <div class="minicart-contain">
                                <a href="javascript:void(0)" class="link-to">
                                    <span class="icon-qty-combine">
                                        <i class="icon-cart-mini biolife-icon"></i>
                                        <span class="qty">8</span>
                                    </span>
                                    <a href="cartClient"><span class="title">My Cart -</span></a>

                                    <span class="sub-total">$0.00</span>
                                </a>
                                <div class="cart-content">
                                    <div class="cart-inner">
                                        <ul class="products">
                                            <li>
                                                <div class="minicart-item">
                                                    <div class="thumb">
                                                        <a href="#"><img src="assets/images/minicart/pr-01.jpg" width="90" height="90" alt="National Fresh"></a>
                                                    </div>
                                                    <div class="left-info">
                                                        <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">ï¿½</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">ï¿½</span>95.00</span></del>
                                                        </div>
                                                        <div class="qty">
                                                            <label for="cart[id123][qty]">Qty:</label>
                                                            <input type="number" class="input-qty" name="cart[id123][qty]" id="cart[id123][qty]" value="1" disabled>
                                                        </div>
                                                    </div>
                                                    <div class="action">
                                                        <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                        <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="minicart-item">
                                                    <div class="thumb">
                                                        <a href="#"><img src="assets/images/minicart/pr-02.jpg" width="90" height="90" alt="National Fresh"></a>
                                                    </div>
                                                    <div class="left-info">
                                                        <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">ï¿½</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">ï¿½</span>95.00</span></del>
                                                        </div>
                                                        <div class="qty">
                                                            <label for="cart[id124][qty]">Qty:</label>
                                                            <input type="number" class="input-qty" name="cart[id124][qty]" id="cart[id124][qty]" value="1" disabled>
                                                        </div>
                                                    </div>
                                                    <div class="action">
                                                        <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                        <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="minicart-item">
                                                    <div class="thumb">
                                                        <a href="#"><img src="assets/images/minicart/pr-03.jpg" width="90" height="90" alt="National Fresh"></a>
                                                    </div>
                                                    <div class="left-info">
                                                        <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">ï¿½</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">ï¿½</span>95.00</span></del>
                                                        </div>
                                                        <div class="qty">
                                                            <label for="cart[id125][qty]">Qty:</label>
                                                            <input type="number" class="input-qty" name="cart[id125][qty]" id="cart[id125][qty]" value="1" disabled>
                                                        </div>
                                                    </div>
                                                    <div class="action">
                                                        <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                        <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="minicart-item">
                                                    <div class="thumb">
                                                        <a href="#"><img src="assets/images/minicart/pr-04.jpg" width="90" height="90" alt="National Fresh"></a>
                                                    </div>
                                                    <div class="left-info">
                                                        <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">ï¿½</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">ï¿½</span>95.00</span></del>
                                                        </div>
                                                        <div class="qty">
                                                            <label for="cart[id126][qty]">Qty:</label>
                                                            <input type="number" class="input-qty" name="cart[id126][qty]" id="cart[id126][qty]" value="1" disabled>
                                                        </div>
                                                    </div>
                                                    <div class="action">
                                                        <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                        <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="minicart-item">
                                                    <div class="thumb">
                                                        <a href="#"><img src="assets/images/minicart/pr-05.jpg" width="90" height="90" alt="National Fresh"></a>
                                                    </div>
                                                    <div class="left-info">
                                                        <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                        <div class="price">
                                                            <ins><span class="price-amount"><span class="currencySymbol">ï¿½</span>85.00</span></ins>
                                                            <del><span class="price-amount"><span class="currencySymbol">ï¿½</span>95.00</span></del>
                                                        </div>
                                                        <div class="qty">
                                                            <label for="cart[id127][qty]">Qty:</label>
                                                            <input type="number" class="input-qty" name="cart[id127][qty]" id="cart[id127][qty]" value="1" disabled>
                                                        </div>
                                                    </div>
                                                    <div class="action">
                                                        <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                        <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                        <p class="btn-control">
                                            <a href="#" class="btn view-cart">view cart</a>
                                            <a href="#" class="btn">checkout</a>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="mobile-menu-toggle">
                            <a class="btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                                <span></span>
                                <span></span>
                                <span></span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>    </div>
</header>

<!-- Wishlist JavaScript -->
<c:if test="${not empty sessionScope.user}">
<script>
    // Set context path for JavaScript
    window.contextPath = '${pageContext.request.contextPath}';
    
    // Wait for jQuery to be available
    function initProfileFeatures() {
        if (typeof jQuery === 'undefined') {
            setTimeout(initProfileFeatures, 100);
            return;
        }
        
        // Load wishlist count when page loads
        $(document).ready(function() {
            loadWishlistCount();
            initAccountDropdown();
        });
    }
    
    // Start initialization
    initProfileFeatures();
      function loadWishlistCount() {
        try {
            $.ajax({
                url: '${pageContext.request.contextPath}/wishlist?action=count',
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    updateWishlistCount(response.count);
                },
                error: function() {
                    // Silently fail for wishlist count
                    console.log('Could not load wishlist count');
                }
            });
        } catch (e) {
            console.log('Error in loadWishlistCount:', e);
        }
    }
    
    function updateWishlistCount(count) {
        try {
            const badge = $('.wishlist-count-badge');
            if (count > 0) {
                badge.text(count).show();
            } else {
                badge.hide();
            }
        } catch (e) {
            console.log('Error in updateWishlistCount:', e);
        }
    }
    
    function initAccountDropdown() {
        try {
            // Toggle dropdown on click
            $('.dropdown-toggle').on('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                
                const dropdown = $(this).closest('.dropdown-account');
                const isOpen = dropdown.hasClass('open');
                
                // Close all dropdowns
                $('.dropdown-account').removeClass('open');
                
                // Toggle current dropdown
                if (!isOpen) {
                    dropdown.addClass('open');
                }
            });
            
            // Close dropdown when clicking outside
            $(document).on('click', function(e) {
                if (!$(e.target).closest('.dropdown-account').length) {
                    $('.dropdown-account').removeClass('open');
                }
            });
            
            // Prevent dropdown from closing when clicking inside menu
            $('.dropdown-menu').on('click', function(e) {
                e.stopPropagation();
            });
        } catch (e) {
            console.log('Error in initAccountDropdown:', e);
        }
    }
</script>

<style>
/* CSS bảo vệ nhẹ cho header - chỉ bảo vệ dropdown và z-index */
#header {
    position: relative;
    z-index: 999;
}

/* Account Dropdown Styles */
#header .dropdown-account {
    position: relative;
    display: inline-block;
}

#header .dropdown-toggle {
    cursor: pointer;
    transition: all 0.3s ease;
}

#header .dropdown-toggle:hover {
    opacity: 0.8;
}

#header .dropdown-menu {
    position: absolute !important;
    top: 100% !important;
    right: 0 !important;
    background: white !important;
    border: 1px solid #e0e0e0 !important;
    border-radius: 8px !important;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1) !important;
    min-width: 220px !important;
    opacity: 0 !important;
    visibility: hidden !important;
    transform: translateY(-10px) !important;
    transition: all 0.3s ease !important;
    z-index: 10000 !important;
    margin-top: 10px !important;
    list-style: none !important;
    padding: 0 !important;
}

#header .dropdown-account.open .dropdown-menu {
    opacity: 1 !important;
    visibility: visible !important;
    transform: translateY(0) !important;
}

#header .dropdown-menu::before {
    content: '' !important;
    position: absolute !important;
    top: -6px !important;
    right: 20px !important;
    width: 12px !important;
    height: 12px !important;
    background: white !important;
    border-left: 1px solid #e0e0e0 !important;
    border-top: 1px solid #e0e0e0 !important;
    transform: rotate(45deg) !important;
}

#header .dropdown-menu li {
    list-style: none !important;
    margin: 0 !important;
    padding: 0 !important;
}

#header .dropdown-menu li a {
    display: block !important;
    padding: 12px 20px !important;
    color: #333 !important;
    text-decoration: none !important;
    font-size: 14px !important;
    font-weight: 400 !important;
    transition: all 0.3s ease !important;
    border-bottom: 1px solid #f5f5f5 !important;
}

#header .dropdown-menu li:last-child a {
    border-bottom: none !important;
}

#header .dropdown-menu li a:hover {
    background: #f8f9fa !important;
    color: #73814B !important;
    transform: translateX(3px) !important;
}

#header .dropdown-menu li a i {
    margin-right: 10px !important;
    width: 16px !important;
    text-align: center !important;
    color: #666 !important;
}

#header .dropdown-menu li a:hover i {
    color: #73814B !important;
}

#header .dropdown-menu .divider {
    height: 1px !important;
    background: #e0e0e0 !important;
    margin: 5px 0 !important;
    border: none !important;
}

/* Mobile responsive */
@media (max-width: 768px) {
    #header .dropdown-account {
        position: static;
    }
    
    #header .dropdown-menu {
        position: fixed !important;
        top: 60px !important;
        right: 10px !important;
        left: 10px !important;
        min-width: auto !important;
        max-width: none !important;
    }
    
    #header .dropdown-menu::before {
        display: none !important;
    }
}

@media (max-width: 480px) {
    #header .dropdown-menu {
        right: 5px !important;
        left: 5px !important;
    }
    
    #header .dropdown-menu li a {
        padding: 15px 20px !important;
        font-size: 16px !important;
    }
}

/* Smooth animations */
#header .dropdown-toggle .fa-chevron-down {
    transition: transform 0.3s ease;
}

#header .dropdown-account.open .dropdown-toggle .fa-chevron-down {
    transform: rotate(180deg);
}
</style>
</style>
</c:if>