<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="com.example.model.User" %>
<%@ page import="com.example.dao.HomeworkDAO" %>
<%@ page import="com.example.dao.CourseDAO" %>

<%
    // =========================
    // AUTH CHECK
    // =========================
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || !"teacher".equals(teacher.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }

    HomeworkDAO hwDAO = new HomeworkDAO();
    CourseDAO courseDAO = new CourseDAO();

    // =========================
    // HANDLE POST (CREATE)
    // =========================
    if ("POST".equalsIgnoreCase(request.getMethod())) {

        int courseId =
            Integer.parseInt(request.getParameter("courseId"));

        String title =
            request.getParameter("title");

        String description =
            request.getParameter("description");

        String difficulty =
            request.getParameter("difficulty");

        String format =
            request.getParameter("format");

        String deadlineStr =
            request.getParameter("deadline");

        Timestamp deadline =
            Timestamp.valueOf(deadlineStr.replace("T", " ") + ":00");

        hwDAO.createHomework(
            teacher.getUserId(),
            courseId,
            title,
            description,
            difficulty,
            deadline,
            format
        );

        response.sendRedirect("homework");
        return;
    }

    // =========================
    // LOAD COURSES FOR DROPDOWN
    // =========================
    List<Map<String,Object>> courses =
        courseDAO.getCoursesByTeacher(teacher.getUserId());
%>

<%@ include file="../layout/header.jsp" %>

<h1>Create Homework</h1>

<form method="post">

    <!-- COURSE -->
    <label>Course:</label><br>
    <select name="courseId" required>
        <option value="">-- Select Course --</option>
        <% for (Map<String,Object> c : courses) { %>
            <option value="<%= c.get("courseId") %>">
                <%= c.get("courseName") %>
            </option>
        <% } %>
    </select>
    <br><br>

    <!-- TITLE -->
    <label>Title:</label><br>
    <input type="text" name="title" required>
    <br><br>

    <!-- DESCRIPTION -->
    <label>Description:</label><br>
    <textarea name="description" rows="4" cols="50"></textarea>
    <br><br>

    <!-- DIFFICULTY -->
    <label>Difficulty:</label><br>
    <select name="difficulty">
        <option value="Easy">Easy</option>
        <option value="Medium">Medium</option>
        <option value="Hard">Hard</option>
    </select>
    <br><br>

    <!-- DEADLINE -->
    <label>Deadline:</label><br>
    <input type="datetime-local" name="deadline" required>
    <br><br>

    <!-- FILE FORMAT -->
    <label>Allowed File Format:</label><br>
    <input type="text" name="format"
           placeholder="pdf,zip,docx" required>
    <br><br>

    <button type="submit">Create Homework</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/teacher/homework">Back to Manage Homework</a>
<%@ include file="../layout/footer.jsp" %>