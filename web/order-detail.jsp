<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Biolife - Thanh toán</title>
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
        .order-meta {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .order-meta-item {
            margin-bottom: 15px;
        }
        .order-meta-label {
            font-weight: 600;
            margin-bottom: 5px;
        }
        .order-meta-value {
            color: #555;
        }
        .order-notes {
            background-color: #fff8e1;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .order-notes-label {
            font-weight: 600;
            margin-bottom: 5px;
        }
        .order-items {
            margin-bottom: 20px;
        }
        .order-items-header {
            background-color: #f5f5f5;
            padding: 10px 15px;
            font-weight: 600;
            border-radius: 5px 5px 0 0;
            margin-bottom: 0;
        }
        .items-table {
            width: 100%;
            border-collapse: collapse;
        }
        .items-table th, .items-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }
        .items-table th {
            background-color: #f5f5f5;
            font-weight: 600;
        }
        .order-summary {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 0 0 5px 5px;
        }
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #e0e0e0;
        }
        .summary-row:last-child {
            border-bottom: none;
            font-weight: 600;
            font-size: 1.1em;
            padding-top: 12px;
        }
        .summary-label {
            color: #555;
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
            gap: 10px;
            margin-top: 20px;
            flex-wrap: wrap;
        }
        .btn-action {
            padding: 8px 15px;
            border-radius: 4px;
            font-size: 14px;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
            border: none;
            cursor: pointer;
            background-color: #f5f5f5;
            color: #333;
        }
        .btn-back {
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
    </style>
</head>
<body class="biolife-body">
    <!-- HEADER -->
    <jsp:include page="header.jsp" />
    
    <!-- MAIN CONTENT -->
    <div class="page-contain">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="order-container">
                        <div class="order-header">
                            <h1 class="order-title">Chi tiết đơn hàng #${order.orderNumber}</h1>
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
                        
                        <!-- Thông tin đơn hàng -->
                        <div class="order-meta">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="order-meta-item">
                                        <div class="order-meta-label">Mã đơn hàng:</div>
                                        <div class="order-meta-value">${order.orderNumber}</div>
                                    </div>
                                    <div class="order-meta-item">
                                        <div class="order-meta-label">Ngày đặt:</div>
                                        <div class="order-meta-value"><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" /></div>
                                    </div>
                                    <div class="order-meta-item">
                                        <div class="order-meta-label">Phương thức thanh toán:</div>
                                        <div class="order-meta-value">
                                            <c:choose>
                                                <c:when test="${order.paymentMethod == 'cod'}">Thanh toán khi nhận hàng</c:when>
                                                <c:when test="${order.paymentMethod == 'e-wallet'}">Ví điện tử</c:when>
                                                <c:otherwise>${order.paymentMethod}</c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="order-meta-item">
                                        <div class="order-meta-label">Trạng thái thanh toán:</div>
                                        <div class="order-meta-value">
                                            <span class="payment-badge payment-${order.paymentStatus}">
                                                <c:choose>
                                                    <c:when test="${order.paymentStatus == 'pending'}">Chưa thanh toán</c:when>
                                                    <c:when test="${order.paymentStatus == 'paid'}">Đã thanh toán</c:when>
                                                    <c:when test="${order.paymentStatus == 'failed'}">Thanh toán thất bại</c:when>
                                                    <c:when test="${order.paymentStatus == 'refunded'}">Đã hoàn tiền</c:when>
                                                    <c:otherwise>${order.paymentStatus}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="order-meta-item">
                                        <div class="order-meta-label">Trạng thái đơn hàng:</div>
                                        <div class="order-meta-value">
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
                                        </div>
                                    </div>
                                    <c:if test="${not empty order.updatedAt}">
                                        <div class="order-meta-item">
                                            <div class="order-meta-label">Cập nhật lần cuối:</div>
                                            <div class="order-meta-value"><fmt:formatDate value="${order.updatedAt}" pattern="dd/MM/yyyy HH:mm" /></div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <!-- Thông tin địa chỉ -->
                            <c:if test="${not empty shippingAddress}">
                                <div class="order-meta-item">
                                    <div class="order-meta-label">Địa chỉ giao hàng:</div>
                                    <div class="order-meta-value">
                                        <c:if test="${not empty shippingAddress}">
                                            <strong>${shippingAddress.recipientName}</strong><br/>
                                            <strong>SĐT:</strong> ${shippingAddress.phone}<br/>
                                            ${shippingAddress.addressDetail}, ${shippingAddress.ward}, 
                                            ${shippingAddress.district}, ${shippingAddress.province}
                                        </c:if>
                                        <c:if test="${empty shippingAddress}">
                                            Địa chỉ #${order.shippingAddressId}
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        
                        <!-- Ghi chú đơn hàng -->
                        <c:if test="${not empty order.notes}">
                            <div class="order-notes">
                                <div class="order-notes-label">Ghi chú đơn hàng:</div>
                                <div>${order.notes}</div>
                            </div>
                        </c:if>
                        
                        <!-- Danh sách sản phẩm -->
                        <div class="order-items">
                            <div class="order-items-header">
                                <i class="fa fa-shopping-cart"></i> Sản phẩm đã đặt
                            </div>
                            <div class="table-responsive">
                                <table class="items-table">
                                    <thead>
                                        <tr>
                                            <th style="width: 60%">Sản phẩm</th>
                                            <th style="width: 15%">Giá</th>
                                            <th style="width: 10%">Số lượng</th>
                                            <th style="width: 15%">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${order.orderItems}">
                                            <tr>
                                                <td>
                                                    <div style="display: flex; align-items: center;">
                                                        <c:choose>
                                                            <c:when test="${not empty item.imageUrl}">
                                                                <img src="${pageContext.request.contextPath}/${item.imageUrl}" alt="${item.productName}" 
                                                                     style="width: 60px; height: 60px; object-fit: cover; margin-right: 10px; border-radius: 4px;">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="${pageContext.request.contextPath}/assets/images/products/p-01.jpg" alt="Default Product" 
                                                                     style="width: 60px; height: 60px; object-fit: cover; margin-right: 10px; border-radius: 4px;">
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <span>${item.productName}</span>
                                                    </div>
                                                </td>
                                                <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></td>
                                                <td>${item.quantity}</td>
                                                <td><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                            <!-- Tổng kết đơn hàng -->
                            <div class="order-summary">
                                <div class="summary-row">
                                    <div class="summary-label">Tổng tiền hàng:</div>
                                    <div class="summary-value"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></div>
                                </div>
                                <div class="summary-row">
                                    <div class="summary-label">Giảm giá:</div>
                                    <div class="summary-value">-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></div>
                                </div>
                                <div class="summary-row">
                                    <div class="summary-label">Phí vận chuyển:</div>
                                    <div class="summary-value"><fmt:formatNumber value="${order.shippingFee}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></div>
                                </div>
                                <div class="summary-row">
                                    <div class="summary-label">Thuế:</div>
                                    <div class="summary-value"><fmt:formatNumber value="${order.tax}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></div>
                                </div>
                                <div class="summary-row">
                                    <div class="summary-label">Tổng thanh toán:</div>
                                    <div class="summary-value"><fmt:formatNumber value="${order.finalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="2"/></div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Các nút hành động -->
                        <div class="action-buttons">
                            <a href="${pageContext.request.contextPath}/order" class="btn-action btn-back">
                                <i class="fa fa-arrow-left"></i> Quay lại
                            </a>
                            
                            <!-- Các nút hành động cho khách hàng -->
                            <c:if test="${sessionScope.user.role != 'admin'}">
                                <!-- Nút hủy đơn hàng (chỉ cho đơn hàng chưa xác nhận hoặc đã xác nhận) -->
                                <c:if test="${order.status == 'pending' || order.status == 'confirmed'}">
                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="cancel">
                                        <input type="hidden" name="id" value="${order.orderId}">
                                        <button type="submit" class="btn-action btn-cancel" onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?');">
                                            <i class="fa fa-times"></i> Hủy đơn hàng
                                        </button>
                                    </form>
                                </c:if>
                                
                                <!-- Nút xác nhận đã nhận hàng (chỉ cho đơn hàng đang giao và đã thanh toán nếu là COD) -->
                                <c:if test="${order.status == 'shipping' && (order.paymentMethod != 'cod' || order.paymentStatus == 'paid')}">
                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="confirm-received">
                                        <input type="hidden" name="id" value="${order.orderId}">
                                        <button type="submit" class="btn-action btn-confirm" onclick="return confirm('Xác nhận đã nhận được hàng?');">
                                            <i class="fa fa-check"></i> Xác nhận đã nhận hàng
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
                                            <i class="fa fa-check"></i> Xác nhận đơn hàng
                                        </button>
                                    </form>
                                </c:if>
                                
                                <!-- Nút xác nhận giao hàng -->
                                <c:if test="${(order.status == 'confirmed' || order.status == 'processing')}">
                                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="confirm-shipped">
                                        <input type="hidden" name="id" value="${order.orderId}">
                                        <button type="submit" class="btn-action btn-ship">
                                            <i class="fa fa-truck"></i> Xác nhận giao hàng
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
                                            <i class="fa fa-times"></i> Hủy đơn hàng
                                        </button>
                                    </form>
                                </c:if>
                                
                                <!-- Nút đánh dấu đã hoàn tiền (chỉ cho đơn hàng VNPay đã hủy và chưa hoàn tiền) -->
                                <c:if test="${order.status == 'cancelled' && order.paymentMethod == 'e-wallet' && order.paymentStatus == 'paid' && !isPaymentRefunded}">
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
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- FOOTER -->
    <jsp:include page="footer.jsp" />
    
    <!-- JAVASCRIPTS -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.countdown.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.nice-select.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/jquery.nicescroll.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/slick.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/biolife.framework.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/functions.js"></script>
</body>
</html>