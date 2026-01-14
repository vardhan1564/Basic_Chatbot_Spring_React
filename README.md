# AI Chatbot Platform (Agent Platform)

A full-stack Chatbot Platform that allows users to create custom AI Agents with specific system prompts (e.g., "Java Strict Tutor") and interact with them in a real-time, structured chat interface.

## 🚀 Key Features
* **JWT Authentication**: Secure user registration and login flows using JSON Web Tokens.
* **Agent Creation**: Users can create and manage multiple AI projects/agents under their account.
* **System Prompt Persistence**: Custom instructions (prompts) are stored and associated with each agent to define its behavior.
* **LLM Integration**: Powered by **Llama 3.1** via the Groq API for lightning-fast, high-performance responses.
* **Markdown Support**: The chat interface renders structured content, including bold text, lists, and dark-themed code blocks.
* **Stateless Reliability**: Implements a session-reset design to ensure high accuracy and a "Smart Mock" fallback for API stability.

## 🛠️ Technology Stack
* **Frontend**: React.js, React-Markdown, CSS3 (Flexbox/Grid).
* **Backend**: Java 17+, Spring Boot, Spring Security (JWT).
* **Database**: MySQL for user and agent persistence.
* **AI Model**: Llama 3.1-70b via Groq Cloud.

## 🏗️ Architecture & Design
The application follows a **Decoupled Client-Server Architecture** to ensure scalability and separation of concerns:

1.  **Stateless Session Design**: To ensure high accuracy and prevent "hallucinations," we implemented a memory-less approach where the AI context is focused solely on the user's current project prompt.
2.  **Security Layer**: A custom JWT Security Filter in Spring Boot intercepts requests to protected routes, verifying the user's token before allowing access to agents or chat history.
3.  **Extensibility**: The system is designed to allow future additions, such as multi-file uploads or advanced analytics, by simply extending the database schema and API controllers.


## 📋 Prerequisites
* Java 17 or higher
* Node.js (v18+)
* MySQL Server
* Groq API Key (Sign up at [Groq Cloud](https://console.groq.com/))

## ⚙️ Setup & Installation

### 1. Database Configuration
1. Create a database named `chatbot_platform` in MySQL.
2. Update your credentials in the backend configuration.

### 2. Backend (Spring Boot)
1. Navigate to the `backend` folder.
2. Open `src/main/resources/application.properties`.
3. Configure your MySQL credentials and add your API key:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/chatbot_platform
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   # API Key for Groq Cloud
   api.groq.key=YOUR_API_KEY_HERE
 4.Run the backend: mvn spring-boot:run
 
## 3. Frontend (React)

1. Navigate to the frontend directory: `cd frontend`
2. Install dependencies: `npm install`
3. Start the dev server: `npm run dev`
4. Access the app at: http://localhost:5173

### Final Submission Notes
This project was developed as a minimal version of a Chatbot Platform, focusing on performance, reliability, and security. It demonstrates the ability to integrate external LLM services with a robust Java backend and a dynamic React frontend.
