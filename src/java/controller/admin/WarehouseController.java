package controller.admin;

import dao.impl.CategoryDAO;
import dao.impl.InventoryLogDAOImpl;
import dao.impl.ProductDAO;
import dao.interfaces.InventoryLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        // Handle any POST requests like filtering, etc.
        doGet(request, response);
    }

    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null || line.isEmpty()) return result;
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        return result;
    }
} 