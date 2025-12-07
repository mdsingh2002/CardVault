# 🎴 CardVault

A full-stack web application for managing and tracking your Pokemon card collection. Monitor card values, build wishlists, and analyze your collection's worth over time.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square&logo=spring)
![React](https://img.shields.io/badge/React-18-blue?style=flat-square&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5.x-blue?style=flat-square&logo=typescript)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Environment Variables](#environment-variables)
- [Screenshots](#screenshots)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)

---

## ✨ Features

### 🎯 Collection Management

- **Add Cards to Collection** - Search and add Pokemon cards from the official Pokemon TCG API
- **Track Quantities** - Manage how many copies of each card you own
- **Card Conditions** - Track card condition (Mint, Near Mint, Excellent, Good, Played, Poor)
- **Grading Information** - Record graded cards with company and grade value (PSA, BGS, etc.)
- **Purchase Price Tracking** - Log what you paid for each card
- **Acquisition Dates** - Track when you obtained each card
- **Notes** - Add custom notes to cards

### 📊 Value Tracking & Analytics

- **Automatic Market Pricing** - Pull current market prices from TCG Player via Pokemon TCG API
- **Collection Value History** - Interactive line chart showing portfolio value over time
- **Price Snapshots** - Automatic snapshots recorded when adding/removing cards
- **Total Value Calculation** - Real-time calculation of entire collection worth
- **Profit/Loss Tracking** - Compare purchase price vs. current value

### 🎯 Wishlist

- **Priority System** - Set priority levels (1-5) for cards you want
- **Max Price Targets** - Set budget limits for wishlist cards
- **Notes** - Add notes about why you want specific cards

### 🔍 Search & Browse

- **Pokemon TCG API Integration** - Search 20,000+ Pokemon cards
- **Filter by Set** - Browse cards by expansion set
- **Filter by Type** - Search by Pokemon type
- **Filter by Rarity** - Find rare, uncommon, or common cards
- **Card Details** - View card images, stats, HP, attacks, and pricing

### 🏆 Achievements (Database Ready)

- Track milestones like "First Card," "Collection Value $1000+," etc.
- Achievement system built into database schema

### 🎨 Modern UI/UX

- **Beautiful Toast Notifications** - Using Sonner for elegant feedback
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Dark Mode Ready** - Built with shadcn/ui components
- **Interactive Charts** - Recharts for data visualization

---

## 🛠 Tech Stack

### **Frontend**

| Technology                                    | Version | Purpose                 |
| --------------------------------------------- | ------- | ----------------------- |
| [React](https://react.dev/)                   | 18.x    | UI framework            |
| [TypeScript](https://www.typescriptlang.org/) | 5.x     | Type safety             |
| [Vite](https://vitejs.dev/)                   | 5.x     | Build tool & dev server |
| [React Router](https://reactrouter.com/)      | 6.x     | Client-side routing     |
| [Axios](https://axios-http.com/)              | 1.x     | HTTP client             |
| [Recharts](https://recharts.org/)             | 2.x     | Data visualization      |
| [Sonner](https://sonner.emilkowal.ski/)       | 1.x     | Toast notifications     |
| [shadcn/ui](https://ui.shadcn.com/)           | Latest  | UI component library    |
| [Tailwind CSS](https://tailwindcss.com/)      | 3.x     | Styling                 |

### **Backend**

| Technology                                                    | Version | Purpose                        |
| ------------------------------------------------------------- | ------- | ------------------------------ |
| [Java](https://www.oracle.com/java/)                          | 17      | Programming language           |
| [Spring Boot](https://spring.io/projects/spring-boot)         | 3.x     | Backend framework              |
| [Spring Security](https://spring.io/projects/spring-security) | 6.x     | Authentication & authorization |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | 3.x     | ORM & database abstraction     |
| [Hibernate](https://hibernate.org/)                           | 6.x     | JPA implementation             |
| [JWT](https://jwt.io/)                                        | -       | Token-based authentication     |
| [Lombok](https://projectlombok.org/)                          | Latest  | Boilerplate code reduction     |
| [Maven](https://maven.apache.org/)                            | 3.x     | Dependency management          |

### **Database**

| Technology                                | Version | Purpose          |
| ----------------------------------------- | ------- | ---------------- |
| [PostgreSQL](https://www.postgresql.org/) | 15+     | Primary database |

### **External APIs**

| API                                       | Purpose                        |
| ----------------------------------------- | ------------------------------ |
| [Pokemon TCG API](https://pokemontcg.io/) | Card data, images, and pricing |

---

## 🏗 Architecture

### **System Architecture**

```
┌─────────────────────────────────────────┐
│          Frontend (React)               │
│  ├─ Pages (Dashboard, Browse, etc.)     │
│  ├─ Components (Cards, Charts)          │
│  ├─ Services (API clients)              │
│  └─ State Management (React hooks)      │
└─────────────────────────────────────────┘
                    ↓ HTTP/REST
┌─────────────────────────────────────────┐
│       Backend (Spring Boot)             │
│  ├─ Controllers (REST endpoints)        │
│  ├─ Services (Business logic)           │
│  ├─ Repositories (Data access)          │
│  ├─ Models (JPA entities)               │
│  └─ Security (JWT authentication)       │
└─────────────────────────────────────────┘
                    ↓ JDBC
┌─────────────────────────────────────────┐
│        Database (PostgreSQL)            │
│  ├─ users, cards, user_cards            │
│  ├─ wishlist, achievements              │
│  └─ collection_value_history            │
└─────────────────────────────────────────┘
                    ↑
┌─────────────────────────────────────────┐
│      Pokemon TCG API                    │
│  └─ Card data & market prices           │
└─────────────────────────────────────────┘
```

### **Project Structure**

```
CardVault/
├── backend/
│   ├── src/main/java/com/cardvault/
│   │   ├── controller/         # REST API endpoints
│   │   ├── service/            # Business logic
│   │   ├── repository/         # Database access
│   │   ├── model/              # JPA entities
│   │   ├── dto/                # Data transfer objects
│   │   ├── config/             # Spring configuration
│   │   └── security/           # JWT & security
│   └── src/main/resources/
│       ├── application.properties
│       └── schema.sql          # Database schema
│
├── frontend/
│   ├── src/
│   │   ├── pages/              # Route pages
│   │   ├── components/         # React components
│   │   │   ├── ui/            # shadcn/ui components
│   │   │   ├── pokemon/       # Card-specific components
│   │   │   └── collection/    # Collection components
│   │   ├── services/          # API clients
│   │   ├── types/             # TypeScript types
│   │   └── App.tsx            # Main app component
│   └── package.json
│
└── README.md
```

---

## 🚀 Getting Started

### **Prerequisites**

- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **PostgreSQL 15+** - [Download](https://www.postgresql.org/download/)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Pokemon TCG API Key** - [Get Free Key](https://pokemontcg.io/)

### **Installation**

#### 1. **Clone the Repository**

```bash
git clone https://github.com/yourusername/CardVault.git
cd CardVault
```

#### 2. **Database Setup**

Create PostgreSQL database:

```bash
psql -U postgres
CREATE DATABASE cardvault;
\q
```

Run schema:

```bash
psql -U postgres -d cardvault -f backend/src/main/resources/schema.sql
psql -U postgres -d cardvault -f backend/src/main/resources/add_value_tracking.sql
```

#### 3. **Backend Setup**

Update `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cardvault
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

jwt.secret=YOUR_SECRET_KEY_MINIMUM_256_BITS
pokemon.tcg.api.key=YOUR_POKEMON_TCG_API_KEY
```

Build and run:

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start at `http://localhost:8080`

#### 4. **Frontend Setup**

Install dependencies:

```bash
cd frontend
npm install
```

Start dev server:

```bash
npm run dev
```

Frontend will start at `http://localhost:3000`

### **First Time Setup**

1. Navigate to `http://localhost:3000`
2. Click **Register** and create an account
3. Log in with your credentials
4. Start browsing cards and building your collection!

---

## 🔮 Future Features

### **Planned Features**

- [ ] **Trading System** - Trade cards with other users
- [ ] **Deck Builder** - Build and save Pokemon TCG decks
- [ ] **Price Alerts** - Get notified when card prices drop
- [ ] **Bulk Import** - Import collection from CSV
- [ ] **Social Features** - Follow other collectors, share collections
- [ ] **Set Completion Tracker** - Track progress on completing sets
- [ ] **Trade Value Calculator** - Calculate fair trades


## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📞 Contact

**Project Maintainer** - Mark Singh

- GitHub: [@mdsingh2002](https://github.com/mdsingh2002)
- Email: your.email@example.com

**Project Link:** [https://github.com/yourusername/CardVault](https://github.com/yourusername/CardVault)

---
