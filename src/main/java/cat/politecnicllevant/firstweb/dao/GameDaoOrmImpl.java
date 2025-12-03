package cat.politecnicllevant.firstweb.dao;

import cat.politecnicllevant.firstweb.model.Game;
import cat.politecnicllevant.firstweb.util.ConnectionManager;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Implementació JPA per persistir les partides.
 */
public class GameDaoOrmImpl implements GameDao {
    @Override
    public Game save(Game game) {
        EntityManager em = ConnectionManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(game);
            em.getTransaction().commit();
            return game;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Game> findRanking() {
        EntityManager em = ConnectionManager.getEntityManager();
        try {
            // Seleccionam i carregam l'usuari associat per evitar LazyInitialization a les JSP
            return em.createQuery(
                            "SELECT g FROM Game g JOIN FETCH g.user ORDER BY g.duration DESC, g.correctAnswers DESC",
                            Game.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
