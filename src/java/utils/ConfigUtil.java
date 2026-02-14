package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration utility for managing application properties
 * Handles API keys, application settings, and other configurations
 * 
 * @author Fish Shop Team
 * @version 1.0
 * @since June 2025
 */
public class ConfigUtil {
    
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    /**
     * Load properties from config file
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Warning: " + CONFIG_FILE + " not found in classpath");
                // Load default properties
                loadDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading " + CONFIG_FILE + ": " + e.getMessage());
            loadDefaultProperties();
        }
    }
    
    /**
     * Load default properties when config file is not found
     */
    private static void loadDefaultProperties() {
        properties.setProperty("tinymce.api.key", "no-api-key");
        properties.setProperty("tinymce.version", "6");
        properties.setProperty("app.name", "Fish Shop");
        properties.setProperty("app.version", "1.0");
        properties.setProperty("app.debug", "false");
        properties.setProperty("upload.max.size", "10485760");
        properties.setProperty("upload.allowed.types", "jpg,jpeg,png,gif,pdf,doc,docx");
        properties.setProperty("blog.posts.per.page", "9");
        properties.setProperty("blog.featured.posts.count", "3");
        properties.setProperty("blog.excerpt.length", "150");
        properties.setProperty("google.oauth.client.id", "");
        properties.setProperty("google.oauth.client.secret", "");
        properties.setProperty("google.oauth.redirect.uri", "http://localhost:8080/DuAnBanCaCanh/googlecallback");
        properties.setProperty("google.oauth.grant.type", "authorization_code");
        properties.setProperty("google.oauth.scope", "openid email profile");
    }
    
    /**
     * Get property value by key
     * 
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default value
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get integer property value
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found or invalid
     * @return Integer property value
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get boolean property value
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Boolean property value
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // Specific getters for common configurations
    
    /**
     * Get TinyMCE API Key
     * 
     * @return TinyMCE API key
     */
    public static String getTinyMCEApiKey() {
        return getProperty("tinymce.api.key", "no-api-key");
    }
    
    /**
     * Get TinyMCE Version
     * 
     * @return TinyMCE version
     */
    public static String getTinyMCEVersion() {
        return getProperty("tinymce.version", "6");
    }
    
    /**
     * Get application name
     * 
     * @return Application name
     */
    public static String getAppName() {
        return getProperty("app.name", "Fish Shop");
    }
    
    /**
     * Get application version
     * 
     * @return Application version
     */
    public static String getAppVersion() {
        return getProperty("app.version", "1.0");
    }
    
    /**
     * Check if debug mode is enabled
     * 
     * @return true if debug mode is enabled
     */
    public static boolean isDebugMode() {
        return getBooleanProperty("app.debug", false);
    }
    
    /**
     * Get maximum upload file size in bytes
     * 
     * @return Maximum upload size
     */
    public static long getMaxUploadSize() {
        return getIntProperty("upload.max.size", 10485760);
    }
    
    /**
     * Get allowed upload file types
     * 
     * @return Comma-separated list of allowed file extensions
     */
    public static String getAllowedUploadTypes() {
        return getProperty("upload.allowed.types", "jpg,jpeg,png,gif,pdf,doc,docx");
    }
    
    /**
     * Get number of blog posts per page
     * 
     * @return Posts per page
     */
    public static int getBlogPostsPerPage() {
        return getIntProperty("blog.posts.per.page", 9);
    }
    
    /**
     * Get number of featured posts to display
     * 
     * @return Featured posts count
     */
    public static int getFeaturedPostsCount() {
        return getIntProperty("blog.featured.posts.count", 3);
    }
    
    /**
     * Get blog excerpt length
     * 
     * @return Excerpt length in characters
     */
    public static int getBlogExcerptLength() {
        return getIntProperty("blog.excerpt.length", 150);
    }
    
    /**
     * Get Google OAuth Client ID
     * 
     * @return Google OAuth Client ID
     */
    public static String getGoogleOAuthClientId() {
        return getProperty("google.oauth.client.id", "");
    }
    
    /**
     * Get Google OAuth Client Secret
     * 
     * @return Google OAuth Client Secret
     */
    public static String getGoogleOAuthClientSecret() {
        return getProperty("google.oauth.client.secret", "");
    }
    
    /**
     * Get Google OAuth Redirect URI
     * 
     * @return Google OAuth Redirect URI
     */
    public static String getGoogleOAuthRedirectUri() {
        return getProperty("google.oauth.redirect.uri", "http://localhost:8080/DuAnBanCaCanh/googlecallback");
    }
    
    /**
     * Get Google OAuth Grant Type
     * 
     * @return Google OAuth Grant Type
     */
    public static String getGoogleOAuthGrantType() {
        return getProperty("google.oauth.grant.type", "authorization_code");
    }
    
    /**
     * Get Google OAuth Scope
     * 
     * @return Google OAuth Scope
     */
    public static String getGoogleOAuthScope() {
        return getProperty("google.oauth.scope", "openid email profile");
    }
    
    /**
     * Check if Google OAuth is configured
     * 
     * @return true if Google OAuth client ID and secret are configured
     */
    public static boolean isGoogleOAuthConfigured() {
        String clientId = getGoogleOAuthClientId();
        String clientSecret = getGoogleOAuthClientSecret();
        return !clientId.isEmpty() && !clientSecret.isEmpty() 
               && !clientId.equals("your-google-client-id") 
               && !clientSecret.equals("your-google-client-secret");
    }

    /**
     * Reload properties from file (useful for runtime configuration changes)
     */
    public static void reloadProperties() {
        loadProperties();
    }
    
    /**
     * Check if a property exists
     * 
     * @param key Property key
     * @return true if property exists
     */
    public static boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
    
    /**
     * Get all property keys
     * 
     * @return Set of all property keys
     */
    public static java.util.Set<String> getAllKeys() {
        return properties.stringPropertyNames();
    }
}
