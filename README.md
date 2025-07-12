# Code Cracker Docker Setup

This project provides Docker images for the API (`api-rest`) and the web client (`web-cli`). The easiest way to run them together is with Docker Compose.

## Build and run using Docker Compose

1. Ensure Docker and Docker Compose are installed.
2. From the project root, build the images:
   ```bash
   docker compose build
   ```
3. Start the containers:
   ```bash
   docker compose up
   ```
4. Access the services:
   - API: `http://localhost:8124/codebreaker/v1`
   - Web client: `http://localhost:8080`

Stop the containers with `Ctrl+C` and remove them with `docker compose down`.

## Configuration

- `api-rest` exposes port **8124** and reads `SERVER_PORT` from the environment.
- `web-cli` is built with the `VITE_API_BASE` build argument. In `docker-compose.yml` it is set to communicate with the `api-rest` container.

