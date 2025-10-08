<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Faculty Tools</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css"/>
</head>
<body>
<div class="container">
    <div class="panel">
        <div class="header">
            <h1>Faculty: Update Course</h1>
            <a class="btn" href="<%= request.getContextPath() %>/">Back</a>
        </div>
        <% String message = (String) request.getAttribute("message"); if (message != null) { %>
            <div class="alert"><%= message %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/faculty">
            <div>
                <label>Course ID
                    <input name="courseId" required/>
                </label>
            </div>
            <div>
                <label>Instructor
                    <input name="instructor" required/>
                </label>
            </div>
            <div>
                <label>Schedule
                    <input name="schedule" required/>
                </label>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">Update</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>



