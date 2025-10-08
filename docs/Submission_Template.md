# University Portal (Servlets + JDBC)

Author: <Your Name>  
Student No: <Your Student Number>  
Course/Module: <Course Name>  
Date: <Date>

---

## Table of Contents
1. Database Integration (JDBC)
2. Web Application Logic (Servlets)
3. User Interface (JSP/HTML)
4. Build & Deployment
5. Testing & Results
6. Challenges & Learnings
7. Appendix

---

## 1. Database Integration (JDBC)

### 1.1 Overview
- Database engine: MySQL 8.x  
- Purpose: store Courses, Students, and Student-Course enrollments.

### 1.2 Schema (DDL)
Paste the SQL you executed:

```sql
-- Schema
CREATE DATABASE IF NOT EXISTS university;
USE university;

-- Tables
CREATE TABLE IF NOT EXISTS courses (
  id INT PRIMARY KEY,
  course_code VARCHAR(20),
  course_name VARCHAR(100),
  instructor VARCHAR(100),
  schedule VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS students (
  id INT PRIMARY KEY,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS student_course (
  student_id INT,
  course_id INT
);
```

Optional seed (use alias or simple INSERT):
```sql
INSERT INTO courses (id, course_code, course_name, instructor, schedule) VALUES
(1,'CS101','Intro to CS','Dr. Smith','Mon 10:00'),
(2,'MATH201','Calculus II','Prof. Lee','Tue 12:00'),
(3,'ENG150','Academic Writing','Dr. Patel','Wed 09:00');

INSERT INTO students (id, first_name, last_name, email) VALUES
(1001,'Alex','Mokoena','alex@example.com'),
(1002,'Lerato','Dlamini','lerato@example.com');

INSERT INTO student_course (student_id, course_id) VALUES (1001,1),(1001,2);
```

### 1.3 Setup Steps (MySQL Workbench)
1) Open MySQL Workbench → connect to Local instance.  
2) SCHEMAS (left) → right‑click → Create Schema… → `university` → Apply.  
3) Toolbar → SQL + (New SQL Tab) → select `university` (double‑click so it’s bold).  
4) Paste DDL above → Execute (lightning bolt).  
5) Seed data → Execute.  
6) Verify with `SELECT * FROM courses;`.

Screenshots to include:  
- [ ] SCHEMAS showing `university`  
- [ ] Tables expanded (courses, students, student_course)  
- [ ] Result grid for `SELECT * FROM courses`  

### 1.4 Java DB Access
Key files (excerpts):

Connection factory:
```java
// File: src/com/university/portal/dao/ConnectionFactory.java
package com.university.portal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static String jdbcUrl = "jdbc:h2:mem:university;DB_CLOSE_DELAY=-1";
    private static String jdbcUser = "sa";
    private static String jdbcPassword = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static void configure(String url, String user, String password) {
        jdbcUrl = url;
        jdbcUser = user;
        jdbcPassword = password;
        if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:mysql:")) {
            try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignored) {}
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }
}
```

Courses DAO (SELECT + UPDATE with PreparedStatement):
```java
// File: src/com/university/portal/dao/CourseDAO.java
public class CourseDAO {
    public List<Course> findAll() throws SQLException {
        String sql = "SELECT id, course_code, course_name, instructor, schedule FROM courses";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseCode(rs.getString("course_code"));
                course.setCourseName(rs.getString("course_name"));
                course.setInstructor(rs.getString("instructor"));
                course.setSchedule(rs.getString("schedule"));
                courses.add(course);
            }
            return courses;
        }
    }

    public void updateCourse(int id, String instructor, String schedule) throws SQLException {
        String sql = "UPDATE courses SET instructor = ?, schedule = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, instructor);
            ps.setString(2, schedule);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }
}
```

Student DAO (JOIN):
```java
// File: src/com/university/portal/dao/StudentDAO.java
public class StudentDAO {
    public List<Course> findScheduleByStudentId(int studentId) throws SQLException {
        String sql = "SELECT c.id, c.course_code, c.course_name, c.instructor, c.schedule " +
                     "FROM student_course sc " +
                     "JOIN courses c ON sc.course_id = c.id " +
                     "WHERE sc.student_id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getInt("id"));
                    course.setCourseCode(rs.getString("course_code"));
                    course.setCourseName(rs.getString("course_name"));
                    course.setInstructor(rs.getString("instructor"));
                    course.setSchedule(rs.getString("schedule"));
                    courses.add(course);
                }
                return courses;
            }
        }
    }
}
```

---

## 2. Web Application Logic (Servlets)

### 2.1 Overview
- Multiple servlets handle specific responsibilities and forward to JSPs.

### 2.2 Servlets (excerpts)
CourseServlet:
```java
// File: src/com/university/portal/web/CourseServlet.java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
        List<Course> courses = courseDAO.findAll();
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/WEB-INF/jsp/courses.jsp").forward(req, resp);
    } catch (SQLException e) {
        throw new ServletException(e);
    }
}
```

StudentServlet:
```java
// File: src/com/university/portal/web/StudentServlet.java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String studentIdParam = req.getParameter("studentId");
    if (studentIdParam == null || studentIdParam.isBlank()) {
        resp.sendRedirect(req.getContextPath() + "/");
        return;
    }
    try {
        int studentId = Integer.parseInt(studentIdParam);
        List<Course> schedule = studentDAO.findScheduleByStudentId(studentId);
        req.setAttribute("schedule", schedule);
        req.setAttribute("studentId", studentId);
        req.getRequestDispatcher("/WEB-INF/jsp/student-schedule.jsp").forward(req, resp);
    } catch (NumberFormatException e) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid studentId");
    } catch (SQLException e) {
        throw new ServletException(e);
    }
}
```

FacultyServlet (POST update):
```java
// File: src/com/university/portal/web/FacultyServlet.java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String idParam = req.getParameter("courseId");
    String instructor = req.getParameter("instructor");
    String schedule = req.getParameter("schedule");
    if (idParam == null || instructor == null || schedule == null) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
        return;
    }
    try {
        int id = Integer.parseInt(idParam);
        courseDAO.updateCourse(id, instructor, schedule);
        req.setAttribute("message", "Course updated successfully");
        req.getRequestDispatcher("/WEB-INF/jsp/faculty.jsp").forward(req, resp);
    } catch (NumberFormatException e) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid courseId");
    } catch (SQLException e) {
        throw new ServletException(e);
    }
}
```

Servlet mappings and DB config:
```xml
<!-- File: web/WEB-INF/web.xml -->
<context-param>
  <param-name>db.url</param-name>
  <param-value>jdbc:mysql://localhost:3306/university?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC</param-value>
</context-param>
<context-param>
  <param-name>db.user</param-name>
  <param-value>uni_user</param-value>
</context-param>
<context-param>
  <param-name>db.password</param-name>
  <param-value>change_me!</param-value>
</context-param>
```

Screenshots to include:  
- [ ] NetBeans showing servlet classes  
- [ ] Browser pages for /courses, /student, /faculty

---

## 3. User Interface (JSP/HTML)

### 3.1 Pages
- `web/index.jsp` (navigation + student form)
- `web/WEB-INF/jsp/courses.jsp` (table)
- `web/WEB-INF/jsp/student-schedule.jsp` (table)
- `web/WEB-INF/jsp/faculty.jsp` (form)

### 3.2 Styling
- Global stylesheet: `web/assets/css/styles.css`.

Screenshots to include:  
- [ ] Home page  
- [ ] Courses page  
- [ ] Student schedule (ID 1001)  
- [ ] Faculty update form  

---

## 4. Build & Deployment

### 4.1 Build (Ant)
- Targets:
  - `get-deps` (downloads jars into `lib/`)
  - `war` (creates `dist/UniversityPortal.war`)

### 4.2 Deployment (Tomcat 10+)
- Copy WAR to `<TOMCAT>/webapps/` or deploy via NetBeans Servers panel.

Screenshots to include:  
- [ ] WAR in Tomcat Manager (optional)  
- [ ] Tomcat home and running app  

---

## 5. Testing & Results
Describe and show outcomes for:
- List courses: table renders with seeded rows.  
- View schedule for student `1001`.  
- Faculty updates course and sees confirmation.

Screenshots to include:  
- [ ] /courses  
- [ ] /student?studentId=1001  
- [ ] /faculty (before/after)

---

## 6. Challenges & Learnings
- Servlet API classpath (Jakarta vs javax).  
- Driver configuration (MySQL Connector/J).  
- SQL syntax adjustments (e.g., `VALUES()` deprecation warnings).  
- NetBeans run targets vs WAR deployment.

---

## 7. Appendix

### 7.1 Key Commands
```bash
ant get-deps
ant war
```

### 7.2 Project Structure (short)
```
UniversityPortal/
  src/com/university/portal/... (dao, model, web)
  web/ (index.jsp, WEB-INF/web.xml, JSPs, assets)
  lib/ (jars)
  dist/UniversityPortal.war
  build.xml
```

### 7.3 Versions
- JDK 17  
- Tomcat 10.x  
- MySQL 8.x  
- MySQL Connector/J 8.x  
- NetBeans  

---

Instructions: Insert your screenshots at the marked checkboxes, adjust credentials/paths, and export this Markdown to PDF (or paste into Word and export).
