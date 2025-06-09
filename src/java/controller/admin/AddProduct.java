package controller.admin;

import model.entity.Product;
import dao.impl.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet(name = "AddProduct", urlPatterns = {"/addProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển đến trang form thêm sản phẩm
        request.getRequestDispatcher("admin/addProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String shortDescription = request.getParameter("shortDescription");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            BigDecimal salePrice = new BigDecimal(request.getParameter("salePrice"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String sku = request.getParameter("sku");
            String status = request.getParameter("status");
            Integer featured = Integer.parseInt(request.getParameter("featured"));
            Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));

            // Tạo đối tượng Product mới
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setShortDescription(shortDescription);
            product.setPrice(price);
            product.setSalePrice(salePrice);
            product.setQuantity(quantity);
            product.setSku(sku);
            product.setStatus(status);
            product.setFeatured(featured);
            product.setCategoryId(categoryId);
            product.setIsDeleted(0); // Mặc định là chưa xóa
            
            // Set thời gian tạo và cập nhật
            LocalDateTime now = LocalDateTime.now();
            product.setCreatedAt(now);
            product.setUpdatedAt(now);

            // Lưu sản phẩm vào database
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.createProduct(product);

            if (success) {
                // Nếu thành công, chuyển về trang danh sách
                response.sendRedirect("listProduct");
            } else {
                // Nếu thất bại, quay lại form với thông báo lỗi
                request.setAttribute("error", "Failed to add product");
                request.getRequestDispatcher("admin/addProduct.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format");
            request.getRequestDispatcher("admin/addProduct.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("admin/addProduct.jsp").forward(request, response);
        }
    }
}