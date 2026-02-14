package controller.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestReviewServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>TestReviewServlet - GET Request</h1>");
        out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
        out.println("<p>Context Path: " + request.getContextPath() + "</p>");
        out.println("<p>Servlet Path: " + request.getServletPath() + "</p>");
        out.println("<p>Path Info: " + request.getPathInfo() + "</p>");
        out.println("<p>Query String: " + request.getQueryString() + "</p>");
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>TestReviewServlet - POST Request</h1>");
        out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
        out.println("<p>Context Path: " + request.getContextPath() + "</p>");
        out.println("<p>Action: " + request.getParameter("action") + "</p>");
        out.println("<p>Product ID: " + request.getParameter("productId") + "</p>");
        out.println("<p>Rating: " + request.getParameter("rating") + "</p>");
        out.println("<p>Comment: " + request.getParameter("comment") + "</p>");
        out.println("<p><a href='" + request.getContextPath() + "/test-review-debug.html'>Back to test page</a></p>");
        out.println("</body></html>");
    }
} 