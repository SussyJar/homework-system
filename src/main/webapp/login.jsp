<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login | Homework System</title>

    <!-- Bootstrap LOCAL -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body class="bg-light">


<!-- LOGIN FORM -->
<div class="container d-flex justify-content-center align-items-center"
     style="min-height: calc(100vh - 56px);">

    <div class="card shadow-sm" style="width: 100%; max-width: 400px;">
        <div class="card-body p-4">

            <h4 class="text-center fw-bold mb-4">
                Login
            </h4>

            <form method="post" action="login">

                <!-- USERNAME -->
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input
                        type="text"
                        name="username"
                        class="form-control"
                        required
                    >
                </div>

                <!-- PASSWORD -->
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input
                        type="password"
                        name="password"
                        class="form-control"
                        required
                    >
                </div>

                <!-- SUBMIT -->
                <div class="d-grid mt-4">
                    <button type="submit" class="btn btn-primary">
                        Login
                    </button>
                </div>

            </form>

        </div>
    </div>
</div>

</body>
</html>
<%@ include file="../layout/footer.jsp" %>