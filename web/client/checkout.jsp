<%-- 
    Document   : checkout
    Created on : May 20, 2025, 3:42:10 PM
    Author     : hoan6
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Biolife - Organic Food</title>
    <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/favicon.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/nice-select.css">
    <link rel="stylesheet" href="assets/css/slick.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color.css">
</head>
<body class="biolife-body">

    <!-- Preloader -->
    <div id="biof-loading">
        <div class="biof-loading-center">
            <div class="biof-loading-center-absolute">
                <div class="dot dot-one"></div>
                <div class="dot dot-two"></div>
                <div class="dot dot-three"></div>
            </div>
        </div>
    </div>

    <!-- HEADER -->
    <header id="header" class="header-area style-01 layout-03">
        <div class="header-top bg-main hidden-xs">
            <div class="container">
                <div class="top-bar left">
                    <ul class="horizontal-menu">
                        <li><a href="#"><i class="fa fa-envelope" aria-hidden="true"></i>Organic@company.com</a></li>
                        <li><a href="#">Free Shipping for all Order of $99</a></li>
                    </ul>
                </div>
                <div class="top-bar right">
                    <ul class="social-list">
                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                        <li><a href="#"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                    </ul>
                    <ul class="horizontal-menu">
                        <li class="horz-menu-item currency">
                            <select name="currency">
                                <option value="eur">€ EUR (Euro)</option>
                                <option value="usd" selected>$ USD (Dollar)</option>
                                <option value="usd">£ GBP (Pound)</option>
                                <option value="usd">¥ JPY (Yen)</option>
                            </select>
                        </li>
                        <li class="horz-menu-item lang">
                            <select name="language">
                                <option value="fr">French (EUR)</option>
                                <option value="en" selected>English (USD)</option>
                                <option value="ger">Germany (GBP)</option>
                                <option value="jp">Japan (JPY)</option>
                            </select>
                        </li>
                        <li><a href="login.html" class="login-link"><i class="biolife-icon icon-login"></i>Login/Register</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="header-middle biolife-sticky-object ">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-2 col-md-6 col-xs-6">
                        <a href="index-2.html" class="biolife-logo"><img src="assets/images/organic-3.png" alt="biolife logo" width="135" height="34"></a>
                    </div>
                    <div class="col-lg-6 col-md-7 hidden-sm hidden-xs">
                        <div class="primary-menu">
                            <ul class="menu biolife-menu clone-main-menu clone-primary-menu" id="primary-menu" data-menuname="main menu">
                                <li class="menu-item"><a href="index-2.html">Home</a></li>
                                <li class="menu-item menu-item-has-children has-megamenu">
                                    <a href="#" class="menu-name" data-title="Shop" >Shop</a>
                                    <div class="wrap-megamenu lg-width-900 md-width-750">
                                        <div class="mega-content">
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Fresh Berries</h4>
                                                    <ul class="menu">
                                                        <li><a href="#">Fruit & Nut Gifts</a></li>
                                                        <li><a href="#">Mixed Fruits</a></li>
                                                        <li><a href="#">Oranges</a></li>
                                                        <li><a href="#">Bananas & Plantains</a></li>
                                                        <li><a href="#">Fresh Gala Apples</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Vegetables</h4>
                                                    <ul class="menu">
                                                        <li><a href="#">Berries</a></li>
                                                        <li><a href="#">Pears</a></li>
                                                        <li><a href="#">Chili Peppers</a></li>
                                                        <li><a href="#">Fresh Avocado</a></li>
                                                        <li><a href="#">Grapes</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu ">
                                                    <h4 class="menu-title">Fresh Fruits</h4>
                                                    <ul class="menu">
                                                        <li><a href="#">Basket of apples</a></li>
                                                        <li><a href="#">Strawberry</a></li>
                                                        <li><a href="#">Blueberry</a></li>
                                                        <li><a href="#">Orange</a></li>
                                                        <li><a href="#">Pineapple</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Featured Products</h4>
                                                    <ul class="menu">
                                                        <li><a href="#">Coffee Creamers</a></li>
                                                        <li><a href="#">Mayonnaise</a></li>
                                                        <li><a href="#">Almond Milk</a></li>
                                                        <li><a href="#">Fruit Jam</a></li>
                                                        <li><a href="#">Beverages</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="menu-item menu-item-has-children has-child">
                                    <a href="#" class="menu-name" data-title="Products">Products</a>
                                    <ul class="sub-menu">
                                        <li class="menu-item"><a href="#">Omelettes</a></li>
                                        <li class="menu-item"><a href="#">Breakfast Scrambles</a></li>
                                        <li class="menu-item menu-item-has-children has-child"><a href="#" class="menu-name" data-title="Eggs & other considerations">Eggs & other considerations</a>
                                            <ul class="sub-menu">
                                                <li class="menu-item"><a href="#">Classic Breakfast</a></li>
                                                <li class="menu-item"><a href="#">Huevos Rancheros</a></li>
                                                <li class="menu-item"><a href="#">Everything Egg Sandwich</a></li>
                                                <li class="menu-item"><a href="#">Egg Sandwich</a></li>
                                                <li class="menu-item"><a href="#">Vegan Burrito</a></li>
                                                <li class="menu-item"><a href="#">Biscuits and Gravy</a></li>
                                                <li class="menu-item"><a href="#">Bacon Avo Egg Sandwich</a></li>
                                            </ul>
                                        </li>
                                        <li class="menu-item"><a href="#">Griddle</a></li>
                                        <li class="menu-item menu-item-has-children has-child"><a href="#" class="menu-name" data-title="Sides & Extras">Sides & Extras</a>
                                            <ul class="sub-menu">
                                                <li class="menu-item"><a href="#">Breakfast Burrito</a></li>
                                                <li class="menu-item"><a href="#">Crab Cake Benedict</a></li>
                                                <li class="menu-item"><a href="#">Corned Beef Hash</a></li>
                                                <li class="menu-item"><a href="#">Steak & Eggs</a></li>
                                                <li class="menu-item"><a href="#">Oatmeal</a></li>
                                                <li class="menu-item"><a href="#">Fruit & Yogurt Parfait</a></li>
                                            </ul>
                                        </li>
                                        <li class="menu-item"><a href="#">Biscuits</a></li>
                                        <li class="menu-item"><a href="#">Seasonal Fruit Plate</a></li>
                                    </ul>
                                </li>
                                <li class="menu-item menu-item-has-children has-megamenu">
                                    <a href="#" class="menu-name" data-title="Demo">Demo</a>
                                    <div class="wrap-megamenu lg-width-800 md-width-750">
                                        <div class="mega-content">
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Home Page</h4>
                                                    <ul class="menu">
                                                        <li><a href="home-01.html">Home 01</a></li>
                                                        <li><a href="home-02.html">Home 02</a></li>
                                                        <li><a href="index-2.html">Home 03</a></li>
                                                        <li><a href="home-03-green.html">Home 03 Green</a></li>
                                                        <li><a href="home-04.html">Home 04</a></li>
                                                        <li><a href="home-04-light.html">Home 04 Light</a></li>
                                                        <li><a href="home-05.html">Home 05</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Inner Pages</h4>
                                                    <ul class="menu">
                                                        <li class="menu-item" ><a class="menu-name" href="blog-post.html">Blog Single</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="blog-v01.html">Blog Style 01</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="blog-v02.html">Blog Style 02</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="blog-v03.html">Blog Style 03</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="contact.html">Contact Us</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="about-us.html">About Us</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="checkout.html">Checkout</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="shopping-cart.html">Shopping Cart</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="login.html">Login/Register</a></li>
                                                        <li class="menu-item" ><a class="menu-name" href="404.html">404</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Category Pages</h4>
                                                    <ul class="menu">
                                                        <li><a href="category-grid-3-cols.html">Grid 3 Cols</a></li>
                                                        <li><a href="category-grid.html">Grid 4 Cols</a></li>
                                                        <li><a href="category-grid-6-cols.html">Grid 6 Cols</a></li>
                                                        <li><a href="category-grid-left-sidebar.html">Grid Left Sidebar</a></li>
                                                        <li><a href="category-grid-right-sidebar.html">Grid Right Sidebar</a></li>
                                                        <li><a href="category-list.html">List Full</a></li>
                                                        <li><a href="category-list-left-sidebar.html">List Left Sidebar</a></li>
                                                        <li><a href="category-list-right-sidebar.html">List Right Sidebar</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-12 md-margin-bottom-0 xs-margin-bottom-25">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Product Types</h4>
                                                    <ul class="menu">
                                                        <li><a href="single-product-simple.html">Simple</a></li>
                                                        <li><a href="single-product-grouped.html">Grouped</a></li>
                                                        <li><a href="single-product.html">Variable</a></li>
                                                        <li><a href="single-product-external.html">External/Affiliate</a></li>
                                                        <li><a href="single-product-onsale.html">Countdown</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="menu-item menu-item-has-children has-megamenu">
                                    <a href="#" class="menu-name" data-title="Blog">Blog</a>
                                    <div class="wrap-megamenu lg-width-800 md-width-750">
                                        <div class="mega-content">
                                            <div class="col-lg-3 col-md-3 col-xs-6">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Blog Categories</h4>
                                                    <ul class="menu">
                                                        <li><a href="#">Beauty (30)</a></li>
                                                        <li><a href="#">Fashion (50)</a></li>
                                                        <li><a href="#">Food (10)</a></li>
                                                        <li><a href="#">Life Style (60)</a></li>
                                                        <li><a href="#">Travel (10)</a></li>
                                                        <li><a href="#">Nutrition (35)</a></li>
                                                        <li><a href="#">Food Decoration (45)</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-3 col-md-3 col-xs-6">
                                                <div class="wrap-custom-menu vertical-menu">
                                                    <h4 class="menu-title">Featured Posts</h4>
                                                    <ul class="menu">
                                                        <li><a href="#">Post example<sup>#1</sup></a></li>
                                                        <li><a href="#">Post example<sup>#2</sup></a></li>
                                                        <li><a href="#">Post example<sup>#3</sup></a></li>
                                                        <li><a href="#">Post example<sup>#4</sup></a></li>
                                                        <li><a href="#">Post example<sup>#5</sup></a></li>
                                                        <li><a href="#">Post example<sup>#6</sup></a></li>
                                                        <li><a href="#">Post example<sup>#7</sup></a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="col-lg-6 col-md-6 col-xs-12 md-margin-top-0 xs-margin-top-25px">
                                                <div class="block-posts">
                                                    <h4 class="menu-title">Recent Posts</h4>
                                                    <ul class="posts">
                                                        <li>
                                                            <div class="block-post-item">
                                                                <div class="thumb"><a href="#"><img src="assets/images/megamenu/thumb-05.jpg" width="100" height="73" alt=""></a></div>
                                                                <div class="left-info">
                                                                    <h4 class="post-name"><a href="#">Ashwagandha: The #1 Herb in the World for Anxiety?</a></h4>
                                                                    <span class="p-date">Jan 05, 2019</span>
                                                                    <span class="p-comment">2 Comments</span>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <div class="block-post-item">
                                                                <div class="thumb"><a href="#"><img src="assets/images/megamenu/thumb-06.jpg" width="100" height="73" alt=""></a></div>
                                                                <div class="left-info">
                                                                    <h4 class="post-name"><a href="#">Ashwagandha: The #1 Herb in the World for Anxiety?</a></h4>
                                                                    <span class="p-date">May 15, 2019</span>
                                                                    <span class="p-comment">8 Comments</span>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <div class="block-post-item">
                                                                <div class="thumb"><a href="#"><img src="assets/images/megamenu/thumb-07.jpg" width="100" height="73" alt=""></a></div>
                                                                <div class="left-info">
                                                                    <h4 class="post-name"><a href="#">Ashwagandha: The #1 Herb in the World for Anxiety?</a></h4>
                                                                    <span class="p-date">Apr 26, 2019</span>
                                                                    <span class="p-comment">10 Comments</span>
                                                                </div>
                                                            </div>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="menu-item"><a href="contact.html">Contact</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-3 col-md-6 col-xs-6">
                        <div class="biolife-cart-info">
                            <div class="mobile-search">
                                <a href="javascript:void(0)" class="open-searchbox"><i class="biolife-icon icon-search"></i></a>
                                <div class="mobile-search-content">
                                    <form action="#" class="form-search" name="mobile-seacrh" method="get">
                                        <a href="#" class="btn-close"><span class="biolife-icon icon-close-menu"></span></a>
                                        <input type="text" name="s" class="input-text" value="" placeholder="Search here...">
                                        <select name="category">
                                            <option value="-1" selected>All Categories</option>
                                            <option value="vegetables">Vegetables</option>
                                            <option value="fresh_berries">Fresh Berries</option>
                                            <option value="ocean_foods">Ocean Foods</option>
                                            <option value="butter_eggs">Butter & Eggs</option>
                                            <option value="fastfood">Fastfood</option>
                                            <option value="fresh_meat">Fresh Meat</option>
                                            <option value="fresh_onion">Fresh Onion</option>
                                            <option value="papaya_crisps">Papaya & Crisps</option>
                                            <option value="oatmeal">Oatmeal</option>
                                        </select>
                                        <button type="submit" class="btn-submit">go</button>
                                    </form>
                                </div>
                            </div>
                            <div class="wishlist-block hidden-sm hidden-xs">
                                <a href="#" class="link-to">
                                    <span class="icon-qty-combine">
                                        <i class="icon-heart-bold biolife-icon"></i>
                                        <span class="qty">4</span>
                                    </span>
                                </a>
                            </div>
                            <div class="minicart-block">
                                <div class="minicart-contain">
                                    <a href="javascript:void(0)" class="link-to">
                                            <span class="icon-qty-combine">
                                                <i class="icon-cart-mini biolife-icon"></i>
                                                <span class="qty">8</span>
                                            </span>
                                        <span class="title">My Cart -</span>
                                        <span class="sub-total">$0.00</span>
                                    </a>
                                    <div class="cart-content">
                                        <div class="cart-inner">
                                            <ul class="products">
                                                <li>
                                                    <div class="minicart-item">
                                                        <div class="thumb">
                                                            <a href="#"><img src="assets/images/minicart/pr-01.jpg" width="90" height="90" alt="National Fresh"></a>
                                                        </div>
                                                        <div class="left-info">
                                                            <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                            </div>
                                                            <div class="qty">
                                                                <label for="cart[id123][qty]">Qty:</label>
                                                                <input type="number" class="input-qty" name="cart[id123][qty]" id="cart[id123][qty]" value="1" disabled>
                                                            </div>
                                                        </div>
                                                        <div class="action">
                                                            <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                            <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="minicart-item">
                                                        <div class="thumb">
                                                            <a href="#"><img src="assets/images/minicart/pr-02.jpg" width="90" height="90" alt="National Fresh"></a>
                                                        </div>
                                                        <div class="left-info">
                                                            <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                            </div>
                                                            <div class="qty">
                                                                <label for="cart[id124][qty]">Qty:</label>
                                                                <input type="number" class="input-qty" name="cart[id124][qty]" id="cart[id124][qty]" value="1" disabled>
                                                            </div>
                                                        </div>
                                                        <div class="action">
                                                            <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                            <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="minicart-item">
                                                        <div class="thumb">
                                                            <a href="#"><img src="assets/images/minicart/pr-03.jpg" width="90" height="90" alt="National Fresh"></a>
                                                        </div>
                                                        <div class="left-info">
                                                            <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                            </div>
                                                            <div class="qty">
                                                                <label for="cart[id125][qty]">Qty:</label>
                                                                <input type="number" class="input-qty" name="cart[id125][qty]" id="cart[id125][qty]" value="1" disabled>
                                                            </div>
                                                        </div>
                                                        <div class="action">
                                                            <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                            <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="minicart-item">
                                                        <div class="thumb">
                                                            <a href="#"><img src="assets/images/minicart/pr-04.jpg" width="90" height="90" alt="National Fresh"></a>
                                                        </div>
                                                        <div class="left-info">
                                                            <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                            </div>
                                                            <div class="qty">
                                                                <label for="cart[id126][qty]">Qty:</label>
                                                                <input type="number" class="input-qty" name="cart[id126][qty]" id="cart[id126][qty]" value="1" disabled>
                                                            </div>
                                                        </div>
                                                        <div class="action">
                                                            <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                            <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="minicart-item">
                                                        <div class="thumb">
                                                            <a href="#"><img src="assets/images/minicart/pr-05.jpg" width="90" height="90" alt="National Fresh"></a>
                                                        </div>
                                                        <div class="left-info">
                                                            <div class="product-title"><a href="#" class="product-name">National Fresh Fruit</a></div>
                                                            <div class="price">
                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                            </div>
                                                            <div class="qty">
                                                                <label for="cart[id127][qty]">Qty:</label>
                                                                <input type="number" class="input-qty" name="cart[id127][qty]" id="cart[id127][qty]" value="1" disabled>
                                                            </div>
                                                        </div>
                                                        <div class="action">
                                                            <a href="#" class="edit"><i class="fa fa-pencil" aria-hidden="true"></i></a>
                                                            <a href="#" class="remove"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                            <p class="btn-control">
                                                <a href="#" class="btn view-cart">view cart</a>
                                                <a href="#" class="btn">checkout</a>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="mobile-menu-toggle">
                                <a class="btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                                    <span></span>
                                    <span></span>
                                    <span></span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="header-bottom hidden-sm hidden-xs">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-4">
                        <div class="vertical-menu vertical-category-block">
                            <div class="block-title">
                                <span class="menu-icon">
                                    <span class="line-1"></span>
                                    <span class="line-2"></span>
                                    <span class="line-3"></span>
                                </span>
                                <span class="menu-title">All departments</span>
                                <span class="angle" data-tgleclass="fa fa-caret-down"><i class="fa fa-caret-up" aria-hidden="true"></i></span>
                            </div>
                            <div class="wrap-menu">
                                <ul class="menu clone-main-menu">
                                    <li class="menu-item menu-item-has-children has-megamenu">
                                        <a href="#" class="menu-name" data-title="Fruit & Nut Gifts"><i class="biolife-icon icon-fruits"></i>Fruit & Nut Gifts</a>
                                        <div class="wrap-megamenu lg-width-900 md-width-640">
                                            <div class="mega-content">
                                                <div class="row">
                                                    <div class="col-lg-3 col-md-4 col-sm-12 xs-margin-bottom-25 md-margin-bottom-0">
                                                        <div class="wrap-custom-menu vertical-menu">
                                                            <h4 class="menu-title">Fresh Fuits</h4>
                                                            <ul class="menu">
                                                                <li><a href="#">Fruit & Nut Gifts</a></li>
                                                                <li><a href="#">Mixed Fruits</a></li>
                                                                <li><a href="#">Oranges</a></li>
                                                                <li><a href="#">Bananas & Plantains</a></li>
                                                                <li><a href="#">Fresh Gala Apples</a></li>
                                                                <li><a href="#">Berries</a></li>
                                                                <li><a href="#">Pears</a></li>
                                                                <li><a href="#">Produce</a></li>
                                                                <li><a href="#">Snack Foods</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-3 col-md-4 col-sm-12 lg-padding-left-23 xs-margin-bottom-25 md-margin-bottom-0">
                                                        <div class="wrap-custom-menu vertical-menu">
                                                            <h4 class="menu-title">Nut Gifts</h4>
                                                            <ul class="menu">
                                                                <li><a href="#">Non-Dairy Coffee Creamers</a></li>
                                                                <li><a href="#">Coffee Creamers</a></li>
                                                                <li><a href="#">Mayonnaise</a></li>
                                                                <li><a href="#">Almond Milk</a></li>
                                                                <li><a href="#">Ghee</a></li>
                                                                <li><a href="#">Beverages</a></li>
                                                                <li><a href="#">Ranch Salad Dressings</a></li>
                                                                <li><a href="#">Hemp Milk</a></li>
                                                                <li><a href="#">Nuts & Seeds</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-6 col-md-4 col-sm-12 lg-padding-left-50 xs-margin-bottom-25 md-margin-bottom-0">
                                                        <div class="biolife-products-block max-width-270">
                                                            <h4 class="menu-title">Bestseller Products</h4>
                                                            <ul class="products-list default-product-style biolife-carousel nav-none-after-1k2 nav-center" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":30,"slidesToShow":1, "responsive":[{"breakpoint":767, "settings":{ "arrows": false}}]}' >
                                                                <li class="product-item">
                                                                    <div class="contain-product none-overlay">
                                                                        <div class="product-thumb">
                                                                            <a href="#" class="link-to-product">
                                                                                <img src="assets/images/products/p-08.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                                                            </a>
                                                                        </div>
                                                                        <div class="info">
                                                                            <b class="categories">Fresh Fruit</b>
                                                                            <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                                                            <div class="price">
                                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </li>
                                                                <li class="product-item">
                                                                    <div class="contain-product none-overlay">
                                                                        <div class="product-thumb">
                                                                            <a href="#" class="link-to-product">
                                                                                <img src="assets/images/products/p-11.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                                                            </a>
                                                                        </div>
                                                                        <div class="info">
                                                                            <b class="categories">Fresh Fruit</b>
                                                                            <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                                                            <div class="price">
                                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </li>
                                                                <li class="product-item">
                                                                    <div class="contain-product none-overlay">
                                                                        <div class="product-thumb">
                                                                            <a href="#" class="link-to-product">
                                                                                <img src="assets/images/products/p-15.jpg" alt="dd" width="270" height="270" class="product-thumnail">
                                                                            </a>
                                                                        </div>
                                                                        <div class="info">
                                                                            <b class="categories">Fresh Fruit</b>
                                                                            <h4 class="product-title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                                                                            <div class="price">
                                                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-lg-12 col-md-12 col-sm-12 md-margin-top-9">
                                                        <div class="biolife-brand" >
                                                            <ul class="brands">
                                                                <li><a href="#"><img src="assets/images/megamenu/brand-organic.png" width="161" height="136" alt="organic"></a></li>
                                                                <li><a href="#"><img src="assets/images/megamenu/brand-explore.png" width="160" height="136" alt="explore"></a></li>
                                                                <li><a href="#"><img src="assets/images/megamenu/brand-organic-2.png" width="99" height="136" alt="organic 2"></a></li>
                                                                <li><a href="#"><img src="assets/images/megamenu/brand-eco-teas.png" width="164"  height="136" alt="eco teas"></a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="menu-item menu-item-has-children has-megamenu">
                                        <a href="#" class="menu-name" data-title="Vegetables"><i class="biolife-icon icon-broccoli-1"></i>Vegetables</a>
                                        <div class="wrap-megamenu lg-width-900 md-width-640 background-mega-01">
                                            <div class="mega-content">
                                                <div class="row">
                                                    <div class="col-lg-3 col-md-4 col-sm-12 xs-margin-bottom-25 md-margin-bottom-0">
                                                        <div class="wrap-custom-menu vertical-menu">
                                                            <h4 class="menu-title">Vegetables</h4>
                                                            <ul class="menu">
                                                                <li><a href="#">Fruit & Nut Gifts</a></li>
                                                                <li><a href="#">Mixed Fruits</a></li>
                                                                <li><a href="#">Oranges</a></li>
                                                                <li><a href="#">Bananas & Plantains</a></li>
                                                                <li><a href="#">Fresh Gala Apples</a></li>
                                                                <li><a href="#">Berries</a></li>
                                                                <li><a href="#">Pears</a></li>
                                                                <li><a href="#">Produce</a></li>
                                                                <li><a href="#">Snack Foods</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-4 col-md-4 col-sm-12 lg-padding-left-23 xs-margin-bottom-25 md-margin-bottom-0">
                                                        <div class="wrap-custom-menu vertical-menu">
                                                            <h4 class="menu-title">Gifts</h4>
                                                            <ul class="menu">
                                                                <li><a href="#">Non-Dairy Coffee Creamers</a></li>
                                                                <li><a href="#">Coffee Creamers</a></li>
                                                                <li><a href="#">Mayonnaise</a></li>
                                                                <li><a href="#">Almond Milk</a></li>
                                                                <li><a href="#">Ghee</a></li>
                                                                <li><a href="#">Beverages</a></li>
                                                                <li><a href="#">Ranch Salad Dressings</a></li>
                                                                <li><a href="#">Hemp Milk</a></li>
                                                                <li><a href="#">Nuts & Seeds</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-5 col-md-4 col-sm-12 lg-padding-left-57 md-margin-bottom-30">
                                                        <div class="biolife-brand vertical md-boder-left-30">
                                                            <h4 class="menu-title">Hot Brand</h4>
                                                            <ul class="brands">
                                                                <li><a href="#"><img src="assets/images/megamenu/v-brand-organic.png" width="167" height="74" alt="organic"></a></li>
                                                                <li><a href="#"><img src="assets/images/megamenu/v-brand-explore.png" width="167" height="72" alt="explore"></a></li>
                                                                <li><a href="#"><img src="assets/images/megamenu/v-brand-organic-2.png" width="167" height="99" alt="organic 2"></a></li>
                                                                <li><a href="#"><img src="assets/images/megamenu/v-brand-eco-teas.png" width="167" height="67" alt="eco teas"></a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="menu-item menu-item-has-children has-megamenu">
                                        <a href="#" class="menu-name" data-title="Fresh Berries"><i class="biolife-icon icon-grape"></i>Fresh Berries</a>
                                        <div class="wrap-megamenu lg-width-900 md-width-640 background-mega-02">
                                            <div class="mega-content">
                                                <div class="row">
                                                    <div class="col-lg-3 col-md-4 sm-col-12 md-margin-bottom-83 xs-margin-bottom-25">
                                                        <div class="wrap-custom-menu vertical-menu">
                                                            <h4 class="menu-title">Fresh Berries</h4>
                                                            <ul class="menu">
                                                                <li><a href="#">Fruit & Nut Gifts</a></li>
                                                                <li><a href="#">Mixed Fruits</a></li>
                                                                <li><a href="#">Oranges</a></li>
                                                                <li><a href="#">Bananas & Plantains</a></li>
                                                                <li><a href="#">Fresh Gala Apples</a></li>
                                                                <li><a href="#">Berries</a></li>
                                                                <li><a href="#">Pears</a></li>
                                                                <li><a href="#">Produce</a></li>
                                                                <li><a href="#">Snack Foods</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-3 col-md-4 sm-col-12 lg-padding-left-23 xs-margin-bottom-36px md-margin-bottom-0">
                                                        <div class="wrap-custom-menu vertical-menu">
                                                            <h4 class="menu-title">Gifts</h4>
                                                            <ul class="menu">
                                                                <li><a href="#">Non-Dairy Coffee Creamers</a></li>
                                                                <li><a href="#">Coffee Creamers</a></li>
                                                                <li><a href="#">Mayonnaise</a></li>
                                                                <li><a href="#">Almond Milk</a></li>
                                                                <li><a href="#">Ghee</a></li>
                                                                <li><a href="#">Beverages</a></li>
                                                                <li><a href="#">Ranch Salad Dressings</a></li>
                                                                <li><a href="#">Hemp Milk</a></li>
                                                                <li><a href="#">Nuts & Seeds</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-6 col-md-4 sm-col-12 lg-padding-left-25 md-padding-top-55">
                                                        <div class="biolife-banner layout-01">
                                                            <h3 class="top-title">Farm Fresh</h3>
                                                            <p class="content"> All the Lorem Ipsum generators on the Internet tend.</p>
                                                            <b class="bottomm-title">Berries Series</b>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="menu-item"><a href="#" class="menu-name" data-title="Ocean Foods"><i class="biolife-icon icon-fish"></i>Ocean Foods</a></li>
                                    <li class="menu-item menu-item-has-children has-child">
                                        <a href="#" class="menu-name" data-title="Butter & Eggs"><i class="biolife-icon icon-honey"></i>Butter & Eggs</a>
                                        <ul class="sub-menu">
                                            <li class="menu-item"><a href="#">Omelettes</a></li>
                                            <li class="menu-item"><a href="#">Breakfast Scrambles</a></li>
                                            <li class="menu-item menu-item-has-children has-child"><a href="#" class="menu-name" data-title="Eggs & other considerations">Eggs & other considerations</a>
                                                <ul class="sub-menu">
                                                    <li class="menu-item"><a href="#">Classic Breakfast</a></li>
                                                    <li class="menu-item"><a href="#">Huevos Rancheros</a></li>
                                                    <li class="menu-item"><a href="#">Everything Egg Sandwich</a></li>
                                                    <li class="menu-item"><a href="#">Egg Sandwich</a></li>
                                                    <li class="menu-item"><a href="#">Vegan Burrito</a></li>
                                                    <li class="menu-item"><a href="#">Biscuits and Gravy</a></li>
                                                    <li class="menu-item"><a href="#">Bacon Avo Egg Sandwich</a></li>
                                                </ul>
                                            </li>
                                            <li class="menu-item"><a href="#">Griddle</a></li>
                                            <li class="menu-item menu-item-has-children has-child"><a href="#" class="menu-name" data-title="Sides & Extras">Sides & Extras</a>
                                                <ul class="sub-menu">
                                                    <li class="menu-item"><a href="#">Breakfast Burrito</a></li>
                                                    <li class="menu-item"><a href="#">Crab Cake Benedict</a></li>
                                                    <li class="menu-item"><a href="#">Corned Beef Hash</a></li>
                                                    <li class="menu-item"><a href="#">Steak & Eggs</a></li>
                                                    <li class="menu-item"><a href="#">Oatmeal</a></li>
                                                    <li class="menu-item"><a href="#">Fruit & Yogurt Parfait</a></li>
                                                </ul>
                                            </li>
                                            <li class="menu-item"><a href="#">Biscuits</a></li>
                                            <li class="menu-item"><a href="#">Seasonal Fruit Plate</a></li>
                                        </ul>
                                    </li>
                                    <li class="menu-item"><a href="#" class="menu-title"><i class="biolife-icon icon-fast-food"></i>Fastfood</a></li>
                                    <li class="menu-item"><a href="#" class="menu-title"><i class="biolife-icon icon-beef"></i>Fresh Meat</a></li>
                                    <li class="menu-item"><a href="#" class="menu-title"><i class="biolife-icon icon-onions"></i>Fresh Onion</a></li>
                                    <li class="menu-item"><a href="#" class="menu-title"><i class="biolife-icon icon-avocado"></i>Papaya & Crisps</a></li>
                                    <li class="menu-item"><a href="#" class="menu-title"><i class="biolife-icon icon-contain"></i>Oatmeal</a></li>
                                    <li class="menu-item"><a href="#" class="menu-title"><i class="biolife-icon icon-fresh-juice"></i>Fresh Bananas & Plantains</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-9 col-md-8 padding-top-2px">
                        <div class="header-search-bar layout-01">
                            <form action="#" class="form-search" name="desktop-seacrh" method="get">
                                <input type="text" name="s" class="input-text" value="" placeholder="Search here...">
                                <select name="category">
                                    <option value="-1" selected>All Categories</option>
                                    <option value="vegetables">Vegetables</option>
                                    <option value="fresh_berries">Fresh Berries</option>
                                    <option value="ocean_foods">Ocean Foods</option>
                                    <option value="butter_eggs">Butter & Eggs</option>
                                    <option value="fastfood">Fastfood</option>
                                    <option value="fresh_meat">Fresh Meat</option>
                                    <option value="fresh_onion">Fresh Onion</option>
                                    <option value="papaya_crisps">Papaya & Crisps</option>
                                    <option value="oatmeal">Oatmeal</option>
                                </select>
                                <button type="submit" class="btn-submit"><i class="biolife-icon icon-search"></i></button>
                            </form>
                        </div>
                        <div class="live-info">
                            <p class="telephone"><i class="fa fa-phone" aria-hidden="true"></i><b class="phone-number">(+900) 123 456 7891</b></p>
                            <p class="working-time">Mon-Fri: 8:30am-7:30pm; Sat-Sun: 9:30am-4:30pm</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">Organic Fruits</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="index-2.html" class="permal-link">Home</a></li>
                <li class="nav-item"><span class="current-page">ShoppingCart</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain checkout">

        <!-- Main content -->
        <div id="main-content" class="main-content">
            <div class="container sm-margin-top-37px">
                <div class="row">

                    <!--checkout progress box-->
                    <div class="col-lg-7 col-md-7 col-sm-6 col-xs-12">
                        <div class="checkout-progress-wrap">
                            <ul class="steps">
                                <li class="step 1st">
                                    <div class="checkout-act active">
                                        <h3 class="title-box"><span class="number">1</span>Customer</h3>
                                        <div class="box-content">
                                            <p class="txt-desc">Checking out as a <a class="pmlink" href="#">Guest?</a> You’ll be able to save your details to create an account with us later.</p>
                                            <div class="login-on-checkout">
                                                <form action="#" name="frm-login" method="post">
                                                    <p class="form-row">
                                                        <label for="input_email">Email Address</label>
                                                        <input type="email" name="email" id="input_email" value="" placeholder="Your email">
                                                        <button type="submit" name="btn-sbmt" class="btn">Continue As Guest</button>
                                                    </p>
                                                    <p class="form-row">
                                                        <input type="checkbox" name="subcribe" id="input_subcribe" >
                                                        <label for="input_subcribe">Subscribe to our newsletter</label>
                                                    </p>
                                                    <p class="msg">Already have an account? <a href="#" class="link-forward">Sign in now</a></p>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="step 2nd">
                                    <div class="checkout-act"><h3 class="title-box"><span class="number">2</span>Shipping</h3></div>
                                </li>
                                <li class="step 3rd">
                                    <div class="checkout-act"><h3 class="title-box"><span class="number">3</span>Billing</h3></div>
                                </li>
                                <li class="step 4th">
                                    <div class="checkout-act"><h3 class="title-box"><span class="number">4</span>Payment</h3></div>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <!--Order Summary-->
                    <div class="col-lg-5 col-md-5 col-sm-6 col-xs-12 sm-padding-top-48px sm-margin-bottom-0 xs-margin-bottom-15px">
                        <div class="order-summary sm-margin-bottom-80px">
                            <div class="title-block">
                                <h3 class="title">Order Summary</h3>
                                <a href="#" class="link-forward">Edit cart</a>
                            </div>
                            <div class="cart-list-box short-type">
                                <span class="number">2 items</span>
                                <ul class="cart-list">
                                    <li class="cart-elem">
                                        <div class="cart-item">
                                            <div class="product-thumb">
                                                <a class="prd-thumb" href="#">
                                                    <figure><img src="assets/images/shippingcart/pr-01.jpg" width="113" height="113" alt="shop-cart" ></figure>
                                                </a>
                                            </div>
                                            <div class="info">
                                                <span class="txt-quantity">1X</span>
                                                <a href="#" class="pr-name">National Fresh Fruit</a>
                                            </div>
                                            <div class="price price-contain">
                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="cart-elem">
                                        <div class="cart-item">
                                            <div class="product-thumb">
                                                <a class="prd-thumb" href="#">
                                                    <figure><img src="assets/images/shippingcart/pr-02.jpg" width="113" height="113" alt="shop-cart" ></figure>
                                                </a>
                                            </div>
                                            <div class="info">
                                                <span class="txt-quantity">1X</span>
                                                <a href="#" class="pr-name">National Fresh Fruit</a>
                                            </div>
                                            <div class="price price-contain">
                                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                                <ul class="subtotal">
                                    <li>
                                        <div class="subtotal-line">
                                            <b class="stt-name">Subtotal</b>
                                            <span class="stt-price">£170.00</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="subtotal-line">
                                            <b class="stt-name">Shipping</b>
                                            <span class="stt-price">£20.00</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="subtotal-line">
                                            <b class="stt-name">Tax</b>
                                            <span class="stt-price">£0.00</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="subtotal-line">
                                            <a href="#" class="link-forward">Promo/Gift Certificate</a>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="subtotal-line">
                                            <b class="stt-name">total:</b>
                                            <span class="stt-price">£190.00</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <footer id="footer" class="footer layout-03">
        <div class="footer-content background-footer-03">
            <div class="container">
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-sm-9">
                        <section class="footer-item">
                            <a href="#" class="logo footer-logo"><img src="assets/images/organic-3.png" alt="biolife logo" width="135" height="34"></a>
                            <div class="footer-phone-info">
                                <i class="biolife-icon icon-head-phone"></i>
                                <p class="r-info">
                                    <span>Got Questions ?</span>
                                    <span>(700)  9001-1909  (900) 689 -66</span>
                                </p>
                            </div>
                            <div class="newsletter-block layout-01">
                                <h4 class="title">Newsletter Signup</h4>
                                <div class="form-content">
                                    <form action="#" name="new-letter-foter">
                                        <input type="email" class="input-text email" value="" placeholder="Your email here...">
                                        <button type="submit" class="bnt-submit" name="ok">Sign up</button>
                                    </form>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Useful Links</h3>
                            <div class="row">
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="#">About Us</a></li>
                                            <li><a href="#">About Our Shop</a></li>
                                            <li><a href="#">Secure Shopping</a></li>
                                            <li><a href="#">Delivery infomation</a></li>
                                            <li><a href="#">Privacy Policy</a></li>
                                            <li><a href="#">Our Sitemap</a></li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-lg-6 col-sm-6 col-xs-6">
                                    <div class="wrap-custom-menu vertical-menu-2">
                                        <ul class="menu">
                                            <li><a href="#">Who We Are</a></li>
                                            <li><a href="#">Our Services</a></li>
                                            <li><a href="#">Projects</a></li>
                                            <li><a href="#">Contacts Us</a></li>
                                            <li><a href="#">Innovation</a></li>
                                            <li><a href="#">Testimonials</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6 md-margin-top-5px sm-margin-top-50px xs-margin-top-40px">
                        <section class="footer-item">
                            <h3 class="section-title">Transport Offices</h3>
                            <div class="contact-info-block footer-layout xs-padding-top-10px">
                                <ul class="contact-lines">
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-location"></i>
                                            <b class="desc">7563 St. Vicent Place, Glasgow, Greater Newyork NH7689, UK </b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-phone"></i>
                                            <b class="desc">Phone: (+067) 234 789  (+068) 222 888</b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-letter"></i>
                                            <b class="desc">Email:  contact@company.com</b>
                                        </p>
                                    </li>
                                    <li>
                                        <p class="info-item">
                                            <i class="biolife-icon icon-clock"></i>
                                            <b class="desc">Hours: 7 Days a week from 10:00 am</b>
                                        </p>
                                    </li>
                                </ul>
                            </div>
                            <div class="biolife-social inline">
                                <ul class="socials">
                                    <li><a href="#" title="twitter" class="socail-btn"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="facebook" class="socail-btn"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="pinterest" class="socail-btn"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="youtube" class="socail-btn"><i class="fa fa-youtube" aria-hidden="true"></i></a></li>
                                    <li><a href="#" title="instagram" class="socail-btn"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                </ul>
                            </div>
                        </section>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="separator sm-margin-top-70px xs-margin-top-40px"></div>
                    </div>
                    <div class="col-lg-6 col-sm-6 col-xs-12">
                       <div class="copy-right-text"><p><a href="templateshub.net">Templates Hub</a></p></div>
                    </div>
                    <div class="col-lg-6 col-sm-6 col-xs-12">
                        <div class="payment-methods">
                            <ul>
                                <li><a href="#" class="payment-link"><img src="assets/images/card1.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card2.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card3.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card4.jpg" width="51" height="36" alt=""></a></li>
                                <li><a href="#" class="payment-link"><img src="assets/images/card5.jpg" width="51" height="36" alt=""></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!--Footer For Mobile-->
    <div class="mobile-footer">
        <div class="mobile-footer-inner">
            <div class="mobile-block block-menu-main">
                <a class="menu-bar menu-toggle btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                    <span class="fa fa-bars"></span>
                    <span class="text">Menu</span>
                </a>
            </div>
            <div class="mobile-block block-sidebar">
                <a class="menu-bar filter-toggle btn-toggle" data-object="open-mobile-filter" href="javascript:void(0)">
                    <i class="fa fa-sliders" aria-hidden="true"></i>
                    <span class="text">Sidebar</span>
                </a>
            </div>
            <div class="mobile-block block-minicart">
                <a class="link-to-cart" href="#">
                    <span class="fa fa-shopping-bag" aria-hidden="true"></span>
                    <span class="text">Cart</span>
                </a>
            </div>
            <div class="mobile-block block-global">
                <a class="menu-bar myaccount-toggle btn-toggle" data-object="global-panel-opened" href="javascript:void(0)">
                    <span class="fa fa-globe"></span>
                    <span class="text">Global</span>
                </a>
            </div>
        </div>
    </div>

    <div class="mobile-block-global">
        <div class="biolife-mobile-panels">
            <span class="biolife-current-panel-title">Global</span>
            <a class="biolife-close-btn" data-object="global-panel-opened" href="#">&times;</a>
        </div>
        <div class="block-global-contain">
            <div class="glb-item my-account">
                <b class="title">My Account</b>
                <ul class="list">
                    <li class="list-item"><a href="#">Login/register</a></li>
                    <li class="list-item"><a href="#">Wishlist <span class="index">(8)</span></a></li>
                    <li class="list-item"><a href="#">Checkout</a></li>
                </ul>
            </div>
            <div class="glb-item currency">
                <b class="title">Currency</b>
                <ul class="list">
                    <li class="list-item"><a href="#">€ EUR (Euro)</a></li>
                    <li class="list-item"><a href="#">$ USD (Dollar)</a></li>
                    <li class="list-item"><a href="#">£ GBP (Pound)</a></li>
                    <li class="list-item"><a href="#">¥ JPY (Yen)</a></li>
                </ul>
            </div>
            <div class="glb-item languages">
                <b class="title">Language</b>
                <ul class="list inline">
                    <li class="list-item"><a href="#"><img src="assets/images/languages/us.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/fr.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/ger.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/jap.jpg" alt="flag" width="24" height="18"></a></li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
</body>

</html>
