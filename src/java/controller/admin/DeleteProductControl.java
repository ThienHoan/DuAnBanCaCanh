

package controller.admin;

import dao.impl.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name="DeleteProductControl", urlPatterns={"/deleteProduct"})
public class DeleteProductControl extends HttpServlet {
   
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DeleteProductControl</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteProductControl at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy productId từ tham số request
            int productId = Integer.parseInt(request.getParameter("id"));
            
            // Khởi tạo ProductDAO và gọi hàm toggleIsDeleted
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.toggleIsDeleted(productId);
            
            if (success) {
                // Chuyển hướng về trang danh sách sản phẩm nếu thành công
                response.sendRedirect("listProduct");
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

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
