-- Script to fix Users table identity issues
-- This script will reset the identity to a safe value based on existing data

USE [fishshopp]
GO

-- Check current max user_id and identity value
DECLARE @max_id INT
DECLARE @current_identity INT

SELECT @max_id = ISNULL(MAX(user_id), 0) FROM Users
SET @current_identity = IDENT_CURRENT('Users')

PRINT 'Current max user_id in table: ' + CAST(@max_id AS VARCHAR(10))
PRINT 'Current IDENTITY value: ' + CAST(@current_identity AS VARCHAR(10))

-- Reset identity to max_id (next insert sẽ là max_id + 1)
PRINT 'Resetting identity to: ' + CAST(@max_id AS VARCHAR(10))
DBCC CHECKIDENT ('Users', RESEED, @max_id)
PRINT 'Identity reset completed successfully'

-- Verify the reset
PRINT 'New IDENTITY value: ' + CAST(IDENT_CURRENT('Users') AS VARCHAR(10))

-- Show current users count and max ID
SELECT 
    COUNT(*) as total_users,
    MAX(user_id) as max_user_id,
    IDENT_CURRENT('Users') as current_identity_value
FROM Users

PRINT 'Fix completed. Next user will be created with ID: ' + CAST(@max_id + 1 AS VARCHAR(10))
