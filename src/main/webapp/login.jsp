<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>

<%-- Formulari de connexió per interactuar amb el LoginServlet --%>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="container">
    <h1>Login</h1>
    <c:if test="${not empty error}">
        <div class="feedback error">${error}</div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="feedback success">${message}</div>
    </c:if>
    <%-- El formulari envia les credencials al servlet d'autenticació --%>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="email">Email address</label>
        <input type="email" id="email" name="email" required/>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required/>

        <%-- Botó d'enviament per intentar autenticar l'usuari --%>
        <button type="submit">Sign in</button>
    </form>
    <%-- Enllaços ràpids a les altres pantalles principals --%>
    <a class="link" href="${pageContext.request.contextPath}/register">Create an account</a>
    <a class="link" href="${pageContext.request.contextPath}/ranking">View ranking</a>
</div>
</body>
</html>
