package foro.guimero.api.domain.topic;
import foro.guimero.api.domain.answer.Answer;
import foro.guimero.api.domain.course.Course;
import foro.guimero.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "Topics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column (unique = true)
    private String title;
    //@Column (unique = true)
    private String message;
    private LocalDateTime creationDate;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    private List<Answer> answers;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;
    private boolean active;

    public Topic(TopicRegisterData topicData){
        this.title = topicData.title();
        this.message = topicData.message();
        this.author = new User();
        this.author.setId(topicData.userId());
        this.course = new Course();
        this.course.setId(topicData.courseId());
        this.creationDate = LocalDateTime.now();
        this.active = true;
    }
}
