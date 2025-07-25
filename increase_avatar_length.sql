-- Script to increase avatar field length to support Google avatar URLs
USE [fishshopp]
GO

-- Check current avatar field length
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Users' AND COLUMN_NAME = 'avatar'

-- Increase avatar field length to support long Google URLs
ALTER TABLE Users 
ALTER COLUMN avatar NVARCHAR(500) NULL

PRINT 'Avatar field length increased to 500 characters'

-- Verify the change
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Users' AND COLUMN_NAME = 'avatar'
