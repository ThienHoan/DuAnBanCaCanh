package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.AddressDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Address;
import model.Order;
import model.Product;
import model.User;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;
    private AddressDAO addressDAO;
    private ProductDAO productDAO;
    private static final int ORDERS_PER_PAGE = 15;
    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        addressDAO = new AddressDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listOrders(request, response, user);
                break;
            case "detail":
                viewOrderDetail(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        switch (action) {
            case "cancel":
                cancelOrder(request, response, user);
                break;
            case "confirmReceived":
                confirmReceived(request, response, user);
                break;
            case "confirmShipped":
                confirmShipped(request, response, user);
                break;
            case "confirm":
                confirmOrder(request, response, user); // Handles admin order confirmation
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            String keyword = request.getParameter("keyword");
            String status = request.getParameter("status");
            if (status == null) status = "all";

            List<Order> allOrders = orderDAO.searchOrders(keyword, status, user.getUserId(), user.getRole());

            int totalOrders = allOrders.size();
            int totalPages = (int) Math.ceil((double) totalOrders / ORDERS_PER_PAGE);
            if (totalPages == 0) totalPages = 1;

            if (page > totalPages) page = totalPages;

            int startIndex = (page - 1) * ORDERS_PER_PAGE;
            int endIndex = Math.min(startIndex + ORDERS_PER_PAGE, totalOrders);

            List<Order> ordersForPage = allOrders.subList(startIndex, endIndex);

            request.setAttribute("orders", ordersForPage);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("ordersPerPage", ORDERS_PER_PAGE);
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);

            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        }

    private void viewOrderDetail(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderDAO.getAllOrders().stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
        if (order == null || ("customer".equals(user.getRole()) && order.getUserId() != user.getUserId())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found or access denied");
            return;
        }
        // Fetch additional data for order details
        List<Address> addresses = addressDAO.getAddressesByUserId(user.getUserId());
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("order", order);
        request.setAttribute("ADDRESSES", addresses);
        request.setAttribute("PRODUCTS", products);
        request.getRequestDispatcher("/orderDetail.jsp").forward(request, response);
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!"customer".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only customers can cancel orders");
            return;
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        boolean success = orderDAO.cancelOrder(orderId);
        if (success) {
            response.sendRedirect("orders?action=list&message=Order cancelled successfully");
        } else {
            response.sendRedirect("orders?action=list&error=Failed to cancel order");
        }
    }

    private void confirmReceived(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!"customer".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only customers can confirm receipt");
            return;
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        boolean success = orderDAO.confirmReceived(orderId); // Fixed: Changed from cancelOrder to confirmReceived
        if (success) {
            response.sendRedirect("orders?action=list&message=Order confirmed received successfully");
        } else {
            response.sendRedirect("orders?action=list&error=Failed to confirm receipt");
        }
    }

    private void confirmShipped(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only admins can confirm shipment");
            return;
        }
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        boolean success = orderDAO.confirmShipped(orderId);
        if (success) {
            response.sendRedirect("orders?action=list&message=Order confirmed shipped successfully");
        } else {
            response.sendRedirect("orders?action=list&error=Failed to confirm shipment");
        }
    }
    private void confirmOrder(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
    if (!"admin".equals(user.getRole())) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only admins can confirm orders");
        return;
    }
    int orderId = Integer.parseInt(request.getParameter("orderId"));
    boolean success = orderDAO.confirmOrder(orderId);
    if (success) {
        response.sendRedirect("orders?action=list&message=Order confirmed successfully");
    } else {
        response.sendRedirect("orders?action=list&error=Failed to confirm order");
    }
}
}