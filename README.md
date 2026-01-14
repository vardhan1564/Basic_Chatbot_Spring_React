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
