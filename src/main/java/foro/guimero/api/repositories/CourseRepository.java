package foro.guimero.api.repositories;

import foro.guimero.api.domain.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllByActive(boolean active, Pageable paging);
    Optional<Course> findByName(String name);

}
