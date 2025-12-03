package cat.politecnicllevant.firstweb.dao;

import cat.politecnicllevant.firstweb.model.User;
import cat.politecnicllevant.firstweb.util.JdbcConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementació JDBC manual per gestionar usuaris.
 * És útil com a alternativa lleugera a l'ORM o per fer proves sense JPA.
 */
public class UserDaoJdbcImpl implements UserDao {

    private static final String FIND_BY_EMAIL = "SELECT id, email, password FROM users WHERE email = ?";
    private static final String INSERT_SQL = "INSERT INTO users (email, password) VALUES (?, ?)";

    @Override
    public User findByEmail(String email) {
        // Recuperam l'usuari pel seu correu amb una consulta directa
        try (Connection connection = JdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user with JDBC", e);
        }
    }

    @Override
    public User save(User user) {
        // Inserim l'usuari i recuperam la clau generada perquè la resta de capes la puguin utilitzar
        try (Connection connection = JdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting user with JDBC", e);
        }
    }
}
