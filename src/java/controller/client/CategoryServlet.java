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
    private static final int PAGE_SIZE = 5; // Products per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId;
        boolean isParent = "true".equals(request.getParameter("isParent"));
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        int page;
        try {
            categoryId = request.getParameter("categoryId") != null ? Integer.parseInt(request.getParameter("categoryId")) : 0;
        } catch (NumberFormatException e) {
            categoryId = 0;
        }
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            if (conn == null) {
                throw new SQLException("Failed to get database connection");
            }

            ProductViewDAO productViewDAO = new ProductViewDAO(conn);
            // Fetch all products for the main listing
            List<ProductView> allProducts;
            if (categoryId == 0) {
                allProducts = productViewDAO.getAllProducts(sort, search);
            } else if (isParent) {
                allProducts = productViewDAO.getProductsByParentCategoryId(categoryId, sort, search);
            } else {
                allProducts = productViewDAO.getProductsByCategoryId(categoryId, sort, search);
            }

            // Calculate total pages for main product listing
            int totalCount = allProducts.size();
            int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
            page = Math.max(1, Math.min(page, totalPages));

            // Paginate the main product list
            int startIndex = (page - 1) * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, totalCount);
            List<ProductView> paginatedProducts = allProducts.subList(startIndex, endIndex);

            // Fetch discounted products for the Hero Section
            List<ProductView> discountedProducts;
            if (categoryId == 0) {
                discountedProducts = productViewDAO.getAllDiscountedProducts(search);
            } else if (isParent) {
                discountedProducts = productViewDAO.getDiscountedProductsByParentCategoryId(categoryId, search);
            } else {
                discountedProducts = productViewDAO.getDiscountedProductsByCategoryId(categoryId, search);
            }

            // Limit to PAGE_SIZE
            if (discountedProducts.size() > PAGE_SIZE) {
                discountedProducts = discountedProducts.subList(0, PAGE_SIZE);
            }

            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getAllCategories();

            // Set attributes for JSP
            request.setAttribute("products", paginatedProducts);
            request.setAttribute("discountedProducts", discountedProducts);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("categories", categories);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

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