package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuration class for OAuth settings
 */
@Deprecated
public class OAuthConfig {
    private static final Logger LOGGER = Logger.getLogger(OAuthConfig.class.getName());
    
    // Google OAuth URLs
    public static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    public static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    private static Properties oauthProperties;
    
    static {
        loadOAuthProperties();
    }
    
    /**
     * Load OAuth properties from oauth.properties file
     */
    private static void loadOAuthProperties() {
        oauthProperties = new Properties();
        try (InputStream input = OAuthConfig.class.getClassLoader().getResourceAsStream("oauth.properties")) {
            if (input != null) {
                oauthProperties.load(input);
                LOGGER.info("OAuth properties loaded successfully");
            } else {
                LOGGER.warning("oauth.properties file not found, using default values");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error loading oauth.properties", e);
        }
    }
    
    /**
     * Get Google Client ID
     */
    public static String getGoogleClientId() {
        return oauthProperties.getProperty("google.client.id", "");
    }
    
    /**
     * Get Google Client Secret
     */
    public static String getGoogleClientSecret() {
        return oauthProperties.getProperty("google.client.secret", "");
    }
    
    /**
     * Get Google Redirect URI
     */
    public static String getGoogleRedirectUri() {
        return oauthProperties.getProperty("google.redirect.uri", "http://localhost:8080/DuAnBanCaCanh/google-callback");
    }
    
    /**
     * Get Google Grant Type
     */
    public static String getGoogleGrantType() {
        return "authorization_code";
    }
    
    /**
     * Get Google Scope
     */
    public static String getGoogleScope() {
        return "openid email profile";
    }
    
    /**
     * Check if OAuth is properly configured
     */
    public static boolean isConfigured() {
        String clientId = getGoogleClientId();
        String clientSecret = getGoogleClientSecret();
        return clientId != null && !clientId.isEmpty() && 
               clientSecret != null && !clientSecret.isEmpty();
    }
    
    /**
     * Get response type for authorization code flow
     */
    public static String getResponseType() {
        return "code";
    }
}
