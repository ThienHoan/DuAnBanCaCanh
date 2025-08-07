package controller.test;

import utils.ConfigUtil;
import utils.OAuthConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Test servlet để kiểm tra configuration
 * URL: /config-test
 * 
 * @author Fish Shop Team
 * @version 1.0
 */
@WebServlet(urlPatterns = {"/config-test"})
public class ConfigTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Configuration Test</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
            out.println(".section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }");
            out.println(".success { background-color: #d4edda; border-color: #c3e6cb; color: #155724; }");
            out.println(".error { background-color: #f8d7da; border-color: #f5c6cb; color: #721c24; }");
            out.println(".info { background-color: #d1ecf1; border-color: #bee5eb; color: #0c5460; }");
            out.println("table { width: 100%; border-collapse: collapse; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>🔧 Configuration Test Page</h1>");
            out.println("<p><em>This page helps you verify that your configuration is working correctly.</em></p>");
            
            // Test TinyMCE Configuration
            out.println("<div class='section info'>");
            out.println("<h2>📝 TinyMCE Configuration</h2>");
            out.println("<table>");
            out.println("<tr><th>Property</th><th>Value</th><th>Status</th></tr>");
            
            String tinyApiKey = ConfigUtil.getTinyMCEApiKey();
            String tinyStatus = "no-api-key".equals(tinyApiKey) ? "⚠️ Not Configured" : "✅ Configured";
            out.println("<tr><td>API Key</td><td>" + maskSensitiveData(tinyApiKey) + "</td><td>" + tinyStatus + "</td></tr>");
            out.println("<tr><td>Version</td><td>" + ConfigUtil.getTinyMCEVersion() + "</td><td>✅ OK</td></tr>");
            out.println("</table>");
            out.println("</div>");
            
            // Test Google OAuth Configuration  
            out.println("<div class='section info'>");
            out.println("<h2>🔐 Google OAuth Configuration</h2>");
            out.println("<table>");
            out.println("<tr><th>Property</th><th>Value</th><th>Status</th></tr>");
            
            String clientId = ConfigUtil.getGoogleOAuthClientId();
            String clientSecret = ConfigUtil.getGoogleOAuthClientSecret();
            boolean isOAuthConfigured = ConfigUtil.isGoogleOAuthConfigured();
            
            out.println("<tr><td>Client ID</td><td>" + maskSensitiveData(clientId) + "</td><td>" + (clientId.isEmpty() ? "❌ Missing" : "✅ Present") + "</td></tr>");
            out.println("<tr><td>Client Secret</td><td>" + maskSensitiveData(clientSecret) + "</td><td>" + (clientSecret.isEmpty() ? "❌ Missing" : "✅ Present") + "</td></tr>");
            out.println("<tr><td>Redirect URI</td><td>" + ConfigUtil.getGoogleOAuthRedirectUri() + "</td><td>✅ OK</td></tr>");
            out.println("<tr><td>Grant Type</td><td>" + ConfigUtil.getGoogleOAuthGrantType() + "</td><td>✅ OK</td></tr>");
            out.println("<tr><td>Scope</td><td>" + ConfigUtil.getGoogleOAuthScope() + "</td><td>✅ OK</td></tr>");
            out.println("<tr><td><strong>Overall Status</strong></td><td><strong>" + (isOAuthConfigured ? "Configured" : "Not Configured") + "</strong></td><td><strong>" + (isOAuthConfigured ? "✅ Ready" : "⚠️ Needs Setup") + "</strong></td></tr>");
            out.println("</table>");
            out.println("</div>");
            
            // Test Backward Compatibility
            out.println("<div class='section info'>");
            out.println("<h2>🔄 Backward Compatibility Test</h2>");
            out.println("<p>Testing OAuthConfig (old class) still works:</p>");
            out.println("<table>");
            out.println("<tr><th>Method</th><th>Value</th><th>Status</th></tr>");
            
            try {
                String oldClientId = OAuthConfig.getGoogleClientId();
                String oldClientSecret = OAuthConfig.getGoogleClientSecret();
                boolean oldIsConfigured = OAuthConfig.isConfigured();
                
                out.println("<tr><td>OAuthConfig.getGoogleClientId()</td><td>" + maskSensitiveData(oldClientId) + "</td><td>✅ Working</td></tr>");
                out.println("<tr><td>OAuthConfig.getGoogleClientSecret()</td><td>" + maskSensitiveData(oldClientSecret) + "</td><td>✅ Working</td></tr>");
                out.println("<tr><td>OAuthConfig.isConfigured()</td><td>" + oldIsConfigured + "</td><td>✅ Working</td></tr>");
            } catch (Exception e) {
                out.println("<tr><td colspan='3'>❌ Error: " + e.getMessage() + "</td></tr>");
            }
            out.println("</table>");
            out.println("</div>");
            
            // Application Configuration
            out.println("<div class='section info'>");
            out.println("<h2>⚙️ Application Configuration</h2>");
            out.println("<table>");
            out.println("<tr><th>Property</th><th>Value</th></tr>");
            out.println("<tr><td>App Name</td><td>" + ConfigUtil.getAppName() + "</td></tr>");
            out.println("<tr><td>App Version</td><td>" + ConfigUtil.getAppVersion() + "</td></tr>");
            out.println("<tr><td>Debug Mode</td><td>" + ConfigUtil.isDebugMode() + "</td></tr>");
            out.println("<tr><td>Max Upload Size</td><td>" + formatBytes(ConfigUtil.getMaxUploadSize()) + "</td></tr>");
            out.println("<tr><td>Allowed Upload Types</td><td>" + ConfigUtil.getAllowedUploadTypes() + "</td></tr>");
            out.println("<tr><td>Blog Posts Per Page</td><td>" + ConfigUtil.getBlogPostsPerPage() + "</td></tr>");
            out.println("</table>");
            out.println("</div>");
            
            // Configuration Status Summary
            out.println("<div class='section " + (isOAuthConfigured ? "success" : "error") + "'>");
            out.println("<h2>📊 Configuration Status Summary</h2>");
            if (isOAuthConfigured) {
                out.println("<p><strong>✅ All Good!</strong> Your configuration is properly set up.</p>");
                out.println("<ul>");
                out.println("<li>✅ Google OAuth is configured</li>");
                out.println("<li>✅ TinyMCE " + ("no-api-key".equals(tinyApiKey) ? "using free tier" : "API key configured") + "</li>");
                out.println("<li>✅ Backward compatibility maintained</li>");
                out.println("</ul>");
            } else {
                out.println("<p><strong>⚠️ Configuration Needed</strong></p>");
                out.println("<ul>");
                out.println("<li>❌ Google OAuth needs setup</li>");
                out.println("<li>" + ("no-api-key".equals(tinyApiKey) ? "⚠️ TinyMCE using free tier (consider getting API key)" : "✅ TinyMCE configured") + "</li>");
                out.println("</ul>");
                out.println("<p><strong>Next Steps:</strong></p>");
                out.println("<ol>");
                out.println("<li>Update <code>src/java/config.properties</code></li>");
                out.println("<li>Add your Google OAuth credentials</li>");
                out.println("<li>Restart the server</li>");
                out.println("<li>Refresh this page to verify</li>");
                out.println("</ol>");
            }
            out.println("</div>");
            
            // Instructions
            out.println("<div class='section info'>");
            out.println("<h2>📖 Quick Setup Instructions</h2>");
            out.println("<p><strong>File Location:</strong> <code>src/java/config.properties</code></p>");
            out.println("<pre>");
            out.println("# Google OAuth 2.0 Configuration");
            out.println("google.oauth.client.id=your-google-client-id");
            out.println("google.oauth.client.secret=your-google-client-secret");
            out.println("google.oauth.redirect.uri=http://localhost:8080/DuAnCaCanh/googlecallback");
            out.println("");
            out.println("# TinyMCE Configuration");
            out.println("tinymce.api.key=your-tinymce-api-key");
            out.println("</pre>");
            out.println("<p><strong>Documentation:</strong></p>");
            out.println("<ul>");
            out.println("<li><a href='OAUTH_MIGRATION_GUIDE.md'>OAuth Migration Guide</a></li>");
            out.println("<li><a href='TINYMCE_API_SETUP.md'>TinyMCE Setup Guide</a></li>");
            out.println("</ul>");
            out.println("</div>");
            
            out.println("<hr>");
            out.println("<p><em>Generated at: " + new java.util.Date() + "</em></p>");
            out.println("<p><a href='javascript:window.location.reload()'>🔄 Refresh Page</a></p>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     * Mask sensitive data for display
     */
    private String maskSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return "<em>Empty</em>";
        }
        if (data.length() <= 10) {
            return data; // Short values like "no-api-key" show as-is
        }
        // Show first 6 and last 4 characters
        return data.substring(0, 6) + "..." + data.substring(data.length() - 4);
    }
    
    /**
     * Format bytes to human readable format
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
