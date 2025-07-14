package controller.client;

import dao.impl.CategoryDAO;
import dao.impl.ProductDAO;
import dao.impl.ProductImageDAO;
import dao.impl.ProductDetailDAO;
import dao.impl.pAttribute.ProductAttributeDAO;
import dao.impl.pAttribute.ProductAttributeValueDAO;
import model.entity.Category;
import model.entity.Product;
import model.entity.ProductImage;
import model.entity.ProductDetail;
import model.entity.pAttribute.ProductAttribute;
import model.entity.pAttribute.ProductAttributeValue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;
    private ProductImageDAO productImageDAO;
    private ProductDetailDAO productDetailDAO;
    private ProductAttributeValueDAO productAttributeValueDAO;
    private ProductAttributeDAO productAttributeDAO;

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
        productDAO = new ProductDAO();
        productImageDAO = new ProductImageDAO();
        productDetailDAO = new ProductDetailDAO();
        productAttributeValueDAO = new ProductAttributeValueDAO();
        productAttributeDAO = new ProductAttributeDAO();
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

            // Fetch attributes
            List<ProductAttributeValue> attributes = productAttributeValueDAO.getProductAttributeValuesWithNamesByProductId(productId);
            productAttributesMap.put(productId, attributes);
        }

        // Fetch attribute map
        List<ProductAttribute> listProductAttribute = productAttributeDAO.getAllProductAttributes();
        Map<Integer, ProductAttribute> attributeMap = listProductAttribute.stream()
            .collect(Collectors.toMap(ProductAttribute::getAttributeId, Function.identity()));
        
        // Group products by name
        Map<String, List<Product>> groupedProducts = new LinkedHashMap<>();
        for (Product product : products) {
            String name = product.getName();
            if (!groupedProducts.containsKey(name)) {
                groupedProducts.put(name, new ArrayList<>());
            }
            groupedProducts.get(name).add(product);
        }
        
        // Create a map to store attribute values for each product group
        Map<String, Map<String, Set<String>>> groupAttributesMap = new HashMap<>();
        
        // For each product group, collect unique attribute values
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String productName = entry.getKey();
            List<Product> productGroup = entry.getValue();
            
            Map<String, Set<String>> attributeValues = new HashMap<>();
            attributeValues.put("Color", new HashSet<>());
            attributeValues.put("Size", new HashSet<>());
            
            for (Product product : productGroup) {
                List<ProductAttributeValue> attributes = productAttributesMap.get(product.getProductId());
                if (attributes != null) {
                    for (ProductAttributeValue attr : attributes) {
                        ProductAttribute productAttr = attributeMap.get(attr.getAttributeId());
                        if (productAttr != null) {
                            String attrName = productAttr.getName();
                            if ("Color".equals(attrName) || "Size".equals(attrName)) {
                                if (!attributeValues.containsKey(attrName)) {
                                    attributeValues.put(attrName, new HashSet<>());
                                }
                                attributeValues.get(attrName).add(attr.getValue());
                            }
                        }
                    }
                }
            }
            
            groupAttributesMap.put(productName, attributeValues);
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

        // Set request attributes
        request.setAttribute("discountedProducts", discountedProducts);
        request.setAttribute("discountedProductImagesMap", discountedProductImagesMap);
        request.setAttribute("products", products);
        request.setAttribute("productImagesMap", productImagesMap);
        request.setAttribute("productDetailsMap", productDetailsMap);
        request.setAttribute("productAttributesMap", productAttributesMap);
        request.setAttribute("attributeMap", attributeMap);
        request.setAttribute("totalPages", (int) Math.ceil((double) productDAO.getTotalProductsByCategoryId(categoryId, isParent, search) / pageSize));
        request.setAttribute("currentPage", page);
        request.setAttribute("categoryId", categoryId);
        
        // Add grouped products and their attributes
        request.setAttribute("groupedProducts", groupedProducts);
        request.setAttribute("groupAttributesMap", groupAttributesMap);

        // Forward to JSP
        request.getRequestDispatcher("/category1.jsp").forward(request, response);
    }
}