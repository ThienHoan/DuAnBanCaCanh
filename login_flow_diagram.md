# Diagram Luồng Đăng Nhập - Dự Án Bán Cá Cảnh

## 1. Sequence Diagram - Luồng đăng nhập chi tiết

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Browser as 🌐 Browser
    participant LoginJSP as 📄 login.jsp
    participant LoginServlet as ⚙️ LoginServlet
    participant UserDAO as 🗃️ UserDAO
    participant PasswordUtil as 🔐 PasswordEncryption
    participant Database as 🗄️ Database
    participant Session as 🍪 Session/Cookie
    
    Note over User, Session: Giai đoạn 1: Display homepage
    
    User->>Browser: Truy cập /login
    Browser->>LoginServlet: GET /login
    
    Note over LoginServlet: Kiểm tra Remember Me cookies
    LoginServlet->>LoginServlet: Đọc cookies (remembered_username, remembered_password)
    LoginServlet->>LoginServlet: Decode Base64 password
    LoginServlet->>LoginJSP: Forward với remembered data
    LoginJSP-->>Browser: Display form login 
    Browser-->>User: Trang đăng nhập
    
    Note over User, Session: Giai đoạn 2: Xử lý đăng nhập
    
    User->>Browser: Nhập username/password + Remember Me
    Browser->>LoginServlet: POST /login {user, pass, remember}
    
    LoginServlet->>LoginServlet: Set encoding UTF-8
    LoginServlet->>UserDAO: checkLogin(username, password)
    
    Note over UserDAO: Truy vấn database
    UserDAO->>Database: SELECT * FROM Users WHERE (username=? OR email=?) AND is_deleted=0 AND status='active'
    Database-->>UserDAO: User record hoặc null
    
    alt User tồn tại
        UserDAO->>PasswordUtil: checkPassword(inputPassword, storedPassword)
        PasswordUtil-->>UserDAO: true/false
        
        alt Password đúng
            UserDAO->>Database: UPDATE last_login
            UserDAO->>UserDAO: Tạo User object
            UserDAO-->>LoginServlet: User object
            
            LoginServlet->>Session: Lưu user vào session
            
            alt Remember Me = "1"
                LoginServlet->>LoginServlet: Tạo cookies (username, Base64 password)
                LoginServlet->>LoginServlet: Set cookie 30 ngày, HttpOnly
                LoginServlet->>Browser: Set cookies
            else Remember Me không chọn
                LoginServlet->>LoginServlet: Xóa existing cookies
                LoginServlet->>Browser: Clear cookies
            end
            
            alt User role = "admin"
                LoginServlet-->>Browser: Redirect to /home (admin)
            else User role = "customer"
                LoginServlet-->>Browser: Redirect to /home (customer)
            end
            
            Browser-->>User: Chuyển đến trang chủ
            
        else Password sai
            UserDAO-->>LoginServlet: null
            LoginServlet->>LoginServlet: Set error message
            LoginServlet->>LoginJSP: Forward với error + entered data
            LoginJSP-->>Browser: Hiển thị form với lỗi
            Browser-->>User: "Tên đăng nhập hoặc mật khẩu không đúng!"
        end
    else User không tồn tại
        UserDAO-->>LoginServlet: null
        LoginServlet->>LoginServlet: Set error message
        LoginServlet->>LoginJSP: Forward với error
        LoginJSP-->>Browser: Hiển thị form với lỗi
        Browser-->>User: "Tên đăng nhập hoặc mật khẩu không đúng!"
    end
```

## 2. Flowchart - Luồng quyết định

```mermaid
flowchart TD
    Start([Bắt đầu]) --> CheckRequest{Loại request?}
    
    CheckRequest -->|GET| CheckCookies[Kiểm tra Remember Me cookies]
    CheckCookies --> DecodeCookies[Decode Base64 password]
    DecodeCookies --> ShowLogin[Hiển thị form login.jsp<br/>với thông tin đã lưu]
    
    CheckRequest -->|POST| GetFormData[Lấy username, password, remember]
    GetFormData --> ValidateDB[Truy vấn Database]
    
    ValidateDB --> UserExists{User tồn tại?}
    UserExists -->|Không| LoginFailed[Đăng nhập thất bại]
    UserExists -->|Có| CheckPassword{Password đúng?}
    
    CheckPassword -->|Không| LoginFailed
    CheckPassword -->|Có| CreateSession[Tạo session user]
    
    CreateSession --> CheckRemember{Remember Me?}
    CheckRemember -->|Có| SetCookies[Tạo cookies 30 ngày<br/>username + Base64 password]
    CheckRemember -->|Không| ClearCookies[Xóa existing cookies]
    
    SetCookies --> UpdateLogin[Cập nhật last_login]
    ClearCookies --> UpdateLogin
    
    UpdateLogin --> CheckRole{Role của user?}
    CheckRole -->|admin| RedirectAdmin[Redirect /home - Admin]
    CheckRole -->|customer| RedirectCustomer[Redirect /home - Customer]
    
    LoginFailed --> SetError[Set error message]
    SetError --> ShowLoginError[Hiển thị login.jsp với lỗi]
    
    RedirectAdmin --> End([Kết thúc])
    RedirectCustomer --> End
    ShowLogin --> End
    ShowLoginError --> End
```

## 3. Component Diagram - Kiến trúc hệ thống

```mermaid
graph TB
    subgraph "Presentation Layer"
        LoginJSP[login.jsp<br/>📄 Giao diện đăng nhập]
        Browser[🌐 Trình duyệt]
    end
    
    subgraph "Controller Layer"
        LoginServlet[LoginServlet<br/>⚙️ Xử lý logic đăng nhập]
    end
    
    subgraph "Service Layer"
        UserDAO[UserDAO<br/>🗃️ Data Access Object]
        PasswordUtil[PasswordEncryption<br/>🔐 Mã hóa mật khẩu]
        SessionUtil[SessionUtils<br/>🍪 Quản lý session]
    end
    
    subgraph "Data Layer"
        Database[🗄️ SQL Server Database<br/>Users Table]
    end
    
    subgraph "Security Components"
        Cookies[🍪 Remember Me Cookies<br/>Base64 Encoding]
        Session[📋 HTTP Session]
    end
    
    Browser <--> LoginJSP
    LoginJSP <--> LoginServlet
    LoginServlet <--> UserDAO
    LoginServlet <--> PasswordUtil
    LoginServlet <--> SessionUtil
    UserDAO <--> Database
    LoginServlet <--> Cookies
    LoginServlet <--> Session
```

## 4. Các đặc điểm bảo mật

### 🔐 Bảo mật mật khẩu:
- Sử dụng `PasswordEncryption.checkPassword()` để verify
- Mật khẩu được hash trước khi lưu DB

### 🍪 Remember Me:
- Lưu username và password (Base64) trong cookies
- Thời gian sống: 30 ngày
- HttpOnly flag để tránh XSS
- Path "/" cho toàn bộ domain

### 🛡️ Validation:
- Kiểm tra `is_deleted = 0` và `status = 'active'`
- UTF-8 encoding support
- SQL injection protection với PreparedStatement

### 📋 Session Management:
- Lưu User object trong session
- Redirect theo role (admin/customer)
- Update last_login timestamp

---

# Class Diagram, Specifications và Sequence Diagrams

## 5. Class Diagram - Kiến trúc tổng thể

```mermaid
classDiagram
    %% Entity Classes
    class User {
        -int userId
        -String username
        -String password
        -String email
        -String phone
        -String fullName
        -String role
        -String status
        -Timestamp createdAt
        -Timestamp lastLogin
        -String avatar
        -String googleId
        -boolean isDeleted
        +User()
        +User(username, password, email, phone, fullName)
        +getUserId() int
        +setUserId(int userId) void
        +getUsername() String
        +setUsername(String username) void
        +checkPassword(String password) boolean
        +isAdmin() boolean
        +isCustomer() boolean
    }

    class Product {
        -int productId
        -Integer categoryId
        -String name
        -String description
        -String shortDescription
        -BigDecimal price
        -BigDecimal salePrice
        -int quantity
        -String sku
        -String status
        -Integer featured
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer isDeleted
        +Product()
        +getProductId() int
        +getName() String
        +getPrice() BigDecimal
        +isAvailable() boolean
        +isFeatured() boolean
    }

    class Category {
        -int categoryId
        -Integer parentId
        -String name
        -String description
        -String status
        -int displayOrder
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Integer isDeleted
        +Category()
        +getCategoryId() int
        +getName() String
        +isActive() boolean
        +hasSubCategories() boolean
    }

    class BlogPost {
        -int postId
        -String title
        -String content
        -String summary
        -String thumbnail
        -String status
        -int authorId
        -int categoryId
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -boolean isDeleted
        +BlogPost()
        +getPostId() int
        +getTitle() String
        +isPublished() boolean
    }

    class Wishlist {
        -int wishlistId
        -int userId
        -int productId
        -LocalDateTime createdAt
        +Wishlist()
        +getWishlistId() int
        +getUserId() int
        +getProductId() int
    }

    class Order {
        -int orderId
        -int userId
        -String orderNumber
        -BigDecimal totalAmount
        -String status
        -String paymentMethod
        -String shippingAddress
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +Order()
        +getOrderId() int
        +getTotalAmount() BigDecimal
        +getStatus() String
        +isCompleted() boolean
    }

    %% Controller Classes
    class LoginServlet {
        +doGet(HttpServletRequest, HttpServletResponse) void
        +doPost(HttpServletRequest, HttpServletResponse) void
        -clearRememberMeCookies(HttpServletResponse) void
    }

    class ProfileController {
        -UserService userService
        -UserDAO userDAO
        +init() void
        +doGet(HttpServletRequest, HttpServletResponse) void
        +doPost(HttpServletRequest, HttpServletResponse) void
        -handleAvatarUpload(Part) String
        -validateProfileData(HttpServletRequest) boolean
    }

    class BlogController {
        -BlogService blogService
        +doGet(HttpServletRequest, HttpServletResponse) void
        +doPost(HttpServletRequest, HttpServletResponse) void
    }

    %% Service Interface Classes
    class UserService {
        <<interface>>
        +registerUser(User) boolean
        +login(String, String) User
        +loginWithGoogle(String, String, String, String) User
        +getUserById(int) User
        +updateProfile(User) boolean
        +changePassword(int, String, String) boolean
        +isUsernameExists(String) boolean
        +isEmailExists(String) boolean
    }

    class UserServiceImpl {
        -UserDAO userDAO
        +UserServiceImpl()
        +registerUser(User) boolean
        +login(String, String) User
        +updateProfile(User) boolean
        +changePassword(int, String, String) boolean
    }

    %% DAO Interface Classes
    class UserDAO {
        <<interface>>
        +createUser(User) boolean
        +getUserById(int) User
        +getUserByUsername(String) User
        +getUserByEmail(String) User
        +updateUser(User) boolean
        +deleteUser(int) boolean
        +checkLogin(String, String) User
        +updateLastLogin(int) void
    }

    class UserDAOImpl {
        +checkLogin(String, String) User
        +registerUser(User) boolean
        +getUserById(int) User
        +updateUser(User) boolean
        +updateLastLogin(int) void
        +isUsernameExists(String) boolean
        +isEmailExists(String) boolean
    }

    %% Utility Classes
    class PasswordEncryption {
        +hashPassword(String) String
        +checkPassword(String, String) boolean
        +generateSalt() String
    }

    class DBContext {
        +getConnection() Connection
        +closeConnection(Connection) void
    }

    class SessionUtils {
        +setUser(HttpSession, User) void
        +getUser(HttpSession) User
        +removeUser(HttpSession) void
        +isLoggedIn(HttpSession) boolean
    }

    %% Relationships
    User ||--o{ Wishlist : "has"
    User ||--o{ Order : "places"
    User ||--o{ BlogPost : "writes"
    Product ||--o{ Wishlist : "in"
    Category ||--o{ Product : "contains"
    Category ||--o{ BlogPost : "categorizes"
    
    LoginServlet --> UserDAO : "uses"
    LoginServlet --> PasswordEncryption : "uses"
    ProfileController --> UserService : "uses"
    BlogController --> BlogService : "uses"
    
    UserServiceImpl ..|> UserService : "implements"
    UserDAOImpl ..|> UserDAO : "implements"
    UserServiceImpl --> UserDAO : "uses"
    UserDAOImpl --> DBContext : "uses"
    UserDAOImpl --> PasswordEncryption : "uses"
```

## 6. Class Specifications

### 6.1 Entity Classes

#### **User Class**
```java
/**
 * Represents a user in the fish shop system
 * Supports both regular users and Google OAuth users
 */
public class User {
    // Fields: userId, username, password, email, phone, fullName, role, status, 
    //         createdAt, lastLogin, avatar, googleId, isDeleted
    
    // Key Methods:
    // - Constructor validation
    // - Role-based access control
    // - Password security
    // - Google OAuth integration
}
```

**Responsibilities:**
- Store user information and credentials
- Handle role-based permissions (admin/customer)
- Support OAuth authentication
- Maintain audit trail (createdAt, lastLogin)

**Key Attributes:**
- `userId`: Primary key, auto-generated
- `role`: "admin" | "customer" 
- `status`: "active" | "inactive" | "suspended"
- `googleId`: For Google OAuth users
- `isDeleted`: Soft delete flag

#### **Product Class**
```java
/**
 * Represents a fish product in the shop
 * Supports pricing, inventory, and categorization
 */
public class Product {
    // Fields: productId, categoryId, name, description, shortDescription,
    //         price, salePrice, quantity, sku, status, featured, timestamps
    
    // Key Methods:
    // - Price calculation with sale pricing
    // - Inventory management
    // - Featured product handling
}
```

**Responsibilities:**
- Store product information and pricing
- Manage inventory levels
- Handle featured/promotional products
- Support categorization

### 6.2 Service Layer Classes

#### **UserService Interface**
```java
/**
 * Business logic layer for user operations
 * Defines contract for user management
 */
public interface UserService {
    // Authentication operations
    User login(String username, String password);
    User loginWithGoogle(String googleId, String email, String fullName, String avatar);
    boolean registerUser(User user);
    
    // Profile management
    User getUserById(int userId);
    boolean updateProfile(User user);
    boolean changePassword(int userId, String oldPassword, String newPassword);
    
    // Validation operations
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}
```

**Responsibilities:**
- Define business rules for user operations
- Provide abstraction between controllers and data access
- Handle complex business logic validation
- Coordinate multiple DAO operations if needed

#### **UserServiceImpl Class**
```java
/**
 * Implementation of UserService interface
 * Contains business logic and validation rules
 */
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    
    // Implements all interface methods with:
    // - Input validation
    // - Business rule enforcement
    // - Error handling
    // - Transaction coordination
}
```

### 6.3 Data Access Layer Classes

#### **UserDAO Interface**
```java
/**
 * Data access contract for user operations
 * Defines database operations without implementation details
 */
public interface UserDAO {
    // CRUD operations
    boolean createUser(User user);
    User getUserById(int userId);
    User getUserByUsername(String username);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    
    // Authentication specific
    User checkLogin(String username, String password);
    void updateLastLogin(int userId);
    
    // Query operations
    List<User> getAllUsers();
    boolean isUsernameExists(String username);
}
```

#### **UserDAOImpl Class**
```java
/**
 * SQL Server implementation of UserDAO
 * Handles all database interactions for user data
 */
public class UserDAOImpl implements UserDAO {
    // Uses PreparedStatement for SQL injection protection
    // Implements connection pooling via DBContext
    // Handles database-specific error scenarios
    // Supports both username and email login
}
```

### 6.4 Controller Classes

#### **LoginServlet**
```java
/**
 * Handles user authentication requests
 * Supports both regular login and Remember Me functionality
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // GET: Display login form with remembered credentials
    // POST: Process login attempt with session management
    // Includes cookie management for Remember Me feature
}
```

**Key Features:**
- Remember Me with Base64 encoding
- UTF-8 support for Vietnamese
- Role-based redirection
- Security cookie settings (HttpOnly)

## 7. Detailed Sequence Diagrams

### 7.1 User Registration Sequence

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Browser as 🌐 Browser
    participant RegisterServlet as ⚙️ RegisterServlet
    participant UserService as 🔧 UserService
    participant UserDAO as 🗃️ UserDAO
    participant PasswordUtil as 🔐 PasswordEncryption
    participant Database as 🗄️ Database
    
    User->>Browser: Fill registration form
    Browser->>RegisterServlet: POST /register {username, email, password, phone, fullName}
    
    RegisterServlet->>RegisterServlet: Validate input format
    RegisterServlet->>UserService: registerUser(user)
    
    UserService->>UserDAO: isUsernameExists(username)
    UserDAO->>Database: SELECT COUNT(*) FROM Users WHERE username=?
    Database-->>UserDAO: count
    UserDAO-->>UserService: boolean exists
    
    alt Username exists
        UserService-->>RegisterServlet: false (username taken)
        RegisterServlet-->>Browser: Error: "Username already exists"
    else Username available
        UserService->>UserDAO: isEmailExists(email)
        UserDAO->>Database: SELECT COUNT(*) FROM Users WHERE email=?
        Database-->>UserDAO: count
        UserDAO-->>UserService: boolean exists
        
        alt Email exists
            UserService-->>RegisterServlet: false (email taken)
            RegisterServlet-->>Browser: Error: "Email already exists"
        else Email available
            UserService->>PasswordUtil: hashPassword(password)
            PasswordUtil-->>UserService: hashedPassword
            UserService->>UserDAO: createUser(user)
            UserDAO->>Database: INSERT INTO Users VALUES(...)
            Database-->>UserDAO: success/failure
            UserDAO-->>UserService: boolean result
            UserService-->>RegisterServlet: boolean result
            RegisterServlet-->>Browser: Success: "Registration successful"
            Browser-->>User: Redirect to login page
        end
    end
```

### 7.2 Profile Update Sequence

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Browser as 🌐 Browser
    participant ProfileController as ⚙️ ProfileController
    participant UserService as 🔧 UserService
    participant UserDAO as 🗃️ UserDAO
    participant FileUpload as 📁 File Upload
    participant Database as 🗄️ Database
    
    User->>Browser: Access /profile
    Browser->>ProfileController: GET /profile
    ProfileController->>ProfileController: Check session authentication
    ProfileController->>UserDAO: getUserById(userId)
    UserDAO->>Database: SELECT * FROM Users WHERE user_id=?
    Database-->>UserDAO: user data
    UserDAO-->>ProfileController: User object
    ProfileController-->>Browser: Display profile form with current data
    
    User->>Browser: Update profile + upload avatar
    Browser->>ProfileController: POST /profile {fullName, email, phone, avatar_file}
    
    ProfileController->>ProfileController: Validate session
    ProfileController->>ProfileController: Validate input data
    
    alt Avatar uploaded
        ProfileController->>FileUpload: handleAvatarUpload(avatarPart)
        FileUpload->>FileUpload: Validate file type (jpg, png, gif)
        FileUpload->>FileUpload: Validate file size (<10MB)
        FileUpload->>FileUpload: Generate unique filename with UUID
        FileUpload->>FileUpload: Save to uploads/avatars/ directory
        FileUpload-->>ProfileController: avatar filename
    end
    
    ProfileController->>UserService: updateProfile(user)
    UserService->>UserService: Validate business rules
    UserService->>UserDAO: updateUser(user)
    UserDAO->>Database: UPDATE Users SET fullName=?, email=?, phone=?, avatar=? WHERE user_id=?
    Database-->>UserDAO: success/failure
    UserDAO-->>UserService: boolean result
    UserService-->>ProfileController: boolean result
    
    alt Update successful
        ProfileController->>ProfileController: Update session user object
        ProfileController-->>Browser: Success message + redirect
        Browser-->>User: "Profile updated successfully"
    else Update failed
        ProfileController-->>Browser: Error message
        Browser-->>User: "Failed to update profile"
    end
```

### 7.3 Password Change Sequence

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Browser as 🌐 Browser
    participant ProfileController as ⚙️ ProfileController
    participant UserService as 🔧 UserService
    participant UserDAO as 🗃️ UserDAO
    participant PasswordUtil as 🔐 PasswordEncryption
    participant Database as 🗄️ Database
    
    User->>Browser: Submit change password form
    Browser->>ProfileController: POST /profile/change-password {oldPassword, newPassword, confirmPassword}
    
    ProfileController->>ProfileController: Validate session
    ProfileController->>ProfileController: Validate input (newPassword == confirmPassword)
    
    ProfileController->>UserService: changePassword(userId, oldPassword, newPassword)
    UserService->>UserDAO: getUserById(userId)
    UserDAO->>Database: SELECT password FROM Users WHERE user_id=?
    Database-->>UserDAO: current hashed password
    UserDAO-->>UserService: current password hash
    
    UserService->>PasswordUtil: checkPassword(oldPassword, currentHash)
    PasswordUtil-->>UserService: boolean isValid
    
    alt Old password incorrect
        UserService-->>ProfileController: false
        ProfileController-->>Browser: Error: "Current password is incorrect"
    else Old password correct
        UserService->>PasswordUtil: hashPassword(newPassword)
        PasswordUtil-->>UserService: newHashedPassword
        UserService->>UserDAO: updatePassword(userId, newHashedPassword)
        UserDAO->>Database: UPDATE Users SET password=? WHERE user_id=?
        Database-->>UserDAO: success/failure
        UserDAO-->>UserService: boolean result
        UserService-->>ProfileController: boolean result
        
        alt Password updated successfully
            ProfileController-->>Browser: Success: "Password changed successfully"
            Browser-->>User: Success message + logout prompt
        else Password update failed
            ProfileController-->>Browser: Error: "Failed to change password"
            Browser-->>User: Error message
        end
    end
```

### 7.4 Google OAuth Login Sequence

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Browser as 🌐 Browser
    participant GoogleOAuth as 🔍 Google OAuth
    participant GoogleLoginServlet as ⚙️ GoogleLoginServlet
    participant UserService as 🔧 UserService
    participant UserDAO as 🗃️ UserDAO
    participant Database as 🗄️ Database
    participant Session as 🍪 Session
    
    User->>Browser: Click "Login with Google"
    Browser->>GoogleOAuth: Redirect to Google OAuth
    GoogleOAuth-->>User: Google login form
    User->>GoogleOAuth: Enter Google credentials
    GoogleOAuth->>GoogleOAuth: Authenticate user
    GoogleOAuth->>Browser: Redirect with authorization code
    Browser->>GoogleLoginServlet: GET /google-login?code=xyz
    
    GoogleLoginServlet->>GoogleOAuth: Exchange code for access token
    GoogleOAuth-->>GoogleLoginServlet: Access token + user info
    GoogleLoginServlet->>GoogleLoginServlet: Extract {googleId, email, fullName, avatar}
    
    GoogleLoginServlet->>UserService: loginWithGoogle(googleId, email, fullName, avatar)
    UserService->>UserDAO: getUserByGoogleId(googleId)
    UserDAO->>Database: SELECT * FROM Users WHERE google_id=?
    Database-->>UserDAO: user data or null
    UserDAO-->>UserService: User object or null
    
    alt Existing Google user
        UserService->>UserDAO: updateLastLogin(userId)
        UserDAO->>Database: UPDATE Users SET last_login=GETDATE() WHERE user_id=?
        UserService-->>GoogleLoginServlet: User object
    else New Google user
        UserService->>UserDAO: createGoogleUser(googleId, email, fullName, avatar)
        UserDAO->>Database: INSERT INTO Users (google_id, email, full_name, avatar, role, status) VALUES(...)
        Database-->>UserDAO: new user_id
        UserDAO->>UserDAO: Retrieve created user
        UserDAO-->>UserService: New User object
        UserService-->>GoogleLoginServlet: User object
    end
    
    GoogleLoginServlet->>Session: Set user in session
    GoogleLoginServlet-->>Browser: Redirect to /home
    Browser-->>User: Display homepage (logged in)
```

## 8. Architecture Summary

### 8.1 Layer Architecture
- **Presentation Layer**: JSP pages, Servlets (Controllers)
- **Business Layer**: Service interfaces and implementations
- **Data Access Layer**: DAO interfaces and implementations  
- **Database Layer**: SQL Server database

### 8.2 Design Patterns Used
- **MVC Pattern**: Controllers, Models, Views separation
- **DAO Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic encapsulation
- **Singleton Pattern**: Database connection management
- **Strategy Pattern**: Multiple authentication methods (regular/Google)

### 8.3 Security Features
- **Password Hashing**: BCrypt/PBKDF2 encryption
- **SQL Injection Protection**: PreparedStatement usage
- **Session Management**: HttpSession with proper timeout
- **Remember Me**: Secure cookie implementation with HttpOnly flag
- **Input Validation**: Both client-side and server-side validation
- **Soft Delete**: Logical deletion with isDeleted flag
