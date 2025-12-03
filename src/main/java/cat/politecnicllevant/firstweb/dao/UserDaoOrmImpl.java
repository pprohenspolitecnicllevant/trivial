package cat.politecnicllevant.firstweb.dao;

import cat.politecnicllevant.firstweb.model.User;
import cat.politecnicllevant.firstweb.util.ConnectionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

/**
 * Implementació basada en JPA per accedir a la taula d'usuaris.
 */
public class UserDaoOrmImpl implements UserDao {
    @Override
    public User findByEmail(String email) {
        EntityManager em = ConnectionManager.getEntityManager();
        try {
            // Utilitzam JPQL per aprofitar el mapping d'entitats i evitar SQL manual
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public User save(User user) {
        EntityManager em = ConnectionManager.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
