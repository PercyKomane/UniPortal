# UniversityPortal (NetBeans Ant Web Application)

A starter Java Servlet/JSP web application with JDBC DAO layer. NetBeans-style Ant project that builds a WAR.

## Requirements
- JDK 17
- A servlet container (e.g., Apache Tomcat 10+) compatible with Jakarta Servlet 5
- Ant (optional; NetBeans uses bundled Ant)

## Structure
- src/ — Java sources (models, DAOs, servlets)
- web/ — JSPs and static web content
- web/WEB-INF/web.xml — servlet mappings and config
- lib/ — third-party jars (e.g., servlet-api when building outside IDE)
- build/ — compiled classes (generated)
- dist/ — packaged WAR (generated)

## Configure Database
By default the app uses an in-memory H2 database via `ConnectionFactory` for quick start. To use MySQL/PostgreSQL:
1. Add the JDBC driver jar to `lib/`.
2. Update `ConnectionFactory.configure(url, user, password)` at startup (e.g., in a context listener) or change defaults in the class.
3. Example MySQL URL: `jdbc:mysql://localhost:3306/university?useSSL=false`.

### Schema (DDL)
Use this minimal schema to test:
```sql
CREATE TABLE courses (
  id INT PRIMARY KEY,
  course_code VARCHAR(20),
  course_name VARCHAR(100),
  instructor VARCHAR(100),
  schedule VARCHAR(50)
);

CREATE TABLE students (
  id INT PRIMARY KEY,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  email VARCHAR(100)
);

CREATE TABLE student_course (
  student_id INT,
  course_id INT
);
```

## Build
From terminal:
```bash
ant war
```
The WAR will be at `dist/UniversityPortal.war`.

## Deploy & Run
- NetBeans: Run/Deploy to your configured server (Tomcat, Payara, etc.).
- Manual: Copy the WAR to your container's deploy directory.

## Endpoints
- `/` index with links
- `/courses` list all courses
- `/student?studentId={id}` view a student's schedule
- `/faculty` GET shows update form; POST updates a course

## MySQL Setup
1. Download MySQL driver and servlet API (if not already):
   ```bash
   ant get-deps
   ```
2. Create a database and tables:
   ```sql
   CREATE DATABASE university;
   USE university;
   -- run the DDL from above
   ```
3. Configure credentials in `web/WEB-INF/web.xml` context-params:
   ```xml
   <context-param>
     <param-name>db.url</param-name>
     <param-value>jdbc:mysql://localhost:3306/university?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC</param-value>
   </context-param>
   <context-param>
     <param-name>db.user</param-name>
     <param-value>root</param-value>
   </context-param>
   <context-param>
     <param-name>db.password</param-name>
     <param-value>your_password</param-value>
   </context-param>
   ```
4. Build and redeploy:
   ```bash
   ant war
   ```
   Copy the new WAR to Tomcat `webapps/`.

