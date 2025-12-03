package cat.politecnicllevant.firstweb.dao;

import cat.politecnicllevant.firstweb.model.User;

public interface UserDao {
    /**
     * Cerca un usuari pel seu correu electrònic.
     */
    User findByEmail(String email);

    /**
     * Desa un nou usuari i retorna l'entitat amb l'identificador generat.
     */
    User save(User user);
}
