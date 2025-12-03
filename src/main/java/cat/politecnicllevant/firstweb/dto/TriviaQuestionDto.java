package cat.politecnicllevant.firstweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Representa la pregunta rebuda de l'API de trivial amb les opcions ja barrejades.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriviaQuestionDto {
    private String id;
    private String category;
    private String questionText;
    private String correctAnswer;
    private List<String> options;
}
