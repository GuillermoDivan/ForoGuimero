package foro.guimero.api.domain.topic;

import java.time.LocalDate;

public record TopicShowData(Long id, String title, String message,
                            String author, String course, LocalDate creationDate,
                            String status) {

    public TopicShowData(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getAuthor(),
                topic.getCourse(), topic.getCreationDate(), topic.getStatus());
    }
}
