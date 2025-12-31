<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Set Reminder</h2>
    <a href="${pageContext.request.contextPath}/student/homework?courseId=${param.courseId}" class="btn btn-outline-secondary">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
        </svg>
        Back to Homework
    </a>
</div>

<div class="card">
    <div class="card-body">
        <h5 class="card-title">Set a reminder for your homework</h5>
        <form action="${pageContext.request.contextPath}/student/set-reminder" method="post">
            <input type="hidden" name="homeworkId" value="${param.homeworkId}">
            <input type="hidden" name="courseId" value="${param.courseId}">
            <div class="mb-3">
                <label for="remindAt" class="form-label">Reminder Date and Time</label>
                <input type="datetime-local" class="form-control" id="remindAt" name="remindAt" required>
            </div>
            <button type="submit" class="btn btn-primary">Set Reminder</button>
        </form>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>
