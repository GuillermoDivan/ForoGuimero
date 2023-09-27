package foro.guimero.api.domain.topic;
import java.time.LocalDateTime;

public record TopicShowData(Long id, String title, String message,
                            Long userId, String course, LocalDateTime creationDate,
                            String status) {

    public TopicShowData(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getAuthor().getId(),
                topic.getCourse(), topic.getCreationDate(), topic.getStatus());
    }
}
