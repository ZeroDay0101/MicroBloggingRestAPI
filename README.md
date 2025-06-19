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

## 🛠 Configuration

### PostgreSQL Setup

Ensure your PostgreSQL database is available at the following default URL:

```
jdbc:postgresql://localhost:5432/SocialMedia
```

Or update the connection settings in `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/SocialMedia
spring.datasource.username=postgres
spring.datasource.password=1234
```

---

## 🔐 JWT Encryption Keys

Sample encryption keys are located at:  
`src/main/resources/encryptionKeys`

**⚠️ WARNING:** These sample keys are public and should **not** be used in production.

To secure your application:

1. Generate your own key pair.
2. Store the public key in **X.509** format.
3. Store the private key in **PKCS#8** format, both with proper headers.

Example header formats:
```
-----BEGIN PUBLIC KEY-----
...your public key...
-----END PUBLIC KEY-----

-----BEGIN PRIVATE KEY-----
...your private key...
-----END PRIVATE KEY-----
```