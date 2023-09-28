package foro.guimero.api.services.course;

import foro.guimero.api.domain.course.*;
import foro.guimero.api.domain.response.ObjectPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseShowData save(CourseRegisterData courseRegisterData);
    Page<CourseShowData> findAll(boolean active, Pageable paging);
    CourseShowData findById(Long id);
   CourseShowData findByName(String name);
    CourseShowData update(CourseUpdateData courseUpdateData);
    ObjectPlus<Boolean> toggleCourse (Long id);

}
