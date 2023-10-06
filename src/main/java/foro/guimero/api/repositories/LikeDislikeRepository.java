package foro.guimero.api.repositories;

import foro.guimero.api.domain.likeAndDislike.LikeDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {
    @Query(""" 
            SELECT count(L) FROM LikeDislike L 
            WHERE L.isLiked = :isLiked  AND L.answer.id = :answerId 
            """)
    Long countByAnswerByIsLiked(Long answerId, boolean isLiked);

    @Query(""" 
            SELECT count(L) FROM LikeDislike L 
            WHERE L.isLiked = :isLiked  AND L.topic.id = :topicId 
            """)
    Long countByTopicByIsLiked(Long topicId, boolean isLiked);
}
