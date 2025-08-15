# online-shopping-platform

## 🗂️ Project Structure
```
online-shopping-platform/
├── eureka-server/                    # Eureka Service Registry
│   ├── src/
│   └── pom.xml
│
├── api-gateway/                      # API Gateway with route forwarding
│   ├── src/
│   └── pom.xml
│
├── account-service/                 # Handles user accounts (MySQL)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── item-service/                    # Product info & inventory (MongoDB + Kafka)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── order-service/                   # Order management (Cassandra + Kafka)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── payment-service/                 # Payment processing (MySQL + Kafka)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── common-kafka-lib/               # Shared DTOs, configs
│   ├── src/
│   └── pom.xml
│
├── common-security-lib/            # Shared security configs
│   ├── src/
│   └── pom.xml
│
├── docker/
│   ├── docker-compose.yml           # For running all services + DBs + Kafka
│   └── init-scripts/                # Init scripts
│
├── README.md
└── pom.xml                          # Parent pom (Spring Boot multi-module)
```

## 👤 Account Service
### 📌 Responsibilities
* Manage user accounts
* Handle authentication + token issuing (JWT)
* Store user profiles securely

### ⠀📘 Key APIs
* POST /accounts/register → Create account
* POST /accounts/login → Login, get JWT token
* GET /accounts/{userId} → Lookup
* PUT /accounts/{userId} → Update account

### ⠀🧾 MySQL Table: users

| **Field**        | **Type** | **Notes**         |
|:----------------:|:--------:|:-----------------:|
| user_id          | UUID     | Primary Key       |
| email            | VARCHAR  | Unique            |
| username         | VARCHAR  |                   |
| password_hash    | VARCHAR  | Hashed via BCrypt |
| shipping_address | VARCHAR  |                   |
| billing_address  | VARCHAR  |                   |
| payment_method   | VARCHAR  |                   |

## 🛍️ Item Service
### 📌 **Responsibilities**
* Provide item metadata (name, image URLs, UPC, price, etc.)
* Inventory lookup and update
* Read-heavy, dynamic schema → **MongoDB**

### 📘 **Key APIs**
* GET /items/{itemId} → Fetch item details
* GET /items → List/search items
* GET /inventory/{itemId} → Available stock
* PUT /inventory/reserve → Reserve inventory
* PUT /inventory/release → Release inventory after cancel/refund
* PUT /inventory/confirm → Confirm inventory after payment
### 🧾 Sample MongoDB Schema
```
{
  "itemId": "UUID",
  "name": "AirPods Pro",
  "price": 249.99,
  "upc": "123456789012",
  "imageUrls": ["url1", "url2"],
  "inventory": {
    "available": 100,
    "reserved": 10
  }
}
```


## 🧾 Order Service
### 📌 Responsibilities
* Handle full order lifecycle: create, cancel, update
* Maintain **order state machine**
* Store orders in **Cassandra** for write scalability
* Expose sync APIs + emit events to Kafka
* Consume payment & inventory events

### ⠀📘 Key APIs
* POST /orders → Create order
* PUT /orders/{orderId} → Update order
* DELETE /orders/{orderId} → Cancel order
* GET /orders/{orderId} → Order lookup

### ⠀📈 Order States
* CREATED, PAID, COMPLETED, CANCELLED

### 🧾 Cassandra Table Design
**Table:** orders_by_id

| **Field**   | **Type**                      | **Notes**          |
|:-----------:|:-----------------------------:|:------------------:|
| order_id    | UUID                          | Primary Key        |
| user_id     | UUID                          | Partition key      |
| status      | TEXT                          | e.g. CREATED, PAID |
| items       | frozen<list<map<text, text>>> | Item metadata      |
| total_price | DECIMAL                       |                    |
| created_at  | TIMESTAMP                     |                    |
| updated_at  | TIMESTAMP                     |                    |

Use time-based UUIDs in clustering key for sorting by recency.

## 💳 Payment Service
### 📌 Responsibilities
* Process payments (credit card, etc.)
* Ensure **idempotency** for submit/refund
* Store payment records in **MySQL** for consistency
* Publish results to Kafka

### 📘 Key APIs
* POST /payments → Submit payment (with idempotency key)
* PUT /payments/{paymentId} → Update payment
* POST /payments/{paymentId}/refund → Reverse/refund
* GET /payments/{paymentId} → Payment lookup

### 🧾 MySQL Table: payments

|    **Field**    | **Type**  |             **Notes**              |
|:---------------:|:---------:|:----------------------------------:|
|   payment_id    |   UUID    |            Primary Key             |
|    order_id     |   UUID    |        Foreign key to order        |
|     user_id     |   UUID    |                                    |
|     amount      |  DECIMAL  |                                    |
|     status      |   ENUM    | PENDING, SUCCESS, FAILED, REFUNDED |
|   created_at    | TIMESTAMP |                                    |
| idempotency_key |  VARCHAR  |   Unique index for safe retries    |

## 🔐 Authentication & Security
* JWT tokens issued on login
* Token sent in Authorization: Bearer <token> for secured endpoints
* Use Spring Security for protecting APIs
* Role-based access: USER, ADMIN

## 📤 Kafka Topics Overview
| **Topic Name**     | **Publisher**   | **Consumer(s)**    |
|:------------------:|:---------------:|:------------------:|
| order_created      | Order Service   | Payment            |
| order_paid         | Order Service   | Inventory          |
| order_completed    | Order Service   | Inventory          |
| order_cancelled    | Order Service   | Inventory, Payment |
| payment_successful | Payment Service | Order Service      |
| payment_cancelled  | Payment Service | Order Service      |
| payment_refunded   | Payment Service | Order Service      |
| inventory_reserved | Item Service    | Order Service      |
| inventory_failed   | Item Service    | Order Service      |

⠀
