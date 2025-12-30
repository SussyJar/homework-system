<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="../layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Homework Assignment</h2>
    <a href="${pageContext.request.contextPath}/student/courses" class="btn btn-outline-secondary">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
        </svg>
        Back to Courses
    </a>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-danger" role="alert">
        ${error}
    </div>
</c:if>

<c:choose>
    <c:when test="${empty homeworkList}">
        <div class="card">
            <div class="card-body text-center py-5">
                <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="currentColor" class="bi bi-inbox text-muted mb-3" viewBox="0 0 16 16">
                    <path d="M4.98 4a.5.5 0 0 0-.39.188L1.54 8H6a.5.5 0 0 1 .5.5 1.5 1.5 0 1 0 3 0A.5.5 0 0 1 10 8h4.46l-3.05-3.812A.5.5 0 0 0 11.02 4H4.98zm-1.17-.437A1.5 1.5 0 0 1 4.98 3h6.04a1.5 1.5 0 0 1 1.17.563l3.7 4.625a.5.5 0 0 1 .106.374l-.39 3.124A1.5 1.5 0 0 1 14.117 13H1.883a1.5 1.5 0 0 1-1.489-1.314l-.39-3.124a.5.5 0 0 1 .106-.374l3.7-4.625z"/>
                </svg>
                <h5 class="text-muted">No homework available</h5>
                <p class="text-muted">Check back later for new assignments</p>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Title</th>
                                <th>Deadline</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="hw" items="${homeworkList}">
                                <tr>
                                    <td>
                                        <strong>${hw.title}</strong>
                                    </td>
                                    <td>
                                        <small class="text-muted">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-calendar3" viewBox="0 0 16 16">
                                                <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zM1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857V3.857z"/>
                                                <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"/>
                                            </svg>
                                            ${hw.deadline}
                                        </small>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${hw.submissionStatus == 'submitted'}">
                                                <span class="badge bg-success">Submitted</span>
                                            </c:when>
                                            <c:when test="${hw.submissionStatus == 'late'}">
                                                <span class="badge bg-warning">Late</span>
                                            </c:when>
                                            <c:when test="${hw.submissionStatus == 'graded'}">
                                                <span class="badge bg-info">Graded</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Not Submitted</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <div class="btn-group btn-group-sm" role="group">
                                            <c:choose>
                                                <c:when test="${hw.hasSubmission}">
                                                    <a href="${pageContext.request.contextPath}/student/submissions?homeworkId=${hw.homeworkId}&courseId=${courseId}"
                                                       class="btn btn-outline-primary">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
                                                            <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z"/>
                                                            <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z"/>
                                                        </svg>
                                                        View
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/student/submit?homeworkId=${hw.homeworkId}&courseId=${courseId}"
                                                       class="btn btn-primary">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-upload" viewBox="0 0 16 16">
                                                            <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"/>
                                                            <path d="M7.646 1.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 2.707V11.5a.5.5 0 0 1-1 0V2.707L5.354 4.854a.5.5 0 1 1-.708-.708l3-3z"/>
                                                        </svg>
                                                        Submit
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                            <button class="btn btn-outline-warning btn-set-reminder"
                                                    data-homework-id="${hw.homeworkId}"
                                                    data-homework-title="${hw.title}">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16">
                                                    <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z"/>
                                                </svg>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<!-- Bootstrap Modal for Reminder -->
<div class="modal fade" id="reminderModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Set Reminder</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="reminderForm">
                <div class="modal-body">
                    <input type="hidden" id="reminderHomeworkId" name="homeworkId">
                    <p id="reminderHomeworkTitle" class="text-muted"></p>
                    <div class="mb-3">
                        <label for="remindAt" class="form-label">Remind me at:</label>
                        <input type="datetime-local" class="form-control" id="remindAt" name="remindAt" required>
                    </div>
                    <div id="reminderMessage" class="alert d-none"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Reminder</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
const reminderModal = new bootstrap.Modal(document.getElementById('reminderModal'));

// Event delegation for reminder buttons
document.addEventListener('click', function(e) {
    if (e.target.closest('.btn-set-reminder')) {
        const btn = e.target.closest('.btn-set-reminder');
        const homeworkId = btn.dataset.homeworkId;
        const homeworkTitle = btn.dataset.homeworkTitle;

        document.getElementById('reminderHomeworkId').value = homeworkId;
        document.getElementById('reminderHomeworkTitle').innerText = 'Homework: ' + homeworkTitle;
        document.getElementById('reminderMessage').classList.add('d-none');
        reminderModal.show();
    }
});

document.getElementById('reminderForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const homeworkId = document.getElementById('reminderHomeworkId').value;
    const remindAt = document.getElementById('remindAt').value;
    const remindAtISO = new Date(remindAt).toISOString().slice(0, 19).replace('T', ' ');

    fetch('${pageContext.request.contextPath}/api/student/reminder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'homeworkId=' + homeworkId + '&remindAt=' + encodeURIComponent(remindAtISO)
    })
    .then(response => response.json())
    .then(data => {
        const messageDiv = document.getElementById('reminderMessage');
        messageDiv.classList.remove('d-none', 'alert-success', 'alert-danger');

        if (data.success) {
            messageDiv.classList.add('alert-success');
            messageDiv.innerText = 'Reminder set successfully!';
            setTimeout(() => reminderModal.hide(), 1500);
        } else {
            messageDiv.classList.add('alert-danger');
            messageDiv.innerText = 'Error: ' + data.message;
        }
    })
    .catch(error => {
        const messageDiv = document.getElementById('reminderMessage');
        messageDiv.classList.remove('d-none');
        messageDiv.classList.add('alert-danger');
        messageDiv.innerText = 'Error setting reminder';
        console.error('Error:', error);
    });
});
</script>

<%@ include file="../layout/footer.jsp" %>
