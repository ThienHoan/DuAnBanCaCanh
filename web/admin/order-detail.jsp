<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Đơn Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .order-detail-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            margin-bottom: 20px;
        }
        .card-header {
            background-color: #f8f9fa;
            border-bottom: 1px solid #dee2e6;
            padding: 15px 20px;
        }
        .order-status {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 0.875rem;
            font-weight: 500;
        }
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        .status-processing {
            background-color: #cce5ff;
            color: #004085;
        }
        .status-completed {
            background-color: #d4edda;
            color: #155724;
        }
        .status-cancelled {
            background-color: #f8d7da;
            color: #721c24;
        }
        .payment-status {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 0.875rem;
            font-weight: 500;
        }
        .payment-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        .payment-paid {
            background-color: #d4edda;
            color: #155724;
        }
        .payment-failed {
            background-color: #f8d7da;
            color: #721c24;
        }
        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 5px;
        }
        .btn-status {
            margin-right: 5px;
            margin-bottom: 5px;
        }
        .inventory-warning {
            color: #dc3545;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="/admin/includes/sidebar.jsp">
                <jsp:param name="page" value="orders"/>
            </jsp:include>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <div class="order-detail-container">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2><i class="bi bi-file-text"></i> Chi Tiết Đơn Hàng #${order.orderNumber}</h2>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Quay Lại
                        </a>
                    </div>
                    
                    <!-- Thông báo -->
                    <c:if test="${not empty sessionScope.SUCCESS_MESSAGE}">
                        <div class="alert alert-success alert-dismissible fade show">
                            ${sessionScope.SUCCESS_MESSAGE}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="SUCCESS_MESSAGE" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.ERROR_MESSAGE}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            ${sessionScope.ERROR_MESSAGE}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="ERROR_MESSAGE" scope="session" />
                    </c:if>
                    
                    <!-- Order Info Card -->
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">Thông Tin Đơn Hàng</h5>
                            <div>
                                <span class="order-status 
                                    <c:choose>
                                        <c:when test="${order.status eq 'pending'}">status-pending</c:when>
                                        <c:when test="${order.status eq 'processing'}">status-processing</c:when>
                                        <c:when test="${order.status eq 'completed'}">status-completed</c:when>
                                        <c:when test="${order.status eq 'cancelled'}">status-cancelled</c:when>
                                    </c:choose>
                                ">
                                    <i class="bi 
                                        <c:choose>
                                            <c:when test="${order.status eq 'pending'}">bi-hourglass-split</c:when>
                                            <c:when test="${order.status eq 'processing'}">bi-gear</c:when>
                                            <c:when test="${order.status eq 'completed'}">bi-check-circle</c:when>
                                            <c:when test="${order.status eq 'cancelled'}">bi-x-circle</c:when>
                                        </c:choose>
                                    "></i>
                                    ${order.status}
                                </span>
                                <span class="payment-status 
                                    <c:choose>
                                        <c:when test="${order.paymentStatus eq 'pending'}">payment-pending</c:when>
                                        <c:when test="${order.paymentStatus eq 'paid'}">payment-paid</c:when>
                                        <c:when test="${order.paymentStatus eq 'failed'}">payment-failed</c:when>
                                    </c:choose>
                                ">
                                    <i class="bi 
                                        <c:choose>
                                            <c:when test="${order.paymentStatus eq 'pending'}">bi-wallet2</c:when>
                                            <c:when test="${order.paymentStatus eq 'paid'}">bi-credit-card</c:when>
                                            <c:when test="${order.paymentStatus eq 'failed'}">bi-exclamation-triangle</c:when>
                                        </c:choose>
                                    "></i>
                                    ${order.paymentStatus}
                                </span>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Mã Đơn Hàng:</strong> ${order.orderNumber}</p>
                                    <p><strong>Khách Hàng:</strong> ${order.user.fullName} (ID: ${order.userId})</p>
                                    <p><strong>Email:</strong> ${order.user.email}</p>
                                    <p><strong>Số Điện Thoại:</strong> ${order.user.phone}</p>
                                    <p><strong>Ngày Đặt Hàng:</strong> <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Phương Thức Thanh Toán:</strong> ${order.paymentMethod}</p>
                                    <p><strong>Trạng Thái Thanh Toán:</strong> ${order.paymentStatus}</p>
                                    <p><strong>Địa Chỉ Giao Hàng:</strong> ${order.shippingAddress.addressDetail}, ${order.shippingAddress.ward}, ${order.shippingAddress.district}, ${order.shippingAddress.province}</p>
                                    <p><strong>Người Nhận:</strong> ${order.shippingAddress.recipientName}</p>
                                    <p><strong>Số Điện Thoại Người Nhận:</strong> ${order.shippingAddress.phone}</p>
                                </div>
                            </div>
                            
                            <!-- Cập nhật trạng thái -->
                            <div class="mt-4">
                                <h6>Cập Nhật Trạng Thái:</h6>
                                <div class="d-flex flex-wrap">
                                    <form action="${pageContext.request.contextPath}/admin/order-status" method="post">
                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                        <input type="hidden" name="returnUrl" value="${pageContext.request.requestURI}?orderId=${order.orderId}">
                                        
                                        <c:if test="${order.status ne 'processing'}">
                                            <button type="submit" name="status" value="processing" class="btn btn-primary btn-status">
                                                <i class="bi bi-gear"></i> Đang Xử Lý
                                            </button>
                                        </c:if>
                                        
                                        <c:if test="${order.status ne 'completed' && (order.paymentStatus eq 'paid' || order.paymentMethod eq 'cod')}">
                                            <button type="submit" name="status" value="completed" class="btn btn-success btn-status">
                                                <i class="bi bi-check-circle"></i> Hoàn Thành
                                            </button>
                                            <c:if test="${order.status ne 'completed'}">
                                                <div class="inventory-warning mt-2">
                                                    <i class="bi bi-exclamation-triangle"></i> 
                                                    Xác nhận hoàn thành sẽ cập nhật kho hàng và trừ số lượng sản phẩm.
                                                </div>
                                            </c:if>
                                        </c:if>
                                        
                                        <c:if test="${order.status ne 'cancelled'}">
                                            <button type="submit" name="status" value="cancelled" class="btn btn-danger btn-status">
                                                <i class="bi bi-x-circle"></i> Hủy Đơn
                                            </button>
                                        </c:if>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Order Items Card -->
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">Chi Tiết Sản Phẩm</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Sản Phẩm</th>
                                            <th>Đơn Giá</th>
                                            <th>Số Lượng</th>
                                            <th>Thành Tiền</th>
                                            <th>Tồn Kho</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${orderItems}" var="item">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <c:if test="${not empty productImages[item.productId]}">
                                                            <img src="${productImages[item.productId]}" alt="${item.productName}" class="product-image me-3">
                                                        </c:if>
                                                        <div>
                                                            <h6 class="mb-0">${item.productName}</h6>
                                                            <small class="text-muted">ID: ${item.productId}</small>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                                <td>${item.quantity}</td>
                                                <td><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                                <td>
                                                    <c:if test="${not empty productInventory[item.productId]}">
                                                        <span class="${productInventory[item.productId] < item.quantity ? 'text-danger' : ''}">${productInventory[item.productId]}</span>
                                                        <c:if test="${productInventory[item.productId] < item.quantity}">
                                                            <div class="inventory-warning">
                                                                <i class="bi bi-exclamation-triangle"></i> Không đủ hàng
                                                            </div>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="3" class="text-end"><strong>Tổng Tiền Hàng:</strong></td>
                                            <td colspan="2"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                        </tr>
                                        <c:if test="${order.discountAmount > 0}">
                                            <tr>
                                                <td colspan="3" class="text-end"><strong>Giảm Giá:</strong></td>
                                                <td colspan="2">-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <td colspan="3" class="text-end"><strong>Phí Vận Chuyển:</strong></td>
                                            <td colspan="2"><fmt:formatNumber value="${order.shippingFee}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" class="text-end"><strong>Thuế (5%):</strong></td>
                                            <td colspan="2"><fmt:formatNumber value="${order.tax}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" class="text-end"><strong>Tổng Thanh Toán:</strong></td>
                                            <td colspan="2"><strong><fmt:formatNumber value="${order.finalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></strong></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Inventory Logs Card -->
                    <c:if test="${not empty inventoryLogs}">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">Lịch Sử Kho Hàng</h5>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Thời Gian</th>
                                                <th>Sản Phẩm</th>
                                                <th>Số Lượng Trước</th>
                                                <th>Số Lượng Sau</th>
                                                <th>Thay Đổi</th>
                                                <th>Lý Do</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${inventoryLogs}" var="log">
                                                <tr>
                                                    <td><fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                                    <td>${log.productName}</td>
                                                    <td>${log.quantityBefore}</td>
                                                    <td>${log.quantityAfter}</td>
                                                    <td class="${log.quantityChange > 0 ? 'text-success' : 'text-danger'}">
                                                        ${log.quantityChange > 0 ? '+' : ''}${log.quantityChange}
                                                    </td>
                                                    <td>${log.reason}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </main>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 