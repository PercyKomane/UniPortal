<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.university.portal.model.Course" %>
<%
    @SuppressWarnings("unchecked")
    List<Course> schedule = (List<Course>) request.getAttribute("schedule");
    Integer studentId = (Integer) request.getAttribute("studentId");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Student Schedule</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css"/>
</head>
<body>
<div class="container">
    <div class="panel">
        <div class="header">
            <h1>Schedule for Student #<%= studentId %></h1>
            <a class="btn" href="<%= request.getContextPath() %>/">Back</a>
        </div>
        <table class="table">
            <tr>
                <th>Code</th><th>Name</th><th>Instructor</th><th>Schedule</th>
            </tr>
            <%
                if (schedule != null) {
                    for (Course c : schedule) {
            %>
            <tr>
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



