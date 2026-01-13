# TEST CASES CHI TIẾT - THIỆN (LEADER)
## AUTHENTICATION & ADMIN DASHBOARD

---

## 🔐 **AUTHENTICATION TESTING**

### **TC_AUTH_001 - Test Login with Valid Credentials**

**Test Case Description:** Verify user can login with valid username and password

**Pre-conditions:** 
- User account "testuser123" exists in Users table with password "password123"
- Application is accessible at http://localhost:8080/DuAnBanCaCanh

**Test Case Procedure:**
1. Open browser and navigate to application URL (http://localhost:8080/DuAnBanCaCanh)
2. Click on "Đăng nhập" link in header or navigate to /login
3. Wait for login page to load completely
4. Locate username input field with name="username"
5. Enter valid username: "testuser123"
6. Locate password input field with name="password"
7. Enter valid password: "password123"
8. Verify "Remember Me" checkbox is present but unchecked
9. Click "Đăng nhập" button
10. Wait for page redirect and verify URL changes
11. Check for success message or user dashboard display

**Expected Results:** 
- User successfully logged in
- Redirected to /dashboard for regular user or /admin-panel for admin
- User menu displays in header
- Session created in browser storage

---

### **TC_AUTH_002 - Test Login with Email**

**Test Case Description:** Verify user can login using email instead of username

**Pre-conditions:** 
- User account with email "testuser@gmail.com" exists in database

**Test Case Procedure:**
1. Navigate to login page at /login
2. Clear browser cache and cookies
3. Locate username input field
4. Enter valid email address: "testuser@gmail.com"
5. Enter corresponding password in password field
6. Click "Đăng nhập" button
7. Observe page behavior and check for redirect
8. Verify user session is created by checking session storage
9. Confirm user details appear in header navigation

**Expected Results:** 
- User successfully logged in using email
- Session created
- Dashboard accessible

---

### **TC_AUTH_003 - Test Login with Invalid Password**

**Test Case Description:** Verify system rejects login with wrong password

**Pre-conditions:** 
- User account "testuser123" exists with different password

**Test Case Procedure:**
1. Navigate to login page /login
2. Enter valid username: "testuser123"
3. Enter incorrect password: "wrongpassword"
4. Click "Đăng nhập" button
5. Wait for response (do not redirect)
6. Check for error message display
7. Verify error message text contains "Invalid credentials" or similar
8. Confirm user remains on login page (URL still /login)
9. Verify input fields are cleared or retain username only
10. Check that no user session is created

**Expected Results:** 
- Error message displayed
- User remains on login page
- No session created

---

### **TC_AUTH_004 - Test Login with Non-existent Account**

**Test Case Description:** Verify system rejects login with non-existent username

**Pre-conditions:** 
- Username "nonexistentuser999" does not exist in Users table

**Test Case Procedure:**
1. Navigate to login page /login
2. Enter non-existent username: "nonexistentuser999"
3. Enter any password: "anypassword123"
4. Click "Đăng nhập" button
5. Wait for server response
6. Check for appropriate error message
7. Verify message indicates user not found
8. Confirm URL remains /login
9. Test with different non-existent usernames to verify consistency

**Expected Results:** 
- Error message indicating account not found
- User remains on login page

---

### **TC_AUTH_005 - Test Remember Me Functionality**

**Test Case Description:** Verify remember me checkbox preserves login session

**Pre-conditions:** 
- User account exists
- Remember me feature implemented with cookies

**Test Case Procedure:**
1. Clear all browser cookies and session data
2. Navigate to login page /login
3. Enter valid credentials (username: "testuser123", password: "password123")
4. Check the "Remember Me" checkbox
5. Verify checkbox is checked/selected
6. Click "Đăng nhập" button
7. Wait for successful login and redirect
8. Close browser completely (all tabs and windows)
9. Wait 5 minutes
10. Reopen browser and navigate to application URL
11. Check if user is automatically logged in
12. Verify user session persists without re-login
13. Test accessing protected pages like /profile

**Expected Results:** 
- User remains logged in after browser restart
- Can access protected pages

---

### **TC_AUTH_006 - Test Google OAuth Login**

**Test Case Description:** Verify Google OAuth integration works correctly

**Pre-conditions:** 
- Google OAuth configured in oauth.properties
- Internet connection available

**Test Case Procedure:**
1. Navigate to login page /login
2. Locate "Login with Google" button
3. Click "Login with Google" button
4. Verify redirect to Google OAuth authorization page
5. Enter valid Google account credentials
6. Complete Google 2FA if required
7. Click "Allow" to authorize application access
8. Wait for redirect back to application
9. Verify user is logged in with Google account
10. Check user profile shows Google account information
11. Verify user record created in database if new user
12. Test logout and re-login with same Google account

**Expected Results:** 
- User logged in via Google account
- Profile created if new user
- Subsequent logins work smoothly

---

### **TC_AUTH_007 - Test Session Timeout**

**Test Case Description:** Verify session expires after configured timeout period

**Pre-conditions:** 
- User logged in
- Session timeout configured in web.xml

**Test Case Procedure:**
1. Login successfully with valid credentials
2. Note current session ID from browser developer tools
3. Navigate to a protected page like /profile
4. Wait for configured session timeout period (check web.xml for session-timeout)
5. After timeout period, try to refresh the page
6. Attempt to access another protected page like /dashboard
7. Verify redirect to login page occurs
8. Check session storage is cleared
9. Try to access admin pages if applicable
10. Confirm all protected resources require re-authentication

**Expected Results:** 
- Session expires
- User redirected to login page
- Must re-authenticate

---

## 📝 **REGISTRATION TESTING**

### **TC_REG_001 - Test Registration with Valid Data**

**Test Case Description:** Verify user registration with all valid information

**Pre-conditions:** 
- Email and username not already in use
- Registration page accessible

**Test Case Procedure:**
1. Navigate to registration page /register
2. Fill form fields with valid data:
   - Username: "newuser" + timestamp
   - Email: "newuser" + timestamp + "@test.com"
   - Full Name: "Test User Full Name"
   - Phone: "0123456789"
   - Password: "validPassword123"
   - Confirm Password: "validPassword123"
3. Check "I agree to terms and conditions" checkbox
4. Click "Đăng ký" button
5. Wait for registration process completion
6. Verify success message or redirect to login/dashboard
7. Check database for new user record
8. Test login with newly created credentials

**Expected Results:** 
- Account created successfully
- User can login with new credentials

---

### **TC_REG_002 - Test Registration with Existing Username**

**Test Case Description:** Verify system prevents duplicate username registration

**Pre-conditions:** 
- Username "testuser123" already exists in Users table

**Test Case Procedure:**
1. Navigate to registration page /register
2. Fill form with existing username:
   - Username: "testuser123" (existing user)
   - Email: "newemail@test.com" (unique email)
   - Fill other fields with valid data
3. Complete all required fields correctly
4. Check terms and conditions checkbox
5. Click "Đăng ký" button
6. Wait for server validation response
7. Check for error message about username availability
8. Verify form submission is rejected
9. Confirm no new user record created in database

**Expected Results:** 
- Error message indicating username already exists
- Registration rejected

---

### **TC_REG_003 - Test Registration with Existing Email**

**Test Case Description:** Verify system prevents duplicate email registration

**Pre-conditions:** 
- Email "testuser@gmail.com" already exists in Users table

**Test Case Procedure:**
1. Navigate to registration page /register
2. Fill form with existing email:
   - Username: "newunique123" (unique username)
   - Email: "testuser@gmail.com" (existing email)
   - Fill other fields with valid data
3. Complete password fields with matching passwords
4. Check terms agreement checkbox
5. Click "Đăng ký" button
6. Observe server response and error handling
7. Verify appropriate error message about email duplication
8. Check that registration process is halted

**Expected Results:** 
- Error message indicating email already registered
- Registration rejected

---

### **TC_REG_004 - Test Required Field Validation**

**Test Case Description:** Verify all required fields are validated

**Pre-conditions:** 
- Registration form has proper validation implemented

**Test Case Procedure:**
1. Navigate to registration page /register
2. Test each required field individually:
   a. Leave username empty, fill others - submit and check error
   b. Leave email empty, fill others - submit and check error
   c. Leave password empty, fill others - submit and check error
   d. Leave confirm password empty, fill others - submit and check error
   e. Leave full name empty, fill others - submit and check error
3. Test multiple empty fields simultaneously
4. Verify client-side validation (JavaScript) triggers before submission
5. Test server-side validation by disabling JavaScript
6. Check error message clarity and positioning

**Expected Results:** 
- Validation errors shown for empty required fields
- Form submission prevented

---

### **TC_REG_005 - Test Invalid Email Format**

**Test Case Description:** Verify email format validation works correctly

**Pre-conditions:** 
- Email validation implemented in both client and server side

**Test Case Procedure:**
1. Navigate to registration page /register
2. Test various invalid email formats:
   - "invalid.email" (no @ symbol)
   - "user@" (missing domain)
   - "@domain.com" (missing username)
   - "user@domain" (missing TLD)
   - "user@.com" (missing domain name)
   - "user name@domain.com" (space in email)
3. Fill other fields correctly for each test
4. Submit form and check validation response
5. Verify appropriate error messages for email format
6. Test valid email format to confirm validation works both ways

**Expected Results:** 
- Email format validation error displayed for invalid formats
- Valid emails accepted

---

### **TC_REG_006 - Test Password Mismatch**

**Test Case Description:** Verify password confirmation validation

**Pre-conditions:** 
- Password confirmation validation implemented

**Test Case Procedure:**
1. Navigate to registration page /register
2. Fill all fields correctly except passwords:
   - Password: "password123"
   - Confirm Password: "differentpassword"
3. Submit form
4. Check for password mismatch error
5. Test with various password combinations
6. Verify error message is clear
7. Test with matching passwords to confirm validation

**Expected Results:** 
- Error message when passwords don't match
- Form submission prevented

---

### **TC_REG_007 - Test Terms Agreement Checkbox**

**Test Case Description:** Verify terms and conditions agreement is required

**Pre-conditions:** 
- Terms agreement checkbox implemented

**Test Case Procedure:**
1. Navigate to registration page /register
2. Fill all fields correctly
3. Leave "I agree to terms and conditions" checkbox unchecked
4. Submit form
5. Check for error about agreeing to terms
6. Check the checkbox and resubmit
7. Verify registration proceeds

**Expected Results:** 
- Error when terms not agreed
- Registration successful when terms agreed

---

## 🔑 **PASSWORD MANAGEMENT**

### **TC_PWD_001 - Test Forgot Password Flow**

**Test Case Description:** Verify forgot password functionality sends reset email

**Pre-conditions:** 
- Email exists in database
- Email service configured (SMTP settings)

**Test Case Procedure:**
1. Navigate to login page /login
2. Click "Quên mật khẩu?" link
3. Verify redirect to /forgot-password page
4. Enter registered email address: "testuser@gmail.com"
5. Click "Gửi liên kết reset" button
6. Wait for processing and check success message
7. Check email inbox for password reset email
8. Verify email contains reset link with token
9. Check database for password reset token generation
10. Verify token expiration time is set appropriately

**Expected Results:** 
- Success message displayed
- Reset email sent with valid token

---

### **TC_PWD_002 - Test Password Reset with Valid Token**

**Test Case Description:** Verify password reset works with valid reset token

**Pre-conditions:** 
- Valid reset token generated and not expired

**Test Case Procedure:**
1. Complete forgot password flow to get reset token
2. Access reset link from email or construct URL with token
3. Navigate to /reset-password?token=VALID_TOKEN
4. Verify reset password form displays
5. Enter new password: "newPassword123"
6. Enter confirm password: "newPassword123"
7. Click "Đặt lại mật khẩu" button
8. Wait for processing completion
9. Verify success message displayed
10. Test login with old password (should fail)
11. Test login with new password (should succeed)
12. Verify token is invalidated after use

**Expected Results:** 
- Password updated successfully
- Can login with new password
- Old password no longer works

---

### **TC_PWD_003 - Test Password Reset with Expired Token**

**Test Case Description:** Verify expired reset tokens are rejected

**Pre-conditions:** 
- Password reset token exists but is expired

**Test Case Procedure:**
1. Generate password reset token
2. Wait for token expiration (or manually expire in database)
3. Try to access reset link with expired token
4. Navigate to /reset-password?token=EXPIRED_TOKEN
5. Verify error message about token expiration
6. Check that reset form is not displayed
7. Verify redirect to forgot password page or error page
8. Test that password cannot be changed with expired token

**Expected Results:** 
- Error message indicating token expired
- Password reset rejected

---

### **TC_PWD_004 - Test Change Password in Profile**

**Test Case Description:** Verify users can change password in their profile

**Pre-conditions:** 
- User logged in with access to profile page

**Test Case Procedure:**
1. Login with valid user credentials
2. Navigate to profile/settings page
3. Locate "Change Password" section
4. Enter current password
5. Enter new password
6. Confirm new password
7. Click "Update Password" button
8. Verify success message
9. Logout and test login with old password (should fail)
10. Login with new password (should succeed)

**Expected Results:** 
- Password changed successfully
- Old password invalidated
- New password works

---

### **TC_PWD_005 - Test Password Encryption/Hashing**

**Test Case Description:** Verify passwords are properly encrypted/hashed in database

**Pre-conditions:** 
- Database access available
- Password hashing implemented

**Test Case Procedure:**
1. Register new user with password "testPassword123"
2. Check Users table in database
3. Verify password field does not contain plain text
4. Verify password appears to be hashed/encrypted
5. Test login with the original password works
6. Change password and verify new hash is different
7. Check that hash algorithm is consistent (BCrypt, SHA-256, etc.)

**Expected Results:** 
- Passwords stored as hashes, not plain text
- Login works with original password
- Hash changes when password changes

---

## 🛡️ **AUTHORIZATION TESTING**

### **TC_AUTH_008 - Test Role-based Redirection**

**Test Case Description:** Verify users redirected based on their roles after login

**Pre-conditions:** 
- Users with different roles (admin, user) exist in database

**Test Case Procedure:**
1. Test with admin user:
   - Login with admin credentials (username: "admin", password: "admin123")
   - Verify redirect to /admin-panel or /admin-dashboard
   - Check admin navigation menu is visible
2. Test with regular user:
   - Logout current session
   - Login with regular user credentials
   - Verify redirect to /dashboard or /home
   - Check user navigation menu (no admin options)
3. Test role enforcement:
   - As regular user, try to access /admin-panel directly
   - Verify access denied or redirect to unauthorized page

**Expected Results:** 
- Admin users go to admin dashboard
- Regular users go to user dashboard
- Role restrictions enforced

---

### **TC_AUTH_009 - Test Admin Page Access with User Role**

**Test Case Description:** Verify regular users cannot access admin pages

**Pre-conditions:** 
- User logged in with regular role
- RoleBasedFilter configured

**Test Case Procedure:**
1. Login with regular user account (not admin role)
2. Verify successful login to user dashboard
3. Try to access admin pages directly via URL:
   - /admin-panel
   - /admin-dashboard
   - /admin-users
   - /admin-products
   - /admin-categories
4. For each URL, verify access is denied
5. Check for appropriate error message (403 Forbidden)
6. Verify redirect to unauthorized page or login
7. Test RoleBasedFilter is working correctly

**Expected Results:** 
- Access denied for all admin pages
- Appropriate error messages displayed

---

### **TC_AUTH_010 - Test Protected Page Access Without Login**

**Test Case Description:** Verify unauthenticated users cannot access protected pages

**Pre-conditions:** 
- No active user session
- AuthenticationFilter configured

**Test Case Procedure:**
1. Ensure no active user session (clear cookies/logout)
2. Try to access protected pages directly:
   - /dashboard
   - /profile
   - /admin-panel
   - /wishlist
   - /order-history
3. For each page, verify redirect to login page
4. Check URL parameters include returnUrl for proper redirect after login
5. Verify AuthenticationFilter is intercepting requests
6. Test that login page is accessible without authentication

**Expected Results:** 
- Redirected to login page for all protected resources

---

### **TC_AUTH_011 - Test RoleBasedFilter Functionality**

**Test Case Description:** Verify RoleBasedFilter correctly filters requests based on user roles

**Pre-conditions:** 
- RoleBasedFilter configured in web.xml
- Users with different roles exist

**Test Case Procedure:**
1. Test with admin user:
   - Login as admin
   - Access admin-only pages - should succeed
   - Access user pages - should succeed
2. Test with regular user:
   - Login as regular user
   - Access user pages - should succeed
   - Access admin pages - should be denied
3. Test filter configuration in web.xml
4. Verify filter is applied to correct URL patterns
5. Check that filter allows appropriate roles for each resource

**Expected Results:** 
- Filter correctly enforces role-based access control for all configured URLs

---

### **TC_AUTH_012 - Test AuthenticationFilter Functionality**

**Test Case Description:** Verify AuthenticationFilter properly manages session authentication

**Pre-conditions:** 
- AuthenticationFilter configured in web.xml

**Test Case Procedure:**
1. Test with valid session:
   - Login user
   - Access protected pages - should succeed
   - Verify filter allows authenticated requests
2. Test with invalid/expired session:
   - Clear session or wait for expiration
   - Access protected pages - should redirect to login
3. Test filter bypass for public pages:
   - Access login, register, home pages without authentication
   - Verify these pages are accessible
4. Test session validation logic
5. Verify proper redirect behavior

**Expected Results:** 
- Authenticated users can access protected resources
- Unauthenticated users redirected to login
- Public pages accessible without authentication

---

## 🎛️ **ADMIN DASHBOARD TESTING**

### **TC_ADMIN_001 - Test Admin Dashboard Display**

**Test Case Description:** Verify admin dashboard loads correctly with all components

**Pre-conditions:** 
- Admin user logged in
- Dashboard data available

**Test Case Procedure:**
1. Login with admin credentials (username: "admin")
2. Verify redirect to admin dashboard /admin-panel
3. Check page title displays "Admin Dashboard"
4. Verify navigation menu contains:
   - Dashboard link
   - Users management
   - Products management
   - Orders management
   - Categories management
   - Reports section
5. Check dashboard widgets display:
   - Total users count
   - Total products count
   - Total orders count
   - Revenue statistics
6. Verify all CSS and JavaScript files load without errors
7. Check responsive design on different screen sizes

**Expected Results:** 
- Dashboard displays with all components
- Navigation menu functional
- Statistics visible

---

### **TC_ADMIN_002 - Test Dashboard Statistics**

**Test Case Description:** Verify statistics display correct counts for users, products, orders

**Pre-conditions:** 
- Admin logged in
- Sample data exists in Users, Products, Orders tables

**Test Case Procedure:**
1. Access admin dashboard after login
2. Manually count records in database:
   - SELECT COUNT(*) FROM Users WHERE is_deleted = 0
   - SELECT COUNT(*) FROM Products WHERE is_deleted = 0
   - SELECT COUNT(*) FROM Orders WHERE is_deleted = 0
3. Compare database counts with dashboard display
4. Verify statistics update when new records added
5. Test statistics refresh functionality
6. Check that deleted records are excluded from counts
7. Verify statistics formatting (thousands separators if applicable)

**Expected Results:** 
- Statistics show accurate counts matching database records

---

### **TC_ADMIN_003 - Test Revenue Statistics**

**Test Case Description:** Verify revenue statistics show correct today and monthly totals

**Pre-conditions:** 
- Admin logged in
- Orders with total_amount exist in database

**Test Case Procedure:**
1. Access admin dashboard
2. Check today's revenue calculation:
   - Query database: SELECT SUM(total_amount) FROM Orders WHERE DATE(created_at) = CURDATE()
   - Compare with dashboard display
3. Check monthly revenue calculation:
   - Query: SELECT SUM(total_amount) FROM Orders WHERE MONTH(created_at) = MONTH(CURDATE())
   - Compare with dashboard display
4. Verify currency formatting (VND symbol, comma separators)
5. Test with different order statuses (only count completed orders)
6. Check revenue growth percentage calculation

**Expected Results:** 
- Revenue figures accurately match database calculations with proper formatting

---

### **TC_ADMIN_004 - Test Recent Orders Display**

**Test Case Description:** Verify recent orders section shows latest orders correctly

**Pre-conditions:** 
- Admin logged in
- Multiple orders exist with different timestamps

**Test Case Procedure:**
1. Access admin dashboard
2. Check recent orders section displays
3. Verify orders are sorted by created_at DESC
4. Check that only recent orders shown (last 10 or configured limit)
5. Verify order information displayed:
   - Order ID
   - Customer name
   - Order total
   - Order status
   - Creation date
6. Test click functionality to view order details
7. Verify orders update in real-time when new orders placed

**Expected Results:** 
- Most recent orders displayed in reverse chronological order with accurate information

---

### **TC_ADMIN_005 - Test Top Products Display**

**Test Case Description:** Verify top products section shows best-selling items

**Pre-conditions:** 
- Admin logged in
- Order items data available for sales calculation

**Test Case Procedure:**
1. Access admin dashboard
2. Check top products section displays
3. Verify products ranked by sales volume or revenue
4. Check product information shown:
   - Product name
   - Sales count
   - Revenue generated
   - Product image thumbnail
5. Verify data accuracy by querying database:
   - JOIN Orders with OrderItems and Products
   - GROUP BY product and SUM quantities/amounts
6. Test that out-of-stock products are handled appropriately

**Expected Results:** 
- Products with highest sales/revenue displayed first with accurate metrics

---

### **TC_ADMIN_006 - Test Order Status Statistics**

**Test Case Description:** Verify order status breakdown displays correctly

**Pre-conditions:** 
- Admin logged in
- Orders with various statuses exist

**Test Case Procedure:**
1. Access admin dashboard
2. Check order status statistics widget
3. Verify counts for each status:
   - Pending orders
   - Confirmed orders
   - Processing orders
   - Shipped orders
   - Delivered orders
   - Cancelled orders
4. Query database to verify counts:
   - SELECT status, COUNT(*) FROM Orders GROUP BY status
5. Check that percentages add up to 100%
6. Verify visual representation (charts/graphs if present)

**Expected Results:** 
- Status counts accurately reflect database records with proper percentage calculations

---

### **TC_ADMIN_007 - Test Admin Navigation Menu**

**Test Case Description:** Verify admin panel navigation menu works correctly

**Pre-conditions:** 
- Admin user logged in
- All admin pages exist and accessible

**Test Case Procedure:**
1. Login to admin panel
2. Test each main navigation item:
   - Click "Dashboard" - verify redirect to /admin-panel
   - Click "Users" - verify redirect to /admin-users
   - Click "Products" - verify redirect to /admin-products
   - Click "Orders" - verify redirect to /admin-orders
   - Click "Categories" - verify redirect to /admin-categories
   - Click "Reports" - verify redirect to reports section
3. Test dropdown menus if present
4. Verify active menu highlighting
5. Test breadcrumb navigation
6. Check menu responsiveness on mobile devices

**Expected Results:** 
- All menu items navigate to correct pages
- Active states work
- Responsive design functional

---

### **TC_ADMIN_008 - Test Breadcrumb Navigation**

**Test Case Description:** Verify breadcrumb navigation works correctly in admin panel

**Pre-conditions:** 
- Admin logged in
- Breadcrumb navigation implemented

**Test Case Procedure:**
1. Navigate to admin dashboard
2. Check breadcrumb shows: Home > Dashboard
3. Navigate to Users management
4. Check breadcrumb shows: Home > Dashboard > Users
5. Navigate to specific user edit page
6. Check breadcrumb shows: Home > Dashboard > Users > Edit User
7. Test clicking on breadcrumb links
8. Verify proper navigation back to previous levels

**Expected Results:** 
- Breadcrumb correctly shows navigation path
- Clickable links work properly

---

### **TC_ADMIN_009 - Test Responsive Design for Admin Pages**

**Test Case Description:** Verify admin panel works correctly on different screen sizes

**Pre-conditions:** 
- Admin logged in
- Responsive design implemented

**Test Case Procedure:**
1. Access admin dashboard on desktop (1920x1080)
2. Verify all elements display correctly
3. Test on tablet size (768x1024):
   - Check menu collapses/transforms appropriately
   - Verify dashboard widgets stack properly
   - Test navigation functionality
4. Test on mobile size (375x667):
   - Check hamburger menu appears
   - Verify tables are scrollable
   - Test touch interactions
5. Test orientation changes (portrait/landscape)

**Expected Results:** 
- Admin panel functional and usable on all screen sizes
- No layout breaking or hidden content

---

### **TC_ADMIN_010 - Test Admin Logout Functionality**

**Test Case Description:** Verify admin logout functionality works correctly

**Pre-conditions:** 
- Admin user logged in to admin panel

**Test Case Procedure:**
1. Login as admin user
2. Navigate to admin dashboard
3. Locate logout button/link in admin interface
4. Click logout button
5. Verify redirect to login page or homepage
6. Check that admin session is terminated
7. Try to access admin pages after logout
8. Verify redirect to login page occurs
9. Check that browser back button doesn't allow access to admin pages
10. Verify session data is cleared from browser storage

**Expected Results:** 
- User logged out successfully
- Session terminated
- Cannot access admin pages without re-authentication

---

## 🗄️ **DATABASE MANAGEMENT**

### **TC_DB_001 - Test Reset Identity Functionality**

**Test Case Description:** Verify database identity reset function works correctly

**Pre-conditions:** 
- Admin logged in
- Database backup available
- Reset identity servlet accessible

**Test Case Procedure:**
1. Login as admin user
2. Navigate to database management section
3. Access reset identity functionality at /admin/reset-all-identity
4. Check current identity values before reset:
   - Note current max IDs in Users, Products, Orders tables
5. Execute reset identity function
6. Verify confirmation dialog appears
7. Confirm reset operation
8. Check that identity values are reset to 1 or configured value
9. Verify existing data remains intact
10. Test creating new records uses reset identity values
11. Check for any foreign key constraint issues

**Expected Results:** 
- Identity values reset successfully without data loss
- New records use reset IDs

---

### **TC_DB_002 - Test Database Connection Status**

**Test Case Description:** Verify database connection monitoring works correctly

**Pre-conditions:** 
- Admin logged in
- Database monitoring implemented

**Test Case Procedure:**
1. Access admin dashboard
2. Check database connection status indicator
3. Verify status shows "Connected" when database is available
4. Test connection details display:
   - Database server
   - Database name
   - Connection pool status
5. Simulate database disconnection (stop database service)
6. Refresh admin dashboard
7. Verify status shows "Disconnected" or error state
8. Restart database service
9. Check that status updates to "Connected"

**Expected Results:** 
- Connection status accurately reflects actual database availability

---

### **TC_DB_003 - Test Data Consistency Checks**

**Test Case Description:** Verify data consistency validation works correctly

**Pre-conditions:** 
- Admin logged in
- Data consistency checks implemented

**Test Case Procedure:**
1. Access database management section
2. Run data consistency checks
3. Verify checks include:
   - Orphaned records detection
   - Foreign key constraint validation
   - Data type consistency
   - Required field validation
4. Test with known inconsistent data
5. Verify detailed report generation
6. Check recommendations for fixing issues
7. Test auto-fix functionality if available

**Expected Results:** 
- Consistency checks identify real issues
- Detailed reports provided
- Auto-fix works when available

---

### **TC_DB_004 - Test Backup/Restore Functionality**

**Test Case Description:** Verify database backup and restore functionality (if implemented)

**Pre-conditions:** 
- Admin logged in
- Backup/restore functionality available

**Test Case Procedure:**
1. Access database management section
2. Create database backup:
   - Click "Create Backup" button
   - Verify backup file generation
   - Check backup file location and naming
3. Test backup download functionality
4. Modify some data in database
5. Restore from backup:
   - Upload backup file
   - Confirm restore operation
   - Verify data restored to backup state
6. Test scheduled backup functionality if available

**Expected Results:** 
- Backup creates successfully
- Restore works correctly
- Data integrity maintained

---

## 📊 **REPORTING & ANALYTICS**

### **TC_REPORT_001 - Test Monthly Revenue Report**

**Test Case Description:** Verify monthly revenue report generation works correctly

**Pre-conditions:** 
- Admin logged in
- Order data available for selected month

**Test Case Procedure:**
1. Access admin reporting section
2. Select "Monthly Revenue Report" option
3. Choose specific month and year from date picker
4. Click "Generate Report" button
5. Wait for report processing
6. Verify report displays:
   - Total revenue for selected month
   - Daily breakdown within month
   - Comparison with previous month
   - Chart/graph visualization
7. Check data accuracy by manual database query
8. Test export functionality (PDF/Excel)
9. Verify report formatting and layout

**Expected Results:** 
- Report shows accurate monthly revenue data with proper visualization and export options

---

### **TC_REPORT_002 - Test User Activity Report**

**Test Case Description:** Verify user activity report shows accurate login/registration trends

**Pre-conditions:** 
- Admin logged in
- User activity data available

**Test Case Procedure:**
1. Access admin reporting section
2. Select "User Activity Report"
3. Set date range for report
4. Generate report and verify it shows:
   - New user registrations count
   - Daily active users
   - Login frequency statistics
   - User engagement metrics
5. Cross-check data with database queries
6. Test different date ranges
7. Verify chart visualizations are accurate
8. Test report export functionality

**Expected Results:** 
- Report displays accurate user activity metrics with proper data visualization

---

### **TC_REPORT_003 - Test Product Performance Report**

**Test Case Description:** Verify product performance report shows accurate sales data

**Pre-conditions:** 
- Admin logged in
- Product sales data available

**Test Case Procedure:**
1. Access admin reporting section
2. Select "Product Performance Report"
3. Choose date range and product categories
4. Generate report and verify it displays:
   - Top selling products
   - Revenue by product
   - Product view/purchase conversion rates
   - Inventory turnover rates
5. Verify data accuracy with database joins
6. Test filtering by category
7. Check sorting options (by sales, revenue, views)
8. Test report export in different formats

**Expected Results:** 
- Report shows accurate product sales and performance metrics with filtering and export options

---

### **TC_REPORT_004 - Test Export Functionality**

**Test Case Description:** Verify report export to PDF/Excel works correctly

**Pre-conditions:** 
- Admin logged in
- Reports available
- Export libraries configured

**Test Case Procedure:**
1. Generate any report in admin panel
2. Test PDF export:
   - Click "Export to PDF" button
   - Verify PDF file downloads
   - Open PDF and check formatting
   - Verify all data is included
3. Test Excel export:
   - Click "Export to Excel" button
   - Verify Excel file downloads
   - Open in Excel and check data integrity
   - Verify column headers and formatting
4. Test large dataset exports
5. Check file naming conventions
6. Verify download permissions and browser compatibility

**Expected Results:** 
- Reports export successfully in both PDF and Excel formats with accurate data and formatting

---

### **TC_REPORT_005 - Test Date Range Filtering**

**Test Case Description:** Verify date range filtering works correctly for all reports

**Pre-conditions:** 
- Admin logged in
- Data available across different time periods

**Test Case Procedure:**
1. Access admin reporting section
2. Select any report type
3. Test date range picker:
   - Select start date: last month
   - Select end date: current date
   - Apply date filter
4. Verify report shows only data within selected range
5. Test edge cases:
   - Same start and end date
   - Future dates
   - Invalid date ranges (end before start)
6. Test predefined ranges (Last 7 days, Last month, Last quarter)
7. Verify database queries use correct date filters
8. Check that all report types respect date filtering

**Expected Results:** 
- Reports show data only for specified date range
- Date validation works correctly

---

## ⚠️ **TESTING NOTES**

### **Test Environment Setup:**
- **Database:** Test database with sample data
- **Browser:** Chrome, Firefox, Edge
- **Tools:** Selenium WebDriver for automation
- **Data:** Create test users with different roles

### **Test Data Requirements:**
- Admin user: username="admin", password="admin123"
- Regular user: username="testuser123", password="password123"
- Email user: email="testuser@gmail.com"
- Sample products, orders, categories for dashboard testing

### **Expected Timeline:**
- **Authentication Testing:** 2 days
- **Admin Dashboard Testing:** 2 days
- **Database & Reporting:** 1 day
- **Integration Testing:** 1 day

**Total: 37 test cases for Thiện**
