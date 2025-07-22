package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp cấu hình cho tích hợp SePay
 */
public class SePayConfig {
    // API endpoints
    public static final String API_BASE_URL = "https://api.sepay.vn/v1";
    public static final String CREATE_PAYMENT_ENDPOINT = "/payments";
    public static final String CHECK_PAYMENT_ENDPOINT = "/payments/status";
    
    // Thông tin xác thực API
    private static String apiKey = "R8OO0QJ7RMVHUH5AA9DG625S10PKKZJRUJJZBXTCSQPNXYLGXNAHBCKVP98G3LYN";
    private static String secretKey = "R8OO0QJ7RMVHUH5AA9DG625S10PKKZJRUJJZBXTCSQPNXYLGXNAHBCKVP98G3LYN"; // Sử dụng API key làm secret key nếu không có secret key riêng
    private static String webhookSecret = "R8OO0QJ7RMVHUH5AA9DG625S10PKKZJRUJJZBXTCSQPNXYLGXNAHBCKVP98G3LYN"; // Sử dụng API key làm webhook secret
    
    // Webhook URL
    private static String webhookUrl = "https://233fa915fd4d.ngrok-free.app/DuAnBanCaCanh/sepay-webhook";
    
    /**
     * Cập nhật URL webhook với URL ngrok
     * @param ngrokUrl URL ngrok (ví dụ: https://233fa915fd4d.ngrok-free.app)
     */
    public static void updateWebhookUrlWithNgrok(String ngrokUrl) {
        // Đảm bảo URL ngrok không có dấu / ở cuối
        if (ngrokUrl.endsWith("/")) {
            ngrokUrl = ngrokUrl.substring(0, ngrokUrl.length() - 1);
        }
        
        // Cập nhật webhook URL với đường dẫn đầy đủ (bao gồm context path)
        String newWebhookUrl = ngrokUrl + "/DuAnBanCaCanh/sepay-webhook";
        webhookUrl = newWebhookUrl;
        
        System.out.println("Webhook URL đã được cập nhật: " + webhookUrl);
        System.out.println("⚠️ QUAN TRỌNG: Vui lòng cập nhật URL này trong SePay dashboard");
    }
    
    // Thông tin tài khoản ngân hàng mặc định
    private static String defaultBankName = "mbbank";
    private static String defaultBankId = "970422";
    private static String defaultAccountNumber = "681888899999";
    private static String defaultAccountName = "DAU DOAN HOAN THIEN";
    
    // Danh sách các ngân hàng được hỗ trợ với mã BIN
    private static final Map<String, String> BANK_BIN_CODES = new HashMap<>();
    static {
        BANK_BIN_CODES.put("vietcombank", "970436");
        BANK_BIN_CODES.put("vietinbank", "970415");
        BANK_BIN_CODES.put("bidv", "970418");
        BANK_BIN_CODES.put("techcombank", "970407");
        BANK_BIN_CODES.put("mbbank", "970422");
        BANK_BIN_CODES.put("acb", "970416");
        BANK_BIN_CODES.put("vpbank", "970432");
        BANK_BIN_CODES.put("sacombank", "970403");
        BANK_BIN_CODES.put("tpbank", "970423");
    }
    
    // Getters và setters
    public static String getApiKey() {
        return apiKey;
    }
    
    public static void setApiKey(String apiKey) {
        SePayConfig.apiKey = apiKey;
    }
    
    public static String getSecretKey() {
        return secretKey;
    }
    
    public static void setSecretKey(String secretKey) {
        SePayConfig.secretKey = secretKey;
    }
    
    public static String getWebhookSecret() {
        return webhookSecret;
    }
    
    public static void setWebhookSecret(String webhookSecret) {
        SePayConfig.webhookSecret = webhookSecret;
    }
    
    public static String getWebhookUrl() {
        return webhookUrl;
    }
    
    public static void setWebhookUrl(String webhookUrl) {
        SePayConfig.webhookUrl = webhookUrl;
    }
    
    public static String getDefaultBankName() {
        return defaultBankName;
    }
    
    public static void setDefaultBankName(String defaultBankName) {
        SePayConfig.defaultBankName = defaultBankName;
    }
    
    public static String getDefaultBankId() {
        return defaultBankId;
    }
    
    public static void setDefaultBankId(String defaultBankId) {
        SePayConfig.defaultBankId = defaultBankId;
    }
    
    public static String getDefaultAccountNumber() {
        return defaultAccountNumber;
    }
    
    public static void setDefaultAccountNumber(String defaultAccountNumber) {
        SePayConfig.defaultAccountNumber = defaultAccountNumber;
    }
    
    public static String getDefaultAccountName() {
        return defaultAccountName;
    }
    
    public static void setDefaultAccountName(String defaultAccountName) {
        SePayConfig.defaultAccountName = defaultAccountName;
    }
    
    public static Map<String, String> getBankBinCodes() {
        return new HashMap<>(BANK_BIN_CODES);
    }
    
    public static String getBankBinCode(String bankName) {
        return BANK_BIN_CODES.getOrDefault(bankName.toLowerCase(), "");
    }
    
    /**
     * Cập nhật thông tin tài khoản ngân hàng mặc định
     */
    public static void updateDefaultBankAccount(String bankName, String accountNumber, String accountName) {
        defaultBankName = bankName;
        defaultBankId = getBankBinCode(bankName);
        defaultAccountNumber = accountNumber;
        defaultAccountName = accountName;
    }
} 