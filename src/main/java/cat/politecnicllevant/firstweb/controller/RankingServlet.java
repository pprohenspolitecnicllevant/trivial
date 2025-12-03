package cat.politecnicllevant.firstweb.controller;

import cat.politecnicllevant.firstweb.dto.GameDto;
import cat.politecnicllevant.firstweb.service.GameService;
import cat.politecnicllevant.firstweb.service.GameServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Recupera el llistat de partides i el presenta ordenat a la vista de rànquing.
 */
@WebServlet(name = "rankingServlet", value = "/ranking")
public class RankingServlet extends HttpServlet {

    private GameService gameService;

    @Override
    public void init() {
        // Podem injectar altres implementacions del DAO si calgués per proves
        gameService = new GameServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Delegam tota la consulta al servei i simplement exposem les dades a la JSP
        List<GameDto> ranking = gameService.findRanking();
        req.setAttribute("ranking", ranking);
        req.getRequestDispatcher("/ranking.jsp").forward(req, resp);
    }
}
