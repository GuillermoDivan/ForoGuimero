package foro.guimero.api.repositories;
import foro.guimero.api.domain.topic.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Page<Topic> findAllByActive(boolean active, Pageable paging);
    Optional<Topic> findByIdAndActive(Long id, Boolean active);
    Optional<Topic> findByCourseIdAndActive(Long id, Boolean active);

    @Query(""" 
            SELECT L.topic FROM LikeDislike L 
            WHERE L.isLiked = :isLiked  AND L.user.id = :userId
            """)
    Page<Topic> findByTopicByIsLiked(Long userId, boolean isLiked, Pageable paging);
}
