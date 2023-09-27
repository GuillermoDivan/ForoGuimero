package foro.guimero.api.services.answer;
import foro.guimero.api.domain.answer.*;
import foro.guimero.api.domain.response.ObjectPlus;
import foro.guimero.api.repositories.AnswerRepository;
import foro.guimero.api.services.authentication.AuthenticationService;
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
    public Page<AnswerShowData> findAllByUsername(String username, Pageable paging){
        return this.answerRepository.findAllByUsername(username, paging).map(AnswerShowData::new);
    }

    @Override
    public Page<AnswerShowData> findAllByTopic(Long topicId, Pageable paging){
        return this.answerRepository.findAllByTopic(topicId, paging).map(AnswerShowData::new);
    }

    @Override
    public AnswerShowData findById(Long id) {
        var showData = this.answerRepository.findById(id).orElse(null);
        return new AnswerShowData(showData);
    }

    @Override
    public AnswerShowData update(AnswerUpdateData answerUpdateData) {
        if (authenticationService.isAdminModOrSelf(answerUpdateData.userId())) {
            var answer = this.answerRepository.findById(answerUpdateData.id()).orElse(null);
            if (answer.isActive()) {
                answer.setMessage(answerUpdateData.message());
                answer.setCreationDate(LocalDateTime.now());
            }
            this.answerRepository.save(answer);
            return new AnswerShowData(answer);
        } return null;
    }

    @Override
    public ObjectPlus<Boolean> toggleAnswer (Long id) {
        var result = new ObjectPlus<Boolean>();
        Answer answerToToggle = this.answerRepository.findById(id).orElse(null);
        if (authenticationService.isAdminOrSelf(answerToToggle.getAuthor().getId())) {
        answerToToggle.setActive(!answerToToggle.isActive());
        this.answerRepository.save(answerToToggle);
            result.setSuccess(true);
            result.setObject(answerToToggle.isActive()); //Devuelve boolean como objeto... diferente del success.
            return result;
        } return result;
    }

    public boolean delete (Long id){
        this.answerRepository.deleteById(id);
        return true;
    }
}
