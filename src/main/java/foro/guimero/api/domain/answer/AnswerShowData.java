package foro.guimero.api.domain.answer;
import java.time.LocalDateTime;

public record AnswerShowData(Long id, String message,
                             Long userId, Long topicId, LocalDateTime creationDate) {

    public AnswerShowData(Answer answer) {
        this(answer.getId(), answer.getMessage(), answer.getAuthor().getId(),
                answer.getTopic().getId(), answer.getCreationDate());
    }
}

