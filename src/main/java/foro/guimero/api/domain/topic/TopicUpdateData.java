package foro.guimero.api.domain.topic;
import jakarta.validation.constraints.NotNull;

public record TopicUpdateData(
        @NotNull Long id, String title, String message,
        @NotNull Long userId, Status status, @NotNull Long courseId) {}
