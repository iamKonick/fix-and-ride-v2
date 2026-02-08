# Fix&Ride - Premium Labor Services

A modern, full-stack application for booking premium manual labor services (car maintenance, tool lending, repairs, and taxi services).

## 🚀 Project Overview

Fix&Ride provides a seamless experience for users to book services and for admins to manage bookings through a dedicated dashboard.

### Key Features
- **User Authentication**: Secure login and registration with JWT.
- **Service Booking**: Dynamic calendar-based booking system.
- **Admin Dashboard**: Comprehensive view of all bookings, stats, and management tools.
- **Persistence**: SQLite database with persistence between restarts.
- **Rich Aesthetics**: Modern UI with a premium look and feel.

## 🏗️ Architecture

The project is divided into two main components:

### 1. Backend (`/fix-and-ride-backend`)
- **Framework**: Spring Boot 3
- **Security**: Spring Security with JWT (Stateless)
- **Database**: SQLite (JPA/Hibernate)
- **Build Tool**: Maven
- **Port**: 9090 (HTTPS)

### 2. Frontend (`/fix-and-ride-frontend`)
- **Technologies**: HTML5, Vanilla JavaScript, Custom CSS
- **Server**: Python HTTPS Server
- **Port**: 5500 (HTTPS)

## 🛠️ Setup & Running

### Prerequisites
- Java 21+
- Python 3+
- Maven

### Run Backend
```bash
cd fix-and-ride-backend
./mvnw spring-boot:run
```

### Run Frontend
```bash
cd fix-and-ride-frontend
python3 https_server.py
```

### Run with Docker (Recommended)
You can run both services together using Docker Compose. See [DOCKER.md](DOCKER.md) for full details.

```bash
docker compose up --build -d
```

## 👥 Demo Accounts

### User
- **Email**: `anyone@gmail.com`
- **Password**: `sinbad123`

### Admin
- **Email**: `admin@fix-and-ride.com`
- **Password**: `admin123`

## 📂 Project Structure
- `fix-and-ride-backend/`: Java Spring Boot source code.
- `fix-and-ride-frontend/`: Static assets (HTML, CSS, JS).
- `docs/assets/`: Design mockups and visual project documentation.
