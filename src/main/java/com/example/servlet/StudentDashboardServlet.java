package com.example.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.example.model.User;
import com.example.dao.CourseDAO;
import com.example.dao.HomeworkDAO;
import com.example.dao.SubmissionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * StudentDashboardServlet - Controller untuk student dashboard
 */
@WebServlet("/student")
public class StudentDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // User sudah divalidasi oleh AuthFilter
        User user = (User) req.getSession().getAttribute("user");
        int studentId = user.getUserId();

        CourseDAO courseDAO = new CourseDAO();
        HomeworkDAO homeworkDAO = new HomeworkDAO();
        SubmissionDAO submissionDAO = new SubmissionDAO();

        // 1. Hitung jumlah course yang diikuti student
        List<Map<String, Object>> courses = courseDAO.getCoursesByStudent(studentId);
        int courseCount = courses.size();

        // 2. Hitung jumlah homework aktif dan pending/overdue
        int activeHomeworkCount = 0;
        int pendingOverdueCount = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (Map<String, Object> course : courses) {
            int courseId = (int) course.get("courseId");
            List<Map<String, Object>> homeworkList = homeworkDAO.getHomeworkByCourse(courseId);

            for (Map<String, Object> hw : homeworkList) {
                int homeworkId = (int) hw.get("homeworkId");
                Timestamp deadline = (Timestamp) hw.get("deadline");

                // Homework masih aktif jika deadline belum lewat
                if (deadline.after(now)) {
                    activeHomeworkCount++;
                }

                // Cek apakah student sudah submit
                Map<String, Object> submission = submissionDAO.getSubmissionByStudentAndHomework(
                        studentId, homeworkId);

                // Pending/Overdue: belum submit atau status bukan 'graded'
                if (submission == null || !"graded".equals(submission.get("status"))) {
                    // Jika deadline sudah lewat atau belum submit
                    if (deadline.before(now) || submission == null) {
                        pendingOverdueCount++;
                    }
                }
            }
        }

        // Set attributes untuk JSP
        req.setAttribute("user", user);
        req.setAttribute("courseCount", courseCount);
        req.setAttribute("activeHomeworkCount", activeHomeworkCount);
        req.setAttribute("pendingOverdueCount", pendingOverdueCount);

        // Forward ke JSP view
        req.getRequestDispatcher("/student/dashboard.jsp").forward(req, resp);
    }
}
