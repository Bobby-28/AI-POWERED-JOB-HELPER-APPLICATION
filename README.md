# 🤖 AI-Powered Job Helper Application

The **AI-Powered Job Helper Application** is a smart, microservices-based system designed to assist job seekers with their job application journey — from parsing resumes to tracking applications, evaluating job compatibility, and generating cover letters — all using AI.

---

## 📌 Key Features

- Upload and parse resumes (PDF)
- Track job application status via email monitoring
- Evaluate job compatibility based on resume and job description
- Generate AI-powered cover letters
- Discover compatible job listings using resume data

---

## 🧱 Architecture Overview

This project is built using a microservices architecture with a mix of Java (Spring Boot), Node.js, and Python (FastAPI), and communicates using **REST APIs** and **Apache Kafka** for asynchronous event handling.

Each service is registered in a central **Eureka Service Registry** and all requests go through the **API Gateway**.

Client → API Gateway → Microservices
↳ User
↳ Resume
↳ Application
↳ Cover Letter
↳ Job Search
↳ Token
↳ Kafka Consumers

markdown
Copy
Edit

---

## 📦 Microservices Breakdown

### ✅ Completed

| Service            | Language     | Description |
|--------------------|--------------|-------------|
| **User Service**        | Java Spring Boot | Handles user registration & login |
| **Token Service**       | Java Spring Boot | Generates and manages JWT & refresh tokens |
| **User Consumer**       | Java Spring Boot | Kafka consumer that stores user data to reduce load on User Service |
| **Resume Service**      | Node.js          | Parses uploaded resumes using OpenAI and stores structured data |
| **API Gateway**         | Java Spring Boot | Central entry point for all services |
| **Registry Service**    | Java Spring Boot | Eureka-based service registry for microservice discovery |

### 🛠️ In Progress / Planned

| Service            | Language     | Description |
|--------------------|--------------|-------------|
| **Application Tracker** | Python (FastAPI) | Monitors email for application updates and checks job compatibility |
| **Cover Letter Generator** | Python (FastAPI) | Generates AI-powered cover letters using resume + job description |
| **Job Search Service** | Node.js          | Searches compatible jobs using public APIs and user resume data |

---

## 🔧 Technologies Used

- **Java + Spring Boot**
- **Node.js + Express**
- **Python + FastAPI**
- **MongoDB**
- **Apache Kafka**
- **Eureka Discovery Server**
- **OpenAI GPT-4o**
- **JWT Authentication**
- **Multer for file handling**
- **Docker (planned)**

---

## 🧠 Resume Parsing Microservice (Deep Dive)

The `Resume Microservice` allows users to upload resumes and uses OpenAI to extract:

- Name
- Skills (as array)
- Education
- Experience

📁 It uses `Multer` for file uploads, converts the file to base64, and sends it to **OpenAI's GPT-4o** with a structured prompt. The parsed data is saved in MongoDB, and is accessible to other services.

---

## 🚀 How to Run (Locally)

> You’ll need Docker, Node.js, Java 17+, MongoDB, Python (for upcoming services), and an OpenAI API key.

1. **Clone the repo**
   ```bash
   git clone https://github.com/yourusername/ai-powered-job-helper-application.git
   cd ai-powered-job-helper-application
Set up environment variables

.env in each service with:

ini
Copy
Edit
PORT=...
MONGODB_URI=...
OPENAI_API_KEY=...
JWT_SECRET=...
Run each microservice

Use npm, mvn spring-boot:run, or uvicorn for respective services

Start Eureka and API Gateway

Eureka: localhost:8761 to monitor registered services

🌟 Upcoming Features
OAuth/Gmail integration for email-based tracking

Kafka for all async communication

WebSocket support for live status updates

AI-based job preference matching

🙌 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss your idea.

📫 Contact
Developed by Aditya Bedi
