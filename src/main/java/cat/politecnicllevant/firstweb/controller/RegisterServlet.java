package cat.politecnicllevant.firstweb.controller;

import cat.politecnicllevant.firstweb.dto.UserDto;
import cat.politecnicllevant.firstweb.service.UserService;
import cat.politecnicllevant.firstweb.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Gestiona el procés d'alta d'usuaris nous i mostra el formulari de registre.
 */
@WebServlet(name = "registerServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Simplement delegam a la vista; no hi ha lògica de negoci en GET
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Creem un usuari nou si el correu no existeix prèviament
        UserDto created = userService.register(email, password);

        if (created == null) {
            // Informam l'usuari del motiu de fallada i mantenim les dades al formulari
            req.setAttribute("error", "Registration failed. The email may already be in use.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect("login?registered=true");
    }
}
