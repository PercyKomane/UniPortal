package com.university.portal.web;

import com.university.portal.dao.ConnectionFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebListener
public class StartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String url = sce.getServletContext().getInitParameter("db.url");
        String user = sce.getServletContext().getInitParameter("db.user");
        String password = sce.getServletContext().getInitParameter("db.password");
        if (url != null && !url.isBlank()) {
            ConnectionFactory.configure(url, user != null ? user : "", password != null ? password : "");
        }

        boolean usingH2 = (url == null || url.startsWith("jdbc:h2:"));

        if (usingH2) {
            try (Connection c = ConnectionFactory.getConnection()) {
                c.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS courses (" +
                    " id INT PRIMARY KEY, " +
                    " course_code VARCHAR(20), " +
                    " course_name VARCHAR(100), " +
                    " instructor VARCHAR(100), " +
                    " schedule VARCHAR(50) )"
                );

                c.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS students (" +
                    " id INT PRIMARY KEY, " +
                    " first_name VARCHAR(50), " +
                    " last_name VARCHAR(50), " +
                    " email VARCHAR(100) )"
                );

                c.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS student_course (" +
                    " student_id INT, " +
                    " course_id INT )"
                );

                try (ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM courses")) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        c.createStatement().executeUpdate(
                            "INSERT INTO courses (id, course_code, course_name, instructor, schedule) VALUES " +
                            "(1,'CS101','Intro to CS','Dr. Smith','Mon 10:00')," +
                            "(2,'MATH201','Calculus II','Prof. Lee','Tue 12:00')," +
                            "(3,'ENG150','Academic Writing','Dr. Patel','Wed 09:00')"
                        );
                    }
                }

                try (ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM students")) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        c.createStatement().executeUpdate(
                            "INSERT INTO students (id, first_name, last_name, email) VALUES " +
                            "(1001,'Alex','Mokoena','alex@example.com')," +
                            "(1002,'Lerato','Dlamini','lerato@example.com')"
                        );
                    }
                }

                try (PreparedStatement check = c.prepareStatement("SELECT COUNT(*) FROM student_course WHERE student_id = ?")) {
                    check.setInt(1, 1001);
                    try (ResultSet rs = check.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            c.createStatement().executeUpdate(
                                "INSERT INTO student_course (student_id, course_id) VALUES (1001,1),(1001,2)"
                            );
                        }
                    }
                }
            } catch (SQLException ignored) {
            }
        }
    }
}



