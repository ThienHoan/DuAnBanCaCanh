<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .error { background: #ffebee; border: 1px solid #f44336; padding: 20px; border-radius: 4px; }
        .stack-trace { background: #f5f5f5; border: 1px solid #ddd; padding: 15px; margin-top: 20px; overflow-x: auto; }
        pre { margin: 0; font-size: 12px; }
    </style>
</head>
<body>
    <h1>Error Occurred</h1>
    
    <div class="error">
        <h2>Exception Details:</h2>
        <p><strong>Exception:</strong> <%= exception != null ? exception.getClass().getName() : "No exception" %></p>
        <p><strong>Message:</strong> <%= exception != null ? exception.getMessage() : "No message" %></p>
        <p><strong>Request URI:</strong> <%= request.getAttribute("jakarta.servlet.error.request_uri") %></p>
        <p><strong>Status Code:</strong> <%= request.getAttribute("jakarta.servlet.error.status_code") %></p>
        <p><strong>Servlet Name:</strong> <%= request.getAttribute("jakarta.servlet.error.servlet_name") %></p>
    </div>
    
    <% if (exception != null) { %>
    <div class="stack-trace">
        <h3>Stack Trace:</h3>
        <pre>
        <% 
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            exception.printStackTrace(pw);
            out.print(sw.toString());
        %>
        </pre>
    </div>
    <% } %>
    
    <p><a href="javascript:history.back()">Go Back</a> | <a href="${pageContext.request.contextPath}/">Home</a></p>
</body>
</html>
