package test;

import dao.impl.CategoryDAO;
import model.entity.Category;
import java.util.List;

public class TestCategory {
    public static void main(String[] args) {
        System.out.println("=== Testing CategoryDAO ===");
        
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            System.out.println("CategoryDAO created successfully");
            
            List<Category> categories = categoryDAO.getAllCategories();
            System.out.println("Found " + categories.size() + " categories");
            
            for (Category cat : categories) {
                System.out.println("Category: " + cat.getName() + ", Status: " + cat.getStatus());
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
