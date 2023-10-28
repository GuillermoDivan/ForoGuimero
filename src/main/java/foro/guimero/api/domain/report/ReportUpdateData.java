package foro.guimero.api.domain.report;
import jakarta.validation.constraints.NotNull;

public record ReportUpdateData(@NotNull Long id, @NotNull Long userId,
                               @NotNull Status status) {
}
