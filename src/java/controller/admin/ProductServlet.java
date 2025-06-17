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
    Product product = productDAO.getProductById(id);
    List<Category> listCategory = categoryDAO.getAllCategories();
    ProductDetail productDetail = productDetailDAO.getProductDetailByProductId(id);
    List<ProductImage> productImages = productImageDAO.getImagesByProductId(id);
    List<ProductAttributeValue> listProductAttributeValueByPID = 
        productAttributeValueDAO.getProductAttributeValuesByProductId(id);
    
    List<ProductAttribute> listProductAttribute = productAttributeDAO.getAllProductAttributes();
    request.setAttribute("product", product);
    request.setAttribute("listCategory", listCategory);
    request.setAttribute("productDetail", productDetail);
    request.setAttribute("productImages", productImages);
    request.setAttribute("listProductAttributeValueByPID", listProductAttributeValueByPID);
    request.setAttribute("listProductAttribute",listProductAttribute);
    
    // Forward đến trang form chỉnh sửa
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
        // ===== VALIDATION =====
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String categoryIdStr = request.getParameter("categoryId");
        String quantityStr = request.getParameter("quantity");
        String sku = request.getParameter("sku");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        if (priceStr == null || priceStr.trim().isEmpty())
            throw new IllegalArgumentException("Giá sản phẩm không được để trống");
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty())
            throw new IllegalArgumentException("Danh mục không được để trống");
        if (sku == null || sku.trim().isEmpty())
            throw new IllegalArgumentException("SKU không được để trống");

        // ===== SET PRODUCT =====
        Product product = new Product();
        product.setProductId(Integer.parseInt(request.getParameter("id")));
        int categoryId = Integer.parseInt(categoryIdStr);
        product.setCategoryId(categoryId > 0 ? categoryId : null);
        product.setName(name.trim());
        product.setDescription(request.getParameter("description"));
        product.setShortDescription(request.getParameter("shortDescription"));
        product.setPrice(new BigDecimal(priceStr));

        String salePriceStr = request.getParameter("salePrice");
        if (salePriceStr != null && !salePriceStr.trim().isEmpty()) {
            try {
                BigDecimal salePrice = new BigDecimal(salePriceStr);
                product.setSalePrice(salePrice.compareTo(BigDecimal.ZERO) > 0 ? salePrice : null);
            } catch (NumberFormatException e) {
                product.setSalePrice(null);
            }
        } else {
            product.setSalePrice(null);
        }

        product.setQuantity(quantityStr != null && !quantityStr.trim().isEmpty() ? Integer.parseInt(quantityStr) : 0);
        product.setSku(sku.trim());
        product.setStatus(request.getParameter("status"));
        product.setFeatured("true".equals(request.getParameter("featured")) ? 1 : 0);
        product.setIsDeleted(0);
        product.setUpdatedAt(LocalDateTime.now());

        // ===== SET PRODUCT DETAIL =====
        ProductDetail productDetail = null;
        String scientificName = request.getParameter("scientificName");
        String commonName = request.getParameter("commonName");
        String origin = request.getParameter("origin");
        String size = request.getParameter("size");
        String lifespan = request.getParameter("lifespan");
        String waterType = request.getParameter("waterType");
        String waterTemperature = request.getParameter("waterTemperature");
        String waterPh = request.getParameter("waterPh");
        String diet = request.getParameter("diet");
        String breedingDifficulty = request.getParameter("breedingDifficulty");
        String careLevel = request.getParameter("careLevel");
        String compatibility = request.getParameter("compatibility");

        boolean hasProductDetailData = 
       (scientificName != null && !scientificName.trim().isEmpty()) ||
       (commonName != null && !commonName.trim().isEmpty()) ||
       (origin != null && !origin.trim().isEmpty()) ||
       (size != null && !size.trim().isEmpty()) ||
       (lifespan != null && !lifespan.trim().isEmpty()) ||
       (waterType != null && !waterType.trim().isEmpty()) ||
       (waterTemperature != null && !waterTemperature.trim().isEmpty()) ||
       (waterPh != null && !waterPh.trim().isEmpty()) ||
       (diet != null && !diet.trim().isEmpty()) ||
       (breedingDifficulty != null && !breedingDifficulty.trim().isEmpty()) ||
       (careLevel != null && !careLevel.trim().isEmpty()) ||
       (compatibility != null && !compatibility.trim().isEmpty());

        if (hasProductDetailData) {
            productDetail = new ProductDetail();
            productDetail.setProductId(product.getProductId());
            productDetail.setScientificName(scientificName);
            productDetail.setCommonName(commonName);
            productDetail.setOrigin(origin);
            productDetail.setSize(size);
            productDetail.setLifespan(lifespan);
            productDetail.setWaterType(waterType);
            productDetail.setWaterTemperature(waterTemperature);
            productDetail.setWaterPh(waterPh);
            productDetail.setDiet(diet);
            productDetail.setBreedingDifficulty(breedingDifficulty);
            productDetail.setCareLevel(careLevel);
            productDetail.setCompatibility(compatibility);
            productDetail.setIsDeleted(0);
        }

        // ===== UPDATE PRODUCT && detail =====
        boolean productSuccess = productDAO.updateProduct(product);
        boolean productDetailSuccess = true;

        if (hasProductDetailData) {
            ProductDetail existingDetail = productDetailDAO.getProductDetailByProductId(product.getProductId());
            if (existingDetail != null) {
                productDetail.setProductDetailId(existingDetail.getProductDetailId());
                productDetailSuccess = productDetailDAO.updateProductDetail(productDetail);
            } else {
                productDetailSuccess = productDetailDAO.insertProductDetail(productDetail);
            }
        }

        // ===== UPDATE ATTRIBUTE VALUES =====
       String[] attributeIdStrs = request.getParameterValues("attributeIds[]");
boolean attributeSuccess = true;

if (attributeIdStrs != null) {
    for (String attrIdStr : attributeIdStrs) {
        try {
            int attrId = Integer.parseInt(attrIdStr);
            String value = request.getParameter("attributeValues[" + attrId + "]");
System.out.println(value);
            boolean exists = productAttributeValueDAO.isAttributeValueExists(product.getProductId(), attrId);

            if (value != null && !value.trim().isEmpty()) {
                ProductAttributeValue pav = new ProductAttributeValue(product.getProductId(), attrId, value.trim());

                    attributeSuccess &= productAttributeValueDAO.restoreOrInsertProductAttributeValue(pav);
                
            } else {
                if (exists) {
                    attributeSuccess &= productAttributeValueDAO.deleteProductAttributeValue(product.getProductId(), attrId);
                }
            }
        } catch (NumberFormatException e) {
            // log error
        }
    }
}

        // ===== FINAL RESPONSE =====
        if (productSuccess && productDetailSuccess && attributeSuccess) {
            String successMsg = "Cập nhật sản phẩm thành công!";
            if (hasProductDetailData) {
                successMsg = "Cập nhật sản phẩm, chi tiết và thuộc tính thành công!";
            }
            request.getSession().setAttribute("successMessage", successMsg);
            response.sendRedirect("products");
        } else {
            StringBuilder errorMsg = new StringBuilder("Không thể cập nhật ");
            if (!productSuccess) errorMsg.append("sản phẩm, ");
            if (!productDetailSuccess) errorMsg.append("chi tiết sản phẩm, ");
            if (!attributeSuccess) errorMsg.append("thuộc tính sản phẩm, ");
            errorMsg.append("vui lòng thử lại.");

            request.setAttribute("errorMessage", errorMsg.toString());
            request.setAttribute("product", product);
            if (hasProductDetailData) request.setAttribute("productDetail", productDetail);
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
        }

    } catch (NumberFormatException e) {
        request.setAttribute("errorMessage", "Định dạng số không hợp lệ: " + e.getMessage());
        request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
    } catch (IllegalArgumentException e) {
        request.setAttribute("errorMessage", e.getMessage());
        request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
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