-- PostgreSQL Database Initialization Script
-- Converted from SQL Server to PostgreSQL

-- Create database schema
CREATE SCHEMA IF NOT EXISTS public;

-- Create blog_categories table
CREATE TABLE IF NOT EXISTS blog_categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create blog_posts table  
CREATE TABLE IF NOT EXISTS blog_posts (
    post_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    summary VARCHAR(500),
    featured_image VARCHAR(255),
    author_id INTEGER,
    tags VARCHAR(500),
    status VARCHAR(20) DEFAULT 'draft',
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false,
    category_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES blog_categories(category_id)
);

-- Create users table (for blog authors)
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'user',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    google_id VARCHAR(100),
    avatar_url VARCHAR(255)
);

-- Create products table (main fish shop functionality)
CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER DEFAULT 0,
    category_id INTEGER,
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create product_categories table
CREATE TABLE IF NOT EXISTS product_categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    user_id INTEGER,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    shipping_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    item_id SERIAL PRIMARY KEY,
    order_id INTEGER,
    product_id INTEGER,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Add foreign key constraints
ALTER TABLE products ADD CONSTRAINT fk_products_category 
    FOREIGN KEY (category_id) REFERENCES product_categories(category_id);

ALTER TABLE blog_posts ADD CONSTRAINT fk_blog_posts_author 
    FOREIGN KEY (author_id) REFERENCES users(user_id);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_blog_posts_status ON blog_posts(status);
CREATE INDEX IF NOT EXISTS idx_blog_posts_category ON blog_posts(category_id);
CREATE INDEX IF NOT EXISTS idx_blog_posts_created_at ON blog_posts(created_at);
CREATE INDEX IF NOT EXISTS idx_blog_posts_published_at ON blog_posts(published_at);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_active ON products(is_active);

-- Create views for blog statistics
CREATE OR REPLACE VIEW v_blog_statistics AS
SELECT 
    COUNT(*) as total_posts,
    COUNT(CASE WHEN status = 'published' THEN 1 END) as published_posts,
    COUNT(CASE WHEN status = 'draft' THEN 1 END) as draft_posts,
    SUM(view_count) as total_views,
    AVG(view_count) as avg_views_per_post
FROM blog_posts 
WHERE is_deleted = false;

-- Create view for popular posts
CREATE OR REPLACE VIEW v_popular_posts AS
SELECT 
    bp.post_id,
    bp.title,
    bp.summary,
    bp.view_count,
    bp.created_at,
    bc.category_name,
    u.full_name as author_name
FROM blog_posts bp
LEFT JOIN blog_categories bc ON bp.category_id = bc.category_id
LEFT JOIN users u ON bp.author_id = u.user_id
WHERE bp.status = 'published' AND bp.is_deleted = false
ORDER BY bp.view_count DESC
LIMIT 10;

-- Insert sample data
-- Insert blog categories
INSERT INTO blog_categories (category_name, description, is_active) VALUES
('Chăm sóc cá', 'Hướng dẫn chăm sóc các loại cá cảnh', true),
('Thiết bị hồ cá', 'Thông tin về các thiết bị cần thiết cho hồ cá', true),
('Loài cá cảnh', 'Giới thiệu các loài cá cảnh phổ biến', true),
('Thức ăn cho cá', 'Hướng dẫn về thức ăn và dinh dưỡng cho cá', true),
('Bệnh và điều trị', 'Thông tin về bệnh thường gặp ở cá cảnh', true)
ON CONFLICT (category_name) DO NOTHING;

-- Insert product categories
INSERT INTO product_categories (category_name, description, is_active) VALUES
('Cá cảnh', 'Các loại cá cảnh đẹp', true),
('Thức ăn cá', 'Thức ăn dinh dưỡng cho cá', true),
('Thiết bị lọc', 'Thiết bị lọc nước cho hồ cá', true),
('Phụ kiện trang trí', 'Phụ kiện trang trí hồ cá', true),
('Hồ cá', 'Các loại hồ cá và bể cá', true)
ON CONFLICT (category_name) DO NOTHING;

-- Insert admin user
INSERT INTO users (username, email, password_hash, full_name, role, is_active) VALUES
('admin', 'admin@fishshop.com', '$2a$10$xGsJUL7/O1x7qzSCWrPe4.ClgSO3KKWKzDAWsT6LzU3Ztq0Np1XhC', 'Administrator', 'admin', true)
ON CONFLICT (username) DO NOTHING;

-- Insert sample blog posts
INSERT INTO blog_posts (title, content, summary, author_id, category_id, status, view_count, published_at) VALUES
('Cách chăm sóc cá vàng tại nhà', 
 '<p>Cá vàng là một trong những loài cá cảnh phổ biến nhất. Để chăm sóc cá vàng tốt, bạn cần chú ý đến chất lượng nước, thức ăn và môi trường sống...</p>', 
 'Hướng dẫn chi tiết cách chăm sóc cá vàng tại nhà cho người mới bắt đầu', 
 1, 1, 'published', 150, CURRENT_TIMESTAMP),

('Top 10 loài cá cảnh đẹp nhất', 
 '<p>Khám phá những loài cá cảnh đẹp nhất và phổ biến nhất hiện nay. Từ cá betta đến cá thiên thần...</p>', 
 'Danh sách 10 loài cá cảnh được yêu thích nhất', 
 1, 3, 'published', 200, CURRENT_TIMESTAMP),

('Thiết bị cần thiết cho hồ cá mới', 
 '<p>Khi mới bắt đầu nuôi cá cảnh, việc chuẩn bị đầy đủ thiết bị là rất quan trọng...</p>', 
 'Danh sách thiết bị cơ bản cần có khi bắt đầu nuôi cá cảnh', 
 1, 2, 'published', 120, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Create trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers to tables
CREATE TRIGGER update_blog_posts_updated_at 
    BEFORE UPDATE ON blog_posts 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_blog_categories_updated_at 
    BEFORE UPDATE ON blog_categories 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at 
    BEFORE UPDATE ON products 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO fishshop_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO fishshop_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO fishshop_user;
