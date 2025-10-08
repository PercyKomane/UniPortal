package com.university.portal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.university.portal.model.Course;

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



