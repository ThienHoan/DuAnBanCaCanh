<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Profile Test</title>
</head>
<body>
    <h1>Profile Test Page</h1>
    
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <p>Logged in as: ${sessionScope.user.fullName} (${sessionScope.user.username})</p>
            <p>User ID: ${sessionScope.user.userId}</p>
            <p>Role: ${sessionScope.user.role}</p>
            
            <h2>Test Links:</h2>
            <ul>
                <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                <li><a href="${pageContext.request.contextPath}/profile?action=edit">Edit Profile</a></li>
                <li><a href="${pageContext.request.contextPath}/profile?action=change-password">Change Password</a></li>
                <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
            </ul>
        </c:when>
        <c:otherwise>
            <p>Not logged in. <a href="${pageContext.request.contextPath}/login">Login here</a></p>
        </c:otherwise>
    </c:choose>
    
    <h2>Debug Info:</h2>
    <p>Context Path: ${pageContext.request.contextPath}</p>
    <p>Request URI: ${pageContext.request.requestURI}</p>
    <p>Server Name: ${pageContext.request.serverName}</p>
    <p>Server Port: ${pageContext.request.serverPort}</p>
</body>
</html>
