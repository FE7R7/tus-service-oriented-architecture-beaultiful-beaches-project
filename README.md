# tus-service-oriented-architecture-beaultiful-beaches-project

# SOA4 Project

**Projct Title:** World's Most Beautiful Beaches & Countries
**Communication Style:** Request-Response (Asynchronous Nonblocking) via OpenFeign

---

## 1. Project Overview
This project comprises two microservices:
1. **country-service (Producer - Service B):** Manages Country entities containing details like Name, Continent, and Capital.
2. **beach-service (Consumer - Service A):** Manages Beach entities containing details like Name, Description, Rating, and City, and stores a reference ID to the country. It serves an HTML/JS frontend to interact with the API.

Both services are developed with Spring Boot 4.x, Java 17, utilize MySQL for storage, and interact asynchronously using OpenFeign.

---

## 2. Running the System
The entire architecture can easily be deployed via `docker-compose`.

```bash
# CD into the project root directory where docker-compose.yml exists
$ docker-compose up --build
```
This single command brings up 4 independent containers:
- `country-mysql` (MySQL DB)
- `country-service` 
- `beach-mysql` (MySQL DB)
- `beach-service`

### Code Excerpt: Docker Deployment Automation
*docker-compose.yml configures dependencies and forces the `docker` profile execution.*
```yaml
  beach-service:
    build: ./beach-service
    ports:
      - "8080:8080"
    environment:
      - SERVER_PORT=8080
      - SPRING_PROFILES_ACTIVE=docker
      - COUNTRY_SERVICE_URL=http://country-service:8080
    depends_on:
      - beach-mysql
      - country-service
```
By mapping `COUNTRY_SERVICE_URL`, our consumer service inherently knows the container networking route to securely talk to the producer.
