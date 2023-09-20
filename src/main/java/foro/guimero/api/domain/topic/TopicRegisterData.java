package foro.guimero.api.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicRegisterData(
        @NotBlank String title,
        @NotBlank String message,
        @NotNull Long userId,
        @NotBlank String course) {}


