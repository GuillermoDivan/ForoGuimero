package foro.guimero.api.domain.course;

public record CourseShowData(Long id, String name, Boolean active) {

    public CourseShowData(Course course){
        this(course.getId(), course.getName(), course.isActive());
    }
}
