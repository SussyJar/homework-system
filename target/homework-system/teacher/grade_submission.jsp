<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.dao.SubmissionDAO" %>
<%@ page import="com.example.model.User" %>

<%
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || !"teacher".equals(teacher.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }

    int submissionId =
        Integer.parseInt(request.getParameter("submissionId"));
    int homeworkId =
        Integer.parseInt(request.getParameter("homeworkId"));

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        int score =
            Integer.parseInt(request.getParameter("score"));
        String feedback =
            request.getParameter("feedback");

        SubmissionDAO dao = new SubmissionDAO();
        dao.gradeSubmission(submissionId, score, feedback);

        response.sendRedirect(
            "submissions?homeworkId=" + homeworkId
        );
        return;
    }
%>

<%@ include file="../layout/header.jsp" %>

<h2>Grade Submission</h2>

<form method="post">
    Score (0-100):<br>
    <input type="number" name="score" min="0" max="100" required><br><br>

    Rubric / Feedback:<br>
    <textarea name="feedback" rows="5" cols="50"
     placeholder="Explain why the student got this score..."
     required></textarea><br><br>

    <button type="submit">Save Grade</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/teacher/submissions.jsp?homeworkId=<%= homeworkId %>">Back</a>
<%@ include file="../layout/footer.jsp" %>