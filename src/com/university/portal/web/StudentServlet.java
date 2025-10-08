package com.university.portal.web;

import com.university.portal.dao.StudentDAO;
import com.university.portal.model.Course;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class StudentServlet extends HttpServlet {
    private final StudentDAO studentDAO = new StudentDAO();

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
}



