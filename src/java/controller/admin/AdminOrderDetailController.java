package controller.admin;

import dao.impl.InventoryLogDAOImpl;
import dao.impl.OrderDAOImpl;
import dao.impl.ProductDAO;
import dao.impl.ProductImageDAO;
import dao.interfaces.InventoryLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.InventoryLog;
import model.entity.Product;
import model.entity.User;
import model.entity.pOrder.Order;
import model.entity.pOrder.OrderItem;

/**
 * Controller for admin order detail page
 */
@WebServlet("/admin/order-detail")
public class AdminOrderDetailController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminOrderDetailController.class.getName());
    private OrderDAOImpl orderDAO;
    private ProductDAO productDAO;
    private ProductImageDAO productImageDAO;
    private InventoryLogDAO inventoryLogDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        productDAO = new ProductDAO();
        productImageDAO = new ProductImageDAO();
        inventoryLogDAO = new InventoryLogDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is admin
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này");
            return;
        }

        try {
            // Get order ID from request
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            
            // Get order details
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                session.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn hàng");
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }
            
            // Get order items with details
            List<OrderItem> orderItems = orderDAO.getOrderItemsById(orderId);
            
            // Get product images and inventory levels
            Map<Integer, String> productImages = new HashMap<>();
            Map<Integer, Integer> productInventory = new HashMap<>();
            
            for (OrderItem item : orderItems) {
                // Get product image
                String mainImage = productImageDAO.getMainImageByProductId1(item.getProductId());
                productImages.put(item.getProductId(), mainImage);
                
                // Get current inventory level
                Product product = productDAO.getProductById(item.getProductId());
                if (product != null) {
                    productInventory.put(item.getProductId(), product.getQuantity());
                }
            }
            
            // Get inventory logs related to this order
            List<InventoryLog> inventoryLogs = inventoryLogDAO.getInventoryLogsByReferenceId(orderId, "order_admin_confirmed");
            
            // Set attributes for the view
            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);
            request.setAttribute("productImages", productImages);
            request.setAttribute("productInventory", productInventory);
            request.setAttribute("inventoryLogs", inventoryLogs);
            
            // Forward to the view
            request.getRequestDispatcher("/admin/order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Mã đơn hàng không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi xử lý chi tiết đơn hàng: " + e.getMessage(), e);
            session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
} 