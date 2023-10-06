package foro.guimero.api.controllers;
import foro.guimero.api.domain.likeAndDislike.LikeDislikeRegisterData;
import foro.guimero.api.domain.likeAndDislike.LikeDislikeShowData;
import foro.guimero.api.domain.likeAndDislike.LikeDislikeUpdateData;
import foro.guimero.api.services.likeDislike.LikeDislikeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/likesDislikes")
public class LikeDislikeController {

    private final LikeDislikeService likeDislikeService;
    public LikeDislikeController(LikeDislikeService likeDislikeService) {
        this.likeDislikeService = likeDislikeService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<LikeDislikeShowData> registerLikeDislike
            (@RequestBody @Valid LikeDislikeRegisterData likeDislikeRegisterData,
             UriComponentsBuilder uriBuilder) {
        var data = this.likeDislikeService.save(likeDislikeRegisterData);
        URI url = uriBuilder.path("/likesDislikes/{id}").buildAndExpand(data.id()).toUri();
        return ResponseEntity.created(url).body(data);
    }

    @GetMapping("/answer/{answerId}/likes")
    public ResponseEntity<Long> findLikesByAnswer (@PathVariable Long answerId) {
        return ResponseEntity.ok(likeDislikeService.countByAnswerByIsLiked(answerId, true));
    }

    @GetMapping("/answer/{answerId}/dislikes")
    public ResponseEntity<Long> findDislikesByAnswer (@PathVariable Long answerId) {
        return ResponseEntity.ok(likeDislikeService.countByAnswerByIsLiked(answerId, false));
    }

    @GetMapping("/topic/{topicId}/likes")
    public ResponseEntity<Long> findLikesByTopic (@PathVariable Long topicId) {
        return ResponseEntity.ok(likeDislikeService.countByTopicByIsLiked(topicId, true));
    }

    @GetMapping("/topic/{topicId}/dislikes")
    public ResponseEntity<Long> findDislikesByTopic (@PathVariable Long topicId) {
        return ResponseEntity.ok(likeDislikeService.countByTopicByIsLiked(topicId, false));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<LikeDislikeShowData> updateLikeDislike(@RequestBody @Valid
                                                                 LikeDislikeUpdateData likeDislikeUpdateData){
        var result = this.likeDislikeService.update(likeDislikeUpdateData);
        if (result != null) {
            return ResponseEntity.ok(result);}
        else
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}
    }


}
