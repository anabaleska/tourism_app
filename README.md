# Tourism App

Tourism App is a web application for exploring tourist places around Ohrid and Struga. It includes user authentication, favorites, place filtering, and two recommendation approaches: content-based recommendations for similar places and collaborative recommendations based on users with similar favorites.

The project is split into two services:

- `backend`: Spring Boot MVC application with Thymeleaf views, PostgreSQL persistence, Spring Security, and Flyway migrations.
- `recommendation-service`: FastAPI microservice that provides content-based and collaborative recommendation endpoints.

## Features

- Browse tourism places with images, descriptions, categories, types, locations, and visit information.
- Filter places by available place attributes.
- Register, log in, view profile, and log out.
- Add places to favorites with one click.
- View favorite places on a dedicated Favorites page.
- Get content-based similar places from the place details page.
- Get collaborative recommendations from the Favorites page.
- PostgreSQL database with Flyway migrations.

## Tech Stack

### Backend

- Java 17
- Spring Boot 4
- Spring MVC
- Thymeleaf
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven

### Recommendation Service

- Python
- FastAPI
- Pandas
- Scikit-learn
- PostgreSQL driver: `psycopg2-binary`


## Prerequisites

- Java 17
- Docker and Docker Compose
- Python 3.10+ recommended
- Maven wrapper is included in `backend`
- PostgreSQL is provided through Docker Compose

## Database Setup

The backend uses PostgreSQL through `backend/docker-compose.yaml`.

Start PostgreSQL:

```bash
cd backend
docker compose up -d
```

Flyway migrations are located in:

```text
backend/src/main/resources/db/migration
```

The Spring Boot application runs migrations automatically when it starts.

## Running the Backend

From the `backend` folder:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

## Running the Recommendation Service

From the `recommendation-service` folder:

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

On Windows PowerShell:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

The recommendation service runs on:

```text
http://localhost:8000
```
