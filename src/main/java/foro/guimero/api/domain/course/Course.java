package foro.guimero.api.domain.course;
import foro.guimero.api.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean active;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private List<Topic> topicList;

    public Course(CourseRegisterData courseRegisterData) {
        this.name = courseRegisterData.name();
        this.active = true;
    }

}
