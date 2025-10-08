<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>University Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css"/>
</head>
<body>
<div class="container">
    <div class="panel">
        <div class="header">
            <h1>University Portal</h1>
            <nav class="nav">
                <a href="<%= request.getContextPath() %>/courses">Courses</a>
                <a href="<%= request.getContextPath() %>/faculty">Faculty</a>
            </nav>
        </div>
        <p>Choose an action:</p>
        <ul>
            <li><a href="<%= request.getContextPath() %>/courses">View all courses</a></li>
            <li>
                <form method="get" action="<%= request.getContextPath() %>/student">
                    <label>Student ID
                        <input name="studentId" required/>
                    </label>
                    <div class="actions">
                        <button class="btn btn-primary" type="submit">View Schedule</button>
                    </div>
                </form>
            </li>
            <li><a href="<%= request.getContextPath() %>/faculty">Faculty tools</a></li>
        </ul>
    </div>
</div>
</body>
</html>



