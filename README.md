# **Shopping Cart Application**

This is a shopping cart application developed in Java using the Spring Boot framework. It implements a REST API that allows users to manage products, categories, customers, and orders (packets) while maintaining stock updates, order validation, and cancellation rules.

[Table of Contents]()

[Project Overview]()

[Technologies Used]()

[Features]()

[Project Structure]()

[Getting Started]()

[API Endpoints]()

[Running the Application]()

[Running Tests]()

## Project Overview
The shopping cart system supports basic e-commerce functionality, including viewing products, adding them to a shopping cart, validating and canceling orders, and tracking order statuses. Each order (or packet) can be confirmed, canceled within a specified time, and updated to reflect stock changes.

### Key Components:
1. **Products**: Items that customers can view and add to their cart.
2. **Packets**: Represents an order placed by a customer; packets track order status, validation, and cancellation.
3. **Customers**: Users who interact with the application to place orders.
4. **Categories**: Product categories, such as "Shirts" or "Pants."


### **Core Entities**

**Product**

Available Stock: Represents the actual stock available for customers to purchase. This value decreases as products are confirmed in orders and increases if orders are canceled.

Temporary Stock: Used to hold product units during the pre-confirmation phase (when items are in the cart but not yet validated). Temporary stock allows other customers to still view available units while maintaining stock consistency during checkout.

**Packet**

Represents an order placed by the customer. It contains a list of products added to the cart and the quantity of each product. Each packet has a status (e.g., IN_PROGRESS, CONFIRMED, CANCELED) that indicates its lifecycle state.

### Technologies Used

Java (JDK 21)
Spring Boot (3.3.5)
Spring Data JPA for database interaction
JUnit 6 and Mockito for unit testing
Maven for project build and dependency management

### Project Structure

**entities:** Defines core entities, including Product, Category, Packet, and PacketItem.

**services:** Contains business logic for handling product stock updates, cart modifications, and order validation.

**repositories:** Provides the data access layer with Spring Data JPA repositories.

**controllers:** Contains REST APIs for handling requests from the frontend.

## Getting Started

### Prerequisites

Java (JDK 21+)

Maven (for dependency management)

Database: This project can be configured to use an in-memory H2 database or an external database (e.g., MySQL).

### Installation

1. Clone the repository:


`git clone https://github.com/yourusername/shopping-cart-application.git`

`cd shopping-cart-application`

2. Configure the database :

* By default, the project is configured to use an H2 in-memory database.

* For a persistent database, update src/main/resources/application.yml with your database configuration.

### Build the project

`mvn clean install`

## API Endpoints
1. Products

   GET /api/products - Get all available products

   POST /api/products - Add a new product

   PUT /api/products/{id} - Update product information

   DELETE /api/products/{id} - Delete a product

2. Categories

   GET /api/categories - Get all product categories

   POST /api/categories - Create a new category

3. Customers

   GET /api/customers - Get all customers

   POST /api/customers - Create a new customer

4. Packets (Orders)

   GET /api/packets - Get all packets

   POST /api/packets - Create a new packet

   POST /api/packets/{id}/validate - Validate a packet

   POST /api/packets/{id}/cancel - Cancel a packet within the time limit

   GET /api/packets/{id}/status - Get the current status of a packet

   GET /api/packets/customer/{customerId} - Get all packets for a customer

## Testing the Shopping Cart Application

### 1. Testing Through the Main Application

   When you run the main application, it will initialize data, simulate packet (order) operations, and log key results. This is useful for seeing how different business operations behave end-to-end and for validating data persistence directly in the database.

**Steps**

1. Run the Application: Use the following command to run the main application:

`mvn spring-boot:run`

This command will:

* Initialize a sample customer, categories, and products.
* Simulate packet operations (adding products, confirming orders, canceling orders).
* Log results, including stock adjustments and packet status updates.

2. Check the Database:

* Ensure that the database (either local or in-memory) is configured in your application.properties.
* After running the main application, use a database tool (e.g., DBeaver, H2 console) to verify that data for customers, packets, and products has been saved correctly.

### 2. Unit Tests

Unit tests validate the functionality of individual components in isolation, often with mocked dependencies to focus on logic rather than persistence or external interactions. For this, JUnit and Mockito can be used.

#### Running Unit Tests

Use the following command to execute unit tests:

`mvn test`

#### Key Unit Tests

* **ProductService:**

Test stock update methods to ensure available and temporary stocks are adjusted as expected.

Mock ProductRepository and test addProductToShoppingCart and removeProductFromShoppingCart methods.

* **PacketService:**

Test packet status changes (e.g., IN_PROGRESS to CONFIRMED, or to CANCELED within the allowed time).

Mock PacketRepository and validate validatePacket, cancelPacket, and getPacketsByCustomerId.

* **PacketController:**

Test controller endpoints with mock services to ensure responses match expected results and that the application returns appropriate status codes (e.g., 200 OK, 400 Bad Request).