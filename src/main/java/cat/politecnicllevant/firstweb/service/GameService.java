package cat.politecnicllevant.firstweb.service;

import cat.politecnicllevant.firstweb.dto.GameDto;
import cat.politecnicllevant.firstweb.dto.UserDto;
import java.util.List;

public interface GameService {
    /**
     * Desa una partida finalitzada associada a l'usuari autenticat.
     */
    GameDto registerGame(UserDto userDto, int correct, int incorrect, long durationMillis);

    /**
     * Recupera el rànquing complet de partides ordenades per durada i encerts.
     */
    List<GameDto> findRanking();
}
