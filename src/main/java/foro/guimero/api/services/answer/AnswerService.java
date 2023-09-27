package foro.guimero.api.services.answer;

import foro.guimero.api.domain.answer.*;
import foro.guimero.api.domain.response.ObjectPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerService {
    AnswerShowData save(AnswerRegisterData answerRegisterData);
    Page<AnswerShowData> findAllByUsername(String username, Pageable paging);
    Page<AnswerShowData> findAllByTopic(Long topicId, Pageable paging);
    Page<AnswerShowData> findAll(boolean active, Pageable paging);
    AnswerShowData findById(Long id);
    AnswerShowData update(AnswerUpdateData answerUpdateData);
    ObjectPlus<Boolean> toggleAnswer (Long id);
}
