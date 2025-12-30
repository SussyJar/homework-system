<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="com.example.model.User" %>
<%@ page import="com.example.dao.HomeworkDAO" %>

<%
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || !"teacher".equals(teacher.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }

    int id = Integer.parseInt(request.getParameter("id"));
    HomeworkDAO dao = new HomeworkDAO();

    if ("POST".equalsIgnoreCase(request.getMethod())) {

        String deadlineStr = request.getParameter("deadline");
        deadlineStr = deadlineStr.replace("T", " ") + ":00";
        Timestamp deadline = Timestamp.valueOf(deadlineStr);

        dao.updateHomework(
            id,
            request.getParameter("title"),
            request.getParameter("description"),
            request.getParameter("difficulty"),
            deadline,
            request.getParameter("format")
        );

        response.sendRedirect("homework");
        return;
    }

    Map<String, Object> h = dao.getHomeworkById(id);
%>

<%@ include file="../layout/header.jsp" %>

<h2>Edit Homework</h2>

<form method="post">
    Title:<br>
    <input type="text" name="title"
           value="<%= h.get("title") %>" required><br><br>

    Description:<br>
    <textarea name="description" rows="4" cols="40"
              required><%= h.get("description") %></textarea><br><br>

    Difficulty:<br>
    <input type="text" name="difficulty"
           value="<%= h.get("difficulty") %>" required><br><br>

    Deadline:<br>
    <input type="datetime-local" name="deadline"
           value="<%= h.get("deadline").toString().replace(" ", "T").substring(0,16) %>"
           required><br><br>

    Format:<br>
    <input type="text" name="format"
           value="<%= h.get("format") %>" required><br><br>

    <button type="submit">Update</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/teacher/homework">Back</a>
<%@ include file="../layout/footer.jsp" %>