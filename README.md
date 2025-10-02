# 📝 Micro-Blogging REST API (Spring Boot)

This is a simple Spring Boot REST API for a micro-blogging platform. It supports basic social features such as posting, liking, commenting, and user management with authentication and authorization.

---

## 🔧 Features

- ✅ Create, update, and delete **users** and **profiles**
- 📝 Create, update, and delete **posts**
- ❤️ Like and comment on posts
- 🔐 JWT-based **authentication**
- 👤 Role-based access (**Admin** functionality)
- 💾 PostgreSQL database integration
- 🔑 JWT signing with encryption keys (customizable)

---
## 🐳 Running with Docker

The project is fully Dockerized, including PostgreSQL. You can build and run everything with one command.
1. Clone the repository
2. Package into a jar
3. Run with Docker Compose
``` bash
git clone https://github.com/ZeroDay0101/MicroBloggingRestAPI.git
cd MicroBloggingRestAPI
docker-compose up --build
```
---
## 🚀 API Endpoints

### 🔑 Authentication Endpoints

| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | `/login`                     | Log user in                          |
| POST   | `/login/refresh_token`       | Refresh access token using refresh token |

### 📬 Post Endpoints

| Method | Endpoint                          | Description                |
|--------|-----------------------------------|----------------------------|
| POST   | `/api/post`                       | Add a new post             |
| PATCH  | `/api/post`                       | Edit an existing post      |
| DELETE | `/api/post?id={id}`               | Delete post by ID          |
| GET    | `/api/post?id={id}`               | Get post by ID             |
| GET    | `/api/post/byProfile?id={userId}` | Get all posts by user ID   |
| POST   | `/api/post/like?id={postId}`      | Like a post                |
| POST   | `/api/post/unlike?id={postId}`    | Remove like from a post    |
| GET    | `/api/post/comments/{id}/offset={offset}` | Get comments for a post |
| GET    | `/api/post/likes/{id}/offset={offset}`    | Get likes for a post    |

### 👤 Profile Endpoints

| Method | Endpoint                                           | Description                 |
|--------|----------------------------------------------------|-----------------------------|
| GET    | `/api/profile/byUsername/{username}`              | Get profile by username     |
| GET    | `/api/profile/byId/{id}`                          | Get profile by ID           |
| GET    | `/api/profile/getId/byUsername/{username}`        | Get profile ID by username  |

### 👥 User Endpoints

| Method | Endpoint                                | Description                            |
|--------|-----------------------------------------|----------------------------------------|
| POST   | `/api/user`                             | Create a new user                      |
| DELETE | `/api/user/{id}`                        | Delete user by ID                      |
| PATCH  | `/api/user?newUsername={newUsername}`   | Change username (logged-in user)       |
| PATCH  | `/api/user/?newUsername={newUsername}/{id}` | Change username by user ID        |
| PATCH  | `/api/user/password`                    | Change user password                   |

---

## 🔐 JWT Encryption Keys

Encryption keys used for jwt are located at:  
`src/main/resources/encryptionKeys`

**⚠️ WARNING:** The keys inside the project are sample and should not be used in production.

To secure your application, generate your own key pair:

1. The public key in **X.509** format.

2. The private key in **PKCS#8** format, both with proper headers.

Header formats:
```
-----BEGIN PUBLIC KEY-----
...your public key...
-----END PUBLIC KEY-----

-----BEGIN PRIVATE KEY-----
...your private key...
-----END PRIVATE KEY-----
```
---
## 🛠 Configuration
Spring Boot API → http://localhost:8080

PostgreSQL (host machine) → localhost:5433

👉 Database data persists in the Docker volume postgres_data.

Default environment variables (from docker-compose.yml):


Spring Boot connects using:

    spring.datasource.url=jdbc:postgresql://postgres:5432/SocialMediaDB
    spring.datasource.username=postgres
    spring.datasource.password=1234

You can change these in docker-compose.yml and application.properties.

If you're running with maven and not docker you must create the db manually and change credentials accordingly.