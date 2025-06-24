/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.client;

import dao.impl.CategoryDAO;
import dao.impl.ProductViewDAO;
import model.entity.Category;
import model.entity.ProductView;
import utils.db.DBContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private static final int PAGE_SIZE = 10; // Default products per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy categoryId và page từ request parameter
        int categoryId;
        int page;
        try {
            categoryId = Integer.parseInt(request.getParameter("categoryId"));
        } catch (NumberFormatException e) {
            categoryId = 1; // Default to category 1 if invalid
        }
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1; // Default to page 1 if invalid
        }

        Connection conn = null;
        try {
            // Sử dụng DBContext để lấy kết nối
            conn = DBContext.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to get database connection");
            }

            // Lấy danh sách sản phẩm với phân trang
            ProductViewDAO productViewDAO = new ProductViewDAO(conn);
            ProductViewDAO.PaginatedResult paginatedResult = productViewDAO.getProductsByCategoryId(categoryId, page, PAGE_SIZE);
            List<ProductView> products = paginatedResult.getProducts();
            int totalPages = paginatedResult.getTotalPages();

            // Lấy danh sách tất cả danh mục
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getAllCategories();

            // Đặt dữ liệu vào request
            request.setAttribute("products", products);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("categories", categories);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // Forward đến category1.jsp
            request.getRequestDispatcher("/category1.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}