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


@WebServlet(name = "EditProduct", urlPatterns = {"/editProduct"})
public class EditProduct extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            ProductDAO dao = new ProductDAO();
            Product product = dao.getProductById(productId);
            
            if (product != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            Product product = new Product();
            product.setProductId(Integer.parseInt(request.getParameter("productId")));
            product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            product.setName(request.getParameter("name"));
            product.setDescription(request.getParameter("description"));
            product.setShortDescription(request.getParameter("shortDescription"));
            product.setPrice(new BigDecimal(request.getParameter("price")));
            product.setSalePrice(new BigDecimal(request.getParameter("salePrice")));
            product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
            product.setSku(request.getParameter("sku"));
            product.setStatus(request.getParameter("status"));
            product.setFeatured(Integer.parseInt(request.getParameter("featured")));
            product.setIsDeleted(Integer.parseInt(request.getParameter("isDeleted")));

            // Cập nhật sản phẩm
            ProductDAO dao = new ProductDAO();
            boolean success = dao.updateProduct(product);

            // Trả về kết quả

            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");


        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }
    }
}