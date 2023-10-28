package foro.guimero.api.repositories;
import foro.guimero.api.domain.answer.Answer;
import foro.guimero.api.domain.report.Report;
import foro.guimero.api.domain.report.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository <Report, Long> {
    Page<Report> findAllByStatus(Status status, Pageable paging);

    @Query("Select A From Report A where A.user.id =:userId")
    Page<Report> findAllByUserId(Long userId, Pageable paging);

    @Query("Select A From Report A where A.topic.id =:topicId")
    Page<Report> findAllByTopic(Long topicId, Pageable paging);

    @Query("Select A From Report A where A.answer.id =:answerId")
    Page<Report> findAllByAnswer(Long answerId, Pageable paging);
}
