# System Patterns

*System architecture:*
- N-tier architecture with presentation, business logic, and data access layers
- RESTful API design for backend services
- MVC pattern for web application structure
- Responsive design for multi-device support

*Key technical decisions:*
- SQL Server database with soft delete pattern for data integrity
- Authentication and authorization with role-based access control
- Image management and serving through optimized CDN
- Server-side rendering for primary pages with client-side rendering for dynamic components

*Design patterns in use:*
- Repository pattern for data access
- Factory pattern for creating complex objects
- Observer pattern for event handling (order status updates, inventory changes)
- Strategy pattern for payment and shipping methods
- Singleton pattern for service instances

*Component relationships:*
- User → Cart → Order → Payment/Shipping flow
- Product → Category hierarchical relationship
- Product → ProductDetails composition relationship
- Order → OrderItems composition relationship

*Critical implementation paths:* 
- User authentication and session management
- Product catalog and search functionality
- Shopping cart and checkout process
- Order processing and payment workflows
- Inventory management and stock control 