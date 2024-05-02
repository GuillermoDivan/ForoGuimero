package foro.guimero.api.services.likeDislike;
import foro.guimero.api.domain.likeAndDislike.*;
import foro.guimero.api.repositories.LikeDislikeRepository;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class LikeDislikeServiceImpl implements LikeDislikeService{

    private final LikeDislikeRepository likeDislikeRepository;
    private final AuthenticationService authenticationService;

    public LikeDislikeServiceImpl(LikeDislikeRepository likeDislikeRepository,
                                  AuthenticationService authenticationService) {
        this.likeDislikeRepository = likeDislikeRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public LikeDislikeShowData save(LikeDislikeRegisterData likeDislikeRegisterData) {
        LikeDislike likeDislike = new LikeDislike(likeDislikeRegisterData);
        this.likeDislikeRepository.save(likeDislike);
        return new LikeDislikeShowData(likeDislike);
    }

    @Override
    public Long countByAnswerByIsLiked(Long answerId, boolean isLiked) {
        return likeDislikeRepository.countByAnswerByIsLiked(answerId, isLiked);
    }

    @Override
    public Long countByTopicByIsLiked(Long topicId, boolean isLiked) {
        return likeDislikeRepository.countByTopicByIsLiked(topicId, isLiked);
    }

    @Override
    public LikeDislikeShowData update(LikeDislikeUpdateData likeDislikeUpdateData)
            throws EntityNotFoundException {
       if (authenticationService.isSelf(likeDislikeUpdateData.userId())) {
            var likeDislike = this.likeDislikeRepository.findById(likeDislikeUpdateData.id())
                    .orElseThrow(EntityNotFoundException::new);
            likeDislike.setLiked(likeDislikeUpdateData.isLiked());
            likeDislike.setDate(LocalDateTime.now());
            this.likeDislikeRepository.save(likeDislike);
            return new LikeDislikeShowData(likeDislike);
        } return null;
    }

    @Override
    public boolean delete(Long id) {
        this.likeDislikeRepository.deleteById(id);
        return true;
    }

}
