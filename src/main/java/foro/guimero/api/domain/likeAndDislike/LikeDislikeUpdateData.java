package foro.guimero.api.domain.likeAndDislike;
import jakarta.validation.constraints.NotNull;

public record LikeDislikeUpdateData(@NotNull Long id,
                                    @NotNull Long userId,
                                    boolean isLiked) {
}
