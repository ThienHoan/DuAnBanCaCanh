<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>FishShop - Checkout</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="view/assets/home/img/favicon.png">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f9fafb;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 1.5rem;
        }
        .breadcrumbs ul {
            display: flex;
            gap: 0.5rem;
            list-style: none;
            padding: 0;
        }
        .breadcrumbs a {
            color: #10b981;
            text-decoration: none;
            transition: color 0.2s;
        }
        .breadcrumbs a:hover {
            color: #047857;
        }
        .card {
            background: white;
            border-radius: 0.75rem;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
            margin-bottom: 1.5rem;
        }
        .form-label {
            font-size: 0.875rem;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 0.5rem;
        }
        .form-input, .form-select, .form-textarea {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #d1d5db;
            border-radius: 0.5rem;
            font-size: 0.875rem;
            color: #1f2937;
            transition: border-color 0.2s, box-shadow 0.2s;
        }
        .form-input:focus, .form-select:focus, .form-textarea:focus {
            outline: none;
            border-color: #10b981;
            box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
        }
        .new_address_fields {
            display: none;
        }
        .order-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
        }
        .order-table th, .order-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #e5e7eb;
        }
        .order-table th {
            background: #f9fafb;
            font-weight: 600;
            color: #1f2937;
        }
        .product-thumb img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 0.25rem;
        }
        .product-name a {
            color: #1f2937;
            text-decoration: none;
            font-weight: 500;
        }
        .product-name a:hover {
            color: #10b981;
        }
        .product-attributes {
            font-size: 0.75rem;
            color: #6b7280;
            margin-top: 0.25rem;
        }
        .cart-subtotal {
            display: flex;
            justify-content: space-between;
            padding: 0.5rem 0;
            font-size: 0.875rem;
        }
        .btn-primary {
            background: #10b981;
            color: white;
            padding: 0.75rem 1.5rem;
            border-radius: 0.5rem;
            border: none;
            font-weight: 500;
            cursor: pointer;
            transition: background 0.2s;
        }
        .btn-primary:hover {
            background: #047857;
        }
        .alert {
            padding: 1rem;
            border-radius: 0.5rem;
            margin-bottom: 1rem;
        }
        .alert-danger {
            background: #fee2e2;
            color: #b91c1c;
        }
        @media (max-width: 768px) {
            .col-lg-6 {
                flex: 0 0 100%;
            }
        }
    </style>
    <script>
        function toggleNewAddressFields() {
            const addressSelect = document.getElementById("addressId");
            const newAddressFields = document.getElementById("new_address_fields");
            const inputs = newAddressFields.querySelectorAll("input");
            newAddressFields.style.display = addressSelect.value === "new" ? "block" : "none";
            inputs.forEach(input => {
                input.disabled = addressSelect.value !== "new";
                if (addressSelect.value !== "new") {
                    input.removeAttribute("required");
                } else {
                    input.setAttribute("required", "");
                }
            });
        }

        function togglePaymentMethod() {
            const paymentMethod = document.getElementById("payment_method").value;
            const placeOrderButton = document.getElementById("place_order_btn");
            placeOrderButton.innerText = paymentMethod === "e-wallet" ? "Thanh toán qua VNPay" : "Đặt hàng";
        }

        window.onload = function() {
            toggleNewAddressFields();
            togglePaymentMethod();
        };
    </script>
</head>
<body>
    <div class="container">
        <div class="breadcrumbs">
            <ul>
                <li><a href="products">Home</a></li>
                <li><span class="text-gray-500 mx-1">/</span></li>
                <li><a href="cart">Cart</a></li>
                <li><span class="text-gray-500 mx-1">/</span></li>
                <li>Checkout</li>
            </ul>
        </div>

        <div class="mt-8">
            <div class="flex flex-wrap -mx-4">
                <c:if test="${not empty sessionScope.ERROR_MESSAGE}">
                    <div class="w-full px-4">
                        <div class="alert alert-danger">${sessionScope.ERROR_MESSAGE}</div>
                        <c:remove var="ERROR_MESSAGE" scope="session"/>
                    </div>
                </c:if>
                <c:if test="${empty sessionScope.CART.items}">
                    <div class="w-full px-4">
                        <div class="alert alert-danger">
                            Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.
                        </div>
                        <a href="DispatchServlet" class="text-emerald-600 hover:text-emerald-800">Quay lại trang chủ</a>
                    </div>
                </c:if>
                <c:if test="${not empty sessionScope.CART.items}">
                    <div class="w-full lg:w-1/2 px-4 col-lg-6">
                        <div class="card">
                            <h3 class="text-lg font-semibold mb-4">Thông tin nhận hàng</h3>
                            <form action="${pageContext.request.contextPath}/checkout" method="POST">
                                <input type="hidden" name="action" value="placeOrder">
                                <div class="mb-4">
                                    <label for="addressId" class="form-label">Chọn địa chỉ *</label>
                                    <select id="addressId" name="addressId" class="form-select" onchange="toggleNewAddressFields()" required>
                                        <c:forEach items="${requestScope.ADDRESSES}" var="address">
                                            <option value="${address.addressId}" ${address.isDefault ? 'selected' : ''}>
                                                ${address.recipientName}, ${address.phone}, ${address.addressDetail}, ${address.ward}, ${address.district}, ${address.province}
                                            </option>
                                        </c:forEach>
                                        <option value="new">Thêm địa chỉ mới</option>
                                    </select>
                                </div>
                                <div id="new_address_fields" class="new_address_fields">
                                    <div class="mb-4">
                                        <label for="recipientName" class="form-label">Tên người nhận *</label>
                                        <input type="text" id="recipientName" name="recipientName" class="form-input" value="${sessionScope.user.fullName}">
                                    </div>
                                    <div class="mb-4">
                                        <label for="phone" class="form-label">Số điện thoại *</label>
                                        <input type="text" id="phone" name="phone" class="form-input" value="${sessionScope.user.phone}" pattern="\d{10,11}" title="Số điện thoại phải có 10-11 chữ số">
                                    </div>
                                    <div class="mb-4">
                                        <label for="province" class="form-label">Tỉnh/Thành phố *</label>
                                        <input type="text" id="province" name="province" class="form-input">
                                    </div>
                                    <div class="mb-4">
                                        <label for="district" class="form-label">Quận/Huyện *</label>
                                        <input type="text" id="district" name="district" class="form-input">
                                    </div>
                                    <div class="mb-4">
                                        <label for="ward" class="form-label">Phường/Xã *</label>
                                        <input type="text" id="ward" name="ward" class="form-input">
                                    </div>
                                    <div class="mb-4">
                                        <label for="addressDetail" class="form-label">Chi tiết địa chỉ *</label>
                                        <input type="text" id="addressDetail" name="addressDetail" class="form-input">
                                    </div>
                                </div>
                                <div class="mb-4">
                                    <label for="payment_method" class="form-label">Phương thức thanh toán *</label>
                                    <select id="payment_method" name="paymentMethod" class="form-select" onchange="togglePaymentMethod()" required>
                                        <option value="cod">Thanh toán khi nhận hàng</option>
                                        <option value="e-wallet">VNPay</option>
                                    </select>
                                </div>
                                <div class="mb-4">
                                    <label for="notes" class="form-label">Ghi chú đơn hàng</label>
                                    <textarea id="notes" name="notes" class="form-textarea" rows="4"></textarea>
                                </div>
                                <div class="text-right">
                                    <button type="submit" id="place_order_btn" class="btn-primary">Đặt hàng</button>
                                </div>
                            </form>
                        </div>
                        <div class="card">
                            <h3 class="text-lg font-semibold mb-4">Mã giảm giá</h3>
                            <form action="${pageContext.request.contextPath}/apply-coupon" method="POST" class="flex gap-2">
                                <input type="text" id="couponCode" name="couponCode" class="form-input" placeholder="Nhập mã giảm giá">
                                <button type="submit" class="btn-primary">Áp dụng</button>
                            </form>
                            <c:if test="${not empty sessionScope.COUPON}">
                                <p class="text-emerald-600 mt-2">Mã giảm giá ${sessionScope.COUPON.code} đã được áp dụng (-<fmt:formatNumber value="${sessionScope.COUPON.discountAmount}" pattern="#,##0"/>đ)</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="w-full lg:w-1/2 px-4 col-lg-6">
                        <div class="card">
                            <h3 class="text-lg font-semibold mb-4">Đơn hàng của bạn</h3>
                            <div class="overflow-x-auto">
                                <table class="order-table">
                                    <thead>
                                        <tr>
                                            <th>Ảnh</th>
                                            <th>Sản phẩm</th>
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                            <th>Tổng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${sessionScope.CART.items}" var="item">
                                            <tr>
                                                <td class="product-thumb">
                                                    <c:set var="imagePath" value="${item.product.mainImageUrl}" />
                                                    <c:choose>
                                                        <c:when test="${not empty imagePath}">
                                                            <c:catch var="exception">
                                                                <img src="${pageContext.request.contextPath}/${imagePath}?v=${System.currentTimeMillis()}" 
                                                                     alt="${item.product.name}" 
                                                                     onerror="this.src='${pageContext.request.contextPath}/views/assets/images/products/test.jpg?v=${System.currentTimeMillis()}'" />
                                                            </c:catch>
                                                            <c:if test="${not empty exception}">
                                                                <img src="${pageContext.request.contextPath}/views/assets/images/products/test.jpg?v=${System.currentTimeMillis()}" 
                                                                     alt="${item.product.name}" />
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/views/assets/images/products/test.jpg?v=${System.currentTimeMillis()}" 
                                                                 alt="${item.product.name}" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="product-name">
                                                    <span>${item.product.name}</span>
                                                    <div class="product-attributes">
                                                        <c:forEach items="${item.product.attributes}" var="attribute">
                                                            <span>${attribute.name}: ${attribute.value}</span>
                                                        </c:forEach>
                                                    </div>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.product.salePrice != null}">
                                                            <fmt:formatNumber value="${item.product.salePrice}" pattern="#,##0"/>đ
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${item.product.price}" pattern="#,##0"/>đ
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${item.quantity}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.product.salePrice != null}">
                                                            <fmt:formatNumber value="${item.product.salePrice * item.quantity}" pattern="#,##0"/>đ
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${item.product.price * item.quantity}" pattern="#,##0"/>đ
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="mt-4">
                                <div class="cart-subtotal">
                                    <span>Tạm tính</span>
                                    <span>
                                        <c:set var="totalPrice" value="0" />
                                        <c:forEach items="${sessionScope.CART.items}" var="item">
                                            <c:set var="productTotal" value="${item.quantity * (item.product.salePrice != null ? item.product.salePrice : item.product.price)}" />
                                            <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                        </c:forEach>
                                        <fmt:formatNumber value="${totalPrice}" pattern="#,##0"/>đ
                                    </span>
                                </div>
                                <div class="cart-subtotal">
                                    <span>Giảm giá</span>
                                    <span>
                                        <c:set var="discountAmount" value="${sessionScope.COUPON != null ? sessionScope.COUPON.discountAmount : 0}" />
                                        <fmt:formatNumber value="${discountAmount}" pattern="#,##0"/>đ
                                    </span>
                                </div>
                                <div class="cart-subtotal">
                                    <span>Phí vận chuyển</span>
                                    <span><fmt:formatNumber value="20000" pattern="#,##0"/>đ</span>
                                </div>
                                <div class="cart-subtotal font-semibold text-lg">
                                    <span>Tổng cộng</span>
                                    <span><fmt:formatNumber value="${totalPrice - discountAmount + 20000}" pattern="#,##0"/>đ</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</body>
</html>