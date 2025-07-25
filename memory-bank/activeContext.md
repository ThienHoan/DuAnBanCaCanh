# Active Context

*Current work focus:*
- Creating project structure based on database schema
- Setting up initial architecture for the fish shop e-commerce platform
- Planning model implementation from existing database schema

*Recent changes:*
- Received database schema definition with comprehensive e-commerce tables
- Schema includes specialized tables for fish products (Product_details)
- Sample data has been provided for testing

*Next steps:*
- Create project folder structure
- Set up backend API project with entity models
- Create database context and repository classes
- Implement basic authentication
- Set up frontend project structure

*Active decisions and considerations:*
- How to organize the project for maintainability and scalability
- Best approach for handling fish-specific product attributes
- Integration strategy for payment and shipping services
- Media storage solution for product and review images

*Important patterns and preferences:*
- Using repository pattern for data access
- Implementing soft delete throughout the application
- Stateless authentication with JWT tokens
- Leveraging SQL Server-specific features from the schema

*Learnings and project insights:* 
- Database schema uses soft delete pattern extensively
- Schema includes specialized tables for ornamental fish data
- Complex relationship between products, categories, and attributes
- Multiple types of promotions and discounts are supported 