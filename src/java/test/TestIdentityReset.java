package test;

import dao.impl.UserDAOImpl;

public class TestIdentityReset {
    public static void main(String[] args) {
        UserDAOImpl userDAO = new UserDAOImpl();
        
        // 1. Lấy giá trị IDENTITY hiện tại
        int currentValue = userDAO.getCurrentIdentityValue();
        System.out.println("Current IDENTITY value: " + currentValue);
        
        // 2. Reset giá trị IDENTITY (ví dụ: về 6)
        boolean success = userDAO.resetIdentity(20);
        if (success) {
            System.out.println("Successfully reset IDENTITY value");
            // Kiểm tra lại giá trị mới
            int newValue = userDAO.getCurrentIdentityValue();
            System.out.println("New IDENTITY value: " + newValue);
        } else {
            System.out.println("Failed to reset IDENTITY value");
        }
    }
}
