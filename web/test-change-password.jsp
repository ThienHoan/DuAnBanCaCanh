<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Đổi Mật Khẩu</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            padding: 2rem 0;
        }
        .test-container {
            max-width: 600px;
            margin: 0 auto;
        }
        .test-section {
            background: white;
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .test-result {
            padding: 1rem;
            border-radius: 4px;
            margin-top: 1rem;
        }
        .success { background-color: #d4edda; color: #155724; }
        .error { background-color: #f8d7da; color: #721c24; }
        .info { background-color: #d1ecf1; color: #0c5460; }
    </style>
</head>
<body>
    <div class="container test-container">
        <h2 class="text-center mb-4">Test Chức Năng Đổi Mật Khẩu</h2>
        
        <!-- Thông tin user hiện tại -->
        <div class="test-section">
            <h4>Thông Tin User Hiện Tại</h4>
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <p><strong>User ID:</strong> ${sessionScope.user.userId}</p>
                    <p><strong>Username:</strong> ${sessionScope.user.username}</p>
                    <p><strong>Email:</strong> ${sessionScope.user.email}</p>
                    <p><strong>Full Name:</strong> ${sessionScope.user.fullName}</p>
                    <p><strong>Password (hashed):</strong> ${sessionScope.user.password.substring(0, 20)}...</p>
                </c:when>
                <c:otherwise>
                    <div class="test-result error">
                        Chưa đăng nhập. <a href="login">Đăng nhập tại đây</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Form test đổi mật khẩu -->
        <c:if test="${sessionScope.user != null}">
            <div class="test-section">
                <h4>Test Đổi Mật Khẩu</h4>
                <form action="profile" method="post" id="changePasswordForm">
                    <input type="hidden" name="action" value="changePassword">
                    
                    <div class="mb-3">
                        <label for="currentPassword" class="form-label">Mật khẩu hiện tại:</label>
                        <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                        <small class="form-text text-muted">Nhập mật khẩu hiện tại để xác thực</small>
                    </div>
                    
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">Mật khẩu mới:</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" required minlength="6">
                        <small class="form-text text-muted">Tối thiểu 6 ký tự</small>
                    </div>
                    
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu mới:</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                        <small class="form-text text-muted">Nhập lại mật khẩu mới để xác nhận</small>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Đổi Mật Khẩu</button>
                    <button type="reset" class="btn btn-secondary">Reset Form</button>
                </form>
            </div>
        </c:if>
        
        <!-- Hiển thị kết quả -->
        <c:if test="${not empty requestScope.success}">
            <div class="test-section">
                <div class="test-result success">
                    <strong>Thành công:</strong> ${requestScope.success}
                </div>
            </div>
        </c:if>
        
        <c:if test="${not empty requestScope.error}">
            <div class="test-section">
                <div class="test-result error">
                    <strong>Lỗi:</strong> ${requestScope.error}
                </div>
            </div>
        </c:if>
        
        <!-- Debug info -->
        <div class="test-section">
            <h4>Debug Info</h4>
            <div class="test-result info">
                <p><strong>Request Method:</strong> ${pageContext.request.method}</p>
                <p><strong>Request URI:</strong> ${pageContext.request.requestURI}</p>
                <p><strong>Session ID:</strong> ${pageContext.session.id}</p>
                <p><strong>Timestamp:</strong> <script>document.write(new Date().toLocaleString());</script></p>
            </div>
        </div>
        
        <!-- Navigation links -->
        <div class="test-section text-center">
            <a href="profile" class="btn btn-outline-primary">Về Profile</a>
            <a href="dashboard" class="btn btn-outline-secondary">Về Dashboard</a>
            <a href="home" class="btn btn-outline-success">Về Trang Chủ</a>
        </div>
    </div>
    
    <script src="assets/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validation form
        document.getElementById('changePasswordForm').addEventListener('submit', function(e) {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu mới và xác nhận mật khẩu không khớp!');
                return false;
            }
            
            if (newPassword.length < 6) {
                e.preventDefault();
                alert('Mật khẩu mới phải có ít nhất 6 ký tự!');
                return false;
            }
            
            return confirm('Bạn có chắc chắn muốn đổi mật khẩu không?');
        });
    </script>
</body>
</html>
