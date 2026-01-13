-- Script to reset IDENTITY values for all main tables
-- Run this script in SQL Server Management Studio (SSMS)
-- Make sure you're connected to the correct database (fishshopp)

USE [fishshopp]
GO

-- Check current IDENTITY values
PRINT 'Current IDENTITY values:'
PRINT 'Users: ' + CAST(IDENT_CURRENT('Users') AS VARCHAR(10))
PRINT 'blog_categories: ' + CAST(IDENT_CURRENT('blog_categories') AS VARCHAR(10))
PRINT 'blog_posts: ' + CAST(IDENT_CURRENT('blog_posts') AS VARCHAR(10))

-- Uncomment the lines below to reset IDENTITY values
-- WARNING: Only run this if you want to reset ID values to start from a lower number

/*
-- Reset Users table identity to 5
DBCC CHECKIDENT ('Users', RESEED, 5)

-- Reset blog_categories table identity to 5
DBCC CHECKIDENT ('blog_categories', RESEED, 5)

-- Reset blog_posts table identity to 5
DBCC CHECKIDENT ('blog_posts', RESEED, 5)

-- Reset other tables if they exist
-- DBCC CHECKIDENT ('Products', RESEED, 5)
-- DBCC CHECKIDENT ('Orders', RESEED, 5)
-- DBCC CHECKIDENT ('Categories', RESEED, 5)

PRINT 'IDENTITY values reset successfully!'
*/

-- Check IDENTITY values after reset
PRINT 'IDENTITY values after reset:'
PRINT 'Users: ' + CAST(IDENT_CURRENT('Users') AS VARCHAR(10))
PRINT 'blog_categories: ' + CAST(IDENT_CURRENT('blog_categories') AS VARCHAR(10))
PRINT 'blog_posts: ' + CAST(IDENT_CURRENT('blog_posts') AS VARCHAR(10))

-- Query to see actual data in tables
SELECT 'Users' as TableName, COUNT(*) as RecordCount, MAX(user_id) as MaxID FROM Users WHERE is_deleted = 0
UNION ALL
SELECT 'blog_categories', COUNT(*), MAX(category_id) FROM blog_categories WHERE is_deleted = 0
UNION ALL
SELECT 'blog_posts', COUNT(*), MAX(post_id) FROM blog_posts WHERE is_deleted = 0
