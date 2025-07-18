<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ai.agents.AquariumAgent"%>
<%@page import="java.util.*"%>

<%
    // Test tích hợp module4 AquariumAgent
    String testMessage = request.getParameter("test_message");
    String response = "";
    boolean agentReady = false;
    String agentInfo = "";
    String errorMessage = "";
    
    try {
        // Khởi tạo AquariumAgent
        AquariumAgent agent = new AquariumAgent();
        agentReady = agent.isReady();
        agentInfo = agent.getAgentInfo();
        
        // Test với message nếu có
        if (testMessage != null && !testMessage.trim().isEmpty()) {
            response = agent.processQuestion(testMessage);
        }
        
    } catch (Exception e) {
        errorMessage = e.getMessage();
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Module4 AquariumAgent Test</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .container {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        
        .header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #f0f0f0;
        }
        
        .status {
            display: flex;
            justify-content: space-between;
            margin-bottom: 30px;
            padding: 15px;
            border-radius: 10px;
        }
        
        .status.ready {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        
        .status.error {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        
        .test-form {
            margin: 30px 0;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        
        input[type="text"], textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 14px;
        }
        
        button {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }
        
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .response {
            margin-top: 30px;
            padding: 20px;
            background: #e7f3ff;
            border: 1px solid #b3d9ff;
            border-radius: 10px;
        }
        
        .agent-info {
            margin-top: 20px;
            padding: 15px;
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 8px;
            white-space: pre-wrap;
        }
        
        .quick-tests {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 10px;
            margin: 20px 0;
        }
        
        .quick-test-btn {
            padding: 8px 15px;
            background: #17a2b8;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        
        .quick-test-btn:hover {
            background: #138496;
        }
        
        .error {
            color: #dc3545;
            background: #f8d7da;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🤖 Module4 AquariumAgent Integration Test</h1>
            <p>Test tích hợp thành công module4 framework vào dự án bán cá cảnh</p>
        </div>
        
        <!-- Status Display -->
        <div class="status <%= agentReady ? "ready" : "error" %>">
            <div>
                <strong>Agent Status:</strong> 
                <%= agentReady ? "✅ Ready" : "❌ Not Ready" %>
            </div>
            <div>
                <strong>Framework:</strong> Module4 Advanced AI
            </div>
        </div>
        
        <% if (!errorMessage.isEmpty()) { %>
            <div class="error">
                <strong>Error:</strong> <%= errorMessage %>
            </div>
        <% } %>
        
        <!-- Test Form -->
        <div class="test-form">
            <h3>🧪 Test AquariumAgent</h3>
            <form method="post">
                <div class="form-group">
                    <label for="test_message">Nhập câu hỏi về cá cảnh:</label>
                    <textarea name="test_message" id="test_message" rows="3" 
                              placeholder="Ví dụ: Tôi muốn nuôi cá cảnh, bạn có thể tư vấn cho tôi không?"
                    ><%= testMessage != null ? testMessage : "" %></textarea>
                </div>
                <button type="submit">🚀 Test Agent</button>
            </form>
            
            <!-- Quick Test Buttons -->
            <div class="quick-tests">
                <button class="quick-test-btn" onclick="quickTest('Xin chào, tôi muốn tư vấn về cá cảnh')">
                    Chào hỏi
                </button>
                <button class="quick-test-btn" onclick="quickTest('Tôi là người mới, nên chọn loại cá nào dễ nuôi?')">
                    Chọn cá cho người mới
                </button>
                <button class="quick-test-btn" onclick="quickTest('Hướng dẫn thiết lập bể cá 50L')">
                    Thiết lập bể cá
                </button>
                <button class="quick-test-btn" onclick="quickTest('Cá của tôi bị bệnh đốm trắng, làm sao?')">
                    Chữa bệnh cá
                </button>
                <button class="quick-test-btn" onclick="quickTest('Cách cho cá ăn đúng cách')">
                    Hướng dẫn cho ăn
                </button>
                <button class="quick-test-btn" onclick="quickTest('Kiểm tra chất lượng nước bể cá')">
                    Chất lượng nước
                </button>
            </div>
        </div>
        
        <!-- Response Display -->
        <% if (!response.isEmpty()) { %>
            <div class="response">
                <h3>🤖 Phản hồi từ AquariumAgent (Module4):</h3>
                <div style="white-space: pre-wrap; line-height: 1.6;">
<%= response %>
                </div>
            </div>
        <% } %>
        
        <!-- Agent Info -->
        <% if (!agentInfo.isEmpty()) { %>
            <div class="agent-info">
                <h3>ℹ️ Thông tin Agent:</h3>
                <%= agentInfo %>
            </div>
        <% } %>
        
        <!-- Integration Summary -->
        <div style="margin-top: 30px; padding: 20px; background: #f1f3f4; border-radius: 10px;">
            <h3>📋 Tóm tắt tích hợp Module4:</h3>
            <ul style="line-height: 1.8;">
                <li><strong>✅ Package ai.agents.AquariumAgent:</strong> Agent chính sử dụng module4</li>
                <li><strong>✅ Package ai.tools.AquariumTools:</strong> 5 tools chuyên về cá cảnh</li>
                <li><strong>✅ Framework:</strong> Sử dụng Agents.createAgent() từ module4</li>
                <li><strong>✅ Goals:</strong> 3 mục tiêu chính (tư vấn, customer service, giáo dục)</li>
                <li><strong>✅ Language Support:</strong> Tiếng Việt tự nhiên</li>
                <li><strong>✅ Integration:</strong> Ready cho production JSP/Servlet</li>
            </ul>
        </div>
    </div>
    
    <script>
        function quickTest(message) {
            document.getElementById('test_message').value = message;
            document.querySelector('form').submit();
        }
    </script>
</body>
</html>
