<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fish Shop - Khơi nguồn sống động</title>
    <link rel="stylesheet" href="assets/css/about-shop.css">
</head>

<body>
    <header>
        <div class="content-fit">
            <li class="logo"><a href="home" style="text-decoration: none">Trang Chủ</a></li>
            <nav>
                <ul>
                    <li class="menu-item"><a href="contact_us.jsp" style="text-decoration: none">Liên Hệ</a></li>
                    <li class="menu-item"><a href="category" style="text-decoration: none">Sản Phẩm</a></li>
                    <li class="menu-item"><a href="login" style="text-decoration: none">Đăng Nhập</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <div class="section" id="banner">
        <div class="content-fit">
            <div class="title" data-before="Mang cả đại dương vào ngôi nhà bạn!">Mang cả đại dương vào ngôi nhà bạn!</div>
        </div>
        <img src="assets/images/home-04/flower.png" class="decorate" alt="" style="width: 20vw; bottom: 0; right: 0;">
        <img src="assets/images/home-04/leaf.png" class="decorate" alt="" style="width: 30vw; bottom: -240px; left: 0; ">
    </div>

    <div class="section" id="intro">
        <div class="content-fit">
            <div class="number">01</div>
            <div class="des">
                <div class="title">Về Fish Shop</div>
                <p>
                    Fish Shop là điểm đến lý tưởng cho những ai đam mê thế giới cá cảnh. Chúng tôi chuyên cung cấp các loại cá khỏe mạnh, đa dạng về màu sắc và chủng loại, cùng các phụ kiện, thức ăn và dịch vụ chăm sóc cá cảnh chuyên nghiệp. Với đội ngũ nhân viên giàu kinh nghiệm, Fish Shop cam kết sẽ giúp bạn tạo dựng một không gian thủy sinh tuyệt đẹp, mang đến niềm vui và thư giãn cho cuộc sống mỗi ngày.
                </p>
            </div>
        </div>
    </div>

    <div class="section" id="description">
        <div class="content-fit">
            <div class="number">02</div>
            <div class="des">
                <div class="title">Tại sao lựa chọn chúng tôi?</div>
                <p>
                    Đến với Fish Shop, bạn không chỉ nhận được sản phẩm chất lượng hàng đầu, mà còn trải nghiệm dịch vụ chăm sóc khách hàng tận tình, chu đáo. Chúng tôi hiểu rõ giá trị của từng sản phẩm, tư vấn tận tâm giúp bạn lựa chọn được những chú cá cảnh và thiết bị phù hợp nhất cho ngôi nhà của mình. Hãy cùng Fish Shop biến ước mơ thủy sinh của bạn thành hiện thực ngay hôm nay!
                </p>
            </div>
        </div>
        <img src="assets/images/home-04/leaf1.png" class="decorate" alt="" style="width: 60vw; bottom: 100px; right: -142px; z-index: 101;">
    </div>

    <div class="section" id="contact">
        <div class="content-fit">
            <div class="number">03</div>
            <div class="des">
                <div class="title">Thông tin liên hệ</div>
                <table>
                    <tr>
                        <td>Email:</td>
                        <td>fishshop@gmail.com</td>
                    </tr>
                    <tr>
                        <td>Số điện thoại:</td>
                        <td>+84 123 456 789</td>
                    </tr>
                    <tr>
                        <td>Website:</td>
                        <td>www.fishshop.vn</td>
                    </tr>
                    <tr>
                        <td>Fanpage:</td>
                        <td>facebook.com/fishshop.vn</td>
                    </tr>
                </table>
                <div class="sign">Fish Shop – Nuôi dưỡng đam mê</div>
            </div>
        </div>
    </div>

    <div id="container3D"></div>
    <script type="module" src="assets/js/about-shop.js"></script>
</body>

</html>
