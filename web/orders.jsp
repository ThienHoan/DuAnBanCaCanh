<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Đơn Hàng - Aqua Paradise</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            color: #333;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 15px 30px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: 600;
            margin-bottom: 30px;
            transition: all 0.3s ease;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .back-link:hover {
            background: linear-gradient(135deg, #764ba2, #667eea);
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102,126,234,0.4);
        }

        .header {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .header::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(102,126,234,0.1) 0%, transparent 70%);
            animation: wave 6s ease-in-out infinite;
        }

        @keyframes wave {
            0%, 100% { transform: rotate(0deg); }
            50% { transform: rotate(180deg); }
        }

        .header h1 {
            color: #2c3e50;
            font-size: 2.5rem;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 15px;
            position: relative;
            z-index: 1;
        }

        .header h1 i {
            color: #3498db;
            font-size: 2.8rem;
        }

        .header p {
            color: #7f8c8d;
            font-size: 1.1rem;
            position: relative;
            z-index: 1;
        }

        .pagination-info {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 15px 25px;
            margin-bottom: 20px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            text-align: center;
            color: #666;
            font-weight: 500;
        }

        .pagination-info i {
            color: #3498db;
            margin-right: 8px;
        }

        .message-container {
            margin-bottom: 20px;
        }

        .message, .error {
            padding: 15px 20px;
            border-radius: 10px;
            margin-bottom: 15px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .message {
            background: linear-gradient(135deg, #d4edda, #c3e6cb);
            color: #155724;
            border-left: 5px solid #28a745;
        }

        .error {
            background: linear-gradient(135deg, #f8d7da, #f1b0b7);
            color: #721c24;
            border-left: 5px solid #dc3545;
        }

        .table-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 30px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            overflow-x: auto;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 0;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #e9ecef;
        }

        th {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            position: sticky;
            top: 0;
            z-index: 10;
        }

        th:first-child {
            border-top-left-radius: 15px;
        }

        th:last-child {
            border-top-right-radius: 15px;
        }

        tbody tr {
            transition: all 0.3s ease;
        }

        tbody tr:hover {
            background: linear-gradient(135deg, rgba(102,126,234,0.1), rgba(118,75,162,0.1));
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        tbody tr:nth-child(even) {
            background: rgba(248, 249, 250, 0.8);
        }

        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .status-pending {
            background: linear-gradient(135deg, #fff3cd, #ffeaa7);
            color: #856404;
        }

        .status-confirmed {
            background: linear-gradient(135deg, #d1ecf1, #bee5eb);
            color: #0c5460;
        }

        .status-processing {
            background: linear-gradient(135deg, #e2e3e5, #d6d8db);
            color: #383d41;
        }

        .status-shipping {
            background: linear-gradient(135deg, #cce5ff, #b3d9ff);
            color: #004085;
        }

        .status-delivered {
            background: linear-gradient(135deg, #d4edda, #c3e6cb);
            color: #155724;
        }

        .status-cancelled {
            background: linear-gradient(135deg, #f8d7da, #f1b0b7);
            color: #721c24;
        }

        .action-buttons {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .action-btn {
            padding: 8px 16px;
            border: none;
            border-radius: 25px;
            font-size: 0.85rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .btn-detail {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
        }

        .btn-detail:hover {
            background: linear-gradient(135deg, #764ba2, #667eea);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102,126,234,0.4);
        }

        .btn-cancel {
            background: linear-gradient(135deg, #ff6b6b, #ee5a52);
            color: white;
        }

        .btn-cancel:hover {
            background: linear-gradient(135deg, #ee5a52, #ff6b6b);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(238,90,82,0.4);
        }

        .btn-confirm {
            background: linear-gradient(135deg, #4ecdc4, #44a08d);
            color: white;
        }

        .btn-confirm:hover {
            background: linear-gradient(135deg, #44a08d, #4ecdc4);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(68,160,141,0.4);
        }

        .btn-ship {
            background: linear-gradient(135deg, #45b7d1, #2196f3);
            color: white;
        }

        .btn-ship:hover {
            background: linear-gradient(135deg, #2196f3, #45b7d1);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(33,150,243,0.4);
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin: 30px 0;
            flex-wrap: wrap;
        }

        .pagination a, .pagination span {
            padding: 12px 18px;
            border-radius: 15px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 50px;
            font-size: 0.95rem;
        }

        .pagination a {
            background: rgba(255, 255, 255, 0.9);
            color: #667eea;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .pagination a:hover {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(102,126,234,0.4);
        }

        .pagination .current {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            box-shadow: 0 8px 20px rgba(102,126,234,0.4);
        }

        .pagination .disabled {
            background: rgba(200, 200, 200, 0.5);
            color: #999;
            cursor: not-allowed;
        }

        .pagination .nav-btn {
            padding: 12px 20px;
            font-weight: 700;
        }

        .pagination .nav-btn i {
            font-size: 1.1rem;
        }

        .currency {
            font-weight: 700;
            color: #27ae60;
        }

        .order-number {
            font-family: 'Courier New', monospace;
            font-weight: 700;
            color: #2c3e50;
            background: rgba(102,126,234,0.1);
            padding: 5px 10px;
            border-radius: 5px;
        }

        .payment-method {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 4px 8px;
            background: rgba(102,126,234,0.1);
            border-radius: 15px;
            font-size: 0.85rem;
        }

        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }
            
            .header h1 {
                font-size: 2rem;
            }
            
            .table-container {
                padding: 15px;
            }
            
            table {
                font-size: 0.85rem;
            }
            
            th, td {
                padding: 10px 8px;
            }
            
            .action-buttons {
                flex-direction: column;
            }

            .pagination {
                gap: 5px;
            }

            .pagination a, .pagination span {
                padding: 10px 14px;
                min-width: 45px;
                font-size: 0.9rem;
            }

            .pagination .nav-btn {
                padding: 10px 16px;
            }
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #7f8c8d;
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            color: #bdc3c7;
        }

        .empty-state h3 {
            font-size: 1.5rem;
            margin-bottom: 10px;
        }
    .filter-container {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 20px;
            align-items: center;
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
        }

        .filter-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 25px;
            font-size: 0.9rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .filter-btn.active {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            box-shadow: 0 5px 15px rgba(102,126,234,0.4);
        }

        .filter-btn:hover:not(.active) {
            background: linear-gradient(135deg, #f1f3f5, #e9ecef);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .search-container {
            display: flex;
            align-items: center;
            gap: 10px;
            flex-grow: 1;
        }

        .search-input {
            padding: 10px 15px;
            border: 1px solid #e9ecef;
            border-radius: 25px;
            font-size: 0.9rem;
            width: 100%;
            max-width: 300px;
        }

        .search-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 25px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .search-btn:hover {
            background: linear-gradient(135deg, #764ba2, #667eea);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102,126,234,0.4);
        }

        @media (max-width: 768px) {
            .filter-container {
                flex-direction: column;
                align-items: stretch;
            }
            
            .search-container {
                width: 100%;
            }
            
            .search-input {
                max-width: none;
            }
        }
    </style>
</head>
<body>
    <div class="container">

        <div class="header">
            <h1>
                <i class="fas fa-fish"></i>
                Quản Lý Đơn Hàng
            </h1>
            <p>Theo dõi và quản lý đơn hàng cá cảnh & phụ kiện</p>
        </div>

        <div class="filter-container">
            <div class="search-container">
                <form action="orders" method="get">
                    <input type="hidden" name="action" value="list">
                    <input type="text" name="keyword" class="search-input" placeholder="Tìm kiếm theo mã đơn hàng..." value="${keyword}">
                    <button type="submit" class="search-btn"><i class="fas fa-search"></i> Tìm kiếm</button>
                </form>
            </div>
            <div>
                <a href="orders?action=list&status=all" class="filter-btn ${status == 'all' || empty status ? 'active' : ''}">
                    <i class="fas fa-list"></i> Tất cả
                </a>
                <a href="orders?action=list&status=pending" class="filter-btn ${status == 'pending' ? 'active' : ''}">
                    <i class="fas fa-hourglass-half"></i> Chờ xác nhận
                </a>
                <a href="orders?action=list&status=confirmed" class="filter-btn ${status == 'confirmed' ? 'active' : ''}">
                    <i class="fas fa-check"></i> Đang chuẩn bị
                </a>
                <a href="orders?action=list&status=shipping" class="filter-btn ${status == 'shipping' ? 'active' : ''}">
                    <i class="fas fa-truck"></i> Đang giao
                </a>
                <a href="orders?action=list&status=delivered" class="filter-btn ${status == 'delivered' ? 'active' : ''}">
                    <i class="fas fa-check-double"></i> Đã giao
                </a>
                <a href="orders?action=list&status=cancelled" class="filter-btn ${status == 'cancelled' ? 'active' : ''}">
                    <i class="fas fa-times"></i> Đã hủy
                </a>
            </div>
        </div>

        <c:if test="${not empty orders}">
            <div class="pagination-info">
                <i class="fas fa-info-circle"></i>
                Hiển thị ${(currentPage - 1) * ordersPerPage + 1} - ${(currentPage - 1) * ordersPerPage + orders.size()} 
                trong tổng số ${totalOrders} đơn hàng
            </div>
        </c:if>

        <div class="message-container">
            <c:if test="${param.message != null}">
                <div class="message">
                    <i class="fas fa-check-circle"></i>
                    ${param.message}
                </div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="error">
                    <i class="fas fa-exclamation-circle"></i>
                    ${param.error}
                </div>
            </c:if>
        </div>

        <div class="table-container">
            <c:choose>
                <c:when test="${empty orders}">
                    <div class="empty-state">
                        <i class="fas fa-inbox"></i>
                        <h3>Chưa có đơn hàng nào</h3>
                        <p>Các đơn hàng sẽ được hiển thị tại đây</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th><i class="fas fa-hashtag"></i> Mã Đơn</th>
                                <th><i class="fas fa-calendar-alt"></i> Ngày Đặt</th>
                                <th><i class="fas fa-money-bill-wave"></i> Tổng Tiền</th>
                                <th><i class="fas fa-info-circle"></i> Trạng Thái</th>
                                <th><i class="fas fa-credit-card"></i> Thanh Toán</th>
                                <th><i class="fas fa-cogs"></i> Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td>
                                        <span class="order-number">${order.orderNumber}</span>
                                    </td>
                                    <td>
                                        <i class="fas fa-clock"></i>
                                        <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <span class="currency">
                                            <fmt:formatNumber value="${order.finalAmount}" type="currency" currencySymbol="₫"/>
                                        </span>
                                    </td>
                                    <td>
                                        <span class="status-badge status-${order.status}">
                                            <c:choose>
                                                <c:when test="${order.status == 'pending'}">
                                                    <i class="fas fa-hourglass-half"></i> Chờ xử lý
                                                </c:when>
                                                <c:when test="${order.status == 'confirmed'}">
                                                    <i class="fas fa-check"></i> Đang chuẩn bị hàng
                                                </c:when>
                                                <c:when test="${order.status == 'shipping'}">
                                                    <i class="fas fa-truck"></i> Đang giao
                                                </c:when>
                                                <c:when test="${order.status == 'delivered'}">
                                                    <i class="fas fa-check-double"></i> Đã giao
                                                </c:when>
                                                <c:when test="${order.status == 'cancelled'}">
                                                    <i class="fas fa-times"></i> Đã hủy
                                                </c:when>
                                                <c:otherwise>
                                                    ${order.status}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <span class="payment-method">
                                            <c:choose>
                                                <c:when test="${order.paymentMethod == 'cod'}">
                                                    <i class="fas fa-money-bill-alt"></i> Tiền mặt
                                                </c:when>
                                                <c:when test="${order.paymentMethod == 'e-wallet'}">
                                                    <i class="fas fa-university"></i> Chuyển khoản
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-question"></i> ${order.paymentMethod}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="orders?action=detail&orderId=${order.orderId}" class="action-btn btn-detail">
                                                <i class="fas fa-eye"></i> Chi Tiết
                                            </a>
                                            
                                            <c:if test="${sessionScope.user.role == 'customer'}">
                                                <c:if test="${order.status == 'pending' || order.status == 'confirmed'}">
                                                    <form action="orders" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="cancel">
                                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                                        <input type="hidden" name="currentPage" value="${currentPage}">
                                                        <input type="hidden" name="keyword" value="${keyword}">
                                                        <input type="hidden" name="status" value="${status}">
                                                        <button type="submit" class="action-btn btn-cancel" onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?')">
                                                            <i class="fas fa-times"></i> Hủy Đơn
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${order.status == 'shipping'}">
                                                    <form action="orders" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="confirmReceived">
                                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                                        <input type="hidden" name="currentPage" value="${currentPage}">
                                                        <input type="hidden" name="keyword" value="${keyword}">
                                                        <input type="hidden" name="status" value="${status}">
                                                        <button type="submit" class="action-btn btn-confirm" onclick="return confirm('Bạn có chắc đã nhận được hàng?')">
                                                            <i class="fas fa-check-double"></i> Đã Nhận Hàng
                                                        </button>
                                                    </form>
                                                </c:if>
                                            </c:if>
                                            
                                            <c:if test="${sessionScope.user.role == 'admin'}">
                                                <c:if test="${order.status == 'pending'}">
                                                    <form action="orders" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="confirm">
                                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                                        <input type="hidden" name="currentPage" value="${currentPage}">
                                                        <input type="hidden" name="keyword" value="${keyword}">
                                                        <input type="hidden" name="status" value="${status}">
                                                        <button type="submit" class="action-btn btn-confirm" onclick="return confirm('Bạn có chắc muốn xác nhận đơn hàng này?')">
                                                            <i class="fas fa-check"></i> Xác Nhận
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${order.status == 'confirmed' || order.status == 'processing'}">
                                                    <form action="orders" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="confirmShipped">
                                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                                        <input type="hidden" name="currentPage" value="${currentPage}">
                                                        <input type="hidden" name="keyword" value="${keyword}">
                                                        <input type="hidden" name="status" value="${status}">
                                                        <button type="submit" class="action-btn btn-ship" onclick="return confirm('Bạn có chắc muốn xác nhận giao hàng cho đơn hàng này?')">
                                                            <i class="fas fa-truck"></i> Giao Hàng
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
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${not empty orders}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="orders?page=${currentPage - 1}&keyword=${keyword}&status=${status}" class="nav-btn"><i class="fas fa-chevron-left"></i> Trước</a>
                </c:if>
                <c:if test="${currentPage <= 1}">
                    <span class="nav-btn disabled"><i class="fas fa-chevron-left"></i> Trước</span>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="orders?page=${i}&keyword=${keyword}&status=${status}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="orders?page=${currentPage + 1}&keyword=${keyword}&status=${status}" class="nav-btn">Tiếp <i class="fas fa-chevron-right"></i></a>
                </c:if>
                <c:if test="${currentPage >= totalPages}">
                    <span class="nav-btn disabled">Tiếp <i class="fas fa-chevron-right"></i></span>
                </c:if>
            </div>
        </c:if>
    </div>
</body>
</html>


