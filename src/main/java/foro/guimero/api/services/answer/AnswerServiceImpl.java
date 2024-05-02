package foro.guimero.api.services.answer;
import foro.guimero.api.domain.answer.*;
import foro.guimero.api.domain.response.ObjectPlus;
import foro.guimero.api.repositories.AnswerRepository;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final AuthenticationService authenticationService;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             AuthenticationService authenticationService) {
        this.answerRepository = answerRepository;
        this.authenticationService = authenticationService;
    }


    @Override
    public AnswerShowData save(AnswerRegisterData answerRegisterData) {
        Answer answer = new Answer(answerRegisterData);
        this.answerRepository.save(answer);
        return new AnswerShowData(answer);
    }

    @Override
    public Page<AnswerShowData> findAll(boolean active, Pageable paging) {
        return this.answerRepository.findAllByActive(active, paging).map(AnswerShowData::new);
    }

    @Override
    public Page<AnswerShowData> findByAnswerByIsLiked(Long userId, boolean isLiked, Pageable paging) {
        return this.answerRepository.findByAnswerByIsLiked(userId, isLiked, paging).map(AnswerShowData::new);
    }

    @Override
    public Page<AnswerShowData> findAllByUsername(String username, Pageable paging){
        return this.answerRepository.findAllByUsername(username, paging).map(AnswerShowData::new);
    }

    @Override
    public Page<AnswerShowData> findAllByTopic(Long topicId, Pageable paging){
        return this.answerRepository.findAllByTopic(topicId, paging).map(AnswerShowData::new);
    }

    @Override
    public AnswerShowData findById(Long id) throws EntityNotFoundException {
        var showData = this.answerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new AnswerShowData(showData);
    }

    @Override
    public AnswerShowData update(AnswerUpdateData answerUpdateData) throws EntityNotFoundException {
        if (authenticationService.isAdminModOrSelf(answerUpdateData.userId())) {
            var answer = this.answerRepository.findById(answerUpdateData.id()).orElseThrow(EntityNotFoundException::new);
            if (answer.isActive()) {
                answer.setMessage(answerUpdateData.message());
                answer.setCreationDate(LocalDateTime.now());
            }
            this.answerRepository.save(answer);
            return new AnswerShowData(answer);
        } return null;
    }

    @Override
    public ObjectPlus<Boolean> toggleAnswer (Long id) throws EntityNotFoundException {
        var result = new ObjectPlus<Boolean>();
        Answer answerToToggle = this.answerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (authenticationService.isAdminOrSelf(answerToToggle.getAuthor().getId())) {
        answerToToggle.setActive(!answerToToggle.isActive());
        this.answerRepository.save(answerToToggle);
            result.setSuccess(true);
            result.setObject(answerToToggle.isActive()); //Devuelve boolean como objeto... diferente del success.
            return result;
        } return result;
    }

    @Override
    public boolean delete (Long id){
        this.answerRepository.deleteById(id);
        return true;
    }
}
