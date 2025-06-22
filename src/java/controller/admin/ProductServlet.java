package controller.admin;

import dao.impl.ProductDAO;
import dao.impl.CategoryDAO;
import dao.impl.ProductDetailDAO;
import dao.impl.ProductImageDAO;
import dao.impl.pAttribute.ProductAttributeDAO;
import dao.impl.pAttribute.ProductAttributeValueDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import model.entity.Category;
import model.entity.Product;
import model.entity.ProductDetail;
import model.entity.ProductImage;
import model.entity.pAttribute.ProductAttribute;
import model.entity.pAttribute.ProductAttributeValue;
import service.impl.ProductsImageService;

@WebServlet("/products")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private ProductDetailDAO productDetailDAO;
    private ProductImageDAO productImageDAO;
    private ProductAttributeDAO productAttributeDAO;
    private ProductAttributeValueDAO productAttributeValueDAO;

    public void init() {
         System.out.println("=== SERVLET START ===");
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
        List<ProductAttribute> listProductAttribute = productAttributeDAO.getAllProductAttributes();
        request.setAttribute("listProductAttribute",listProductAttribute);
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
        product.setCategoryId(Integer.parseInt(categoryIdStr));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());        // ===== INSERT PRODUCT =====
        boolean success = productDAO.createProduct(product);
        System.out.println("DEBUG: createProduct success = " + success);
        if (!success) {
            request.setAttribute("errorMessage", "Không thể thêm sản phẩm");
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
            return;
        }
        // Lấy productId mới vừa thêm
        int productId = productDAO.getLastInsertProductId();
        System.out.println("DEBUG: productId = " + productId);
        if (productId <= 0) {
            request.setAttribute("errorMessage", "Không lấy được Product ID vừa tạo");
            request.getRequestDispatcher("admin/product-form.jsp").forward(request, response);
            return;
        }

        // ===== INSERT PRODUCT DETAIL (nếu có) =====
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
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProductId(productId);
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

            productDetailDAO.insertProductDetail(productDetail);
        }

        // ===== INSERT ATTRIBUTE VALUES =====
        String[] attributeIdStrs = request.getParameterValues("attributeIds[]");
        if (attributeIdStrs != null) {
            for (String attrIdStr : attributeIdStrs) {
                try {
                    int attrId = Integer.parseInt(attrIdStr);
                    String value = request.getParameter("attributeValues[" + attrId + "]");
                    if (value != null && !value.trim().isEmpty()) {
                        ProductAttributeValue pav = new ProductAttributeValue(productId, attrId, value);
                        productAttributeValueDAO.restoreOrInsertProductAttributeValue(pav);
                    }
                } catch (NumberFormatException e) {
                    // log error
                }
            }
        }

        // ===== XỬ LÝ ẢNH SẢN PHẨM =====
        ProductsImageService imageService = new ProductsImageService();
        boolean imageUploadSuccess = true;
        String imageErrorMessage = "";

        try {
            // --- ẢNH CHÍNH ---
            boolean uploadedMainImage = false;
            Part mainImagePart = request.getPart("mainImage");
            String mainImageUrlInput = request.getParameter("mainImageUrl");

            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                if (!imageService.isValidImageFile(mainImagePart)) {
                    throw new IllegalArgumentException("File ảnh chính không hợp lệ. Chỉ chấp nhận JPG, PNG, GIF");
                }
                String mainImageUrl = imageService.uploadImage(mainImagePart, request);
                if (mainImageUrl != null) {
                    ProductImage mainImage = new ProductImage();
                    mainImage.setProductId(productId);
                    mainImage.setImageUrl(mainImageUrl);
                    mainImage.setMain(1);
                    mainImage.setDisplayOrder(1);
                    mainImage.setDeleted(0);
                    productImageDAO.insertProductImage(mainImage);
                    uploadedMainImage = true;
                }
            }
            if (!uploadedMainImage && mainImageUrlInput != null && !mainImageUrlInput.trim().isEmpty()) {
                ProductImage mainImage = new ProductImage();
                mainImage.setProductId(productId);
                mainImage.setImageUrl(mainImageUrlInput.trim());
                mainImage.setMain(1);
                mainImage.setDisplayOrder(1);
                mainImage.setDeleted(0);
                productImageDAO.insertProductImage(mainImage);
            }

            // --- ẢNH PHỤ UPLOAD FILE ---
            Part[] additionalImageParts = request.getParts().stream()
                    .filter(part -> "additionalImages".equals(part.getName()) && part.getSize() > 0)
                    .toArray(Part[]::new);

            for (Part imagePart : additionalImageParts) {
                if (!imageService.isValidImageFile(imagePart)) {
                    imageErrorMessage += " File ảnh phụ không hợp lệ: " + imagePart.getSubmittedFileName() + ". ";
                    continue;
                }
                String imageUrl = imageService.uploadImage(imagePart, request);
                if (imageUrl != null) {
                    ProductImage additionalImage = new ProductImage();
                    additionalImage.setProductId(productId);
                    additionalImage.setImageUrl(imageUrl);
                    additionalImage.setMain(0);
                    additionalImage.setDeleted(0);
                    additionalImage.setDisplayOrder(productImageDAO.getNextDisplayOrder(productId));
                    productImageDAO.insertProductImage(additionalImage);
                }
            }

            // --- ẢNH PHỤ BẰNG LINK URL ---
            String additionalImageUrlsRaw = request.getParameter("additionalImageUrls");
            if (additionalImageUrlsRaw != null && !additionalImageUrlsRaw.trim().isEmpty()) {
                String[] additionalImageUrls = additionalImageUrlsRaw.split("\\r?\\n");
                for (String url : additionalImageUrls) {
                    if (url != null && !url.trim().isEmpty()) {
                        ProductImage image = new ProductImage();
                        image.setProductId(productId);
                        image.setImageUrl(url.trim());
                        image.setMain(0);
                        image.setDeleted(0);
                        image.setDisplayOrder(productImageDAO.getNextDisplayOrder(productId));
                        productImageDAO.insertProductImage(image);
                    }
                }
            }
        } catch (Exception e) {
            imageUploadSuccess = false;
            imageErrorMessage = "Lỗi khi upload hoặc thêm ảnh: " + e.getMessage();
        }

        // Xử lý kết quả ảnh
        if (!imageUploadSuccess) {
            request.setAttribute("errorMessage", imageErrorMessage);
        } else if (!imageErrorMessage.isEmpty()) {
            request.setAttribute("warningMessage", "Upload thành công nhưng có một số lỗi: " + imageErrorMessage);
        }

        // ===== FINAL RESPONSE =====
        request.getSession().setAttribute("successMessage", "Thêm sản phẩm thành công!");
        response.sendRedirect("products");

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

            boolean exists = productAttributeValueDAO.isAttributeValueExists(product.getProductId(), attrId);

            if (value != null && !value.trim().isEmpty()) {
                
                ProductAttributeValue pav = new ProductAttributeValue(product.getProductId(), attrId, value);
                    System.out.println(pav.toString());
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

// Khởi tạo service
// === PHẦN XỬ LÝ ẢNH SẢN PHẨM TRONG updateProduct ===

ProductsImageService imageService = new ProductsImageService();
boolean imageUploadSuccess = true;
String imageErrorMessage = "";

// ----------- 1. XỬ LÝ ẢNH CHÍNH ------------- //
try {
    boolean uploadedMainImage = false;
    Part mainImagePart = request.getPart("mainImage");
    String mainImageUrlInput = request.getParameter("mainImageUrl");

    // ƯU TIÊN upload file nếu có
    if (mainImagePart != null && mainImagePart.getSize() > 0) {
        if (!imageService.isValidImageFile(mainImagePart)) {
            throw new IllegalArgumentException("File ảnh chính không hợp lệ. Chỉ chấp nhận JPG, PNG, GIF");
        }

        String mainImageUrl = imageService.uploadImage(mainImagePart, request);
        if (mainImageUrl != null) {
            ProductImage mainImage = new ProductImage();
            mainImage.setProductId(product.getProductId());
            mainImage.setImageUrl(mainImageUrl);
            mainImage.setMain(1); // Là ảnh chính
            mainImage.setDisplayOrder(1);
            mainImage.setDeleted(0);

            if (!productImageDAO.replaceMainImage(mainImage)) {
                imageErrorMessage += " Không thể cập nhật ảnh chính.";
            }
            uploadedMainImage = true;
        }
    }

    // Nếu KHÔNG upload file, cho phép điền link ảnh chính
    if (!uploadedMainImage && mainImageUrlInput != null && !mainImageUrlInput.trim().isEmpty()) {
        System.out.println("mainImageUrlInput: " + mainImageUrlInput); // DEBUG

        ProductImage mainImage = new ProductImage();
        mainImage.setProductId(product.getProductId());
        mainImage.setImageUrl(mainImageUrlInput.trim());
        mainImage.setMain(1);
        mainImage.setDisplayOrder(1);
        mainImage.setDeleted(0);

        if (!productImageDAO.replaceMainImage(mainImage)) {
            imageErrorMessage += " Không thể lưu ảnh chính từ URL. ";
        }
    }
} catch (Exception e) {
    imageUploadSuccess = false;
    imageErrorMessage = "Lỗi khi upload hoặc thêm ảnh chính: " + e.getMessage();
}

// ----------- 2. XỬ LÝ ẢNH PHỤ ------------- //
try {
    // --- 2.1 ẢNH PHỤ UPLOAD FILE ---
    Part[] additionalImageParts = request.getParts().stream()
            .filter(part -> "additionalImages".equals(part.getName()) && part.getSize() > 0)
            .toArray(Part[]::new);

    for (Part imagePart : additionalImageParts) {
        if (!imageService.isValidImageFile(imagePart)) {
            imageErrorMessage += " File ảnh phụ không hợp lệ: " + imagePart.getSubmittedFileName() + ". ";
            continue;
        }

        String imageUrl = imageService.uploadImage(imagePart, request);
        if (imageUrl != null) {
            ProductImage additionalImage = new ProductImage();
            additionalImage.setProductId(product.getProductId());
            additionalImage.setImageUrl(imageUrl);
            additionalImage.setMain(0); // Là ảnh phụ
            additionalImage.setDeleted(0); // Mặc định chưa xóa
            // Lấy thứ tự tăng tự động
            additionalImage.setDisplayOrder(productImageDAO.getNextDisplayOrder(product.getProductId()));

            if (!productImageDAO.insertProductImage(additionalImage)) {
                imageErrorMessage += " Không thể lưu ảnh phụ " + imagePart.getSubmittedFileName() + ". ";
            }
        }
    }

    // --- 2.2 ẢNH PHỤ BẰNG LINK URL ---
    String additionalImageUrlsRaw = request.getParameter("additionalImageUrls");
    System.out.println("additionalImageUrlsRaw: " + additionalImageUrlsRaw); // DEBUG

    if (additionalImageUrlsRaw != null && !additionalImageUrlsRaw.trim().isEmpty()) {
        String[] additionalImageUrls = additionalImageUrlsRaw.split("\\r?\\n");
        for (String url : additionalImageUrls) {
            if (url != null && !url.trim().isEmpty()) {
                ProductImage image = new ProductImage();
                image.setProductId(product.getProductId());
                image.setImageUrl(url.trim());
                image.setMain(0);
                image.setDeleted(0);
                // Lấy thứ tự tăng tự động
                image.setDisplayOrder(productImageDAO.getNextDisplayOrder(product.getProductId()));

                if (!productImageDAO.insertProductImage(image)) {
                    imageErrorMessage += " Không thể lưu ảnh phụ từ URL: " + url + ". ";
                }
            }
        }
    }

} catch (Exception e) {
    imageUploadSuccess = false;
    imageErrorMessage = "Lỗi khi upload hoặc thêm ảnh phụ: " + e.getMessage();
}

// ----------- 3. THÔNG BÁO LỖI ------------- //
if (!imageUploadSuccess) {
    request.setAttribute("errorMessage", imageErrorMessage);
} else if (!imageErrorMessage.isEmpty()) {
    request.setAttribute("warningMessage", "Upload thành công nhưng có một số lỗi: " + imageErrorMessage);
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