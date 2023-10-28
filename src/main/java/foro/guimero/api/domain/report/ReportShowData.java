package foro.guimero.api.domain.report;
import java.time.LocalDateTime;

public record ReportShowData(Long id, Long answerId,
                             Long topicId, Long userId,
                             LocalDateTime date,
                             Status status) {

    public ReportShowData(Report report) {
        this(report.getId(),
                report.getAnswer() != null ? report.getAnswer().getId() : 0,
                report.getTopic() != null ? report.getTopic().getId() : 0,
                report.getUser().getId(),
                report.getDate(),
                report.getStatus());
    }

}
