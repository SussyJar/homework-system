<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<c:if test="${not empty submission}">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Grade Submission</h2>
        <a href="${pageContext.request.contextPath}/teacher/submissions?homeworkId=${submission.homeworkId}" class="btn btn-secondary btn-hover">
            <i class="bi bi-arrow-left"></i> Back to Submissions
        </a>
    </div>

    <div class="card">
        <div class="card-header">
            <div class="d-flex justify-content-between">
                <div>
                    <strong>Homework:</strong>
                    <span class="text-primary">${submission.homeworkTitle}</span>
                </div>
                <div>
                    <strong>Student:</strong>
                    <span class="text-primary">${submission.studentName}</span>
                </div>
            </div>
        </div>
        <div class="card-body">
            <div class="mb-4 p-3 bg-light rounded">
                <h5>Submitted File</h5>
                <p>
                    <i class="bi bi-file-earmark-arrow-down"></i>
                    <a href="${pageContext.request.contextPath}/uploads/${submission.filePath}" target="_blank">
                        <c:out value="${submission.filePath}"/>
                    </a>
                </p>
                <small class="text-muted">Submitted at: ${submission.submitTime}</small>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/teacher/grade-submission">
                <input type="hidden" name="submissionId" value="${submission.submissionId}">
                <input type="hidden" name="homeworkId" value="${submission.homeworkId}">

                <div class="mb-3">
                    <label for="score" class="form-label">Score (0-100):</label>
                    <input type="number" id="score" name="score" class="form-control" min="0" max="100"
                           value="<c:out value='${submission.score}'/>" required>
                </div>

                <div class="mb-3">
                    <label for="feedback" class="form-label">Rubric / Feedback:</label>
                    <textarea id="feedback" name="feedback" class="form-control" rows="5"
                              placeholder="Explain why the student got this score..."
                              required><c:out value='${submission.feedback}'/></textarea>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-lg btn-hover">
                        <i class="bi bi-save"></i> Save Grade
                    </button>
                </div>
            </form>
        </div>
    </div>
</c:if>

<%@ include file="../layout/footer.jsp" %>