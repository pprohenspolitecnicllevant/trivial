package cat.politecnicllevant.firstweb.dto;

import cat.politecnicllevant.firstweb.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dades simplificades de la partida per lliurar-les a les capes web.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    private Long id;
    private UserDto user;
    private int correctAnswers;
    private int incorrectAnswers;
    private long duration;

    /**
     * Converteix l'entitat JPA a un objecte pla utilitzat a la capa web.
     */
    public static GameDto fromEntity(Game game) {
        if (game == null) {
            return null;
        }
        return GameDto.builder()
                .id(game.getId())
                .user(UserDto.fromEntity(game.getUser()))
                .correctAnswers(game.getCorrectAnswers())
                .incorrectAnswers(game.getIncorrectAnswers())
                .duration(game.getDuration())
                .build();
    }
}
