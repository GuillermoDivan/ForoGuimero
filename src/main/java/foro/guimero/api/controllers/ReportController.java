package foro.guimero.api.controllers;
import foro.guimero.api.domain.report.*;
import foro.guimero.api.services.report.ReportService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    public ReportController(ReportService reportService) { this.reportService = reportService; }

    @PostMapping
    @Transactional
    public ResponseEntity<ReportShowData> registerReport
            (@RequestBody @Valid ReportRegisterData reportRegisterData,
             UriComponentsBuilder uriBuilder) {
        var data = this.reportService.save(reportRegisterData);
        var reportId = data.id();
        URI url = uriBuilder.path("/{id}").buildAndExpand(data.id()).toUri();
        return ResponseEntity.created(url).body(data);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReportShowData>> findReportListByStatus
            (@PathVariable Status status, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(reportService.findAllByStatus(status, paging));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReportShowData>> findReportListByUsername
            (@PathVariable Long userId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(reportService.findAllByUserId(userId, paging));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<Page<ReportShowData>> findReportListByTopic
            (@PathVariable Long topicId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(reportService.findAllByTopic(topicId, paging));
    }

    @GetMapping("/answer/{answerId}")
    public ResponseEntity<Page<ReportShowData>> findReportListByAnswer
            (@PathVariable Long answerId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(reportService.findAllByAnswer(answerId, paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportShowData> findReportById(@PathVariable Long id){
        return ResponseEntity.ok(reportService.findById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReportShowData> updateReport(@RequestBody @Valid
                                                       ReportUpdateData reportUpdateData) {
            var result = this.reportService.update(reportUpdateData);
            if (result != null) {
                return ResponseEntity.ok(result);}
            else
            {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}
        }

}
