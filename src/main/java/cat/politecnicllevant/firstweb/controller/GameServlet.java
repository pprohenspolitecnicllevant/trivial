package cat.politecnicllevant.firstweb.controller;

import cat.politecnicllevant.firstweb.dto.GameDto;
import cat.politecnicllevant.firstweb.dto.TriviaQuestionDto;
import cat.politecnicllevant.firstweb.dto.UserDto;
import cat.politecnicllevant.firstweb.service.GameService;
import cat.politecnicllevant.firstweb.service.GameServiceImpl;
import cat.politecnicllevant.firstweb.service.TriviaClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;

/**
 * Orquestra el flux principal del joc: carrega preguntes, valida respostes i controla la caducitat de la sessió.
 */
@WebServlet(name = "gameServlet", value = "/game")
public class GameServlet extends HttpServlet {

    private static final long INITIAL_LIFETIME = 60_000L;
    private static final long BONUS_MILLIS = 7_000L;
    private static final long PENALTY_MILLIS = 10_000L;

    private GameService gameService;
    private TriviaClient triviaClient;

    @Override
    public void init() {
        // Inicialitzam els serveis que fan servir la lògica de persistència i la petició a l'API externa
        gameService = new GameServiceImpl();
        triviaClient = new TriviaClient();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("login");
            return;
        }
        // Ens asseguram que els comptadors existeixin per evitar NullPointerException i tenir valors inicials coherents
        checkCounters(session);
        if (isExpired(session)) {
            finishGame(req, resp, session);
            return;
        }

        long remaining = getRemainingMillis(session);

        // Escalem la dificultat segons els encerts per fer la partida progressivament més exigent
        String difficulty = resolveDifficulty(session);

        // Reutilitzem la pregunta si l'usuari ha refrescat la pàgina per evitar servir-ne una de nova
        TriviaQuestionDto question = (TriviaQuestionDto) session.getAttribute("currentQuestion");
        if (question == null) {
            question = triviaClient.fetchQuestion(difficulty);
            session.setAttribute("currentQuestion", question);
        }

        // Exposem les dades necessàries a la vista
        session.setAttribute("currentAnswer", question.getCorrectAnswer());
        req.setAttribute("question", question);
        req.setAttribute("remainingSeconds", remaining / 1000);
        req.setAttribute("correctAnswers", session.getAttribute("correctAnswers"));
        req.setAttribute("incorrectAnswers", session.getAttribute("incorrectAnswers"));
        req.setAttribute("difficulty", difficulty);
        req.getRequestDispatcher("/game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("login");
            return;
        }
        // Tornam a inicialitzar els valors de sessió per si s'han netejat a conseqüència d'altres peticions
        checkCounters(session);

        if (isExpired(session)) {
            finishGame(req, resp, session);
            return;
        }

        // Recuperem la pregunta actual per poder registrar-la i evitar perdre el context
        TriviaQuestionDto question = (TriviaQuestionDto) session.getAttribute("currentQuestion");
        String answer = req.getParameter("answer");
        String correct = (String) session.getAttribute("currentAnswer");
        boolean isCorrect = Objects.equals(answer, correct);

        // Guardem la darrera pregunta contestada per si l'usuari refresca la pàgina a mig joc
        if (question != null) {
            session.setAttribute("lastAnsweredQuestionId", question.getId());
        }

        if (isCorrect) {
            int correctCount = (int) session.getAttribute("correctAnswers") + 1;
            session.setAttribute("correctAnswers", correctCount);
            extendLifetime(session, BONUS_MILLIS);
            req.setAttribute("feedback", "Correct answer! +" + BONUS_MILLIS/1000 + " seconds.");
            req.setAttribute("feedbackType", "success");
        } else {
            int incorrectCount = (int) session.getAttribute("incorrectAnswers") + 1;
            session.setAttribute("incorrectAnswers", incorrectCount);
            extendLifetime(session, -PENALTY_MILLIS);
            req.setAttribute("feedback", "Wrong answer. -" + PENALTY_MILLIS/1000 + " seconds.");
            req.setAttribute("feedbackType", "error");
        }

        if (isExpired(session)) {
            finishGame(req, resp, session);
            return;
        }

        // Alliberem la pregunta perquè la propera petició en carregui una de nova
        session.removeAttribute("currentQuestion");
        session.removeAttribute("currentAnswer");

        doGet(req, resp);
    }

    /**
     * Comprova si el temps de sessió establert ha vençut.
     */
    private boolean isExpired(HttpSession session) {
        return getRemainingMillis(session) <= 0;
    }

    /**
     * Inicialitza els valors guardats a sessió si encara no existeixen (primera visita o sessió recreada).
     */
    private void checkCounters(HttpSession session) {
        if (session.getAttribute("correctAnswers") == null) {
            session.setAttribute("correctAnswers", 0);
        }
        if (session.getAttribute("incorrectAnswers") == null) {
            session.setAttribute("incorrectAnswers", 0);
        }
        if (session.getAttribute("startedAt") == null) {
            long startedAt = System.currentTimeMillis();
            session.setAttribute("startedAt", startedAt);
            session.setAttribute("expiration", startedAt + INITIAL_LIFETIME);
        }
    }

    /**
     * Calcula el temps restant en mil·lisegons respectant l'expiració guardada a sessió.
     */
    private long getRemainingMillis(HttpSession session) {
        Long expiration = (Long) session.getAttribute("expiration");
        if (expiration == null) {
            long now = System.currentTimeMillis();
            expiration = now + INITIAL_LIFETIME;
            session.setAttribute("expiration", expiration);
        }
        return expiration - System.currentTimeMillis();
    }

    /**
     * Afegeix o resta mil·lisegons al temps límit, generant el feedback de bonificació o penalització.
     */
    private void extendLifetime(HttpSession session, long deltaMillis) {
        Long expiration = (Long) session.getAttribute("expiration");
        if (expiration == null) {
            expiration = System.currentTimeMillis() + INITIAL_LIFETIME;
        }
        expiration += deltaMillis;
        session.setAttribute("expiration", expiration);
    }

    /**
     * Determina la dificultat de la següent pregunta en funció dels encerts acumulats.
     */
    private String resolveDifficulty(HttpSession session) {
        // Dificultat: easy fins 2 encerts, medium fins a 7, a partir de 8 hard
        int correct = (int) session.getAttribute("correctAnswers");
        if (correct >= 8) {
            return "hard";
        }
        if (correct >= 3) {
            return "medium";
        }
        return "easy";
    }

    /**
     * Persisteix la partida, invalida la sessió i mostra la pantalla de final de joc.
     */
    private void finishGame(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws ServletException, IOException {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            session.invalidate();
            resp.sendRedirect("login");
            return;
        }
        Integer correct = (Integer) session.getAttribute("correctAnswers");
        Integer incorrect = (Integer) session.getAttribute("incorrectAnswers");
        Long startedAt = (Long) session.getAttribute("startedAt");
        Long expiration = (Long) session.getAttribute("expiration");
        long endInstant = Math.min(System.currentTimeMillis(), expiration != null ? expiration : System.currentTimeMillis());
        long duration = startedAt != null ? Math.max(0, endInstant - startedAt) : 0;

        GameDto stored = gameService.registerGame(user, correct != null ? correct : 0, incorrect != null ? incorrect : 0, duration);
        session.invalidate();
        req.setAttribute("game", stored);
        req.getRequestDispatcher("/game-over.jsp").forward(req, resp);
    }
}
