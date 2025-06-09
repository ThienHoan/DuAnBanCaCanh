package controller.admin;

import model.entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.impl.ProductDAO;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListProduct", urlPatterns = {"/listProduct"})
public class ListProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Tao doi tuong DAO
        ProductDAO dao = new ProductDAO();

        // Lay danh sach san pham (ban co the thay doi tham so de loc theo y muon)
        List<Product> productList = dao.getAllProducts();

        // Dat danh sach vao request
        request.setAttribute("products", productList);

        // Chuyen toi trang hien thi
        request.getRequestDispatcher("admin/managerProduct.jsp").forward(request, response);
    }
}
