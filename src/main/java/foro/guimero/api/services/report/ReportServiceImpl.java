package foro.guimero.api.services.report;
import foro.guimero.api.domain.report.*;
import foro.guimero.api.repositories.ReportRepository;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final AuthenticationService authenticationService;

    public ReportServiceImpl(ReportRepository reportRepository, AuthenticationService authenticationService) {
        this.reportRepository = reportRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public ReportShowData save(ReportRegisterData reportRegisterData) {
        Report report = new Report(reportRegisterData);
        this.reportRepository.save(report);
        return new ReportShowData(report);
    }

    @Override
    public Page<ReportShowData> findAllByUserId(Long userId, Pageable paging) {
        return this.reportRepository.findAllByUserId(userId, paging).map(ReportShowData::new);
    }

    @Override
    public Page<ReportShowData> findAllByTopic(Long topicId, Pageable paging) {
        return this.reportRepository.findAllByTopic( topicId, paging).map(ReportShowData::new);
    }

    @Override
    public Page<ReportShowData> findAllByAnswer(Long answerId, Pageable paging) {
        return this.reportRepository.findAllByAnswer(answerId, paging).map(ReportShowData::new);
    }

    @Override
    public Page<ReportShowData> findAllByStatus(Status status, Pageable paging) {
        return this.reportRepository.findAllByStatus(status, paging).map(ReportShowData::new);
    }

    @Override
    public ReportShowData findById(Long id) {
        var showData = this.reportRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new ReportShowData(showData);
    }

    @Override
    public ReportShowData update(ReportUpdateData reportUpdateData) throws EntityNotFoundException{
        if (authenticationService.isAdminOrMod(reportUpdateData.userId())) {
            var report = this.reportRepository.findById(reportUpdateData.id())
                    .orElseThrow(EntityNotFoundException::new);
            if (report != null) {
                report.setStatus(reportUpdateData.status());
                report.setDate(LocalDateTime.now());
            }
            this.reportRepository.save(report);
            return new ReportShowData(report);
        } return null;
    }

    @Override
    public boolean delete(Long id) {
        this.reportRepository.deleteById(id);
        return true;
    }

}
