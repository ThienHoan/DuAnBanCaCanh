package controller.admin;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.impl.ProductDAO;
import model.entity.Product;

/**
 *
 * @author DELL
 */
@WebServlet(name="HomeServletTestKha", urlPatterns={"/home"})
public class HomeServletTestKha extends HttpServlet {
   
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            // Lấy danh sách sản phẩm cho customer (chỉ active và chưa bị xóa)
            List<Product> products = productDAO.getAllProducts();
            
            // Lấy sản phẩm nổi bật cho customer
            
            // Đẩy dữ liệu lên JSP
            request.setAttribute("products", products);
            
            // Forward đến JSP
            request.getRequestDispatcher("/hometest.jsp").forward(request, response);
            
        
    } 

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

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
        return "Home servlet for loading products data to hometest.jsp";
    }
}