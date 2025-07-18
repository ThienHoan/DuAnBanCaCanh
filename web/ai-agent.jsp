<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- PHIÊN BẢN JSP ĐÃ ĐƯỢC DỌN DẸP --%>
<%-- Toàn bộ logic Java backend đã được xóa bỏ và chuyển sang AIAgentServlet. --%>
<%-- File JSP này giờ đây chỉ còn nhiệm vụ hiển thị giao diện người dùng. --%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>🤖 AI Agent - DuAnCaCanh</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        /* Toàn bộ CSS của bạn giữ nguyên, không cần thay đổi. */
        /* ... (Phần CSS dài của bạn ở đây) ... */
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .agent-container { background: rgba(255, 255, 255, 0.95); border-radius: 15px; box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1); backdrop-filter: blur(10px); margin-top: 50px; padding: 30px; }
        .chat-container { max-height: 500px; overflow-y: auto; border: 2px solid #e9ecef; border-radius: 15px; padding: 20px; background: #ffffff; }
        .message { margin: 15px 0; padding: 12px 18px; border-radius: 18px; max-width: 80%; position: relative; word-wrap: break-word; animation: fadeIn 0.3s ease-in; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
        .user-message { background: linear-gradient(135deg, #007bff, #0056b3); color: white; margin-left: auto; border-bottom-right-radius: 5px; box-shadow: 0 2px 5px rgba(0,123,255,0.3); }
        .agent-message { background: linear-gradient(135deg, #20c997, #17a2b8); color: white; margin-right: auto; border-bottom-left-radius: 5px; box-shadow: 0 3px 10px rgba(32,201,151,0.3); line-height: 1.6; }
        .system-message { background: linear-gradient(135deg, #6c757d, #495057); color: white; text-align: center; font-style: italic; margin: 20px auto; max-width: 60%; border-radius: 20px; }
        .typing-indicator { background: #e9ecef; color: #6c757d; margin-right: auto; border-radius: 18px; padding: 12px 18px; max-width: 100px; display: none; }
        .typing-dots span { display: inline-block; width: 8px; height: 8px; border-radius: 50%; background-color: #6c757d; margin: 0 2px; animation: typing 1.4s infinite ease-in-out; }
        @keyframes typing { 0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; } 40% { transform: scale(1); opacity: 1; } }
        .btn-agent { background: linear-gradient(45deg, #007bff, #0056b3); border: none; padding: 12px 30px; border-radius: 25px; font-weight: bold; transition: all 0.3s ease; }
        .btn-agent:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,123,255,0.4); }
        .smart-suggestions { background: linear-gradient(135deg, #f8f9fa, #e3f2fd); border-radius: 12px; padding: 20px; margin: 15px 0; border-left: 4px solid #2196f3; box-shadow: 0 2px 8px rgba(33, 150, 243, 0.1); }
        .suggestion-list { display: flex; flex-wrap: wrap; gap: 8px; margin: 10px 0; }
        .suggestion-item { background: rgba(0,123,255,0.1); color: #007bff; padding: 8px 12px; border-radius: 15px; font-size: 0.9em; border: 1px solid rgba(0,123,255,0.2); cursor: pointer; transition: all 0.2s ease; }
        .suggestion-item:hover { background: rgba(0,123,255,0.2); transform: translateY(-1px); }
        .agent-message * { color: inherit; }
        .agent-message .enhanced-response *, .agent-message .product-card *, .agent-message .quick-answer * { color: #333 !important; }
        .enhanced-response { background: rgba(0,123,255,0.05); border-radius: 10px; padding: 15px; margin: 10px 0; border: 1px solid rgba(0,123,255,0.1); }
        .product-card { background: rgba(255,255,255,0.95); border-radius: 12px; padding: 15px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); border: 1px solid rgba(255,255,255,0.2); transition: all 0.3s ease; }
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
    function setExample(text) {
        document.getElementById('userInput').value = text;
    }

    function addMessage(type, content) {
        // ... (hàm này giữ nguyên)
        const chatContainer = document.getElementById('chatContainer');
        const typingIndicator = document.getElementById('typingIndicator');
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message ' + type + '-message';
        messageDiv.innerHTML = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>').replace(/\n/g, '<br>');
        chatContainer.insertBefore(messageDiv, typingIndicator);
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }

    function showLoading(show) {
        // ... (hàm này giữ nguyên)
        document.getElementById('normalText').style.display = show ? 'none' : 'inline';
        document.getElementById('loadingText').style.display = show ? 'inline' : 'none';
        document.getElementById('sendBtn').disabled = show;
        document.getElementById('typingIndicator').style.display = show ? 'block' : 'none';
    }

    async function sendMessage(event) {
        event.preventDefault();
        const userInput = document.getElementById('userInput').value.trim();
        if (!userInput) return;

        addMessage('user', userInput);
        document.getElementById('userInput').value = '';
        showLoading(true);

        try {
            // **QUAY TRỞ LẠI SỬ DỤNG POST REQUEST VỚI JSON**
            const response = await fetch('ai-agent', {
                method: 'POST', // 1. Sử dụng lại phương thức POST
                headers: {
                    // 2. Báo cho server biết chúng ta đang gửi JSON
                    'Content-Type': 'application/json; charset=UTF-8',
                },
                // 3. Đóng gói dữ liệu thành một chuỗi JSON
                body: JSON.stringify({ request: userInput })
            });

            if (!response.ok) {
                throw new Error('Lỗi server: ' + response.status);
            }

            const result = await response.json();

            if (result.success) {
                addMessage('agent', result.response);
            } else {
                addMessage('agent', 'Lỗi: ' + result.error);
            }

        } catch (error) {
            console.error('SendMessage Error:', error);
            addMessage('agent', '❌ Đã xảy ra lỗi kết nối. Vui lòng thử lại.');
        } finally {
            showLoading(false);
        }
    }
</script>


</body>
</html>
