package controller.admin;

import dao.impl.CategoryDAO;
import dao.impl.InventoryLogDAOImpl;
import dao.impl.ProductDAO;
import dao.interfaces.InventoryLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import model.entity.Category;
import model.entity.InventoryLog;
import model.entity.Product;

/**
 * Controller for warehouse management page
 */
@WebServlet("/admin/warehouse")
@MultipartConfig
public class WarehouseController extends HttpServlet {
    
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private InventoryLogDAO inventoryLogDAO;
    
    @Override
    public void init() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        inventoryLogDAO = new InventoryLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String tab = request.getParameter("tab");
        if (tab == null) {
            tab = "products"; // Default tab is products, skipping the file upload form
        }
        
        // Get sorting parameters
        String sortColumn = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        
        // Default sorting
        if (sortColumn == null) {
            if ("inventory".equals(tab)) {
                sortColumn = "time"; // Default sort for inventory logs
                sortOrder = "desc"; // Newest first
            } else {
                sortColumn = "name"; // Default sort for products
                sortOrder = "asc"; // A-Z
            }
        }
        
        if (sortOrder == null) {
            sortOrder = "asc"; // Default order
        }
        
        // Get products for both tabs
        List<Product> products = getAllProducts(sortColumn, sortOrder);
        request.setAttribute("listProduct", products);
        
        // Get categories for product display
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("listCategory", categories);
        
        // If inventory tab is active, load inventory logs
        if ("inventory".equals(tab)) {
            String productIdParam = request.getParameter("productId");
            List<InventoryLog> inventoryLogs;
            
            if (productIdParam != null && !productIdParam.isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIdParam);
                    inventoryLogs = getInventoryLogsByProductId(productId, sortColumn, sortOrder);
                    request.setAttribute("selectedProductId", productId);
                } catch (NumberFormatException e) {
                    inventoryLogs = getAllInventoryLogs(sortColumn, sortOrder);
                }
            } else {
                inventoryLogs = getAllInventoryLogs(sortColumn, sortOrder);
            }
            
            request.setAttribute("inventoryLogs", inventoryLogs);
        }
        
        request.setAttribute("activeTab", tab);
        request.getRequestDispatcher("/admin/warehouse.jsp").forward(request, response);
    }
    
    /**
     * Get all products with sorting
     */
    private List<Product> getAllProducts(String sortColumn, String sortOrder) {
        List<Product> products = productDAO.getAllProducts();
        
        if (products != null && !products.isEmpty()) {
            // Sort the products based on the specified column and order
            products.sort((p1, p2) -> {
                int result = 0;
                boolean isAscending = "asc".equalsIgnoreCase(sortOrder);
                
                switch (sortColumn) {
                    case "id":
                        result = Integer.compare(p1.getProductId(), p2.getProductId());
                        break;
                    case "name":
                        result = p1.getName().compareToIgnoreCase(p2.getName());
                        break;
                    case "sku":
                        result = p1.getSku().compareToIgnoreCase(p2.getSku());
                        break;
                    case "price":
                        result = p1.getPrice().compareTo(p2.getPrice());
                        break;
                    case "salePrice":
                        // Handle potential null values for sale prices
                        if (p1.getSalePrice() == null && p2.getSalePrice() == null) {
                            result = 0;
                        } else if (p1.getSalePrice() == null) {
                            result = -1;
                        } else if (p2.getSalePrice() == null) {
                            result = 1;
                        } else {
                            result = p1.getSalePrice().compareTo(p2.getSalePrice());
                        }
                        break;
                    case "quantity":
                        result = Integer.compare(p1.getQuantity(), p2.getQuantity());
                        break;
                    case "status":
                        result = p1.getStatus().compareToIgnoreCase(p2.getStatus());
                        break;
                    case "featured":
                        result = Integer.compare(p1.getFeatured(), p2.getFeatured());
                        break;
                    case "createdAt":
                        // Handle potential null values for createdAt
                        if (p1.getCreatedAt() == null && p2.getCreatedAt() == null) {
                            result = 0;
                        } else if (p1.getCreatedAt() == null) {
                            result = -1;
                        } else if (p2.getCreatedAt() == null) {
                            result = 1;
                        } else {
                            result = p1.getCreatedAt().compareToIgnoreCase(p2.getCreatedAt());
                        }
                        break;
                    case "category":
                        // Compare by category ID
                        result = Integer.compare(p1.getCategoryId(), p2.getCategoryId());
                        break;
                    default:
                        // Default to sorting by name
                        result = p1.getName().compareToIgnoreCase(p2.getName());
                }
                
                // Reverse the order if descending
                return isAscending ? result : -result;
            });
        }
        
        return products;
    }
    
    /**
     * Get all inventory logs with sorting
     */
    private List<InventoryLog> getAllInventoryLogs(String sortColumn, String sortOrder) {
        List<InventoryLog> logs = inventoryLogDAO.getAllInventoryLogs();
        sortInventoryLogs(logs, sortColumn, sortOrder);
        return logs;
    }
    
    /**
     * Get inventory logs by product ID with sorting
     */
    private List<InventoryLog> getInventoryLogsByProductId(int productId, String sortColumn, String sortOrder) {
        List<InventoryLog> logs = inventoryLogDAO.getInventoryLogsByProductId(productId);
        sortInventoryLogs(logs, sortColumn, sortOrder);
        return logs;
    }
    
    /**
     * Sort inventory logs by the specified column and order
     */
    private void sortInventoryLogs(List<InventoryLog> logs, String sortColumn, String sortOrder) {
        if (logs != null && !logs.isEmpty()) {
            logs.sort((log1, log2) -> {
                int result = 0;
                boolean isAscending = "asc".equalsIgnoreCase(sortOrder);
                
                switch (sortColumn) {
                    case "id":
                        result = Integer.compare(log1.getLogId(), log2.getLogId());
                        break;
                    case "name":
                        result = log1.getProductName().compareToIgnoreCase(log2.getProductName());
                        break;
                    case "sku":
                        result = log1.getProductSku().compareToIgnoreCase(log2.getProductSku());
                        break;
                    case "qtyBefore":
                        result = Integer.compare(log1.getQuantityBefore(), log2.getQuantityBefore());
                        break;
                    case "qtyAfter":
                        result = Integer.compare(log1.getQuantityAfter(), log2.getQuantityAfter());
                        break;
                    case "change":
                        // Calculate the change
                        int change1 = log1.getQuantityAfter() - log1.getQuantityBefore();
                        int change2 = log2.getQuantityAfter() - log2.getQuantityBefore();
                        result = Integer.compare(change1, change2);
                        break;
                    case "type":
                        result = log1.getChangeType().compareToIgnoreCase(log2.getChangeType());
                        break;
                    case "reason":
                        // Handle potential null values
                        if (log1.getReason() == null && log2.getReason() == null) {
                            result = 0;
                        } else if (log1.getReason() == null) {
                            result = -1;
                        } else if (log2.getReason() == null) {
                            result = 1;
                        } else {
                            result = log1.getReason().compareToIgnoreCase(log2.getReason());
                        }
                        break;
                    case "time":
                        // Compare timestamps
                        if (log1.getCreatedAt() == null && log2.getCreatedAt() == null) {
                            result = 0;
                        } else if (log1.getCreatedAt() == null) {
                            result = -1;
                        } else if (log2.getCreatedAt() == null) {
                            result = 1;
                        } else {
                            result = log1.getCreatedAt().compareTo(log2.getCreatedAt());
                        }
                        break;
                    default:
                        // Default to sorting by time descending
                        if (log1.getCreatedAt() == null && log2.getCreatedAt() == null) {
                            result = 0;
                        } else if (log1.getCreatedAt() == null) {
                            result = -1;
                        } else if (log2.getCreatedAt() == null) {
                            result = 1;
                        } else {
                            result = log1.getCreatedAt().compareTo(log2.getCreatedAt());
                        }
                }
                
                // Reverse the order if descending
                return isAscending ? result : -result;
            });
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("import_csv".equals(action)) {
            // --- KHÔI PHỤC LOGIC UPLOAD NHẬT KÝ KHO HÀNG ---
            Part filePart = request.getPart("csvFile");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8))) {
                    String line;
                    boolean isFirstLine = true;
                    int successCount = 0;
                    int failCount = 0;
                    StringBuilder errors = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue; // Skip header row
                        }
                        List<String> values = parseCSVLine(line);
                        if (values.size() < 9) {
                            failCount++;
                            errors.append("Thiếu cột dữ liệu: ").append(line).append("<br>");
                            continue;
                        }
                        try {
                            // Expected columns: ID | Sản Phẩm | SKU | SL Trước | SL Sau | Thay Đổi | Loại | Lý Do | Thời Gian
                            String productName = values.get(1);
                            String sku = values.get(2);
                            int qtyBefore = Integer.parseInt(values.get(3));
                            int qtyAfter = Integer.parseInt(values.get(4));
                            String change = values.get(5);
                            String type = values.get(6);
                            String reason = values.get(7);
                            String time = values.get(8);
                            // Tìm sản phẩm theo SKU
                            Product product = productDAO.getProductBySku(sku);
                            if (product == null) {
                                failCount++;
                                errors.append("Không tìm thấy sản phẩm với SKU: ").append(sku).append("<br>");
                                continue;
                            }
                            // Ghi log kho hàng
                            InventoryLog log = new InventoryLog();
                            log.setProductId(product.getProductId());
                            log.setQuantityBefore(qtyBefore);
                            log.setQuantityAfter(qtyAfter);
                            log.setChangeType(type);
                            log.setReason(reason);
                            log.setCreatedAt(time);
                            log.setProductName(productName);
                            log.setProductSku(sku);
                            boolean result = inventoryLogDAO.insertInventoryLog(log);
                            if (result) {
                                successCount++;
                                // Cập nhật số lượng sản phẩm
                                product.setQuantity(qtyAfter);
                                productDAO.updateProduct(product);
                            } else {
                                failCount++;
                                errors.append("Không thể thêm log cho SKU: ").append(sku).append("<br>");
                            }
                        } catch (Exception e) {
                            failCount++;
                            errors.append("Lỗi xử lý: ").append(line).append(" - ").append(e.getMessage()).append("<br>");
                        }
                    }
                    String message = "Đã nhập: " + successCount + " nhật ký kho hàng thành công";
                    if (failCount > 0) {
                        message += ", lỗi: " + failCount;
                    }
                    if (errors.length() > 0) {
                        request.setAttribute("importErrors", errors.toString());
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/warehouse?tab=inventory&success=import&msg=" +
                            java.net.URLEncoder.encode(message, "UTF-8"));
                    return;
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/admin/warehouse?tab=inventory&error=import&msg=" +
                            java.net.URLEncoder.encode("Lỗi xử lý file: " + e.getMessage(), "UTF-8"));
                    return;
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/warehouse?tab=inventory&error=import&msg=" +
                        java.net.URLEncoder.encode("Chưa chọn file hoặc file rỗng", "UTF-8"));
                return;
            }
        }
        // --- TÁCH RIÊNG UPLOAD SẢN PHẨM MỚI ---
        else if ("import_product_csv".equals(action)) {
            Part filePart = request.getPart("csvFile");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8))) {
                    String line;
                    int successCount = 0;
                    int failCount = 0;
                    StringBuilder errors = new StringBuilder();
                    boolean isFirstLine = true;
                    int lineNum = 0;
                    while ((line = reader.readLine()) != null) {
                        lineNum++;
                        if (line.trim().isEmpty()) continue;
                        List<String> values = parseCSVLine(line);
                        // Bỏ qua dòng đầu tiên nếu là header (có thể có BOM)
                        String firstCol = values.get(0).replace("\uFEFF", "").trim();
                        if (isFirstLine && (firstCol.equalsIgnoreCase("category_id") || firstCol.equalsIgnoreCase("Category ID"))) {
                            isFirstLine = false;
                            continue;
                        }
                        isFirstLine = false;
                        if (values.size() < 11) {
                            failCount++;
                            errors.append("Thiếu cột dữ liệu dòng ").append(lineNum).append(": ").append(line).append("<br>");
                            continue;
                        }
                        try {
                            int categoryId = Integer.parseInt(values.get(0).replaceAll("[^0-9]", ""));
                            String name = values.get(1);
                            String description = values.get(2);
                            String shortDescription = values.get(3);
                            // Xử lý giá, sale_price, quantity loại bỏ ký tự lạ
                            String priceStr = values.get(4).replaceAll("[^0-9.]", "");
                            String salePriceStr = values.get(5).replaceAll("[^0-9.]", "");
                            String quantityStr = values.get(6).replaceAll("[^0-9]", "");
                            java.math.BigDecimal price = new java.math.BigDecimal(priceStr);
                            java.math.BigDecimal salePrice = null;
                            if (!salePriceStr.isEmpty()) {
                                try {
                                    salePrice = new java.math.BigDecimal(salePriceStr);
                                } catch (Exception ex) {
                                    salePrice = null; // Nếu không hợp lệ thì bỏ qua, không báo lỗi
                                }
                            }
                            int quantity = Integer.parseInt(quantityStr);
                            String sku = values.get(7);
                            String status = values.get(8);
                            // Chuẩn hóa featured
                            int featured = 0;
                            String featuredStr = values.get(9).trim();
                            if (featuredStr.equalsIgnoreCase("TRUE") || featuredStr.equals("1")) featured = 1;
                            // Chuẩn hóa is_deleted
                            int isDeleted = 0;
                            String isDeletedStr = values.get(10).trim();
                            if (isDeletedStr.equalsIgnoreCase("TRUE") || isDeletedStr.equals("1")) isDeleted = 1;
                            // Nếu sku đã tồn tại thì bỏ qua, báo lỗi
                            if (productDAO.skuExists(sku)) {
                                failCount++;
                                errors.append("SKU đã tồn tại: ").append(sku).append(" (Sản phẩm: ").append(name).append(") dòng ").append(lineNum).append("<br>");
                                continue;
                            }
                            Product product = new Product();
                            product.setName(name);
                            product.setSku(sku);
                            product.setQuantity(quantity);
                            product.setCategoryId(categoryId);
                            product.setPrice(price);
                            product.setSalePrice(salePrice);
                            product.setStatus(status);
                            product.setFeatured(featured);
                            product.setIsDeleted(isDeleted);
                            product.setDescription(description);
                            product.setShortDescription(shortDescription);
                            boolean success = productDAO.createProduct(product);
                            if (success) {
                                successCount++;
                                int productId = productDAO.getLastInsertProductId();
                                if (productId > 0) {
                                    product.setProductId(productId);
                                    utils.InventoryLogUtil.logInventoryChange(
                                            product,
                                            0,
                                            "Initial import from CSV",
                                            null,
                                            "csv_import");
                                }
                            } else {
                                failCount++;
                                errors.append("Không thể thêm: ").append(name).append(" (SKU: ").append(sku).append(") dòng ").append(lineNum).append("<br>");
                            }
                        } catch (NumberFormatException e) {
                            failCount++;
                            errors.append("Lỗi định dạng số ở dòng ").append(lineNum).append(": ").append(line).append("<br>");
                        } catch (Exception e) {
                            failCount++;
                            errors.append("Lỗi xử lý dòng ").append(lineNum).append(": ").append(line).append(" - ").append(e.getMessage()).append("<br>");
                        }
                    }
                    String message = "Đã nhập: " + successCount + " sản phẩm thành công";
                    if (failCount > 0) {
                        message += ", lỗi: " + failCount;
                    }
                    if (errors.length() > 0) {
                        request.setAttribute("importErrors", errors.toString());
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/warehouse?tab=products&success=import&msg=" +
                            java.net.URLEncoder.encode(message, "UTF-8") +
                            (errors.length() > 0 ? "&errorDetail=" + java.net.URLEncoder.encode(errors.toString(), "UTF-8") : ""));
                    return;
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/admin/warehouse?tab=products&error=import&msg=" +
                            java.net.URLEncoder.encode("Lỗi xử lý file: " + e.getMessage(), "UTF-8"));
                    return;
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/warehouse?tab=products&error=import&msg=" +
                        java.net.URLEncoder.encode("Chưa chọn file hoặc file rỗng", "UTF-8"));
                return;
            }
        }
        else if ("update_quantity".equals(action)) {
            // Handle quantity update
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int newQuantity = Integer.parseInt(request.getParameter("quantity"));
                String reason = request.getParameter("reason");
                
                Product product = productDAO.getProductById(productId);
                if (product != null) {
                    int oldQuantity = product.getQuantity();
                    product.setQuantity(newQuantity);
                    
                    boolean updated = productDAO.updateProduct(product);
                    if (updated) {
                        // Log the inventory change
                        utils.InventoryLogUtil.logInventoryChange(
                                product,
                                oldQuantity,
                                reason,
                                null,
                                "manual_adjustment");
                        
                        response.sendRedirect(request.getContextPath() + "/admin/warehouse?success=update&msg=" + 
                                java.net.URLEncoder.encode("Quantity updated successfully", "UTF-8"));
                        return;
                    }
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/warehouse?error=update&msg=" + 
                        java.net.URLEncoder.encode("Failed to update quantity", "UTF-8"));
                
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/warehouse?error=update&msg=" + 
                        java.net.URLEncoder.encode("Invalid number format", "UTF-8"));
            }
        } else {
            // Unknown action
            response.sendRedirect(request.getContextPath() + "/admin/warehouse");
        }
    }
    
    /**
     * Get category ID by name, creating a new category if it doesn't exist
     */
    private int getCategoryIdByName(String categoryName) {
        // Try to find existing category
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName.trim())) {
                return category.getCategoryId();
            }
        }
        
        // Create new category if not found
        Category newCategory = new Category();
        newCategory.setName(categoryName.trim());
        newCategory.setDescription("Auto-created category from warehouse import");
        newCategory.setStatus("active");
        
        categoryDAO.createCategory(newCategory);
        
        // Get the ID of the newly created category
        categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName.trim())) {
                return category.getCategoryId();
            }
        }
        
        return 1; // Default category ID if all else fails
    }

    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null || line.isEmpty()) return result;
        // Sử dụng split với limit = -1 để giữ trường rỗng cuối dòng
        String[] parts = line.split(",", -1);
        for (String part : parts) {
            result.add(part.trim());
        }
        return result;
    }

    // Helper để tìm index header
    private int findHeaderIndex(String[] headers, String name) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(name)) return i;
        }
        return -1;
    }
} 