<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login | Homework System</title>

    <!-- Bootstrap LOCAL -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="bg-light">

<%@ include file="../layout/header.jsp" %>

<!-- LOGIN FORM -->
<div class="container d-flex justify-content-center align-items-center"
     style="min-height: calc(100vh - 56px);">

    <div class="card shadow-sm" style="width: 100%; max-width: 400px;">
        <div class="card-body p-4">

            <h4 class="text-center fw-bold mb-4">
                Login
            </h4>

            <!-- ERROR MESSAGE -->
            <c:if test="${param.error == 1}">
                <div class="alert alert-danger text-center">
                    Username atau password salah
                </div>
            </c:if>

            <form method="post"
                  action="${pageContext.request.contextPath}/login">

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

<%@ include file="../layout/footer.jsp" %>

</body>
</html>
