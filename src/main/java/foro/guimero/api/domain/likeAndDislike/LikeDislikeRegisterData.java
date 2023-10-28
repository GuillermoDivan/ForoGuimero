package foro.guimero.api.domain.likeAndDislike;
import jakarta.validation.constraints.NotNull;

public record LikeDislikeRegisterData(
        Long answerId,
        Long topicId,
        @NotNull Long userId,
        boolean isLiked
) {
}
