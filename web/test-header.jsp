<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Header Blog Data</title>
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <!-- Include header -->
    <jsp:include page="header.jsp" />
    
    <div class="container" style="margin-top: 100px; padding: 50px;">
        <h1>Test Header Blog Data</h1>
        <p>Trang này test xem header có load được dữ liệu blog không.</p>
        <p>Hãy click vào menu "Blog" ở header để xem categories và featured posts.</p>
        
        <h2>Debug Info:</h2>
        <p>Header Categories: ${not empty headerCategories ? headerCategories.size() : 'null/empty'}</p>
        <p>Header Latest Posts: ${not empty headerLatestPosts ? headerLatestPosts.size() : 'null/empty'}</p>
        
        <h3>Categories Detail:</h3>
        <c:if test="${not empty headerCategories}">
            <ul>
                <c:forEach var="cat" items="${headerCategories}">
                    <li>${cat.categoryName} (${cat.slug})</li>
                </c:forEach>
            </ul>
        </c:if>
        
        <h3>Latest Posts Detail:</h3>
        <c:if test="${not empty headerLatestPosts}">
            <ul>
                <c:forEach var="post" items="${headerLatestPosts}">
                    <li>${post.title} (ID: ${post.postId})</li>
                </c:forEach>
            </ul>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/test-header-data" class="btn btn-primary">Test Raw Data</a>
        <a href="${pageContext.request.contextPath}/blog" class="btn btn-success">Go to Blog</a>
    </div>
    
    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.slicknav.js"></script>
    <script src="assets/js/functions.js"></script>
</body>
</html>
