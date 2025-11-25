## Online-Fitness-Tracking-Application
The application will allow users to log their workouts, set fitness goals, and track their progress over time. It will provide personalized fitness recommendations and allow users to join fitness challenges. Administrators will handle user management, oversee fitness content, and maintain system settings.


## Project Overview

The Online Fitness Tracker (Review-1 Version) is the foundational stage of a multi-module fitness application.
It includes core architecture, login functionality, DAO–Service structure, database connectivity, error handling, and a minimal workout log screen.

This version is intentionally limited to meet Review-1 requirements 



## 2. Review-1 Scope
 Included

    Login System (Swing + JDBC)
    DB Connection (MySQL + JDBC)
    User & Workout Models
    DAO Interfaces + Implementations
    Service Layer + Validation
    Multithreading (login thread)
    Collections + Generics
    Transaction Management
    Error Handling
    Basic UI (Login + Workout Log)


Not included (Review-2 features)

    Admin Module
    Challenges System
    Activity Logs 
    User Profile
    Fitness Content System
    System Settings 
    Analytics / Graphs
    Exporting / Reports

# 3. Project Directory Structure
     OnlineFitnessTracker/
     │
     ├── sql/
     │   └── fitness_db.sql
     │
     ├── src/
     │   └── com/fitnesstracker/
     │
     │       ├── model/
     │       │   ├── User.java
     │       │   └── Workout.java
     │
     │       ├── dao/
     │       │   ├── UserDao.java
     │       │   ├── WorkoutDao.java
     │       │   └── impl/
     │       │       ├── UserDaoImpl.java
     │       │       └── WorkoutDaoImpl.java
     │
     │       ├── service/
     │       │   ├── AuthService.java
     │       │   ├── WorkoutService.java
     │       │   └── impl/
     │       │       ├── AuthServiceImpl.java
     │       │       └── WorkoutServiceImpl.java
     │
     │       ├── ui/
     │       │   └── auth/
     │       │       └── LoginFrame.java
     │       │
     │       │   └── user/
     │       │       └── WorkoutLogFrame.java
     │
     │       ├── util/
     │       │   └── DBConnection.java
     │
     │       └── main/
     │           └── MainApp.java
     │
     └── README.md

# 4. Technology Stack

    | Layer        | Technology                |
    | ------------ | ------------------------- |
    | Language     | Java 17                   |
    | UI Framework | Java Swing                |
    | Database     | MySQL 8                   |
    | Connectivity | JDBC                      |
    | Architecture | DAO + Service + MVC       |
    | IDE          | IntelliJ IDEA (non-Maven) |


# 5. Database Schema

   Table: users

    | Column   | Type                     | Description           |
    | -------- | ------------------------ | --------------------- |
    | id       | INT (PK, AUTO_INCREMENT) | User ID               |
    | name     | VARCHAR(100)             | User name             |
    | email    | VARCHAR(100)             | Unique email          |
    | password | VARCHAR(100)             | Plain password for R1 |
    | role     | ENUM("USER")             | Only USER role in R1  |


   Table: workouts

    | Column       | Type                     | Description     |
    | ------------ | ------------------------ | --------------- |
    | id           | INT (PK, AUTO_INCREMENT) | Workout ID      |
    | user_id      | INT FK                   | Ref to users.id |
    | workout_type | VARCHAR(50)              | Cardio/Yoga/etc |
    | duration     | INT                      | Time in minutes |
    | workout_date | DATE                     | Date of workout |




# 6. Features Implemented

   1. Login System
          Swing UI
          Email & password validation
          Error messages
          Successful login → Workout Log Screen
          Uses AuthService + UserDao

   2. DAO Layer
          UserDao + WorkoutDao
          PreparedStatement (safe from SQL injection)

   3. Service Layer
          Validation of inputs
          Business rules
          Simplified logic for Review-1
      
   4. Multithreading
          Login operation runs in:
                   new Thread(this::handleLogin).start();
          Prevents UI freezing.

   5. Collections & Generics
          Workouts fetched as:
                   List<Workout> workouts = new ArrayList<>();

   6. Transaction Management
         Used in WorkoutDaoImpl:
         conn.setAutoCommit(false);
         conn.commit();
         conn.rollback();

   7. Exception & Error Handling
          DAO
          Service
          UI

# 7. How to Run
Step 1 — Install

Java 17

MySQL 8

IntelliJ IDEA

MySQL JDBC Driver (Connector/J)

Step 2 — Configure Database
 sql/fitness_db.sql
Step 3 — Update DB Credentials

Edit:
src/com/fitnesstracker/util/DBConnection.java

Set:
URL, USER, PASSWORD
Step 4 — Run Application

Launch:
MainApp.java
# 8. OOP Concepts Used
Encapsulation

Private fields + getters/setters.

Abstraction

DAO interfaces & Service interfaces.

Polymorphism

ServiceImpl overrides interface methods.

Exception Handling

Try-catch across layers.

Clean Architecture

DAO → Service → UI separation.

# 9.Future Scope (For Review-2)

Review-2 will add:

Admin Dashboard

Challenges

User Progress Tracking

Fitness Content System

System Settings

User Profile

Activity Logs

Data Visualization

Export Features (PDF/Excel)

# 10. Conclusion

This Review-1 version establishes the complete architecture and essential functionality needed for the full Online Fitness Tracker system.
It demonstrates:

Clean coding style

OOP mastery

JDBC understanding

Architectural planning

Practical GUI development

Perfectly aligned with Review-1 expectations, ready for final expansion in Review-2.



