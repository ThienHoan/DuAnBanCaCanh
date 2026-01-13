<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
Component hiển thị avatar người dùng với fallback
Tham số:
- user: đối tượng User
- size: kích thước avatar (small, medium, large) - default: medium
- showName: hiển thị tên bên cạnh avatar (true/false) - default: false
--%>

<c:set var="avatarSize" value="${param.size != null ? param.size : 'medium'}" />
<c:set var="showName" value="${param.showName == 'true'}" />

<c:choose>
    <c:when test="${avatarSize == 'small'}">
        <c:set var="sizeClass" value="avatar-small" />
        <c:set var="dimensions" value="40px" />
    </c:when>
    <c:when test="${avatarSize == 'large'}">
        <c:set var="sizeClass" value="avatar-large" />
        <c:set var="dimensions" value="120px" />
    </c:when>
    <c:otherwise>
        <c:set var="sizeClass" value="avatar-medium" />
        <c:set var="dimensions" value="60px" />
    </c:otherwise>
</c:choose>

<div class="user-avatar-wrapper ${showName ? 'with-name' : ''}">
    <div class="user-avatar ${sizeClass}">
        <c:choose>
            <c:when test="${not empty user.avatar}">
                <img src="${pageContext.request.contextPath}/uploads/avatars/${user.avatar}" 
                     alt="${user.fullName}" 
                     class="avatar-img"
                     onerror="this.style.display='none'; this.nextElementSibling.style.display='block';" />
                <div class="avatar-placeholder" style="display: none;">
                    <i class="fas fa-user"></i>
                </div>
            </c:when>
            <c:otherwise>
                <div class="avatar-placeholder">
                    <c:choose>
                        <c:when test="${not empty user.fullName}">
                            ${fn:substring(user.fullName, 0, 1).toUpperCase()}
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-user"></i>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <c:if test="${showName}">
        <div class="user-name">
            <div class="name">${user.fullName}</div>
            <c:if test="${not empty user.role}">
                <div class="role">${user.role == 'admin' ? 'Quản trị viên' : 'Khách hàng'}</div>
            </c:if>
        </div>
    </c:if>
</div>

<style>
.user-avatar-wrapper {
    display: inline-flex;
    align-items: center;
    gap: 12px;
}

.user-avatar {
    position: relative;
    border-radius: 50%;
    overflow: hidden;
    background: #f8f9fa;
    border: 2px solid #e9ecef;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.avatar-small {
    width: 40px;
    height: 40px;
}

.avatar-medium {
    width: 60px;
    height: 60px;
}

.avatar-large {
    width: 120px;
    height: 120px;
}

.avatar-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
}

.avatar-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #73814B, #8fa05c);
    color: white;
    font-weight: bold;
}

.avatar-small .avatar-placeholder {
    font-size: 16px;
}

.avatar-medium .avatar-placeholder {
    font-size: 24px;
}

.avatar-large .avatar-placeholder {
    font-size: 48px;
}

.user-name .name {
    font-weight: 600;
    color: #333;
    margin-bottom: 2px;
}

.user-name .role {
    font-size: 12px;
    color: #666;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

/* Hover effects */
.user-avatar:hover {
    transform: scale(1.05);
    transition: transform 0.3s ease;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

/* Responsive */
@media (max-width: 768px) {
    .user-avatar-wrapper.with-name {
        flex-direction: column;
        text-align: center;
        gap: 8px;
    }
}
</style>
