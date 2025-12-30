<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Edit Homework</h2>
    <a href="${pageContext.request.contextPath}/teacher/homework" class="btn btn-secondary btn-hover">
        <i class="bi bi-arrow-left me-1"></i>
        Back to Homework List
    </a>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<c:if test="${not empty homework}">
    <div class="card">
        <div class="card-header">
            Editing: <span class="fw-bold text-primary">${homework.title}</span>
        </div>
        <div class="card-body">
            <form method="post" action="${pageContext.request.contextPath}/teacher/edit-homework">
                <input type="hidden" name="id" value="${homework.homeworkId}">

                <div class="mb-3">
                    <label for="title" class="form-label">Title</label>
                    <input type="text" id="title" name="title" class="form-control" value="<c:out value='${homework.title}'/>" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Description</label>
                    <textarea id="description" name="description" class="form-control" rows="5" required><c:out value='${homework.description}'/></textarea>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="difficulty" class="form-label">Difficulty</label>
                        <input type="text" id="difficulty" name="difficulty" class="form-control" value="<c:out value='${homework.difficulty}'/>" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="format" class="form-label">Allowed File Format (e.g., pdf, zip)</label>
                        <input type="text" id="format" name="format" class="form-control" value="<c:out value='${homework.format}'/>" required>
                    </div>
                </div>

                <div class="mb-4">
                    <label for="deadline" class="form-label">Deadline</label>
                    <input type="datetime-local" id="deadline" name="deadline" class="form-control" value="${homework.formattedDeadline}" required>
                </div>

                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                    <button type="submit" class="btn btn-primary btn-hover px-4">
                        <i class="bi bi-save me-1"></i>
                        Update Homework
                    </button>
                </div>
            </form>
        </div>
    </div>
</c:if>

<%@ include file="../layout/footer.jsp" %>