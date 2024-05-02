package foro.guimero.api.controllers;

import foro.guimero.api.domain.answer.*;
import foro.guimero.api.services.answer.AnswerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AnswerShowData> registerAnswer
            (@RequestBody @Valid AnswerRegisterData answerRegisterData,
             UriComponentsBuilder uriBuilder) {
        var data = this.answerService.save(answerRegisterData);
        var topicId = answerRegisterData.topicId();
        URI url = uriBuilder.path("/answers/{topicId}/{id}").buildAndExpand(data.topicId(),data.id()).toUri();
        return ResponseEntity.created(url).body(data);
    }

    @GetMapping
    public ResponseEntity<Page<AnswerShowData>> findAnswersList
            (@PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(answerService.findAll(true, paging));
    }

    @GetMapping("/inactive")
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Page<AnswerShowData>> findInactiveTopicList
            (@PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(answerService.findAll(false, paging));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<AnswerShowData>> findAnswersListByUsername
            (@PathVariable String username, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(answerService.findAllByUsername(username, paging));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<Page<AnswerShowData>> findAnswersListByTopic
            (@PathVariable Long topicId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(answerService.findAllByTopic(topicId, paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerShowData> findAnswerById(@PathVariable Long id){
        return ResponseEntity.ok(answerService.findById(id));
    }

    @GetMapping("/user/{userId}/likes")
    ResponseEntity<Page<AnswerShowData>> findLikedAnswerList
            (@PathVariable Long userId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(answerService.findByAnswerByIsLiked(userId,true, paging));
    }

    @GetMapping("/user/{userId}/dislikes")
    ResponseEntity<Page<AnswerShowData>> findDislikedAnswerList
            (@PathVariable Long userId, @PageableDefault(size = 3) Pageable paging) {
        return ResponseEntity.ok(answerService.findByAnswerByIsLiked(userId,false, paging));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<AnswerShowData> updateAnswer(@RequestBody @Valid
                                                     AnswerUpdateData answerUpdateData){
        var result = this.answerService.update(answerUpdateData);
        if (result != null) {
            return ResponseEntity.ok(result);}
        else
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> toggleAnswer(@PathVariable Long id){
        var toggledAnswer = answerService.toggleAnswer(id);
        if (toggledAnswer.isSuccess()) {
                return ResponseEntity.ok(toggledAnswer);
            }  else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}