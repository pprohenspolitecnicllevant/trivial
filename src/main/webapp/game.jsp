<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>

<%-- Vista principal del joc amb la pregunta actual i el cronòmetre --%>
<html>
<head>
    <title>Trivia survival</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/game.css"/>
</head>
<body>
<div class="container">
    <header>
        <h1>Keep the session alive</h1>
        <div class="status">
            <div>Time left: <strong id="time-remaining"><c:out value="${remainingSeconds}"/>s</strong></div>
            <div>Correct answers: <strong><c:out value="${correctAnswers}"/></strong></div>
            <div>Incorrect answers: <strong><c:out value="${incorrectAnswers}"/></strong></div>
        </div>
    </header>

    <c:if test="${not empty feedback}">
        <div class="feedback ${feedbackType}">${feedback}</div>
    </c:if>

    <div class="question-card">
        <p class="category">Category: <c:out value="${question.category}"/></p>
        <p class="category">Difficulty: <c:out value="${difficulty}"/></p>
        <h2><c:out value="${question.questionText}"/></h2>
        <%-- Cada opció de resposta es genera dinàmicament segons el DTO retornat pel servidor --%>
        <form method="post" action="${pageContext.request.contextPath}/game">
            <c:forEach var="option" items="${question.options}">
                <label class="option">
                    <input type="radio" name="answer" value="${option}" required/>
                    <span><c:out value="${option}"/></span>
                </label>
            </c:forEach>
            <%-- Botó per confirmar la resposta seleccionada i passar a la següent pregunta --%>
            <button type="submit">Submit answer</button>
        </form>
    </div>
</div>
<script>
    (() => {
        const timerEl = document.getElementById('time-remaining');
        const contextPath = '<c:out value="${pageContext.request.contextPath}"/>';
        // Injectam el valor inicial del temps restant des del model de la vista.
        let remainingSeconds = parseInt('<c:out value="${remainingSeconds}"/>', 10);

        // Ens asseguram que el cronòmetre comença amb un nombre vàlid.
        if (isNaN(remainingSeconds) || remainingSeconds <= 0) {
            timerEl.textContent = '0s';
            window.location.replace(`${contextPath}/game`);
            return;
        }

        const intervalId = setInterval(() => {
            remainingSeconds--;
            timerEl.textContent = Math.max(remainingSeconds, 0) + 's';

            if (remainingSeconds <= 0) {
                clearInterval(intervalId);
                window.location.replace(`${contextPath}/game`);
            }

        }, 1000);
    })();
</script>
</body>
</html>
