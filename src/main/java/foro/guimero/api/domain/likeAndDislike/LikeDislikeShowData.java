package foro.guimero.api.domain.likeAndDislike;

import java.time.LocalDateTime;

public record LikeDislikeShowData(Long id, Long answerId,
                                  Long topicId, Long userId,
                                  LocalDateTime date,
                                  boolean isLiked) {

    public LikeDislikeShowData(LikeDislike likeDislike) {
        this(likeDislike.getId(),
        likeDislike.getAnswer() != null? likeDislike.getAnswer().getId() : 0,
        likeDislike.getTopic() != null? likeDislike.getTopic().getId() : 0,
                likeDislike.getUser().getId(),
                likeDislike.getDate(),
                likeDislike.isLiked());
    }
}
