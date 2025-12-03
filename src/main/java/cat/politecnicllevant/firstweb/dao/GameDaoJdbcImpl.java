package cat.politecnicllevant.firstweb.dao;

import cat.politecnicllevant.firstweb.model.Game;
import cat.politecnicllevant.firstweb.model.User;
import cat.politecnicllevant.firstweb.util.JdbcConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementació JDBC per guardar les partides sense ORM.
 * Permet veure clarament les consultes SQL que alimenten el rànquing.
 */
public class GameDaoJdbcImpl implements GameDao {

    private static final String INSERT_SQL = "INSERT INTO games (user_id, correct_answers, incorrect_answers, duration) VALUES (?,?,?,?)";
    private static final String FIND_RANKING_SQL = "SELECT g.id, g.correct_answers, g.incorrect_answers, g.duration, u.id as user_id, u.email " +
            "FROM games g JOIN users u ON g.user_id = u.id " +
            "ORDER BY g.duration DESC, g.correct_answers DESC";

    @Override
    public Game save(Game game) {
        // Inserim les dades bàsiques de la partida i recuperam l'ID generat
        try (Connection connection = JdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, game.getUser().getId());
            statement.setInt(2, game.getCorrectAnswers());
            statement.setInt(3, game.getIncorrectAnswers());
            statement.setLong(4, game.getDuration());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    game.setId(keys.getLong(1));
                }
            }
            return game;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting game with JDBC", e);
        }
    }

    @Override
    public List<Game> findRanking() {
        List<Game> games = new ArrayList<>();
        // Construïm el rànquing amb una única consulta que ja retorna l'usuari associat
        try (Connection connection = JdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_RANKING_SQL);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Game game = new Game();
                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                game.setUser(user);
                game.setId(rs.getLong("id"));
                game.setCorrectAnswers(rs.getInt("correct_answers"));
                game.setIncorrectAnswers(rs.getInt("incorrect_answers"));
                game.setDuration(rs.getLong("duration"));
                games.add(game);
            }
            return games;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching games ranking with JDBC", e);
        }
    }
}
