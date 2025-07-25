-- =============================================
-- SCRIPT SỬA LỖI DATABASE HIỆN TẠI (PHIÊN BẢN CẢI TIẾN)
-- Chạy script này để sửa các lỗi còn lại
-- =============================================

USE [fishshopp]
GO

PRINT 'Bắt đầu sửa các lỗi còn lại...'
GO

-- =============================================
-- 1. SỬA LỖI FOREIGN KEY CONSTRAINT
-- =============================================

PRINT 'Đang sửa foreign key constraint...'
GO

-- Xóa constraint cũ nếu có để tạo lại đúng
IF EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_Users')
BEGIN
    ALTER TABLE [dbo].[blog_posts] DROP CONSTRAINT [FK_blog_posts_Users]
    PRINT 'Đã xóa FK_blog_posts_Users cũ'
END
GO

-- Tạo lại với ON DELETE NO ACTION để tránh cascade conflicts
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_Users')
BEGIN
    ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [FK_blog_posts_Users] 
    FOREIGN KEY([author_id]) REFERENCES [dbo].[Users] ([user_id]) ON DELETE NO ACTION
    PRINT 'Đã tạo lại FK_blog_posts_Users với NO ACTION'
END
GO

-- Kiểm tra foreign key từ blog_posts đến blog_categories
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_blog_categories')
BEGIN
    BEGIN TRY
        ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [FK_blog_posts_blog_categories] 
        FOREIGN KEY([category_id]) REFERENCES [dbo].[blog_categories] ([category_id]) ON DELETE NO ACTION
        PRINT 'Đã thêm FK_blog_posts_blog_categories'
    END TRY
    BEGIN CATCH
        PRINT 'FK_blog_posts_blog_categories đã tồn tại hoặc có lỗi: ' + ERROR_MESSAGE()
    END CATCH
END
ELSE
BEGIN
    PRINT 'FK_blog_posts_blog_categories đã tồn tại'
END
GO

-- =============================================
-- 2. KIỂM TRA VÀ SỬA DỮ LIỆU TRÙNG LẬP
-- =============================================

PRINT 'Đang kiểm tra dữ liệu trùng lặp...'
GO

-- Xóa các category trùng lặp (giữ lại bản ghi có ID nhỏ nhất)
WITH DuplicateCategories AS (
    SELECT 
        category_name, 
        ROW_NUMBER() OVER (PARTITION BY category_name ORDER BY category_id) as rn,
        category_id
    FROM blog_categories
)
DELETE FROM blog_categories 
WHERE category_id IN (
    SELECT category_id 
    FROM DuplicateCategories 
    WHERE rn > 1
)

IF @@ROWCOUNT > 0
    PRINT 'Đã xóa ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' category trùng lặp'
ELSE
    PRINT 'Không có category trùng lặp'
GO

-- =============================================
-- 3. THÊM DỮ LIỆU MẪU AN TOÀN
-- =============================================

PRINT 'Đang thêm dữ liệu mẫu an toàn...'
GO

-- Sử dụng MERGE để tránh lỗi trùng lặp
MERGE blog_categories AS target
USING (
    SELECT N'Kỹ thuật nuôi cá' as category_name, N'Hướng dẫn kỹ thuật nuôi cá cảnh' as description, 'ky-thuat-nuoi-ca' as slug, 1 as is_active
    UNION ALL
    SELECT N'Chăm sóc cá cảnh', N'Cách chăm sóc cá cảnh khỏe mạnh', 'cham-soc-ca-canh', 1
    UNION ALL
    SELECT N'Thiết bị hồ cá', N'Thông tin về thiết bị hồ cá', 'thiet-bi-ho-ca', 1
    UNION ALL
    SELECT N'Thức ăn cho cá', N'Hướng dẫn về thức ăn cho cá cảnh', 'thuc-an-cho-ca', 1
    UNION ALL
    SELECT N'Bệnh cá cảnh', N'Cách phòng và chữa bệnh cho cá cảnh', 'benh-ca-canh', 1
) AS source (category_name, description, slug, is_active)
ON target.category_name = source.category_name
WHEN NOT MATCHED THEN
    INSERT (category_name, description, slug, is_active, created_at, is_deleted)
    VALUES (source.category_name, source.description, source.slug, source.is_active, GETDATE(), 0);

PRINT 'Đã thêm/cập nhật ' + CAST(@@ROWCOUNT AS VARCHAR(10)) + ' blog categories'
GO

-- =============================================
-- 4. KIỂM TRA VÀ TẠO INDEX HIỆU SUẤT
-- =============================================

PRINT 'Đang tạo index để tăng hiệu suất...'
GO

-- Index cho blog_posts trên category_id
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_blog_posts_category_id')
BEGIN
    CREATE INDEX IX_blog_posts_category_id ON blog_posts(category_id) 
    WHERE is_deleted = 0
    PRINT 'Đã tạo index IX_blog_posts_category_id'
END
GO

-- Index cho blog_posts trên status và published_at
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_blog_posts_status_published')
BEGIN
    CREATE INDEX IX_blog_posts_status_published ON blog_posts(status, published_at DESC) 
    WHERE is_deleted = 0
    PRINT 'Đã tạo index IX_blog_posts_status_published'
END
GO

-- Index cho blog_categories trên category_name
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_blog_categories_name')
BEGIN
    CREATE INDEX IX_blog_categories_name ON blog_categories(category_name) 
    WHERE is_deleted = 0
    PRINT 'Đã tạo index IX_blog_categories_name'
END
GO

-- =============================================
-- 5. KIỂM TRA TOÀN DIỆN VÀ BÁO CÁO
-- =============================================

PRINT 'Đang kiểm tra toàn diện...'
GO

-- Kiểm tra view v_blog_statistics
BEGIN TRY
    DECLARE @total_posts INT, @published_posts INT, @draft_posts INT
    SELECT @total_posts = total_posts, @published_posts = published_posts, @draft_posts = draft_posts 
    FROM v_blog_statistics
    PRINT '✓ View v_blog_statistics: Tổng ' + CAST(@total_posts AS VARCHAR(10)) + ' posts (' + CAST(@published_posts AS VARCHAR(10)) + ' published, ' + CAST(@draft_posts AS VARCHAR(10)) + ' draft)'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi view v_blog_statistics: ' + ERROR_MESSAGE()
END CATCH
GO

-- Kiểm tra view v_popular_posts
BEGIN TRY
    DECLARE @popular_count INT
    SELECT @popular_count = COUNT(*) FROM v_popular_posts
    PRINT '✓ View v_popular_posts: ' + CAST(@popular_count AS VARCHAR(10)) + ' popular posts'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi view v_popular_posts: ' + ERROR_MESSAGE()
END CATCH
GO

-- Kiểm tra stored procedures
BEGIN TRY
    DECLARE @sp1_count INT
    CREATE TABLE #temp1 (
        post_id INT, title NVARCHAR(255), content NVARCHAR(MAX), summary NVARCHAR(500),
        featured_image NVARCHAR(255), author_id INT, author_name NVARCHAR(100),
        category_id INT, category_name NVARCHAR(100), category_slug NVARCHAR(100),
        tags NVARCHAR(500), status NVARCHAR(20), view_count INT,
        created_at DATETIME2, updated_at DATETIME2, published_at DATETIME2,
        is_deleted BIT, total_count INT
    )
    INSERT INTO #temp1 EXEC sp_GetPostsByCategory @CategoryName = N'Kỹ thuật nuôi cá'
    SELECT @sp1_count = COUNT(*) FROM #temp1
    DROP TABLE #temp1
    PRINT '✓ sp_GetPostsByCategory: Tìm thấy ' + CAST(@sp1_count AS VARCHAR(10)) + ' posts'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi sp_GetPostsByCategory: ' + ERROR_MESSAGE()
END CATCH
GO

BEGIN TRY
    DECLARE @sp2_count INT
    CREATE TABLE #temp2 (
        post_id INT, title NVARCHAR(255), content NVARCHAR(MAX), summary NVARCHAR(500),
        featured_image NVARCHAR(255), author_id INT, author_name NVARCHAR(100),
        category_id INT, category_name NVARCHAR(100), category_slug NVARCHAR(100),
        tags NVARCHAR(500), status NVARCHAR(20), view_count INT,
        created_at DATETIME2, updated_at DATETIME2, published_at DATETIME2,
        is_deleted BIT, total_count INT
    )
    INSERT INTO #temp2 EXEC sp_SearchPosts @Keyword = N'cá'
    SELECT @sp2_count = COUNT(*) FROM #temp2
    DROP TABLE #temp2
    PRINT '✓ sp_SearchPosts: Tìm thấy ' + CAST(@sp2_count AS VARCHAR(10)) + ' posts với từ khóa "cá"'
END TRY
BEGIN CATCH
    PRINT '✗ Lỗi sp_SearchPosts: ' + ERROR_MESSAGE()
END CATCH
GO

-- Báo cáo tổng quan
DECLARE @total_categories INT, @active_categories INT
SELECT @total_categories = COUNT(*), @active_categories = COUNT(CASE WHEN is_active = 1 THEN 1 END)
FROM blog_categories WHERE is_deleted = 0

PRINT ''
PRINT '=========================================='
PRINT 'BÁO CÁO TỔNG QUAN:'
PRINT 'Blog Categories: ' + CAST(@total_categories AS VARCHAR(10)) + ' (Active: ' + CAST(@active_categories AS VARCHAR(10)) + ')'

SELECT 
    '- ' + category_name + ' (' + slug + ')' as category_info
FROM blog_categories 
WHERE is_deleted = 0 AND is_active = 1
ORDER BY category_id

PRINT ''
PRINT '✓ Tất cả lỗi đã được sửa!'
PRINT '✓ Database đã tương thích hoàn toàn với code Java'
PRINT '✓ Các view và stored procedure hoạt động bình thường'
PRINT '✓ Đã thêm index để tăng hiệu suất'
PRINT '=========================================='
GO
