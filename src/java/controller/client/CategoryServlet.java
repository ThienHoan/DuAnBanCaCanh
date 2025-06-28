package controller.client;

import dao.impl.CategoryDAO;
import dao.impl.ProductDAO;
import dao.impl.ProductImageDAO;
import dao.impl.ProductDetailDAO;
import dao.impl.pAttribute.ProductAttributeValueDAO;
import model.entity.Category;
import model.entity.Product;
import model.entity.ProductImage;
import model.entity.ProductDetail;
import model.entity.pAttribute.ProductAttributeValue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;
    private ProductImageDAO productImageDAO;
    private ProductDetailDAO productDetailDAO;
    private ProductAttributeValueDAO productAttributeValueDAO;

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
        productDAO = new ProductDAO();
        productImageDAO = new ProductImageDAO();
        productDetailDAO = new ProductDetailDAO();
        productAttributeValueDAO = new ProductAttributeValueDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters
        int categoryId = request.getParameter("categoryId") != null ? 
                         Integer.parseInt(request.getParameter("categoryId")) : 0;
        boolean isParent = Boolean.parseBoolean(request.getParameter("isParent"));
        int page = request.getParameter("page") != null ? 
                   Integer.parseInt(request.getParameter("page")) : 1;
        String sort = request.getParameter("sort");
        String search = request.getParameter("search");

        // Set page size
        int pageSize = 10; // Number of products per page

        // Fetch categories for sidebar
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // Fetch products
        List<Product> products = productDAO.getProductsByCategoryId(categoryId, isParent, page, pageSize, sort, search);

        // Maps to store additional data for each product
        Map<Integer, List<ProductImage>> productImagesMap = new HashMap<>();
        Map<Integer, ProductDetail> productDetailsMap = new HashMap<>();
        Map<Integer, List<ProductAttributeValue>> productAttributesMap = new HashMap<>();

        // Fetch additional data for each product
        for (Product product : products) {
            int productId = product.getProductId();
            // Fetch images
            List<ProductImage> images = productImageDAO.getImagesByProductId(productId);
            productImagesMap.put(productId, images);

            // Fetch product details
            ProductDetail productDetail = productDetailDAO.getProductDetailByProductId(productId);
            productDetailsMap.put(productId, productDetail);

            // Fetch attributes with names
            List<ProductAttributeValue> attributes = productAttributeValueDAO.getProductAttributeValuesWithNamesByProductId(productId);
            productAttributesMap.put(productId, attributes);
        }

        // Fetch discounted products (only if not searching)
        List<Product> discountedProducts = null;
        Map<Integer, List<ProductImage>> discountedProductImagesMap = new HashMap<>();
        if (search == null || search.trim().isEmpty()) {
            discountedProducts = productDAO.getDiscountedProducts(10); // Limit to 10 discounted products
            for (Product product : discountedProducts) {
                List<ProductImage> images = productImageDAO.getImagesByProductId(product.getProductId());
                discountedProductImagesMap.put(product.getProductId(), images);
            }
        }
        request.setAttribute("discountedProducts", discountedProducts);
        request.setAttribute("discountedProductImagesMap", discountedProductImagesMap);

        // Calculate pagination
        int totalProducts = productDAO.getTotalProductsByCategoryId(categoryId, isParent, search);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        // Set request attributes
        request.setAttribute("products", products);
        request.setAttribute("productImagesMap", productImagesMap);
        request.setAttribute("productDetailsMap", productDetailsMap);
        request.setAttribute("productAttributesMap", productAttributesMap);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("categoryId", categoryId);

        // Forward to JSP
        request.getRequestDispatcher("/category1.jsp").forward(request, response);
    }
}