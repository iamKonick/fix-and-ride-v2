# Fix&Ride - Docker Usage Guide

This guide describes how to run the Fix&Ride application using Docker Compose.

## 🚀 Getting Started

### Prerequisites
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Running the Application

To build and start both the backend and frontend services:

```bash
docker compose up --build -d
```

- `--build`: Forces a rebuild of the images.
- `-d`: Runs the containers in detached mode (background).

### Stopping the Application

To stop and remove the containers:

```bash
docker compose down
```

## 🌐 Accessing the Services

Once the containers are running, you can access the services at the following URLs:

- **Frontend**: [https://localhost:5500](https://localhost:5500)
- **Backend API**: [https://localhost:9090](https://localhost:9090)

> [!NOTE]
> Both services use self-signed SSL certificates. You may need to click "Advanced" and "Proceed to localhost" in your browser.

## 📂 Data Persistence

The backend database (`app.db`) is mapped from your local file system to the container:
- Local: `./fix-and-ride-backend/database`
- Container: `/app/database`

This ensures that your data (bookings, users, etc.) is preserved even if the containers are destroyed.

## 📝 Viewing Logs

To see the logs for both services:

```bash
docker compose logs -f
```

To see logs for a specific service:

```bash
docker compose logs -f backend
# or
docker compose logs -f frontend
```
