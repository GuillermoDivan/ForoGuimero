package foro.guimero.api.services.likeDislike;
import foro.guimero.api.domain.likeAndDislike.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeDislikeService {
    LikeDislikeShowData save(LikeDislikeRegisterData likeDislikeRegisterData);
    Long countByAnswerByIsLiked(Long answerId, boolean isLiked);
    Long countByTopicByIsLiked(Long topicId, boolean isLiked);
    LikeDislikeShowData update(LikeDislikeUpdateData likeDislikeUpdateData);
}
