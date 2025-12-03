package cat.politecnicllevant.firstweb.service;

import cat.politecnicllevant.firstweb.dto.UserDto;

public interface UserService {
    /**
     * Verifica les credencials rebudes des del formulari i retorna un usuari segur per guardar a sessió.
     */
    UserDto authenticate(String email, String password);

    /**
     * Dona d'alta un usuari nou aplicant les validacions prèvies necessàries.
     */
    UserDto register(String email, String password);
}
