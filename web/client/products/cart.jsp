<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng của bạn</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <!-- JQuery, Popper.js, và Bootstrap JS (đảm bảo đúng thứ tự) -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <style>
        /* Modern Cart Styles - Matching Product Management Style */
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --secondary-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            --warning-gradient: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            --danger-gradient: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            --info-gradient: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);

            --shadow-soft: 0 7.5px 32.5px 0 rgba(31, 38, 135, 0.37);
            --shadow-hover: 0 15px 35px rgba(31, 38, 135, 0.2);
            --border-radius: 22px;
            --transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
        }

        /* Global Styles */
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            font-size: 1rem;
        }

        .container {
            padding: 3rem 15px;
            max-width: 1400px;
            margin: 0 auto;
        }

        /* Page Header */
        .page-header {
            background: linear-gradient(90deg, #57d6fd 0%, #7857fd 100%);
            color: #fff;
            padding: 2rem 2.5rem;
            border-radius: 27.5px;
            margin-bottom: 2rem;
            box-shadow: var(--shadow-soft);
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .page-title {
            font-size: 2.1rem;
            font-weight: 800;
            text-shadow: 1.25px 2.5px 7.5px #8671e7a2;
            letter-spacing: .04em;
            margin-bottom: 0;
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        /* Glass Card Effect */
        .card {
            border-radius: 27.5px;
            box-shadow: 0 11px 32.5px 0 rgba(90,120,200,0.16), 0 2.75px 7.5px 0 rgba(60,72,88,0.12);
            overflow: hidden;
            border: none;
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            margin-bottom: 2rem;
        }

        .card-body {
            padding: 2rem;
        }

        /* Alert Messages */
        .alert {
            border-radius: 15px;
            border: none;
            margin-bottom: 25px;
            box-shadow: 0 3.75px 15px rgba(0,0,0,0.1);
            font-size: 1rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .alert-success {
            background: linear-gradient(90deg, #81fbb8 0%, #28c76f 100%);
            color: #23653a;
        }

        .alert-danger {
            background: linear-gradient(90deg, #fdc5c5 0%, #fc5c7d 100%);
            color: #84213b;
        }

        .alert-info {
            background: linear-gradient(90deg, #a8edea 0%, #fed6e3 100%);
            color: #495057;
        }

        /* Cart Table Styles */
        .table-responsive {
            border-radius: 16.25px;
            box-shadow: var(--shadow-soft);
            overflow: hidden;
            margin-bottom: 2rem;
        }

        .table {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            margin: 0;
            border-collapse: separate;
            border-spacing: 0;
            font-size: 1rem;
        }

        .table thead th {
            background: var(--primary-gradient);
            color: white;
            font-weight: 700;
            text-align: center;
            padding: 1.2rem 0.8rem;
            font-size: 0.95rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            border: none;
        }

        .table tbody td {
            padding: 1rem 0.8rem;
            vertical-align: middle;
            text-align: center;
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
            transition: var(--transition);
            font-size: 0.95rem;
        }

        .table-hover tbody tr {
            transition: background-color 0.2s ease, box-shadow 0.2s ease;
        }

        .table-hover tbody tr:hover {
            background: linear-gradient(90deg,#e8f0fe 0%, #ffe3ed 100%);
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }

        /* Product Info in Cart */
        .product-name {
            font-weight: 700;
            color: #5643fa;
            font-size: 1rem;
            margin-bottom: 0.25rem;
        }

        .product-description {
            font-size: 0.8em;
            color: #6c757d;
            font-style: italic;
            line-height: 1.4;
        }

        /* Price Styling */
        .price {
            font-weight: 600;
            color: #28a745;
            font-size: 1rem;
        }

        .price-total {
            font-weight: 700;
            color: #dc3545;
            font-size: 1.1rem;
        }

        /* Quantity Input */
        .quantity-input {
            width: 70px;
            text-align: center;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 0.375rem;
            font-weight: 600;
            transition: var(--transition);
        }

        .quantity-input:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
            outline: none;
        }

        /* Button Styles */
        .btn {
            border-radius: 12.5px;
            font-weight: 600;
            padding: 0.5rem 1rem;
            transition: var(--transition);
            border: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }

        .btn-primary {
            background: linear-gradient(90deg,#ee9ca7 0%, #ffdde1 100%);
            color: #424874 !important;
            box-shadow: 0 2.75px 13.75px #c48ce25a;
        }

        .btn-primary:hover {
            background: linear-gradient(90deg, #a8edea 0%, #fed6e3 100%);
            color: #7c2ae8 !important;
            box-shadow: 0 7.5px 30px #9469e46a;
            transform: translateY(-2px);
        }

        .btn-info {
            background: linear-gradient(90deg,#d7d2cc 0%, #304352 100%);
            color: #fff !important;
            font-size: 0.875rem;
        }

        .btn-info:hover {
            background: #81ecec !important;
            color: #342343 !important;
            transform: translateY(-1px);
        }

        .btn-warning {
            background: linear-gradient(90deg,#fceabb 0%, #f8b500 100%);
            color: #654321 !important;
        }

        .btn-warning:hover {
            background: #fffde4 !important;
            color: #f59e00 !important;
            transform: translateY(-1px);
        }

        .btn-danger {
            background: linear-gradient(90deg,#ff5858 0%, #f09819 100%);
            color: #fff !important;
            font-size: 0.875rem;
        }

        .btn-danger:hover {
            background: #ffc3a0 !important;
            color: #84213b !important;
            transform: translateY(-1px);
        }

        .btn-success {
            background: linear-gradient(90deg,#43e97b 0%, #38f9d7 100%);
            color: #fff !important;
        }

        .btn-success:hover {
            background: #bff098 !important;
            color: #17575c !important;
            transform: translateY(-1px);
        }

        /* Cart Summary */
        .cart-summary {
            background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
            border-radius: 20px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: var(--shadow-soft);
        }

        .cart-summary h4 {
            color: #6c5b7b;
            font-weight: 700;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .summary-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0.75rem 0;
            border-bottom: 1px solid rgba(255,255,255,0.3);
            font-size: 1.1rem;
        }

        .summary-item:last-child {
            border-bottom: none;
            font-weight: 700;
            font-size: 1.25rem;
            color: #8b4513;
        }

        .cart-counter {
            background: #28a745;
            color: white;
            padding: 0.25rem 0.75rem;
            border-radius: 15px;
            font-weight: 700;
            font-size: 0.9rem;
        }

        /* Empty State */
        .empty-state {
            background: linear-gradient(120deg,#f6d365 0%, #fda085 100%);
            border-radius: 27.5px;
            padding: 4rem 2rem;
            box-shadow: 0 2.75px 12.5px #dcb6f5a5;
            text-align: center;
        }

        .empty-state i {
            color: #ad5389;
            margin-bottom: 1rem;
            font-size: 4rem;
        }

        .empty-state h5 {
            color: #6c5b7b;
            font-weight: 600;
            margin-bottom: 1rem;
            font-size: 1.5rem;
        }

        .empty-state p {
            color: #8b7ca3;
            margin-bottom: 2rem;
            font-size: 1.125rem;
        }

        /* Action Buttons Section */
        .cart-actions {
            display: flex;
            gap: 1rem;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            margin-top: 2rem;
        }

        .cart-actions .btn {
            flex: 1;
            min-width: 200px;
            justify-content: center;
            padding: 0.75rem 1.5rem;
            font-size: 1rem;
        }

        /* Responsive Design */
        @media (max-width: 1200px) {
            .table th, .table td {
                font-size: 0.9em;
                padding: 0.8rem 0.5rem;
            }
            
            .page-title {
                font-size: 1.8rem;
            }
            
            .container {
                padding: 2rem 15px;
            }
        }

        @media (max-width: 768px) {
            .table th, .table td {
                font-size: 0.8em;
                padding: 0.6rem 0.3rem;
            }
            
            .page-header {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }
            
            .page-title {
                font-size: 1.5rem;
            }

            .cart-actions {
                flex-direction: column;
            }

            .cart-actions .btn {
                width: 100%;
            }

            .quantity-input {
                width: 60px;
            }
        }

        /* Animation for alerts */
        .alert {
            animation: slideInDown 0.5s ease-out;
        }

        @keyframes slideInDown {
            from {
                transform: translateY(-100%);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        /* Fade Out Animation */
        .fade-out {
            animation: fadeOut 0.5s ease-out forwards;
        }

        @keyframes fadeOut {
            from {
                opacity: 1;
                transform: translateY(0);
            }
            to {
                opacity: 0;
                transform: translateY(-20px);
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Page Header -->
        <div class="page-header">
            <h1 class="page-title">
                <i class="fas fa-shopping-cart"></i>
                Giỏ hàng của bạn
            </h1>
        </div>
        
        <!-- Alert Messages -->
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle"></i>
                ${sessionScope.message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <c:remove var="message" scope="session"/>
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="fas fa-exclamation-circle"></i>
                ${sessionScope.error}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>
        
        <c:if test="${not empty cartItems}">
            <!-- Cart Items Table -->
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th><i class="fas fa-hashtag"></i> STT</th>
                                    <th><i class="fas fa-box"></i> Sản phẩm</th>
                                    <th><i class="fas fa-tag"></i> Đơn giá</th>
                                    <th><i class="fas fa-sort-numeric-up"></i> Số lượng</th>
                                    <th><i class="fas fa-calculator"></i> Thành tiền</th>
                                    <th><i class="fas fa-cogs"></i> Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${cartItems}" varStatus="status">
                                    <tr>
                                        <td>
                                            <span class="badge badge-primary">${status.index + 1}</span>
                                        </td>
                                        <td>
                                            <div class="product-name">${item.productName}</div>
                                        </td>
                                        <td>
                                            <span class="price">
                                                <fmt:formatNumber value="${item.productPrice}" pattern="#,##0"/> ₫
                                            </span>
                                        </td>
                                        <td>
                                            <form action="cartClient" method="post" style="display: inline-block;">
                                                <input type="hidden" name="action" value="update"/>
                                                <input type="hidden" name="cartItemId" value="${item.cartItemId}"/>
                                                <div class="input-group" style="max-width: 120px; margin: 0 auto;">
                                                    <input type="number" name="quantity" value="${item.quantity}" 
                                                           min="1" class="quantity-input"/>
                                                    <div class="input-group-append">
                                                        <button type="submit" class="btn btn-info btn-sm">
                                                            <i class="fas fa-sync-alt"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </form>
                                        </td>
                                        <td>
                                            <span class="price-total">
                                                <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0"/> ₫
                                            </span>
                                        </td>
                                        <td>
                                            <form action="cartClient" method="post" style="display:inline-block;">
                                                <input type="hidden" name="action" value="remove"/>
                                                <input type="hidden" name="cartItemId" value="${item.cartItemId}"/>
                                                <button type="submit" class="btn btn-danger btn-sm" 
                                                        onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?')">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Cart Summary -->
            <div class="cart-summary">
                <h4>
                    <i class="fas fa-receipt"></i>
                    Tổng kết đơn hàng
                </h4>
                <div class="summary-item">
                    <span><i class="fas fa-cubes"></i> Tổng số sản phẩm:</span>
                    <span class="cart-counter">${itemCount}</span>
                </div>
                <div class="summary-item">
                    <span><i class="fas fa-money-bill-wave"></i> Tổng tiền:</span>
                    <span class="price-total">
                        <fmt:formatNumber value="${cartTotal}" pattern="#,##0"/> ₫
                    </span>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="cart-actions">
                <form action="cartClient" method="post" style="flex: 1;">
                    <input type="hidden" name="action" value="clear"/>
                    <button type="submit" class="btn btn-warning"
                            onclick="return confirm('Bạn có chắc chắn muốn xóa toàn bộ giỏ hàng?')">
                        <i class="fas fa-trash-alt"></i>
                        Xóa toàn bộ giỏ hàng
                    </button>
                </form>
                
                <a href="products.jsp" class="btn btn-primary">
                    <i class="fas fa-shopping-bag"></i>
                    Tiếp tục mua hàng
                </a>
                
                <a href="checkout.jsp" class="btn btn-success">
                    <i class="fas fa-credit-card"></i>
                    Thanh toán
                </a>
            </div>
        </c:if>

        <!-- Empty Cart State -->
        <c:if test="${empty cartItems}">
            <div class="empty-state">
                <i class="fas fa-shopping-cart"></i>
                <h5>Giỏ hàng của bạn đang trống</h5>
                <p>Hãy khám phá các sản phẩm tuyệt vời của chúng tôi!</p>
                <a href="products.jsp" class="btn btn-primary">
                    <i class="fas fa-shopping-bag"></i>
                    Mua sắm ngay
                </a>
            </div>
        </c:if>
    </div>

    <script>
        // Tự động ẩn thông báo sau 5 giây
        setTimeout(function(){
            let alerts = document.querySelectorAll('.alert-success, .alert-danger');
            alerts.forEach(alert => {
                alert.classList.add('fade-out');
                setTimeout(() => {
                    alert.style.display = 'none';
                }, 500);
            });
        }, 5000);

        // Xác nhận xóa sản phẩm với hiệu ứng
        function confirmDelete(productName) {
            return confirm(`Bạn có chắc chắn muốn xóa "${productName}" khỏi giỏ hàng?`);
        }

        // Animation cho buttons khi hover
        document.querySelectorAll('.btn').forEach(btn => {
            btn.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-2px)';
            });
            
            btn.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0)';
            });
        });
    </script>
</body>
</html>