# Online-Fitness-Tracking-Application
This is GUI based project for GUVI. The application will allow users to log their workouts, set fitness goals, and track their progress over time. It will provide personalized fitness recommendations and allow users to join fitness challenges. Administrators will handle user management, oversee fitness content, and maintain system settings.


## Project Overview:-

The Online Fitness Tracking Application is a complete, multi-module fitness management system built using Java (JDK 17) with JDBC–MySQL integration.

This final version extends the Review-1 architecture by implementing all remaining modules, advanced UI screens, admin controls, analytics, and reporting features while maintaining a clean DAO–Service–UI architecture.


##  Project Scope

###  Included Features

- MySQL database integration using JDBC
- MySQL Connector/J (`mysql-connector-j-9.5.0`)
- Complete Model layer implementation
- DAO interfaces with concrete implementations
- Service layer with validation and business logic
- Multithreading for login operations (non-blocking UI)
- Use of Collections and Generics
- Transaction management (commit & rollback)
- Centralized exception and error handling

### 👤 User Features
- Secure login system
- Workout logging and workout history
- User profile management
- Fitness challenges participation
- Challenge history tracking
- User progress tracking
- Access to fitness content
- Data visualization of fitness activities

### 🛠️ Admin Features
- Admin dashboard
- User management
- Challenge management
- Fitness content moderation
- System settings configuration
- Activity log monitoring

### 📊 Analytics & Reports
- Fitness analytics and progress insights
- Data visualization (charts/graphs)
- Export features (PDF / Excel)


##  Project Directory Structure:-
     OnlineFitnessTracker/
     │
     ├── lib/
     │   └── mysql-connector-j-9.5.0.jar
     │       
     ├── sql/
     │   └── fitness_db.sql
     │
     ├── src/
     │   └── com/fitnesstracker/
     │       │
     │       ├── model/
     │       │   ├── User.java
     │       │   |── Workout.java
     |       |   |── ActivityLog .java
     |       |   |── Challenge.java
     |       |   |── FitnessContent
     |       |   |── SystemSetting
     |       |   └── UserChallenge 
     |       |
     │       ├── dao/
     │       │   ├── UserDao.java
     │       │   ├── WorkoutDao.java
     |       |   |── ActivityLogDao.java
     |       |   |── ChallengeDao.java
     |       |   |── FitnessContentDao.java
     |       |   |── SystemSettingDao.java
     |       |   |── UserChallengeDao.java
     |       |   |
     │       │   └── impl/
     │       │       ├── UserDaoImpl.java
     │       │       |── WorkoutDaoImpl.java
     |       |       |── ActivityLogDao.java
     |       |       |── ChallengeDao.java
     |       |       |── FitnessContentDao.java
     |       |       |── SystemSettingDao.java
     |       |       └── UserChallengeDao.java
     |       |   
     |       |
     │       ├── service/
     │       │   ├── AuthService.java
     │       │   ├── WorkoutService.java
     │       │   ├── ActivityLogService.java
     │       │   ├── ChallengeService.java
     │       │   ├── FitnessContentService.java
     │       │   ├── ProfileService.java
     │       │   ├── SystemSettingService.java
     │       │   │── UserChallengeService.java
     │       │   │ 
     │       │   └── impl/
     │       │       ├── AuthServiceImpl.java
     │       │       ├── WorkoutServiceImpl.java
     │       │       ├── ActivityLogServiceImpl.java
     │       │       ├── ChallengeServiceImpl.java
     │       │       ├── FitnessContentServiceImpl.java
     │       │       ├── ProfileServiceImpl.java
     │       │       ├── SystemSettingServiceImpl.java
     │       │       └── UserChallengeServiceImpl.java
     │       │
     │       ├── ui/
     │       |    ├── auth/
     │       │    |    └── LoginFrame.java
     |       |    ├──admin/
     │       │    |    ├── AdminDashboardFrame.java
     │       │    |    ├── AdminChallengeFrame.java
     │       │    |    ├── AdminContentModerationFrame.java
     │       |    |    ├── AdminSystemSettingsFrame.java
     |       |    |    └── AdminActivityLogFrame.java
     │       │    └── user/
     |       |         ├── UserDashboardFrame.java
     |       |         ├── WorkoutLogFrame.java
     |       |         ├── AddWorkoutDialog.java
     |       |         ├── EditWorkoutDialog.java
     │       │         ├── ChallengeListFrame.java
     │       │         ├── ChallengeHistoryFrame.java
     │       │         ├── UserProfileFrame.java
     │       │         └── FitnessContentSubmitFrame.java
     │       │ 
     |       ├── logging/
     |       |    └── ActivityLogger.java
     │       │
     │       ├── util/
     │       │   └── DBConnection.java
     │       │
     │       └── main/
     │           └── MainApp.java
     |
     ├── fitness-db.sql
     └── README.md

## Technology Stack:-
   | Layer                      | Technology                |
   | -------------------------- | ------------------------- |
   | Language                   | Java 17                   |
   | UI Framework               | Java Swing                |
   | Database                   | MySQL 8                   |
   | Connectivity               | JDBC                      |
   | Architecture               | DAO + Service + MVC       |
   | IDE                        | IntelliJ IDEA (non-Maven) |


## Database Schema:-

   Table: users

   | Column                | Type                     | Description           |
   | --------------------- | ------------------------ | --------------------- |
   | id                    | INT (PK, AUTO_INCREMENT) | User ID               |
   | name                  | VARCHAR(100)             | User name             |
   | email                 | VARCHAR(100)             | Unique email          |
   | password              | VARCHAR(100)             | Plain password for R1 |
   | role                  | ENUM("USER")             | Only USER role in R1  |


   Table: workouts

   | Column                | Type                     | Description          |
   | --------------------- | ------------------------ | -------------------- |
   | id                    | INT (PK, AUTO_INCREMENT) | Workout ID           |
   | user_id               | INT FK                   | Ref to users.id      |
   | workout_type          | VARCHAR(50)              | Cardio/Yoga/etc      |
   | duration              | INT                      | Time in minutes      |
   | workout_date          | DATE                     | Date of workout      |




## Features Implemented
   1. Login System(Basic ui)            
       - Swing UI       
       - Email & password validation    
       - Error messages      
       - Successful login → Workout Log Screen     
       - Uses AuthService + UserDao
      

   2. DAO Layer                  
       - UserDao + WorkoutDao       
       -  PreparedStatement (safe from SQL injection)        

   3. Service Layer                
       - Validation of inputs         
       - Business rules           
       -  Simplified logic for Review-1        
      
   4. Multithreading                  
      - Login operation runs in:
      
                                new Thread(this::handleLogin).start();                          
      - Prevents UI freezing.                   

   6. Collections & Generics
      - Workouts fetched as:
        
                 List<Workout> workouts = new ArrayList<>();

   7. Transaction Management
      - Used in WorkoutDaoImpl
        
               conn.setAutoCommit(false)
               conn.commit()
               conn.rollback()

   8. Exception & Error Handling     
       -  dao
       - service
       -  ui

##  How to Run:-         
   -  Install
      - Java 17
      - MySQL 8
      - IntelliJ IDEA
      -  MySQL JDBC Driver (Connector/J)

   - Configure Database
     
            sql/fitness_db.sql
   - Update DB Credential

             src/com/fitnesstracker/util/DBConnection.java
     Set:
     
         URL = "jdbc:mysql://localhost:3306/fitness_db";
         USER = "your_db_username";
         PASSWORD = "your_db_password";
  - Run Application
      - Launch:
        
                 MainApp.java

-👨‍💼 Admin Login
- **Email:** admin@fit.com  
- **Password:** admin123  
---

-👤 User Login
- **Email:** user1@fit.com  
- **Password:** user123  

## OOP Concepts Used:-       
   - Encapsulation
        - Private fields + getters/setters.

  - Abstraction
        - DAO interfaces & Service interfaces.

   - Polymorphism
      - ServiceImpl overrides interface methods.

  - Exception Handling
      - Try-catch across layers.

  - Clean Architecture
     - DAO → Service → UI separation. 

##  Conclusion:-

This project establishes a complete and well-structured architecture for the **Online Fitness Tracking Application** and delivers all essential functionalities required for a full-scale fitness management system.

It demonstrates:
- Clean and maintainable coding practices
- Strong understanding of Object-Oriented Programming (OOP)
- Effective use of JDBC for database connectivity
- Proper architectural planning using DAO and Service layers
- Practical implementation of a responsive Java Swing GUI



