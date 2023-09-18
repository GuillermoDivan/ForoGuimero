package services;

import domain.topic.TopicRegisterData;
import domain.topic.TopicShowData;
import domain.topic.TopicUpdateData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TopicService {
    TopicShowData save(TopicRegisterData topicRegisterData);
    Page<TopicShowData> findAll(boolean active, Pageable paging);
    TopicShowData findById(Long id);
    TopicShowData update(TopicUpdateData topicUpdateData);
    boolean toggleTopic(Long id);
    boolean delete(Long id);
}
