package foro.guimero.api.domain.report;
import jakarta.validation.constraints.NotNull;

public record ReportRegisterData(Long answerId,
                                 Long topicId,
                                 @NotNull Long userId,
                                 @NotNull String reportedText) {
}
