package foro.guimero.api.repositories;
import foro.guimero.api.domain.answer.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findAllByActive(Boolean active, Pageable paging);

    @Query("Select A From Answer A where A.author.username =:username")
    Page<Answer> findAllByUsername(String username, Pageable paging);

    @Query("Select A From Answer A where A.topic.id =:topicId")
    Page<Answer> findAllByTopic(Long topicId, Pageable paging);

    @Query(""" 
            SELECT L.answer FROM LikeDislike L 
            WHERE L.isLiked = :isLiked  AND L.user.id = :userId
            """)
    Page<Answer> findByAnswerByIsLiked(Long userId, boolean isLiked, Pageable paging);
}
