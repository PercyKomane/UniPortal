package com.university.portal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.university.portal.model.Course;

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



