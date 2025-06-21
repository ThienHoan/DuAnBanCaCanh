<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    Wishlist Button Component
    Parameters:
    - productId: ID của sản phẩm
    - iconOnly: chỉ hiển thị icon không có text (optional, default: false)
--%>

<c:set var="isIconOnly" value="${iconOnly == true}" />

<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <!-- User is logged in - show functional wishlist button -->
        <c:choose>
            <c:when test="${isIconOnly}">
                <!-- Icon only button -->
                <button type="button" 
                        class="wishlist-heart" 
                        data-product-id="${productId}"
                        title="Thêm vào danh sách yêu thích">
                    <i class="fa fa-heart-o"></i>
                </button>
            </c:when>
            <c:otherwise>
                <!-- Button with text -->
                <button type="button" 
                        class="btn btn-wishlist btn-toggle-wishlist" 
                        data-product-id="${productId}">
                    <i class="fa fa-heart-o"></i>
                    <span class="btn-text">Yêu thích</span>
                </button>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <!-- User not logged in - show login prompt -->
        <c:choose>
            <c:when test="${isIconOnly}">
                <!-- Icon only button -->
                <button type="button" 
                        class="wishlist-heart-login" 
                        title="Đăng nhập để thêm vào danh sách yêu thích"
                        onclick="showLoginPrompt()">
                    <i class="fa fa-heart-o"></i>
                </button>
            </c:when>
            <c:otherwise>
                <!-- Button with text -->
                <button type="button" 
                        class="btn btn-wishlist" 
                        onclick="showLoginPrompt()">
                    <i class="fa fa-heart-o"></i>
                    <span class="btn-text">Yêu thích</span>
                </button>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
