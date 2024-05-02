package foro.guimero.api.services.report;
import foro.guimero.api.domain.report.ReportRegisterData;
import foro.guimero.api.domain.report.ReportUpdateData;
import foro.guimero.api.domain.report.ReportShowData;
import foro.guimero.api.domain.report.Status;
import foro.guimero.api.domain.response.ObjectPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReportService {
    ReportShowData save(ReportRegisterData ReportRegisterData);
    Page<ReportShowData> findAllByUserId(Long userId, Pageable paging);
    Page<ReportShowData> findAllByTopic(Long topicId, Pageable paging);
    Page<ReportShowData> findAllByAnswer(Long answerId, Pageable paging);
    Page<ReportShowData> findAllByStatus(Status status, Pageable paging);
    ReportShowData findById(Long id);
    ReportShowData update(ReportUpdateData ReportUpdateData);
    boolean delete(Long id);
}
