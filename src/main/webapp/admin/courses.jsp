<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Manage Courses</h2>
    <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-secondary">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
        </svg>
        Back to Dashboard
    </a>
</div>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">All Courses</h5>
        <a href="${pageContext.request.contextPath}/admin/add_course.jsp" class="btn btn-primary btn-sm">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-lg" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2Z"/>
            </svg>
            Add New Course
        </a>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Course</th>
                        <th>Teacher</th>
                        <th>Students</th>
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${courses}">
                        <tr>
                            <td><strong>${c.courseName}</strong></td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty c.teacher}">
                                        <span class="text-muted fst-italic">Not assigned</span>
                                    </c:when>
                                    <c:otherwise>${c.teacher}</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${c.students}</td>
                            <td class="text-end">
                                <div class="btn-group btn-group-sm">
                                    <a href="${pageContext.request.contextPath}/admin/assign_teacher?courseId=${c.courseId}" class="btn btn-outline-primary">Assign Teacher</a>
                                    <a href="${pageContext.request.contextPath}/admin/assign_student?courseId=${c.courseId}" class="btn btn-outline-secondary">Assign Student</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>