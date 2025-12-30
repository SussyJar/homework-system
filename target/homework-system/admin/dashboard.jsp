<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Admin Dashboard</h2>
    <span class="badge bg-primary fs-6">Welcome, ${sessionScope.user.username}!</span>
</div>

<!-- Quick Actions -->
<div class="row g-3">
    <!-- Manage Users -->
    <div class="col-md-6 col-lg-4">
        <div class="card dashboard-card h-100">
            <div class="card-body text-center">
                <div class="mb-3 text-primary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-people" viewBox="0 0 16 16">
                        <path d="M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1h8Zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022ZM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0ZM6.957 11.657c.033-.161.109-.336.217-.528C7.56 10.59 8.51 10 10 10c.863 0 1.62.245 2.213.658.108.192.184.367.217.528H6.957ZM4.5 8a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5Zm0-1a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3Zm1-1a.5.5 0 0 1 .5.5v2.5a.5.5 0 0 1-1 0V6.5a.5.5 0 0 1 .5-.5Z"/>
                    </svg>
                </div>
                <h5 class="card-title">Manage Users</h5>
                <p class="card-text text-muted small">Add, edit, or remove user accounts.</p>
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary w-100">Go</a>
            </div>
        </div>
    </div>

    <!-- Manage Courses -->
    <div class="col-md-6 col-lg-4">
        <div class="card dashboard-card h-100">
            <div class="card-body text-center">
                <div class="mb-3 text-success">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-book" viewBox="0 0 16 16">
                        <path d="M1 2.828c.885-.37 2.154-.769 3.388-.893 1.33-.134 2.458.063 3.112.752v9.746c-.935-.53-2.12-.603-3.213-.493-1.18.12-2.37.461-3.287.811V2.828zm7.5-.141c.654-.689 1.782-.886 3.112-.752 1.234.124 2.503.523 3.388.893v9.923c-.918-.35-2.107-.692-3.287-.81-1.094-.111-2.278-.039-3.213.492V2.687zM8 1.783C7.015.936 5.587.81 4.287.94c-1.514.153-3.042.672-3.994 1.105A.5.5 0 0 0 0 2.5v11a.5.5 0 0 0 .707.455c.882-.4 2.303-.881 3.68-1.02 1.409-.142 2.59.087 3.223.877a.5.5 0 0 0 .78 0c.633-.79 1.814-1.019 3.222-.877 1.378.139 2.8.62 3.681 1.02A.5.5 0 0 0 16 13.5v-11a.5.5 0 0 0-.293-.455c-.952-.433-2.48-.952-3.994-1.105C10.413.809 8.985.936 8 1.783z"/>
                    </svg>
                </div>
                <h5 class="card-title">Manage Courses</h5>
                <p class="card-text text-muted small">Create courses and assign roles.</p>
                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-success w-100">Go</a>
            </div>
        </div>
    </div>

    <!-- Login Logs -->
    <div class="col-md-6 col-lg-4">
        <div class="card dashboard-card h-100">
            <div class="card-body text-center">
                <div class="mb-3 text-warning">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-card-list" viewBox="0 0 16 16">
                        <path d="M14.5 3a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h13zm-13-1A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-13z"/>
                        <path d="M5 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 5 8zm0-2.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm0 5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5zm-1-5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zM4 8a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0zm0 2.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0z"/>
                    </svg>
                </div>
                <h5 class="card-title">Login Logs</h5>
                <p class="card-text text-muted small">Review user login activity.</p>
                <a href="${pageContext.request.contextPath}/admin/login_logs" class="btn btn-warning w-100">Go</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>