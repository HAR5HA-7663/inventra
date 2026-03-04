# Inventra – Inventory Management REST API

A production-ready RESTful API for managing products and inventory built with **Spring Boot 3**, **Spring Data JPA**, and **PostgreSQL**. Follows clean layered architecture with full CRUD operations, pagination, search filtering, and automated stock status tracking.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.5 |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL (prod), H2 (dev/test) |
| Validation | Jakarta Validation (Bean Validation 3.0) |
| Documentation | OpenAPI 3 / Swagger UI (springdoc) |
| Testing | JUnit 5, Mockito |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |

## Features

- Full CRUD for **Products** and **Categories**
- **Pagination & sorting** on all list endpoints
- **Search filtering** by product name and category
- Automatic **stock status** (`IN_STOCK` / `LOW_STOCK` / `OUT_OF_STOCK`) computed per response
- **Duplicate detection** for SKUs and category names
- **Global exception handling** via `@RestControllerAdvice` with structured error responses
- **Jakarta Validation** on all request DTOs
- Interactive **Swagger UI** at `/swagger-ui.html`
- H2 **console** available at `/h2-console` in dev mode
- **Docker Compose** setup with PostgreSQL for local production simulation

## Architecture

```
src/main/java/com/inventra/
├── controller/     # REST endpoints (@RestController)
├── service/        # Business logic (@Service, @Transactional)
├── repository/     # Data access (JpaRepository)
├── model/          # JPA entities (@Entity)
├── dto/            # Request/Response records
├── exception/      # Custom exceptions + @RestControllerAdvice
└── config/         # OpenAPI configuration
```

## API Endpoints

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | List products (paginated, filterable) |
| `GET` | `/api/products/{id}` | Get product by ID |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Delete product |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | List all categories |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `POST` | `/api/categories` | Create category |
| `PUT` | `/api/categories/{id}` | Update category |
| `DELETE` | `/api/categories/{id}` | Delete category |

### Query Parameters (Products)
| Param | Default | Description |
|-------|---------|-------------|
| `name` | — | Partial match search |
| `categoryId` | — | Filter by category |
| `page` | `0` | Page number |
| `size` | `20` | Page size |
| `sortBy` | `id` | Sort field |
| `sortDir` | `asc` | Sort direction |

## Getting Started

### Run with H2 (dev mode – no setup needed)

```bash
./mvnw spring-boot:run
```

- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:inventradb`)

Sample data (4 categories, 7 products) is loaded automatically.

### Run with PostgreSQL (Docker Compose)

```bash
# Build the jar first
./mvnw clean package -DskipTests

# Start app + PostgreSQL
docker-compose up --build
```

### Run Tests

```bash
./mvnw test
```

## Example Requests

**Create a product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mechanical Keyboard",
    "sku": "ELEC-MK-001",
    "description": "Tactile mechanical keyboard with RGB backlight",
    "price": 89.99,
    "quantity": 45,
    "categoryId": 1
  }'
```

**Search products (paginated, filtered):**
```bash
curl "http://localhost:8080/api/products?name=keyboard&categoryId=1&page=0&size=10&sortBy=price&sortDir=asc"
```

**Response format:**
```json
{
  "id": 1,
  "name": "Mechanical Keyboard",
  "sku": "ELEC-MK-001",
  "description": "Tactile mechanical keyboard with RGB backlight",
  "price": 89.99,
  "quantity": 45,
  "status": "IN_STOCK",
  "categoryId": 1,
  "categoryName": "Electronics",
  "createdAt": "2026-03-03T10:00:00",
  "updatedAt": "2026-03-03T10:00:00"
}
```

**Error response:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 99",
  "details": [],
  "timestamp": "2026-03-03T10:00:00"
}
```

## License

MIT
