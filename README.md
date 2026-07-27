# Hospital Management System

A Spring Boot backend application for managing hospital operations, doctors, appointments, and RBAC authentication.

## Tech Stack
* **Java**: 21
* **Framework**: Spring Boot
* **Build System**: Gradle
* **Database**: PostgreSQL
* **Security**: JWT & Spring Security

## Automated CI/CD & Deployment
This repository is configured with a Jenkins Declarative Pipeline (`Jenkinsfile`) and multi-stage `Dockerfile`.

* Pushing to `main` branch automatically triggers Jenkins build and pushes image to Docker Hub (`dikshanta07/hospital-management:latest`).
* Run locally via Docker Compose using `.env` file and `docker-compose.yml`.
