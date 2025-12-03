<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>

<%-- Pantalla de final de partida quan el temps s'esgota --%>
<html>
<head>
    <title>Session expired</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/game.css"/>
</head>
<body>
<div class="container">
    <h1>Session expired</h1>
    <p class="summary">The session has ended. Here is the summary:</p>
    <%-- Resum de la partida acabada abans d'anar al rànquing o reiniciar --%>
    <ul class="score">
        <li>Total survival time: <strong><c:out value="${game.duration/1000}"/>s</strong></li>
        <li>Correct answers: <strong><c:out value="${game.correctAnswers}"/></strong></li>
        <li>Incorrect answers: <strong><c:out value="${game.incorrectAnswers}"/></strong></li>
    </ul>
    <a class="button" href="${pageContext.request.contextPath}/login">Play again</a>
    <a class="button" href="${pageContext.request.contextPath}/ranking">View ranking</a>
</div>
</body>
</html>
