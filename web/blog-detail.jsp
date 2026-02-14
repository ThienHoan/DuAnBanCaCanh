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
            <c:when test="${not empty post}">
                ${fn:escapeXml(post.title)} - Fish Shop Blog
            </c:when>
            <c:otherwise>
                Bài viết không tồn tại - Fish Shop Blog
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
    <link rel="stylesheet" href="assets/css/slick.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color.css">
    <style>
        .blog-detail-content {
            line-height: 1.8;
            font-size: 16px;
        }
        .blog-detail-content h1, .blog-detail-content h2, .blog-detail-content h3 {
            margin-top: 30px;
            margin-bottom: 20px;
            color: #333;
        }
        .blog-detail-content p {
            margin-bottom: 20px;
        }
        .blog-detail-content ul, .blog-detail-content ol {
            margin-bottom: 20px;
            padding-left: 30px;
        }
        .blog-detail-content li {
            margin-bottom: 8px;
        }
        .blog-meta {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .blog-meta .meta-item {
            display: inline-block;
            margin-right: 20px;
            margin-bottom: 10px;
        }
        .blog-meta .meta-item i {
            margin-right: 5px;
            color: #7faf51;
        }
        .blog-tags {
            margin-top: 30px;
        }
        .blog-tags .tag {
            display: inline-block;
            background: #7faf51;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            margin-right: 10px;
            margin-bottom: 10px;
            text-decoration: none;
        }
        .blog-tags .tag:hover {
            background: #6a9444;
            color: white;
        }
        .related-posts {
            margin-top: 50px;
        }
        .related-post-item {
            border: 1px solid #eee;
            border-radius: 8px;
            overflow: hidden;
            margin-bottom: 20px;
            transition: transform 0.3s ease;
        }
        .related-post-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .related-post-item img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }
        .related-post-content {
            padding: 15px;
        }
        .related-post-title {
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 10px;
        }
        .related-post-title a {
            color: #333;
            text-decoration: none;
        }
        .related-post-title a:hover {
            color: #7faf51;
        }
        .related-post-meta {
            font-size: 12px;
            color: #666;
        }
        .back-to-blog {
            margin-bottom: 30px;
        }
        .back-to-blog a {
            color: #7faf51;
            text-decoration: none;
            font-weight: 600;
        }
        .back-to-blog a:hover {
            color: #6a9444;
        }
        .social-share {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        .social-share h4 {
            margin-bottom: 15px;
            color: #333;
        }
        .social-share .share-btn {
            display: inline-block;
            padding: 10px 15px;
            margin-right: 10px;
            border-radius: 5px;
            color: white;
            text-decoration: none;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .share-facebook { background: #3b5998; }
        .share-twitter { background: #1da1f2; }
        .share-linkedin { background: #0077b5; }
        .share-email { background: #666; }
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
    <jsp:include page="header.jsp"></jsp:include>

    <!--Hero Section-->
    <div class="hero-section hero-background style-02">
        <h1 class="page-title">Chi tiết bài viết</h1>
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/home" class="permal-link">Trang chủ</a></li>
                <li class="nav-item"><a href="${pageContext.request.contextPath}/blog" class="permal-link">Blog</a></li>
                <li class="nav-item">                    <span class="current-page">
                        <c:choose>
                            <c:when test="${not empty post}">
                                <c:choose>
                                    <c:when test="${fn:length(post.title) > 30}">
                                        ${fn:substring(post.title, 0, 30)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${post.title}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                Bài viết
                            </c:otherwise>
                        </c:choose>
                    </span>
                </li>
            </ul>
        </nav>
    </div>

    <!-- Page Contain -->
    <div class="page-contain blog-detail-page">
        <div class="container">
            <div class="row">
                <!-- Main Content -->
                <div class="col-lg-9 col-md-8 col-sm-7 col-xs-12">
                    <div id="main-content" class="main-content">
                        
                        <!-- Back to Blog -->
                        <div class="back-to-blog">
                            <a href="${pageContext.request.contextPath}/blog">
                                <i class="fa fa-arrow-left"></i> Quay lại danh sách blog
                            </a>
                        </div>

                        <c:choose>
                            <c:when test="${empty post}">
                                <div class="alert alert-warning text-center">
                                    <h3>Bài viết không tồn tại</h3>
                                    <p>Bài viết bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                                    <a href="${pageContext.request.contextPath}/blog" class="btn btn-primary">Quay lại Blog</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <!-- Article Content -->
                                <article class="blog-detail-article">
                                    
                                    <!-- Article Header -->
                                    <header class="article-header">
                                        <h1 class="article-title">${fn:escapeXml(post.title)}</h1>
                                        
                                        <!-- Article Meta -->
                                        <div class="blog-meta">
                                            <div class="meta-item">
                                                <i class="fa fa-user"></i>
                                                <span>Tác giả: <strong>${fn:escapeXml(post.authorName)}</strong></span>
                                            </div>
                                            <div class="meta-item">
                                                <i class="fa fa-calendar"></i>
                                                <span>
                                                    <fmt:formatDate value="${post.publishedAt != null ? post.publishedAt : post.createdAt}" 
                                                                  pattern="dd/MM/yyyy HH:mm"/>
                                                </span>
                                            </div>                                            <div class="meta-item">
                                                <i class="fa fa-folder"></i>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${post.categoryObject != null}">
                                                            <a href="${pageContext.request.contextPath}/blog-category?categoryId=${post.categoryId}">
                                                                ${fn:escapeXml(post.categoryObject.categoryName)}
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span>Chưa phân loại</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                            <div class="meta-item">
                                                <i class="fa fa-eye"></i>
                                                <span>${post.viewCount} lượt xem</span>
                                            </div>
                                        </div>
                                    </header>

                                    <!-- Featured Image -->
                                    <c:if test="${not empty post.featuredImage}">
                                        <div class="article-featured-image" style="text-align: center; margin-bottom: 30px;">
                                            <img src="${post.featuredImage}" alt="${fn:escapeXml(post.title)}" 
                                                 style="max-width: 100%; height: auto; border-radius: 8px;">
                                        </div>
                                    </c:if>

                                    <!-- Article Content -->
                                    <div class="blog-detail-content">
                                        ${post.content}
                                    </div>

                                    <!-- Article Footer -->
                                    <footer class="article-footer">
                                        <!-- Tags -->
                                        <c:if test="${not empty post.tags}">
                                            <div class="blog-tags">
                                                <h4>Tags:</h4>
                                                <c:forEach var="tag" items="${fn:split(post.tags, ',')}">
                                                    <c:if test="${not empty fn:trim(tag)}">
                                                        <a href="${pageContext.request.contextPath}/blog-search?q=${fn:trim(tag)}" 
                                                           class="tag">${fn:trim(tag)}</a>
                                                    </c:if>
                                                </c:forEach>
                                            </div>
                                        </c:if>

                                        <!-- Social Share -->
                                        <div class="social-share">
                                            <h4>Chia sẻ bài viết:</h4>
                                            <a href="https://www.facebook.com/sharer/sharer.php?u=${pageContext.request.requestURL}" 
                                               target="_blank" class="share-btn share-facebook" rel="noopener">
                                                <i class="fa fa-facebook"></i> Facebook
                                            </a>
                                            <a href="https://twitter.com/intent/tweet?url=${pageContext.request.requestURL}&text=${fn:escapeXml(post.title)}" 
                                               target="_blank" class="share-btn share-twitter" rel="noopener">
                                                <i class="fa fa-twitter"></i> Twitter
                                            </a>
                                            <a href="https://www.linkedin.com/sharing/share-offsite/?url=${pageContext.request.requestURL}" 
                                               target="_blank" class="share-btn share-linkedin" rel="noopener">
                                                <i class="fa fa-linkedin"></i> LinkedIn
                                            </a>
                                            <a href="mailto:?subject=${fn:escapeXml(post.title)}&body=Xem bài viết này: ${pageContext.request.requestURL}" 
                                               class="share-btn share-email">
                                                <i class="fa fa-envelope"></i> Email
                                            </a>
                                        </div>
                                    </footer>
                                </article>

                                <!-- Related Posts -->
                                <c:if test="${not empty relatedPosts}">
                                    <div class="related-posts">
                                        <h3>Bài viết liên quan</h3>
                                        <div class="row">
                                            <c:forEach var="relatedPost" items="${relatedPosts}" end="2">
                                                <div class="col-md-6 col-sm-12">
                                                    <div class="related-post-item">
                                                        <a href="${pageContext.request.contextPath}/blog-detail?id=${relatedPost.postId}">
                                                            <c:choose>
                                                                <c:when test="${not empty relatedPost.featuredImage}">
                                                                    <img src="${relatedPost.featuredImage}" alt="${fn:escapeXml(relatedPost.title)}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="assets/images/our-blog/post-thumb-01.jpg" alt="${fn:escapeXml(relatedPost.title)}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                        <div class="related-post-content">
                                                            <h4 class="related-post-title">
                                                                <a href="${pageContext.request.contextPath}/blog-detail?id=${relatedPost.postId}">
                                                                    ${fn:escapeXml(relatedPost.title)}
                                                                </a>
                                                            </h4>                                                            <div class="related-post-meta">
                                                                <c:choose>
                                                                    <c:when test="${relatedPost.categoryObject != null}">
                                                                        <span>${fn:escapeXml(relatedPost.categoryObject.categoryName)}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span>Chưa phân loại</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                • 
                                                                <span>
                                                                    <fmt:formatDate value="${relatedPost.publishedAt != null ? relatedPost.publishedAt : relatedPost.createdAt}" 
                                                                                  pattern="dd/MM/yyyy"/>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Sidebar -->
                <div class="col-lg-3 col-md-4 col-sm-5 col-xs-12">
                    <div class="sidebar">
                        
                        <!-- Search Box -->
                        <div class="widget search-widget">
                            <h3 class="widget-title">Tìm kiếm</h3>
                            <form action="${pageContext.request.contextPath}/blog-search" method="get" class="search-form">
                                <input type="text" name="q" placeholder="Nhập từ khóa..." class="form-control">
                                <button type="submit" class="btn btn-search">
                                    <i class="fa fa-search"></i>
                                </button>
                            </form>
                        </div>

                        <!-- Categories -->
                        <c:if test="${not empty categories}">
                            <div class="widget categories-widget">
                                <h3 class="widget-title">Danh mục</h3>
                                <ul class="category-list">
                                    <c:forEach var="category" items="${categories}">
                                        <li>
                                            <a href="${pageContext.request.contextPath}/blog-category?category=${category.categoryName}">
                                                ${fn:escapeXml(category.categoryName)}
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>

                        <!-- Recent Posts -->
                        <div class="widget recent-posts-widget">
                            <h3 class="widget-title">Bài viết mới nhất</h3>
                            <div class="recent-posts">
                                <c:choose>
                                    <c:when test="${not empty relatedPosts}">
                                        <c:forEach var="recentPost" items="${relatedPosts}" end="4">
                                            <div class="recent-post-item">
                                                <div class="recent-post-thumb">
                                                    <a href="${pageContext.request.contextPath}/blog-detail?id=${recentPost.postId}">
                                                        <c:choose>
                                                            <c:when test="${not empty recentPost.featuredImage}">
                                                                <img src="${recentPost.featuredImage}" alt="${fn:escapeXml(recentPost.title)}" 
                                                                     style="width: 60px; height: 60px; object-fit: cover; border-radius: 4px;">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="assets/images/our-blog/post-thumb-01.jpg" alt="${fn:escapeXml(recentPost.title)}" 
                                                                     style="width: 60px; height: 60px; object-fit: cover; border-radius: 4px;">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </a>
                                                </div>
                                                <div class="recent-post-content">                                                    <h4 class="recent-post-title">
                                                        <a href="${pageContext.request.contextPath}/blog-detail?id=${recentPost.postId}">
                                                            <c:choose>
                                                                <c:when test="${fn:length(recentPost.title) > 50}">
                                                                    ${fn:substring(recentPost.title, 0, 50)}...
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${recentPost.title}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </a>
                                                    </h4>
                                                    <div class="recent-post-date">
                                                        <fmt:formatDate value="${recentPost.publishedAt != null ? recentPost.publishedAt : recentPost.createdAt}" 
                                                                      pattern="dd/MM/yyyy"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Chưa có bài viết nào.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <jsp:include page="footer.jsp" />

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
</body>

</html>
