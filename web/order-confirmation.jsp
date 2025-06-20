<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>FishShop-Order Confirmation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="view/assets/home/img/favicon.png">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            width: 80%;
            margin: auto;
            overflow: hidden;
            padding: 20px;
        }
        .confirmation_area {
            background: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            padding: 20px;
            text-align: center;
        }
        .confirmation_area h3 {
            font-size: 24px;
            margin-bottom: 20px;
        }
        .alert {
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 4px;
        }
        .alert-success {
            background: #d4edda;
            color: #155724;
        }
        .alert-danger {
            background: #f8d7da;
            color: #721c24;
        }
        .confirmation_area a {
            display: inline-block;
            padding: 10px 20px;
            background: #00bba6;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 20px;
        }
        .confirmation_area a:hover {
            background: #009688;
        }
    </style>
</head>
<body>
    <div class="pos_page">
        <div class="container">
            <div class="confirmation_area">
                <h3>Xác nhận đơn hàng</h3>
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success">${param.success}</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>
                <p>Cảm ơn bạn đã đặt hàng! Mã đơn hàng của bạn là: <strong>${param.orderId}</strong></p>
                <p>Chúng tôi sẽ gửi thông tin chi tiết qua email: <strong>${sessionScope.user.email}</strong></p>
                <a href="home">Quay lại trang chủ</a>
            </div>
        </div>
    </div>
</body>
</html>
