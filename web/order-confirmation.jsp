<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>FISH SHOP</title>
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
        .order-confirmation {
            padding: 80px 0;
            text-align: center;
        }
        .confirmation-header {
            margin-bottom: 40px;
        }
        .confirmation-header i {
            font-size: 100px;
            color: #5cb85c;
            margin-bottom: 30px;
            display: block;
        }
        .confirmation-header h2 {
            font-size: 32px;
            margin-bottom: 15px;
            color: #333;
        }
        .confirmation-header p {
            font-size: 18px;
            color: #666;
            max-width: 600px;
            margin: 0 auto;
        }
        .action-buttons {
            margin-top: 40px;
        }
        .btn-continue-shopping {
            background-color: #5ec6fa;
            border-color: #5ec6fa;
            color: #fff;
            border-radius: 24px;
            padding: 12px 30px;
            font-size: 16px;
            font-weight: 600;
            transition: all 0.3s;
            margin-right: 15px;
        }
        .btn-continue-shopping:hover {
            background-color: #36a2eb;
            border-color: #36a2eb;
            color: #fff;
        }
        .btn-view-orders {
            background-color: #6c757d;
            border-color: #6c757d;
            color: #fff;
            border-radius: 24px;
            padding: 12px 30px;
            font-size: 16px;
            font-weight: 600;
            transition: all 0.3s;
        }
        .btn-view-orders:hover {
            background-color: #5a6268;
            border-color: #545b62;
            color: #fff;
        }
    </style>
</head>

<body class="biolife-body">
    <!-- HEADER -->
    <jsp:include page="header.jsp" />

    <!-- Main content -->
    <div class="page-contain">
        <div class="container">
            <div class="order-confirmation">
                <div class="confirmation-header">
                    <i class="fa fa-check-circle"></i>
                    <h2>Đặt hàng thành công!</h2>
                    <p>Cảm ơn bạn đã đặt hàng. Đơn hàng của bạn đã được ghi nhận và đang được xử lý.</p>
                    <p>Bạn sẽ nhận được email xác nhận đơn hàng trong thời gian sớm nhất.</p>
                </div>

                <div class="action-buttons">
                    <a href="category" class="btn btn-continue-shopping">Tiếp tục mua sắm</a>
                    <a href="order" class="btn btn-view-orders">Xem đơn hàng của tôi</a>
                </div>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <jsp:include page="footer.jsp" />

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
</body>

</html>
