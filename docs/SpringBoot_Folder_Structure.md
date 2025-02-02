## Project Structure Explained
This project follows the MVC structure that's usually followed in SpringBoot projects. The backend is structured in the following folders explained below:

## Spring Boot Folder Structure

### 1. **Config (Configuration Files)**
**Purpose:** Manages application configuration settings.  
**Annotation Used:** `@Configuration`, `@Bean`, `@Enable(Feature)`  
**Responsibility:**
* Defines application-wide configuration settings.
* Manages beans, properties, and external integrations.
* Enables/disables features using annotations.

### 2. **Controller (Handles HTTP Requests)**
**Purpose:** Manages HTTP requests and responses.  
**Annotation Used:** `@RestController`, `@Controller`  
**Responsibility:**
* Receives user input via HTTP (`GET`, `POST`, `PUT`, `DELETE`).
* Calls the appropriate service layer methods.
* Returns a response (`JSON`, `HTML`, etc.).

### 3. **DTO (Data Transfer Object)**
**Purpose:** Transfers data between layers.  
**Annotation Used:** `@Data`, `@Getter`, `@Setter`, `@AllArgsConstructor`  
**Responsibility:**
* Defines structured data without business logic.
* Used to prevent exposing entity models directly.
* Helps in request/response formatting.

### 4. **Exception (Handles Application Errors)**
**Purpose:** Manages custom exceptions and error handling.  
**Annotation Used:** `@ControllerAdvice`, `@ExceptionHandler`  
**Responsibility:**
* Defines custom exception classes.
* Handles application-wide exceptions.
* Provides meaningful error responses.

### 5. **Model (Represents Data)**
**Purpose:** Defines the structure of data (Entity/DTO).  
**Annotation Used:** `@Entity`, `@Table`  
**Responsibility:**
* Represents database tables (for JPA).
* Contains fields, getters, setters, and relationships.

### 6. **Repository (Handles Database Operations)**
**Purpose:** Communicates with the database.  
**Annotation Used:** `@Repository` (though optional when extending `JpaRepository`).  
**Responsibility:**
* Provides CRUD operations.
* Uses Spring Data JPA (or other ORM tools).

### 7. **Service (Business Logic Layer)**
**Purpose:** Implements business logic.  
**Annotation Used:** `@Service`  
**Responsibility:**
* Contains core logic.
* Calls repository layer for data operations.
* Used by the controller to process requests.

### 8. **Utils (Utility Classes and Helpers)**
**Purpose:** Provides reusable utility functions.  
**Annotation Used:** None (regular Java classes with static methods).  
**Responsibility:**
* Contains helper methods for common tasks (e.g., string formatting, date conversion).
* Used across multiple layers to avoid redundant code.
* Improves code maintainability.


