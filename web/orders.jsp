<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng của tôi - Fish Shop</title>
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/animate.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/nice-select.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/slick.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main-color.css">
    
    <style>
        .order-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .order-title {
            font-size: 24px;
            font-weight: 600;
            margin-bottom: 10px;
        }
        .order-filters {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        .filter-group {
            display: flex;
            align-items: center;
        }
        .filter-label {
            margin-right: 10px;
            font-weight: 500;
        }
        .order-table {
            width: 100%;
            border-collapse: collapse;
        }
        .order-table th, .order-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }
        .order-table th {
            background-color: #f5f5f5;
            font-weight: 600;
        }
        .status-badge, .payment-badge {
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
            display: inline-block;
            text-align: center;
            min-width: 100px;
        }
        .status-pending {
            background-color: #fff8e1;
            color: #ffa000;
        }
        .status-confirmed {
            background-color: #e3f2fd;
            color: #1976d2;
        }
        .status-processing {
            background-color: #e8f5e9;
            color: #388e3c;
        }
        .status-shipping {
            background-color: #e0f7fa;
            color: #0097a7;
        }
        .status-delivered {
            background-color: #e8f5e9;
            color: #388e3c;
        }
        .status-cancelled {
            background-color: #ffebee;
            color: #d32f2f;
        }
        .payment-pending {
            background-color: #fff8e1;
            color: #ffa000;
        }
        .payment-paid {
            background-color: #e8f5e9;
            color: #388e3c;
        }
        .payment-failed {
            background-color: #ffebee;
            color: #d32f2f;
        }
        .payment-refunded {
            background-color: #f3e5f5;
            color: #7b1fa2;
        }
        .action-buttons {
            display: flex;
            gap: 5px;
            flex-wrap: wrap;
        }
        .btn-action {
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
            border: none;
            cursor: pointer;
            background-color: #f5f5f5;
            color: #333;
        }
        .btn-view {
            background-color: #e3f2fd;
            color: #1976d2;
        }
        .btn-confirm {
            background-color: #e8f5e9;
            color: #388e3c;
        }
        .btn-cancel {
            background-color: #ffebee;
            color: #d32f2f;
        }
        .btn-ship {
            background-color: #e0f7fa;
            color: #0097a7;
        }
        .btn-refund {
            background-color: #f3e5f5;
            color: #7b1fa2;
        }
        .btn-action:hover {
            opacity: 0.9;
        }
        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
            gap: 5px;
        }
        .pagination a, .pagination span {
            padding: 8px 12px;
            border-radius: 4px;
            text-decoration: none;
        }
        .pagination a {
            background-color: #f5f5f5;
            color: #333;
        }
        .pagination span.active {
            background-color: #1976d2;
            color: white;
        }
        .no-orders {
            text-align: center;
            padding: 40px 0;
        }
        
        /* CSS cho bộ lọc kích thước đều nhau */
        .search-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .search-input,
        .status-filter,
        .search-button {
            height: 40px;
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            flex: 1; /* Chia đều kích thước */
        }
        
        .search-input {
            background-color: #fff;
            color: #333;
        }
        
        .search-input:focus {
            outline: none;
            border-color: #1976d2;
        }
        
        .status-filter {
            background-color: #fff;
            color: #333;
            cursor: pointer;
        }
        
        .status-filter:focus {
            outline: none;
            border-color: #1976d2;
        }
        
        .search-button {
            background-color: #1976d2;
            color: white;
            border: none;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
        }
        
        .search-button:hover {
            background-color: #1565c0;
        }
        
        /* Responsive */
        @media (max-width: 768px) {
            .search-form {
                flex-direction: column;
            }
            
            .search-input,
            .status-filter,
            .search-button {
                width: 100%;
            }
        }
        
        /* Ẩn các phần tử không cần thiết của nice-select */
        .nice-select, .nice-select-dropdown, .list, .nice-select:after {
            display: none !important;
        }
        #statusFilter + div {
            display: none !important;
        }
    </style>
</head>
<body>
    <!-- HEADER -->
    <jsp:include page="header.jsp" />
    
    <!-- MAIN CONTENT -->
    <div class="page-contain">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="order-container">
                        <div class="order-header">
                            <h1 class="order-title">Đơn hàng của tôi</h1>
                        </div>
                        
                        <!-- Thông báo lỗi/thành công -->
                        <c:if test="${not empty param.error}">
                            <div class="alert alert-danger" role="alert">
                                ${param.error}
                            </div>
                        </c:if>
                        <c:if test="${not empty param.message}">
                            <div class="alert alert-success" role="alert">
                                ${param.message}
                            </div>
                        </c:if>
                        
                        <!-- Bộ lọc đơn hàng -->
                        <div class="order-filters">
                            <form action="${pageContext.request.contextPath}/order" method="get" class="search-form">
                                <input type="text" 
                                       name="keyword" 
                                       value="${keyword}" 
                                       placeholder="Tìm kiếm đơn hàng..." 
                                       class="search-input">
                                       
                                <select name="status" id="statusFilter" class="status-filter">
                                    <option value="all" ${status == 'all' || empty status ? 'selected' : ''}>Tất cả trạng thái</option>
                                    <option value="pending" ${status == 'pending' ? 'selected' : ''}>Chờ xác nhận</option>
                                    <option value="confirmed" ${status == 'confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                                    <option value="processing" ${status == 'processing' ? 'selected' : ''}>Đang xử lý</option>
                                    <option value="shipping" ${status == 'shipping' ? 'selected' : ''}>Đang giao hàng</option>
                                    <option value="delivered" ${status == 'delivered' ? 'selected' : ''}>Đã giao hàng</option>
                                    <option value="cancelled" ${status == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
                                </select>
                                
                                <button type="submit" class="search-button">
                                    <i class="fa fa-search"></i>
                                    Tìm kiếm
                                </button>
                            </form>
                        </div>
                        
                        <c:choose>
                            <c:when test="${empty orders}">
                                <div class="no-orders">
                                    <h3>Không tìm thấy đơn hàng nào</h3>
                                    <p>Bạn chưa có đơn hàng nào hoặc không có đơn hàng phù hợp với tiêu chí tìm kiếm.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="order-table">
                                        <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <c:if test="${sessionScope.user.role == 'admin'}">
                                                    <th>Khách hàng</th>
                                                </c:if>
                                                <th>Ngày đặt</th>
                                                <th>Tổng tiền</th>
                                                <th>Phương thức thanh toán</th>
                                                <th>Trạng thái thanh toán</th>
                                                <th>Trạng thái đơn hàng</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="order" items="${orders}">
                                                <tr>
                                                    <td>${order.orderNumber}</td>
                                                    <c:if test="${sessionScope.user.role == 'admin'}">
                                                        <td>${order.customerName != null ? order.customerName : 'Khách hàng #'.concat(order.userId)}</td>
                                                    </c:if>
                                                    <td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                                                    <td><fmt:formatNumber value="${order.finalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${order.paymentMethod == 'cod'}">Thanh toán khi nhận hàng</c:when>
                                                            <c:when test="${order.paymentMethod == 'e-wallet'}">Ví điện tử</c:when>
                                                            <c:otherwise>${order.paymentMethod}</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <span class="payment-badge payment-${order.paymentStatus}">
                                                            <c:choose>
                                                                <c:when test="${order.paymentStatus == 'pending'}">Chưa thanh toán</c:when>
                                                                <c:when test="${order.paymentStatus == 'paid'}">Đã thanh toán</c:when>
                                                                <c:when test="${order.paymentStatus == 'failed'}">Thanh toán thất bại</c:when>
                                                                <c:when test="${order.paymentStatus == 'refunded'}">Đã hoàn tiền</c:when>
                                                                <c:otherwise>${order.paymentStatus}</c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span class="status-badge status-${order.status}">
                                                            <c:choose>
                                                                <c:when test="${order.status == 'pending'}">Chờ xác nhận</c:when>
                                                                <c:when test="${order.status == 'confirmed'}">Đã xác nhận</c:when>
                                                                <c:when test="${order.status == 'processing'}">Đang xử lý</c:when>
                                                                <c:when test="${order.status == 'shipping'}">Đang giao hàng</c:when>
                                                                <c:when test="${order.status == 'delivered'}">Đã giao hàng</c:when>
                                                                <c:when test="${order.status == 'cancelled'}">Đã hủy</c:when>
                                                                <c:otherwise>${order.status}</c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="action-buttons">
                                                            <!-- Nút xem chi tiết -->
                                                            <a href="${pageContext.request.contextPath}/order?action=detail&id=${order.orderId}" class="btn-action btn-view">
                                                                <i class="fa fa-eye"></i> Chi tiết
                                                            </a>
                                                            <!-- Các nút hành động cho khách hàng -->
                                                            <c:if test="${sessionScope.user.role != 'admin'}">
                                                                <!-- Nút hủy đơn hàng (chỉ cho đơn hàng chưa xác nhận hoặc đã xác nhận) -->
                                                                <c:if test="${order.status == 'pending' || order.status == 'confirmed'}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="cancel">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-cancel" onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?');">
                                                                            <i class="fa fa-times"></i> Hủy đơn
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                                <!-- Nút xác nhận đã nhận hàng (chỉ cho đơn hàng đang giao) -->
                                                                <c:if test="${order.status == 'shipping' && (order.paymentMethod != 'cod' || order.paymentStatus == 'paid')}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="confirm-received">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-confirm" onclick="return confirm('Xác nhận đã nhận được hàng?');">
                                                                            <i class="fa fa-check"></i> Đã nhận hàng
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                            </c:if>
                                                            <!-- Các nút hành động cho admin -->
                                                            <c:if test="${sessionScope.user.role == 'admin'}">
                                                                <!-- Nút xác nhận đơn hàng -->
                                                                <c:if test="${order.status == 'pending'}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="confirm-order">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-confirm">
                                                                            <i class="fa fa-check"></i> Xác nhận
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                                <!-- Nút xác nhận giao hàng -->
                                                                <c:if test="${(order.status == 'confirmed' || order.status == 'processing')}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="confirm-shipped">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-ship">
                                                                            <i class="fa fa-truck"></i> Giao hàng
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                                <!-- Nút xác nhận thanh toán (chỉ cho COD và đang giao hàng) -->
                                                                <c:if test="${order.paymentMethod == 'cod' && order.paymentStatus == 'pending' && order.status == 'shipping'}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="confirm-payment">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-confirm">
                                                                            <i class="fa fa-money"></i> Xác nhận thanh toán
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                                <!-- Nút hủy đơn hàng -->
                                                                <c:if test="${order.status == 'pending' || order.status == 'confirmed' || order.status == 'processing'}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="cancel">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-cancel" onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?');">
                                                                            <i class="fa fa-times"></i> Hủy đơn
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                                <!-- Nút đánh dấu đã hoàn tiền (chỉ cho đơn hàng VNPay đã hủy và chưa hoàn tiền) -->
                                                                <c:if test="${order.status == 'cancelled' && order.paymentMethod == 'e-wallet' && order.paymentStatus == 'paid' && !refundedPayments[order.orderId]}">
                                                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                                                        <input type="hidden" name="action" value="mark-refunded">
                                                                        <input type="hidden" name="id" value="${order.orderId}">
                                                                        <button type="submit" class="btn-action btn-refund" onclick="return confirm('Xác nhận đã hoàn tiền cho đơn hàng này?');">
                                                                            <i class="fa fa-money"></i> Đã hoàn tiền
                                                                        </button>
                                                                    </form>
                                                                </c:if>
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                
                                <!-- Phân trang -->
                                <c:if test="${totalPages > 1}">
                                    <div class="pagination">
                                        <c:if test="${currentPage > 1}">
                                            <a href="${pageContext.request.contextPath}/order?page=${currentPage - 1}&keyword=${keyword}&status=${status}">
                                                <i class="fa fa-angle-left"></i> Trước
                                            </a>
                                        </c:if>
                                        
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${currentPage == i}">
                                                    <span class="active">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/order?page=${i}&keyword=${keyword}&status=${status}">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        
                                        <c:if test="${currentPage < totalPages}">
                                            <a href="${pageContext.request.contextPath}/order?page=${currentPage + 1}&keyword=${keyword}&status=${status}">
                                                Tiếp <i class="fa fa-angle-right"></i>
                                            </a>
                                        </c:if>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- JAVASCRIPTS -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.countdown.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.nice-select.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.nicescroll.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/slick.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/biolife.framework.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/functions.js"></script>
    <script>
        // Khởi tạo khi trang tải xong
        window.onload = function() {
            console.log("Window loaded");
            
            // Fix select box issue for status filter
            const statusSelect = document.getElementById("statusFilter");
            if (statusSelect) {
                // Giải pháp 1: Tạo một select mới để thay thế cái cũ
                const fixSelectDisplay = () => {
                    // Force browser to redraw select element
                    const originalValue = statusSelect.value;
                    const originalHtml = statusSelect.innerHTML;
                    
                    // Clone select và thay thế
                    const parent = statusSelect.parentNode;
                    const newSelect = document.createElement('select');
                    newSelect.id = 'statusFilter';
                    newSelect.name = 'status';
                    newSelect.className = 'status-filter';
                    newSelect.innerHTML = originalHtml;
                    newSelect.value = originalValue;
                    
                    // Thay thế select cũ bằng select mới
                    parent.replaceChild(newSelect, statusSelect);
                    
                    // Xóa bỏ mọi phần tử không cần thiết được tạo ra bởi nice-select
                    const unnecessaryElements = document.querySelectorAll('.nice-select, .nice-select-dropdown, .list');
                    unnecessaryElements.forEach(el => {
                        if (el && el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    });
                };
                
                // Giải pháp 2: Vô hiệu hóa nice-select
                if (typeof jQuery !== 'undefined') {
                    try {
                        // Vô hiệu hóa nice-select
                        if ($.fn.niceSelect) {
                            $('select').each(function() {
                                try {
                                    $(this).niceSelect('destroy');
                                } catch (e) {}
                            });
                        }
                    } catch (e) {
                        console.error("Error with jQuery:", e);
                    }
                }
                
                // Thực hiện sau khi trang đã load xong
                setTimeout(fixSelectDisplay, 100);
                
                // Thêm một lần nữa để đảm bảo các phần tử không cần thiết bị xóa
                setTimeout(() => {
                    const unnecessaryElements = document.querySelectorAll('.nice-select, .nice-select-dropdown, .list');
                    unnecessaryElements.forEach(el => {
                        if (el && el.parentNode) {
                            el.parentNode.removeChild(el);
                        }
                    });
                }, 500);
            }
        };
    </script>
</body>
</html>