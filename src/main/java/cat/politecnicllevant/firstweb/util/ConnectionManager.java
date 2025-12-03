package cat.politecnicllevant.firstweb.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Proporciona EntityManagers compartint una única fàbrica per tot l'aplicatiu.
 */
public class ConnectionManager {

    // Utilitzem directament el nom de la persistence-unit del persistence.xml
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("quizzMysql");

    private ConnectionManager() {
        // Evitar instanciació
    }

    /**
     * Crea un EntityManager nou per a cada operació de DAO.
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
