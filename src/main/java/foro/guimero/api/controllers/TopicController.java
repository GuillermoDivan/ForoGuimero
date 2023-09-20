package foro.guimero.api.controllers;

import foro.guimero.api.domain.topic.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import foro.guimero.api.services.topic.TopicService;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicShowData> registerTopic
            (@RequestBody @Valid TopicRegisterData topicRegisterData,
             UriComponentsBuilder uriBuilder) {
        var data = this.topicService.save(topicRegisterData);
        URI url = uriBuilder.path("/topics/id/{id}").buildAndExpand(data.id()).toUri();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping
    public ResponseEntity<Page<TopicShowData>> findTopicList
            (@PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(topicService.findAll(true, paging));
    }

    @GetMapping("/inactive")
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Page<TopicShowData>> findInactiveTopicList
            (@PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(topicService.findAll(false, paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicShowData> findTopicById(@PathVariable Long id){
        return ResponseEntity.ok(topicService.findById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicShowData> updateTopic(@RequestBody @Valid
                                                     TopicUpdateData topicUpdateData){
        return ResponseEntity.ok(this.topicService.update(topicUpdateData));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Boolean> toggleTopic(@PathVariable Long id){
        boolean toggledTopic = topicService.toggleTopic(id);
        if (!toggledTopic){ return ResponseEntity.noContent().build();}
        else {return ResponseEntity.ok().build();}
    }
}

