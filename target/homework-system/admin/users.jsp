<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Manage Users</h2>
    <a href="${pageContext.request.contextPath}/admin" class="btn btn-outline-secondary">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
        </svg>
        Back to Dashboard
    </a>
</div>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">All Users</h5>
        <a href="${pageContext.request.contextPath}/admin/create_user.jsp" class="btn btn-primary btn-sm">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-lg" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2Z"/>
            </svg>
            Add New User
        </a>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Name</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="userRow" items="${users}">
                        <c:set var="isAdminRow" value="${userRow.role eq 'admin'}" />
                        <tr>
                            <td><span class="badge bg-secondary">${userRow.userId}</span></td>
                            <td><strong>${userRow.username}</strong></td>
                            <td>${userRow.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${userRow.role == 'admin'}"><span class="badge bg-danger">Admin</span></c:when>
                                    <c:when test="${userRow.role == 'teacher'}"><span class="badge bg-info">Teacher</span></c:when>
                                    <c:otherwise><span class="badge bg-light text-dark">Student</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${userRow.status == 'active'}"><span class="badge bg-success">Active</span></c:when>
                                    <c:otherwise><span class="badge bg-warning">Disabled</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end">
                                <c:choose>
                                    <c:when test="${!isAdminRow}">
                                        <div class="btn-group btn-group-sm">
                                            <a href="${pageContext.request.contextPath}/admin/edit_user?userId=${userRow.userId}" class="btn btn-outline-primary">Edit</a>
                                            <c:choose>
                                                <c:when test="${userRow.status eq 'active'}">
                                                    <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                                        <input type="hidden" name="action" value="disable" />
                                                        <input type="hidden" name="userId" value="${userRow.userId}" />
                                                        <button type="submit" class="btn btn-outline-warning">Disable</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form method="post" action="${pageContext.request.contextPath}/admin/users" style="display:inline;">
                                                        <input type="hidden" name="action" value="enable" />
                                                        <input type="hidden" name="userId" value="${userRow.userId}" />
                                                        <button type="submit" class="btn btn-outline-success">Enable</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted fst-italic">Protected</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>