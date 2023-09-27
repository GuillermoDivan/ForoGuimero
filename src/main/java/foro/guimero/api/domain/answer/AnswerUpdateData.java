package foro.guimero.api.domain.answer;
import jakarta.validation.constraints.NotNull;

public record AnswerUpdateData(
        @NotNull Long id, String message, Long userId) {}
