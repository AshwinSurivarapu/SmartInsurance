# 🚀 SmartInsure Platform

SmartInsure is a modern, polyglot microservices-based insurance enrollment platform built with a blend of cutting-edge technologies: **Spring Boot (Java)**, **Flask with Transformers (Python)**, and **React (TypeScript)**. It emphasizes scalable architecture, AI-powered features, and containerized deployment for real-world readiness.

---

## 🧭 Overview

| Category         | Details                                                                 |
|------------------|-------------------------------------------------------------------------|
| 🛠️ Tech Stack    | Java (Spring Boot), Python (Flask, Transformers), React (TypeScript)    |
| 🗃️ Databases      | PostgreSQL (structured), MongoDB (unstructured docs)                   |
| 🔧 Infrastructure | Docker, Docker Compose, Eureka (Service Registry), Spring Cloud Gateway |
| 🎯 AI Models      | Hugging Face transformers for recommendations and document analysis     |
| 🌐 APIs           | REST (Java, Python), GraphQL (Java)                                    |
| 🧪 Auth & Security| JWT-based authentication                                                |

---

## 📅 Development Plan & Daily Tracker

| Day | Focus                           | Java                              | Python                                | React                               |
|-----|----------------------------------|------------------------------------|----------------------------------------|-------------------------------------|
| 1   | Skeleton Setup                   | `user-auth-service`, `application-service` | `plan-recommender-service`       | `customer-portal-app` init          |
| 2   | Basic REST Endpoints            | `/register`                        | `/recommend` (hardcoded response)      | Registration Form                   |
| 3   | Service Communication           | Call recommender via `RestTemplate`| Add Hugging Face pipeline              | Connect registration UI             |
| 4   | JWT Authentication              | `/login` + JWT                     | Refine pipeline inputs                 | JWT storage via localStorage        |
| 5   | GraphQL API                     | `createApplication` mutation       | Model research                         | Apollo Client + ApplicationForm     |
| 6   | New Services & UIs              | Pre-empt doc analysis call         | `document-analysis-service` skeleton   | Init `underwriter-dashboard-app`    |
| 7   | Document Storage                | Manual testing                     | MongoDB integration                    | List applications via GraphQL       |
| 8   | Dockerization                   | Dockerfiles for services           | Dockerfiles + transformers handling    | Build & serve React via Docker      |
| 9   | Docker Compose Integration      | Comprehensive `docker-compose.yml`| Configure networking & volumes         | —                                   |
| 10  | Service Registry & Gateway      | Eureka + Spring Cloud Gateway      | —                                      | —                                   |

---

## 🧾 Service Breakdown

### 🔐 User Authentication Service (Java)
- Technologies: Spring Boot, Spring Security, JWT
- Endpoints: `/register`, `/login`
- Features: User & Role entities, password hashing, JWT generation

### 📝 Application Service (Java)
- Technologies: Spring Boot, GraphQL
- Endpoints: `/applications`, `createApplication` mutation
- Features: Inter-service communication, plan orchestration

### 🤖 Plan Recommender (Python)
- Technologies: Flask, Hugging Face Transformers
- Endpoint: `/recommend`
- Features: AI-based text classification, model experimentation

### 📄 Document Analysis Service (Python)
- Technologies: Flask, MongoDB (via `pymongo`)
- Endpoint: `/analyze`
- Features: Store documents, extract metadata for underwriters

### 🌐 Customer Portal App (React)
- Technologies: React (TypeScript), Axios
- Features: Registration, Login, Application Form

### 🧑‍💼 Underwriter Dashboard (React)
- Technologies: React, Apollo Client
- Features: Application list via GraphQL, filtering options

---

## ⚙️ Infrastructure Details

### 🧭 Eureka Service Registry
- Enables dynamic service discovery
- Java annotation: `@EnableEurekaServer`

### 🛡 API Gateway (Spring Cloud Gateway)
- Routes external requests to backend services
- Example routes:
    - `/api/auth/** → user-auth-service`
    - `/api/app/** → application-service`

### 🐳 Docker
- Services containerized individually
- React apps served via Nginx

### 🧩 Docker Compose
- Orchestrates full stack locally
- Includes:
    - 6 apps
    - PostgreSQL & MongoDB
    - Eureka and API Gateway

---

## 🔮 Future Enhancements

| Area              | Description |
|-------------------|-------------|
| 🔁 Kafka / RabbitMQ | Asynchronous event handling |
| 🧠 Advanced AI/ML   | RAG architecture, fine-tuned models |
| 🛡 Resilience4j    | Circuit breakers, retry strategies |
| 🚀 CI/CD           | GitHub Actions, automated pipelines |
| 📈 Observability   | Zipkin, Jaeger, Prometheus, Grafana |
| ☁️ Deployment      | Kubernetes-ready architecture |

---

## 🏁 Getting Started

1. Clone the repo and navigate to each service directory
2. Run each using `./gradlew bootRun`, `flask run`, or `npm start`
3. Or run the entire stack with:

```bash
docker-compose up --build
