package com.university.portal.web;

import com.university.portal.dao.CourseDAO;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FacultyServlet extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/faculty.jsp").forward(req, resp);
    }

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
}



