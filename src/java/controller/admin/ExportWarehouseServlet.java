package controller.admin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import model.entity.InventoryLog;
import model.entity.Product;
import dao.impl.ProductDAO;
import dao.impl.InventoryLogDAOImpl;
import dao.interfaces.InventoryLogDAO;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Controller for exporting and importing warehouse data
 */
@WebServlet("/ExportWarehouseServlet")
@MultipartConfig
public class ExportWarehouseServlet extends HttpServlet {
    
    private ProductDAO productDAO;
    private InventoryLogDAO inventoryLogDAO;
    
    @Override
    public void init() {
        productDAO = new ProductDAO();
        inventoryLogDAO = new InventoryLogDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Determine what type of export (products or inventory)
        String exportType = request.getParameter("type");
        if (exportType == null || exportType.isEmpty()) {
            exportType = "products"; // Default to product export
        }
        
        try {
            // Reset buffer to make sure we have a clean start
            response.reset();
            
            // Check if this is a template request
            if ("template".equals(exportType)) {
                response.setContentType("text/csv; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=inventory_template.csv");
                exportTemplateToCSV(response);
                return;
            }
            
            // Set content type to CSV format
            response.setContentType("text/csv; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            // Set download filename based on type
            if ("products".equals(exportType)) {
                response.setHeader("Content-Disposition", "attachment; filename=products_export.csv");
                List<Product> products = productDAO.getAllProducts();
                try {
                    // Xuất danh sách sản phẩm trực tiếp thay vì dùng CSVExporter
                    exportProductsToCSV(products, response.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/admin/warehouse?error=export&msg=" + e.getMessage());
                }
            } else if ("inventory".equals(exportType)) {
                response.setHeader("Content-Disposition", "attachment; filename=inventory_logs_export.csv");
                
                // Get product ID filter if any
                String productIdParam = request.getParameter("productId");
                List<InventoryLog> logs;
                
                if (productIdParam != null && !productIdParam.isEmpty()) {
                    try {
                        int productId = Integer.parseInt(productIdParam);
                        logs = inventoryLogDAO.getInventoryLogsByProductId(productId);
                    } catch (NumberFormatException e) {
                        logs = inventoryLogDAO.getAllInventoryLogs();
                    }
                } else {
                    logs = inventoryLogDAO.getAllInventoryLogs();
                }
                
                // Export logs as CSV
                exportInventoryLogsToCSV(logs, response.getOutputStream());
            } else if ("excel".equals(exportType)) {
                exportWarehouseToExcel(response);
                return;
            } else if ("inventory_excel".equals(exportType)) {
                String productIdParam = request.getParameter("productId");
                List<InventoryLog> logs;
                if (productIdParam != null && !productIdParam.isEmpty()) {
                    try {
                        int productId = Integer.parseInt(productIdParam);
                        logs = inventoryLogDAO.getInventoryLogsByProductId(productId);
                    } catch (NumberFormatException e) {
                        logs = inventoryLogDAO.getAllInventoryLogs();
                    }
                } else {
                    logs = inventoryLogDAO.getAllInventoryLogs();
                }
                exportInventoryLogsToExcel(logs, response);
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/warehouse?error=export&msg=Invalid export type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Export error: " + e.getMessage());
            try {
                // Only redirect if headers haven't been committed
                if (!response.isCommitted()) {
                    response.sendRedirect(request.getContextPath() + "/admin/warehouse?error=export&msg=" + e.getMessage());
                }
            } catch (Exception ex) {
                // If we can't redirect, at least log the error
                System.err.println("Failed to redirect after error: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Export products to CSV format
     */
    private void exportProductsToCSV(List<Product> products, OutputStream outputStream) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        // Write UTF-8 BOM
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);
        
        try (OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(osw)) {
            
            // Ghi tiêu đề dưới dạng CSV chuẩn với dấu phẩy ngăn cách
            // Mỗi tiêu đề sẽ nằm ở một cột riêng trong Excel (A, B, C...)
            writer.println("ID,Sản Phẩm,SKU,SL Trước,SL Sau,Thay Đổi,Loại,Lý Do,Thời Gian");
            
            // Ghi dữ liệu sản phẩm
            for (Product product : products) {
                StringBuilder line = new StringBuilder();
                line.append(product.getProductId());
                line.append(",").append(escapeForCsv(product.getName()));
                line.append(",").append(escapeForCsv(product.getSku()));
                line.append(","); // SL Trước - để trống
                line.append(",").append(product.getQuantity()); // SL Sau
                line.append(","); // Thay Đổi - để trống
                line.append(",").append(escapeForCsv("Tăng")); // Mặc định là Tăng
                line.append(",").append(escapeForCsv("Nhập hàng")); // Lý do mặc định
                line.append(",").append(escapeForCsv(dateFormat.format(new java.util.Date()))); // Thời gian hiện tại
                
                writer.println(line.toString());
            }
            
            writer.flush();
            osw.flush();
        }
    }
    
    /**
     * Export inventory logs to CSV format
     */
    private void exportInventoryLogsToCSV(List<InventoryLog> logs, OutputStream outputStream) throws Exception {
        // Write UTF-8 BOM
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);
        
        try (OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(osw)) {
            
            // Ghi tiêu đề theo đúng định dạng để mỗi cột trong Excel hiển thị một tiêu đề
            writer.println("ID,Sản Phẩm,SKU,SL Trước,SL Sau,Thay Đổi,Loại,Lý Do,Thời Gian");
            
            // Chỉ xuất dữ liệu thực từ database
            if (logs != null && !logs.isEmpty()) {
                for (InventoryLog log : logs) {
                    int change = log.getQuantityAfter() - log.getQuantityBefore();
                    String changeType = "increase".equals(log.getChangeType()) ? "Tăng" : "Giảm";
                    
                    StringBuilder line = new StringBuilder();
                    line.append(log.getLogId());
                    line.append(",").append(escapeForCsv(log.getProductName()));
                    line.append(",").append(escapeForCsv(log.getProductSku()));
                    line.append(",").append(log.getQuantityBefore());
                    line.append(",").append(log.getQuantityAfter());
                    line.append(",").append(((change > 0) ? "+" : "")).append(change);
                    line.append(",").append(escapeForCsv(changeType));
                    line.append(",").append(escapeForCsv(log.getReason()));
                    line.append(",").append(escapeForCsv(log.getCreatedAt().toString()));
                    writer.println(line.toString());
                }
            }
            
            writer.flush();
            osw.flush();
        }
    }
    
    /**
     * Escape text for CSV (handle quotes and commas)
     */
    private String escapeForCsv(String input) {
        if (input == null) {
            return "";
        }
        
        // Với CSV sử dụng dấu phẩy làm dấu phân cách, phải bọc giá trị trong dấu ngoặc kép nếu có dấu phẩy hoặc dấu ngoặc kép
        String escaped = input.replace("\"", "\"\""); // Escape dấu ngoặc kép bằng cách gấp đôi
        
        // Nếu chuỗi chứa dấu phẩy, dấu ngoặc kép hoặc dòng mới, bọc nó trong dấu ngoặc kép
        if (escaped.contains(",") || escaped.contains("\"") || 
            escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        
        return escaped;
    }
    
    /**
     * Handle export functionality from POST request
     */
    private void handleExport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // This method simply redirects to the doGet method which already handles exports
        doGet(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // Xử lý phần export trước
        String action = request.getParameter("action");
        if (action != null && action.equals("export")) {
            handleExport(request, response);
            return;
        }
        
        // Xử lý import CSV
        String contentType = request.getContentType();
        List<String> errors = new ArrayList<>();
        String successMessage = null;
        int successCount = 0;
        
        System.out.println("Processing import, content type: " + contentType);
        
        // Check if request contains multipart content for file upload
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
            try {
                Part filePart = request.getPart("file");
                if (filePart != null) {
                    String fileName = getSubmittedFileName(filePart);
                    System.out.println("File uploaded: " + fileName);
                    
                    if (fileName == null || fileName.isEmpty()) {
                        errors.add("Không có tên file");
                    } else {
                        // Check file extension
                        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                        System.out.println("File extension: " + fileExtension);
                        
                        // Handle based on file type
                        if ("csv".equals(fileExtension) || "txt".equals(fileExtension)) {
                            // Process as CSV file
                            try (BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8))) {
                                System.out.println("Reading CSV file with UTF-8 encoding");
                                successCount = processCsvImport(reader, errors);
                            } catch (Exception e) {
                                System.out.println("Error processing CSV file: " + e.getMessage());
                                errors.add("Lỗi khi xử lý file CSV: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else if ("xlsx".equals(fileExtension) || "xls".equals(fileExtension)) {
                            // Process as Excel file
                            errors.add("Hiện chỉ hỗ trợ định dạng file CSV. Vui lòng xuất file Excel ra định dạng CSV trước khi tải lên.");
                            System.out.println("Excel format not supported yet");
                        } else {
                            errors.add("Định dạng file không được hỗ trợ. Chỉ chấp nhận file .csv");
                            System.out.println("Unsupported file format: " + fileExtension);
                        }
                        
                        if (successCount > 0) {
                            successMessage = successCount + " bản ghi đã được nhập thành công";
                        } else if (errors.isEmpty()) {
                            errors.add("Không có bản ghi nào được nhập. Kiểm tra định dạng file và dữ liệu.");
                        }
                    }
                } else {
                    errors.add("Không có file nào được chọn");
                    System.out.println("No file part in the request");
                }
            } catch (Exception e) {
                errors.add("Lỗi khi xử lý file: " + e.getMessage());
                System.out.println("Error processing file upload: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            errors.add("Yêu cầu không đúng định dạng để tải file lên");
            System.out.println("Not a multipart/form-data request");
        }
        
        // Redirect to the warehouse page with appropriate parameters
        StringBuilder redirectUrl = new StringBuilder(request.getContextPath() + "/admin/warehouse?tab=products");
        
        if (!errors.isEmpty()) {
            redirectUrl.append("&error=import");
            redirectUrl.append("&msg=").append(java.net.URLEncoder.encode(String.join("; ", errors), "UTF-8"));
        } else if (successMessage != null) {
            redirectUrl.append("&success=import");
            redirectUrl.append("&msg=").append(java.net.URLEncoder.encode(successMessage, "UTF-8"));
            redirectUrl.append("&successCount=").append(successCount);
        }
        
        response.sendRedirect(redirectUrl.toString());
    }
    
    /**
     * Extract the filename from a multipart request's part
     */
    private String getSubmittedFileName(Part part) {
        System.out.println("Getting filename from part: " + part.getName());
        System.out.println("Content disposition: " + part.getHeader("content-disposition"));
        
        // Different browsers/clients may use different header formats
        for (String cd : part.getHeader("content-disposition").split(";")) {
            cd = cd.trim();
            System.out.println("  Content disposition part: [" + cd + "]");
            
            if (cd.startsWith("filename=")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim();
                if (filename.startsWith("\"") && filename.endsWith("\"")) {
                    filename = filename.substring(1, filename.length() - 1);
                }
                System.out.println("  Extracted filename: [" + filename + "]");
                return filename;
            } else if (cd.startsWith("filename*=")) {
                // Handle RFC 5987 encoding
                String encodedFilename = cd.substring(cd.indexOf('=') + 1).trim();
                System.out.println("  Encoded filename: [" + encodedFilename + "]");
                
                try {
                    // Extract charset and encoded part
                    if (encodedFilename.contains("''")) {
                        String[] parts = encodedFilename.split("''");
                        String charset = parts[0].substring(parts[0].indexOf('\'') + 1);
                        String encodedName = parts[1];
                        
                        // URLDecode and convert to UTF-8
                        String decodedName = URLDecoder.decode(encodedName, charset);
                        System.out.println("  Decoded filename: [" + decodedName + "]");
                        return decodedName;
                    }
                } catch (Exception e) {
                    System.out.println("  Error decoding filename: " + e.getMessage());
                }
            }
        }
        
        // If can't extract, try the old method that worked before
        String filename = part.getSubmittedFileName();
        if (filename != null) {
            System.out.println("  Using getSubmittedFileName(): [" + filename + "]");
            return filename;
        }
        
        // Last resort: get the last segment of the path parameter if present
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                String path = item.substring(item.indexOf("=") + 2, item.length() - 1);
                int slash = path.lastIndexOf('/');
                if (slash < 0) {
                    slash = path.lastIndexOf('\\');
                }
                if (slash >= 0) {
                    path = path.substring(slash + 1);
                }
                System.out.println("  Extracted from path: [" + path + "]");
                return path;
            }
        }
        
        return null; // Filename could not be determined
    }
    
    /**
     * Import inventory data from Excel file
     * @param filePart The uploaded file part
     * @param errors List to store any errors encountered
     * @return Number of successfully imported records
     */
    private int importInventoryFromExcel(Part filePart, List<String> errors) throws Exception {
        int successCount = 0;
        String fileName = getFileName(filePart);
        String fileContent = "";
        
        System.out.println("Starting to process uploaded file: " + fileName);
        
        // First, try to read some of the file content to determine format
        try (InputStream sampleStream = filePart.getInputStream();
             BufferedReader sampleReader = new BufferedReader(new InputStreamReader(sampleStream, "UTF-8"))) {
            
            StringBuilder sampleContent = new StringBuilder();
            String line;
            int lineCount = 0;
            
            while ((line = sampleReader.readLine()) != null && lineCount < 5) {
                sampleContent.append(line).append("\n");
                lineCount++;
            }
            
            fileContent = sampleContent.toString();
            System.out.println("Sample file content:");
            System.out.println(fileContent);
        }
        
        // Special handling for CSV files
        if (fileName.toLowerCase().endsWith(".csv")) {
            System.out.println("Processing as CSV format because file has .csv extension");
            try (InputStream inputStream = filePart.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                return processCsvImport(reader, errors);
            }
        }
        
        // Determine file type based on content and extension for other files
        boolean isHtml = fileContent.toLowerCase().contains("<html") || 
                          fileContent.toLowerCase().contains("<table") ||
                          fileContent.toLowerCase().contains("<tr>");
        
        boolean isCsv = fileContent.contains(",") || 
                         fileContent.contains(";");
        
        boolean isTabSeparated = fileContent.contains("\t");
        
        boolean isExcel = fileName.toLowerCase().endsWith(".xlsx") || 
                           fileName.toLowerCase().endsWith(".xls");
        
        boolean isSrv = fileName.toLowerCase().endsWith(".srv");
        
        System.out.println("File type detection:");
        System.out.println("- isHtml: " + isHtml);
        System.out.println("- isCsv: " + isCsv);
        System.out.println("- isTabSeparated: " + isTabSeparated);
        System.out.println("- isExcel: " + isExcel);
        System.out.println("- isSrv: " + isSrv);
        
        // Process based on detected format
        try {
            if (isHtml) {
                System.out.println("Processing as HTML format");
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                    successCount = processHtmlImport(reader, errors);
                }
            } else if (isCsv || isTabSeparated) {
                System.out.println("Processing as CSV/Tab-separated format");
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                    successCount = processCsvImport(reader, errors);
                }
            } else if (isExcel) {
                System.out.println("Processing as Excel format");
                // Thử phương pháp đọc đơn giản trước
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                    
                    successCount = processSimpleExcelImport(reader, errors);
                    
                    if (successCount == 0) {
                        // Nếu không thành công, thử phương pháp binary
                        System.out.println("Simple Excel import failed, trying binary format");
                        try (InputStream binaryStream = filePart.getInputStream()) {
                            successCount = processExcelAsBinary(binaryStream, errors);
                        }
                    }
                }
            } else if (isSrv) {
                // Use the new .srv file import method
                return importSrvFile(filePart, errors);
            } else {
                // If we can't determine format, try all methods in sequence
                System.out.println("Unknown format, trying all import methods");
                
                // First try as CSV
                try (InputStream inputStream = filePart.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                    successCount = processCsvImport(reader, errors);
                }
                
                // If CSV didn't work, try simple Excel import
                if (successCount == 0) {
                    try (InputStream inputStream = filePart.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                        successCount = processSimpleExcelImport(reader, errors);
                    }
                }
                
                // If still not successful, try Excel binary
                if (successCount == 0) {
                    try (InputStream inputStream = filePart.getInputStream()) {
                        successCount = processExcelAsBinary(inputStream, errors);
                    }
                }
                
                // Last resort, try HTML
                if (successCount == 0) {
                    try (InputStream inputStream = filePart.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                        successCount = processHtmlImport(reader, errors);
                    }
                }
            }
            
            if (successCount == 0) {
                errors.add("Không thể đọc file. Vui lòng đảm bảo file có định dạng CSV, HTML hoặc Excel đơn giản.");
                System.out.println("All import methods failed");
            } else {
                System.out.println("Import successful: " + successCount + " records processed");
            }
        } catch (Exception e) {
            errors.add("Lỗi xử lý file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return successCount;
    }
    
    /**
     * Phương thức đơn giản để đọc file Excel, thích hợp với các file định dạng cơ bản
     */
    private int processSimpleExcelImport(BufferedReader reader, List<String> errors) throws Exception {
        int successCount = 0;
        
        // Đọc từng dòng của file
        String line;
        boolean isFirstLine = true;
        boolean foundHeader = false;
        
        System.out.println("Starting simple Excel import process");
        
        while ((line = reader.readLine()) != null) {
            // Debug: In ra dòng đang đọc để kiểm tra
            System.out.println("Reading line: " + line);
            
            // Bỏ qua dòng đầu tiên nếu nó không phải là header
            if (isFirstLine) {
                isFirstLine = false;
                // Kiểm tra xem đây có phải là dòng header không
                if (line.contains("ID") && line.contains("SKU") &&
                    (line.contains("SL Trước") || line.contains("SL Sau"))) {
                    foundHeader = true;
                }
                continue;
            }
            
            // Nếu chưa tìm thấy header, kiểm tra xem dòng hiện tại có phải là header không
            if (!foundHeader) {
                if (line.contains("ID") && line.contains("SKU") &&
                    (line.contains("SL Trước") || line.contains("SL Sau"))) {
                    foundHeader = true;
                    continue;
                }
            }
            
            // Bỏ qua các dòng trống
            if (line.trim().isEmpty()) {
                continue;
            }
            
            // Chỉ xử lý nếu đã tìm thấy header và dòng hiện tại có vẻ là dữ liệu
            if (foundHeader) {
                // Chuyển dòng thành các ô dữ liệu
                List<String> cells = new ArrayList<>();
                
                // Tách dòng dựa trên tab, dấu phẩy hoặc khoảng trắng
                String[] parts;
                if (line.contains("\t")) {
                    // Debug: Hiển thị nếu phát hiện định dạng tab
                    System.out.println("Detected tab-separated format");
                    parts = line.split("\t");
                } else if (line.contains(",")) {
                    parts = line.split(",");
                } else {
                    parts = line.split("\\s+");
                }
                
                // Debug: In ra số lượng phần tử sau khi tách
                System.out.println("Number of parts: " + parts.length);
                
                // Thêm các phần tử vào danh sách cells
                for (int i = 0; i < parts.length; i++) {
                    String part = parts[i].trim();
                    cells.add(part);
                    // Debug: In ra từng phần tử
                    System.out.println("Part " + i + ": '" + part + "'");
                }
                
                // Kiểm tra xem dòng có đủ thông tin không
                if (cells.size() >= 3) {
                    try {
                        // Extract data
                        String idStr = cells.get(0).trim();
                        String productName = cells.size() > 1 ? cells.get(1).trim() : "";
                        String sku = cells.size() > 2 ? cells.get(2).trim() : "";
                        
                        // Debug: In ra thông tin SKU và tên sản phẩm
                        System.out.println("Processing product: " + productName + " (SKU: " + sku + ")");
                        
                        // Bỏ qua dòng không có SKU hoặc tên sản phẩm
                        if (sku.isEmpty() || productName.isEmpty()) {
                            System.out.println("Skipping row: Empty SKU or product name");
                            continue;
                        }
                        
                        // Lấy thông tin về số lượng
                        String qtyBeforeStr = cells.size() > 3 ? cells.get(3).trim() : "";
                        int qtyBefore = 0;
                        boolean isQtyBeforeEmpty = qtyBeforeStr.isEmpty();
                        
                        // Debug: In ra thông tin về SL Trước
                        System.out.println("SL Trước: '" + qtyBeforeStr + "', empty: " + isQtyBeforeEmpty);
                        
                        if (!isQtyBeforeEmpty) {
                            qtyBefore = parseIntSafely(qtyBeforeStr);
                        }
                        
                        String qtyAfterStr = cells.size() > 4 ? cells.get(4).trim() : "";
                        int qtyAfter = 0;
                        boolean isQtyAfterEmpty = qtyAfterStr.isEmpty();
                        
                        // Debug: In ra thông tin về SL Sau
                        System.out.println("SL Sau: '" + qtyAfterStr + "', empty: " + isQtyAfterEmpty);
                        
                        if (!isQtyAfterEmpty) {
                            qtyAfter = parseIntSafely(qtyAfterStr);
                        }
                        
                        // Lấy thông tin về thay đổi và loại
                        String changeStr = cells.size() > 5 ? cells.get(5).trim() : "";
                        String changeTypeText = cells.size() > 6 ? cells.get(6).trim() : "";
                        
                        // Debug: In ra thông tin về thay đổi và loại
                        System.out.println("Thay đổi: '" + changeStr + "', Loại: '" + changeTypeText + "'");
                        
                        int changeAmount = 0;
                        if (!changeStr.isEmpty()) {
                            try {
                                changeAmount = parseIntSafely(changeStr);
                                System.out.println("Parsed change amount: " + changeAmount);
                            } catch (NumberFormatException e) {
                                System.out.println("Failed to parse change amount: " + e.getMessage());
                            }
                        }
                        
                        // Lấy lý do
                        String reason = cells.size() > 7 ? cells.get(7).trim() : "";
                        
                        // Tìm sản phẩm theo SKU
                        Product product = findProductBySku(sku);
                        
                        if (product != null) {
                            System.out.println("Found product in database: " + product.getName() + ", current quantity: " + product.getQuantity());
                        } else {
                            System.out.println("Product not found in database, will try to create new");
                            
                            // Nếu sản phẩm không tồn tại, tạo mới (nếu có đủ dữ liệu)
                            if (!sku.isEmpty() && !productName.isEmpty()) {
                                // Sử dụng SL Sau nếu có, nếu không thì dùng SL Trước + Thay đổi
                                int initialQty = !isQtyAfterEmpty ? qtyAfter : 
                                               (!isQtyBeforeEmpty ? qtyBefore + changeAmount : changeAmount);
                                
                                product = createNewProduct(sku, productName, initialQty, errors);
                                if (product == null) {
                                    System.out.println("Failed to create new product, skipping row");
                                    continue; // Skip if product creation failed
                                }
                            } else {
                                errors.add("Sản phẩm với mã SKU " + sku + " không tồn tại. Đảm bảo cung cấp cả SKU và tên sản phẩm để hệ thống có thể tự tạo mới.");
                                System.out.println("Product not found and cannot create new, skipping row");
                                continue;
                            }
                        }
                        
                        // Nếu SL Trước để trống, lấy số lượng hiện tại từ database
                        if (isQtyBeforeEmpty) {
                            qtyBefore = product.getQuantity();
                            System.out.println("Quantity before is empty, using current database quantity: " + qtyBefore);
                        }
                        
                        // Nếu SL Sau để trống hoặc bằng 0, giữ nguyên số lượng hiện tại
                        if (isQtyAfterEmpty || qtyAfter == 0) {
                            qtyAfter = product.getQuantity();
                            System.out.println("Quantity after is empty or zero, using current database quantity: " + qtyAfter);
                        }
                        
                        // Kiểm tra xem có sự thay đổi số lượng không
                        if (qtyBefore == qtyAfter) {
                            // Không có thay đổi, bỏ qua dòng này
                            System.out.println("No quantity change detected, skipping log creation for SKU: " + sku);
                            continue;
                        }
                        
                        // Determine change type based on quantities
                        String changeType = qtyAfter > qtyBefore ? "increase" : "decrease";
                        System.out.println("Quantity change detected: " + qtyBefore + " -> " + qtyAfter + ", Change Type: " + changeType);
                        
                        // Create and insert log
                        InventoryLog log = new InventoryLog();
                        log.setProductId(product.getProductId());
                        log.setQuantityBefore(qtyBefore);
                        log.setQuantityAfter(qtyAfter);
                        log.setChangeType(changeType);
                        log.setReason(reason);
                        log.setCreatedAt(LocalDateTime.now());
                        
                        // Set product name and SKU for display
                        log.setProductName(product.getName());
                        log.setProductSku(product.getSku());
                        
                        // Insert the log
                        boolean result = inventoryLogDAO.insertInventoryLog(log);
                        if (result) {
                            successCount++;
                            System.out.println("Successfully added inventory log");
                            
                            // Update product quantity to match the imported quantity
                            product.setQuantity(qtyAfter);
                            boolean updateResult = productDAO.updateProduct(product);
                            System.out.println("Updated product quantity to " + qtyAfter + ", result: " + updateResult);
                        } else {
                            errors.add("Failed to insert log for SKU " + sku);
                            System.out.println("Failed to add inventory log");
                        }
                    } catch (Exception e) {
                        errors.add("Error processing row: " + e.getMessage() + " - " + line);
                        System.out.println("Exception processing row: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        
        System.out.println("Simple Excel import completed, success count: " + successCount);
        return successCount;
    }
    
    /**
     * Process import data from HTML format
     */
    private int processHtmlImport(BufferedReader reader, List<String> errors) throws Exception {
        int successCount = 0;
        
        String line;
        boolean foundTable = false;
        boolean foundHeaders = false;
        
        // First, read until we find the table and headers
        while ((line = reader.readLine()) != null) {
            if (!foundTable && line.contains("<table")) {
                foundTable = true;
                continue;
            }
            
            if (foundTable && line.contains("<tr") && line.contains("<th")) {
                foundHeaders = true;
                break;
            }
        }
        
        if (!foundHeaders) {
            errors.add("Could not find table headers in uploaded file");
            return 0;
        }
        
        // Now read and process data rows
        StringBuilder currentRow = new StringBuilder();
        boolean inRow = false;
        
        while ((line = reader.readLine()) != null) {
            if (line.contains("<tr")) {
                inRow = true;
                currentRow = new StringBuilder();
                currentRow.append(line);
            } else if (inRow && line.contains("</tr>")) {
                currentRow.append(line);
                
                // Process completed row
                try {
                    List<String> cells = extractCellsFromHtml(currentRow.toString());
                    
                    // Skip empty rows
                    if (cells.size() < 3) {
                        continue;
                    }
                    
                    // Process based on row structure
                    // If first cell is "ID" or other header text, skip it (it's a header row)
                    if (cells.get(0).equalsIgnoreCase("ID") || cells.get(0).equalsIgnoreCase("Sản Phẩm")) {
                        continue;
                    }
                    
                    // Extract data - columns might vary by import format
                    String sku = cells.size() > 2 ? cells.get(2).trim() : "";
                    String productName = cells.size() > 1 ? cells.get(1).trim() : "";
                    String reason = "";
                    
                    // Extract quantity data
                    String qtyBeforeStr = cells.size() > 3 ? cells.get(3).trim() : "";
                    int qtyBefore = 0;
                    boolean isQtyBeforeEmpty = qtyBeforeStr.isEmpty();
                    
                    if (!isQtyBeforeEmpty) {
                        try {
                            qtyBefore = Integer.parseInt(qtyBeforeStr);
                        } catch (NumberFormatException e) {
                            qtyBefore = 0;
                            isQtyBeforeEmpty = true;
                        }
                    }
                    
                    String qtyAfterStr = cells.size() > 4 ? cells.get(4).trim() : "";
                    int qtyAfter = 0;
                    boolean isQtyAfterEmpty = qtyAfterStr.isEmpty();
                    
                    try {
                        if (!isQtyAfterEmpty) {
                            qtyAfter = Integer.parseInt(qtyAfterStr);
                        }
                        reason = cells.size() > 7 ? cells.get(7).trim() : "";
                    } catch (NumberFormatException e) {
                        errors.add("Invalid quantity format in row: " + currentRow.toString());
                        continue;
                    }
                    
                    // Find product by SKU
                    Product product = findProductBySku(sku);
                    
                    // If product doesn't exist, create it (if we have enough data)
                    if (product == null && !sku.isEmpty() && !productName.isEmpty()) {
                        product = createNewProduct(sku, productName, qtyAfter, errors);
                        if (product == null) {
                            continue; // Skip if product creation failed
                        }
                    } else if (product == null) {
                        errors.add("Sản phẩm với mã SKU " + sku + " không tồn tại. Đảm bảo cung cấp cả SKU và tên sản phẩm để hệ thống có thể tự tạo mới.");
                        continue;
                    }
                    
                    // Nếu SL Trước để trống, lấy số lượng hiện tại từ database
                    if (isQtyBeforeEmpty) {
                        qtyBefore = product.getQuantity();
                        System.out.println("Quantity before is empty, using current database quantity: " + qtyBefore);
                    }
                    
                    // Nếu SL Sau để trống hoặc bằng 0, giữ nguyên số lượng hiện tại
                    if (isQtyAfterEmpty || qtyAfter == 0) {
                        qtyAfter = product.getQuantity();
                        System.out.println("Quantity after is empty or zero, using current database quantity: " + qtyAfter);
                    }
                    
                    // Kiểm tra xem có sự thay đổi số lượng không
                    if (qtyBefore == qtyAfter) {
                        // Không có thay đổi, bỏ qua dòng này
                        System.out.println("No quantity change detected, skipping log creation for SKU: " + sku);
                        continue;
                    }
                    
                    // Determine change type based on quantities
                    String changeType = qtyAfter > qtyBefore ? "increase" : "decrease";
                    System.out.println("Quantity change detected: " + qtyBefore + " -> " + qtyAfter + ", Change Type: " + changeType);
                    
                    // Create and insert log
                    InventoryLog log = new InventoryLog();
                    log.setProductId(product.getProductId());
                    log.setQuantityBefore(qtyBefore);
                    log.setQuantityAfter(qtyAfter);
                    log.setChangeType(changeType);
                    log.setReason(reason);
                    log.setCreatedAt(LocalDateTime.now());
                    
                    // Set product name and SKU for display
                    log.setProductName(product.getName());
                    log.setProductSku(product.getSku());
                    
                    // Insert the log
                    boolean result = inventoryLogDAO.insertInventoryLog(log);
                    if (result) {
                        successCount++;
                        
                        // Update product quantity to match the imported quantity
                        product.setQuantity(qtyAfter);
                        productDAO.updateProduct(product);
                    } else {
                        errors.add("Failed to insert log for SKU " + sku);
                    }
                } catch (Exception e) {
                    errors.add("Error processing row: " + e.getMessage() + " - " + currentRow.toString());
                }
                
                inRow = false;
            } else if (inRow) {
                currentRow.append(line);
            }
        }
        
        return successCount;
    }
    
    /**
     * Process import data from CSV format
     */
    private int processCsvImport(BufferedReader reader, List<String> errors) throws Exception {
        int successCount = 0;
        
        String line;
        boolean isFirstLine = true;
        
        System.out.println("Starting CSV import process");
        
        while ((line = reader.readLine()) != null) {
            // Debug: In ra dòng đang đọc
            System.out.println("CSV Line: " + line);
            
            // Skip UTF-8 BOM if present
            if (isFirstLine && line.startsWith("\uFEFF")) {
                line = line.substring(1);
                System.out.println("Removed BOM marker");
            }
            
            // Skip empty lines
            if (line.trim().isEmpty()) {
                System.out.println("Skipping empty line");
                continue;
            }
            
            // Skip header row if this is the first line
            if (isFirstLine) {
                isFirstLine = false;
                System.out.println("Skipping header row: " + line);
                continue;
            }
            
            try {
                // Parse CSV row
                List<String> cells = parseCSVLine(line);
                
                // Debug: In ra số lượng ô và nội dung
                System.out.println("CSV cells count: " + cells.size());
                for (int i = 0; i < Math.min(cells.size(), 10); i++) {
                    System.out.println("  Cell " + i + ": '" + (i < cells.size() ? cells.get(i) : "") + "'");
                }
                
                // Skip if not enough data (need at least ID, product name and SKU)
                if (cells.size() < 3) {
                    System.out.println("Not enough columns, skipping row");
                    continue;
                }
                
                // Extract data based on Excel format in the image (exact matching with image)
                // Expected columns: ID | Sản Phẩm | SKU | SL Trước | SL Sau | Thay Đổi | Loại | Lý Do | Thời Gian
                String productName = "";
                String sku = "";
                String slSau = "";
                String thayDoi = "";
                String loai = "";
                String lyDo = "";
                
                // Map columns correctly based on the provided image
                if (cells.size() >= 3) {
                    // ID is at index 0, but we don't use it directly
                    productName = cells.size() > 1 ? cells.get(1).trim() : "";
                    sku = cells.size() > 2 ? cells.get(2).trim() : "";
                }
                
                // Get quantity fields
                if (cells.size() >= 5) {
                    slSau = cells.get(4).trim(); // SL Sau at index 4
                }
                
                if (cells.size() >= 6) {
                    thayDoi = cells.get(5).trim(); // Thay Đổi at index 5
                }
                
                if (cells.size() >= 7) {
                    loai = cells.get(6).trim(); // Loại at index 6
                }
                
                if (cells.size() >= 8) {
                    lyDo = cells.get(7).trim(); // Lý Do at index 7
                }
                
                System.out.println("Extracted data:");
                System.out.println("  Product Name: '" + productName + "'");
                System.out.println("  SKU: '" + sku + "'");
                System.out.println("  SL Sau: '" + slSau + "'");
                System.out.println("  Thay Đổi: '" + thayDoi + "'");
                System.out.println("  Loại: '" + loai + "'");
                System.out.println("  Lý Do: '" + lyDo + "'");
                
                // Validate SKU
                if (sku.isEmpty()) {
                    System.out.println("Empty SKU, skipping row");
                    continue;
                }
                
                // Find product in database by SKU
                Product product = findProductBySku(sku);
                if (product == null) {
                    System.out.println("Product not found in database with SKU: " + sku);
                    
                    // Create a new product if we have a SKU (name can be generated if missing)
                    String newProductName = productName.isEmpty() ? "Sản phẩm " + sku : productName;
                    System.out.println("Creating new product with SKU: " + sku + ", Name: " + newProductName);
                    
                    try {
                        // Parse initial quantity
                        int initialQuantity = 0;
                        if (!slSau.isEmpty()) {
                            initialQuantity = parseIntSafely(slSau);
                        } else if (!thayDoi.isEmpty()) {
                            initialQuantity = parseIntSafely(thayDoi);
                            // If it's a negative change, we'll start from 0
                            if (initialQuantity < 0) initialQuantity = 0;
                        }
                        
                        // Create the new product
                        product = createNewProduct(sku, newProductName, initialQuantity, errors);
                        if (product == null) {
                            errors.add("Không thể tạo sản phẩm mới với SKU " + sku);
                            continue;
                        }
                        System.out.println("Successfully created new product: " + product.getProductId());
                    } catch (Exception e) {
                        System.out.println("Error creating new product: " + e.getMessage());
                        errors.add("Lỗi khi tạo sản phẩm mới với SKU " + sku + ": " + e.getMessage());
                        continue;
                    }
                }
                
                System.out.println("Working with product: " + product.getName() + ", SKU: " + product.getSku() + ", Current Quantity: " + product.getQuantity());
                
                // Set SL Trước to current quantity in database
                int qtyBefore = product.getQuantity();
                System.out.println("Using current database quantity as SL Trước: " + qtyBefore);
                
                // Parse SL Sau
                int qtyAfter = 0;
                boolean hasQtyAfter = !slSau.isEmpty();
                
                if (hasQtyAfter) {
                    try {
                        qtyAfter = parseIntSafely(slSau);
                        System.out.println("Parsed SL Sau: " + qtyAfter);
                    } catch (Exception e) {
                        System.out.println("Error parsing SL Sau: " + e.getMessage());
                        errors.add("Lỗi khi đọc SL Sau cho SKU " + sku + ": " + e.getMessage());
                        continue;
                    }
                } else if (!thayDoi.isEmpty()) {
                    // If SL Sau is empty, use Thay Đổi to calculate it
                    try {
                        int change = parseIntSafely(thayDoi);
                        qtyAfter = qtyBefore + change;
                        System.out.println("Calculated SL Sau from Thay Đổi: " + qtyBefore + " + (" + change + ") = " + qtyAfter);
                    } catch (Exception e) {
                        System.out.println("Error parsing Thay Đổi: " + e.getMessage());
                        errors.add("Lỗi khi đọc Thay Đổi cho SKU " + sku + ": " + e.getMessage());
                        continue;
                    }
                } else {
                    // No quantity information provided
                    System.out.println("No quantity information provided, skipping row");
                    errors.add("Không có thông tin số lượng cho SKU " + sku);
                    continue;
                }
                
                // Check if there's any change in quantity
                if (qtyBefore == qtyAfter) {
                    System.out.println("No change in quantity for SKU " + sku + " (" + qtyBefore + " = " + qtyAfter + "), skipping");
                    continue;
                }
                
                // Determine change type from Loại column or from quantity comparison
                String changeType;
                if (!loai.isEmpty()) {
                    if (loai.equalsIgnoreCase("Tăng")) {
                        changeType = "increase";
                    } else if (loai.equalsIgnoreCase("Giảm")) {
                        changeType = "decrease";
                    } else {
                        // Default based on quantity comparison
                        changeType = qtyAfter > qtyBefore ? "increase" : "decrease";
                    }
                } else {
                    // If Loại is empty, determine from quantity change
                    changeType = qtyAfter > qtyBefore ? "increase" : "decrease";
                }
                
                System.out.println("Change type: " + changeType + " (" + qtyBefore + " -> " + qtyAfter + ")");
                
                // Create inventory log
                InventoryLog log = new InventoryLog();
                log.setProductId(product.getProductId());
                log.setProductName(product.getName());
                log.setProductSku(product.getSku());
                log.setQuantityBefore(qtyBefore);
                log.setQuantityAfter(qtyAfter);
                log.setChangeType(changeType);
                log.setReason(lyDo);
                log.setCreatedAt(LocalDateTime.now());
                
                // Insert the log
                boolean result = inventoryLogDAO.insertInventoryLog(log);
                if (result) {
                    successCount++;
                    System.out.println("Successfully added inventory log for SKU " + sku);
                    
                    // Update product quantity
                    product.setQuantity(qtyAfter);
                    boolean updateResult = productDAO.updateProduct(product);
                    System.out.println("Updated product quantity to " + qtyAfter + ", result: " + updateResult);
                } else {
                    errors.add("Không thể thêm log cho SKU " + sku);
                    System.out.println("Failed to add inventory log for SKU " + sku);
                }
            } catch (Exception e) {
                errors.add("Lỗi xử lý dòng: " + e.getMessage() + " - " + line);
                System.out.println("Error processing CSV line: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("CSV import completed, success count: " + successCount);
        return successCount;
    }
    
    /**
     * Detect the most likely delimiter in a CSV line
     */
    private char detectDelimiter(String line) {
        // Count occurrences of common delimiters
        int commaCount = countChar(line, ',');
        int semicolonCount = countChar(line, ';');
        int tabCount = countChar(line, '\t');
        
        System.out.println("Delimiter counts - Comma: " + commaCount + ", Semicolon: " + semicolonCount + ", Tab: " + tabCount);
        
        // If there are quotes, we need to count delimiters only outside quotes
        if (line.indexOf('"') >= 0) {
            boolean inQuotes = false;
            int commaOutsideQuotes = 0;
            int semicolonOutsideQuotes = 0;
            int tabOutsideQuotes = 0;
            
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                
                if (c == '"') {
                    // Handle escaped quotes
                    if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        i++; // Skip the next quote
                    } else {
                        inQuotes = !inQuotes;
                    }
                } else if (!inQuotes) {
                    if (c == ',') commaOutsideQuotes++;
                    else if (c == ';') semicolonOutsideQuotes++;
                    else if (c == '\t') tabOutsideQuotes++;
                }
            }
            
            System.out.println("Delimiters outside quotes - Comma: " + commaOutsideQuotes + 
                             ", Semicolon: " + semicolonOutsideQuotes + 
                             ", Tab: " + tabOutsideQuotes);
            
            commaCount = commaOutsideQuotes;
            semicolonCount = semicolonOutsideQuotes;
            tabCount = tabOutsideQuotes;
        }
        
        // Choose the most frequent delimiter
        if (commaCount > 0 && commaCount >= semicolonCount && commaCount >= tabCount) {
            return ',';
        } else if (semicolonCount > 0 && semicolonCount >= commaCount && semicolonCount >= tabCount) {
            return ';';
        } else if (tabCount > 0) {
            return '\t';
        }
        
        // If no delimiter found but the line has spaces, it might be space-separated
        // but be careful - regular text also has spaces
        if (line.contains(" ")) {
            // If we have multiple consecutive spaces that might separate data
            if (line.contains("  ") || countChar(line, ' ') > 5) {
                return ' ';
            }
        }
        
        // No obvious delimiter, default to comma
        return ',';
    }
    
    /**
     * Parse a CSV line into a list of values, handling quoted values properly
     * and supporting multiple delimiter types (comma, semicolon, tab)
     */
    private List<String> parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        
        // If line is empty, return empty list
        if (line == null || line.trim().isEmpty()) {
            return values;
        }
        
        // Special case for Excel-exported tab-separated values without quotes
        if (line.contains("\t")) {
            String[] parts = line.split("\t");
            for (String part : parts) {
                values.add(part.trim());
            }
            System.out.println("Parsed as tab-separated values: " + values.size() + " columns");
            return values;
        }
        
        // First try to determine the delimiter
        char delimiter = detectDelimiter(line);
        System.out.println("Detected delimiter: " + (delimiter == ',' ? "COMMA" : 
                                                  delimiter == ';' ? "SEMICOLON" : 
                                                  delimiter == '\t' ? "TAB" : 
                                                  delimiter == ' ' ? "SPACE" : "OTHER"));
        
        // Special case for space-separated data
        if (delimiter == ' ' && !line.contains("\"")) {
            // This could be space-separated or just text with spaces
            // Try to split by multiple spaces which is more likely for Excel exports
            String[] parts = line.split("\\s{2,}");
            if (parts.length > 2) {
                // Seems like a valid space-separated format
                for (String part : parts) {
                    values.add(part.trim());
                }
                System.out.println("Parsed as space-separated values: " + values.size() + " columns");
                return values;
            }
            
            // Try regular expression to match data columns that might be separated by spaces
            // This is a more aggressive approach and may not work in all cases
            parts = line.split("\\s+(?=\\d+|[A-Za-z]{2}\\d+|Tăng|Giảm)");
            if (parts.length > 2) {
                for (String part : parts) {
                    values.add(part.trim());
                }
                System.out.println("Parsed with regex space separation: " + values.size() + " columns");
                return values;
            }
        }
        
        // If we couldn't find a good delimiter and can't parse as space-separated,
        // use a simple split by delimiter
        if (delimiter == 0 || delimiter == ' ') {
            System.out.println("No clear delimiter found, trying simple comma split");
            String[] parts = line.split(",");
            for (String part : parts) {
                values.add(part.trim());
            }
            return values;
        }
        
        // Standard CSV parsing with quotes handling
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // If we're already in quotes and the next char is also a quote, it's an escaped quote
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentValue.append('"');
                    i++; // Skip the next quote
                } else {
                    // Toggle quote mode
                    inQuotes = !inQuotes;
                }
            } else if (c == delimiter && !inQuotes) {
                // End of value
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                // Regular character
                currentValue.append(c);
            }
        }
        
        // Add the last value
        values.add(currentValue.toString().trim());
        
        // Debug output
        System.out.println("Parsed " + values.size() + " values from line using standard CSV parsing");
        
        return values;
    }
    
    /**
     * Trích xuất văn bản từ file Excel binary
     */
    private String extractTextFromExcelBinary(byte[] excelBytes) {
        StringBuilder result = new StringBuilder();
        
        try {
            // Tìm các chuỗi văn bản trong file Excel
            String content = new String(excelBytes, "UTF-8");
            
            // Tìm các ô có chứa dữ liệu
            int index = 0;
            while ((index = content.indexOf("<v>", index)) != -1) {
                int endIndex = content.indexOf("</v>", index);
                if (endIndex != -1) {
                    String cellValue = content.substring(index + 3, endIndex);
                    result.append(cellValue).append(",");
                    index = endIndex;
                } else {
                    break;
                }
            }
            
            // Nếu không tìm thấy dữ liệu theo định dạng XML, thử phương pháp khác
            if (result.length() == 0) {
                // Tìm các chuỗi có thể là dữ liệu
                for (int i = 0; i < excelBytes.length - 4; i++) {
                    // Kiểm tra xem có phải là chuỗi ASCII không
                    if (excelBytes[i] >= 32 && excelBytes[i] <= 126) {
                        int charCount = 0;
                        int j = i;
                        while (j < excelBytes.length && excelBytes[j] >= 32 && excelBytes[j] <= 126) {
                            charCount++;
                            j++;
                        }
                        
                        // Nếu chuỗi đủ dài, có thể là dữ liệu
                        if (charCount > 3) {
                            String text = new String(excelBytes, i, charCount, "UTF-8");
                            result.append(text).append("\n");
                            i = j - 1;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return result.toString();
    }
    
    /**
     * Xử lý file Excel bằng cách đọc dưới dạng binary
     */
    private int processExcelAsBinary(InputStream inputStream, List<String> errors) throws Exception {
        int successCount = 0;
        
        try {
            // Đọc toàn bộ file vào byte array
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            
            buffer.flush();
            byte[] excelBytes = buffer.toByteArray();
            
            // Phân tích cú pháp Excel từ byte array
            List<List<String>> rows = extractRowsFromExcel(excelBytes);
            
            if (rows != null && !rows.isEmpty()) {
                // Bỏ qua dòng header
                if (rows.size() > 1) {
                    // Xử lý từng dòng dữ liệu
                    for (int i = 1; i < rows.size(); i++) {
                        List<String> row = rows.get(i);
                        
                        // Bỏ qua dòng trống
                        if (row == null || row.isEmpty()) {
                            continue;
                        }
                        
                        try {
                            // Kiểm tra xem dòng có đủ cột không
                            if (row.size() >= 5) {
                                // Cột 0: ID (bỏ qua)
                                // Cột 1: Tên sản phẩm
                                String productName = row.size() > 1 ? row.get(1).trim() : "";
                                // Cột 2: SKU
                                String sku = row.size() > 2 ? row.get(2).trim() : "";
                                // Cột 3: SL Trước
                                String qtyBeforeStr = row.size() > 3 ? row.get(3).trim() : "";
                                int qtyBefore = 0;
                                boolean isQtyBeforeEmpty = qtyBeforeStr.isEmpty();
                                
                                if (!isQtyBeforeEmpty) {
                                    qtyBefore = parseIntSafely(qtyBeforeStr);
                                }
                                
                                // Cột 4: SL Sau
                                String qtyAfterStr = row.size() > 4 ? row.get(4).trim() : "";
                                int qtyAfter = 0;
                                boolean isQtyAfterEmpty = qtyAfterStr.isEmpty();
                                
                                if (!isQtyAfterEmpty) {
                                    qtyAfter = parseIntSafely(qtyAfterStr);
                                }
                                
                                // Cột 5: Thay Đổi (bỏ qua, tính toán lại)
                                // Cột 6: Loại
                                String changeTypeText = row.size() > 6 ? row.get(6).trim() : "";
                                
                                // Cột 7: Lý Do
                                String reason = row.size() > 7 ? row.get(7).trim() : "";
                                
                                // Bỏ qua dòng không có SKU hoặc tên sản phẩm
                                if (sku.isEmpty() || productName.isEmpty()) {
                                    continue;
                                }
                                
                                // Find product in database by SKU
                                Product product = findProductBySku(sku);
                                if (product == null) {
                                    System.out.println("Product not found in database with SKU: " + sku);
                                    
                                    // Create new product if we have a name
                                    if (!productName.isEmpty()) {
                                        System.out.println("Creating new product with SKU: " + sku + ", Name: " + productName);
                                        product = createNewProduct(sku, productName, 0, errors);
                                        if (product == null) {
                                            errors.add("Không thể tạo sản phẩm mới với SKU " + sku);
                                            continue;
                                        }
                                        System.out.println("New product created: " + product.getProductId());
                                    } else {
                                        errors.add("Sản phẩm với mã SKU " + sku + " không tồn tại trong cơ sở dữ liệu và không có tên để tạo mới");
                                        continue;
                                    }
                                }
                                
                                System.out.println("Found/created product: " + product.getName() + ", SKU: " + product.getSku() + ", Current Quantity: " + product.getQuantity());
                                
                                // Nếu SL Trước để trống, lấy số lượng hiện tại từ database
                                if (isQtyBeforeEmpty) {
                                    qtyBefore = product.getQuantity();
                                    System.out.println("Quantity before is empty, using current database quantity: " + qtyBefore);
                                }
                                
                                // Nếu SL Sau để trống hoặc bằng 0, giữ nguyên số lượng hiện tại
                                if (isQtyAfterEmpty || qtyAfter == 0) {
                                    qtyAfter = product.getQuantity();
                                    System.out.println("Quantity after is empty or zero, using current database quantity: " + qtyAfter);
                                }
                                
                                // Kiểm tra xem có sự thay đổi số lượng không
                                if (qtyBefore == qtyAfter) {
                                    // Không có thay đổi, bỏ qua dòng này
                                    System.out.println("No quantity change detected, skipping log creation for SKU: " + sku);
                                    continue;
                                }
                                
                                // Determine change type based on quantities
                                String changeType = qtyAfter > qtyBefore ? "increase" : "decrease";
                                System.out.println("Quantity change detected: " + qtyBefore + " -> " + qtyAfter + ", Change Type: " + changeType);
                                
                                // Create and insert log
                                InventoryLog log = new InventoryLog();
                                log.setProductId(product.getProductId());
                                log.setQuantityBefore(qtyBefore);
                                log.setQuantityAfter(qtyAfter);
                                log.setChangeType(changeType);
                                log.setReason(reason.isEmpty() ? "Nhập từ Excel" : reason);
                                log.setCreatedAt(LocalDateTime.now());
                                
                                // Thiết lập tên sản phẩm và SKU để hiển thị
                                log.setProductName(product.getName());
                                log.setProductSku(product.getSku());
                                
                                // Chèn log
                                boolean result = inventoryLogDAO.insertInventoryLog(log);
                                if (result) {
                                    successCount++;
                                    
                                    // Cập nhật số lượng sản phẩm theo dữ liệu nhập vào
                                    product.setQuantity(qtyAfter);
                                    productDAO.updateProduct(product);
                                } else {
                                    errors.add("Không thể thêm log cho sản phẩm với SKU " + sku);
                                }
                            }
                        } catch (Exception e) {
                            errors.add("Lỗi khi xử lý dòng " + (i + 1) + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                // Thử phương pháp cũ nếu không thành công
                String excelContent = extractTextFromExcelBinary(excelBytes);
                
                // Xử lý nội dung đã trích xuất
                if (excelContent != null && !excelContent.isEmpty()) {
                    try (BufferedReader reader = new BufferedReader(new StringReader(excelContent))) {
                        successCount = processCsvImport(reader, errors);
                    }
                } else {
                    errors.add("Không thể đọc dữ liệu từ file Excel. Vui lòng thử xuất sang CSV hoặc HTML.");
                }
            }
            
        } catch (Exception e) {
            errors.add("Lỗi khi xử lý file Excel: " + e.getMessage());
            e.printStackTrace();
        }
        
        return successCount;
    }
    
    /**
     * Trích xuất các dòng dữ liệu từ file Excel binary
     */
    private List<List<String>> extractRowsFromExcel(byte[] excelBytes) {
        List<List<String>> rows = new ArrayList<>();
        
        try {
            // Chuyển đổi thành chuỗi để tìm kiếm dữ liệu
            String content = new String(excelBytes, "UTF-8");
            
            // Tìm các dòng dữ liệu
            String[] lines = content.split("\\r?\\n");
            
            // Đọc từng dòng
            boolean foundHeader = false;
            List<String> currentRow = null;
            
            for (String line : lines) {
                // Tìm dòng header
                if (!foundHeader && line.contains("ID") && line.contains("SKU") && 
                    (line.contains("SL Trước") || line.contains("SL Sau"))) {
                    
                    foundHeader = true;
                    currentRow = parseExcelRow(line);
                    rows.add(currentRow);
                    continue;
                }
                
                // Nếu đã tìm thấy header, tiếp tục đọc các dòng dữ liệu
                if (foundHeader) {
                    // Kiểm tra xem dòng có phải là dữ liệu không
                    if (line.matches(".*\\d+.*") && (line.contains("CA") || line.contains("TB"))) {
                        currentRow = parseExcelRow(line);
                        rows.add(currentRow);
                    }
                }
            }
            
            // Nếu không tìm thấy dữ liệu theo cách trên, thử cách khác
            if (rows.isEmpty()) {
                // Tìm các chuỗi có thể là dữ liệu theo định dạng bảng
                List<String> tableData = new ArrayList<>();
                
                for (int i = 0; i < excelBytes.length - 4; i++) {
                    // Tìm các chuỗi có thể là dữ liệu
                    if (isStartOfExcelRow(excelBytes, i)) {
                        int endIdx = findEndOfExcelRow(excelBytes, i);
                        if (endIdx > i) {
                            String rowData = new String(excelBytes, i, endIdx - i, "UTF-8");
                            tableData.add(rowData);
                            i = endIdx - 1;
                        }
                    }
                }
                
                // Xử lý các dòng dữ liệu tìm được
                for (String rowData : tableData) {
                    List<String> rowCells = parseExcelRow(rowData);
                    if (!rowCells.isEmpty()) {
                        rows.add(rowCells);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        
        return rows;
    }
    
    /**
     * Kiểm tra xem vị trí hiện tại có phải là đầu dòng dữ liệu không
     */
    private boolean isStartOfExcelRow(byte[] data, int position) {
        // Kiểm tra xem có phải là số (có thể là ID) không
        if (position + 10 < data.length) {
            // Kiểm tra xem có chuỗi số theo sau bởi tab/space và chữ không
            return Character.isDigit(data[position]) && 
                   (data[position + 1] == '\t' || data[position + 1] == ' ' || Character.isDigit(data[position + 1]));
        }
        return false;
    }
    
    /**
     * Tìm vị trí kết thúc của dòng dữ liệu
     */
    private int findEndOfExcelRow(byte[] data, int startPosition) {
        for (int i = startPosition; i < data.length - 1; i++) {
            if (data[i] == '\r' && data[i + 1] == '\n') {
                return i + 2;
            }
            if (data[i] == '\n') {
                return i + 1;
            }
        }
        return startPosition + 1;
    }
    
    /**
     * Phân tích một dòng dữ liệu từ Excel thành các ô
     */
    private List<String> parseExcelRow(String rowData) {
        List<String> cells = new ArrayList<>();
        
        // Thử phân tách bằng tab
        String[] tabCells = rowData.split("\t");
        if (tabCells.length > 3) {
            for (String cell : tabCells) {
                cells.add(cell.trim());
            }
            return cells;
        }
        
        // Nếu không có tab, thử phân tách bằng khoảng trắng
        // Nhưng cần xử lý cẩn thận vì tên sản phẩm có thể có khoảng trắng
        String[] parts = rowData.split("\\s+");
        StringBuilder currentCell = new StringBuilder();
        boolean isInProductName = false;
        
        for (String part : parts) {
            // Nếu phần tử hiện tại giống mã SKU (CA, TB, v.v.)
            if (part.matches("(?i)CA\\d+|TB\\d+") && !isInProductName) {
                if (currentCell.length() > 0) {
                    cells.add(currentCell.toString().trim());
                    currentCell = new StringBuilder();
                }
                cells.add(part);
                isInProductName = false;
                continue;
            }
            
            // Nếu phần tử hiện tại là số
            if (part.matches("\\d+") && !isInProductName) {
                if (currentCell.length() > 0) {
                    cells.add(currentCell.toString().trim());
                    currentCell = new StringBuilder();
                }
                cells.add(part);
                continue;
            }
            
            // Nếu là "Tăng" hoặc "Giảm"
            if ((part.equalsIgnoreCase("Tăng") || part.equalsIgnoreCase("Giảm")) && !isInProductName) {
                if (currentCell.length() > 0) {
                    cells.add(currentCell.toString().trim());
                    currentCell = new StringBuilder();
                }
                cells.add(part);
                continue;
            }
            
            // Các phần tử khác (có thể là tên sản phẩm hoặc lý do)
            if (currentCell.length() > 0) {
                currentCell.append(" ");
            }
            currentCell.append(part);
            
            // Nếu đây có thể là tên sản phẩm
            if (part.contains("Cá") || part.contains("Máy") || part.contains("Đèn") || part.contains("Bể")) {
                isInProductName = true;
            }
        }
        
        // Thêm ô cuối cùng nếu có
        if (currentCell.length() > 0) {
            cells.add(currentCell.toString().trim());
        }
        
        return cells;
    }
    
    /**
     * Chuyển đổi chuỗi thành số an toàn
     */
    private int parseIntSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        
        try {
            // Clean the input - remove any non-numeric characters except + and -
            String cleanedValue = value.trim();
            
            // Handle percentage notation (e.g., "-5%")
            if (cleanedValue.endsWith("%")) {
                cleanedValue = cleanedValue.substring(0, cleanedValue.length() - 1);
            }
            
            // Check for + prefix
            boolean isPositive = cleanedValue.startsWith("+");
            if (isPositive) {
                cleanedValue = cleanedValue.substring(1);
            }
            
            // Handle negative numbers
            boolean isNegative = cleanedValue.startsWith("-");
            if (isNegative) {
                cleanedValue = cleanedValue.substring(1);
            }
            
            // Remove any remaining non-numeric characters
            cleanedValue = cleanedValue.replaceAll("[^0-9]", "");
            
            if (cleanedValue.isEmpty()) {
                return 0;
            }
            
            int result = Integer.parseInt(cleanedValue);
            return isNegative ? -result : result;
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse number: " + value);
            return 0;
        }
    }
    
    /**
     * Create a new product if it doesn't exist yet
     */
    private Product createNewProduct(String sku, String name, int quantity, List<String> errors) {
        try {
            System.out.println("Creating new product - SKU: " + sku + ", Name: " + name + ", Quantity: " + quantity);
            
            // Make sure the SKU is unique
            String uniqueSku = productDAO.getUniqueSku(sku);
            if (!uniqueSku.equals(sku)) {
                System.out.println("SKU " + sku + " already exists, using " + uniqueSku + " instead");
            }
            
            // Create a new product with safe defaults
            Product product = new Product();
            product.setSku(uniqueSku); // Use unique SKU
            product.setName(name != null ? name : ""); // Name is required
            product.setQuantity(quantity);
            product.setPrice(new BigDecimal("0.00"));
            product.setSalePrice(null); // No sale price by default
            product.setStatus("active");
            product.setFeatured(0);
            product.setCategoryId(1); // Default category ID
            product.setShortDescription("Sản phẩm thêm từ nhập kho");
            product.setDescription(""); // Empty description
            product.setIsDeleted(0);
            
            // Insert the new product
            boolean success = productDAO.createProduct(product);
            System.out.println("Product creation result: " + success);
            
            if (success) {
                // Get the newly created product with ID
                int lastInsertId = productDAO.getLastInsertProductId();
                System.out.println("New product ID: " + lastInsertId);
                return productDAO.getProductById(lastInsertId);
            } else {
                errors.add("Failed to create new product with SKU " + uniqueSku);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error creating new product: " + e.getMessage());
            errors.add("Error creating new product with SKU " + sku + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Extract cell values from HTML table row
     */
    private List<String> extractCellsFromHtml(String rowHtml) {
        List<String> cells = new ArrayList<>();
        
        int startIndex = 0;
        while (true) {
            int tdStartIndex = rowHtml.indexOf("<td", startIndex);
            if (tdStartIndex == -1) break;
            
            int contentStartIndex = rowHtml.indexOf(">", tdStartIndex);
            if (contentStartIndex == -1) break;
            contentStartIndex++;
            
            int tdEndIndex = rowHtml.indexOf("</td>", contentStartIndex);
            if (tdEndIndex == -1) break;
            
            String cellContent = rowHtml.substring(contentStartIndex, tdEndIndex).trim();
            
            // Clean up HTML entities and tags in cell content
            cellContent = cellContent
                    .replaceAll("<[^>]*>", "") // Remove HTML tags
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&quot;", "\"")
                    .replace("&#039;", "'")
                    .trim();
            
            cells.add(cellContent);
            startIndex = tdEndIndex + 5;
        }
        
        return cells;
    }
    
    /**
     * Parse a space-separated line, being careful to keep words within cells together
     */
    private List<String> parseSpaceSeparatedLine(String line) {
        // For space-separated lines, we need to be smarter about how we split
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inWord = false;
        int valueCount = 0;
        int skuIndex = -1;
        
        System.out.println("Parsing space-separated line");
        
        // Split by whitespace initially
        String[] parts = line.split("\\s+");
        
        // Try to identify SKU pattern (usually something like XX123)
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].matches("(?i)[A-Za-z]{2}\\d+")) {
                skuIndex = i;
                System.out.println("Found potential SKU at position " + i + ": " + parts[i]);
                break;
            }
        }
        
        // If we found a SKU, we can use it to help structure our parsing
        if (skuIndex >= 0) {
            // Assume: ID, Product Name, SKU, qtyBefore, qtyAfter, Change, Type, Reason
            
            // ID is typically at position 0
            if (skuIndex > 0) {
                values.add(parts[0]); // ID
                
                // Everything between ID and SKU is the product name
                StringBuilder productName = new StringBuilder();
                for (int i = 1; i < skuIndex; i++) {
                    if (productName.length() > 0) productName.append(" ");
                    productName.append(parts[i]);
                }
                values.add(productName.toString()); // Product Name
            } else {
                // If SKU is at position 0, add empty values for ID and name
                values.add(""); // Empty ID
                values.add(""); // Empty Product Name
            }
            
            // Add SKU
            values.add(parts[skuIndex]);
            
            // Try to identify quantity fields after the SKU
            List<String> remainingValues = new ArrayList<>();
            boolean foundNumbers = false;
            StringBuilder currentField = new StringBuilder();
            
            for (int i = skuIndex + 1; i < parts.length; i++) {
                String part = parts[i];
                
                // If this part looks like a number or is "Tăng"/"Giảm", treat it as a separate field
                if (part.matches("-?\\d+") || part.equalsIgnoreCase("Tăng") || part.equalsIgnoreCase("Giảm")) {
                    if (currentField.length() > 0) {
                        remainingValues.add(currentField.toString().trim());
                        currentField = new StringBuilder();
                    }
                    remainingValues.add(part);
                    foundNumbers = true;
                } else {
                    // If we've already found number fields and this is text, it's probably part of the reason
                    if (foundNumbers) {
                        if (currentField.length() > 0) currentField.append(" ");
                        currentField.append(part);
                    } else {
                        // If we haven't found numbers yet, this could be a field on its own
                        remainingValues.add(part);
                    }
                }
            }
            
            // Add the final field if it exists
            if (currentField.length() > 0) {
                remainingValues.add(currentField.toString().trim());
            }
            
            // Add remaining values
            values.addAll(remainingValues);
            
        } else {
            // If we can't identify the SKU, just split by whitespace
            for (String part : parts) {
                values.add(part);
            }
        }
        
        // Debug output
        System.out.println("Parsed " + values.size() + " values from space-separated line");
        for (int i = 0; i < values.size(); i++) {
            System.out.println("  Value " + i + ": [" + values.get(i) + "]");
        }
        
        return values;
    }
    
    /**
     * Count occurrences of a character in a string
     */
    private int countChar(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Count consecutive spaces in a string to detect if it's space-separated
     */
    private int countConsecutiveSpaces(String str) {
        int maxConsecutive = 0;
        int currentConsecutive = 0;
        
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                currentConsecutive++;
                maxConsecutive = Math.max(maxConsecutive, currentConsecutive);
            } else {
                currentConsecutive = 0;
            }
        }
        
        return maxConsecutive;
    }
    
    /**
     * Find product by SKU
     */
    private Product findProductBySku(String sku) {
        if (sku == null || sku.isEmpty()) {
            return null;
        }
        return productDAO.getProductBySku(sku);
    }
    
    /**
     * Get filename from Part with better handling for Content-Disposition header
     */
    private String getFileName(Part part) {
        System.out.println("Getting filename for part: " + part.getName());
        
        // First try standard approach
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("Content-Disposition: " + contentDisp);
        
        if (contentDisp == null) {
            System.out.println("Content-Disposition header is null");
            return "unknown";
        }
        
        String[] items = contentDisp.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                String filename = item.substring(item.indexOf('=') + 1).trim();
                filename = filename.replace("\"", "");
                System.out.println("Found filename: " + filename);
                
                // If filename contains path info, extract just the filename
                int lastSlash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
                if (lastSlash > -1) {
                    filename = filename.substring(lastSlash + 1);
                    System.out.println("Extracted pure filename: " + filename);
                }
                
                return filename;
            }
        }
        
        // If standard approach failed, try a more lenient approach
        if (contentDisp.contains("filename")) {
            int start = contentDisp.indexOf("filename") + 9;
            int end = contentDisp.length();
            if (contentDisp.indexOf(";", start) != -1) {
                end = contentDisp.indexOf(";", start);
            }
            
            String filename = contentDisp.substring(start, end).trim().replace("\"", "").replace("=", "");
            System.out.println("Found filename using fallback method: " + filename);
            return filename;
        }
        
        System.out.println("Could not extract filename, returning 'unknown'");
        return "unknown";
    }
    
    /**
     * Parse .srv file with special handling for inventory data
     * @param filePart Uploaded file part
     * @param errors List to collect any errors during parsing
     * @return Number of successfully imported records
     */
    private int importSrvFile(Part filePart, List<String> errors) throws Exception {
        int successCount = 0;
        
        try (InputStream inputStream = filePart.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Trim and skip empty lines
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                // Split line by semicolon or comma (flexible delimiter)
                String[] parts = line.split("[;,]");
                
                // Ensure we have enough parts
                if (parts.length < 4) {
                    errors.add("Invalid .srv file format: Not enough columns in line: " + line);
                    continue;
                }
                
                // Extract data
                String sku = parts[0].trim();
                String productName = parts[1].trim();
                String qtyBeforeStr = parts[2].trim();
                String qtyAfterStr = parts[3].trim();
                String reason = parts.length > 4 ? parts[4].trim() : "";
                
                // Parse quantities
                int qtyBefore = 0;
                int qtyAfter = 0;
                boolean isQtyBeforeEmpty = qtyBeforeStr.isEmpty();
                boolean isQtyAfterEmpty = qtyAfterStr.isEmpty();
                
                try {
                    if (!isQtyBeforeEmpty) {
                        qtyBefore = Integer.parseInt(qtyBeforeStr);
                    }
                    
                    if (!isQtyAfterEmpty) {
                        qtyAfter = Integer.parseInt(qtyAfterStr);
                    }
                } catch (NumberFormatException e) {
                    errors.add("Invalid quantity format in line: " + line);
                    continue;
                }
                
                // Find product by SKU
                Product product = findProductBySku(sku);
                
                // If product doesn't exist, create it (if we have enough data)
                if (product == null && !sku.isEmpty() && !productName.isEmpty()) {
                    product = createNewProduct(sku, productName, qtyAfter, errors);
                    if (product == null) {
                        continue; // Skip if product creation failed
                    }
                } else if (product == null) {
                    errors.add("Sản phẩm với mã SKU " + sku + " không tồn tại. Đảm bảo cung cấp cả SKU và tên sản phẩm để hệ thống có thể tự tạo mới.");
                    continue;
                }
                
                // If initial quantity is empty, use current database quantity
                if (isQtyBeforeEmpty) {
                    qtyBefore = product.getQuantity();
                    System.out.println("Quantity before is empty, using current database quantity: " + qtyBefore);
                }
                
                // If final quantity is empty or zero, use current database quantity
                if (isQtyAfterEmpty || qtyAfter == 0) {
                    qtyAfter = product.getQuantity();
                    System.out.println("Quantity after is empty or zero, using current database quantity: " + qtyAfter);
                }
                
                // Check if there's an actual quantity change
                if (qtyBefore == qtyAfter) {
                    System.out.println("No quantity change detected, skipping log creation for SKU: " + sku);
                    continue;
                }
                
                // Determine change type
                String changeType = qtyAfter > qtyBefore ? "increase" : "decrease";
                System.out.println("Quantity change detected: " + qtyBefore + " -> " + qtyAfter + ", Change Type: " + changeType);
                
                // Create and insert log
                InventoryLog log = new InventoryLog();
                log.setProductId(product.getProductId());
                log.setQuantityBefore(qtyBefore);
                log.setQuantityAfter(qtyAfter);
                log.setChangeType(changeType);
                log.setReason(reason);
                log.setCreatedAt(LocalDateTime.now());
                
                // Set product name and SKU for display
                log.setProductName(product.getName());
                log.setProductSku(product.getSku());
                
                // Insert the log
                boolean result = inventoryLogDAO.insertInventoryLog(log);
                if (result) {
                    successCount++;
                    
                    // Update product quantity
                    product.setQuantity(qtyAfter);
                    productDAO.updateProduct(product);
                } else {
                    errors.add("Failed to insert log for SKU " + sku);
                }
            }
        }
        
        return successCount;
    }
    
    /**
     * Export a CSV template file for inventory import
     */
    private void exportTemplateToCSV(HttpServletResponse response) throws IOException {
        List<Product> products = productDAO.getAllProducts();
        PrintWriter out = response.getWriter();
        
        // Write UTF-8 BOM
        out.print("\uFEFF");
        
        // Header row
        out.println("ID;Sản Phẩm;SKU;SL Trước;SL Sau;Thay Đổi;Loại;Lý Do;Thời Gian");
        
        // Get current date/time formatted
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        // Sample data with a few products
        int rowIndex = 3; // Starting from ID=3 like in the example
        for (Product product : products) {
            if (rowIndex > 12) break; // Include only first 10 products as examples (ending at ID=12)
            
            String sku = product.getSku();
            String name = product.getName();
            int currentQty = product.getQuantity();
            
            // For odd rows: decrease example
            if (rowIndex % 2 != 0) {
                int decreaseBy = 5;
                if (sku.equals("CA04")) decreaseBy = 2;  // Special case for CA04
                if (sku.equals("CA05")) decreaseBy = 2;  // Special case for CA05
                if (sku.equals("CA06")) decreaseBy = 2;  // Special case for CA06
                
                out.print(rowIndex);
                out.print(";");
                out.print("\"" + name + "\"");
                out.print(";");
                out.print(sku);
                out.print(";");
                out.print(""); // SL Trước is empty - will be filled from database
                out.print(";");
                out.print(currentQty - decreaseBy); // Example: decrease by amount
                out.print(";");
                out.print("-" + decreaseBy); // Thay đổi
                out.print(";");
                out.print("Giảm"); // Loại
                out.print(";");
                
                // Customize reason based on product
                String reason;
                if (sku.equals("CA01")) {
                    reason = "Bán cho khách hàng #1001";
                } else if (sku.equals("CA03")) {
                    reason = "Khách hàng đặt hàng #1002";
                } else if (sku.equals("CA04")) {
                    reason = "Kiểm kê phát hiện thiếu 2 con cá";
                } else if (sku.equals("CA05")) {
                    reason = "Kiểm kê thiếu 2 cá thòi lòi";
                } else if (sku.equals("CA06")) {
                    reason = "Khách mua 2 cá hề Nemo";
                } else if (sku.equals("TB02")) {
                    reason = "Hỏng 5 bơm nước mini";
                } else if (sku.equals("CA15")) {
                    reason = "Bán 5 cá neon";
                } else {
                    reason = "Bán cho khách hàng";
                }
                out.print("\"" + reason + "\"");
                out.print(";");
                out.println(currentTime);
            } 
            // For even rows: increase example
            else {
                int increaseBy;
                if (sku.equals("CA02")) {
                    increaseBy = 30;
                } else if (sku.equals("CA13")) {
                    increaseBy = 10;
                } else {
                    increaseBy = 5;
                }
                
                out.print(rowIndex);
                out.print(";");
                out.print("\"" + name + "\"");
                out.print(";");
                out.print(sku);
                out.print(";");
                out.print(""); // SL Trước is empty - will be filled from database
                out.print(";");
                out.print(currentQty + increaseBy); // Example: increase by amount
                out.print(";");
                out.print("+" + increaseBy); // Thay đổi
                out.print(";");
                out.print("Tăng"); // Loại
                out.print(";");
                
                // Customize reason based on product
                String reason;
                if (sku.equals("CA02")) {
                    reason = "Nhập hàng từ nhà cung cấp A";
                } else if (sku.equals("CA13")) {
                    reason = "Nhập thêm cá Guppy sinh sản";
                } else if (sku.equals("TB01")) {
                    reason = "Nhập thêm bể kính 50L";
                } else {
                    reason = "Nhập hàng mới";
                }
                out.print("\"" + reason + "\"");
                out.print(";");
                out.println(currentTime);
            }
            
            rowIndex++;
        }
        
        // Instructions row
        out.println(";;;;;;;;");
        out.println("\"HƯỚNG DẪN:\";;;;;;;;");
        out.println("\"1. Để trống cột SL Trước để sử dụng số lượng hiện có trong cơ sở dữ liệu\";;;;;;;;");
        out.println("\"2. Điền SL Sau hoặc cột Thay Đổi (thêm dấu + hoặc - cho tăng/giảm)\";;;;;;;;");
        out.println("\"3. Cột Loại nên là 'Tăng' hoặc 'Giảm', nếu trống sẽ dựa vào số lượng\";;;;;;;;");
        out.println("\"4. Cột Thời Gian sẽ được tự động cập nhật khi nhập\";;;;;;;;");
        out.println("\"5. Cần giữ đúng thứ tự các cột như trong mẫu này\";;;;;;;;");
        out.println("\"6. Nên lưu file này dưới dạng CSV (Comma Separated Values) với mã UTF-8\";;;;;;;;");
        
        out.flush();
    }
    
    private void exportWarehouseToExcel(HttpServletResponse response) throws IOException {
        List<Product> products = productDAO.getAllProducts();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Warehouse");

        // Tạo font in đậm cho header
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        
        // Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        
        // Tạo style cho ngày tháng
        CellStyle dateStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("dd/MM/yyyy HH:mm"));

        // Header
        String[] headers = {"ID", "Sản Phẩm", "SKU", "SL Trước", "SL Sau", "Thay Đổi", "Loại", "Lý Do", "Thời Gian"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data - Sử dụng dữ liệu mẫu như trong hình
        // Tạo dữ liệu mẫu thể hiện trong hình
        Object[][] sampleData = {
            {41, "Cá Ngừ", "CA16", "", "", 20, "Tăng", "Nhập hàng từ nhà cung cấp B", "03/07/2025 12:00"},
            {42, "Cá Chim", "CA17", "", "", 3, "Tăng", "Bán cho khách hàng #1005", "03/07/2025 12:00"},
            {43, "Cá Lóc", "CA18", "", "", 10, "Tăng", "Kiểm kê bổ sung", "03/07/2025 12:00"},
            {44, "Cá Mú", "CA19", "", "", 5, "Tăng", "Hư hỏng do vận chuyển", "03/07/2025 12:00"},
            {45, "Cá Chình", "CA20", "", "", 15, "Tăng", "Nhập từ nguồn đặc biệt", "03/07/2025 12:00"}
        };
        
        // Thêm dữ liệu mẫu vào sheet
        for (int i = 0; i < sampleData.length; i++) {
            Row row = sheet.createRow(i + 1);
            Object[] rowData = sampleData[i];
            
            // ID - số nguyên
            Cell idCell = row.createCell(0);
            idCell.setCellValue((Integer) rowData[0]);
            
            // Sản Phẩm - chuỗi
            row.createCell(1).setCellValue((String) rowData[1]);
            
            // SKU - chuỗi
            row.createCell(2).setCellValue((String) rowData[2]);
            
            // SL Trước - để trống
            row.createCell(3).setCellValue((String) rowData[3]);
            
            // SL Sau - để trống
            row.createCell(4).setCellValue((String) rowData[4]);
            
            // Thay Đổi - số nguyên
            Cell changeCell = row.createCell(5);
            changeCell.setCellValue((Integer) rowData[5]);
            
            // Loại - chuỗi
            row.createCell(6).setCellValue((String) rowData[6]);
            
            // Lý Do - chuỗi
            row.createCell(7).setCellValue((String) rowData[7]);
            
            // Thời Gian - chuỗi định dạng ngày tháng
            Cell dateCell = row.createCell(8);
            dateCell.setCellValue((String) rowData[8]);
        }
        
        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=warehouse_export.xlsx");

        // Write to output
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        }
        workbook.close();
    }
    
    private void exportInventoryLogsToExcel(List<InventoryLog> logs, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory Logs");
        String[] headers = {"ID", "Sản Phẩm", "SKU", "SL Trước", "SL Sau", "Thay Đổi", "Loại", "Lý Do", "Thời Gian"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        int rowIdx = 1;
        for (InventoryLog log : logs) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(log.getLogId());
            row.createCell(1).setCellValue(log.getProductName());
            row.createCell(2).setCellValue(log.getProductSku());
            row.createCell(3).setCellValue(log.getQuantityBefore());
            row.createCell(4).setCellValue(log.getQuantityAfter());
            row.createCell(5).setCellValue(log.getQuantityAfter() - log.getQuantityBefore());
            row.createCell(6).setCellValue("increase".equals(log.getChangeType()) ? "Tăng" : "Giảm");
            row.createCell(7).setCellValue(log.getReason());
            row.createCell(8).setCellValue(log.getCreatedAt().toString());
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=inventory_logs_export.xlsx");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        }
        workbook.close();
    }
    
} 