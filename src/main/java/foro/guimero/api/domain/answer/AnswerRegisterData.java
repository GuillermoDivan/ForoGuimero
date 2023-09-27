package foro.guimero.api.domain.answer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record AnswerRegisterData(
        @NotBlank String message,
        @NotNull Long userId,
        @NotNull Long topicId) {}

