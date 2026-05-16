# TaskFlow

TaskFlow is a full-stack task management project built as a learning and portfolio application. It provides a simple interface to create, list, edit, complete, and delete tasks while practicing a React frontend integrated with a Spring Boot REST API.

## Features

- Create tasks with title and description
- List all registered tasks
- Edit task details
- Mark tasks as completed or incomplete
- Delete tasks
- Display task summary counts for total, completed, and incomplete tasks
- Persist data in a PostgreSQL database
- Database migration with Flyway
- Basic backend validation and error handling

## Technologies Used

### Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven
- Springdoc OpenAPI

### Frontend

- React
- Vite
- JavaScript
- Axios
- CSS

## Project Status

This project is currently under development as a learning and portfolio project. The main CRUD flow is implemented, and future improvements can still be added to make it more complete and closer to a real-world application.

## How to Run the Backend

The backend is located in the `backend` folder.

### Requirements

- Java 21
- PostgreSQL
- Maven, or use the included Maven wrapper

### Environment Variables

The backend reads the database connection from environment variables:

```bash
DB_URL=jdbc:postgresql://localhost:5432/taskflow
DB_USERNAME=your_postgres_user
DB_PASSWORD=your_postgres_password
```

Create a PostgreSQL database before starting the application. Example:

```sql
CREATE DATABASE taskflow;
```

### Start the Backend

On Windows PowerShell:

```powershell
cd backend
$env:DB_URL="jdbc:postgresql://localhost:5432/taskflow"
$env:DB_USERNAME="your_postgres_user"
$env:DB_PASSWORD="your_postgres_password"
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
cd backend
export DB_URL="jdbc:postgresql://localhost:5432/taskflow"
export DB_USERNAME="your_postgres_user"
export DB_PASSWORD="your_postgres_password"
./mvnw spring-boot:run
```

By default, the API runs at:

```text
http://localhost:8080
```

Main endpoint:

```text
/task
```

## How to Run the Frontend

The frontend is located in the `frontend/taskflow` folder.

### Requirements

- Node.js
- npm

### Start the Frontend

```bash
cd frontend/taskflow
npm install
npm run dev
```

By default, the Vite development server runs at:

```text
http://localhost:5173
```

The frontend expects the backend to be running at:

```text
http://localhost:8080
```

## Folder Structure

```text
Taskflow/
+-- backend/
|   +-- .mvn/
|   +-- src/
|   |   +-- main/
|   |   |   +-- java/com/henrique/taskflow/
|   |   |   |   +-- controller/
|   |   |   |   +-- dto/
|   |   |   |   +-- exceptions/
|   |   |   |   +-- infra/
|   |   |   |   +-- mapper/
|   |   |   |   +-- model/
|   |   |   |   +-- repository/
|   |   |   |   +-- service/
|   |   |   |   +-- TaskflowApplication.java
|   |   |   +-- resources/
|   |   |       +-- db/migration/
|   |   |       +-- application.properties
|   |   +-- test/
|   +-- mvnw
|   +-- mvnw.cmd
|   +-- pom.xml
+-- frontend/
|   +-- taskflow/
|       +-- public/
|       +-- src/
|       |   +-- assets/
|       |   +-- pages/
|       |   +-- services/
|       |   +-- index.css
|       |   +-- main.jsx
|       +-- index.html
|       +-- package.json
|       +-- vite.config.js
+-- README.md
```

## Screenshots

Screenshots will be added here.


## What I Learned

- How to structure a Spring Boot REST API using controller, service, repository, DTO, mapper, and model layers
- How to connect a Java backend to PostgreSQL using Spring Data JPA
- How to manage database schema changes with Flyway migrations
- How to validate incoming API data with Jakarta Validation
- How to consume a REST API from a React application using Axios
- How to organize a React/Vite frontend with pages, services, assets, and CSS
- How frontend and backend applications communicate during local development

## Future Improvements

- Add user authentication with Spring Security and JWT
- Add task due dates and priorities
- Add filtering and search by task status
- Add pagination for task listing
- Improve frontend form validation and user feedback
- Add backend automated tests for services and API endpoints
- Add frontend tests
- Add Docker support for easier local setup
- Expand API documentation
- Deploy the backend and frontend

## Author

Developed by Henrique Brandão as a learning and portfolio project.
- GitHub: [henrique-brandao](https://github.com/henrique-brandao)
- LinkedIn: [Henrique Brandão](https://www.linkedin.com/in/Brandaohenrique)
