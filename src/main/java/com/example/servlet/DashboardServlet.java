package com.example.servlet;

import com.example.model.User;

// DAO imports
import com.example.dao.CourseDAO;
import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;
import com.example.dao.TeacherDashboardDAO;

// Jakarta Servlet imports
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Java util & sql
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        String role = user.getRole();

        switch (role) {
            case "admin":
                loadAdminDashboard(req, user);
                req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
                break;

            case "teacher":
                loadTeacherDashboard(req, user);
                req.getRequestDispatcher("/teacher/dashboard.jsp").forward(req, resp);
                break;

            case "student":
            default:
                loadStudentDashboard(req, user);
                req.getRequestDispatcher("/student/dashboard.jsp").forward(req, resp);
        }
    }

    private void loadAdminDashboard(HttpServletRequest req, User user) {
        req.setAttribute("user", user);

    }

    private void loadTeacherDashboard(HttpServletRequest req, User user) {
        TeacherDashboardDAO dashDAO = new TeacherDashboardDAO();

        req.setAttribute("courseCount", dashDAO.countCourses(user.getUserId()));
        req.setAttribute("hwCount", dashDAO.countActiveHomework(user.getUserId()));
        req.setAttribute("pendingCount", dashDAO.countPendingSubmissions(user.getUserId()));
    }

    private void loadStudentDashboard(HttpServletRequest req, User user) {

        int studentId = user.getUserId();

        CourseDAO courseDAO = new CourseDAO();
        HomeworkDAO homeworkDAO = new HomeworkDAO();
        SubmissionDAO submissionDAO = new SubmissionDAO();

        List<Map<String, Object>> courses = courseDAO.getCoursesByStudent(studentId);
        int courseCount = courses.size();

        int activeHomeworkCount = 0;
        int pendingOverdueCount = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (Map<String, Object> course : courses) {
            int courseId = (int) course.get("courseId");
            List<Map<String, Object>> homeworkList =
                    homeworkDAO.getHomeworkByCourse(courseId);

            for (Map<String, Object> hw : homeworkList) {
                int homeworkId = (int) hw.get("homeworkId");
                Timestamp deadline = (Timestamp) hw.get("deadline");

                if (deadline.after(now)) {
                    activeHomeworkCount++;
                }

                Map<String, Object> submission =
                        submissionDAO.getSubmissionByStudentAndHomework(
                                studentId, homeworkId);

                if (submission == null || !"graded".equals(submission.get("status"))) {
                    if (deadline.before(now) || submission == null) {
                        pendingOverdueCount++;
                    }
                }
            }
        }

        req.setAttribute("user", user);
        req.setAttribute("courseCount", courseCount);
        req.setAttribute("activeHomeworkCount", activeHomeworkCount);
        req.setAttribute("pendingOverdueCount", pendingOverdueCount);
    }
}
