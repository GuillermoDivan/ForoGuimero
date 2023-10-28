package foro.guimero.api.services.topic;

import foro.guimero.api.domain.response.ObjectPlus;
import foro.guimero.api.domain.topic.*;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import foro.guimero.api.repositories.TopicRepository;

import java.time.LocalDateTime;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final AuthenticationService authenticationService;

    public TopicServiceImpl(TopicRepository topicRepository,
                            AuthenticationService authenticationService) {
        this.topicRepository = topicRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public TopicShowData save(TopicRegisterData topicRegisterData) {
        Topic topic = new Topic(topicRegisterData);
        this.topicRepository.save(topic);
        return new TopicShowData(topic);
    }

    @Override
    public Page<TopicShowData> findAll(boolean active, Pageable paging) {
        return this.topicRepository.findAllByActive(active, paging).map(TopicShowData::new);
    }

    @Override
    public Page<TopicShowData> findByTopicByIsLiked(Long userId, boolean isLiked, Pageable paging) {
        return this.topicRepository.findByTopicByIsLiked(userId, isLiked, paging).map(TopicShowData::new);
    }

    @Override
    public TopicShowData findById(Long id) throws EntityNotFoundException {
        Topic topic = this.topicRepository.findByIdAndActive(id, true)
                .orElseThrow(EntityNotFoundException::new);
        return new TopicShowData(topic);
    }

    @Override
    public TopicShowData findByCourse(Long id) throws EntityNotFoundException {
        Topic topic = this.topicRepository.findByCourseIdAndActive(id, true)
               .orElseThrow(EntityNotFoundException::new);
        return new TopicShowData(topic);
    }

    @Override
    public TopicShowData update(TopicUpdateData topicUpdateData)
            throws EntityNotFoundException {
        if (authenticationService.isAdminOrSelf(topicUpdateData.userId())) {
            var topic = this.topicRepository.findById(topicUpdateData.id())
                    .orElseThrow(EntityNotFoundException::new);

            if (topic.isActive()) {
                if (topicUpdateData.title() != null) {
                    topic.setTitle(topicUpdateData.title());
                }
                if (topicUpdateData.message() != null) {
                    topic.setMessage(topicUpdateData.message());
                }
                if (topicUpdateData.status() != null) {
                    topic.setStatus(topicUpdateData.status());
                }
                topic.setCreationDate(LocalDateTime.now());
                this.topicRepository.save(topic);
            }
            return new TopicShowData(topic);
        }
        return null;
    }

    @Override
    public ObjectPlus<Boolean> toggleTopic(Long id) throws EntityNotFoundException {
        var result = new ObjectPlus<Boolean>();
        Topic topicToToggle = this.topicRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (authenticationService.isAdminModOrSelf(topicToToggle.getAuthor().getId())) {
            topicToToggle.setActive(!topicToToggle.isActive());
            this.topicRepository.save(topicToToggle);
            result.setSuccess(true);
            result.setObject(topicToToggle.isActive()); //Devuelve boolean como objeto... diferente del success.
            return result;
        }
        return result;
    }

    @Override
    public boolean delete(Long id) {
        this.topicRepository.deleteById(id);
        return true;
    }
}






