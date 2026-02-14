package controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@WebServlet("/uploads/*")
public class FileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Lấy đường dẫn file từ URL
        String fileName = pathInfo.substring(1); // Bỏ dấu / đầu tiên
        
        // Tạo đường dẫn đầy đủ đến file
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File file = new File(uploadPath, fileName);
        
        // Kiểm tra file có tồn tại không
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Kiểm tra file có nằm trong thư mục uploads không (security check)
        String canonicalUploadPath = new File(uploadPath).getCanonicalPath();
        String canonicalFilePath = file.getCanonicalPath();
        if (!canonicalFilePath.startsWith(canonicalUploadPath)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        // Xác định content type
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            // Fallback cho các loại file ảnh
            String extension = fileName.toLowerCase();
            if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (extension.endsWith(".png")) {
                contentType = "image/png";
            } else if (extension.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (extension.endsWith(".webp")) {
                contentType = "image/webp";
            } else {
                contentType = "application/octet-stream";
            }
        }
        
        // Set headers
        response.setContentType(contentType);
        response.setContentLengthLong(file.length());
        
        // Cache headers để tăng performance
        response.setHeader("Cache-Control", "public, max-age=31536000"); // 1 year
        response.setDateHeader("Expires", System.currentTimeMillis() + 31536000000L);
        
        // Gửi file
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
