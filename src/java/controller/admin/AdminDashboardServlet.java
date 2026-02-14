package controller.admin;

import dao.impl.ProductDAO;
import dao.impl.OrderDAO;
import dao.impl.UserDAO;
import model.entity.User;
import utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin-dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private UserDAO userDAO = new UserDAO();
      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Filter đã kiểm tra authentication và authorization
        // Không cần kiểm tra lại ở đây
        
        try {
            // Lấy dữ liệu dashboard cho admin
            Map<String, Object> dashboardData = getDashboardData();
              // Đưa dữ liệu vào request
            request.setAttribute("dashboardData", dashboardData);
            request.setAttribute("totalProducts", dashboardData.get("totalProducts"));
            request.setAttribute("totalOrders", dashboardData.get("totalOrders"));
            request.setAttribute("totalUsers", dashboardData.get("totalUsers"));
            request.setAttribute("totalRevenue", dashboardData.get("totalRevenue"));
            request.setAttribute("todayRevenue", dashboardData.get("todayRevenue"));
            request.setAttribute("todayOrders", dashboardData.get("todayOrders"));
            request.setAttribute("pendingOrders", dashboardData.get("pendingOrders"));
            request.setAttribute("revenueGrowth", dashboardData.get("revenueGrowth"));
            request.setAttribute("monthlyRevenue", dashboardData.get("monthlyRevenue"));
            request.setAttribute("recentOrders", dashboardData.get("recentOrders"));
            request.setAttribute("topProducts", dashboardData.get("topProducts"));
            request.setAttribute("orderStatistics", dashboardData.get("orderStatistics"));
            
            // Forward đến trang dashboard
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
      private Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // Thống kê tổng quan
            data.put("totalProducts", productDAO.getAllProducts());
            data.put("totalOrders", orderDAO.getAllOrders());
            data.put("totalUsers", userDAO.getAllUsers());
            data.put("totalRevenue", orderDAO.getTotalRevenue());
            
            // Thống kê thêm
            data.put("todayRevenue", orderDAO.getTodayRevenue());
            data.put("todayOrders", orderDAO.getTodayOrders());
            data.put("pendingOrders", orderDAO.getPendingOrders());
            data.put("revenueGrowth", orderDAO.getRevenueGrowth());
            
            // Doanh thu theo tháng
            data.put("monthlyRevenue", orderDAO.getMonthlyRevenue());
            
            // Đơn hàng gần đây
            data.put("recentOrders", orderDAO.getRecentOrders(10));
            
            // Top sản phẩm bán chạy
            
            
            // Thống kê đơn hàng theo trạng thái
            data.put("orderStatistics", orderDAO.getOrderStatistics());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
}