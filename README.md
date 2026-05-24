# TaskFlow

TaskFlow is a full-stack task management application built as a learning and portfolio project. It combines a React/Vite frontend with a Spring Boot REST API, JWT authentication, PostgreSQL persistence, Flyway migrations, and a deployed frontend/backend workflow.

The app lets authenticated users create, list, update, complete, reopen, and delete their own tasks. The frontend also includes a light/dark theme switcher and a local demo mode for previewing the UI without running the backend.

## Features

- User registration and login
- JWT-based authentication
- Protected task routes
- User-scoped task management
- Create, list, edit, complete, reopen, and delete tasks
- Task summary counters for total, completed, and pending tasks
- Light and dark themes with saved user preference
- Subtle animated background on the frontend
- PostgreSQL database persistence
- Database schema versioning with Flyway
- Configurable CORS for deployed frontend/backend environments
- Local frontend demo mode for UI preview

## Tech Stack

### Backend

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- OAuth2 Resource Server
- JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven
- Springdoc OpenAPI

### Frontend

- React 19
- Vite 8
- JavaScript
- Axios
- CSS

### Deployment

- Backend: Railway
- Frontend: Vercel
- Database: Supabase PostgreSQL

## Project Structure

```text
taskflow/
+-- backend/
|   +-- src/main/java/com/henrique/taskflow/
|   |   +-- config/
|   |   +-- controller/
|   |   +-- dto/
|   |   +-- exceptions/
|   |   +-- infra/
|   |   +-- mapper/
|   |   +-- model/
|   |   +-- repository/
|   |   +-- service/
|   |   +-- TaskflowApplication.java
|   +-- src/main/resources/
|   |   +-- certs/
|   |   +-- db/migration/
|   |   +-- application.properties
|   +-- pom.xml
|   +-- mvnw
|   +-- mvnw.cmd
+-- frontend/
|   +-- public/
|   +-- src/
|   |   +-- assets/
|   |   +-- pages/
|   |   +-- services/
|   |   +-- App.jsx
|   |   +-- index.css
|   |   +-- main.jsx
|   +-- package.json
|   +-- vite.config.js
+-- README.md
```

## API Overview

### Authentication

```text
POST /auth/register
POST /auth/login
```

### Tasks

```text
POST   /task
GET    /task
GET    /task/{id}
PATCH  /task/{id}
DELETE /task/{id}
```

Task endpoints require authentication.

## Running Locally

### Requirements

- Java 21
- Node.js and npm
- PostgreSQL
- Maven, or the included Maven wrapper

## Backend Setup

The backend is located in the `backend` folder.

Create a PostgreSQL database:

```sql
CREATE DATABASE taskflow;
```

Create `backend/.env` based on `backend/.env.example`:

```env
DB_URL=jdbc:postgresql://localhost:5432/taskflow
DB_USERNAME=your_postgres_user
DB_PASSWORD=your_postgres_password
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
JWT_PUBLIC_KEY_BASE64=base64_of_app_pub
JWT_PRIVATE_KEY_BASE64=base64_of_app_key
```

The project also includes local key files under `backend/src/main/resources/certs/`. For deployed environments, the JWT keys can be provided through the base64 environment variables above.

Start the backend:

```bash
cd backend
./mvnw spring-boot:run
```

On Windows:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

## Frontend Setup

The frontend is located in the `frontend` folder.

Install dependencies:

```bash
cd frontend
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend runs at:

```text
http://localhost:5173
```

### Frontend Environment Variables

To point the frontend to a different backend:

```env
VITE_API_URL=http://localhost:8080
```

For production:

```env
VITE_API_URL=https://your-railway-api-url
```

### Demo Mode

During local development, the frontend includes a demo mode so the task board can be previewed without running the backend.

Run with demo mode disabled when you want to test the real login/API flow:

```bash
VITE_DEMO_MODE=false npm run dev
```

In production builds, demo mode is only enabled if explicitly configured:

```env
VITE_DEMO_MODE=true
```

## Deployment Notes

Recommended setup:

- Deploy the backend to Railway.
- Deploy the frontend to Vercel.
- Use Supabase PostgreSQL as the production database.
- Set the Railway backend URL as `VITE_API_URL` in Vercel.
- Set the Vercel frontend URL as `APP_CORS_ALLOWED_ORIGINS` in Railway.

Backend environment variables:

```env
DB_URL=your_supabase_jdbc_url
DB_USERNAME=your_supabase_database_user
DB_PASSWORD=your_supabase_database_password
APP_CORS_ALLOWED_ORIGINS=https://your-vercel-app.vercel.app
JWT_PUBLIC_KEY_BASE64=base64_of_public_key
JWT_PRIVATE_KEY_BASE64=base64_of_private_key
```

Frontend environment variables:

```env
VITE_API_URL=https://your-railway-api-url
```

## Useful Commands

Backend:

```bash
cd backend
./mvnw compile
./mvnw test
./mvnw spring-boot:run
```

Frontend:

```bash
cd frontend
npm run dev
npm run build
npm run lint
```

## What I Learned

- Building a layered Spring Boot REST API with controllers, services, repositories, DTOs, mappers, and models
- Securing endpoints with Spring Security and JWT
- Loading application configuration from environment variables for local and deployed environments
- Connecting Spring Boot to PostgreSQL with Spring Data JPA
- Managing database migrations with Flyway
- Handling frontend authentication state and protected UI flows
- Consuming a backend API from React with Axios
- Deploying a full-stack project with Vercel, Railway, and Supabase
- Debugging deployment issues involving CORS, environment variables, and JWT keys
- Improving UI polish with responsive layouts, theme switching, and animation

## Future Improvements

- Add automated backend tests with JUnit 5 and Mockito
- Add integration tests for authentication and task ownership
- Add frontend tests
- Add task due dates and priorities
- Add filtering and search by task status
- Add pagination for larger task lists
- Integrate TaskFlow with a Discord bot for task notifications and commands
- Improve API documentation examples
- Add Docker support for local development

## Author

Developed by Henrique Brandao as a learning and portfolio project.

- GitHub: [henrique-brandao](https://github.com/henrique-brandao)
- LinkedIn: [Henrique Brandao](https://www.linkedin.com/in/Brandaohenrique)
