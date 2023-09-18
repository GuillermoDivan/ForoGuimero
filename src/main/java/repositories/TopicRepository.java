package repositories;

import domain.topic.Topic;
import domain.topic.TopicShowData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Page<Topic> findAllbyActive(boolean active, Pageable paging);
    Optional<Topic> findByIdAndActive(Long id, Boolean active);

}
