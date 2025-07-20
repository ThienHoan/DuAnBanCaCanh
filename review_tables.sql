-- Create Reviews table
CREATE TABLE Reviews (
    review_id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT,
    user_id INT,
    order_id INT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    review_date DATETIME2 DEFAULT GETDATE(),
    status NVARCHAR(50) DEFAULT 'pending',
    is_verified_purchase BIT DEFAULT 0,
    is_deleted BIT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

-- Create Review_images table
CREATE TABLE Review_images (
    image_id INT IDENTITY(1,1) PRIMARY KEY,
    review_id INT NOT NULL,
    image_url NVARCHAR(500) NOT NULL,
    is_deleted BIT DEFAULT 0,
    FOREIGN KEY (review_id) REFERENCES Reviews(review_id)
);

-- Create indexes for better performance
CREATE INDEX IX_Reviews_ProductId ON Reviews(product_id);
CREATE INDEX IX_Reviews_UserId ON Reviews(user_id);
CREATE INDEX IX_Reviews_Status ON Reviews(status);
CREATE INDEX IX_Reviews_IsDeleted ON Reviews(is_deleted);
CREATE INDEX IX_ReviewImages_ReviewId ON Review_images(review_id);
CREATE INDEX IX_ReviewImages_IsDeleted ON Review_images(is_deleted);

-- Insert sample data for testing
INSERT INTO Reviews (product_id, user_id, rating, comment, status, is_verified_purchase, is_deleted)
VALUES 
(1, 1, 5, 'Great fish! Very healthy and active.', 'approved', 1, 0),
(1, 2, 4, 'Good quality fish, arrived safely.', 'approved', 1, 0),
(2, 1, 3, 'Fish is okay, but shipping took longer than expected.', 'approved', 1, 0),
(3, 3, 5, 'Excellent service and beautiful fish!', 'approved', 1, 0);

-- Insert sample review images
INSERT INTO Review_images (review_id, image_url, is_deleted)
VALUES 
(1, '/uploads/reviews/sample_review_1.jpg', 0),
(1, '/uploads/reviews/sample_review_1_2.jpg', 0),
(2, '/uploads/reviews/sample_review_2.jpg', 0),
(3, '/uploads/reviews/sample_review_3.jpg', 0); 