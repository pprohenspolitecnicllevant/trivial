package cat.politecnicllevant.firstweb.service;

import cat.politecnicllevant.firstweb.dao.GameDao;
import cat.politecnicllevant.firstweb.dto.GameDto;
import cat.politecnicllevant.firstweb.dto.UserDto;
import cat.politecnicllevant.firstweb.model.Game;
import cat.politecnicllevant.firstweb.model.User;
import cat.politecnicllevant.firstweb.dao.GameDaoJdbcImpl;
import cat.politecnicllevant.firstweb.dao.GameDaoOrmImpl;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * Gestiona la persistència de les partides amb les dues estratègies de DAO disponibles.
 */
public class GameServiceImpl implements GameService {

    private final GameDao gameDao;

    /**
     * Constructor per defecte que empra l'ORM per persistir dades.
     */
    public GameServiceImpl() {
        this(new GameDaoOrmImpl());
    }

    /**
     * Permet triar entre l'estratègia JDBC o JPA segons el paràmetre.
     */
    public GameServiceImpl(boolean useJdbc) {
        this(useJdbc ? new GameDaoJdbcImpl() : new GameDaoOrmImpl());
    }

    /**
     * Injecta una implementació concreta de GameDao (útil per a proves).
     */
    public GameServiceImpl(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    @Override
    public GameDto registerGame(UserDto userDto, int correct, int incorrect, long durationMillis) {
        // Validam que la partida sempre estigui vinculada a un usuari vàlid abans de persistir-la
        Objects.requireNonNull(userDto, "UserDto is required to store the game");

        // Mapam el DTO rebut des de la capa web a l'entitat de persistència
        Game game = new Game();
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        game.setUser(user);
        game.setCorrectAnswers(correct);
        game.setIncorrectAnswers(incorrect);
        game.setDuration(durationMillis);

        // Guardam la partida i retornam una versió segura per a la vista
        Game persisted = gameDao.save(game);
        return GameDto.fromEntity(persisted);
    }

    @Override
    public List<GameDto> findRanking() {
        // Transformam les entitats a DTO per no exposar el model JPA directament a la capa de presentació
        return gameDao.findRanking()
                .stream()
                .map(GameDto::fromEntity)
                .collect(Collectors.toList());
    }
}
