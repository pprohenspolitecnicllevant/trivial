package cat.politecnicllevant.firstweb.dto;

import cat.politecnicllevant.firstweb.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dades mínimes de l'usuari per exposar al controlador sense revelar el hash.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;

    /**
     * Produeix una versió segura de l'usuari sense el hash de contrasenya.
     */
    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
