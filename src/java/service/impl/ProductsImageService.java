
package service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class ProductsImageService {
    
    public String uploadImage(Part filePart, HttpServletRequest request) throws IOException, ServletException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Tạo tên file duy nhất (tránh trùng lặp)
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = System.currentTimeMillis() + "_" + 
                               UUID.randomUUID().toString().substring(0, 8) + fileExtension;
        
        // Đường dẫn lưu file
        String uploadDir = request.getServletContext().getRealPath("") + 
                          File.separator + "uploads" + File.separator + "products";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }
        
        // Lưu file
        String filePath = uploadDir + File.separator + uniqueFileName;
        filePart.write(filePath);
        
        // Trả về đường dẫn tương đối để lưu vào database
        return "uploads/products/" + uniqueFileName;
    }
    
    /**
     * Kiểm tra xem file có phải là hình ảnh hợp lệ không
     * @param filePart Part object chứa file cần kiểm tra
     * @return true nếu file là hình ảnh hợp lệ, false nếu không
     */
    public boolean isValidImageFile(Part filePart) {
        if (filePart == null) return false;
        
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null) return false;
        
        String contentType = filePart.getContentType();
        
        // Kiểm tra extension
        String fileExtension = fileName.toLowerCase();
        boolean validExtension = fileExtension.endsWith(".jpg") || 
                                fileExtension.endsWith(".jpeg") || 
                                fileExtension.endsWith(".png") || 
                                fileExtension.endsWith(".gif");
        
        // Kiểm tra MIME type
        boolean validMimeType = contentType != null && 
                               (contentType.equals("image/jpeg") || 
                                contentType.equals("image/png") || 
                                contentType.equals("image/gif"));
        
        return validExtension && validMimeType;
    }
    
    /**
     * Xóa file hình ảnh khỏi server
     * @param imagePath Đường dẫn tương đối của file cần xóa
     * @param request HttpServletRequest để lấy context path
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteImage(String imagePath, HttpServletRequest request) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            String fullPath = request.getServletContext().getRealPath("") + 
                             File.separator + imagePath.replace("/", File.separator);
            File file = new File(fullPath);
            
            if (file.exists() && file.isFile()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra xem file có tồn tại trên server không
     * @param imagePath Đường dẫn tương đối của file
     * @param request HttpServletRequest để lấy context path
     * @return true nếu file tồn tại, false nếu không
     */
    public boolean imageExists(String imagePath, HttpServletRequest request) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            String fullPath = request.getServletContext().getRealPath("") + 
                             File.separator + imagePath.replace("/", File.separator);
            File file = new File(fullPath);
            return file.exists() && file.isFile();
        } catch (Exception e) {
            return false;
        }
    }
}