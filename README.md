# E-Commerce api service

This is a Spring Boot application using Java 25. It provides the E-Commerce API endpoints to manage customers, orders in an e-commerce system.

## Building the application

To build the application, use the below instructions:

```bash
./mvnw clean package

Running the Application Locally
To run the application locally, you can use the following command:

./mvnw spring-boot:run -Dspring.profiles.active=h2
This command will spin up an H2 in-memory database with some pre-loaded data. Ensure you have Java 17 installed on your machine before running this command.
swagger url: http://localhost:8080/swagger-ui/index.html

Recommended IDE
I recommend using IntelliJ IDEA as the IDE for this project. It provides excellent support for Spring Boot applications and Java development in general.

Contributing
I welcome contributions! If you find any issues or have suggestions for improvements, feel free to open an issue or submit a pull request.
