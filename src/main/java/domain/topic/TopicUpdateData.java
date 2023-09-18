package domain.topic;

import jakarta.validation.constraints.NotBlank;

public record TopicUpdateData(
        @NotBlank Long id, String title, String message, String status) {}
