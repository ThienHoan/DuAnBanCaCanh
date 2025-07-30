<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>🤖 AI Agent - DuAnCaCanh</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="assets/css/ai-buttons.css" rel="stylesheet">
    <style>
        body { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
            min-height: 100vh; 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
        }
        .agent-container { 
            background: rgba(255, 255, 255, 0.95); 
            border-radius: 15px; 
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1); 
            backdrop-filter: blur(10px); 
            margin-top: 50px; 
            padding: 30px; 
        }
        .chat-container { 
            max-height: 500px; 
            overflow-y: auto; 
            border: 2px solid #e9ecef; 
            border-radius: 15px; 
            padding: 20px; 
            background: #ffffff; 
        }
        .message { 
            margin: 15px 0; 
            padding: 12px 18px; 
            border-radius: 18px; 
            max-width: 80%; 
            position: relative; 
            word-wrap: break-word; 
            animation: fadeIn 0.3s ease-in; 
        }
        @keyframes fadeIn { 
            from { opacity: 0; transform: translateY(10px); } 
            to { opacity: 1; transform: translateY(0); } 
        }
        .user-message { 
            background: linear-gradient(135deg, #007bff, #0056b3); 
            color: white; 
            margin-left: auto; 
            border-bottom-right-radius: 5px; 
            box-shadow: 0 2px 5px rgba(0,123,255,0.3); 
        }
        .agent-message { 
            background: linear-gradient(135deg, #20c997, #17a2b8); 
            color: white; 
            margin-right: auto; 
            border-bottom-left-radius: 5px; 
            box-shadow: 0 3px 10px rgba(32,201,151,0.3); 
            line-height: 1.6; 
        }
        .system-message { 
            background: linear-gradient(135deg, #6c757d, #495057); 
            color: white; 
            text-align: center; 
            font-style: italic; 
            margin: 20px auto; 
            max-width: 60%; 
            border-radius: 20px; 
        }
        .typing-indicator { 
            background: #e9ecef; 
            color: #6c757d; 
            margin-right: auto; 
            border-radius: 18px; 
            padding: 12px 18px; 
            max-width: 100px; 
            display: none; 
        }
        .typing-dots span { 
            display: inline-block; 
            width: 8px; 
            height: 8px; 
            border-radius: 50%; 
            background-color: #6c757d; 
            margin: 0 2px; 
            animation: typing 1.4s infinite ease-in-out; 
        }
        @keyframes typing { 
            0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; } 
            40% { transform: scale(1); opacity: 1; } 
        }
        .btn-agent { 
            background: linear-gradient(45deg, #007bff, #0056b3); 
            border: none; 
            padding: 12px 30px; 
            border-radius: 25px; 
            font-weight: bold; 
            transition: all 0.3s ease; 
        }
        .btn-agent:hover { 
            transform: translateY(-2px); 
            box-shadow: 0 5px 15px rgba(0,123,255,0.4); 
        }
        
        /* Gợi ý câu hỏi phổ biến */
        .smart-suggestions { 
            background: linear-gradient(135deg, #f8f9fa, #e3f2fd); 
            border-radius: 12px; 
            padding: 20px; 
            margin: 15px 0; 
            border-left: 4px solid #2196f3; 
            box-shadow: 0 2px 8px rgba(33, 150, 243, 0.1); 
        }
        .suggestion-list { 
            display: flex; 
            flex-wrap: wrap; 
            gap: 8px; 
            margin: 10px 0; 
        }
        .suggestion-item { 
            background: rgba(0,123,255,0.1); 
            color: #007bff; 
            padding: 8px 12px; 
            border-radius: 15px; 
            font-size: 0.9em; 
            border: 1px solid rgba(0,123,255,0.2); 
            cursor: pointer; 
            transition: all 0.2s ease; 
        }
        .suggestion-item:hover { 
            background: rgba(0,123,255,0.2); 
            transform: translateY(-1px); 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="agent-container">
                    <div class="text-center mb-4">
                        <h1 class="display-4 text-primary"><i class="fas fa-robot"></i> AI Agent Interface</h1>
                        <p class="lead text-muted">🐠 Chuyên gia tư vấn cá cảnh AI - Hỗ trợ chọn cá, thiết bị và chăm sóc</p>
                        
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle"></i> <strong>AI Agent:</strong> Sẵn sàng tư vấn!
                        </div>
                    </div>

                    <div class="chat-container mb-4" id="chatContainer">
                        <div class="message system-message">
                            <i class="fas fa-fish"></i> 🐠 Chuyên gia cá cảnh AI đã sẵn sàng tư vấn!
                        </div>
                        <div class="smart-suggestions">
                             <div class="suggestions-header">💡 Gợi ý câu hỏi phổ biến:</div>
                             <div class="suggestion-list">
                                <span class="suggestion-item" onclick="setExample('Cá nào dễ nuôi cho người mới bắt đầu?')">🐠 Cá dễ nuôi cho người mới</span>
                                <span class="suggestion-item" onclick="setExample('Tư vấn cho ăn cá betta')">🍽️ Tư vấn cho ăn cá Betta</span>
                                <span class="suggestion-item" onclick="setExample('Cá của tôi bị bệnh đốm trắng')">🩺 Chẩn đoán bệnh cá</span>
                             </div>
                        </div>
                        <div class="typing-indicator" id="typingIndicator">
                            <div class="typing-dots"><span></span><span></span><span></span></div>
                        </div>
                    </div>

                    <!-- Khu vực nút đề xuất -->
                    <div id="contextButtons" class="mb-3" style="display: none;">
                        <div class="ai-context-suggestions">
                            <div class="ai-context-suggestion-header">
                                <i class="fas fa-lightbulb"></i> AI đề xuất cho bạn:
                            </div>
                            <div class="ai-suggestion-buttons" id="suggestionButtonsContainer">
                                <!-- Các nút sẽ được thêm động bằng JavaScript -->
                            </div>
                        </div>
                    </div>

                    <form id="agentForm" onsubmit="sendMessage(event)">
                        <div class="input-group mb-3">
                            <input type="text" class="form-control form-control-lg" id="userInput" name="request" placeholder="Hãy hỏi tôi về cá cảnh..." required>
                            <button class="btn btn-agent btn-lg" type="submit" id="sendBtn">
                                <span id="normalText"><i class="fas fa-paper-plane"></i> Gửi</span>
                                <span id="loadingText" style="display: none;"><span class="spinner-border spinner-border-sm"></span> Đang xử lý...</span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Lưu trạng thái hiện tại của chat
        let currentContext = {
            pendingCodCheckout: false,
            hasCart: false,
            viewingProduct: false,
            productName: null,
            lastProductsViewed: [],
            lastMessage: '',
            conversationHistory: [],
            lastIntent: null
        };

        // Các câu hỏi thường gặp theo ngữ cảnh
        const contextualQuestions = {
            general: [
                { text: "Cá nào phù hợp với bể nhỏ?", icon: "fa-fish" },
                { text: "Cách vệ sinh bể cá đúng cách", icon: "fa-broom" },
                { text: "Các loại thức ăn phổ biến cho cá", icon: "fa-drumstick-bite" }
            ],
            product: [
                { text: "So sánh với sản phẩm khác", icon: "fa-balance-scale" },
                { text: "Cách chăm sóc sản phẩm này", icon: "fa-hand-holding-heart" },
                { text: "Có khuyến mãi gì không?", icon: "fa-tags" }
            ],
            cart: [
                { text: "Thời gian giao hàng", icon: "fa-shipping-fast" },
                { text: "Chính sách đổi trả", icon: "fa-exchange-alt" },
                { text: "Có mã giảm giá không?", icon: "fa-percent" }
            ],
            checkout: [
                { text: "Phương thức thanh toán khác", icon: "fa-credit-card" },
                { text: "Phí vận chuyển", icon: "fa-truck" },
                { text: "Thời gian nhận hàng", icon: "fa-clock" }
            ],
            disease: [
                { text: "Cách phòng bệnh cho cá", icon: "fa-shield-virus" },
                { text: "Thuốc điều trị phổ biến", icon: "fa-prescription-bottle-alt" },
                { text: "Dấu hiệu cá khỏe mạnh", icon: "fa-heartbeat" }
            ],
            feeding: [
                { text: "Tần suất cho ăn phù hợp", icon: "fa-clock" },
                { text: "Thức ăn tự nhiên vs thức ăn chế biến", icon: "fa-leaf" },
                { text: "Cách cho ăn đúng cách", icon: "fa-hand-holding" }
            ]
        };

        function setExample(text) {
            document.getElementById('userInput').value = text;
        }

        function addMessage(type, content) {
            const chatContainer = document.getElementById('chatContainer');
            const typingIndicator = document.getElementById('typingIndicator');
            const messageDiv = document.createElement('div');
            messageDiv.className = 'message ' + type + '-message';
            messageDiv.innerHTML = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>').replace(/\n/g, '<br>');
            chatContainer.insertBefore(messageDiv, typingIndicator);
            chatContainer.scrollTop = chatContainer.scrollHeight;
            
            // Lưu tin nhắn vào lịch sử
            if (type === 'user') {
                currentContext.lastMessage = content;
                currentContext.conversationHistory.push({type: 'user', content: content});
            } else if (type === 'agent') {
                currentContext.conversationHistory.push({type: 'agent', content: content});
                // Nếu là tin nhắn từ AI, phân tích để hiển thị nút tương tác
                analyzeResponseAndShowButtons(content);
            }
        }

        function showLoading(show) {
            document.getElementById('normalText').style.display = show ? 'none' : 'inline';
            document.getElementById('loadingText').style.display = show ? 'inline' : 'none';
            document.getElementById('sendBtn').disabled = show;
            document.getElementById('typingIndicator').style.display = show ? 'block' : 'none';
        }

        // Phân tích phản hồi và hiển thị nút tương tác phù hợp
        function analyzeResponseAndShowButtons(response) {
            const buttonContainer = document.getElementById('suggestionButtonsContainer');
            buttonContainer.innerHTML = ''; // Xóa các nút cũ
            
            // Mảng để lưu các nút sẽ hiển thị
            const buttonsToShow = [];
            let contextType = 'general';
            
            // Phân tích ngữ cảnh từ nội dung phản hồi
            if (response.toLowerCase().includes('bệnh') || response.toLowerCase().includes('triệu chứng') || 
                response.toLowerCase().includes('điều trị') || response.toLowerCase().includes('chữa')) {
                contextType = 'disease';
                currentContext.lastIntent = 'disease';
            } else if (response.toLowerCase().includes('cho ăn') || response.toLowerCase().includes('thức ăn') || 
                      response.toLowerCase().includes('dinh dưỡng')) {
                contextType = 'feeding';
                currentContext.lastIntent = 'feeding';
            }
            
            // Kiểm tra xem có đang trong quá trình thanh toán COD không
            if (response.includes('Đặt hàng COD') || response.includes('Xác nhận đơn hàng') || 
                response.includes('thanh toán khi nhận hàng') || response.includes('COD')) {
                currentContext.pendingCodCheckout = true;
                currentContext.lastIntent = 'checkout';
                contextType = 'checkout';
                
                // Thêm nút xác nhận đặt COD
                buttonsToShow.push({
                    text: 'Xác nhận COD',
                    icon: 'fa-check-circle',
                    class: 'confirm',
                    action: 'xác nhận COD'
                });
                
                // Thêm nút hủy
                buttonsToShow.push({
                    text: 'Hủy đơn hàng',
                    icon: 'fa-times-circle',
                    class: 'cancel',
                    action: 'hủy đơn hàng'
                });
                
                // Thêm nút câu hỏi về phí vận chuyển
                buttonsToShow.push({
                    text: 'Phí vận chuyển là bao nhiêu?',
                    icon: 'fa-truck',
                    class: 'question',
                    action: 'phí vận chuyển là bao nhiêu?'
                });
            }
            
            // Kiểm tra xem đơn hàng đã được xác nhận chưa
            else if (response.includes('Đặt hàng thành công') || response.includes('đơn hàng đã được xác nhận')) {
                currentContext.lastIntent = 'order_confirmed';
                contextType = 'order_confirmed';
                
                // Thêm nút hủy đơn hàng
                buttonsToShow.push({
                    text: 'Hủy đơn hàng',
                    icon: 'fa-times-circle',
                    class: 'cancel',
                    action: 'hủy đơn hàng'
                });
                
                // Thêm nút theo dõi đơn hàng
                buttonsToShow.push({
                    text: 'Theo dõi đơn hàng',
                    icon: 'fa-truck',
                    class: 'question',
                    action: 'theo dõi đơn hàng'
                });
                
                // Thêm nút tiếp tục mua sắm
                buttonsToShow.push({
                    text: 'Tiếp tục mua sắm',
                    icon: 'fa-shopping-bag',
                    class: '',
                    action: 'tiếp tục mua sắm'
                });
            }
            
            // Kiểm tra xem có đang hiển thị giỏ hàng không
            else if (response.includes('Giỏ hàng của bạn') || response.includes('Tổng giỏ hàng')) {
                currentContext.hasCart = true;
                currentContext.lastIntent = 'cart';
                contextType = 'cart';
                
                // Thêm nút thanh toán
                buttonsToShow.push({
                    text: 'Thanh toán',
                    icon: 'fa-credit-card',
                    class: 'checkout',
                    action: 'thanh toán'
                });
                
                // Thêm nút thanh toán COD
                buttonsToShow.push({
                    text: 'Thanh toán COD',
                    icon: 'fa-money-bill',
                    class: 'checkout',
                    action: 'thanh toán COD'
                });
                
                // Thêm nút tiếp tục mua sắm
                buttonsToShow.push({
                    text: 'Tiếp tục mua sắm',
                    icon: 'fa-shopping-bag',
                    class: '',
                    action: 'tiếp tục mua sắm'
                });
                
                // Nếu giỏ hàng trống
                if (response.includes('trống rỗng') || response.includes('chưa có sản phẩm')) {
                    // Thêm nút xem sản phẩm nổi bật
                    buttonsToShow.push({
                        text: 'Xem sản phẩm nổi bật',
                        icon: 'fa-star',
                        class: 'product',
                        action: 'sản phẩm nổi bật'
                    });
                    
                    // Thêm nút gợi ý sản phẩm
                    buttonsToShow.push({
                        text: 'Gợi ý sản phẩm cho tôi',
                        icon: 'fa-lightbulb',
                        class: 'product',
                        action: 'gợi ý sản phẩm cho người mới nuôi cá'
                    });
                }
            }
            
            // Kiểm tra nếu đơn hàng đã bị hủy
            else if (response.includes('đơn hàng đã được hủy') || response.includes('Đơn hàng đã được hủy')) {
                currentContext.lastIntent = 'order_cancelled';
                contextType = 'order_cancelled';
                
                // Thêm nút tiếp tục mua sắm
                buttonsToShow.push({
                    text: 'Tiếp tục mua sắm',
                    icon: 'fa-shopping-bag',
                    class: '',
                    action: 'tiếp tục mua sắm'
                });
                
                // Thêm nút xem giỏ hàng
                buttonsToShow.push({
                    text: 'Xem giỏ hàng',
                    icon: 'fa-shopping-cart',
                    class: 'view-cart',
                    action: 'xem giỏ hàng'
                });
                
                // Thêm nút xem sản phẩm nổi bật
                buttonsToShow.push({
                    text: 'Xem sản phẩm nổi bật',
                    icon: 'fa-star',
                    class: 'product',
                    action: 'sản phẩm nổi bật'
                });
            }
            
            // Kiểm tra xem có đang hiển thị thông tin sản phẩm không
            else {
                const productRegex = /Cá\s+\w+|Thức ăn\s+\w+|Thuốc\s+\w+|Phụ kiện\s+\w+/i;
                const priceRegex = /Giá:?\s+[\d,.]+\s*VNĐ/i;
                
                if (priceRegex.test(response)) {
                    const productMatch = response.match(productRegex);
                    if (productMatch) {
                        currentContext.viewingProduct = true;
                        currentContext.productName = productMatch[0];
                        currentContext.lastIntent = 'product';
                        contextType = 'product';
                        
                        // Thêm sản phẩm vào danh sách đã xem
                        if (!currentContext.lastProductsViewed.includes(currentContext.productName)) {
                            currentContext.lastProductsViewed.push(currentContext.productName);
                            if (currentContext.lastProductsViewed.length > 5) {
                                currentContext.lastProductsViewed.shift();
                            }
                        }
                        
                        // Thêm nút mua sản phẩm
                        buttonsToShow.push({
                            text: 'Mua ' + currentContext.productName,
                            icon: 'fa-cart-plus',
                            class: '',
                            action: 'mua ' + currentContext.productName
                        });
                        
                        // Thêm nút xem giỏ hàng
                        buttonsToShow.push({
                            text: 'Xem giỏ hàng',
                            icon: 'fa-shopping-cart',
                            class: 'view-cart',
                            action: 'xem giỏ hàng'
                        });
                        
                        // Thêm nút so sánh với sản phẩm khác
                        buttonsToShow.push({
                            text: 'So sánh với sản phẩm khác',
                            icon: 'fa-balance-scale',
                            class: 'question',
                            action: 'so sánh ' + currentContext.productName + ' với sản phẩm khác'
                        });
                        
                        // Thêm nút cách chăm sóc
                        buttonsToShow.push({
                            text: 'Cách chăm sóc',
                            icon: 'fa-hand-holding-heart',
                            class: 'question',
                            action: 'cách chăm sóc ' + currentContext.productName
                        });
                    }
                }
            }
            
            // Nếu không có nút nào được thêm từ phân tích trên, thêm các nút dựa trên ngữ cảnh
            if (buttonsToShow.length === 0) {
                // Thêm nút xem giỏ hàng
                buttonsToShow.push({
                    text: 'Xem giỏ hàng',
                    icon: 'fa-shopping-cart',
                    class: 'view-cart',
                    action: 'xem giỏ hàng'
                });
                
                // Thêm các câu hỏi theo ngữ cảnh
                const contextQuestions = contextualQuestions[contextType] || contextualQuestions.general;
                contextQuestions.forEach(question => {
                    buttonsToShow.push({
                        text: question.text,
                        icon: question.icon,
                        class: 'question',
                        action: question.text
                    });
                });
                
                // Nếu đã xem sản phẩm trước đó, thêm nút để hỏi về sản phẩm đó
                if (currentContext.lastProductsViewed.length > 0) {
                    const lastProduct = currentContext.lastProductsViewed[currentContext.lastProductsViewed.length - 1];
                    buttonsToShow.push({
                        text: 'Mua ' + lastProduct,
                        icon: 'fa-cart-plus',
                        class: 'product',
                        action: 'mua ' + lastProduct
                    });
                }
            }
            
            // Tạo và hiển thị các nút
            if (buttonsToShow.length > 0) {
                // Giới hạn số lượng nút hiển thị để tránh quá tải giao diện
                const maxButtons = 5;
                const limitedButtons = buttonsToShow.slice(0, maxButtons);
                
                limitedButtons.forEach(button => {
                    // Sử dụng Bootstrap button
                    const btn = document.createElement('button');
                    btn.type = 'button';
                    btn.className = 'btn m-1 btn-primary';
                    
                    // Tạo icon
                    const icon = document.createElement('i');
                    icon.className = 'fas ' + button.icon;
                    btn.appendChild(icon);
                    
                    // Thêm khoảng trắng giữa icon và text
                    btn.appendChild(document.createTextNode(' '));
                    
                    // Tạo text node để đảm bảo hiển thị chữ
                    const textNode = document.createTextNode(button.text);
                    btn.appendChild(textNode);
                    
                    // Thêm sự kiện click
                    btn.onclick = function() {
                        handleButtonClick(button.action);
                    };
                    
                    // Debug: In ra console để kiểm tra
                    console.log("Tạo nút:", button.text);
                    
                    buttonContainer.appendChild(btn);
                });
                
                document.getElementById('contextButtons').style.display = 'block';
            } else {
                document.getElementById('contextButtons').style.display = 'none';
            }
            
            // Debug: In ra console để kiểm tra
            console.log("Hiển thị nút:", buttonsToShow);
        }
        
        // Xử lý khi người dùng nhấn nút tương tác
        function handleButtonClick(action) {
            document.getElementById('userInput').value = action;
            document.getElementById('agentForm').dispatchEvent(new Event('submit'));
        }

        async function sendMessage(event) {
    event.preventDefault();
    const userInput = document.getElementById('userInput').value.trim();
    if (!userInput) return;

    addMessage('user', userInput);
    document.getElementById('userInput').value = '';
    showLoading(true);

    try {
        const response = await fetch('ai-agent', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'question=' + encodeURIComponent(userInput)
        });

        if (!response.ok) {
            throw new Error('Lỗi server: ' + response.status);
        }

        const responseText = await response.text();
        
        // Kiểm tra xem response có phải là JSON không
        let finalResponse;
        try {
            const jsonData = JSON.parse(responseText);
            // Nếu là JSON và có field response, lấy nội dung từ field đó
            if (jsonData.success && jsonData.response) {
                finalResponse = jsonData.response;
            } else if (jsonData.response) {
                finalResponse = jsonData.response;
            } else if (jsonData.message) {
                finalResponse = jsonData.message;
            } else {
                // Nếu JSON không có field mong muốn, hiển thị toàn bộ
                finalResponse = responseText;
            }
        } catch (parseError) {
            // Nếu không phải JSON, hiển thị nguyên văn
            finalResponse = responseText;
        }

        addMessage('agent', finalResponse);

    } catch (error) {
        console.error('SendMessage Error:', error);
        addMessage('agent', '❌ Đã xảy ra lỗi kết nối. Vui lòng thử lại.');
    } finally {
        showLoading(false);
    }
}

        
        // Thiết lập SSE để nhận thông báo thanh toán thành công
        function setupPaymentNotifications() {
            const userId = <%= session.getAttribute("userId") != null ? session.getAttribute("userId") : "null" %>;
            if (!userId || userId === "null") return;
            
            // Tạo kết nối SSE
            const eventSource = new EventSource("payment-notifications?userId=" + userId);
            
            // Xử lý khi nhận được thông báo
            eventSource.addEventListener("payment_success", function(event) {
                const data = JSON.parse(event.data);
                
                // Hiển thị thông báo thanh toán thành công
                const aiResponseDiv = document.getElementById('ai-response');
                if (aiResponseDiv) {
                    // Tạo phần tử thông báo
                    const notificationDiv = document.createElement('div');
                    notificationDiv.className = 'ai-message payment-success-notification';
                    notificationDiv.innerHTML = 
                        '<div class="message-content">' +
                        '<h4>✅ Thông báo thanh toán thành công</h4>' +
                        '<p>🛍️ ' + data.message + '</p>' +
                        '<p>Bạn có thể kiểm tra chi tiết đơn hàng tại <a href="/DuAnBanCaCanh/orders.jsp">Đơn hàng của tôi</a></p>' +
                        '<p>Cảm ơn bạn đã mua hàng tại cửa hàng của chúng tôi! 😊</p>' +
                        '</div>';
                    
                    // Thêm thông báo vào đầu danh sách tin nhắn
                    aiResponseDiv.insertBefore(notificationDiv, aiResponseDiv.firstChild);
                    
                    // Đánh dấu thông báo đã đọc
                    fetch('mark-notification-read?id=' + data.notificationId, {method: 'POST'});
                    
                    // Phát âm thanh thông báo (tùy chọn)
                    const audio = new Audio('/DuAnBanCaCanh/assets/sounds/notification.mp3');
                    audio.play().catch(e => console.log('Không thể phát âm thanh thông báo'));
                }
            });
            
            // Xử lý lỗi kết nối
            eventSource.onerror = function(error) {
                console.error("SSE Error:", error);
                eventSource.close();
                // Thử kết nối lại sau 5 giây
                setTimeout(setupPaymentNotifications, 5000);
            };
        }
        
        // Khởi tạo kết nối khi trang đã tải xong
        document.addEventListener('DOMContentLoaded', function() {
            setupPaymentNotifications();
        });
    </script>
</body>
</html>