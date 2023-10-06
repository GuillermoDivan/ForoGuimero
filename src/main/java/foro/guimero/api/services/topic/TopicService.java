package foro.guimero.api.services.topic;

import foro.guimero.api.domain.response.ObjectPlus;
import foro.guimero.api.domain.topic.TopicRegisterData;
import foro.guimero.api.domain.topic.TopicShowData;
import foro.guimero.api.domain.topic.TopicUpdateData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {
    TopicShowData save(TopicRegisterData topicRegisterData);
    Page<TopicShowData> findAll(boolean active, Pageable paging);
    TopicShowData findById(Long id);
    TopicShowData findByCourse(Long id);
    TopicShowData update(TopicUpdateData topicUpdateData);
    ObjectPlus<Boolean> toggleTopic (Long id);
    boolean delete(Long id);
}
