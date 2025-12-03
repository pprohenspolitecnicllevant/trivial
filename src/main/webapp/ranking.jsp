<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>

<%-- Classificació de les partides guardades --%>
<html>
<head>
    <title>Game ranking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/game.css"/>
</head>
<body>
<div class="container">
    <header>
        <h1>Ranking</h1>
        <p class="summary">Sessions ordered by duration and correct answers.</p>
    </header>

    <%-- Taula amb les partides persistides ordenades segons la consulta del servei --%>
    <table class="ranking">
        <thead>
        <tr>
            <th>#</th>
            <th>Player</th>
            <th>Duration (s)</th>
            <th>Correct answers</th>
            <th>Incorrect answers</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="game" items="${ranking}" varStatus="status">
            <tr>
                <td><c:out value="${status.index + 1}"/></td>
                <td><c:out value="${game.user.email}"/></td>
                <td><c:out value="${game.duration / 1000}"/></td>
                <td><c:out value="${game.correctAnswers}"/></td>
                <td><c:out value="${game.incorrectAnswers}"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty ranking}">
            <tr>
                <td colspan="5">No games have been played yet.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <div class="actions">
        <a class="button" href="${pageContext.request.contextPath}/login">Return to login</a>
    </div>
</div>
</body>
</html>
