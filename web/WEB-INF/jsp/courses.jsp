<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.university.portal.model.Course" %>
<%
    @SuppressWarnings("unchecked")
    List<Course> courses = (List<Course>) request.getAttribute("courses");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>All Courses</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css"/>
</head>
<body>
<div class="container">
    <div class="panel">
        <div class="header">
            <h1>All Courses</h1>
            <a class="btn" href="<%= request.getContextPath() %>/">Back</a>
        </div>
        <table class="table">
            <tr>
                <th>ID</th><th>Code</th><th>Name</th><th>Instructor</th><th>Schedule</th>
            </tr>
            <%
                if (courses != null) {
                    for (Course c : courses) {
            %>
            <tr>
                <td><%= c.getId() %></td>
                <td><%= c.getCourseCode() %></td>
                <td><%= c.getCourseName() %></td>
                <td><%= c.getInstructor() %></td>
                <td><%= c.getSchedule() %></td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</div>
</body>
</html>



