<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="java.util.*" %>
<%@ page import="com.example.model.User" %>
<%@ page import="com.example.dao.CourseDAO" %>

<%
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || !"teacher".equals(teacher.getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }

    CourseDAO dao = new CourseDAO();
    List<Map<String,Object>> courses =
        dao.getCoursesOverviewByTeacher(teacher.getUserId());
%>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>View Courses</h2>
    <div>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
            </svg>
            Back to Dashboard
        </a>
    </div>
</div>



<div class="card">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Course</th>
                        <th>Total Homework</th>
                        <th>Pending / Overdue</th>
                        <th class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (courses.isEmpty()) {
                %>
                    <tr>
                        <td colspan="4" class="text-center text-muted py-4">
                            No courses assigned.
                        </td>
                    </tr>
                <%
                    } else {
                        for (Map<String,Object> c : courses) {
                            int overdue = (int) c.get("overdue");
                %>
                    <tr>
                        <td>
                            <strong><%= c.get("courseName") %></strong>
                        </td>

                        <td>
                            <span class="badge bg-secondary">
                                <%= c.get("totalHomework") %>
                            </span>
                        </td>

                        <td>
                            <% if (overdue == 0) { %>
                                <span class="badge bg-success">
                                    0 overdue
                                </span>
                            <% } else { %>
                                <span class="badge bg-danger">
                                    <%= overdue %> overdue
                                </span>
                            <% } %>
                        </td>

                        <td class="text-end">
                            <a href="${pageContext.request.contextPath}/teacher/homework?courseId=<%= c.get("courseId") %>"
                               class="btn btn-outline-primary btn-sm">
                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
                                                            <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z"/>
                                                            <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z"/>
                                </svg>
                                View Homework
                            </a>
                        </td>
                    </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>


<%@ include file="../layout/footer.jsp" %>