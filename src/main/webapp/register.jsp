<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>

<%-- Formulari de registre --%>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css" />
</head>
<body>
<div class="container">
    <h1>Create an account</h1>
    <c:if test="${not empty error}">
        <div class="feedback error">${error}</div>
    </c:if>
    <%-- Mostram errors si l'email ja existeix o si falta informació --%>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <label for="email">Email address</label>
        <input type="email" id="email" name="email" required />

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required />

        <%-- Botó per enregistrar el nou usuari i validar dades al servlet --%>
        <button type="submit">Register</button>
    </form>
    <%-- Enllaç per tornar al login un cop registrada la persona --%>
    <a class="link" href="${pageContext.request.contextPath}/login">Already have an account? Sign in</a>
</div>
</body>
</html>
