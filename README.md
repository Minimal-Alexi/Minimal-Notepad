# Project Structure Explained
This project follows the MVC sturcture that's usually followed in SpringBoot projects. The backend is structured in 4 folders explained below:

## 1. Controller 

Purpose: Manages HTTP requests and responses.
Annotation Used: @RestController or @Controller
Responsibility:
* Receives user input via HTTP (GET, POST, PUT, DELETE).
* Calls the appropriate service layer methods.
* Returns a response (JSON, HTML, etc.).

## 2.  Model (Represents Data)
Purpose: Defines the structure of data (Entity/DTO).
Annotation Used: @Entity, @Table
Responsibility:
* Represents database tables (for JPA).
* Contains fields, getters, setters, and relationships.

## 3. Repository(Handles Database Operations)
Purpose: Communicates with the database.
Annotation Used: @Repository (though optional when extending JpaRepository).
Responsibility:
* Provides CRUD operations. 
* Uses Spring Data JPA (or other ORM tools).

## 4. Service (Business Logic Layer)
Purpose: Implements business logic.
Annotation Used: @Service
Responsibility:
* Contains core logic. 
* Calls repository layer for data operations.
* Used by the controller to process requests.