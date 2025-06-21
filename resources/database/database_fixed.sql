USE [fishshopp]
GO

-- =============================================
-- Drop existing objects if they exist
-- =============================================
IF EXISTS (SELECT * FROM sys.views WHERE name = 'v_popular_posts')
    DROP VIEW [dbo].[v_popular_posts]
GO

IF EXISTS (SELECT * FROM sys.views WHERE name = 'v_blog_statistics')
    DROP VIEW [dbo].[v_blog_statistics]
GO

IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_GetPostsByCategory')
    DROP PROCEDURE [dbo].[sp_GetPostsByCategory]
GO

IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_SearchPosts')
    DROP PROCEDURE [dbo].[sp_SearchPosts]
GO

-- =============================================
-- Create Tables
-- =============================================

-- Users Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Users' AND xtype='U')
CREATE TABLE [dbo].[Users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[username] [nvarchar](50) NULL,
	[password] [nvarchar](255) NULL,
	[email] [nvarchar](100) NULL,
	[phone] [nvarchar](20) NULL,
	[full_name] [nvarchar](100) NULL,
	[role] [nvarchar](20) NULL,
	[status] [nvarchar](20) NULL,
	[created_at] [datetime2](0) NULL,
	[last_login] [datetime2](0) NULL,
	[avatar] [nvarchar](255) NULL,
	[is_deleted] [bit] NULL,
	[reset_token] [nvarchar](10) NULL,
	[reset_token_expiry] [datetime] NULL,
	[google_id] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED ([user_id] ASC),
CONSTRAINT [UQ_Users_email] UNIQUE NONCLUSTERED ([email] ASC),
CONSTRAINT [UQ_Users_username] UNIQUE NONCLUSTERED ([username] ASC)
)
GO

-- Blog Categories Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='blog_categories' AND xtype='U')
CREATE TABLE [dbo].[blog_categories](
	[category_id] [int] IDENTITY(1,1) NOT NULL,
	[category_name] [nvarchar](100) NOT NULL,
	[description] [nvarchar](max) NULL,
	[slug] [nvarchar](100) NULL,
	[created_at] [datetime2](0) NULL,
	[is_deleted] [bit] NULL,
	[is_active] [bit] NOT NULL,
PRIMARY KEY CLUSTERED ([category_id] ASC),
CONSTRAINT [UQ_blog_categories_slug] UNIQUE NONCLUSTERED ([slug] ASC),
CONSTRAINT [UQ_blog_categories_name] UNIQUE NONCLUSTERED ([category_name] ASC)
)
GO

-- Blog Posts Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='blog_posts' AND xtype='U')
CREATE TABLE [dbo].[blog_posts](
	[post_id] [int] IDENTITY(1,1) NOT NULL,
	[title] [nvarchar](255) NOT NULL,
	[content] [nvarchar](max) NOT NULL,
	[summary] [nvarchar](500) NULL,
	[featured_image] [nvarchar](255) NULL,
	[author_id] [int] NULL,
	[tags] [nvarchar](500) NULL,
	[status] [nvarchar](20) NULL,
	[view_count] [int] NULL,
	[created_at] [datetime2](0) NULL,
	[updated_at] [datetime2](0) NULL,
	[published_at] [datetime2](0) NULL,
	[is_deleted] [bit] NULL,
	[category_id] [int] NULL,
PRIMARY KEY CLUSTERED ([post_id] ASC)
)
GO

-- Categories Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Categories' AND xtype='U')
CREATE TABLE [dbo].[Categories](
	[category_id] [int] IDENTITY(1,1) NOT NULL,
	[parent_id] [int] NULL,
	[name] [nvarchar](100) NULL,
	[description] [nvarchar](max) NULL,
	[image] [nvarchar](255) NULL,
	[status] [nvarchar](20) NULL,
	[display_order] [int] NULL,
	[created_at] [datetime2](0) NULL,
	[updated_at] [datetime2](0) NULL,
	[is_deleted] [bit] NULL,
PRIMARY KEY CLUSTERED ([category_id] ASC)
)
GO

-- Products Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Products' AND xtype='U')
CREATE TABLE [dbo].[Products](
	[product_id] [int] IDENTITY(1,1) NOT NULL,
	[category_id] [int] NULL,
	[name] [nvarchar](255) NULL,
	[description] [nvarchar](max) NULL,
	[short_description] [nvarchar](255) NULL,
	[price] [decimal](12, 2) NULL,
	[sale_price] [decimal](12, 2) NULL,
	[quantity] [int] NULL,
	[sku] [nvarchar](50) NULL,
	[status] [nvarchar](20) NULL,
	[featured] [bit] NULL,
	[created_at] [datetime2](0) NULL,
	[updated_at] [datetime2](0) NULL,
	[is_deleted] [bit] NULL,
PRIMARY KEY CLUSTERED ([product_id] ASC),
CONSTRAINT [UQ_Products_sku] UNIQUE NONCLUSTERED ([sku] ASC)
)
GO

-- Wishlist Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Wishlist' AND xtype='U')
CREATE TABLE [dbo].[Wishlist](
	[wishlist_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NULL,
	[product_id] [int] NULL,
	[added_at] [datetime2](0) NULL,
	[is_deleted] [bit] NULL,
PRIMARY KEY CLUSTERED ([wishlist_id] ASC)
)
GO

-- =============================================
-- Add Default Constraints
-- =============================================

-- Users defaults
IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_Users_role')
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [DF_Users_role] DEFAULT ('customer') FOR [role]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_Users_status')
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [DF_Users_status] DEFAULT ('active') FOR [status]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_Users_created_at')
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [DF_Users_created_at] DEFAULT (getdate()) FOR [created_at]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_Users_is_deleted')
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [DF_Users_is_deleted] DEFAULT ((0)) FOR [is_deleted]
GO

-- Blog categories defaults
IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_categories_created_at')
ALTER TABLE [dbo].[blog_categories] ADD CONSTRAINT [DF_blog_categories_created_at] DEFAULT (getdate()) FOR [created_at]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_categories_is_deleted')
ALTER TABLE [dbo].[blog_categories] ADD CONSTRAINT [DF_blog_categories_is_deleted] DEFAULT ((0)) FOR [is_deleted]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_categories_is_active')
ALTER TABLE [dbo].[blog_categories] ADD CONSTRAINT [DF_blog_categories_is_active] DEFAULT ((1)) FOR [is_active]
GO

-- Blog posts defaults
IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_posts_status')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_status] DEFAULT ('draft') FOR [status]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_posts_view_count')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_view_count] DEFAULT ((0)) FOR [view_count]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_posts_created_at')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_created_at] DEFAULT (getdate()) FOR [created_at]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_posts_updated_at')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_updated_at] DEFAULT (getdate()) FOR [updated_at]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_blog_posts_is_deleted')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [DF_blog_posts_is_deleted] DEFAULT ((0)) FOR [is_deleted]
GO

-- Wishlist defaults
IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_Wishlist_added_at')
ALTER TABLE [dbo].[Wishlist] ADD CONSTRAINT [DF_Wishlist_added_at] DEFAULT (getdate()) FOR [added_at]
GO

IF NOT EXISTS (SELECT * FROM sys.default_constraints WHERE name = 'DF_Wishlist_is_deleted')
ALTER TABLE [dbo].[Wishlist] ADD CONSTRAINT [DF_Wishlist_is_deleted] DEFAULT ((0)) FOR [is_deleted]
GO

-- =============================================
-- Add Foreign Key Constraints
-- =============================================

-- Blog posts foreign keys
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_Users')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [FK_blog_posts_Users] 
FOREIGN KEY([author_id]) REFERENCES [dbo].[Users] ([user_id]) ON DELETE SET NULL
GO

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_blog_posts_blog_categories')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [FK_blog_posts_blog_categories] 
FOREIGN KEY([category_id]) REFERENCES [dbo].[blog_categories] ([category_id])
GO

-- Products foreign keys
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_Products_Categories')
ALTER TABLE [dbo].[Products] ADD CONSTRAINT [FK_Products_Categories] 
FOREIGN KEY([category_id]) REFERENCES [dbo].[Categories] ([category_id]) ON DELETE SET NULL
GO

-- Wishlist foreign keys
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_Wishlist_Products')
ALTER TABLE [dbo].[Wishlist] ADD CONSTRAINT [FK_Wishlist_Products] 
FOREIGN KEY([product_id]) REFERENCES [dbo].[Products] ([product_id])
GO

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_Wishlist_Users')
ALTER TABLE [dbo].[Wishlist] ADD CONSTRAINT [FK_Wishlist_Users] 
FOREIGN KEY([user_id]) REFERENCES [dbo].[Users] ([user_id])
GO

-- =============================================
-- Add Check Constraints
-- =============================================

-- Users check constraints
IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_Users_role')
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [CK_Users_role] 
CHECK ([role]='customer' OR [role]='admin')
GO

IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_Users_status')
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [CK_Users_status] 
CHECK ([status]='banned' OR [status]='inactive' OR [status]='active')
GO

-- Blog posts check constraints
IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_blog_posts_status')
ALTER TABLE [dbo].[blog_posts] ADD CONSTRAINT [CK_blog_posts_status] 
CHECK ([status]='archived' OR [status]='published' OR [status]='draft')
GO

-- =============================================
-- Create Views
-- =============================================

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

CREATE VIEW [dbo].[v_popular_posts] AS
SELECT TOP 10
    bp.post_id,
    bp.title,
    bp.view_count,
    bp.published_at,
    bc.category_name as category
FROM blog_posts bp
LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
WHERE bp.status = 'published' AND bp.is_deleted = 0
ORDER BY bp.view_count DESC
GO

-- =============================================
-- Create Stored Procedures
-- =============================================

CREATE PROCEDURE [dbo].[sp_GetPostsByCategory]
    @CategoryName NVARCHAR(100),
    @PageNumber INT = 1,
    @PageSize INT = 10
AS
BEGIN
    DECLARE @Offset INT = (@PageNumber - 1) * @PageSize;
    
    SELECT 
        bp.*,
        u.username as author_name,
        bc.category_name,
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

CREATE PROCEDURE [dbo].[sp_SearchPosts]
    @Keyword NVARCHAR(255),
    @PageNumber INT = 1,
    @PageSize INT = 10
AS
BEGIN
    DECLARE @Offset INT = (@PageNumber - 1) * @PageSize;
    
    SELECT 
        bp.*,
        u.username as author_name,
        bc.category_name,
        COUNT(*) OVER() as total_count
    FROM blog_posts bp
    INNER JOIN Users u ON bp.author_id = u.user_id
    LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
    WHERE (bp.title LIKE '%' + @Keyword + '%' 
           OR bp.content LIKE '%' + @Keyword + '%' 
           OR bp.tags LIKE '%' + @Keyword + '%')
        AND bp.status = 'published' 
        AND bp.is_deleted = 0
    ORDER BY bp.published_at DESC
    OFFSET @Offset ROWS 
    FETCH NEXT @PageSize ROWS ONLY;
END
GO

-- =============================================
-- Insert Sample Data (Optional)
-- =============================================

-- Insert sample blog categories
IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = 'Kỹ thuật nuôi cá')
INSERT INTO blog_categories (category_name, description, slug, is_active) 
VALUES (N'Kỹ thuật nuôi cá', N'Hướng dẫn kỹ thuật nuôi cá cảnh', 'ky-thuat-nuoi-ca', 1)
GO

IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = 'Chăm sóc cá cảnh')
INSERT INTO blog_categories (category_name, description, slug, is_active) 
VALUES (N'Chăm sóc cá cảnh', N'Cách chăm sóc cá cảnh khỏe mạnh', 'cham-soc-ca-canh', 1)
GO

IF NOT EXISTS (SELECT * FROM blog_categories WHERE category_name = 'Thiết bị hồ cá')
INSERT INTO blog_categories (category_name, description, slug, is_active) 
VALUES (N'Thiết bị hồ cá', N'Thông tin về thiết bị hồ cá', 'thiet-bi-ho-ca', 1)
GO

-- Insert sample product categories
IF NOT EXISTS (SELECT * FROM Categories WHERE name = 'Cá cảnh')
INSERT INTO Categories (name, description, status, display_order, is_deleted) 
VALUES (N'Cá cảnh', N'Các loại cá cảnh đẹp', 'active', 1, 0)
GO

IF NOT EXISTS (SELECT * FROM Categories WHERE name = 'Thiết bị')
INSERT INTO Categories (name, description, status, display_order, is_deleted) 
VALUES (N'Thiết bị', N'Thiết bị cho hồ cá', 'active', 2, 0)
GO

PRINT 'Database setup completed successfully!'
GO
