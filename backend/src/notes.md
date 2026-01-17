| Layer      | Responsibility  |
| ---------- | --------------- |
| Controller | HTTP / REST     |
| Service    | Business rules  |
| Repository | Database access |
| Entity     | Table mapping   |

1. Customer.java (Entity / Domain Model)

# Purpose

    - Represents the business object and database table
    - Holds data only, no business logic

# Role

    - Maps to a database table via JPA/Hibernate
    - Defines fields, relationships, and constraints

# Typical contents

    - Fields (id, name, email, company, etc.)
    - JPA annotations (@Entity, @Id, @ManyToOne)
    - Getters/setters

# Why it exists

    - Keeps your data model independent from web or business logic
    - Allows Hibernate/JPA to manage persistence

# Think of it as

<!-- - “What a Customer is” -->

2. CustomerService.java (Service Interface)

# Purpose

    - Defines what operations are allowed
    - Acts as a contract

# Role

    *Declares business actions without implementation
    *Decouples controllers from implementations

# Typical contents

    public interface CustomerService {
        List<Customer> getCustomers(Long companyId);
        Customer getCustomer(Long customerId, Long companyId);
        Customer createCustomer(Customer customer, Long companyId);
    }

# Why it exists

# Enables:

    - Multiple implementations
    - Easier testing (mocking)
    - Cleaner architecture

# Think of it as

<!-- - “What the system can do with Customers” -->

3. CustomerServiceImpl.java (Business Logic Layer)

# Purpose

    - Contains business rules
    - Orchestrates repositories and validations

# Role

    - Implements CustomerService
    - Handles:
    - Validation
    - Ownership checks
    - Transactions
    - Business constraints

# Typical contents

    - Calls CustomerRepository
    - Applies rules (e.g., customer belongs to company)
    - Throws domain-specific errors
    - Why it exists
    - Keeps business logic out of controllers
    - Prevents duplication
    - Centralizes rules

# Think of it as

<!-- - “How customer operations actually work” -->

4. CustomerController.java (Web / API Layer)

# Purpose

    - Exposes REST endpoints
    - Handles HTTP requests and responses

# Role

    - Maps URLs to service calls
    - Converts:
    - HTTP → Java
    - Java → HTTP

# Typical contents

    @RestController
    @RequestMapping("/companies/{companyId}/customers")
    public class CustomerController {
        @GetMapping
        public List<Customer> getCustomers(...) { ... }
    }

# What it should NOT do

    - Business logic
    - Database access

# Why it exists

    - Separates web concerns from business logic
    - Makes APIs easier to change or version

# Think of it as

<!-- - “How the outside world talks to your system” -->

# How They Work Together (Flow)

    HTTP Request
    ↓
    Controller (CustomerController)
    ↓
    Service Interface (CustomerService)
    ↓
    Service Implementation (CustomerServiceImpl)
    ↓
    Repository → Database

# Why This Structure Matters (Real-World Reasons)

    ✅ Easier to test
    ✅ Easier to scale
    ✅ Easier to debug
    ✅ Easier to change UI / API without breaking logic
    ✅ Required for enterprise & fintech systems
    ✅ Aligns with Spring Boot & DDD best practices

# Why @Override Is Needed in CustomerServiceImpl

    - 1. It Enforces the Contract
    * CustomerService defines a contract
    * CustomerServiceImpl promises to follow it

    @Override tells the compiler:

<!-- “This method MUST match a method from the interface” -->

    If it doesn’t match exactly → compile-time error

🧠 Why This Structure Matters (Architecturally)

# Interface (ProductService)

    -Contract
    -Testable
    -Replaceable

# ServiceImpl

    -Business rules
    -Ownership validation (companyId)
    -No HTTP or DB leakage

Repository

Pure persistence

No business logic
