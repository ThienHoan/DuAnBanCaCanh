package controller.admin;

import dao.impl.ProductDAO;
import dao.impl.CategoryDAO;
import dao.impl.ProductDetailDAO;
import dao.impl.ProductImageDAO;
import dao.impl.pAttribute.ProductAttributeDAO;
import dao.impl.pAttribute.ProductAttributeValueDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import model.entity.Category;
import model.entity.Product;
import model.entity.ProductDetail;
import model.entity.ProductImage;
import model.entity.pAttribute.ProductAttribute;
import model.entity.pAttribute.ProductAttributeValue;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private ProductDetailDAO productDetailDAO;
    private ProductImageDAO productImageDAO;
    private ProductAttributeDAO productAttributeDAO;
    private ProductAttributeValueDAO productAttributeValueDAO;

    public void init() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        productDetailDAO = new ProductDetailDAO();
        productImageDAO = new ProductImageDAO();
        productAttributeDAO = new ProductAttributeDAO();
        productAttributeValueDAO = new ProductAttributeValueDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Để trống hoặc có thể chuyển hướng đến một trang cụ thể nếu cần
        // Ví dụ: response.sendRedirect("some-page.jsp");
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "insert":
                    insertProduct(request, response);
                    break;
                case "delete":
                    
                    deleteProduct(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "view":
                    viewProduct(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + ex.getMessage());
            listProducts(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> listProduct = productDAO.getAllProducts();
        request.setAttribute("listProduct", listProduct);

        List<Category> listCategory = categoryDAO.getAllCategories();
        request.setAttribute("listCategory", listCategory);

        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/products.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> listCategory = categoryDAO.getAllCategories();
        request.setAttribute("listCategory", listCategory);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/product-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product existingProduct = productDAO.getProductById(id);
        List<Category> listCategory = categoryDAO.getAllCategories();

        request.setAttribute("product", existingProduct);
        request.setAttribute("listCategory", listCategory);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/product-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String shortDescription = request.getParameter("shortDescription");
        String priceStr = request.getParameter("price");
        String salePriceStr = request.getParameter("salePrice");
        String quantityStr = request.getParameter("quantity");
        String sku = request.getParameter("sku");
        String status = request.getParameter("status");
        String featuredStr = request.getParameter("featured");
        String categoryIdStr = request.getParameter("categoryId");

        // Validate bắt buộc
        if (name == null || name.trim().isEmpty() ||
            priceStr == null || priceStr.trim().isEmpty() ||
            quantityStr == null || quantityStr.trim().isEmpty() ||
            sku == null || sku.trim().isEmpty() ||
            status == null || status.trim().isEmpty() ||
            categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin bắt buộc.");
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
            return;
        }

        BigDecimal price = new BigDecimal(priceStr);
        BigDecimal salePrice = (salePriceStr != null && !salePriceStr.trim().isEmpty())
                ? new BigDecimal(salePriceStr) : BigDecimal.ZERO;
        int quantity = Integer.parseInt(quantityStr);
        Integer categoryId = Integer.parseInt(categoryIdStr);

        // Checkbox: nếu không check thì null, nếu check thì "true"
        Integer featured = (featuredStr != null && featuredStr.equals("true")) ? 1 : 0;

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
        boolean success = productDAO.createProduct(product);

        if (success) {
            // Nếu thành công, chuyển về trang danh sách
            response.sendRedirect("products");
        } else {
            // Nếu thất bại, quay lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể thêm sản phẩm");
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
        }

    } catch (NumberFormatException e) {
        request.setAttribute("errorMessage", "Định dạng số không hợp lệ");
        request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
    } catch (Exception e) {
        request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
    }
}

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
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
            product.setSalePrice(request.getParameter("salePrice") != null && !request.getParameter("salePrice").isEmpty() 
                ? new BigDecimal(request.getParameter("salePrice")) : null);
            product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
            product.setSku(request.getParameter("sku"));
            product.setStatus(request.getParameter("status"));
            product.setFeatured(Integer.parseInt(request.getParameter("featured")));
            product.setIsDeleted(Integer.parseInt(request.getParameter("isDeleted")));

            // Cập nhật thời gian
            product.setUpdatedAt(LocalDateTime.now());

            // Cập nhật sản phẩm
            boolean success = productDAO.updateProduct(product);

            if (success) {
                response.sendRedirect("products");
            } else {
                request.setAttribute("error", "Không thể cập nhật sản phẩm");
                request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Định dạng số không hợp lệ");
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy productId từ tham số request
            int productId = Integer.parseInt(request.getParameter("id"));

            // Khởi tạo ProductDAO và gọi hàm toggleIsDeleted
            boolean success = productDAO.toggleIsDeleted(productId);

            if (success) {
                // Chuyển hướng về trang danh sách sản phẩm nếu thành công
                response.sendRedirect("products");
            } else {
                // Ném ngoại lệ nếu không tìm thấy sản phẩm hoặc có lỗi
                throw new ServletException("Không thể cập nhật trạng thái xóa sản phẩm");
            }
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu productId không hợp lệ
            throw new ServletException("ID sản phẩm không hợp lệ", e);
        } catch (Exception e) {
            // Xử lý các lỗi khác
            e.printStackTrace();
            throw new ServletException("Lỗi khi xử lý yêu cầu xóa sản phẩm", e);
        }
    }

    private void viewProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.getProductById(id);
        List<Category> categorys = categoryDAO.getAllCategories();
        ProductDetail productDetail = productDetailDAO.getProductDetailByProductId(id);
        List<ProductImage> productImage = productImageDAO.getImagesByProductId(id);
        List<ProductAttributeValue> listProductAttributeValueByPID = productAttributeValueDAO.getProductAttributeValuesByProductId(id);
        ProductAttribute productAttribute = productAttributeDAO.getProductAttributeById(id);
        request.setAttribute("productAttribute", productAttribute);
        request.setAttribute("listProductAttributeValueByPID", listProductAttributeValueByPID);
        request.setAttribute("productImages", productImage);
        request.setAttribute("productDetail", productDetail);
        request.setAttribute("categorys", categorys);
        request.setAttribute("product", product);
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/product-view.jsp");
        dispatcher.forward(request, response);
    }
}