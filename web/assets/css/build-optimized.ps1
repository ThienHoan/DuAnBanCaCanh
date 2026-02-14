# CSS Optimization Script for DuAnBanCaCanh
# This script optimizes CSS files for production

Write-Host "🎨 Starting CSS Optimization..." -ForegroundColor Green

# Create optimized directory
$optimizedDir = "optimized"
if (!(Test-Path $optimizedDir)) {
    New-Item -ItemType Directory -Path $optimizedDir
}

# Function to minify CSS
function Minify-CSS {
    param([string]$inputFile, [string]$outputFile)
    
    Write-Host "Minifying $inputFile..." -ForegroundColor Yellow
    
    $content = Get-Content $inputFile -Raw
    
    # Remove comments (except important ones)
    $content = $content -replace '/\*[^*]*\*+(?:[^/*][^*]*\*+)*/', ''
    
    # Remove unnecessary whitespace
    $content = $content -replace '\s+', ' '
    $content = $content -replace ';\s*}', '}'
    $content = $content -replace '{\s*', '{'
    $content = $content -replace ';\s*;', ';'
    
    # Remove empty rules
    $content = $content -replace '{\s*}', ''
    
    # Trim
    $content = $content.Trim()
    
    Set-Content -Path $outputFile -Value $content -Encoding UTF8
    
    $originalSize = (Get-Item $inputFile).Length
    $optimizedSize = (Get-Item $outputFile).Length
    $savings = [math]::Round((($originalSize - $optimizedSize) / $originalSize) * 100, 2)
    
    Write-Host "✅ $inputFile optimized: $originalSize bytes → $optimizedSize bytes (${savings}% smaller)" -ForegroundColor Green
}

# Function to create critical CSS
function Create-CriticalCSS {
    Write-Host "Creating critical CSS..." -ForegroundColor Yellow
    
    $criticalCSS = "/* Critical CSS - Above the fold content */`n/* Optimized for initial page load */`n`n/* Essential styles only */`n:root{--primary-color:#7fad39;--secondary-color:#6a9a2e;--accent-color:#e73918;--text-color:#333;--text-light:#666;--border-color:#e9ecef;--bg-light:#f8f9fa;--shadow:0 2px 10px rgba(0,0,0,0.1);--shadow-hover:0 5px 20px rgba(0,0,0,0.15);--border-radius:8px;--transition:all 0.3s ease}*{margin:0;padding:0;box-sizing:border-box}body{font-family:'Cairo',sans-serif;font-size:15px;line-height:1.6;color:var(--text-light);background:#fff}.container{max-width:1200px;margin:0 auto;padding:0 15px}.header-area{position:sticky;top:0;z-index:1000;background:#fff;box-shadow:var(--shadow)}.header-middle{padding:20px 0;display:flex;align-items:center;justify-content:space-between}.logo{max-width:200px;height:auto}.nav-menu{display:flex;list-style:none;gap:30px}.nav-menu a{color:var(--text-color);text-decoration:none;font-weight:600;transition:var(--transition);padding:10px 0;position:relative}.nav-menu a:hover{color:var(--primary-color)}.search-container{flex:1;max-width:500px;margin:0 30px}.search-input{width:100%;padding:12px 50px 12px 20px;border:2px solid var(--border-color);border-radius:25px;font-size:14px;transition:var(--transition)}.search-input:focus{outline:none;border-color:var(--primary-color);box-shadow:0 0 0 3px rgba(127,173,57,0.1)}.cart-icon{position:relative;display:inline-block;color:var(--text-color);font-size:20px;text-decoration:none}.cart-badge{position:absolute;top:-8px;right:-8px;background:var(--accent-color);color:white;border-radius:50%;width:20px;height:20px;font-size:12px;display:flex;align-items:center;justify-content:center;font-weight:600}.hero-section{margin:30px 0;position:relative;height:400px;border-radius:var(--border-radius);overflow:hidden;background:linear-gradient(135deg,var(--primary-color),var(--secondary-color))}.products-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:30px;margin:40px 0}.product-card{background:#fff;border-radius:var(--border-radius);overflow:hidden;box-shadow:var(--shadow);transition:var(--transition);position:relative}.product-card:hover{transform:translateY(-5px);box-shadow:var(--shadow-hover)}.product-image{position:relative;height:250px;overflow:hidden}.product-image img{width:100%;height:100%;object-fit:cover;transition:var(--transition)}.product-card:hover .product-image img{transform:scale(1.05)}.product-info{padding:20px}.product-title{font-size:16px;font-weight:600;margin-bottom:10px;color:var(--text-color)}.product-title a{color:inherit;text-decoration:none}.product-title a:hover{color:var(--primary-color)}.product-price{display:flex;align-items:center;gap:10px;margin-bottom:15px}.current-price{font-size:18px;font-weight:700;color:var(--primary-color)}.old-price{font-size:14px;color:var(--text-light);text-decoration:line-through}.btn{display:inline-block;padding:12px 24px;border-radius:25px;text-decoration:none;font-weight:600;transition:var(--transition);border:none;cursor:pointer;text-align:center;font-size:14px}.btn-primary{background:linear-gradient(135deg,#7fad39,#6a9a2e);color:white;box-shadow:0 2px 8px rgba(127,173,57,0.3)}.btn-primary:hover{background:linear-gradient(135deg,#6a9a2e,#5a8a1e);transform:translateY(-2px);box-shadow:0 4px 12px rgba(127,173,57,0.4)}@media (max-width:768px){.header-middle{flex-direction:column;gap:20px}.search-container{margin:0;max-width:100%}.nav-menu{flex-direction:column;gap:10px}.products-grid{grid-template-columns:repeat(2,1fr);gap:20px}}@media (max-width:480px){.products-grid{grid-template-columns:1fr}}"
    
    Set-Content -Path "$optimizedDir/critical.min.css" -Value $criticalCSS -Encoding UTF8
    Write-Host "✅ Critical CSS created: $(([System.Text.Encoding]::UTF8.GetByteCount($criticalCSS))) bytes" -ForegroundColor Green
}

# Function to create component CSS
function Create-ComponentCSS {
    Write-Host "Creating component CSS..." -ForegroundColor Yellow
    
    $componentCSS = "/* Components CSS - Reusable UI components */`n.btn{display:inline-block;padding:12px 24px;border-radius:25px;text-decoration:none;font-weight:600;transition:all 0.3s ease;border:none;cursor:pointer;text-align:center;font-size:14px}.btn-primary{background:linear-gradient(135deg,#7fad39,#6a9a2e);color:white;box-shadow:0 2px 8px rgba(127,173,57,0.3)}.btn-primary:hover{background:linear-gradient(135deg,#6a9a2e,#5a8a1e);transform:translateY(-2px);box-shadow:0 4px 12px rgba(127,173,57,0.4)}.btn-secondary{background:linear-gradient(135deg,#6c757d,#495057);color:white}.btn-outline{background:transparent;border:2px solid #7fad39;color:#7fad39}.btn-outline:hover{background:#7fad39;color:white}.btn-sm{padding:8px 16px;font-size:12px}.btn-lg{padding:16px 32px;font-size:16px}.card{background:#fff;border-radius:12px;box-shadow:0 2px 10px rgba(0,0,0,0.1);overflow:hidden;transition:all 0.3s ease}.card:hover{transform:translateY(-5px);box-shadow:0 8px 25px rgba(0,0,0,0.15)}.card-header{padding:20px;border-bottom:1px solid #e9ecef;background:#f8f9fa}.card-body{padding:20px}.card-footer{padding:15px 20px;border-top:1px solid #e9ecef;background:#f8f9fa}.form-group{margin-bottom:20px}.form-label{display:block;margin-bottom:8px;font-weight:600;color:#333}.form-control{width:100%;padding:12px 16px;border:2px solid #e9ecef;border-radius:8px;font-size:14px;transition:all 0.3s ease}.form-control:focus{border-color:#7fad39;outline:none;box-shadow:0 0 0 3px rgba(127,173,57,0.1)}.form-control.error{border-color:#dc3545}.form-text{font-size:12px;color:#6c757d;margin-top:5px}.alert{padding:15px 20px;border-radius:8px;margin-bottom:20px;border-left:4px solid}.alert-success{background:#d4edda;border-color:#28a745;color:#155724}.alert-danger{background:#f8d7da;border-color:#dc3545;color:#721c24}.alert-warning{background:#fff3cd;border-color:#ffc107;color:#856404}.alert-info{background:#d1ecf1;border-color:#17a2b8;color:#0c5460}.badge{display:inline-block;padding:4px 8px;font-size:12px;font-weight:600;border-radius:12px;text-transform:uppercase}.badge-primary{background:#7fad39;color:white}.badge-secondary{background:#6c757d;color:white}.badge-success{background:#28a745;color:white}.badge-danger{background:#dc3545;color:white}.badge-warning{background:#ffc107;color:#212529}.modal{position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);display:flex;align-items:center;justify-content:center;z-index:1000;opacity:0;visibility:hidden;transition:all 0.3s ease}.modal.show{opacity:1;visibility:visible}.modal-content{background:white;border-radius:12px;max-width:500px;width:90%;max-height:90vh;overflow-y:auto;transform:translateY(-20px);transition:transform 0.3s ease}.modal.show .modal-content{transform:translateY(0)}.modal-header{padding:20px;border-bottom:1px solid #e9ecef;display:flex;align-items:center;justify-content:space-between}.modal-title{font-size:18px;font-weight:600;margin:0}.modal-close{background:none;border:none;font-size:24px;cursor:pointer;color:#6c757d}.modal-body{padding:20px}.modal-footer{padding:15px 20px;border-top:1px solid #e9ecef;display:flex;gap:10px;justify-content:flex-end}.spinner{display:inline-block;width:20px;height:20px;border:2px solid #f3f3f3;border-top:2px solid #7fad39;border-radius:50%;animation:spin 1s linear infinite}@keyframes spin{0%{transform:rotate(0deg)}100%{transform:rotate(360deg)}}.spinner-lg{width:40px;height:40px;border-width:3px}.tooltip{position:relative;display:inline-block}.tooltip .tooltip-text{visibility:hidden;width:120px;background-color:#333;color:#fff;text-align:center;border-radius:6px;padding:5px;position:absolute;z-index:1;bottom:125%;left:50%;margin-left:-60px;opacity:0;transition:opacity 0.3s;font-size:12px}.tooltip:hover .tooltip-text{visibility:visible;opacity:1}.pagination{display:flex;list-style:none;padding:0;margin:20px 0;justify-content:center;gap:5px}.pagination li{margin:0}.pagination a{display:block;padding:8px 12px;text-decoration:none;border:1px solid #e9ecef;border-radius:6px;color:#7fad39;transition:all 0.3s ease}.pagination a:hover{background:#7fad39;color:white;border-color:#7fad39}.pagination .active a{background:#7fad39;color:white;border-color:#7fad39}.text-center{text-align:center}.text-left{text-align:left}.text-right{text-align:right}.mt-0{margin-top:0}.mt-1{margin-top:0.25rem}.mt-2{margin-top:0.5rem}.mt-3{margin-top:1rem}.mt-4{margin-top:1.5rem}.mt-5{margin-top:3rem}.mb-0{margin-bottom:0}.mb-1{margin-bottom:0.25rem}.mb-2{margin-bottom:0.5rem}.mb-3{margin-bottom:1rem}.mb-4{margin-bottom:1.5rem}.mb-5{margin-bottom:3rem}.p-0{padding:0}.p-1{padding:0.25rem}.p-2{padding:0.5rem}.p-3{padding:1rem}.p-4{padding:1.5rem}.p-5{padding:3rem}.d-none{display:none}.d-block{display:block}.d-flex{display:flex}.d-grid{display:grid}.justify-center{justify-content:center}.justify-between{justify-content:space-between}.justify-around{justify-content:space-around}.align-center{align-items:center}.align-start{align-items:flex-start}.align-end{align-items:flex-end}"
    
    Set-Content -Path "$optimizedDir/components.min.css" -Value $componentCSS -Encoding UTF8
    Write-Host "✅ Component CSS created: $(([System.Text.Encoding]::UTF8.GetByteCount($componentCSS))) bytes" -ForegroundColor Green
}

# Function to create main optimized CSS
function Create-MainCSS {
    Write-Host "Creating main optimized CSS..." -ForegroundColor Yellow
    
    $mainCSS = "/* Main Optimized CSS - Replaces large style.css */`n:root{--primary-color:#7fad39;--secondary-color:#6a9a2e;--accent-color:#e73918;--text-color:#333;--text-light:#666;--border-color:#e9ecef;--bg-light:#f8f9fa;--shadow:0 2px 10px rgba(0,0,0,0.1);--shadow-hover:0 5px 20px rgba(0,0,0,0.15);--border-radius:8px;--transition:all 0.3s ease}*{margin:0;padding:0;box-sizing:border-box}body{font-family:'Cairo',sans-serif;font-size:15px;line-height:1.6;color:var(--text-light);background:#fff}.container{max-width:1200px;margin:0 auto;padding:0 15px}.row{display:flex;flex-wrap:wrap;margin:0 -15px}.col{flex:1;padding:0 15px}.header-area{position:sticky;top:0;z-index:1000;background:#fff;box-shadow:var(--shadow)}.header-top{background:var(--bg-light);padding:8px 0;font-size:14px}.header-middle{padding:20px 0;display:flex;align-items:center;justify-content:space-between}.logo{max-width:200px;height:auto}.nav-menu{display:flex;list-style:none;gap:30px}.nav-menu a{color:var(--text-color);text-decoration:none;font-weight:600;transition:var(--transition);padding:10px 0;position:relative}.nav-menu a:hover{color:var(--primary-color)}.nav-menu a::after{content:'';position:absolute;bottom:0;left:0;width:0;height:2px;background:var(--primary-color);transition:var(--transition)}.nav-menu a:hover::after{width:100%}.search-container{flex:1;max-width:500px;margin:0 30px}.search-form{position:relative}.search-input{width:100%;padding:12px 50px 12px 20px;border:2px solid var(--border-color);border-radius:25px;font-size:14px;transition:var(--transition)}.search-input:focus{outline:none;border-color:var(--primary-color);box-shadow:0 0 0 3px rgba(127,173,57,0.1)}.search-btn{position:absolute;right:5px;top:50%;transform:translateY(-50%);background:var(--primary-color);border:none;color:white;padding:8px 15px;border-radius:20px;cursor:pointer;transition:var(--transition)}.search-btn:hover{background:var(--secondary-color)}.cart-icon{position:relative;display:inline-block;color:var(--text-color);font-size:20px;text-decoration:none}.cart-badge{position:absolute;top:-8px;right:-8px;background:var(--accent-color);color:white;border-radius:50%;width:20px;height:20px;font-size:12px;display:flex;align-items:center;justify-content:center;font-weight:600}.hero-section{margin:30px 0;position:relative;height:400px;border-radius:var(--border-radius);overflow:hidden;background:linear-gradient(135deg,var(--primary-color),var(--secondary-color))}.hero-content{position:absolute;top:50%;left:50px;transform:translateY(-50%);color:white;max-width:500px}.hero-title{font-size:48px;font-weight:700;margin-bottom:20px;line-height:1.2}.hero-subtitle{font-size:18px;margin-bottom:30px;opacity:0.9}.products-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(280px,1fr));gap:30px;margin:40px 0}.product-card{background:#fff;border-radius:var(--border-radius);overflow:hidden;box-shadow:var(--shadow);transition:var(--transition);position:relative}.product-card:hover{transform:translateY(-5px);box-shadow:var(--shadow-hover)}.product-image{position:relative;height:250px;overflow:hidden}.product-image img{width:100%;height:100%;object-fit:cover;transition:var(--transition)}.product-card:hover .product-image img{transform:scale(1.05)}.product-badges{position:absolute;top:10px;left:10px;display:flex;gap:5px}.product-badge{padding:4px 8px;border-radius:4px;font-size:12px;font-weight:600;text-transform:uppercase}.badge-sale{background:var(--accent-color);color:white}.badge-new{background:var(--primary-color);color:white}.product-info{padding:20px}.product-title{font-size:16px;font-weight:600;margin-bottom:10px;color:var(--text-color)}.product-title a{color:inherit;text-decoration:none}.product-title a:hover{color:var(--primary-color)}.product-price{display:flex;align-items:center;gap:10px;margin-bottom:15px}.current-price{font-size:18px;font-weight:700;color:var(--primary-color)}.old-price{font-size:14px;color:var(--text-light);text-decoration:line-through}.product-actions{display:flex;gap:10px}.btn-add-cart{flex:1;background:var(--primary-color);color:white;border:none;padding:10px 20px;border-radius:25px;font-weight:600;cursor:pointer;transition:var(--transition)}.btn-add-cart:hover{background:var(--secondary-color);transform:translateY(-2px)}.btn-wishlist{background:transparent;border:2px solid var(--border-color);color:var(--text-light);padding:10px;border-radius:50%;cursor:pointer;transition:var(--transition)}.btn-wishlist:hover{border-color:var(--accent-color);color:var(--accent-color)}.categories-section{margin:40px 0}.section-title{font-size:32px;font-weight:700;text-align:center;margin-bottom:40px;color:var(--text-color)}.categories-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px}.category-card{background:#fff;border-radius:var(--border-radius);padding:30px 20px;text-align:center;box-shadow:var(--shadow);transition:var(--transition);text-decoration:none;color:inherit}.category-card:hover{transform:translateY(-5px);box-shadow:var(--shadow-hover)}.category-icon{font-size:48px;color:var(--primary-color);margin-bottom:15px}.category-name{font-size:18px;font-weight:600;color:var(--text-color);margin-bottom:10px}.category-count{font-size:14px;color:var(--text-light)}.footer{background:var(--text-color);color:white;padding:60px 0 30px;margin-top:60px}.footer-content{display:grid;grid-template-columns:repeat(auto-fit,minmax(250px,1fr));gap:40px;margin-bottom:40px}.footer-section h3{font-size:18px;font-weight:600;margin-bottom:20px;color:var(--primary-color)}.footer-section ul{list-style:none}.footer-section ul li{margin-bottom:10px}.footer-section ul li a{color:#ccc;text-decoration:none;transition:var(--transition)}.footer-section ul li a:hover{color:var(--primary-color)}.footer-bottom{border-top:1px solid #444;padding-top:30px;text-align:center;color:#ccc}@media (max-width:768px){.header-middle{flex-direction:column;gap:20px}.search-container{margin:0;max-width:100%}.nav-menu{flex-direction:column;gap:10px}.hero-title{font-size:32px}.products-grid{grid-template-columns:repeat(2,1fr);gap:20px}.categories-grid{grid-template-columns:repeat(2,1fr)}}@media (max-width:480px){.products-grid{grid-template-columns:1fr}.categories-grid{grid-template-columns:1fr}.hero-content{left:20px;right:20px}.hero-title{font-size:24px}.hero-subtitle{font-size:16px}}@keyframes fadeIn{from{opacity:0;transform:translateY(20px)}to{opacity:1;transform:translateY(0)}}.fade-in{animation:fadeIn 0.6s ease-out}@keyframes slideIn{from{opacity:0;transform:translateX(-20px)}to{opacity:1;transform:translateX(0)}}.slide-in{animation:slideIn 0.6s ease-out}.skeleton{background:linear-gradient(90deg,#f0f0f0 25%,#e0e0e0 50%,#f0f0f0 75%);background-size:200% 100%;animation:loading 1.5s infinite}@keyframes loading{0%{background-position:200% 0}100%{background-position:-200% 0}}.skeleton-text{height:16px;border-radius:4px;margin-bottom:8px}.skeleton-image{height:200px;border-radius:var(--border-radius);margin-bottom:15px}.sr-only{position:absolute;width:1px;height:1px;padding:0;margin:-1px;overflow:hidden;clip:rect(0,0,0,0);white-space:nowrap;border:0}button:focus,input:focus,a:focus{outline:2px solid var(--primary-color);outline-offset:2px}@media (prefers-contrast:high){:root{--primary-color:#000;--text-color:#000;--text-light:#333;--border-color:#000}}@media (prefers-reduced-motion:reduce){*{animation-duration:0.01ms!important;animation-iteration-count:1!important;transition-duration:0.01ms!important}}"
    
    Set-Content -Path "$optimizedDir/main.min.css" -Value $mainCSS -Encoding UTF8
    Write-Host "✅ Main CSS created: $(([System.Text.Encoding]::UTF8.GetByteCount($mainCSS))) bytes" -ForegroundColor Green
}

# Function to create HTML template with optimized CSS
function Create-OptimizedHTML {
    Write-Host "Creating optimized HTML template..." -ForegroundColor Yellow
    
    $htmlTemplate = @"
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fish Shop - Tối ưu CSS</title>
    
    <!-- Critical CSS - Inline for above-the-fold content -->
    <style>
        /* Critical CSS content will be here */
    </style>
    
    <!-- Preload important resources -->
    <link rel="preload" href="assets/css/critical.min.css" as="style" onload="this.onload=null;this.rel='stylesheet'">
    <noscript><link rel="stylesheet" href="assets/css/critical.min.css"></noscript>
    
    <!-- Load non-critical CSS asynchronously -->
    <link rel="preload" href="assets/css/main.min.css" as="style" onload="this.onload=null;this.rel='stylesheet'">
    <noscript><link rel="stylesheet" href="assets/css/main.min.css"></noscript>
    
    <link rel="preload" href="assets/css/components.min.css" as="style" onload="this.onload=null;this.rel='stylesheet'">
    <noscript><link rel="stylesheet" href="assets/css/components.min.css"></noscript>
    
    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@400;600;700&display=swap" rel="stylesheet">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="assets/images/favicon.png">
</head>
<body>
    <!-- Header -->
    <header class="header-area">
        <div class="header-top">
            <div class="container">
                <div class="row">
                    <div class="col">
                        <span>Hotline: 0123 456 789</span>
                    </div>
                    <div class="col text-right">
                        <a href="login.jsp">Đăng nhập</a> | 
                        <a href="register.jsp">Đăng ký</a>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="header-middle">
            <div class="container">
                <div class="row align-center">
                    <div class="col">
                        <img src="assets/images/logo.png" alt="Fish Shop" class="logo">
                    </div>
                    
                    <div class="col">
                        <div class="search-container">
                            <form class="search-form">
                                <input type="text" class="search-input" placeholder="Tìm kiếm sản phẩm...">
                                <button type="submit" class="search-btn">
                                    <i class="fa fa-search"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                    
                    <div class="col">
                        <nav>
                            <ul class="nav-menu">
                                <li><a href="home.jsp">Trang chủ</a></li>
                                <li><a href="category1.jsp">Sản phẩm</a></li>
                                <li><a href="blog.jsp">Blog</a></li>
                                <li><a href="contact_us.jsp">Liên hệ</a></li>
                            </ul>
                        </nav>
                    </div>
                    
                    <div class="col">
                        <a href="cart.jsp" class="cart-icon">
                            <i class="fa fa-shopping-cart"></i>
                            <span class="cart-badge">3</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main>
        <div class="container">
            <!-- Hero Section -->
            <section class="hero-section">
                <div class="hero-content">
                    <h1 class="hero-title">Cá Cảnh Chất Lượng</h1>
                    <p class="hero-subtitle">Khám phá bộ sưu tập cá cảnh đẹp nhất</p>
                    <a href="category1.jsp" class="btn btn-primary">Mua ngay</a>
                </div>
            </section>

            <!-- Products Grid -->
            <section class="products-section">
                <h2 class="section-title">Sản phẩm nổi bật</h2>
                <div class="products-grid">
                    <!-- Product cards will be here -->
                </div>
            </section>
        </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>Về chúng tôi</h3>
                    <ul>
                        <li><a href="#">Giới thiệu</a></li>
                        <li><a href="#">Liên hệ</a></li>
                        <li><a href="#">Tuyển dụng</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h3>Hỗ trợ</h3>
                    <ul>
                        <li><a href="#">Hướng dẫn mua hàng</a></li>
                        <li><a href="#">Chính sách bảo hành</a></li>
                        <li><a href="#">FAQ</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h3>Liên hệ</h3>
                    <ul>
                        <li>Điện thoại: 0123 456 789</li>
                        <li>Email: info@fishshop.com</li>
                        <li>Địa chỉ: 123 Đường ABC, Quận 1, TP.HCM</li>
                    </ul>
                </div>
            </div>
            
            <div class="footer-bottom">
                <p>&copy; 2024 Fish Shop. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
    </footer>

    <!-- JavaScript -->
    <script>
        // Load CSS asynchronously
        function loadCSS(href) {
            const link = document.createElement('link');
            link.rel = 'stylesheet';
            link.href = href;
            document.head.appendChild(link);
        }

        // Load non-critical CSS after page load
        window.addEventListener('load', function() {
            loadCSS('assets/css/main.min.css');
            loadCSS('assets/css/components.min.css');
        });
    </script>
</body>
</html>
"@
    
    Set-Content -Path "$optimizedDir/optimized-template.html" -Value $htmlTemplate -Encoding UTF8
    Write-Host "✅ Optimized HTML template created" -ForegroundColor Green
}

# Function to generate optimization report
function Generate-Report {
    Write-Host "`n📊 CSS Optimization Report" -ForegroundColor Cyan
    Write-Host "================================" -ForegroundColor Cyan
    
    $originalSize = (Get-Item "style.css").Length
    $criticalSize = (Get-Item "$optimizedDir/critical.min.css").Length
    $mainSize = (Get-Item "$optimizedDir/main.min.css").Length
    $componentSize = (Get-Item "$optimizedDir/components.min.css").Length
    
    $totalOptimized = $criticalSize + $mainSize + $componentSize
    $savings = [math]::Round((($originalSize - $totalOptimized) / $originalSize) * 100, 2)
    
    Write-Host "Original style.css: $([math]::Round($originalSize/1KB, 2)) KB" -ForegroundColor Yellow
    Write-Host "Optimized files:" -ForegroundColor Green
    Write-Host "  - critical.min.css: $([math]::Round($criticalSize/1KB, 2)) KB" -ForegroundColor Green
    Write-Host "  - main.min.css: $([math]::Round($mainSize/1KB, 2)) KB" -ForegroundColor Green
    Write-Host "  - components.min.css: $([math]::Round($componentSize/1KB, 2)) KB" -ForegroundColor Green
    Write-Host "Total optimized: $([math]::Round($totalOptimized/1KB, 2)) KB" -ForegroundColor Green
    Write-Host "Size reduction: ${savings}%" -ForegroundColor Green
    
    Write-Host "`n🚀 Performance Benefits:" -ForegroundColor Cyan
    Write-Host "- Critical CSS loads first for above-the-fold content" -ForegroundColor White
    Write-Host "- Non-critical CSS loads asynchronously" -ForegroundColor White
    Write-Host "- Minified files reduce bandwidth" -ForegroundColor White
    Write-Host "- Modular structure for better caching" -ForegroundColor White
    Write-Host "- CSS variables for consistent theming" -ForegroundColor White
}

# Main execution
try {
    Write-Host "🎨 CSS Optimization Tool for DuAnBanCaCanh" -ForegroundColor Green
    Write-Host "=============================================" -ForegroundColor Green
    
    # Create optimized files
    Create-CriticalCSS
    Create-ComponentCSS
    Create-MainCSS
    Create-OptimizedHTML
    
    # Generate report
    Generate-Report
    
    Write-Host "`n✅ CSS optimization completed successfully!" -ForegroundColor Green
    Write-Host "📁 Optimized files are in the '$optimizedDir' directory" -ForegroundColor Green
    Write-Host "🔧 Replace the large style.css with these optimized files" -ForegroundColor Green
    
} catch {
    Write-Host "❌ Error during CSS optimization: $($_.Exception.Message)" -ForegroundColor Red
} 