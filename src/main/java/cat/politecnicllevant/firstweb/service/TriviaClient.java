package cat.politecnicllevant.firstweb.service;

import cat.politecnicllevant.firstweb.dto.TriviaQuestionDto;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;

import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Client lleuger per recuperar preguntes des de l'API de trivial. Utilitza builder per generar DTOs
 */
public class TriviaClient {

    private static final String API_URL = "https://the-trivia-api.com/v2/questions?limit=1";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Permet passar la dificultat (easy, medium, hard) tal com la demana l'API.
     * Retorna un DTO pensat per ser mostrat directament a la vista.
     */
    public TriviaQuestionDto fetchQuestion(String difficulty) {
        String url = API_URL;
        if (difficulty != null && !difficulty.isBlank()) {
            // Afegim la dificultat només quan el paràmetre és vàlid per no generar crides incorrectes
            url += "&difficulties=" + URLEncoder.encode(difficulty, StandardCharsets.UTF_8);
        }

        try {
            // Construïm i enviam la petició HTTP sincrònica contra l'API externa
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Trivia API returned status " + response.statusCode());
            }
            return parseQuestion(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch trivia question", e);
        }
    }

    /**
     * Construeix el DTO a partir del JSON cru retornat per l'API externa.
     */
    private TriviaQuestionDto parseQuestion(String payload) {
        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonArray questions = reader.readArray();
            if (questions.isEmpty()) {
                throw new IllegalStateException("No questions received");
            }
            JsonObject raw = questions.getJsonObject(0);
            JsonObject questionObject = raw.getJsonObject("question");
            String text = questionObject.getString("text", "");
            String correct = raw.getString("correctAnswer", "");
            JsonArray incorrect = raw.getJsonArray("incorrectAnswers");

            List<String> options = new ArrayList<>();
            options.add(correct);
            if (incorrect != null) {
                // Convertim cada resposta incorrecta des del JSON a text pla
                for (var value : incorrect) {
                    options.add(((JsonString) value).getString());
                }
            }
            // Barregem les opcions per mostrar-les en ordre aleatori a l'usuari
            Collections.shuffle(options);

            return TriviaQuestionDto.builder()
                    .id(raw.getString("id", ""))
                    .category(raw.getString("category", ""))
                    .questionText(text)
                    .correctAnswer(correct)
                    .options(options)
                    .build();
        }
    }
}
