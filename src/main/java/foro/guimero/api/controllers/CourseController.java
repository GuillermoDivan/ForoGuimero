package foro.guimero.api.controllers;
import foro.guimero.api.domain.course.*;
import foro.guimero.api.services.answer.course.CourseService;
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
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Transactional
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseShowData> registerCourse
            (@RequestBody @Valid CourseRegisterData courseRegisterData,
             UriComponentsBuilder uriBuilder) {
        var course = this.courseService.save(courseRegisterData);
        URI url = uriBuilder.path("/courses/{id}")
                .buildAndExpand(course.id()).toUri();
        return ResponseEntity.created(url).body(course);
    }

    @GetMapping
    public ResponseEntity<Page<CourseShowData>> findCourseList
            (@PageableDefault(size = 5) Pageable paging) {
        return ResponseEntity.ok(courseService.findAll(true, paging));
    }

    @GetMapping("/inactive")
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Page<CourseShowData>> findInactiveCourseList
            (@PageableDefault(size = 5) Pageable paging) {
        return ResponseEntity.ok(courseService.findAll(false, paging));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CourseShowData> findCourseByName
            (@PathVariable String name) {
        return ResponseEntity.ok(courseService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseShowData> findCourseById(@PathVariable Long id){
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<CourseShowData> updateAnswer(@RequestBody @Valid
                                                       CourseUpdateData courseUpdateData){
        var result = this.courseService.update(courseUpdateData);
        if (result != null) {
            return ResponseEntity.ok(result);}
        else
        {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> toggleCourse(@PathVariable Long id){
        var toggledCourse = courseService.toggleCourse(id);
        if (toggledCourse.isSuccess()) {
            return ResponseEntity.ok(toggledCourse);
        }  else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

