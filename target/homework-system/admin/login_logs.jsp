<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Login Logs</h2>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
        </svg>
        Back to Dashboard
    </a>
</div>

<div class="card">
    <div class="card-header">
        <h5 class="mb-0">User Login History</h5>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Log ID</th>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Login Time</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty logs}">
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">No login records found.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="log" items="${logs}">
                                <tr>
                                    <td><span class="badge bg-secondary">${log.logId}</span></td>
                                    <td><strong>${log.username != null ? log.username : '-'}</strong></td>
                                    <td>${log.role}</td>
                                    <td>
                                        <fmt:formatDate value="${log.loginTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>
