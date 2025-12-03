package cat.politecnicllevant.firstweb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe d'ajuda per obtenir connexions JDBC contra la base de dades MySQL del docker-compose.
 */
public class JdbcConnector {
    private static String url = "jdbc:mysql://db:3306/quizz?createDatabaseIfNotExist=true";
    private static String user = "root";
    private static String password = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading JDBC Driver", e);
        }
    }

    /**
     * Obté una connexió configurada amb les credencials per defecte.
     */
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
