package foro.guimero.api.domain.answer;
import com.fasterxml.jackson.annotation.JsonFormat;
import foro.guimero.api.domain.likeAndDislike.LikeDislike;
import foro.guimero.api.domain.topic.Topic;
import foro.guimero.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Answers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column (unique = true)
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime creationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topics_id")
    private Topic topic;
    private boolean active;

    public Answer(AnswerRegisterData answerData){
        this.message = answerData.message();
        this.author = new User();
        this.author.setId(answerData.userId());
        this.topic = new Topic();
        this.topic.setId(answerData.topicId());
        this.creationDate = LocalDateTime.now();
        this.active = true;
    }
}

