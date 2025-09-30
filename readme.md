# Ecommerce Microservices Project

A learning project demonstrating microservices architecture for an ecommerce application using Spring Boot and Spring Cloud.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Learning Outcomes](#learning-outcomes)
- [Future Enhancements](#future-enhancements)
- [References](#references)

## Overview

This project consists of four main components:

- **Product Service** – Manages product catalog and inventory
- **Order Service** – Handles order creation and management
- **Eureka Server** – Service registry for service discovery
- **API Gateway** – Central entry point for routing requests

## Architecture

```
                    ┌─────────────────┐
                    │   API Gateway   │
                    │ (Port: 8080)    │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
     ┌────────▼────────┐          ┌────────▼────────┐
     │ Product Service │          │  Order Service  │
     │  (Port: 8081)   │          │  (Port: 8082)   │
     └────────┬────────┘          └────────┬────────┘
              │                             │
              │      ┌─────────────┐        │
              └──────►   MySQL DB  ◄────────┘
                     │  productdb  │
                     │   orderdb   │
                     └─────────────┘
                             ▲
                             │
                    ┌────────┴────────┐
                    │  Eureka Server  │
                    │  (Port: 8761)   │
                    └─────────────────┘
```

### How It Works

1. **API Gateway** receives all client requests and routes them to appropriate services
2. **Eureka Server** maintains a registry of all available service instances
3. **Product Service** provides product information via REST API
4. **Order Service** creates orders and communicates with Product Service using WebClient
5. All services register themselves with Eureka for service discovery

## Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming Language |
| Spring Boot | 3.5.6 | Application Framework |
| Spring Cloud Gateway | Latest | API Gateway |
| Spring Cloud Netflix Eureka | Latest | Service Discovery |
| Spring Data JPA | Latest | Data Persistence |
| MySQL | Latest | Database |
| Maven | Latest | Build Tool |

## Prerequisites

Before running this project, ensure you have:

- **JDK 21** installed
- **Maven 3.6+** installed
- **MySQL 8.0+** running
- **Postman** (optional, for testing APIs)

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/Sivasothy-Tharsi/ecommerce-microservices.git
cd ecommerce-microservices
```

### Step 2: Configure MySQL

Create the required databases:

```sql
CREATE DATABASE IF NOT EXISTS productdb;
CREATE DATABASE IF NOT EXISTS orderdb;
```

Update `application.properties` in each service with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<database-name>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

### Step 3: Start Services

Start the services in the following order:

1. **Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
   Verify at: http://localhost:8761

2. **Product Service**
   ```bash
   cd product-service
   mvn spring-boot:run
   ```

3. **Order Service**
   ```bash
   cd order-service
   mvn spring-boot:run
   ```

4. **API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

### Step 4: Verify Setup

- Eureka Dashboard: http://localhost:8761
- Ensure all services are registered before testing

## API Endpoints

All requests go through the API Gateway at `http://localhost:8080`

### Product Service

#### Create Product
```http
POST http://localhost:8080/products
Content-Type: application/json

{
  "name": "Shoap",
  "price": 120
}
```

#### Get All Products
```http
GET http://localhost:8080/products
```

#### Get Product by ID
```http
GET http://localhost:8080/products/{id}
```

### Order Service

#### Get All Orders
```http
GET http://localhost:8080/orders
```

#### Create Order
```http
POST http://localhost:8080/orders
Content-Type: application/json

{
  "items": [
    {
      "productId": "1",
      "quantity": 2
    },
    {
      "productId": "2",
      "quantity": 1
    }
  ]
}
```

## Configuration

### API Gateway Configuration

Key configuration in `application.properties`:

```properties
spring.application.name=api-gateway
server.port=8080

# Eureka Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

# Route to Product Service
spring.cloud.gateway.routes[0].id=product-service
spring.cloud.gateway.routes[0].uri=lb://PRODUCT-SERVICE
spring.cloud.gateway.routes[0].predicates[0]=Path=/products/**

# Route to Order Service
spring.cloud.gateway.routes[1].id=order-service
spring.cloud.gateway.routes[1].uri=lb://ORDER-SERVICE
spring.cloud.gateway.routes[1].predicates[0]=Path=/orders/**
```

### Important Notes

- ⚠️ Ensure **no trailing spaces** in URLs when testing (avoid `%20` encoding)
- ⚠️ Services must be **registered in Eureka** before API Gateway can route requests
- ⚠️ Start services in the correct order for proper initialization
- ⚠️ Check Eureka dashboard to verify all services are UP

## Learning Outcomes

By working on this project, you will learn:

- ✅ **Microservices Architecture** – Understanding service decomposition and independence
- ✅ **Service Discovery** – Using Eureka for dynamic service registration
- ✅ **API Gateway Pattern** – Centralized routing and load balancing
- ✅ **Inter-Service Communication** – Using WebClient for synchronous calls
- ✅ **Spring Data JPA** – Entity relationships (Order → OrderItem)
- ✅ **Spring Cloud** – Integration of Spring Cloud components

## Future Enhancements

Consider these improvements to extend your learning:

- 🔐 **Security** – Add Spring Security with JWT authentication
- 📚 **Documentation** – Integrate Swagger/OpenAPI for API documentation
- 🛡️ **Resilience** – Implement circuit breakers with Resilience4j
- 🐳 **Containerization** – Deploy with Docker and Docker Compose
- 📊 **Monitoring** – Add Spring Boot Actuator and distributed tracing
- 🔄 **Event-Driven** – Implement asynchronous communication with message queues
- 💾 **Caching** – Add Redis for improved performance
- 🧪 **Testing** – Write unit and integration tests

## References

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Netflix Eureka](https://spring.io/projects/spring-cloud-netflix)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Microservices Pattern](https://microservices.io/)

---

## 🤝 Contributing

This is a learning project. Feel free to fork, experiment, and extend it for your own learning purposes.

## 📝 License

This project is open source and available for educational purposes.

---

**Happy Learning! 🚀**