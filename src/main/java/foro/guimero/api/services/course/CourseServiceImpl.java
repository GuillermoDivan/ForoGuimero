package foro.guimero.api.services.course;
import foro.guimero.api.domain.course.*;
import foro.guimero.api.domain.response.ObjectPlus;
import foro.guimero.api.repositories.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;}

    @Override
    public CourseShowData save(CourseRegisterData courseRegisterData) {
        Course course = new Course(courseRegisterData);
        this.courseRepository.save(course);
        return new CourseShowData(course);
    }

    @Override
    public Page<CourseShowData> findAll(boolean active, Pageable paging) {
        return this.courseRepository.findAllByActive(active, paging).map(CourseShowData::new);
    }

    @Override
    public CourseShowData findById(Long id) {
        return new CourseShowData(this.courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException()));
    }

    @Override
    public CourseShowData findByName(String name) {
        return new CourseShowData(this.courseRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException()));
    }

    @Override
    public CourseShowData update(CourseUpdateData courseUpdateData) {
            var course = this.courseRepository.findById(courseUpdateData.id())
                    .orElseThrow(() -> new EntityNotFoundException());
            if (course.isActive()){
                course.setName(courseUpdateData.name());
                this.courseRepository.save(course);
                return new CourseShowData(course);
            } return null;
    }

    @Override
    public ObjectPlus<Boolean> toggleCourse(Long id) {
        var result = new ObjectPlus<Boolean>();
        Course courseToToggle = this.courseRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException());
            courseToToggle.setActive(!courseToToggle.isActive());
            this.courseRepository.save(courseToToggle);
            result.setSuccess(true);
            result.setObject(courseToToggle.isActive());return result;
    }
}
