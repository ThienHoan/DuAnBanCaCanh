<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${not empty pageTitle}">
                ${pageTitle} - Fish Shop Blog
            </c:when>
            <c:otherwise>
                Blog - Fish Shop
            </c:otherwise>
        </c:choose>
    </title>
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/favicon.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/nice-select.css">
    <link rel="stylesheet" href="assets/css/slick.min.css">    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color.css">
      <!-- Custom CSS for Blog Layout -->
    <style>
        /* Blog Grid Layout Enhancement */
        .blog-posts-container {
            display: flex;
            flex-wrap: wrap;
            margin: 0 -15px;
        }
        
        .blog-post-column {
            padding: 0 15px;
            margin-bottom: 30px;
        }
        
        .post-item {
            height: 100%;
            display: flex;
            flex-direction: column;
            border: 1px solid #eee;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            background: #fff;
        }
        
        .post-item.animated {
            transition: all 0.3s ease;
        }
        
        .post-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        
        .post-item .thumbnail {
            flex-shrink: 0;
            position: relative;
            overflow: hidden;
        }
        
        .post-item .thumbnail img {
            width: 100%;
            height: 250px;
            object-fit: cover;
            display: block;
            transition: transform 0.3s ease;
        }
        
        .post-item:hover .thumbnail img,
        .post-item .thumbnail img.hover-scale {
            transform: scale(1.05);
        }
        
        .category-badge {
            position: absolute;
            top: 15px;
            left: 15px;
            background: rgba(0,123,255,0.9);
            color: white;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: 500;
            z-index: 2;
            transition: background 0.3s ease;
        }
        
        .post-item:hover .category-badge {
            background: rgba(0,123,255,1);
        }
        
        .post-content {
            padding: 20px;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }
        
        .post-meta {
            margin-bottom: 10px;
            color: #666;
            font-size: 14px;
        }
        
        .post-category {
            color: #007bff !important;
            font-weight: 500;
        }
        
        .post-name {
            margin-bottom: 15px;
            flex-grow: 1;
            line-height: 1.4;
        }
        
        .post-name a {
            color: #333;
            text-decoration: none;
            font-weight: 600;
            display: block;
            font-size: 16px;
            transition: color 0.3s ease;
        }
        
        .post-name a:hover {
            color: #007bff;
        }
        
        .post-summary {
            margin-bottom: 15px;
            color: #666;
            line-height: 1.6;
            font-size: 14px;
        }
        
        .post-footer {
            margin-top: auto;
            padding-top: 15px;
            border-top: 1px solid #eee;
        }
        
        .post-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .post-stats {
            color: #666;
            font-size: 14px;
        }
        
        .btn-primary.btn-sm {
            transition: all 0.3s ease;
        }
        
        .btn-primary.btn-sm:hover {
            transform: translateX(3px);
        }
        
        /* Filter and Search Enhancement */
        .filter-search-section {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 30px;
            border: 1px solid #dee2e6;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        
        .filter-search-section label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
        }
        
        .filter-search-section select,
        .filter-search-section input {
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        
        .filter-search-section select:focus,
        .filter-search-section input:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0,123,255,0.25);
            outline: none;
        }
          /* Pagination Enhancement */
        .biolife-panigations-block {
            text-align: center;
            margin: 30px 0;
        }
        
        .biolife-panigations-block .biolife-panigation {
            display: inline-block;
        }
        
        .biolife-panigations-block .biolife-panigation ul.panigation-contain {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-wrap: wrap;
            gap: 8px;
            margin: 0;
            padding: 0;
            list-style: none;
        }
        
        .biolife-panigations-block .biolife-panigation ul li {
            margin: 0;
            display: flex;
            align-items: center;
        }
        
        .biolife-panigations-block .biolife-panigation ul li a,
        .biolife-panigations-block .biolife-panigation ul li span {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 12px 16px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            transition: all 0.3s ease;
            text-decoration: none;
            color: #495057;
            font-weight: 600;
            min-width: 48px;
            min-height: 48px;
            background: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .biolife-panigations-block .biolife-panigation ul li a:hover {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(0,123,255,0.3);
        }
        
        .biolife-panigations-block .biolife-panigation ul li .current-page {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
            box-shadow: 0 4px 12px rgba(0,123,255,0.4);
            font-weight: 700;
        }
        
        .biolife-panigations-block .biolife-panigation ul li .sep {
            border: none;
            background: transparent;
            color: #6c757d;
            font-weight: bold;
            padding: 8px 4px;
            min-width: auto;
            box-shadow: none;
        }
        
        .biolife-panigations-block .biolife-panigation ul li .link-page.prev,
        .biolife-panigations-block .biolife-panigation ul li .link-page.next {
            padding: 12px 20px;
            font-weight: 600;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }
        
        .biolife-panigations-block .biolife-panigation ul li .link-page.prev:hover,
        .biolife-panigations-block .biolife-panigation ul li .link-page.next:hover {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
        }
        
        /* Loading State */
        body.loading {
            pointer-events: none;
        }
        
        /* Alert Enhancement */
        .alert {
            border-radius: 8px;
            border: none;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .alert-info {
            background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
            color: #0c5460;
        }
          /* Responsive Adjustments */
        @media (max-width: 768px) {
            .filter-search-section {
                padding: 20px 15px;
            }
            
            .blog-post-column {
                margin-bottom: 20px;
            }
            
            .post-content {
                padding: 15px;
            }
            
            .post-item .thumbnail img {
                height: 220px;
            }
            
            .biolife-panigations-block .biolife-panigation ul li a,
            .biolife-panigations-block .biolife-panigation ul li span {
                padding: 10px 14px;
                min-width: 44px;
                min-height: 44px;
                font-size: 14px;
            }
            
            .biolife-panigations-block .biolife-panigation ul li .link-page.prev,
            .biolife-panigations-block .biolife-panigation ul li .link-page.next {
                padding: 10px 16px;
            }
            
            .category-badge {
                font-size: 11px;
                padding: 4px 8px;
            }
        }
        
        @media (max-width: 576px) {
            .post-item .thumbnail img {
                height: 200px;
            }
            
            .post-content {
                padding: 12px;
            }
            
            .post-name a {
                font-size: 15px;
            }
            
            .filter-search-section .row > div {
                margin-bottom: 15px;
            }
            
            .filter-search-section .row > div:last-child {
                margin-bottom: 0;
            }
            
            .biolife-panigations-block .biolife-panigation ul.panigation-contain {
                gap: 6px;
            }
            
            .biolife-panigations-block .biolife-panigation ul li a,
            .biolife-panigations-block .biolife-panigation ul li span {
                padding: 8px 12px;
                min-width: 40px;
                min-height: 40px;
                font-size: 13px;
            }
            
            .biolife-panigations-block .biolife-panigation ul li .link-page.prev,
            .biolife-panigations-block .biolife-panigation ul li .link-page.next {
                padding: 8px 14px;
                font-size: 13px;
            }
        }
        
        /* Hover Animation for Blog Grid */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .post-item.animated {
            animation: fadeInUp 0.6s ease-out;
        }
        
        /* Staggered Animation */
        .post-item:nth-child(1) { animation-delay: 0.1s; }
        .post-item:nth-child(2) { animation-delay: 0.2s; }
        .post-item:nth-child(3) { animation-delay: 0.3s; }
        .post-item:nth-child(4) { animation-delay: 0.4s; }
        .post-item:nth-child(5) { animation-delay: 0.5s; }
        .post-item:nth-child(6) { animation-delay: 0.6s; }
    </style>
</head>
<body class="biolife-body">

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

    <!-- HEADER -->
    <jsp:include page="header.jsp"></jsp:include>   <!--Hero Section-->
    <div class="hero-section hero-background style-02">
        <h1 class="page-title">
            <c:choose>
                <c:when test="${not empty pageTitle}">
                    ${fn:escapeXml(pageTitle)}
                </c:when>
                <c:otherwise>
                    Blog cá cảnh
                </c:otherwise>
            </c:choose>
        </h1>
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/home" class="permal-link">Trang chủ</a></li>
                <li class="nav-item">
                    <c:choose>
                        <c:when test="${not empty selectedCategory}">
                            <a href="${pageContext.request.contextPath}/blog" class="permal-link">Blog</a>
                        </c:when>
                        <c:when test="${not empty searchQuery}">
                            <a href="${pageContext.request.contextPath}/blog" class="permal-link">Blog</a>
                        </c:when>
                        <c:otherwise>
                            <span class="current-page">Blog</span>
                        </c:otherwise>
                    </c:choose>
                </li>
                <c:if test="${not empty selectedCategory}">
                    <li class="nav-item"><span class="current-page">${fn:escapeXml(selectedCategory)}</span></li>
                </c:if>
                <c:if test="${not empty searchQuery}">
                    <li class="nav-item"><span class="current-page">Tìm kiếm</span></li>
                </c:if>
            </ul>
        </nav>
    </div>    <!-- Page Contain -->
    <div class="page-contain blog-page">
        <div class="container">
            <!-- Main content -->
            <div id="main-content" class="main-content">                  <!-- Filter và Search Bar -->
                <div class="filter-search-section">
                    <!-- DEBUG: Check categories -->
                    <!-- Categories count: ${fn:length(categories)} -->
                    <c:if test="${not empty categories}">
                        <!-- Categories found: 
                        <c:forEach var="cat" items="${categories}">
                            ${cat.categoryName} (${cat.slug}) | 
                        </c:forEach>
                        -->
                    </c:if>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="category-filter">
                                <label for="categoryFilter">
                                    <i class="fa fa-filter"></i> Lọc theo danh mục:
                                </label>                                <select id="categoryFilter" class="form-control" onchange="filterByCategory()" style="height: 45px;">
                                    <option value="">Tất cả danh mục</option>
                                    <!-- Debug: Categories count = ${fn:length(categories)} -->
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.slug}" ${selectedCategory eq category.slug ? 'selected' : ''}>
                                            ${category.categoryName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="search-form">
                                <label>
                                    <i class="fa fa-search"></i> Tìm kiếm bài viết:
                                </label>
                                <form action="${pageContext.request.contextPath}/blog-search" method="get" class="input-group">
                                    <input type="text" name="q" class="form-control" placeholder="Nhập từ khóa tìm kiếm..." 
                                           value="${searchQuery}" style="height: 45px;">
                                    <span class="input-group-btn">
                                        <button type="submit" class="btn btn-primary" style="height: 45px;">
                                            <i class="fa fa-search"></i> Tìm kiếm
                                        </button>
                                    </span>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>                <!-- Articles Grid -->
                <div class="blog-posts-container">
                    <c:choose>
                        <c:when test="${empty posts}">
                            <div class="col-xs-12">
                                <div class="alert alert-info text-center" style="padding: 40px; margin: 20px 0;">
                                    <h4><i class="fa fa-info-circle"></i> Chưa có bài viết nào</h4>
                                    <p>
                                        <c:choose>
                                            <c:when test="${not empty searchQuery}">
                                                Không tìm thấy bài viết nào với từ khóa: "<strong>${fn:escapeXml(searchQuery)}</strong>"
                                            </c:when>
                                            <c:when test="${not empty selectedCategory}">
                                                Chưa có bài viết nào trong danh mục: "<strong>${fn:escapeXml(selectedCategory)}</strong>"
                                            </c:when>
                                            <c:otherwise>
                                                Hệ thống chưa có bài viết nào được đăng.
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <a href="${pageContext.request.contextPath}/blog" class="btn btn-primary">
                                        <i class="fa fa-arrow-left"></i> Về trang blog
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="post" items="${posts}" varStatus="status">
                                <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12 blog-post-column">
                                    <article class="post-item effect-04 style-bottom-info">
                                        <div class="thumbnail">
                                            <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" class="link-to-post">
                                                <c:choose>
                                                    <c:when test="${not empty post.featuredImage}">
                                                        <img src="${fn:escapeXml(post.featuredImage)}" 
                                                             width="370" height="250" 
                                                             alt="${fn:escapeXml(post.title)}"
                                                             onerror="this.src='assets/images/our-blog/post-thumb-01.jpg'">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="assets/images/our-blog/post-thumb-01.jpg" 
                                                             width="370" height="250" 
                                                             alt="${fn:escapeXml(post.title)}">
                                                    </c:otherwise>
                                                </c:choose>                                                <!-- Category Badge -->
                                                <div class="category-badge" style="position: absolute; top: 15px; left: 15px; background: rgba(0,123,255,0.9); color: white; padding: 5px 10px; border-radius: 15px; font-size: 12px; font-weight: 500;">
                                                    <c:choose>
                                                        <c:when test="${post.categoryObject != null}">
                                                            ${fn:escapeXml(post.categoryObject.categoryName)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Chưa phân loại
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </a>
                                        </div>
                                        <div class="post-content">
                                            <div class="post-meta">
                                                <span class="post-date">
                                                    <i class="fa fa-calendar"></i> 
                                                    <fmt:formatDate value="${post.publishedAt != null ? post.publishedAt : post.createdAt}" 
                                                                    pattern="dd/MM/yyyy"/>
                                                </span>
                                                <span class="post-author" style="margin-left: 15px;">
                                                    <i class="fa fa-user"></i> ${fn:escapeXml(post.authorName)}
                                                </span>
                                            </div>
                                            <h4 class="post-name">
                                                <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" 
                                                   class="linktopost" 
                                                   title="${fn:escapeXml(post.title)}">
                                                    <c:choose>
                                                        <c:when test="${fn:length(post.title) > 60}">
                                                            ${fn:escapeXml(fn:substring(post.title, 0, 60))}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${fn:escapeXml(post.title)}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a>
                                            </h4>
                                            <div class="post-summary">
                                                <c:choose>
                                                    <c:when test="${not empty post.summary}">
                                                        <c:choose>
                                                            <c:when test="${fn:length(post.summary) > 120}">
                                                                ${fn:escapeXml(fn:substring(post.summary, 0, 120))}...
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${fn:escapeXml(post.summary)}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${fn:length(post.content) > 120}">
                                                                ${fn:escapeXml(fn:substring(post.content, 0, 120))}...
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${fn:escapeXml(post.content)}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="post-footer">
                                                <div class="post-actions">
                                                    <div class="post-stats">
                                                        <span class="view-count">
                                                            <i class="fa fa-eye"></i> ${post.viewCount} lượt xem
                                                        </span>
                                                    </div>
                                                    <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" 
                                                       class="btn btn-primary btn-sm">
                                                        Đọc tiếp <i class="fa fa-arrow-right"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </article>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>                </div>
                  <!-- Blog Statistics and Pagination -->
                <c:if test="${totalPages > 1 or not empty posts}">
                    <div class="blog-statistics-section" style="background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%); padding: 25px; border-radius: 12px; margin-top: 40px; border: 1px solid #dee2e6; box-shadow: 0 4px 15px rgba(0,0,0,0.08);">
                        <!-- Blog Info -->
                        <div class="blog-info" style="text-align: center; margin-bottom: 25px;">
                            <c:choose>
                                <c:when test="${not empty posts}">
                                    <div style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); display: inline-block;">
                                        <i class="fa fa-info-circle" style="color: #007bff; font-size: 18px; margin-right: 8px;"></i>
                                        <span style="color: #495057; font-size: 16px; font-weight: 600;">
                                            Hiển thị <strong style="color: #007bff;">${fn:length(posts)}</strong> trên tổng <strong style="color: #007bff;">${totalPosts}</strong> bài viết
                                        </span>
                                        <c:if test="${totalPages > 1}">
                                            <span style="color: #6c757d; margin-left: 10px; font-size: 14px;">
                                                • Trang <strong>${currentPage}</strong> / <strong>${totalPages}</strong>
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty selectedCategory}">
                                            <div style="margin-top: 8px; color: #6c757d; font-size: 14px;">
                                                <i class="fa fa-folder-o"></i> Danh mục: <strong style="color: #007bff;">${fn:escapeXml(selectedCategory)}</strong>
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty searchQuery}">
                                            <div style="margin-top: 8px; color: #6c757d; font-size: 14px;">
                                                <i class="fa fa-search"></i> Từ khóa: <strong style="color: #007bff;">"${fn:escapeXml(searchQuery)}"</strong>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); display: inline-block;">
                                        <i class="fa fa-exclamation-circle" style="color: #ffc107; font-size: 18px; margin-right: 8px;"></i>
                                        <span style="color: #495057; font-size: 16px; font-weight: 600;">
                                            Không tìm thấy bài viết nào
                                        </span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <!-- Pagination -->
                        <c:if test="${totalPages > 1}">
                            <div class="biolife-panigations-block">
                                <div class="biolife-panigation">
                                    <ul class="panigation-contain">
                                        <!-- Previous page -->
                                        <c:if test="${currentPage > 1}">
                                            <li>
                                                <a href="?page=${currentPage - 1}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                   class="link-page prev" title="Trang trước">
                                                    <i class="fa fa-angle-left" aria-hidden="true"></i> Trước
                                                </a>
                                            </li>
                                        </c:if>

                                        <!-- Page numbers -->
                                        <c:choose>
                                            <c:when test="${totalPages <= 7}">
                                                <!-- Show all pages if total <= 7 -->
                                                <c:forEach var="i" begin="1" end="${totalPages}">
                                                    <li>
                                                        <c:choose>
                                                            <c:when test="${i == currentPage}">
                                                                <span class="current-page" title="Trang hiện tại">${i}</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <a href="?page=${i}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                                   class="link-page" title="Đến trang ${i}">${i}</a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Complex pagination for many pages -->
                                                <c:choose>
                                                    <c:when test="${currentPage <= 4}">
                                                        <!-- Near beginning -->
                                                        <c:forEach var="i" begin="1" end="5">
                                                            <li>
                                                                <c:choose>
                                                                    <c:when test="${i == currentPage}">
                                                                        <span class="current-page">${i}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <a href="?page=${i}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                                           class="link-page">${i}</a>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </li>
                                                        </c:forEach>
                                                        <li><span class="sep">...</span></li>
                                                        <li>
                                                            <a href="?page=${totalPages}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                               class="link-page">${totalPages}</a>
                                                        </li>
                                                    </c:when>
                                                    <c:when test="${currentPage >= totalPages - 3}">
                                                        <!-- Near end -->
                                                        <li>
                                                            <a href="?page=1${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                               class="link-page">1</a>
                                                        </li>
                                                        <li><span class="sep">...</span></li>
                                                        <c:forEach var="i" begin="${totalPages - 4}" end="${totalPages}">
                                                            <li>
                                                                <c:choose>
                                                                    <c:when test="${i == currentPage}">
                                                                        <span class="current-page">${i}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <a href="?page=${i}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                                           class="link-page">${i}</a>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </li>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!-- Middle pages -->
                                                        <li>
                                                            <a href="?page=1${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                               class="link-page">1</a>
                                                        </li>
                                                        <li><span class="sep">...</span></li>
                                                        <c:forEach var="i" begin="${currentPage - 2}" end="${currentPage + 2}">
                                                            <li>
                                                                <c:choose>
                                                                    <c:when test="${i == currentPage}">
                                                                        <span class="current-page">${i}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <a href="?page=${i}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                                           class="link-page">${i}</a>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </li>
                                                        </c:forEach>
                                                        <li><span class="sep">...</span></li>
                                                        <li>
                                                            <a href="?page=${totalPages}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                               class="link-page">${totalPages}</a>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Next page -->
                                        <c:if test="${currentPage < totalPages}">
                                            <li>
                                                <a href="?page=${currentPage + 1}${not empty selectedCategory ? '&category=' + selectedCategory : ''}${not empty searchQuery ? '&q=' + searchQuery : ''}" 
                                                   class="link-page next" title="Trang sau">
                                                    Sau <i class="fa fa-angle-right" aria-hidden="true"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                    </ul>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </c:if>

            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <footer id="footer" class="footer layout-03">
        <div class="footer-content background-footer-03">
            <div class="container">
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-sm-9">
                        <section class="footer-item">
                            <a href="#" class="logo footer-logo"><img src="assets/images/organic-3.png" alt="biolife logo" width="135" height="34"></a>
                            <div class="footer-phone-info">
                                <i class="biolife-icon icon-head-phone"></i>
                                <p class="r-info">
                                    <span>Got Questions ?</span>
                                    <span>(700)  9001-1909  (900) 689 -66</span>
                                </p>
                            </div>
                            <div class="newsletter-block layout-01">
                                <h4 class="title">Newsletter Signup</h4>
                                <div class="form-content">
                                    <form action="#" name="new-letter-foter">
                                        <input type="email" class="input-text email" value="" placeholder="Your email here...">
                                        <button type="submit" class="bnt-submit" name="ok">Sign up</button>
                                    </form>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Useful Links</h3>
                            <div class="row">
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="#">About Us</a></li>
                                            <li><a href="#">About Our Shop</a></li>
                                            <li><a href="#">Secure Shopping</a></li>
                                            <li><a href="#">Delivery infomation</a></li>
                                            <li><a href="#">Privacy Policy</a></li>
                                            <li><a href="#">Our Sitemap</a></li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="#">Who We Are</a></li>
                                            <li><a href="#">Our Services</a></li>
                                            <li><a href="#">Projects</a></li>
                                            <li><a href="#">Contacts Us</a></li>
                                            <li><a href="#">Innovation</a></li>
                                            <li><a href="#">Testimonials</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Transport Offices</h3>
                            <div class="contact-info-block footer-layout xs-padding-top-10px">
                                <ul class="contact-lines">
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-location"></i>
                                            <b class="desc">7563 St. Vicent Place, Glasgow, Greater Newyork NH7689, UK </b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-phone"></i>
                                            <b class="desc">Phone: (+067) 234 789  (+068) 222 888</b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-letter"></i>
                                            <b class="desc">Email:  contact@company.com</b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-clock"></i>
                                            <b class="desc">Hours: 7 Days a week from 10:00 am</b>
                                        </p>
                                    </li>
                                </ul>
                            </div>
                            <div class="biolife-social inline">
                                <ul class="socials">
                                    <li><a href="#" title="twitter" class="socail-btn"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="facebook" class="socail-btn"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="pinterest" class="socail-btn"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="youtube" class="socail-btn"><i class="fa fa-youtube" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="instagram" class="socail-btn"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                </ul>
                            </div>
                        </section>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="separator sm-margin-top-70px xs-margin-top-40px"></div>
                    </div>
                    <div class="col-lg-6 col-sm-6 col-xs-12">
                        <div class="copy-right-text"><p><a href="templateshub.net">Templates Hub</a></p></div>
                    </div>
                    <div class="col-lg-6 col-sm-6 col-xs-12">
                        <div class="payment-methods">
                            <ul>
                                <li><a href="#" class="payment-link"><img src="assets/images/card1.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card2.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card3.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card4.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card5.jpg" width="51" height="36" alt=""></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!--Footer For Mobile-->
    <div class="mobile-footer">
        <div class="mobile-footer-inner">
            <div class="mobile-block block-menu-main">
                <a class="menu-bar menu-toggle btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                    <span class="fa fa-bars"></span>
                    <span class="text">Menu</span>
                </a>
            </div>
            <div class="mobile-block block-sidebar">
                <a class="menu-bar filter-toggle btn-toggle" data-object="open-mobile-filter" href="javascript:void(0)">
                    <i class="fa fa-sliders" aria-hidden="true"></i>
                    <span class="text">Sidebar</span>
                </a>
            </div>
            <div class="mobile-block block-minicart">
                <a class="link-to-cart" href="#">
                    <span class="fa fa-shopping-bag" aria-hidden="true"></span>
                    <span class="text">Cart</span>
                </a>
            </div>
            <div class="mobile-block block-global">
                <a class="menu-bar myaccount-toggle btn-toggle" data-object="global-panel-opened" href="javascript:void(0)">
                    <span class="fa fa-globe"></span>
                    <span class="text">Global</span>
                </a>
            </div>
        </div>
    </div>

    <div class="mobile-block-global">
        <div class="biolife-mobile-panels">
            <span class="biolife-current-panel-title">Global</span>
            <a class="biolife-close-btn" data-object="global-panel-opened" href="#">&times;</a>
        </div>
        <div class="block-global-contain">
            <div class="glb-item my-account">
                <b class="title">My Account</b>
                <ul class="list">
                    <li class="list-item"><a href="#">Login/register</a></li>
                    <li class="list-item"><a href="#">Wishlist <span class="index">(8)</span></a></li>
                    <li class="list-item"><a href="#">Checkout</a></li>
                </ul>
            </div>
            <div class="glb-item currency">
                <b class="title">Currency</b>
                <ul class="list">
                    <li class="list-item"><a href="#">€ EUR (Euro)</a></li>
                    <li class="list-item"><a href="#">$ USD (Dollar)</a></li>
                    <li class="list-item"><a href="#">£ GBP (Pound)</a></li>
                    <li class="list-item"><a href="#">¥ JPY (Yen)</a></li>
                </ul>
            </div>
            <div class="glb-item languages">
                <b class="title">Language</b>
                <ul class="list inline">
                    <li class="list-item"><a href="#"><img src="assets/images/languages/us.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/fr.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/ger.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/jap.jpg" alt="flag" width="24" height="18"></a></li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
    
    <!-- Custom Blog Scripts -->
    <script>
        // Category Filter Function
        function filterByCategory() {
            var categorySelect = document.getElementById('categoryFilter');
            var selectedCategory = categorySelect.value;
            var currentUrl = new URL(window.location.href);
            
            // Remove existing category and page parameters
            currentUrl.searchParams.delete('category');
            currentUrl.searchParams.delete('page');
            
            // Add new category if selected
            if (selectedCategory) {
                currentUrl.searchParams.set('category', selectedCategory);
            }
            
            // Redirect to filtered page
            window.location.href = currentUrl.toString();
        }
        
        // Smooth scroll to top when pagination is clicked
        $(document).ready(function() {
            // Smooth scroll for pagination links
            $('.panigation-contain a').on('click', function(e) {
                // Add loading effect
                $('body').addClass('loading');
                
                // Show loading overlay
                if ($('#blog-loading').length === 0) {
                    $('body').append('<div id="blog-loading" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(255,255,255,0.8); z-index: 9999; display: flex; justify-content: center; align-items: center;"><div style="text-align: center;"><i class="fa fa-spinner fa-spin fa-3x" style="color: #007bff;"></i><p style="margin-top: 15px; color: #666;">Đang tải...</p></div></div>');
                }
            });
            
            // Remove loading when page loads
            $(window).on('load', function() {
                $('#blog-loading').fadeOut(500, function() {
                    $(this).remove();
                });
                $('body').removeClass('loading');
            });
            
            // Smooth scroll to top for Better UX
            $('.btn-scroll-top').on('click', function(e) {
                e.preventDefault();
                $('html, body').animate({
                    scrollTop: 0
                }, 800);
            });
            
            // Auto-hide scroll top button
            $(window).scroll(function() {
                if ($(this).scrollTop() > 300) {
                    $('.btn-scroll-top').fadeIn();
                } else {
                    $('.btn-scroll-top').fadeOut();
                }
            });
            
            // Initialize scroll top button state
            if ($(window).scrollTop() <= 300) {
                $('.btn-scroll-top').hide();
            }
            
            // Search form enhancement
            $('.search-form form').on('submit', function(e) {
                var searchInput = $(this).find('input[name="q"]');
                var searchValue = searchInput.val().trim();
                
                if (!searchValue) {
                    e.preventDefault();
                    searchInput.focus();
                    searchInput.attr('placeholder', 'Vui lòng nhập từ khóa tìm kiếm!');
                    setTimeout(function() {
                        searchInput.attr('placeholder', 'Nhập từ khóa tìm kiếm...');
                    }, 3000);
                    return false;
                }
            });
            
            // Blog post hover effects
            $('.post-item').hover(
                function() {
                    $(this).find('.thumbnail img').addClass('hover-scale');
                },
                function() {
                    $(this).find('.thumbnail img').removeClass('hover-scale');
                }
            );
            
            // Responsive handling for mobile
            function handleMobileLayout() {
                if ($(window).width() <= 768) {
                    // Adjust pagination text for mobile
                    $('.link-page.prev').html('<i class="fa fa-angle-left"></i>');
                    $('.link-page.next').html('<i class="fa fa-angle-right"></i>');
                } else {
                    // Restore full text for desktop
                    $('.link-page.prev').html('<i class="fa fa-angle-left"></i> Trước');
                    $('.link-page.next').html('Sau <i class="fa fa-angle-right"></i>');
                }
            }
            
            // Initial call and resize handler
            handleMobileLayout();
            $(window).resize(handleMobileLayout);
            
            // Add animation class after page load for better performance
            setTimeout(function() {
                $('.post-item').addClass('animated');
            }, 500);
        });
        
        // Add keyboard navigation for accessibility
        $(document).keydown(function(e) {
            // Previous page with left arrow
            if (e.keyCode === 37 && $('.link-page.prev').length) {
                window.location.href = $('.link-page.prev').attr('href');
            }
            // Next page with right arrow
            if (e.keyCode === 39 && $('.link-page.next').length) {
                window.location.href = $('.link-page.next').attr('href');
            }
        });
    </script>
</body>

</html>
