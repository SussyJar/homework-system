<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<h2>Student Submissions</h2>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<c:if test="${not empty submissions}">
    <table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>Student</th>
        <th>Status</th>
        <th>Submit Time</th>
        <th>Score</th>
        <th>Action</th>
    </tr>

    <c:forEach var="s" items="${submissions}">
        <tr>
            <td>${s.studentName}</td>
            <td>
                <c:choose>
                    <c:when test="${empty s.submissionId}">Not Submitted</c:when>
                    <c:otherwise>Submitted</c:otherwise>
                </c:choose>
            </td>
            <td>${s.submitTime}</td>
            <td>${s.score}</td>
            <td>
                <c:choose>
                    <c:when test="${not empty s.submissionId}">
                        <a href="${pageContext.request.contextPath}/teacher/grade_submission.jsp?submissionId=${s.submissionId}&homeworkId=${homeworkId}">Grade</a>
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </table>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/teacher/homework">Back</a>
<%@ include file="../layout/footer.jsp" %>
