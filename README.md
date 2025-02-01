# Minimal Notepad (Backend)
Minimal Notepad is a digital note-taking tool designed for students, teachers, and professionals. It simplifies creating, organizing, and sharing notes with a user-friendly interface. With features like tagging, annotations, and collaboration, it enhances learning and teamwork and offers a seamless and efficient note-taking experience.

This project was created as part of SEP1 (Software Engineering Project 1).

📆 January - March, 2025

## Technical stack:
- Spring Boot
- MariaDB
- dotenv

## How to run locally:
1) Clone the repository
2) Copy the .env.example file and create a new `.env` file in the same directory and add your database password inside .env
3) Execute the script from the `create_db.sql` file located in the resources folder to create the database
4) Run the `MinimalnotepadApplication` file to start the application.

## Project Structure Explained
This project follows the MVC sturcture that's usually followed in SpringBoot projects. The backend is structured in 4 folders explained below:

### 1. Controller 

Purpose: Manages HTTP requests and responses.
Annotation Used: @RestController or @Controller
Responsibility:
* Receives user input via HTTP (GET, POST, PUT, DELETE).
* Calls the appropriate service layer methods.
* Returns a response (JSON, HTML, etc.).

### 2.  Model (Represents Data)
Purpose: Defines the structure of data (Entity/DTO).
Annotation Used: @Entity, @Table
Responsibility:
* Represents database tables (for JPA).
* Contains fields, getters, setters, and relationships.

### 3. Repository(Handles Database Operations)
Purpose: Communicates with the database.
Annotation Used: @Repository (though optional when extending JpaRepository).
Responsibility:
* Provides CRUD operations. 
* Uses Spring Data JPA (or other ORM tools).

### 4. Service (Business Logic Layer)
Purpose: Implements business logic.
Annotation Used: @Service
Responsibility:
* Contains core logic. 
* Calls repository layer for data operations.
* Used by the controller to process requests.
