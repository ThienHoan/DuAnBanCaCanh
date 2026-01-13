package utils.admin;
public class NumberChecker {

    // Đây là phương thức main để bắt đầu chạy chương trình
    public static void main(String[] args) {
        System.out.println("--- Bắt đầu kiểm thử ---");

        // Chạy thử các Test Case đã thiết kế
        // TC1: n=6, op="Perfect" -> Mong đợi: 1
        System.out.println("Kết quả TC1 (n=0, op=Perfect): " + checkOpNumber(1, "Square"));

        // TC2: n=10, op="Perfect" -> Mong đợi: 0
        System.out.println("Kết quả TC2 (n=2, op=Null): " + checkOpNumber(2, ""));

        // TC3: n=9, op="Square" -> Mong đợi: 1
        System.out.println("Kết quả TC3 (n=3, op=Square): " + checkOpNumber(3, "Square"));

        // TC4: n=10, op="Square" -> Mong đợi: 0
        System.out.println("Kết quả TC4 (n=4, op=Perfect): " + checkOpNumber(4, "Perfect"));

        // TC5: n=5, op="Invalid" -> Mong đợi: -1
        System.out.println("Kết quả TC5 (n=5, op=Null): " + checkOpNumber(5, ""));

        
        System.out.println("Kết quả TC6 (n=6, op=Invalid): " + checkOpNumber(6, "Square"));
        System.out.println("--- Kết thúc kiểm thử ---");
    }

    // Đoạn code từ đề bài (đã sửa lỗi 'string' -> 'String')
    public static int checkOpNumber(int n, String op) {
        switch (op) {
            case "Perfect":
                int i, sum;
                sum = 0;
                for (i = 1; i < n; i++) {
                    if (n % i == 0) {
                        sum = sum + i;
                    }
                }
                if (sum == n) {
                    return 1;
                } else {
                    return 0;
                }
            case "Square":
                int sqr = (int) Math.sqrt(n);
                if (sqr * sqr == n) {
                    return 1;
                } else {
                    return 0;
                }
        }
        return -1;
    }
}