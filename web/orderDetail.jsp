<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Đơn Hàng - FishShop</title>
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
            line-height: 1.6;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .header {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(15px);
            border-radius: 20px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        
        .header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(135deg, #667eea, #764ba2);
        }
        
        .header h1 {
            font-size: 2.5rem;
            background: linear-gradient(135deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 10px;
            font-weight: 700;
        }
        
        .order-number {
            font-size: 1.2rem;
            color: #666;
            font-weight: 600;
        }
        
        .main-content {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }
        
        .card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(15px);
            border-radius: 20px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            border: 1px solid rgba(255, 255, 255, 0.2);
            position: relative;
            overflow: hidden;
        }
        
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
        }
        
        .card-header {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }
        
        .card-header .icon {
            width: 50px;
            height: 50px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 20px;
        }
        
        .card-header h3 {
            font-size: 1.5rem;
            color: #2c3e50;
            font-weight: 600;
        }
        
        .info-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            margin-bottom: 10px;
            background: rgba(102, 126, 234, 0.05);
            border-radius: 12px;
            transition: all 0.3s ease;
        }
        
        .info-item:hover {
            background: rgba(102, 126, 234, 0.1);
            transform: translateX(5px);
        }
        
        .info-item .label {
            font-weight: 600;
            color: #495057;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .info-item .value {
            font-weight: 600;
            color: #2c3e50;
        }
        
        .status-badge {
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .status-pending {
            background: linear-gradient(135deg, #ffecd2, #fcb69f);
            color: #d68910;
        }
        
        .status-confirmed {
            background: linear-gradient(135deg, #a8edea, #fed6e3);
            color: #0e6655;
        }
        
        .status-shipping {
            background: linear-gradient(135deg, #d299c2, #fef9d7);
            color: #6f42c1;
        }
        
        .status-delivered {
            background: linear-gradient(135deg, #89f7fe, #66a6ff);
            color: #0c5460;
        }
        
        .status-cancelled {
            background: linear-gradient(135deg, #fc4a1a, #f7b733);
            color: #721c24;
        }
        
        .payment-method {
            display: flex;
            align-items: center;
            gap: 8px;
            padding: 8px 16px;
            background: rgba(40, 167, 69, 0.1);
            border-radius: 20px;
            color: #155724;
            font-weight: 600;
        }
        
        .products-section {
            grid-column: 1 / -1;
        }
        
        .products-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        
        .products-table th {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 20px 15px;
            text-align: left;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-size: 0.9rem;
        }
        
        .products-table td {
            padding: 20px 15px;
            border-bottom: 1px solid #eee;
            vertical-align: middle;
        }
        
        .products-table tr:hover {
            background: rgba(102, 126, 234, 0.05);
        }
        
        .products-table tr:last-child td {
            border-bottom: none;
        }
        
        .product-thumb {
            text-align: center;
        }
        
        .product-thumb img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }
        
        .product-thumb img:hover {
            transform: scale(1.1);
        }
        
        .product-info {
            max-width: 200px;
        }
        
        .product-name {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 8px;
            font-size: 1.1rem;
        }
        
        .product-attributes {
            display: flex;
            flex-wrap: wrap;
            gap: 5px;
        }
        
        .product-attributes span {
            background: #e9ecef;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.8rem;
            color: #495057;
        }
        
        .quantity-badge {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            text-align: center;
            min-width: 60px;
        }
        
        .price {
            font-weight: 700;
            color: #2c3e50;
            font-size: 1.1rem;
        }
        
        .total-price {
            font-weight: 700;
            color: #667eea;
            font-size: 1.2rem;
        }
        
        .price-breakdown {
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            border-radius: 15px;
            padding: 25px;
            margin-top: 20px;
        }
        
        .price-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #dee2e6;
        }
        
        .price-row:last-child {
            border-bottom: none;
            font-size: 1.3rem;
            font-weight: 700;
            color: #2c3e50;
            margin-top: 15px;
            padding-top: 20px;
            border-top: 2px solid #667eea;
        }
        
        .price-row .label {
            display: flex;
            align-items: center;
            gap: 8px;
            font-weight: 600;
        }
        
        .discount {
            color: #28a745 !important;
        }
        
        .back-button {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 15px 30px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            text-decoration: none;
            border-radius: 50px;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
            margin-top: 20px;
        }
        
        .back-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
            text-decoration: none;
            color: white;
        }
        
        .notes-section {
            background: rgba(255, 243, 205, 0.5);
            border: 1px solid #ffeaa7;
            border-radius: 15px;
            padding: 20px;
            margin-top: 20px;
        }
        
        .notes-section .icon {
            color: #fdcb6e;
            font-size: 1.2rem;
            margin-right: 10px;
        }
        
        /* Animation for page load */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .card {
            animation: fadeInUp 0.6s ease forwards;
        }
        
        .card:nth-child(2) {
            animation-delay: 0.1s;
        }
        
        .card:nth-child(3) {
            animation-delay: 0.2s;
        }
        
        /* Responsive Design */
        @media (max-width: 1024px) {
            .main-content {
                grid-template-columns: 1fr;
                gap: 20px;
            }
            
            .products-table {
                font-size: 0.9rem;
            }
            
            .products-table th,
            .products-table td {
                padding: 15px 10px;
            }
        }
        
        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }
            
            .header h1 {
                font-size: 2rem;
            }
            
            .card {
                padding: 20px;
            }
            
            .products-table {
                font-size: 0.8rem;
            }
            
            .products-table th,
            .products-table td {
                padding: 10px 8px;
            }
            
            .product-thumb img {
                width: 60px;
                height: 60px;
            }
            
            .info-item {
                flex-direction: column;
                align-items: flex-start;
                gap: 8px;
            }
            
            .price-row {
                font-size: 0.9rem;
            }
            
            .price-row:last-child {
                font-size: 1.1rem;
            }
        }
        
        @media (max-width: 480px) {
            .products-table {
                font-size: 0.7rem;
            }
            
            .card-header {
                flex-direction: column;
                text-align: center;
                gap: 10px;
            }
            
            .card-header .icon {
                width: 40px;
                height: 40px;
                font-size: 16px;
            }
        }
        
        /* Print styles */
        @media print {
            body {
                background: white !important;
            }
            
            .card {
                box-shadow: none !important;
                border: 1px solid #ddd !important;
            }
            
            .back-button {
                display: none !important;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="header">
            <h1><i class="fas fa-fish"></i> FishShop</h1>
            <div class="order-number">Chi tiết đơn hàng #${order.orderNumber}</div>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <!-- Order Information -->
            <div class="card">
                <div class="card-header">
                    <div class="icon">
                        <i class="fas fa-receipt"></i>
                    </div>
                    <h3>Thông tin đơn hàng</h3>
                </div>
                
                <div class="info-item">
                    <div class="label">
                        <i class="fas fa-calendar-alt"></i>
                        Ngày đặt hàng
                    </div>
                    <div class="value">
                        <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                </div>
                
                <div class="info-item">
                    <div class="label">
                        <i class="fas fa-info-circle"></i>
                        Trạng thái
                    </div>
                    <div class="value">
                        <c:choose>
                            <c:when test="${order.status.equalsIgnoreCase('PENDING')}">
                                <span class="status-badge status-pending">
                                    <i class="fas fa-clock"></i> Chờ xác nhận
                                </span>
                            </c:when>
                            <c:when test="${order.status.equalsIgnoreCase('CONFIRMED')}">
                                <span class="status-badge status-confirmed">
                                    <i class="fas fa-check"></i> Đã xác nhận
                                </span>
                            </c:when>
                            <c:when test="${order.status.equalsIgnoreCase('SHIPPING')}">
                                <span class="status-badge status-shipping">
                                    <i class="fas fa-truck"></i> Đang giao
                                </span>
                            </c:when>
                            <c:when test="${order.status.equalsIgnoreCase('DELIVERED')}">
                                <span class="status-badge status-delivered">
                                    <i class="fas fa-check-circle"></i> Đã giao
                                </span>
                            </c:when>
                            <c:when test="${order.status.equalsIgnoreCase('CANCELLED')}">
                                <span class="status-badge status-cancelled">
                                    <i class="fas fa-times-circle"></i> Đã hủy
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge status-pending">Unknown: ${order.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="info-item">
                    <div class="label">
                        <i class="fas fa-credit-card"></i>
                        Phương thức thanh toán
                    </div>
                    <div class="value">
                        <div class="payment-method">
                            <c:choose>
                                <c:when test="${order.paymentMethod == 'cod'}">
                                    <i class="fas fa-money-bill-wave"></i> Thanh toán khi nhận hàng
                                </c:when>
                                <c:when test="${order.paymentMethod == 'e-wallet'}">
                                    <i class="fas fa-wallet"></i> VNPay
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-question-circle"></i> ${order.paymentMethod}
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Address Information -->
            <div class="card">
                <div class="card-header">
                    <div class="icon">
                        <i class="fas fa-map-marker-alt"></i>
                    </div>
                    <h3>Địa chỉ giao hàng</h3>
                </div>
                
                <c:set var="address" value="${null}"/>
                <c:forEach items="${requestScope.ADDRESSES}" var="addr">
                    <c:if test="${addr.addressId == order.shippingAddressId}">
                        <c:set var="address" value="${addr}"/>
                    </c:if>
                </c:forEach>
                
                <c:choose>
                    <c:when test="${address != null}">
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-user"></i>
                                Người nhận
                            </div>
                            <div class="value">${address.recipientName}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-phone"></i>
                                Số điện thoại
                            </div>
                            <div class="value">${address.phone}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-map"></i>
                                Tỉnh/Thành phố
                            </div>
                            <div class="value">${address.province}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-map-pin"></i>
                                Quận/Huyện
                            </div>
                            <div class="value">${address.district}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-location-arrow"></i>
                                Phường/Xã
                            </div>
                            <div class="value">${address.ward}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-home"></i>
                                Địa chỉ chi tiết
                            </div>
                            <div class="value">${address.addressDetail}</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="info-item">
                            <div class="label">
                                <i class="fas fa-exclamation-triangle"></i>
                                Thông tin địa chỉ
                            </div>
                            <div class="value">Không có dữ liệu</div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Products List -->
            <div class="card products-section">
                <div class="card-header">
                    <div class="icon">
                        <i class="fas fa-box"></i>
                    </div>
                    <h3>Danh sách sản phẩm</h3>
                </div>
                
                <table class="products-table">
                    <thead>
                        <tr>
                            <th><i class="fas fa-image"></i> Hình ảnh</th>
                            <th><i class="fas fa-fish"></i> Sản phẩm</th>
                            <th><i class="fas fa-sort-numeric-up"></i> Số lượng</th>
                            <th><i class="fas fa-tag"></i> Đơn giá</th>
                            <th><i class="fas fa-calculator"></i> Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${order.items}">
                            <c:set var="product" value="${null}"/>
                            <c:forEach items="${requestScope.PRODUCTS}" var="prod">
                                <c:if test="${prod.productId == item.productId}">
                                    <c:set var="product" value="${prod}"/>
                                </c:if>
                            </c:forEach>
                            <tr>
                                <td class="product-thumb">
                                    <c:choose>
                                        <c:when test="${not empty product.mainImageUrl}">
                                            <c:catch var="exception">
                                                <img src="${pageContext.request.contextPath}/${product.mainImageUrl}?v=${System.currentTimeMillis()}" 
                                                     alt="${item.productName}" 
                                                     onerror="this.src='${pageContext.request.contextPath}/views/assets/images/products/test.jpg?v=${System.currentTimeMillis()}'" />
                                            </c:catch>
                                            <c:if test="${not empty exception}">
                                                <img src="${pageContext.request.contextPath}/views/assets/images/products/test.jpg?v=${System.currentTimeMillis()}" 
                                                     alt="${item.productName}" />
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/views/assets/images/products/test.jpg?v=${System.currentTimeMillis()}" 
                                                 alt="${item.productName}" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="product-info">
                                    <div class="product-name">${item.productName}</div>
                                    <c:if test="${not empty product.attributes}">
                                        <div class="product-attributes">
                                            <c:forEach items="${product.attributes}" var="attribute">
                                                <span>${attribute.name}: ${attribute.value}</span>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="quantity-badge">${item.quantity}</div>
                                </td>
                                <td class="price">
                                    <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0"/>đ
                                </td>
                                <td class="total-price">
                                    <fmt:formatNumber value="${item.subtotal}" pattern="#,##0"/>đ
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <!-- Price Breakdown -->
                <div class="price-breakdown">
                    <div class="price-row">
                        <div class="label">
                            <i class="fas fa-calculator"></i>
                            Tạm tính
                        </div>
                        <div class="value">
                            <fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/>đ
                        </div>
                    </div>
                    
                    <div class="price-row">
                        <div class="label discount">
                            <i class="fas fa-percent"></i>
                            Giảm giá
                        </div>
                        <div class="value discount">
                            -<fmt:formatNumber value="${order.discountAmount}" pattern="#,##0"/>đ
                        </div>
                    </div>
                    
                    <div class="price-row">
                        <div class="label">
                            <i class="fas fa-shipping-fast"></i>
                            Phí vận chuyển
                        </div>
                        <div class="value">
                            <fmt:formatNumber value="${order.shippingFee}" pattern="#,##0"/>đ
                        </div>
                    </div>
                    
                    <div class="price-row">
                        <div class="label">
                            <i class="fas fa-receipt"></i>
                            Tổng thanh toán
                        </div>
                        <div class="value">
                            <fmt:formatNumber value="${order.finalAmount}" pattern="#,##0"/>đ
                        </div>
                    </div>
                </div>
                
                <!-- Notes Section -->
                <c:if test="${not empty order.notes}">
                    <div class="notes-section">
                        <i class="fas fa-sticky-note icon"></i>
                        <strong>Ghi chú:</strong> ${order.notes}
                    </div>
                </c:if>
            </div>
        </div>
        
        <!-- Back Button -->
        <div style="text-align: center;">
            <a href="orders" class="back-button">
                <i class="fas fa-arrow-left"></i>
                Quay lại danh sách đơn hàng
            </a>
        </div>
    </div>
</body>
</html>