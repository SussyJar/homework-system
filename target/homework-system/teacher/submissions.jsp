<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="../layout/header.jsp" %>

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Student Submissions for: <span class="text-primary">${homeworkTitle}</span></h2>
    <a href="${pageContext.request.contextPath}/teacher/homework" class="btn btn-secondary btn-hover">
        <i class="bi bi-arrow-left me-1"></i>
        Back to Homework
    </a>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<div class="card">
    <div class="card-header">
        Submissions List
    </div>
    <div class="card-body">
        <c:if test="${empty submissions}">
            <p class="text-center text-muted">No submissions found for this assignment yet.</p>
        </c:if>
        <c:if test="${not empty submissions}">
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th scope="col">Student</th>
                            <th scope="col">Status</th>
                            <th scope="col">Submit Time</th>
                            <th scope="col">Score</th>
                            <th scope="col" class="text-center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${submissions}">
                            <tr>
                                <td>${s.studentName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty s.submissionId}">
                                            <span class="badge bg-danger">Not Submitted</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-success">Submitted</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${not empty s.submitTime}">
                                        <fmt:formatDate value="${s.submitTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </c:if>
                                    <c:if test="${empty s.submitTime}">-</c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${s.score != null && s.score >= 0}">${s.score}</c:when>
                                        <c:otherwise><span class="text-muted">Not Graded</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${not empty s.submissionId}">
                                            <a href="${pageContext.request.contextPath}/teacher/grade-submission?submissionId=${s.submissionId}&homeworkId=${homeworkId}" class="btn btn-sm btn-outline-primary btn-hover">
                                                <i class="bi bi-pencil-square me-1"></i> Grade
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">-</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>
