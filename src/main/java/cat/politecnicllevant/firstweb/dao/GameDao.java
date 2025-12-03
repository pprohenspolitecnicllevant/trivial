package cat.politecnicllevant.firstweb.dao;

import cat.politecnicllevant.firstweb.model.Game;
import java.util.List;

public interface GameDao {
    /**
     * Desa una partida jugada.
     */
    Game save(Game game);

    /**
     * Recupera les partides ordenades per mostrar el rànquing global.
     */
    List<Game> findRanking();
}
