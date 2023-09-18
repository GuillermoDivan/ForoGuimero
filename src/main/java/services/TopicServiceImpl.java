package services;
import domain.topic.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repositories.TopicRepository;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public TopicShowData save(TopicRegisterData topicRegisterData) {
        Topic topic = new Topic(topicRegisterData);
        this.topicRepository.save(topic); //"Se llama al método save del repository pasando la entidad a guardar"
        return new TopicShowData(topic);
    }

    @Override
    public Page<TopicShowData> findAll(boolean active, Pageable paging) {
        return this.topicRepository.findAllbyActive(active, paging).map(TopicShowData::new);
        //map transforma la colección de page entidad a page de dto show.
    }

    @Override
    public TopicShowData findById(Long id) {
        Topic topic = this.topicRepository.findByIdAndActive(id, true).
                orElseThrow(() -> new EntityNotFoundException());
        return new TopicShowData(topic);
    }

    @Override
    public TopicShowData update(TopicUpdateData topicUpdateData) {
        Topic newTopic = new Topic(topicUpdateData);
        Topic oldTopic = this.topicRepository.findById(newTopic.getId()).orElse(null);

        if (oldTopic.isActive()) {
            if (newTopic.getTitle() != null) {
                oldTopic.setTitle(newTopic.getTitle());
            }
            if (newTopic.getMessage() != null) {
                oldTopic.setMessage(newTopic.getMessage());
            }
            if (newTopic.getStatus() != null) {
                oldTopic.setStatus(newTopic.getStatus());
            }
        } return new TopicShowData(oldTopic);
    }

        @Override
        public boolean toggleTopic (Long id){
            Topic topicToToggle = this.topicRepository.findById(id).orElse(null);
            topicToToggle.setActive(!topicToToggle.isActive());
            this.topicRepository.save(topicToToggle);
            return topicToToggle.isActive();
        }

        @Override
        public boolean delete (Long id){
            this.topicRepository.deleteById(id);
            return true;
        }
    }






