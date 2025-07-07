# 🎓 Student Attendance Management System
 
A secure and role-based Spring Boot web application for managing student attendance, designed to streamline attendance tracking and reporting across admin, teacher, and student roles.
 
## 📌 Project Objective
 
Build a secure, scalable, and role-based backend system to:
 
- Register and manage student profiles
- Record daily attendance
- Enable role-based access for Admins, Teachers, and Students
- Provide real-time attendance tracking and summary views
 
---
 
## 🧰 Tech Stack
 
| Layer        | Technology             |
|--------------|------------------------|
| **Backend**  | Java 17+, Spring Boot 3+ |
| **Security** | Spring Security, JWT    |
| **Database** | PostgreSQL              |
| **ORM**      | Hibernate (JPA)         |
| **Build Tool** | Maven or Gradle       |
| **API Testing** | Swagger / Postman    |
 
---
 
## 🚀 Features
 
### ✅ Authentication & Authorization
 
- **User Roles:** Admin, Teacher, Student
- **JWT-based Login & Token Management**
- **Role-Based Access Control (RBAC)**:
  - **Admin:** Full access (CRUD operations)
  - **Teacher:** View-only access to class attendance
  - **Student:** View-only access to personal attendance
 
### 👤 Student Module
 
- Register new students (Admin)
- View student profile and their attendance records (All roles as per access)
 
### 📝 Attendance Module
 
- Mark attendance by:
  - Date
  - Class
  - Individual student
- View attendance:
  - By student
  - By date
  - By month
- Edit/Delete attendance (Admin only)
 
### 🛠️ Admin Module
 
- CRUD operations for student records
- Class-wise attendance reports
- Dashboard: Summary with:
  - Student name
  - Attendance/Leave count
 
---
 
## 📂 Project Structure
 
src/
├── config/ # Spring Security and JWT configuration
├── controller/ # REST APIs
├── dto/ # Data Transfer Objects
├── entity/ # JPA Entities
├── repository/ # JpaRepository interfaces
├── service/ # Business logic
├── utils/ # Utility classes (e.g., JWT utils)
└── Application.java # Main class
 
yaml
Copy
Edit
 
---
 
## 🔐 Authentication Flow
 
1. **User Registration** (Admin or Student)
2. **Login**: Returns JWT token
3. **Token Validation**: Attached to header (`Authorization: Bearer <token>`)
4. **Role Check**: Intercepts requests and validates against user role
 
---
 
## 🧪 API Testing
 
You can test all API endpoints using:
 
- **Postman Collection** (to be added in repo)
- **Swagger UI** at `/swagger-ui/index.html` (enabled via SpringDoc)
 
---
 
## 🏁 Getting Started
 
### Prerequisites
 
- Java 17+
- PostgreSQL
- Maven or Gradle
- IDE (e.g., IntelliJ or VSCode)
 
### Clone the repository
 
```bash
git clone https://github.com/shreedar636/student-attendance-system.git
cd student-attendance-system
Setup Database
Create PostgreSQL database: attendance_db
 
Update application.properties with your DB credentials
 
Run the App
bash
Copy
Edit
./mvnw spring-boot:run
# or if using Gradle
./gradlew bootRun
Access Swagger
bash
Copy
Edit
http://localhost:8080/swagger-ui/index.html
📬 Contact
💼 LinkedIn: Shreedar Ramanathan
 
💻 GitHub: @shreedar636
