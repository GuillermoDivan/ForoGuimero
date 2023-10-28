package foro.guimero.api.controllers;

import foro.guimero.api.domain.likeAndDislike.LikeDislikeShowData;
import foro.guimero.api.domain.topic.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
        URI url = uriBuilder.path("/topics/{id}").buildAndExpand(data.id()).toUri();
        return ResponseEntity.created(url).body(data);
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
    public ResponseEntity<TopicShowData> findTopicById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findById(id));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<TopicShowData> findTopicByCourse(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.findByCourse(id));
    }

    @GetMapping("/user/{userId}/likes")
    ResponseEntity<Page<TopicShowData>> findLikedTopicList
            (@PathVariable Long userId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(topicService.findByTopicByIsLiked(userId,true, paging));
    }

    @GetMapping("/user/{userId}/dislikes")
    ResponseEntity<Page<TopicShowData>> findDislikedTopicList
            (@PathVariable Long userId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(topicService.findByTopicByIsLiked(userId,false, paging));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicShowData> updateTopic(@RequestBody @Valid
                                                     TopicUpdateData topicUpdateData) {
        var result = this.topicService.update(topicUpdateData);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> toggleTopic(@PathVariable Long id) {
        var toggledTopic = topicService.toggleTopic(id);
        if (toggledTopic.isSuccess()) {
            return ResponseEntity.ok(toggledTopic);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

