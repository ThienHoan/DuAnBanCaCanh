package controller.admin;

import dao.impl.OrderDAOImpl;
import dao.impl.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.entity.Product;
import model.entity.User;
import model.entity.pOrder.Order;
import model.entity.pOrder.OrderItem;
import utils.InventoryLogUtil;
import java.util.List;

/**
* Controller for handling order status changes with inventory updates
*/
@WebServlet("/admin/order-status")
public class OrderStatusController extends HttpServlet {
   private static final Logger LOGGER = Logger.getLogger(OrderStatusController.class.getName());
   private OrderDAOImpl orderDAO;
   private ProductDAO productDAO;

   @Override
   public void init() throws ServletException {
       orderDAO = new OrderDAOImpl();
       productDAO = new ProductDAO();
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       HttpSession session = request.getSession();
       User user = (User) session.getAttribute("user");

       // Check if user is admin
       if (user == null || !"admin".equals(user.getRole())) {
           response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện thao tác này");
           return;
       }

       try {
           // Get parameters
           int orderId = Integer.parseInt(request.getParameter("orderId"));
           String status = request.getParameter("status");
           String returnUrl = request.getParameter("returnUrl");
           
           if (returnUrl == null || returnUrl.isEmpty()) {
               returnUrl = request.getContextPath() + "/admin/orders";
           }

           // Validate status
           if (status == null || status.isEmpty()) {
               session.setAttribute("ERROR_MESSAGE", "Trạng thái đơn hàng không hợp lệ");
               response.sendRedirect(returnUrl);
               return;
           }

           // Get the current order to check its status
           Order order = orderDAO.getOrderById(orderId);
           if (order == null) {
               session.setAttribute("ERROR_MESSAGE", "Không tìm thấy đơn hàng");
               response.sendRedirect(returnUrl);
               return;
           }

           // Update order status
           boolean updated = orderDAO.updateOrderStatus(orderId, status);
           
           if (updated) {
               // Chỉ cập nhật kho và ghi log khi chuyển sang shipping
               if ("shipping".equals(status)) {
                   List<OrderItem> orderItems = orderDAO.getOrderItemsById(orderId);
                   for (OrderItem item : orderItems) {
                       Product product = productDAO.getProductById(item.getProductId());
                       if (product != null) {
                           int currentQuantity = product.getQuantity();
                           int newQuantity = currentQuantity - item.getQuantity();
                           if (newQuantity < 0) newQuantity = 0;

                           // Update product quantity
                           String updateSql = "UPDATE Products SET quantity = ? WHERE product_id = ?";
                           try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                                java.sql.PreparedStatement ps = conn.prepareStatement(updateSql)) {
                               ps.setInt(1, newQuantity);
                               ps.setInt(2, product.getProductId());
                               ps.executeUpdate();

                               // Ghi log xuất kho
                               Product updatedProduct = new Product();
                               updatedProduct.setProductId(product.getProductId());
                               updatedProduct.setName(product.getName());
                               updatedProduct.setQuantity(newQuantity);

                               boolean logResult = InventoryLogUtil.logInventoryChange(
                                   updatedProduct,
                                   currentQuantity,
                                   "Bán cho khách hàng #" + orderId,
                                   orderId,
                                   "order_shipping"
                               );
                               System.out.println("[DEBUG] Ghi log inventory cho order #" + orderId + ", product #" + product.getProductId() + ": " + logResult);
                           } catch (Exception e) {
                               LOGGER.log(Level.SEVERE, "Error updating inventory: " + e.getMessage(), e);
                           }
                       } else {
                           System.out.println("[DEBUG] Không tìm thấy sản phẩm với ID: " + item.getProductId());
                       }
                   }
               }
               // Nếu trạng thái là "completed", vẫn giữ logic cũ (nếu cần)
               if ("completed".equals(status)) {
                   List<OrderItem> orderItems = orderDAO.getOrderItemsById(orderId);
                   for (OrderItem item : orderItems) {
                       Product product = productDAO.getProductById(item.getProductId());
                       if (product != null) {
                           int currentQuantity = product.getQuantity();
                           int newQuantity = currentQuantity - item.getQuantity();
                           if (newQuantity < 0) {
                               newQuantity = 0;
                               LOGGER.log(Level.WARNING, "Product ID " + product.getProductId() +
                                         " quantity would go below zero. Setting to 0 instead.");
                           }
                           String updateSql = "UPDATE Products SET quantity = ? WHERE product_id = ?";
                           try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                                java.sql.PreparedStatement ps = conn.prepareStatement(updateSql)) {
                               ps.setInt(1, newQuantity);
                               ps.setInt(2, product.getProductId());
                               ps.executeUpdate();
                               Product updatedProduct = new Product();
                               updatedProduct.setProductId(product.getProductId());
                               updatedProduct.setName(product.getName());
                               updatedProduct.setQuantity(newQuantity);
                               InventoryLogUtil.logInventoryChange(
                                   updatedProduct,
                                   currentQuantity,
                                   "Order completed: #" + orderId + " - Admin confirmed",
                                   orderId,
                                   "order_admin_confirmed"
                               );
                           } catch (Exception e) {
                               LOGGER.log(Level.SEVERE, "Error updating inventory: " + e.getMessage(), e);
                           }
                       }
                   }
               }
               
               // If status is "cancelled" and payment status is "paid", restore inventory
               if ("cancelled".equals(status) && "paid".equals(order.getPaymentStatus())) {
                   restoreInventory(orderId);
               }
               
               session.setAttribute("SUCCESS_MESSAGE", "Đã cập nhật trạng thái đơn hàng thành " + status);
           } else {
               session.setAttribute("ERROR_MESSAGE", "Không thể cập nhật trạng thái đơn hàng");
           }
           
           response.sendRedirect(returnUrl);
           
       } catch (NumberFormatException e) {
           LOGGER.log(Level.SEVERE, "Lỗi định dạng số: " + e.getMessage(), e);
           session.setAttribute("ERROR_MESSAGE", "Mã đơn hàng không hợp lệ");
           response.sendRedirect(request.getContextPath() + "/admin/orders");
       } catch (Exception e) {
           LOGGER.log(Level.SEVERE, "Lỗi xử lý cập nhật trạng thái đơn hàng: " + e.getMessage(), e);
           session.setAttribute("ERROR_MESSAGE", "Có lỗi xảy ra: " + e.getMessage());
           response.sendRedirect(request.getContextPath() + "/admin/orders");
       }
   }

   /**
    * Restore inventory when an order is cancelled
    * @param orderId The order ID
    */
   private void restoreInventory(int orderId) {
       try {
           // Get order items
           List<OrderItem> orderItems = orderDAO.getOrderItemsById(orderId);
           
           // For each item, restore inventory and log changes
           for (OrderItem item : orderItems) {
               // Get the current product
               Product product = productDAO.getProductById(item.getProductId());
               if (product != null) {
                   // Calculate new quantity
                   int newQuantity = product.getQuantity() + item.getQuantity();
                   int oldQuantity = product.getQuantity();
                   
                   // Update product quantity in database
                   String updateSql = "UPDATE Products SET quantity = ? WHERE product_id = ?";
                   try (java.sql.Connection conn = utils.db.DBContext.getConnection();
                        java.sql.PreparedStatement ps = conn.prepareStatement(updateSql)) {
                       ps.setInt(1, newQuantity);
                       ps.setInt(2, product.getProductId());
                       ps.executeUpdate();
                       
                       // Update product object for logging
                       product.setQuantity(newQuantity);
                       
                       // Log the inventory change
                       InventoryLogUtil.logInventoryChange(
                           product,
                           oldQuantity,
                           "Order cancelled: #" + orderId + " - Inventory restored",
                           orderId,
                           "order_cancelled"
                       );
                   }
               }
           }
       } catch (Exception e) {
           LOGGER.log(Level.SEVERE, "Lỗi khi khôi phục kho hàng: " + e.getMessage(), e);
       }
   }
} 