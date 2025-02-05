# 🏆 Task Prioritization API

A RESTful API that allows users to manage tasks efficiently by providing CRUD operations while integrating a smart priority system based on predefined business rules and user-defined parameters.

## 🚀 Features
- ✅ Full CRUD (Create, Read, Update, Delete) functionality for tasks
- ✅ Containerized setup for easy deployment
- ✅ Secure endpoints with JWT authentication
- ✅ Robust error handling with clear messages
- ✅ Comprehensive tests to ensure system stability
- ✅ Auditing to track task history
- ✅ AOP-driven logging for streamlined debugging


## 🛠️ Tech Stack
- **Language:** Java / Spring Boot
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **API:** RESTful
- **Security:** JWT Authentication

## 📦 Installation & Setup

### Prerequisites
Ensure you have the following installed:
- IntelliJ IDEA (or any preferred IDE)
- Docker Desktop (or any Docker Engine)
- Maven ([Installation Guide](https://maven.apache.org/install.html) or use your IDE’s built-in Maven lifecycle)

### Build & Run
```sh
git clone https://github.com/prodanov33/TaskApplication
mvn package #if maven is not installed, use your IDE’s built-in Maven lifecycle
```

## 🔐 Authentication
Before making API requests, generate a JWT token for authorization.

### Generate JWT for Admin
**POST**: `http://localhost:8080/auth?authType=ADMIN`

### Generate JWT for User
**POST**: `http://localhost:8080/auth?authType=USER`

Use the generated token in the `Authorization` header with the `Bearer` prefix.

---

## 📌 API Endpoints

### 📝 Create a Task
**POST** `/tasks`
#### Request
```json
{
    "title":"TestTitle",
    "description":"TestDesc",
    "dueDate":"2025-02-04",
    "isCritical":true
}
```
#### Response
```json
{
    "id": 1,
    "title": "TestTitle",
    "description": "TestDesc",
    "dueDate": "2025-02-04",
    "priority": "HIGH",
    "completed": false
}
```

### ✏️ Update a Task
**PUT** `/tasks/{taskId}`
#### Request
```json
{
    "title":"UpdateTitle",
    "description":"UpdateDescription",
    "dueDate":"2025-02-10",
    "isCritical":true,
    "isCompleted":true
}
```
#### Response
```json
{
    "id": 1,
    "title": "UpdateTitle",
    "description": "UpdateDescription",
    "dueDate": "2025-02-10",
    "priority": "LOW",
    "completed": true
}
```

### 📖 Retrieve a Task
**GET** `/tasks/{taskId}`
#### Response
```json
{
    "id": 1,
    "title": "Title",
    "description": "Description",
    "dueDate": "2025-02-10",
    "priority": "LOW",
    "completed": true
}
```

### 📋 Retrieve All Tasks (Sorted)
**GET** `/tasks?sort=PRIORITY` (Other options: `DUE_DATE`)
#### Response
```json
[
    {
        "id": 5,
        "title": "Title5",
        "description": "Description5",
        "dueDate": "2025-02-02",
        "priority": "HIGH",
        "completed": false
    },
    {
        "id": 6,
        "title": "Title6",
        "description": "Description6",
        "dueDate": "2025-02-04",
        "priority": "MEDIUM",
        "completed": false
    },
    {
        "id": 2,
        "title": "Title2",
        "description": "Description2",
        "dueDate": "2025-02-10",
        "priority": "LOW",
        "completed": false
    }
]
```

### 🎯 Filter Tasks
**GET** `/tasks?filter=PRIORITY&value=HIGH` (Other options: `IS_COMPLETED`)
#### Response
```json
[
    {
        "id": 5,
        "title": "Title5",
        "description": "Description5",
        "dueDate": "2025-02-02",
        "priority": "HIGH",
        "completed": false
    }
]
```

---

## 📚 External Libraries Used
The following dependencies are used in the project:

- **Spring Boot Starters**: `web`, `data-jpa`, `security`, `validation`, `test`,`aop`
- **JWT Authentication**: `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
- **Database**: `PostgreSQL JDBC Driver`
- **Lombok**: Simplifies code by auto-generating boilerplate
- **Testing**: `Mockito`, `JUnit`, `Spring Security Test`

---

### 💡 Notes
- Ensure that `Docker` is running before deployment.
- The priority system automatically classifies tasks based on predefined business rules.
- The API supports filtering and sorting for better task management.

📌 *This API is designed for efficiency, security, and ease of use in managing tasks with priority-based organization.*

