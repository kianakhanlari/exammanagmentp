# Exam Management System

## Overview

Exam Management System is a console-based application designed for creating, managing, and conducting educational examinations.

The system supports three primary roles:

* Administrator
* Teacher
* Student

The application follows an object-oriented and layered architecture approach and provides facilities for user management, course management, exam creation, question banks, and exam grading.

This repository contains the implementation of **Phase 1** of the project.

---

# Phase 1 Features

## User Registration

The system allows users to register as:

* Student
* Teacher

During registration, users must select their role.

After successful registration, the user status becomes:

```text
PENDING_APPROVAL
```

and the user cannot access the system until approved by the administrator.

---

## User Approval by Administrator

The administrator has access to all registered users.

Administrator capabilities:

* View all registered users
* Approve pending registrations
* Edit user information
* Change user roles
* Manage user status

After approval, the user status changes to:

```text
APPROVED
```

---

## Search and Filter Users

The administrator can search and filter users using different criteria:

* First Name
* Last Name
* Username
* Role (Student / Teacher)
* Registration Status

This functionality helps administrators efficiently manage large numbers of users.

---

## Course Management

The administrator can create and manage courses.

Each course contains at least:

| Field        | Description           |
| ------------ | --------------------- |
| Course ID    | Unique Identifier     |
| Course Title | Course Name           |
| Start Date   | Course Beginning Date |
| End Date     | Course Ending Date    |

Administrator capabilities:

* Create Course
* Edit Course
* Delete Course
* View All Courses

---

## Assign Teacher to Course

Each course can have only one teacher.

Requirements:

* Teacher must already be registered in the system.
* Teacher must be approved by the administrator.
* One teacher may teach multiple courses.

Administrator capabilities:

* Assign Teacher to Course
* Replace Course Teacher
* Remove Teacher from Course

---

## Assign Students to Course

Students can be added to courses after registration and approval.

Administrator capabilities:

* Add Student to Course
* Remove Student from Course
* Edit Course Memberships

A student can participate in multiple courses.

---

## Course Participants

The administrator can view participants of a course separately.

### Teacher List

Displays:

* Teacher Name
* Teacher Information

### Student List

Displays:

* Student Name
* Student Information

This allows complete monitoring of course memberships.

---

# Planned Features (Next Phases)

The following features are planned for future phases:

## Exam Management

* Create Exams
* Edit Exams
* Delete Exams

## Question Bank

* Multiple Choice Questions
* Descriptive Questions
* Reusable Question Bank

## Exam Configuration

* Default Score for Questions
* Exam Scheduling
* Exam Duration Management

## Exam Participation

* Student Exam Submission
* Automatic Grading
* Manual Grading

## Reporting

* Student Scores
* Course Reports
* Exam Statistics

---

# System Roles

## Administrator

Responsibilities:

* User Approval
* User Management
* Course Management
* Teacher Assignment
* Student Assignment

---

## Teacher

Responsibilities (Future Phases):

* Create Exams
* Manage Questions
* Grade Exams

---

## Student

Responsibilities (Future Phases):

* Participate in Courses
* Take Exams
* View Results

---

# Project Architecture

The project follows a layered architecture.

```text
Presentation Layer
        │
        ▼
Service Layer
        │
        ▼
Repository Layer
        │
        ▼
Database Layer
```

### Presentation Layer

Handles:

* Console Menus
* User Interaction

### Service Layer

Handles:

* Business Logic
* Validation Rules

### Repository Layer

Handles:

* Data Persistence
* Database Operations

### Database Layer

Stores application data.

---

# Main Entities

## User

Common user information:

* id
* firstName
* lastName
* username
* password
* role
* status

---

## Student

Student-specific information.

---

## Teacher

Teacher-specific information.

---

## Course

Contains:

* courseId
* title
* startDate
* endDate
* assignedTeacher
* enrolledStudents

---

# Enumerations

The project uses enums for fixed values.

Example:

```java
UserRole
```

```java
Student
Teacher
Admin
```

```java
UserStatus
```

```java
PendingApproval
Approved
```

Using enums improves:

* Readability
* Type Safety
* Maintainability

---

# Technologies

* Java
* Object-Oriented Programming (OOP)
* Collection Framework
* Layered Architecture
* Maven

---

# Project Structure

```text
src
├── entity
├── repository
├── service
├── validation
├── menu
├── util
└── Main.java
```

---

# How to Run

Clone the repository:

```bash
git clone <repository-url>
```

Build the project:

```bash
mvn clean install
```

Run the application:

```bash
mvn exec:java
```

---

# Implemented Requirements Checklist

✔ Registration as Student

✔ Registration as Teacher

✔ Pending Approval Workflow

✔ User Approval by Administrator

✔ User Editing

✔ Role Management

✔ Search and Filtering

✔ Course Creation

✔ Teacher Assignment

✔ Student Assignment

✔ View Course Participants

✔ Layered Architecture

---

# Author

University Project – Exam Management System

Phase 1: User & Course Management
