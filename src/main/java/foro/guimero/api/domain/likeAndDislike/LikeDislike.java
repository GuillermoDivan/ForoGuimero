package foro.guimero.api.domain.likeAndDislike;

import foro.guimero.api.domain.answer.Answer;
import foro.guimero.api.domain.topic.Topic;
import foro.guimero.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "LikesDislikes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDislike {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answers_id")
    private Answer answer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topics_id")
    private Topic topic;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
    private LocalDateTime date;
    private boolean isLiked;

    public LikeDislike(LikeDislikeRegisterData likeDislikeData){
        if (likeDislikeData.answerId() != null && likeDislikeData.answerId() != 0){
        this.answer = new Answer();
        this.answer.setId(likeDislikeData.answerId());}
        if (likeDislikeData.topicId() != null && likeDislikeData.topicId() != 0){
        this.topic = new Topic();
        this.topic.setId(likeDislikeData.topicId());}
        this.user = new User();
        this.user.setId(likeDislikeData.userId());
        this.date = LocalDateTime.now();
        this.isLiked = likeDislikeData.isLiked();
    }
}
