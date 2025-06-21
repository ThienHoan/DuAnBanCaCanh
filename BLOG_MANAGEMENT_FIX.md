# Fix Blog Management URLs trong Admin Panel

## Vấn đề đã phát hiện:
- **Conflict URL Mapping**: AdminBlogController có cả @WebServlet annotation và mapping trong web.xml
- **URL không khớp**: Sidebar sử dụng URL khác với mapping thực tế
- **Broken Links**: Một số link trong trang không hoạt động

## Đã sửa:

### 1. **AdminBlogController.java**
```java
// Comment @WebServlet annotation để tránh conflict
// @WebServlet(urlPatterns = {"/admin-blog", "/admin-blog-form", "/admin-blog-categories", "/admin-blog-action"})

// Cập nhật case statement để khớp với web.xml
case "/admin-blogs": // Thay vì "/admin-blog"
    handleBlogList(request, response);
    break;
```

### 2. **web.xml**
Thêm đầy đủ mapping cho AdminBlogController:
```xml
<servlet-mapping>
    <servlet-name>AdminBlogController</servlet-name>
    <url-pattern>/admin-blogs</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>AdminBlogController</servlet-name>
    <url-pattern>/admin-blog-form</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>AdminBlogController</servlet-name>
    <url-pattern>/admin-blog-categories</url-pattern>
</servlet-mapping>
<servlet-mapping>
    <servlet-name>AdminBlogController</servlet-name>
    <url-pattern>/admin-blog-action</url-pattern>
</servlet-mapping>
```

### 3. **Cập nhật URLs trong các file JSP:**

#### ✅ admin-dashboard.jsp
```jsp
// Cũ: admin-blog
// Mới: admin-blogs
<a href="..."/admin-blogs">Manage Posts</a>
```

#### ✅ admin/blog-form.jsp
```jsp
// Cập nhật 2 link quay lại danh sách
<a href="..."/admin-blogs">Quay lại danh sách</a>
```

#### ✅ admin-panel.jsp
```jsp
// Cập nhật navigation links
<a href="..."/admin-blogs">Blog Management</a>
```

### 4. **sidebar.jsp** 
Đã được cập nhật với URLs đúng:
```jsp
<a href="${pageContext.request.contextPath}/admin-blogs">Danh sách bài viết</a>
<a href="${pageContext.request.contextPath}/admin-blogs?action=create">Thêm bài viết</a>
<a href="${pageContext.request.contextPath}/admin-categories">Quản lý danh mục</a>
```

## URL Mapping hiện tại:

| Chức năng | URL | Controller Method |
|-----------|-----|------------------|
| Danh sách blog | `/admin-blogs` | handleBlogList() |
| Form blog | `/admin-blog-form` | handleBlogForm() |
| Danh sách danh mục | `/admin-blog-categories` | handleCategoriesList() |
| Xử lý action | `/admin-blog-action` | handleBlogAction() |
| Quản lý danh mục | `/admin-categories` | AdminCategoryController |

## Kết quả:
✅ **Blog Management menu trong sidebar hoạt động bình thường**  
✅ **Tất cả links chuyển hướng đúng**  
✅ **Không còn conflict URL mapping**  
✅ **Admin có thể truy cập đầy đủ chức năng blog**

## Test các link:
1. Dashboard → "Manage Posts" → `/admin-blogs` ✅
2. Sidebar → "Danh sách bài viết" → `/admin-blogs` ✅  
3. Sidebar → "Thêm bài viết" → `/admin-blogs?action=create` ✅
4. Sidebar → "Quản lý danh mục" → `/admin-categories` ✅
