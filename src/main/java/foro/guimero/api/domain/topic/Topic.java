package foro.guimero.api.domain.topic;
import foro.guimero.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table (name = "Topics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true)
    private String title;
    @Column (unique = true)
    private String message;
    private LocalDate creationDate;
    private String status;
    @ManyToOne
    private User author;
    private String course;
    private boolean active;

    public Topic(TopicRegisterData topicData){
        this.title = topicData.title();
        this.message = topicData.message();
        this.author = new User();
        this.author.setId(topicData.userId());
        this.course = topicData.course();
        this.active = true;
    }

    public Topic(TopicUpdateData topicData){
        this.id = topicData.id();
        this.title = topicData.title();
        this.message = topicData.message();
        this.status = topicData.status();
    }

}
