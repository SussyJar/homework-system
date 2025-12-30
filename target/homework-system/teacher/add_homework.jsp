<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Create New Homework</h2>
    <a href="${pageContext.request.contextPath}/teacher/homework" class="btn btn-secondary btn-hover">
        <i class="bi bi-arrow-left me-1"></i>
        Back to Homework List
    </a>
</div>

<div class="card">
    <div class="card-body">
        <form method="post" action="${pageContext.request.contextPath}/teacher/add-homework">
            <div class="mb-3">
                <label for="courseId" class="form-label">Course</label>
                <select id="courseId" name="courseId" class="form-select" required>
                    <option value="" disabled selected>-- Select a Course --</option>
                    <c:forEach var="course" items="${courses}">
                        <option value="${course.courseId}">${course.courseName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label for="title" class="form-label">Title</label>
                <input type="text" id="title" name="title" class="form-control" required>
            </div>

            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea id="description" name="description" class="form-control" rows="5" required></textarea>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="difficulty" class="form-label">Difficulty</label>
                    <select id="difficulty" name="difficulty" class="form-select">
                        <option>Easy</option>
                        <option selected>Medium</option>
                        <option>Hard</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="format" class="form-label">Allowed File Formats</label>
                    <input type="text" id="format" name="format" class="form-control" placeholder="e.g., pdf, zip, docx" required>
                </div>
            </div>

            <div class="mb-4">
                <label for="deadline" class="form-label">Deadline</label>
                <input type="datetime-local" id="deadline" name="deadline" class="form-control" required>
            </div>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary btn-lg btn-hover">
                    <i class="bi bi-plus-circle me-1"></i>
                    Create Homework
                </button>
            </div>
        </form>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>