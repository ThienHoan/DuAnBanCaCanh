package controller.client;

import dao.impl.ProductDAO;
import dao.impl.CategoryDAO;
import model.entity.Product;
import model.entity.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/category", "/category-search"})
public class CategoryController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CategoryController.class.getName());
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        LOGGER.info("CategoryController initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/category":
                showCategoryPage(request, response);
                break;
            case "/category-search":
                handleCategorySearch(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/category");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Hiển thị trang tất cả sản phẩm
    private void showCategoryPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            List<Product> allProducts = productDAO.getAllProducts();
            request.setAttribute("allProducts", allProducts); // Truyền tất cả sản phẩm

            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);

            // Lấy danh mục mặc định (nếu có) hoặc tất cả
            String category = request.getParameter("category");
            category = (category != null) ? category.trim() : "-1";
            List<Product> products = ("-1".equals(category)) ? allProducts : productDAO.getProductsByCategory(category);
            request.setAttribute("products", products);
            request.setAttribute("selectedCategory", category);

            request.setAttribute("pageTitle", "Danh sách tất cả sản phẩm");
            request.getRequestDispatcher("/category1.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tải danh sách sản phẩm", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm trong trang category
    private void handleCategorySearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            List<Product> allProducts = productDAO.getAllProducts();
            request.setAttribute("allProducts", allProducts); // Truyền tất cả sản phẩm

            String keyword = request.getParameter("s");
            String category = request.getParameter("category");

            keyword = (keyword != null) ? keyword.trim() : "";
            category = (category != null) ? category.trim() : "-1";

            List<Product> products;
            if (!keyword.isEmpty()) {
                if (!"-1".equals(category)) {
                    products = productDAO.searchProductsByNameInCategory(keyword, category);
                } else {
                    products = productDAO.searchProductsByName(keyword);
                }
            } else {
                products = ("-1".equals(category)) ? allProducts : productDAO.getProductsByCategory(category);
            }

            if (products == null) {
                products = new java.util.ArrayList<>();
            }

            request.setAttribute("products", products);
            request.setAttribute("searchQuery", keyword);
            request.setAttribute("selectedCategory", category);
            request.setAttribute("pageTitle", "Kết quả tìm kiếm: " + (keyword.isEmpty() ? "Tất cả sản phẩm" : keyword));

            List<Category> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);

            LOGGER.info("Tìm kiếm từ khóa: " + keyword + ", category: " + category + ", kết quả: " + products.size());

            request.getRequestDispatcher("/category1.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm sản phẩm theo từ khóa", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}