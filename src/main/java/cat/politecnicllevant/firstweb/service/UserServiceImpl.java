package cat.politecnicllevant.firstweb.service;

import cat.politecnicllevant.firstweb.dao.UserDao;
import cat.politecnicllevant.firstweb.dao.UserDaoJdbcImpl;
import cat.politecnicllevant.firstweb.dao.UserDaoOrmImpl;
import cat.politecnicllevant.firstweb.dto.UserDto;
import cat.politecnicllevant.firstweb.model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementa lògica d'autenticació i registre aplicant les regles bàsiques de validació.
 */
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoOrmImpl();
    }

    public UserServiceImpl(boolean useJdbc) {
        this.userDao = useJdbc ? new UserDaoJdbcImpl() : new UserDaoOrmImpl();
    }

    @Override
    public UserDto authenticate(String email, String password) {
        // Validació bàsica per evitar consultes innecessàries
        if (email == null || password == null) {
            return null;
        }
        User user = userDao.findByEmail(email);
        if (user == null) {
            return null;
        }

        // Comprovam el hash bcrypt desat a la base de dades
        if (BCrypt.checkpw(password, user.getPassword())) {
            return UserDto.fromEntity(user);
        }

        return null;
    }

    @Override
    public UserDto register(String email, String password) {
        // Impedim crear usuaris incomplets abans de consultar la base de dades
        if (email == null || password == null) {
            return null;
        }

        User existing = userDao.findByEmail(email);
        if (existing != null) {
            return null;
        }

        // Guardam sempre el hash i mai la contrasenya en pla
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(email, hashedPassword);
        return UserDto.fromEntity(userDao.save(user));
    }
}
