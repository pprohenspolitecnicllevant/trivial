package cat.politecnicllevant.firstweb.controller;

import cat.politecnicllevant.firstweb.dto.UserDto;
import cat.politecnicllevant.firstweb.service.UserService;
import cat.politecnicllevant.firstweb.service.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Gestiona el procés d'inici de sessió i prepara la sessió HTTP per començar la partida.
 */
@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Quan arribam d'un registre correcte informam l'usuari perquè pugui autenticar-se
        if (request.getParameter("registered") != null) {
            request.setAttribute("message", "Registration completed. Please sign in.");
        }
        // Delegam a la JSP que mostra el formulari de login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Verifiquem les credencials que arriben des del formulari de login
        UserDto user = userService.authenticate(email, password);

        if (user == null) {
            // Retornam al formulari indicant que les credencials no són correctes
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Guardem l'usuari autenticat a la sessió amb els comptadors inicials
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        long startedAt = System.currentTimeMillis();
        session.setAttribute("startedAt", startedAt);
        session.setAttribute("correctAnswers", 0);
        session.setAttribute("incorrectAnswers", 0);

        response.sendRedirect("game");
    }
}