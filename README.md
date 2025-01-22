# PiglioTech Back-End

## Overview
The back-end is a Java-based RESTful API hosted at [https://pigliotech-backend.onrender.com](https://pigliotech-backend.onrender.com). 
It serves as the backbone of the application, handling user authentication, data management, and API requests.

---

## Features
- RESTful API endpoints for managing resources.
- Database integration with PostgreSQL (Production) and H2 (Local).
- Dockerised setup for easy deployment.

---

## Tech Stack
- **Language**: Java
- **Framework**: Spring Boot (e.g., Starter, Data JPA).
- **Database**: PostgreSQL (via Neon hosting), H2 (for local development).
- **APIs**: Google Books API.
- **Build Tool**: Maven
- **Hosting**: Render
- **Containerisation**: Docker

---

## Accessing the Hosted Back-End
The API is publicly available at: [https://pigliotech-backend.onrender.com](https://pigliotech-backend.onrender.com).

### Example Endpoints
- **GET** `/api/books`: Fetches all available books.
- **POST** `/api/books`: Adds a new book.

No authentication is required to access the API endpoints.

---

## Installation and Setup

### Setting Up Profiles
The active profile for the application is managed in `application.properties`. By default, the profile is set to `h2` for local development:
```properties
spring.profiles.default=h2
```

To switch to the production profile:
1. Update the `application.properties` file:
   ```properties
   spring.profiles.active=prod
   ```
2. Or pass it as a command-line argument:
   ```bash
   java -Dspring.profiles.active=prod -jar app.jar
   ```

### Using the `.env` File
For Docker Compose, the `.env` file is used to configure the environment variables. Here is an example structure:
```env
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://<host>/<database>?sslmode=require
DB_USER=<your-username>
DB_PASSWORD=<your-password>
DB_HOST=<host>
DB_PORT=5432
DB_NAME=<database>
DB_SSLMODE=require
```
Make sure the `.env` file is placed in the root of your project and is referenced in your `docker-compose.yml`:
```yaml
services:
  app:
    env_file:
      - .env
```

### Running with H2 Database Profile
To run the application locally using the in-memory H2 database:
1. Ensure `spring.profiles.active=h2` is set in `application.properties`.
2. Start the application:
   ```bash
   mvnw spring-boot:run
   ```
3. Access the H2 console at `http://localhost:8080/h2-console`.

### Running with Docker Compose (Production Profile)
1. Ensure `SPRING_PROFILES_ACTIVE=prod` is set in the `.env` file.
2. Run the application:
   ```bash
   docker-compose up
   ```

---

## Contribution
1. Fork the repository and create your feature branch:
   ```bash
   git checkout -b feature/YourFeatureName
   ```
2. Commit your changes:
   ```bash
   git commit -m 'Add some feature'
   ```
3. Push to the branch:
   ```bash
   git push origin feature/YourFeatureName
   ```
4. Create a pull request.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

## Acknowledgements
- **Team Members**:
  - [Ge-Drei](https://github.com/ge-drei)
  - [JacksonR64](https://github.com/JacksonR64)
  - [Jnc-Panda](https://github.com/jnc-panda)
  - [Kadri-K](https://github.com/orgs/jv-kune-kune/people/kadri-k)
  - [Tchabva](https://github.com/tchabva)

- **Tools and Libraries**:
  - Spring Boot (Starter, Data JPA).
  - Google Books API.
  - Maven for build automation.
  - Docker for containerisation.

---

## Contact
For queries or support, contact jvkunekune@gmail.com.
