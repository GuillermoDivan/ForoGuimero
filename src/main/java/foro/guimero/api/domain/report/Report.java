package foro.guimero.api.domain.report;
import foro.guimero.api.domain.answer.Answer;
import foro.guimero.api.domain.topic.Topic;
import foro.guimero.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Reports")
public class Report {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topics_id")
    private Topic topic;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "answers_id")
    private Answer answer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
    private LocalDateTime date;
    private String reportedText;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Report(ReportRegisterData reportRegisterData) {
        if (reportRegisterData.topicId() != null) {
        this.topic = new Topic();
        this.topic.setId(reportRegisterData.topicId()); }
        if (reportRegisterData.answerId() != null) {
        this.answer = new Answer();
        this.answer.setId(reportRegisterData.answerId()); }
        this.user = new User();
        this.user.setId(reportRegisterData.userId());
        this.date = LocalDateTime.now();
        this.reportedText = reportRegisterData.reportedText();
        this.status = Status.PENDIENTE;
    }

}
