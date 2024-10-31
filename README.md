# Task Manager API

A simple task management REST API built with Java, Spring Boot, and JWT-based authentication. This project allows users to create, update, retrieve, and delete tasks securely.

## Features

- **User Authentication**: JWT-based login and authorization.
- **Task Management**: Create, read, update, and delete tasks.
- **Role-Based Access**: Only authenticated users can access the API.

## Technologies

- **Java**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **PostgreSQL** (or any SQL database)

## Dependencies

The project includes the following dependencies:

- **Spring Boot Starter Data JPA**: For interacting with the database.
- **Spring Boot Starter Security**: To handle authentication and authorization.
- **Spring Boot Starter Web**: For building RESTful web services.
- **PostgreSQL**: PostgreSQL driver for database connection.
- **Lombok**: For reducing boilerplate code with annotations like `@Data`, `@Getter`, and `@Setter`.
- **Spring Boot Starter Test**: For testing with Spring Boot.
- **Spring Security Test**: For testing Spring Security configurations.
- **Mockito**: For mocking dependencies in tests.
- **JSON Web Token (JWT)**: 
  - `jjwt-api`: Core library for JWT.
  - `jjwt-impl`: Implementation details for JWT.
  - `jjwt-jackson`: Jackson support for JWT.
- **Jakarta Servlet API**: For servlet-related functionality.

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven**
- **PostgreSQL** or another SQL database

