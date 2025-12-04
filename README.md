# Student Progress Tracker API

A backend REST API for tracking student academic performance in educational institutions.
Provides secure authentication via JWT tokens and Google OAuth, along with comprehensive progress monitoring across various subjects.


## Base URL
```
http://localhost:8080/api
```

---

## 1. POST /auth/login
**Authentication with Email and Password**

**Request:**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "example@gmail.com",
  "password": "example"
}
```

**Response (200 - Success):**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "Ivanov Ivan",
    "email": "example@gmail.com"
  }
}
```

**Response (401 - Unauthorized):**
```json
{
  "success": false,
  "error": "Invalid credentials"
}
```

---

## 2. POST /auth/google
**Authentication via Google OAuth**

**Request:**
```http
POST /auth/google
Content-Type: application/json

{
  "googleToken": "ya29.a0AfH6SMC..."
}
```

**Response (200 - Success):**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "Ivanov Ivan",
    "email": "example@gmail.com"
  }
}
```

**Response (401 - Invalid Google token):**
```json
{
  "success": false,
  "error": "Invalid Google token"
}
```

**Response (404 - User not found):**
```json
{
  "success": false,
  "error": "User not found in system"
}
```

---

## 3. GET /user
**Get User Data**

**Headers:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200):**
```json
{
  "id": 1,
  "name": "Ivanov Ivan",
  "email": "example@gmail.com"
}
```

**Response (401):**
```json
{
  "error": "Invalid token"
}
```

---

## 4. GET /progress
**Get Academic Progress**

**Headers:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200):**
```json
{
  "summary": {
    "totalScore": 291,
    "totalMaxScore": 350,
    "overallProgress": 83
  },
  "subjects": [
    {
      "name": "Mathematics",
      "currentScore": 125,
      "maxScore": 150,
      "components": [
        {
          "name": "Laboratory Works",
          "score": 45,
          "maxScore": 50,
          "completed": true
        }
      ]
    }
  ]
}
```

**Response (401):**
```json
{
  "error": "Invalid token"
}
```

---

## Authentication
All endpoints except `/auth/login` and `/auth/google` require JWT authentication token in the header:
```
Authorization: Bearer <your_jwt_token_here>

Tokens are valid for 10 hours from issuance.
