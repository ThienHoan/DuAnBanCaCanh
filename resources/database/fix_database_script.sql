-- =============================================
-- SCRIPT SỬA LỖI DATABASE HIỆN TẠI
-- Chạy script này trên database hiện tại để sửa các lỗi
-- =============================================

USE [fishshopp]
GO

PRINT 'Bắt đầu sửa lỗi database...'
GO

-- =============================================
-- 1. XÓA CÁC VIEW/STORED PROCEDURE BỊ LỖI
-- =============================================

PRINT 'Đang xóa các view/stored procedure cũ...'
GO

-- Xóa view v_popular_posts bị lỗi
IF EXISTS (SELECT * FROM sys.views WHERE name = 'v_popular_posts')
BEGIN
    DROP VIEW [dbo].[v_popular_posts]
    PRINT 'Đã xóa view v_popular_posts cũ'
END
GO

-- Xóa view v_blog_statistics (để tạo lại cho chắc chắn)
IF EXISTS (SELECT * FROM sys.views WHERE name = 'v_blog_statistics')
BEGIN
    DROP VIEW [dbo].[v_blog_statistics]
    PRINT 'Đã xóa view v_blog_statistics cũ'
END
GO

-- Xóa stored procedure sp_GetPostsByCategory bị lỗi
IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_GetPostsByCategory')
BEGIN
    DROP PROCEDURE [dbo].[sp_GetPostsByCategory]
    PRINT 'Đã xóa stored procedure sp_GetPostsByCategory cũ'
END
GO

-- Xóa stored procedure sp_SearchPosts bị lỗi
IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_SearchPosts')
BEGIN
    DROP PROCEDURE [dbo].[sp_SearchPosts]
    PRINT 'Đã xóa stored procedure sp_SearchPosts cũ'
END
GO

-- =============================================
-- 2. TẠO LẠI CÁC VIEW ĐÚNG
-- =============================================

PRINT 'Đang tạo lại các view đã sửa...'
GO

-- Tạo lại view v_blog_statistics
CREATE VIEW [dbo].[v_blog_statistics] AS
SELECT 
    COUNT(*) as total_posts,
    COUNT(CASE WHEN status = 'published' THEN 1 END) as published_posts,
    COUNT(CASE WHEN status = 'draft' THEN 1 END) as draft_posts,
    SUM(view_count) as total_views,
    AVG(view_count) as avg_views_per_post
FROM blog_posts 
WHERE is_deleted = 0
GO

PRINT 'Đã tạo lại view v_blog_statistics'
GO

-- Tạo lại view v_popular_posts với JOIN đúng
CREATE VIEW [dbo].[v_popular_posts] AS
SELECT TOP 10
    bp.post_id,
    bp.title,
    bp.view_count,
    bp.published_at,
    ISNULL(bc.category_name, 'Chưa phân loại') as category
FROM blog_posts bp
LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
WHERE bp.status = 'published' AND bp.is_deleted = 0
ORDER BY bp.view_count DESC
GO

PRINT 'Đã tạo lại view v_popular_posts với JOIN đúng'
GO

-- =============================================
-- 3. TẠO LẠI CÁC STORED PROCEDURE ĐÚNG
-- =============================================

PRINT 'Đang tạo lại các stored procedure đã sửa...'
GO

-- Tạo lại stored procedure sp_GetPostsByCategory với JOIN đúng
CREATE PROCEDURE [dbo].[sp_GetPostsByCategory]
    @CategoryName NVARCHAR(100),
    @PageNumber INT = 1,
    @PageSize INT = 10
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @Offset INT = (@PageNumber - 1) * @PageSize;
    
    SELECT 
        bp.post_id,
        bp.title,
        bp.content,
        bp.summary,
        bp.featured_image,
        bp.author_id,
        ISNULL(u.full_name, u.username) as author_name,
        bp.category_id,
        bc.category_name,
        bc.slug as category_slug,
        bp.tags,
        bp.status,
        bp.view_count,
        bp.created_at,
        bp.updated_at,
        bp.published_at,
        bp.is_deleted,
        COUNT(*) OVER() as total_count
    FROM blog_posts bp
    INNER JOIN Users u ON bp.author_id = u.user_id
    LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
    WHERE bc.category_name = @CategoryName 
        AND bp.status = 'published' 
        AND bp.is_deleted = 0
    ORDER BY bp.published_at DESC
    OFFSET @Offset ROWS 
    FETCH NEXT @PageSize ROWS ONLY;
END
GO

PRINT 'Đã tạo lại stored procedure sp_GetPostsByCategory'
GO

-- Tạo lại stored procedure sp_SearchPosts với JOIN đúng
CREATE PROCEDURE [dbo].[sp_SearchPosts]
    @Keyword NVARCHAR(255),
    @PageNumber INT = 1,
    @PageSize INT = 10
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @Offset INT = (@PageNumber - 1) * @PageSize;
    
    SELECT 
        bp.post_id,
        bp.title,
        bp.content,
        bp.summary,
        bp.featured_image,
        bp.author_id,
        ISNULL(u.full_name, u.username) as author_name,
        bp.category_id,
        bc.category_name,
        bc.slug as category_slug,
        bp.tags,
        bp.status,
        bp.view_count,
        bp.created_at,
        bp.updated_at,
        bp.published_at,
        bp.is_deleted,
        COUNT(*) OVER() as total_count
    FROM blog_posts bp
    INNER JOIN Users u ON bp.author_id = u.user_id
    LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
    WHERE (bp.title LIKE '%' + @Keyword + '%' 
           OR bp.content LIKE '%' + @Keyword + '%' 
           OR bp.summary LIKE '%' + @Keyword + '%'
           OR bp.tags LIKE '%' + @Keyword + '%')
        AND bp.status = 'published' 
        AND bp.is_deleted = 0
    ORDER BY bp.published_at DESC
    OFFSET @Offset ROWS 
    FETCH NEXT @PageSize ROWS ONLY;
END
GO

PRINT 'Đã tạo lại stored procedure sp_SearchPosts'
GO

-- =============================================
-- 4. THÊM CÁC RÀNG BUỘC VÀ DEFAULT VALUES (NẾU CHƯA CÓ)
-- =============================================

PRINT 'Đang kiểm tra và thêm các ràng buộc còn thiếu...'
GO

-- Kiểm tra và thêm default values cho blog_categories
IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_categories') AND name = 'DF_blog_categories_created_at')
BEGIN
    ALTER TABLE [dbo].[blog_categories] ADD CONSTRAINT [DF_blog_categories_created_at] DEFAULT (getdate()) FOR [created_at]
    PRINT 'Đã thêm default constraint cho blog_categories.created_at'
END
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_categories') AND name = 'DF_blog_categories_is_deleted')
BEGIN
    ALTER TABLE [dbo].[blog_categories] ADD CONSTRAINT [DF_blog_categories_is_deleted] DEFAULT ((0)) FOR [is_deleted]
    PRINT 'Đã thêm default constraint cho blog_categories.is_deleted'
END
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_categories') AND name = 'DF_blog_categories_is_active')
BEGIN
    ALTER TABLE [dbo].[blog_categories] ADD CONSTRAINT [DF_blog_categories_is_active] DEFAULT ((1)) FOR [is_active]
    PRINT 'Đã thêm default constraint cho blog_categories.is_active'
END
GO

-- Kiểm tra và thêm default values cho blog_posts
IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_posts') AND name = 'DF_blog_posts_status')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_status] DEFAULT ('draft') FOR [status]
    PRINT 'Đã thêm default constraint cho blog_posts.status'
END
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_posts') AND name = 'DF_blog_posts_view_count')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_view_count] DEFAULT ((0)) FOR [view_count]
    PRINT 'Đã thêm default constraint cho blog_posts.view_count'
END
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_posts') AND name = 'DF_blog_posts_created_at')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_created_at] DEFAULT (getdate()) FOR [created_at]
    PRINT 'Đã thêm default constraint cho blog_posts.created_at'
END
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_posts') AND name = 'DF_blog_posts_updated_at')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_updated_at] DEFAULT (getdate()) FOR [updated_at]
    PRINT 'Đã thêm default constraint cho blog_posts.updated_at'
END
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE parent_object_id = OBJECT_ID('blog_posts') AND name = 'DF_blog_posts_is_deleted')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_is_deleted] DEFAULT ((0)) FOR [is_deleted]
    PRINT 'Đã thêm default constraint cho blog_posts.is_deleted'
END
GO

-- =============================================
-- 5. KIỂM TRA VÀ THÊM FOREIGN KEY CONSTRAINTS (NẾU CHƯA CÓ)
-- =============================================

PRINT 'Đang kiểm tra và thêm foreign key constraints...'
GO

-- Kiểm tra foreign key từ blog_posts đến Users
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_Users')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [FK_blog_posts_Users] 
    FOREIGN KEY([author_id]) REFERENCES [dbo].[Users] ([user_id]) ON DELETE SET NULL
    PRINT 'Đã thêm foreign key constraint FK_blog_posts_Users'
END
GO

-- Kiểm tra foreign key từ blog_posts đến blog_categories
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_blog_categories')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [FK_blog_posts_blog_categories] 
    FOREIGN KEY([category_id]) REFERENCES [dbo].[blog_categories] ([category_id])
    PRINT 'Đã thêm foreign key constraint FK_blog_posts_blog_categories'
END
GO

-- =============================================
-- 6. THÊM DỮ LIỆU MẪU (NẾU CHƯA CÓ)
-- =============================================

PRINT 'Đang kiểm tra và thêm dữ liệu mẫu...'
GO

-- Thêm sample blog categories nếu chưa có
IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = N'Kỹ thuật nuôi cá')
BEGIN
    INSERT INTO blog_categories (category_name, description, slug, is_active) 
    VALUES (N'Kỹ thuật nuôi cá', N'Hướng dẫn kỹ thuật nuôi cá cảnh', 'ky-thuat-nuoi-ca', 1)
    PRINT 'Đã thêm category: Kỹ thuật nuôi cá'
END
GO

IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = N'Chăm sóc cá cảnh')
BEGIN
    INSERT INTO blog_categories (category_name, description, slug, is_active) 
    VALUES (N'Chăm sóc cá cảnh', N'Cách chăm sóc cá cảnh khỏe mạnh', 'cham-soc-ca-canh', 1)
    PRINT 'Đã thêm category: Chăm sóc cá cảnh'
END
GO

IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = N'Thiết bị hồ cá')
BEGIN
    INSERT INTO blog_categories (category_name, description, slug, is_active) 
    VALUES (N'Thiết bị hồ cá', N'Thông tin về thiết bị hồ cá', 'thiet-bi-ho-ca', 1)
    PRINT 'Đã thêm category: Thiết bị hồ cá'
END
GO

IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = N'Thức ăn cho cá')
BEGIN
    INSERT INTO blog_categories (category_name, description, slug, is_active) 
    VALUES (N'Thức ăn cho cá', N'Hướng dẫn về thức ăn cho cá cảnh', 'thuc-an-cho-ca', 1)
    PRINT 'Đã thêm category: Thức ăn cho cá'
END
GO

-- =============================================
-- 7. KIỂM TRA KẾT QUẢ
-- =============================================

PRINT 'Đang kiểm tra kết quả...'
GO

-- Test view v_blog_statistics
BEGIN TRY
    SELECT TOP 1 * FROM v_blog_statistics
    PRINT '✓ View v_blog_statistics hoạt động bình thường'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi khi test view v_blog_statistics: ' + ERROR_MESSAGE()
END CATCH
GO

-- Test view v_popular_posts
BEGIN TRY
    SELECT TOP 1 * FROM v_popular_posts
    PRINT '✓ View v_popular_posts hoạt động bình thường'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi khi test view v_popular_posts: ' + ERROR_MESSAGE()
END CATCH
GO

-- Test stored procedure sp_GetPostsByCategory
BEGIN TRY
    EXEC sp_GetPostsByCategory @CategoryName = N'Kỹ thuật nuôi cá', @PageNumber = 1, @PageSize = 5
    PRINT '✓ Stored procedure sp_GetPostsByCategory hoạt động bình thường'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi khi test sp_GetPostsByCategory: ' + ERROR_MESSAGE()
END CATCH
GO

-- Test stored procedure sp_SearchPosts
BEGIN TRY
    EXEC sp_SearchPosts @Keyword = N'cá', @PageNumber = 1, @PageSize = 5
    PRINT '✓ Stored procedure sp_SearchPosts hoạt động bình thường'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi khi test sp_SearchPosts: ' + ERROR_MESSAGE()
END CATCH
GO

-- Hiển thị số lượng blog categories
SELECT COUNT(*) as total_blog_categories FROM blog_categories WHERE is_deleted = 0
PRINT 'Tổng số blog categories: ' + CAST(@@ROWCOUNT AS VARCHAR(10))
GO

PRINT '=========================================='
PRINT 'HOÀN THÀNH SỬA LỖI DATABASE!'
PRINT 'Tất cả các view và stored procedure đã được sửa'
PRINT 'Database hiện tại đã tương thích với code Java'
PRINT '=========================================='
GO
